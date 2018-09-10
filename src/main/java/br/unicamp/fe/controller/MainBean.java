package br.unicamp.fe.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Profile;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.ProfileOptions;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Trait;

import br.unicamp.fe.thinksim.dao.CommentDAO;
import br.unicamp.fe.thinksim.dao.PostDAO;
import br.unicamp.fe.thinksim.dao.UserDAO;
import br.unicamp.fe.thinksim.entity.Comment;
import br.unicamp.fe.thinksim.entity.EntityDataModel;
import br.unicamp.fe.thinksim.entity.Icon;
import br.unicamp.fe.thinksim.entity.Post;
import br.unicamp.fe.thinksim.entity.TraitThinkSim;
import br.unicamp.fe.thinksim.entity.User;
import br.unicamp.fe.thinksim.util.Util;

@ManagedBean
@SessionScoped
public class MainBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(MainBean.class);

	public static final String[] POSSIBLE_THEMES = { "afterdark", "afternoon", "afterwork", "aristo", "black-tie",
			"blitzer", "bluesky", "casablanca", "cruze", "cupertino", "dark-hive", "dot-luv", "eggplant", "excite-bike",
			"flick", "glass-x", "home", "hot-sneaks", "humanity", "le-frog", "midnight", "mint-choc", "overcast",
			"pepper-grinder", "redmond", "rocket", "sam", "smoothness", "south-street", "start", "sunny",
			"swanky-purse", "trontastic", "bootstrap", "ui-darkness", "ui-lightness", "vader" };

	private PersonalityInsights analiseSentimentalService;
	private List<Post> postList;
	private EntityDataModel<Post> postModel;
	private Post post;
	private List<String> chipList;
	private Comment comment;
	private boolean insert = false;

	private String filtraCategoria;
	private List<String> categoriaList;

	private EntityDataModel<Icon> icons;
	private Icon icon;
	private User user;
	private String theme;

	private String sentiment;
	private HorizontalBarChartModel resumoModel;
	private HorizontalBarChartModel opennessModel;
	private HorizontalBarChartModel conscientiousnessModel;
	private HorizontalBarChartModel extraversionModel;
	private HorizontalBarChartModel agreeablenessModel;
	private HorizontalBarChartModel emotionalRangeModel;
	private HorizontalBarChartModel needsModel;
	private HorizontalBarChartModel valuesModel;

	private String screenHeight;
	private boolean autoRefresh;

	@PostConstruct
	public void init() {
		this.post = null;
		this.loadList();

		this.resumoModel = new HorizontalBarChartModel();
		this.opennessModel = new HorizontalBarChartModel();
		this.conscientiousnessModel = new HorizontalBarChartModel();
		this.extraversionModel = new HorizontalBarChartModel();
		this.agreeablenessModel = new HorizontalBarChartModel();
		this.emotionalRangeModel = new HorizontalBarChartModel();

		this.needsModel = new HorizontalBarChartModel();
		this.valuesModel = new HorizontalBarChartModel();

		Util.setSentiments();

		this.icon = new Icon(new Long(0), "images/user0.png");

		this.initiliazeUser();

		this.theme = "cupertino";

		this.autoRefresh = true;
	}

	private void initiliazeUser() {
		this.user = new User();
		this.user.setIcon(this.icon.getImg());
		this.user.setTheme("cupertino");
		this.user.setUsername(null);
		this.user.setActive(User.ACTIVE_YES);
		this.user.setDateTime(new Date(System.currentTimeMillis()));
	}

	public void defineAutoRefresh() {
		logger.debug("AutoRefresh " + this.autoRefresh);
		this.loadList();
	}

	public void defineIcon(SelectEvent event) {
		logger.debug("Icon selected  " + this.icon);

	}

	public void confirmaLogin() {
		logger.debug("Username " + this.user.getUsername() + " icon: " + this.icon.getImg() + " Email: "
				+ this.user.getEmail());

		if (!this.isValidEmailAddress(this.user.getEmail())) {
			this.prepareMessage("E-mail inválido", FacesMessage.SEVERITY_WARN);
			return;
		}

		User userUsername = UserDAO.getInstance().getById(User.class, this.user.getUsername().trim());

		if (userUsername != null) {
			this.user = userUsername;
			this.prepareMessage("Bem vindo novamente " + this.user.getUsername() + ". Seu último login foi "
					+ this.user.getDateTime().toString(), FacesMessage.SEVERITY_INFO);
		} else {
			UserDAO.getInstance().insert(this.user);
			logger.debug("Usuario inserido na base de dados");
			this.prepareMessage("Seja bem vindo " + this.user.getUsername(), FacesMessage.SEVERITY_INFO);
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgLogin').hide()");

	}

	public void sair() {
		this.initiliazeUser();
		this.icon = new Icon();
	}

	private int lastSize = 0;

	public void loadList() {
		this.loadItems();

		this.postList = PostDAO.getInstance().getAllByCategoria(this.filtraCategoria);
		this.postModel = new EntityDataModel<Post>(this.postList);

		for (Post post : this.postList) {
			post.getComments().size();
		}

		if (this.lastSize < this.postList.size()) {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("tocarSom();");
		}

		this.lastSize = this.postList.size();
	}

	public void loadItems() {
		this.icons = new EntityDataModel<>(Icon.getIcons());
		this.categoriaList = PostDAO.getInstance().getCategory();
	}

	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	public void gerarAnaliseSentimentalGeral() {

		try {
			StringBuilder sb = new StringBuilder();
			for (Comment comm : this.post.getComments()) {
				sb.append(comm.getText()).append(" ");
			}

			sb.append(this.post.getText());

			this.gerarAnaliseSentimental(sb.toString());

		} catch (Exception e) {
			logger.error(e);
			this.prepareMessage("Ocorreu o seguinte erro ao gerar análise " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
		}
	}

	public void gerarAnaliseSentimentalComment() {
		logger.debug("Comment  " + this.comment);
		try {
			if ((this.comment.getText() == null) || this.comment.getText().isEmpty()) {
				this.prepareMessage("Não há texto suficiente para análise", FacesMessage.SEVERITY_WARN);
				return;
			}
			if (this.gerarAnaliseSentimental(this.comment.getText())) {
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlgAnalise').show();");

			}
		} catch (Exception e) {
			logger.error(e);
			this.prepareMessage("Ocorreu o seguinte erro ao gerar análise " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
		}
	}

	public void gerarAnaliseSentimentalPost() {
		logger.debug("Gerando... " + this.post.getText());
		try {
			if ((this.post.getText() == null) || this.post.getText().isEmpty()) {
				this.prepareMessage("Não há texto suficiente para análise", FacesMessage.SEVERITY_WARN);
				return;
			}

			if (this.gerarAnaliseSentimental(this.post.getText() + " " + this.post.getTitle())) {
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlgAnalise').show();");
			}

		} catch (Exception e) {
			logger.error(e);
			this.prepareMessage("Ocorreu o seguinte erro ao gerar análise " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
		}
	}

	public boolean gerarAnaliseSentimental(String texto) throws IOException {

		String text = this.adequaTexto(texto);
		String textoTraduzidoToEnglish = this.traduz(text);

		if (!textoTraduzidoToEnglish.isEmpty()) {
			text = textoTraduzidoToEnglish;
		}

		String[] numWords = text.split(" ");
		logger.debug("num. palavras: " + numWords.length);

		if (numWords.length < 5) {
			this.prepareMessage("Para análise sentimental é necessário um minimo de 20 palavras e sua postagem contêm "
					+ numWords.length + " palavras", FacesMessage.SEVERITY_INFO);
			return false;
		}

		if (numWords.length < 100) {
			while (numWords.length < 100) {
				numWords = text.split(" ");
				text = text + " " + text;
			}
		}

		logger.debug("Texto Traduzido " + text);

		this.analiseSentimentalService = new PersonalityInsights("2016-10-19");
		this.analiseSentimentalService.setUsernameAndPassword("a529329e-f393-43c5-b49a-893c1b47fcd2", "My8VBsyfWOhK");
		ProfileOptions options = new ProfileOptions.Builder().text(text).build();

		Profile profile = this.analiseSentimentalService.profile(options).execute();

		logger.debug(profile);

		List<TraitThinkSim> array = this.prepareTraitThinkSimList(profile.getPersonality());
		this.resumoModel = this.prepareChart("Resumo Big5", array);

		array = this.prepareTraitThinkSimList(profile.getPersonality().get(0).getChildren());
		this.opennessModel = this.prepareChart("Receptividade", array);

		array = this.prepareTraitThinkSimList(profile.getPersonality().get(1).getChildren());
		this.conscientiousnessModel = this.prepareChart("Conscienciosidade", array);

		array = this.prepareTraitThinkSimList(profile.getPersonality().get(2).getChildren());
		this.extraversionModel = this.prepareChart("Extroversão", array);

		array = this.prepareTraitThinkSimList(profile.getPersonality().get(3).getChildren());
		this.agreeablenessModel = this.prepareChart("Agradabilidade", array);

		array = this.prepareTraitThinkSimList(profile.getPersonality().get(4).getChildren());
		this.emotionalRangeModel = this.prepareChart("Range Emocional", array);

		array = this.prepareTraitThinkSimList(profile.getValues());
		this.valuesModel = this.prepareChart("Valores", array);

		array = this.prepareTraitThinkSimList(profile.getNeeds());
		this.needsModel = this.prepareChart("Necessidades", array);

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgAnalise').show();");

		return true;

	}

	private List<TraitThinkSim> prepareTraitThinkSimList(List<Trait> arrayWatson) {
		try {
			List<TraitThinkSim> arrayWatsonTraduzido = new ArrayList<TraitThinkSim>();

			for (int i = 0; i < arrayWatson.size(); i++) {
				Trait trait = arrayWatson.get(i);
				TraitThinkSim traitTS = new TraitThinkSim();
				traitTS.setTrait(trait);
				traitTS.setNome(Util.CARACTERES.get(trait.getName()));
				arrayWatsonTraduzido.add(traitTS);
			}
			return arrayWatsonTraduzido;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	private String traduz(String text) {
		try {
			String textoTraduzidoToEnglish = "";
			int offset = 1000;

			if (text.length() > offset) {
				for (int i = 0; i < text.length(); i = i + offset) {

					if (text.length() < (i + offset)) {
						offset = text.length();
					} else {
						offset = i + offset;
					}
					String texto = new Translator().translate("pt", "en", text.substring(i, offset));
					textoTraduzidoToEnglish = textoTraduzidoToEnglish + texto;
				}
			} else {
				textoTraduzidoToEnglish = new Translator().translate("pt", "en", text);
			}
			return textoTraduzidoToEnglish;
		} catch (IOException e) {
			logger.error(e);
		}

		return "";
	}

	private String adequaTexto(String texto) {
		String text = StringEscapeUtils.escapeHtml3(texto);
		text = StringEscapeUtils.escapeHtml4(texto);
		text = this.replaceAll(text, "&quot;", "\"");
		text = this.replaceAll(text, "&amp;", "&");
		text = this.replaceAll(text, "&rsquo;", "’");
		return text;
	}

	private String replaceAll(String source, String pattern, String replacement) {
		if (source == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int index;
		int patIndex = 0;
		while ((index = source.indexOf(pattern, patIndex)) != -1) {
			sb.append(source.substring(patIndex, index));
			sb.append(replacement);
			patIndex = index + pattern.length();
		}
		sb.append(source.substring(patIndex));
		return sb.toString();
	}

	public HorizontalBarChartModel prepareChart(String label, List<TraitThinkSim> traits) throws IOException {
		HorizontalBarChartModel chart = new HorizontalBarChartModel();
		ChartSeries cs = new ChartSeries();
		cs.setLabel(label);

		for (Object object : traits) {
			TraitThinkSim tr = (TraitThinkSim) object;
			int valor = (int) Math.round(tr.getTrait().getPercentile() * 100);
			Integer vlr = new Integer(valor);
			cs.set(new String(tr.getNome().getBytes(), "UTF-8"), vlr);
		}

		chart.addSeries(cs);

		chart.setTitle(label);
		chart.setLegendPosition(null);
		chart.setStacked(true);
		chart.setAnimate(true);
		chart.setShadow(true);
		chart.setSeriesColors("e60000");

		Axis xAxis = chart.getAxis(AxisType.X);
		xAxis.setLabel("Percentuais");
		xAxis.setMin(0);
		xAxis.setMax(100);

		Axis yAxis = chart.getAxis(AxisType.Y);
		yAxis.setLabel("Caracteristicas");
		yAxis.setTickAngle(-50);

		return chart;

	}

	public String convertList(List<String> list) {
		StringBuilder sb = new StringBuilder();
		if (list == null) {
			return "";
		}
		for (String item : list) {
			sb.append(item).append(",");
		}

		if (!list.isEmpty()) {
			sb.delete(sb.length() - 1, sb.length());
		}

		return sb.toString();
	}

	public List<String> convertStringToList(String items) {
		if (items == null) {
			return new ArrayList<String>();
		}

		if (items.isEmpty()) {
			return new ArrayList<String>();
		}

		String[] itemArray = items.split(",");
		List<String> itemList = new ArrayList<String>();
		for (String item : itemArray) {
			itemList.add(item);
		}

		return itemList;
	}

	public void insertPost() {
		logger.info("Post " + this.post);
		if (this.post != null) {

			if (this.post.getText() != null) {

				this.post.setText(this.post.getText().trim());
				this.post.setUsername(this.user.getUsername());
				this.post.setIcon(this.icon.getImg());

				if (this.post.getComments() == null) {
					this.post.setComments(new ArrayList<Comment>());
				}

				this.post.setChips(this.convertList(this.chipList));

				if (this.insert) {
					PostDAO.getInstance().insert(this.post);
				} else {
					PostDAO.getInstance().save(this.post);
				}

				this.loadList();

				this.prepareMessage("Postagem concluída", FacesMessage.SEVERITY_INFO);
			} else {
				this.prepareMessage("Informe um texto para a sua postagem", FacesMessage.SEVERITY_WARN);
			}
		}
	}

	public void liked(String type) {
		logger.debug("Post " + this.post + " - " + type);
		if (this.post != null) {
			if (type.startsWith(Post.LIKE)) {
				this.post.setLikes(this.post.getLikes() + 1);
			} else if (type.startsWith(Post.LOVED)) {
				this.post.setLoved(this.post.getLoved() + 1);
			} else if (type.startsWith(Post.FUNNY)) {
				this.post.setFunny(this.post.getFunny() + 1);
			} else if (type.startsWith(Post.ANGRY)) {
				this.post.setAngry(this.post.getAngry() + 1);
			} else if (type.startsWith(Post.VIEWS)) {
				this.post.setViews(this.post.getViews() + 1);
			}
			PostDAO.getInstance().save(this.post);
		}
	}

	public void remove() {

		if (this.post == null) {
			return;
		}

		PostDAO.getInstance().remove(this.post);
		this.prepareMessage("Postagem excluída", FacesMessage.SEVERITY_INFO);
		this.loadList();
		this.post = new Post();

		this.lastSize--;
	}

	public List<String> completeCategory(String query) {
		List<String> results = PostDAO.getInstance().getCategory(query);

		return results;
	}

	public void novaPostagem() {
		logger.debug("Nova postagem...");
		this.insert = true;
		this.post = new Post();
		this.post.setUsername(this.user.getUsername());
		this.post.setDatePost(new Date(System.currentTimeMillis()));
		this.post.setColor("cfc");
		this.chipList = new ArrayList<String>();
	}

	public void insertComment() {
		if (this.post != null) {
			if (this.comment != null) {
				if (this.comment.getId() == null) {
					this.comment.setId(Util.getNewID("comment"));
					this.comment.setIcon(this.user.getIcon());
					this.comment.setUsername(this.user.getUsername());
					this.comment.setPost(this.post);
					this.comment.setDateComment(new Date(System.currentTimeMillis()));
					this.post.getComments().add(this.comment);
					PostDAO.getInstance().save(this.post);
					this.prepareMessage("Comentário inserido na postagem - " + this.post.getTitle(),
							FacesMessage.SEVERITY_INFO);
				} else {
					CommentDAO.getInstance().updateComment(this.comment);
				}
				this.loadList();

				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("goComment('pnl" + this.post.getId() + "_wid')");
			} else {
				this.prepareMessage("Informe um texto para o seu comentário", FacesMessage.SEVERITY_WARN);
			}
		}
	}

	public void edit() {
		logger.info("Post1 " + this.post);
		this.post = PostDAO.getInstance().getById(Post.class, this.post.getId());

		logger.info("Post2 " + this.post);

		this.chipList = this.convertStringToList(this.post.getChips());
		this.insert = false;

	}

	private UploadedFile file;

	public void makeComment() {
		logger.debug("Selected post " + this.post);
		this.comment = new Comment();
	}

	public void editComment() {
		logger.debug("Comentario selecionado: " + this.comment);
		this.post = this.comment.getPost();
	}

	public void removeComment() {

		if (this.comment != null) {
			this.post.getComments().remove(this.comment);
			PostDAO.getInstance().save(this.post);
			CommentDAO.getInstance().delete(Comment.class, this.comment.getId());
			this.loadList();

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("goComment('pnl" + this.post.getId() + "_wid')");

		}
	}

	public static void main(String[] args) {
		PersonalityInsights service = new PersonalityInsights("2016-10-19");
		service.setUsernameAndPassword("a529329e-f393-43c5-b49a-893c1b47fcd2", "My8VBsyfWOhK");

		// Demo content from Moby Dick by Hermann Melville (Chapter 1)
		String text = "Call me Ishmael. Some years ago-never mind how long precisely-having "
				+ "little or no money in my purse, and nothing particular to interest me on shore, "
				+ "I thought I would sail about a little and see the watery part of the world. "
				+ "It is a way I have of driving off the spleen and regulating the circulation. "
				+ "Whenever I find myself growing grim about the mouth; whenever it is a damp, "
				+ "drizzly November in my soul; whenever I find myself involuntarily pausing before "
				+ "coffin warehouses, and bringing up the rear of every funeral I meet; and especially "
				+ "whenever my hypos get such an upper hand of me, that it requires a strong moral "
				+ "principle to prevent me from deliberately stepping into the street, and methodically "
				+ "knocking people's hats off-then, I account it high time to get to sea as soon as I can. "
				+ "This is my substitute for pistol and ball. With a philosophical flourish Cato throws himself "
				+ "upon his sword; I quietly take to the ship. There is nothing surprising in this. "
				+ "If they but knew it, almost all men in their degree, some time or other, cherish "
				+ "very nearly the same feelings towards the ocean with me. There now is your insular "
				+ "city of the Manhattoes, belted round by wharves as Indian isles by coral reefs-commerce surrounds "
				+ "it with her surf. Right and left, the streets take you waterward.";

		ProfileOptions options = new ProfileOptions.Builder().text(text).build();
		Profile profile = service.profile(options).execute();

		System.out.println(profile);

	}

	public void prepareMessage(String msg, Severity type) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(type, msg, ""));
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
	}

	public Post getPost() {
		return this.post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Comment getComment() {
		return this.comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public List<Post> getPostList() {
		return this.postList;
	}

	public void setPostList(List<Post> postList) {
		this.postList = postList;
	}

	public EntityDataModel<Post> getPostModel() {
		return this.postModel;
	}

	public void setPostModel(EntityDataModel<Post> postModel) {
		this.postModel = postModel;
	}

	public String getSentiment() {
		return this.sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public HorizontalBarChartModel getNeedsModel() {
		return this.needsModel;
	}

	public void setNeedsModel(HorizontalBarChartModel needsModel) {
		this.needsModel = needsModel;
	}

	public HorizontalBarChartModel getValuesModel() {
		return this.valuesModel;
	}

	public void setValuesModel(HorizontalBarChartModel valuesModel) {
		this.valuesModel = valuesModel;
	}

	public PersonalityInsights getAnaliseSentimentalService() {
		return this.analiseSentimentalService;
	}

	public void setAnaliseSentimentalService(PersonalityInsights analiseSentimentalService) {
		this.analiseSentimentalService = analiseSentimentalService;
	}

	public HorizontalBarChartModel getOpennessModel() {
		return this.opennessModel;
	}

	public void setOpennessModel(HorizontalBarChartModel opennessModel) {
		this.opennessModel = opennessModel;
	}

	public HorizontalBarChartModel getConscientiousnessModel() {
		return this.conscientiousnessModel;
	}

	public void setConscientiousnessModel(HorizontalBarChartModel conscientiousnessModel) {
		this.conscientiousnessModel = conscientiousnessModel;
	}

	public HorizontalBarChartModel getExtraversionModel() {
		return this.extraversionModel;
	}

	public void setExtraversionModel(HorizontalBarChartModel extraversionModel) {
		this.extraversionModel = extraversionModel;
	}

	public HorizontalBarChartModel getAgreeablenessModel() {
		return this.agreeablenessModel;
	}

	public void setAgreeablenessModel(HorizontalBarChartModel agreeablenessModel) {
		this.agreeablenessModel = agreeablenessModel;
	}

	public HorizontalBarChartModel getEmotionalRangeModel() {
		return this.emotionalRangeModel;
	}

	public void setEmotionalRangeModel(HorizontalBarChartModel emotionalRangeModel) {
		this.emotionalRangeModel = emotionalRangeModel;
	}

	public HorizontalBarChartModel getResumoModel() {
		return this.resumoModel;
	}

	public void setResumoModel(HorizontalBarChartModel resumoModel) {
		this.resumoModel = resumoModel;
	}

	public EntityDataModel<Icon> getIcons() {
		return this.icons;
	}

	public void setIcons(EntityDataModel<Icon> icons) {
		this.icons = icons;
	}

	public Icon getIcon() {
		return this.icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public String[] getThemes() {
		return (POSSIBLE_THEMES);
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getScreenHeight() {
		return this.screenHeight;
	}

	public void setScreenHeight(String screenHeight) {
		this.screenHeight = screenHeight;
	}

	public boolean isAutoRefresh() {
		return this.autoRefresh;
	}

	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	public String getTheme() {
		return this.theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public List<String> getChipList() {
		return this.chipList;
	}

	public void setChipList(List<String> chipList) {
		this.chipList = chipList;
	}

	public String getFiltraCategoria() {
		return this.filtraCategoria;
	}

	public void setFiltraCategoria(String filtraCategoria) {
		this.filtraCategoria = filtraCategoria;
	}

	public List<String> getCategoriaList() {
		return this.categoriaList;
	}

	public void setCategoriaList(List<String> categoriaList) {
		this.categoriaList = categoriaList;
	}

}
