package com.servion.etso.ivr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class SynthesiserV2 extends BaseSynthsiser {

	private static final String GOOGLE_SYNTHESISER_URL = "https://www.google.com/speech-api/v2/synthesize?enc=mpeg"
			+ "&client=chromium";

	private final String API_KEY;

	private String languageCode;

	private double pitch = 1;

	private double speed = 0.85;

	public SynthesiserV2(String API_KEY) {
		this.API_KEY = API_KEY;
	}

	public String getLanguage() {
		return languageCode;
	}

	public void setLanguage(String languageCode) {
		this.languageCode = languageCode;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public InputStream getMP3Data(String synthText) throws IOException {

		String languageCode = this.languageCode;

		if (languageCode == null || languageCode.equals("") || languageCode.equalsIgnoreCase("auto")) {
			try {
				languageCode = detectLanguage(synthText);
				if (languageCode == null) {
					languageCode = "en-us";
											
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				languageCode = "en-us";
			}
		}

		if (synthText.length() > 100) {
			List<String> fragments = parseString(synthText);
			String tmp = getLanguage();
			setLanguage(languageCode);
			InputStream out = getMP3Data(fragments);
			setLanguage(tmp);
								
			return out;
		}

		String encoded = URLEncoder.encode(synthText, "UTF-8"); 

		StringBuilder sb = new StringBuilder(GOOGLE_SYNTHESISER_URL);
		sb.append("&key=" + API_KEY);
		sb.append("&text=" + encoded);
		sb.append("&lang=" + languageCode);

		if (speed >= 0 && speed <= 2.0) {
			sb.append("&speed=" + speed / 2.0);
		}

		if (pitch >= 0 && pitch <= 2.0) {
			sb.append("&pitch=" + pitch / 2.0);
		}

		URL url = new URL(sb.toString());

		URLConnection urlConn = url.openConnection();

		urlConn.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) Gecko/20100101 Firefox/4.0");
		return urlConn.getInputStream();
	}
}