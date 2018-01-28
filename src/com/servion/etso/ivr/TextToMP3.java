package com.servion.etso.ivr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.servion.etso.ivr.lang.translator.LanguageTranslator;

/**
 * @author logesh.m
 *
 */

public class TextToMP3 {

	static SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
	
	static LanguageTranslator translator = new LanguageTranslator();

	public static void MP3Synthesizer(String hindiText, String A, String B) throws IOException, FileNotFoundException {
		download(hindiText,A,B);
	}

	public static void download(String text,String A, String B) {
		System.out.println(text);

		Thread thread = new Thread(() -> {
			try {

				InputStream is = synthesizer.getMP3Data(text);
				
				OutputStream outstream = new FileOutputStream(new File("E:\\ETSO\\apache-tomcat-6.0.43-windows-x64\\apache-tomcat-6.0.43\\webapps\\mp3\\"+A+"_"+B+".mp3"));
				byte[] buffer = new byte[4096];
				int len;
				while ((len = is.read(buffer)) > 0) {
					outstream.write(buffer, 0, len);
				}
				outstream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		thread.setDaemon(false);
		thread.start();
	}
	
	public static void MP3SynthesizerForDB(String hindiText,String A ,String B) throws IOException, FileNotFoundException {
		downloadForDB(hindiText, A, B);
	}

	public static void downloadForDB(String text, String A, String B) {
		System.out.println(text+ A);
		Thread thread = new Thread(() -> {
			try {
				String firstHitText ="";
				if(B.equalsIgnoreCase("y")){
					firstHitText = "Hello "+text+", How may I help you today"; 
				}else{
					firstHitText = text;
				}
				String hindiName = translator.translateToHindi(firstHitText);
				InputStream is = synthesizer.getMP3Data(hindiName);
				
				OutputStream outstream = new FileOutputStream(new File("E:\\ETSO\\apache-tomcat-6.0.43-windows-x64\\apache-tomcat-6.0.43\\webapps\\mp3\\"+A+"firstHit.mp3"));
				byte[] buffer = new byte[4096];
				int len;
				while ((len = is.read(buffer)) > 0) {
					outstream.write(buffer, 0, len);
				}
				outstream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		thread.setDaemon(false);
		thread.start();
	}
	
	public static void download(String text) {
			try {

				InputStream is = synthesizer.getMP3Data(text);
				
				OutputStream outstream = new FileOutputStream(new File("E:\\ETSO\\output\\noinput_close.mp3"));
				byte[] buffer = new byte[4096];
				int len;
				while ((len = is.read(buffer)) > 0) {
					outstream.write(buffer, 0, len);
				}
				outstream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	/*public static void main(String []args){
		download("సాధారణ భీమా చోళమండలం స్వాగతం");
	}*/

}
