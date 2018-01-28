package com.servion.etso.ivr;

import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Gather.Input;
import com.twilio.twiml.voice.Gather.Language;
import com.twilio.twiml.voice.Play;

/**
 * @author logesh.m
 *
 */
public class TwilioClass {


	public static void main(String []args) {

		VoiceResponse twiml;
			Play play = new Play.Builder("https://raw.githubusercontent.com/logipod/router/master/targetFile.wav").build();
			twiml = new VoiceResponse.Builder().play(play).build();

		try {
			System.out.println(twiml.toXml());
		} catch (TwiMLException e) {
			e.printStackTrace();
		}
	}
}