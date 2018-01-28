package com.servion.etso.ivr.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
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

/**
 * @author logesh.m
 *
 */
public class SpeechServlet extends HttpServlet {

	private static final long serialVersionUID = -8087350960751443366L;
	private String speechResult;
	private String englishText;
	private String NLPResult;
	private String hindiText;

	Thread thread;

	MessageResponse watsonResponse;
	LanguageTranslator translator = new LanguageTranslator();
	Map context = new HashMap();
	ConversationUtility convUtility = new ConversationUtility();
	private String callSId;
	private String dtmfResult;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		VoiceResponse twiml;
		String mobileNumber = (String)request.getSession().getAttribute("mnumber");
		if(mobileNumber == null){
			mobileNumber = request.getParameter("mnumber");
			request.getSession().setAttribute("", request.getParameter("mnumber"));
		}
		/*if(mobileNumber == null){
			mobileNumber = (String)request.getSession().getAttribute("mnumber");
		}*/
		if (request.getParameter("SpeechResult") != null) {
			speechResult = request.getParameter("SpeechResult");
			System.out.println(speechResult);
		}

		if (request.getParameter("Digits") != null) {
			dtmfResult = request.getParameter("Digits");
			System.out.println(dtmfResult);
		}

		callSId = request.getParameter("CallSid");

		if (speechResult != null) {

			if (speechResult != null) {
				englishText = translator.translateToEnglish(speechResult);
			}
			if (englishText != null) {
				System.out.println("mobile Number "+mobileNumber);
				NLPResult = convUtility.conversationUtil(context, englishText, mobileNumber);
			}
			if (NLPResult != null) {
				hindiText = translator.translateToHindi(NLPResult);
			}
			if (hindiText != null) {
				thread = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							TextToMP3.MP3Synthesizer(hindiText, callSId, convUtility.getIntent());
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
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (convUtility.getIntent() != null && !convUtility.getIntent().isEmpty()) {
				Play play = new Play.Builder(
						"https://bcf2ee76.ngrok.io/mp3/" + callSId + "_" + convUtility.getIntent() + ".mp3").build();
				Pause pause = new Pause.Builder().length(5).build();
				Play playNoInput = new Play.Builder(
						"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput.mp3").build();
				Play playend = new Play.Builder(
						"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput_close.mp3").build();
				Gather gather = new Gather.Builder().input(Input.SPEECH).speechTimeout("3").language(Language.HI_IN)
						.action("/ivr.hindi/ivr?mnumber=" + mobileNumber).play(play).pause(pause).play(playNoInput).pause(pause).play(playend).build();
				twiml = new VoiceResponse.Builder().gather(gather).build();
				
			} else {
				Play play = new Play.Builder("https://raw.githubusercontent.com/logipod/Chola/Audio/nomatch.mp3")
						.build();
				Pause pause = new Pause.Builder().length(5).build();
				Play playNoInput = new Play.Builder(
						"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput.mp3").build();
				Play playend = new Play.Builder(
						"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput_close.mp3").build();
				Gather gather = new Gather.Builder().input(Input.SPEECH).speechTimeout("3").language(Language.HI_IN)
						.action("/ivr.hindi/ivr?mnumber=" + mobileNumber).play(play).pause(pause).play(playNoInput).pause(pause).play(playend).build();
				twiml = new VoiceResponse.Builder().gather(gather).build();

			}
		} else {
			response.sendRedirect("/ivr.hindi/hindi");
			return;
		}

		response.setContentType("application/xml");
		try {
			response.getWriter().print(twiml.toXml());
		} catch (TwiMLException e) {
			e.printStackTrace();
		}
	}
}