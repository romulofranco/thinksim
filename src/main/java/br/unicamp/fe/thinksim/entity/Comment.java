package br.unicamp.fe.thinksim.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "comment")
public class Comment implements Serializable, EntityModel {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String username;

	@Column(name = "text", length = 5000)
	private String text;
	private String color;
	private String icon;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateComment;

	public Comment() {

	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Post getPost() {
		return this.post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Date getDateComment() {
		return this.dateComment;
	}

	public void setDateComment(Date dateComment) {
		this.dateComment = dateComment;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "Comment [id=" + this.id + ", username=" + this.username + ", text=" + this.text + ", color="
				+ this.color + ", icon=" + this.icon + ", post=" + this.post + ", dateComment=" + this.dateComment
				+ "]";
	}

}
