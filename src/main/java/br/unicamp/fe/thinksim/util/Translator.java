package br.unicamp.fe.thinksim.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;

public class Translator {

	public static void main(String[] args) throws Exception {

		Translator http = new Translator();
		String word = http.callUrlAndParseResult("en", "pt",
				"Eu nasci em um tempo em que a maioria dos jovens haviam perdido a crença em Deus, pela mesma razão que os seus maiores a haviam tido - sem saber porquê. E então, porque o espírito humano tende naturalmente para criticar porque sente, e não porque pensa, a maioria desses jovens escolheu a Humanidade para sucedâneo de Deus. Pertenço, porém, àquela espécie de homens que estão sempre na margem daquilo a que pertencem, nem vêem só a multidão de que são, senão também os grandes espaços que há ao lado. Por isso nem abandonei Deus tão amplamente como eles, nem aceitei nunca a Humanidade. Considerei que Deus, sendo improvável, poderia ser, podendo pois dever ser adorado; mas que a Humanidade, sendo uma mera ideia biológica, e não- significando mais que a espécie animal humana, não era mais digna de adoração do que qualquer outra espécie animal. Este culto da Humanidade, com seus ritos de Liberdade e Igualdade, pareceu-me sempre uma revivescência dos cultos antigos, em que animais eram como deuses, ou os deuses tinham cabeças de animais.\r\n"
						+ "Assim, não sabendo crer em Deus, e não podendo crer numa soma de animais, fiquei, como outros da orla das gentes, naquela distância de tudo a que comummente se chama a Decadência. A Decadência é a perda total da inconsciência; porque a inconsciência é o fundamento da vida. O coração, se pudesse pensar, pararia.\r\n"
						+ "A quem, como eu, assim, vivendo não sabe ter vida, que resta senão, como a meus poucos pares, a renúncia por modo e a contemplação por destino? Não sabendo o que é a vida religiosa, nem podendo sabê-lo, porque se não tem fé com a razão; não podendo ter fé na abstracção do homem, nem sabendo mesmo que fazer dela perante nós, ficava-nos, como motivo de ter alma, a contemplação estética da vida. E, assim, alheios à solenidade de todos os mundos, indiferentes ao divino e desprezadores do humano, entregamo-nos futilmente à sensação sem propósito, cultivada num epicurismo subtilizado, como convém aos nossos nervos cerebrais.\r\n"
						+ "Retendo, da ciência, somente aquele seu preceito central, de que tudo é sujeito às leis fatais, contra as quais se não reage independentemente, porque reagir é elas terem feito que reagíssemos; e verificando como esse preceito se ajusta ao outro, mais antigo, da divina fatalidade das coisas, abdicamos do esforço como os débeis do entre timento dos atletas, e curvamo-nos sobre o livro das sensações com um grande escrúpulo de erudição sentida.\r\n"
						+ "Não tomando nada a sério, nem considerando que nos fosse dada, por certa, outra realidade que não as nossas sensações, nelas nos abrigamos, e a elas exploramos como a grandes países desconhecidos. E, se nos empregamos assiduamente, não só na contemplação estética mas também na expressão dos seus modos e resultados, é que a prosa ou o verso que escrevemos, destituídos de vontade de querer convencer o alheio entendimento ou mover a alheia vontade, é apenas como o falar alto de quem lê, feito para dar plena objectividade ao prazer subjectivo da leitura.\r\n"
						+ "Sabemos bem que toda a obra tem que ser imperfeita, e que a menos segura das nossas contemplações estéticas será a daquilo que escrevemos. Mas imperfeito é tudo, nem há poente tão belo que o não pudesse ser mais, ou brisa leve que nos dê sono que não pudesse dar-nos um sono mais calmo ainda. E assim, contempladores iguais das montanhas e das estátuas, gozando os dias como os livros, sonhando tudo, sobretudo, para o converter na nossa íntima substância, faremos também descrições e análises, que, uma vez feitas, passarão a ser coisas alheias, que podemos gozar como se viessem na tarde.\r\n"
						+ "Não é este o conceito dos pessimistas, como aquele de Vigny, para quem a vida é uma cadeia, onde ele tecia palha para se distrair. Ser pessimista é tomar qualquer coisa como trágico, e essa atitude é um exagero e um incómodo. Não temos, é certo, um conceito de valia que apliquemos à obra que produzimos. Produzimo-la, é certo, para nos distrair, porém não como o preso que tece a palha, para se distrair do Destino, senão da menina que borda almofadas, para se distrair, sem mais nada.\r\n"
						+ "Considero a vida uma estalagem onde tenho que me demorar até que chegue a diligência do abismo. Não sei onde ela me levará, porque não sei nada. Poderia considerar esta estalagem uma prisão, porque estou compelido a aguardar nela; poderia considerá-la um lugar de sociáveis, porque aqui me encontro com outros. Não sou, porém, nem impaciente nem comum. Deixo ao que são os que se fecham no quarto, deitados moles na cama onde esperam sem sono; deixo ao ue fazem os que conversam nas salas, de onde as músicas e as vozes cegam cómodas até mim. Sento-me à porta e embebo meus olhos e ouvidos nas cores e nos sons da paisagem, e canto lento, para mim só, vagos cantos que componho enquanto espero.\r\n"
						+ "Para todos nós descerá a noite e chegará a diligência. Gozo a brisa que me dão e a alma que me deram para gozá-la, e não interrogo mais nem procuro. Se o que deixar escrito no livro dos viajantes puder, relido um dia por outros, entretê-los também na passagem, será bem. Se não o lerem, nem se entretiverem, será bem também.");

		System.out.println(word);
	}

	private String callUrlAndParseResult(String langFrom, String langTo, String word) throws Exception {

		String url = "https://translate.googleapis.com/translate_a/single?" + "client=gtx&" + "sl=" + langFrom + "&tl="
				+ langTo + "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		Thread.sleep(10000);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return this.parseResult(response.toString());
	}

	private String parseResult(String inputJson) throws Exception {
		/*
		 * inputJson for word 'hello' translated to language Hindi from English-
		 * [[["नमस्ते","hello",,,1]],,"en"] We have to get 'नमस्ते ' from this json.
		 */

		JSONArray jsonArray = new JSONArray(inputJson);
		JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
		JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);

		return jsonArray3.get(0).toString();
	}
}