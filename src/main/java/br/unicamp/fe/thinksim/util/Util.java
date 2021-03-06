package br.unicamp.fe.thinksim.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class Util {

	public static Long lastID = new Long(0);
	private final static Logger logger = Logger.getLogger(Util.class);

	public static final Map<String, String> CARACTERES = new HashMap<String, String>();

	public static void setSentiments() {
		CARACTERES.put("Openness", "Abertura");
		CARACTERES.put("Adventurousness", "Aventureira");
		CARACTERES.put("Artistic interests", "Interesses artisticos");
		CARACTERES.put("Emotionality", "Emotividade");
		CARACTERES.put("Imagination", "Imaginacao");
		CARACTERES.put("Intellect", "Intelecto");
		CARACTERES.put("Authority-challenging", "Autoridade desafiadora");
		CARACTERES.put("Conscientiousness", "conscienciosidade");
		CARACTERES.put("Achievement striving", "Realizacao de conquistas");
		CARACTERES.put("Cautiousness", "Cautela");
		CARACTERES.put("Dutifulness", "Obediencia");
		CARACTERES.put("Orderliness", "Ordem");
		CARACTERES.put("Self-discipline", "Autodisciplina");
		CARACTERES.put("Self-efficacy", "Auto-eficacia");
		CARACTERES.put("Extraversion", "Extroversao");
		CARACTERES.put("Activity level", "Nivel de atividade");
		CARACTERES.put("Assertiveness", "Assertividade");
		CARACTERES.put("Cheerfulness", "Alegria");
		CARACTERES.put("Excitement-seeking", "Procura de excitacao");
		CARACTERES.put("Outgoing", "Extrovertido");
		CARACTERES.put("Gregariousness", "Socializacao");
		CARACTERES.put("Agreeableness", "Agradabilidade");
		CARACTERES.put("Altruism", "Altruismo");
		CARACTERES.put("Cooperation", "Cooperacao");
		CARACTERES.put("Modesty", "Modestia");
		CARACTERES.put("Uncompromising", "Intransigente");
		CARACTERES.put("Sympathy", "Simpatia");
		CARACTERES.put("Trust", "Confiar em");
		CARACTERES.put("Emotional range", "Alcance emocional");
		CARACTERES.put("Fiery", "Impetuoso");
		CARACTERES.put("Prone to worry", "Propenso a se preocupar");
		CARACTERES.put("Melancholy", "Melancolico");
		CARACTERES.put("Immoderation", "Imoderacao");
		CARACTERES.put("Self-consciousness", "Autoconsciencia");
		CARACTERES.put("Susceptible to stress", "Suscetivel ao estresse");
		CARACTERES.put("Needs", "Necessidades");
		CARACTERES.put("Challenge", "Desafio");
		CARACTERES.put("Closeness", "Proximidade");
		CARACTERES.put("Curiosity", "Curiosidade");
		CARACTERES.put("Excitement", "Excitacao");
		CARACTERES.put("Harmony", "Harmonia");
		CARACTERES.put("Ideal", "Ideal");
		CARACTERES.put("Liberty", "Liberdade");
		CARACTERES.put("Love", "Ame");
		CARACTERES.put("Practicality", "Praticidade");
		CARACTERES.put("Self-expression", "Auto-expressao");
		CARACTERES.put("Stability", "Estabilidade");
		CARACTERES.put("Structure", "Estrutura");
		CARACTERES.put("Values", "Valores");
		CARACTERES.put("Conservation", "Conservacao");
		CARACTERES.put("Openness to change", "Abertura para mudar");
		CARACTERES.put("Hedonism", "Hedonismo");
		CARACTERES.put("Self-enhancement", "Auto-aperfeicoamento");
		CARACTERES.put("Self-transcendence", "Autotranscendencia");
	}

	public static Long getNewID(String tbl) {

		try {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");

			Long id = Long.parseLong(sdf.format(date));

			id++;

			if (lastID == id) {
				id++;
				lastID = id;
			} else {
				lastID = id;
			}
			logger.debug(tbl + "- NEW ID: " + id);

			return id;
		} catch (Exception e) {

			logger.error(e);
		}
		return new Long(-1);
	}

	public static void espere(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
