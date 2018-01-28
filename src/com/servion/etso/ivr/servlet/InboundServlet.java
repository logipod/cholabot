package com.servion.etso.ivr.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*import com.twilio.twiml.Gather;
import com.twilio.twiml.Language;
import com.twilio.twiml.Say;*/
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Gather.Input;
import com.twilio.twiml.voice.Gather.Language;
import com.twilio.twiml.voice.Pause;
import com.twilio.twiml.voice.Play;

/**
 * @author logesh.m
 *
 */
public class InboundServlet extends HttpServlet {

	private static final long serialVersionUID = -5314738993304316034L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Play play = new Play.Builder("https://raw.githubusercontent.com/logipod/Chola/Audio/Welcome2.mp3").build();
		Pause pause = new Pause.Builder().length(5).build();
		Play playNoInput = new Play.Builder(
				"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput.mp3").build();
		Play playend = new Play.Builder(
				"https://raw.githubusercontent.com/logipod/Chola/Audio/noinput_close.mp3").build();
		Gather gather = new Gather.Builder().input(Input.DTMF).timeout(3).language(Language.HI_IN).action("/ivr.hindi/auth")
				.play(play).pause(pause).play(playNoInput).pause(pause).play(playend).build();
		VoiceResponse twiml = new VoiceResponse.Builder().gather(gather).build();

		response.setContentType("application/xml");
		
		try {
			response.getWriter().print(twiml.toXml());
		} catch (TwiMLException e) {
			e.printStackTrace();
		}
	}
}