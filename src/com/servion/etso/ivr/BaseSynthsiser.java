package com.servion.etso.ivr;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class BaseSynthsiser {

    
    public abstract InputStream getMP3Data(String synthText) throws IOException;

   
    public InputStream getMP3Data(List<String> synthText) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(1000);
        Set<Future<InputStream>> set = new LinkedHashSet<Future<InputStream>>(synthText.size());
        for(String part: synthText){ 
            Callable<InputStream> callable = new MP3DataFetcher(part);
            Future<InputStream> future = pool.submit(callable);
            set.add(future);
        }
        List<InputStream> inputStreams = new ArrayList<InputStream>(set.size());
        for(Future<InputStream> future: set){
            try {
                inputStreams.add(future.get());
            } catch (ExecutionException e) {
                Throwable ex = e.getCause();
                if(ex instanceof IOException){
                    throw (IOException)ex;
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        return new SequenceInputStream(Collections.enumeration(inputStreams));
    }

    protected List<String> parseString(String input){
        return parseString (input, new ArrayList<String>());
    }

    private List<String> parseString(String input, List<String> fragments){
        if(input.length()<=100){
            fragments.add(input);
            return fragments;
        }
        else{
            int lastWord = findLastWord(input);
            if(lastWord<=0){
                fragments.add(input.substring(0,100));
                return parseString(input.substring(100), fragments);
            }else{
                fragments.add(input.substring(0,lastWord));
                return parseString(input.substring(lastWord), fragments);
            }
        }
    }

    private int findLastWord(String input){
        if(input.length()<100)
            return input.length();
        int space = -1;
        for(int i = 99; i>0; i--){
            char tmp = input.charAt(i);
            if(isEndingPunctuation(tmp)){
                return i+1;
            }
            if(space==-1 && tmp == ' '){
                space = i;
            }
        }
        if(space>0){
            return space;
        }
        return -1;
    }

    private boolean isEndingPunctuation(char input){
        return input == '.' || input == '!' || input == '?' || input == ';' || input == ':' || input == '|';
    }

    public String detectLanguage(String text) throws IOException{
        return GoogleTranslate.detectLanguage(text);
    }

    private class MP3DataFetcher implements Callable<InputStream>{
        private String synthText;

        public MP3DataFetcher(String synthText){
            this.synthText = synthText;
        }

        public InputStream call() throws IOException{
            return getMP3Data(synthText);
        }
    }
}