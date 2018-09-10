package br.unicamp.fe.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translator {

	private static final String ISO_8859_1 = "ISO-8859-1";
	private static final String UTF_8 = "UTF-8";

	public static void main(String[] args) throws IOException {
		String text = "Presente no Sindicato dos Metalúrgicos do ABC nos últimos dias de liberdade de Lula, o ex-ministro das Relações Exteriores avalia, em entrevista à TV 247, que considera ter havido influência externa na perseguição contra o maior líder político brasileiro dos últimos tempos; “Acredito que há muitos setores conservadores no Brasil que também não gostem do ex-presidente, mas acho razoável que tenha um dedo de Washington nessa condenação sim”, diz; sobre as provas usadas no processo, ele ressalta: \"isso não existe, isso mancha a credibilidade brasileira\"; assista";
		// Translated text: Hallo Welt!
		System.out.println("Translated text: " + new Translator().translate("pt", "en", text));
	}

	public String translate(String langFrom, String langTo, String text) throws IOException {
		String url1 = "https://script.google.com/macros/s/AKfycbzlY93co6aQPbN018io42wijN52kz7NtSzJUnLlqgEdVJTD2iA/exec";

		String urlStr = url1 + "?q=" + URLEncoder.encode(text, ISO_8859_1) + "&target=" + langTo + "&source="
				+ langFrom;
		URL url = new URL(urlStr);
		StringBuilder response = new StringBuilder();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Encoding", "gzip,deflate");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

}