package br.unicamp.fe.thinksim.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.unicamp.fe.thinksim.util.Util;

@Entity
@Table(name = "post")
public class Post implements Serializable, EntityModel {

	private static final long serialVersionUID = 1L;

	public static final String LIKE = "L";
	public static final String LOVED = "D";
	public static final String ANGRY = "A";
	public static final String FUNNY = "F";
	public static final String VIEWS = "V";

	@Id
	private Long id;
	private String username;
	private String icon;

	@Column(name = "text", length = 5000)
	private String text;

	private String title;
	private int likes;
	private int angry;
	private int loved;
	private int funny;
	private int views;
	private int sharing;
	private boolean chartOK;
	private String postAnalise;

	private String color;
	private String category;
	private String chips;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "post")
	private List<Comment> comments;

	@Temporal(TemporalType.TIMESTAMP)
	private Date datePost;

	@Transient
	private String shortText;

	public Post() {
		this.id = Util.getNewID("post");
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLikes() {
		return this.likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getAngry() {
		return this.angry;
	}

	public void setAngry(int angry) {
		this.angry = angry;
	}

	public int getLoved() {
		return this.loved;
	}

	public void setLoved(int loved) {
		this.loved = loved;
	}

	public int getFunny() {
		return this.funny;
	}

	public void setFunny(int funny) {
		this.funny = funny;
	}

	public int getViews() {
		return this.views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getSharing() {
		return this.sharing;
	}

	public void setSharing(int sharing) {
		this.sharing = sharing;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Date getDatePost() {
		return this.datePost;
	}

	public void setDatePost(Date datePost) {
		this.datePost = datePost;
	}

	public String getShortText() {
		if (this.text.length() > 450) {
			return this.getText().substring(0, 448);
		}
		return this.text;
	}

	@Override
	public String toString() {
		return "Post [id=" + this.id + ", username=" + this.username + ", icon=" + this.icon + ", text=" + this.text
				+ ", title=" + this.title + ", likes=" + this.likes + ", angry=" + this.angry + ", loved=" + this.loved
				+ ", funny=" + this.funny + ", views=" + this.views + ", sharing=" + this.sharing + ", chartOK="
				+ this.chartOK + ", postAnalise=" + this.postAnalise + ", color=" + this.color + ", category="
				+ this.category + ", chips=" + this.chips + ", comments=" + this.comments + ", datePost="
				+ this.datePost + ", shortText=" + this.shortText + "]";
	}

	public boolean isChartOK() {
		return this.chartOK;
	}

	public void setChartOK(boolean chartOK) {
		this.chartOK = chartOK;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPostAnalise() {
		return this.postAnalise;
	}

	public void setPostAnalise(String postAnalise) {
		this.postAnalise = postAnalise;
	}

	public String getColor() {
		if (this.color == null) {
			return "cfc";
		}
		if (this.color.isEmpty()) {
			return "cfc";
		}
		return this.color;
	}

	public String getColorHit() {
		if (this.color == null) {
			return "#cfc";
		}
		if (this.color.isEmpty()) {
			return "#cfc";
		}
		return "#" + this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getChips() {
		return this.chips;
	}

	public void setChips(String chips) {
		this.chips = chips;
	}

}