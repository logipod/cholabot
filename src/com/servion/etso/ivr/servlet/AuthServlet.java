package com.servion.etso.ivr.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.servion.etso.ivr.TextToMP3;
import com.servion.etso.ivr.lang.translator.LanguageTranslator;
import com.servion.etso.ivr.watson.ConversationUtility;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Pause;
import com.twilio.twiml.voice.Gather.Input;
import com.twilio.twiml.voice.Gather.Language;
import com.twilio.twiml.voice.Play;

import co.sgsl.db.DbConstants;
import co.sgsl.db.DbManager;

/**
 * @author logesh.m
 *
 */
public class AuthServlet extends HttpServlet {

	private static final long serialVersionUID = -8087350960751443366L;
	Thread thread;

	MessageResponse watsonResponse;
	LanguageTranslator translator = new LanguageTranslator();
	ConversationUtility convUtility = new ConversationUtility();
	private String callSId;
	public String dtmfResult;
	private String PolicyHolderName;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		VoiceResponse twiml;
		request.getSession().setAttribute("PolicyHolderNumber", request.getParameter("Digits"));
		if (request.getParameter("Digits") != null) {
			dtmfResult = request.getParameter("Digits");
			System.out.println(dtmfResult);
		}

		callSId = request.getParameter("CallSid");
		PolicyHolderName = "";
		List<HashMap<String, String>> dataMap = DbManager.instance
				.fetchData(DbConstants.FETCH_NAME_MAIN.replace(DbConstants.MOBILENUMBER, dtmfResult));

		if (dataMap != null && !dataMap.isEmpty()) {
			PolicyHolderName = dataMap.get(0).get("PolicyHolder");

			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						TextToMP3.MP3SynthesizerForDB(PolicyHolderName, callSId, "y");
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
			try {
				thread.join();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Play play = new Play.Builder("https://bcf2ee76.ngrok.io/mp3/" + callSId + "firstHit.mp3").build();
			Pause pause = new Pause.Builder().length(5).build();
			Play playNoInput = new Play.Builder(
					"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput.mp3").build();
			Play playend = new Play.Builder(
					"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput_close.mp3").build();
			Gather gather = new Gather.Builder().input(Input.SPEECH).speechTimeout("3").language(Language.HI_IN)
					.action("/ivr.hindi/ivr?mnumber=" + dtmfResult).play(play).pause(pause).play(playNoInput).pause(pause).play(playend).build();
			twiml = new VoiceResponse.Builder().gather(gather).build();

			response.setContentType("application/xml");
			try {
				response.getWriter().print(twiml.toXml());
			} catch (TwiMLException e) {
				e.printStackTrace();
			}
		} else {
			PolicyHolderName = "I am sorry, mobile number enterned is not registered with us. Please try again with the registered number";

			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						TextToMP3.MP3SynthesizerForDB(PolicyHolderName, callSId, "n");
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
			try {
				thread.join();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Play play = new Play.Builder("https://bcf2ee76.ngrok.io/mp3/" + callSId + "firstHit.mp3").build();
			Pause pause = new Pause.Builder().length(5).build();
			Play playNoInput = new Play.Builder(
					"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput.mp3").build();
			Play playend = new Play.Builder(
					"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput_close.mp3").build();
			Gather gather = new Gather.Builder().input(Input.DTMF).timeout(3).language(Language.HI_IN)
					.action("/ivr.hindi/auth").play(play).pause(pause).play(playNoInput).pause(pause).play(playend).build();
			twiml = new VoiceResponse.Builder().gather(gather).build();

			response.setContentType("application/xml");
			try {
				response.getWriter().print(twiml.toXml());
			} catch (TwiMLException e) {
				e.printStackTrace();
			}
		}

		/*
		 * if (speechResult != null ) {
		 * 
		 * if (speechResult != null) { englishText =
		 * translator.translateToEnglish(speechResult); } if (englishText !=
		 * null) { NLPResult = convUtility.conversationUtil(context,
		 * englishText); } if (NLPResult != null) { hindiText =
		 * translator.translateToHindi(NLPResult); } if (hindiText != null) {
		 * thread = new Thread(new Runnable() {
		 * 
		 * @Override public void run() { try {
		 * TextToMP3.MP3Synthesizer(hindiText, callSId,
		 * convUtility.getIntent()); } catch (FileNotFoundException e) {
		 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		 * } }); thread.start(); try { thread.join(); Thread.sleep(5000); }
		 * catch (InterruptedException e) { e.printStackTrace(); } } if
		 * (convUtility.getIntent() != null &&
		 * !convUtility.getIntent().isEmpty()) { Play play = new Play.Builder(
		 * "https://bcf2ee76.ngrok.io/mp3/" + callSId + "_" +
		 * convUtility.getIntent() + ".mp3").build(); Play play2 = new
		 * Play.Builder(
		 * "https://raw.githubusercontent.com/logipod/Chola/Audio/anything_else.mp3")
		 * .build(); Gather gather = new
		 * Gather.Builder().input(Input.SPEECH).speechTimeout("3").language(
		 * Language.HI_IN) .action("/cholamandal/ivr").play(play).build(); twiml
		 * = new VoiceResponse.Builder().gather(gather).build();
		 * 
		 * } else if(firstHit){
		 * 
		 * if(PolicyHolderName != null){ firstHit = false;
		 * 
		 * }else {
		 * 
		 * } }else { Play play = new Play.Builder(
		 * "https://raw.githubusercontent.com/logipod/Chola/Audio/nomatch.mp3")
		 * .build(); Gather gather = new
		 * Gather.Builder().input(Input.SPEECH).speechTimeout("3").language(
		 * Language.HI_IN) .action("/cholamandal/ivr").play(play).build(); twiml
		 * = new VoiceResponse.Builder().gather(gather).build();
		 * 
		 * } } else { response.sendRedirect("/cholamandal/hindi"); return; }
		 * 
		 * response.setContentType("application/xml"); try {
		 * response.getWriter().print(twiml.toXml()); } catch (TwiMLException e)
		 * { e.printStackTrace(); }
		 */
	}
}