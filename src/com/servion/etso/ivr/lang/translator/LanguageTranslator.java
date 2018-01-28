package com.servion.etso.ivr.lang.translator;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.translate.Translate.TranslateOption;

/**
 * @author santhoshkumar.m
 *
 */
public class LanguageTranslator {

	private String englishText;
	private String hindiText;

	@SuppressWarnings("deprecation")
	public String translateToEnglish(String speechResult) {
		System.out.println("SpeechResult---->" + speechResult);
		Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyBbz4ZE96V3LhAivN9F2CAkCK5gLOktUyA").build()
				.getService();
		Translation translation = translate.translate(speechResult, TranslateOption.sourceLanguage("hi"),
				TranslateOption.targetLanguage("en"));
		englishText = translation.getTranslatedText();
		System.out.println("TranslatedText---->" + englishText);
		return englishText;
	}

	@SuppressWarnings("deprecation")
	public String translateToHindi(String NLPResult) {
		if (NLPResult != null) {
			Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyBbz4ZE96V3LhAivN9F2CAkCK5gLOktUyA")
					.build().getService();
			Translation translation = translate.translate(NLPResult, TranslateOption.sourceLanguage("en"),
					TranslateOption.targetLanguage("hi"));
			hindiText = translation.getTranslatedText();
			System.out.println("TranslatedText---->" + hindiText);
		}
		return hindiText;
	}

}
