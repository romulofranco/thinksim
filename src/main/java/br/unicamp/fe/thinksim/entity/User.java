package br.unicamp.fe.thinksim.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String ACTIVE_YES = "1";
	public static final String ACTIVE_NO = "0";

	public static final String MUDAR_SENHA_SIM = "s";
	public static final String MUDAR_SENHA_NAO = "n";

	@Id
	private String username;

	private String email = "";

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_time")
	private java.util.Date dateTime;

	@Column
	private String active = User.ACTIVE_YES;

	@Column(name = "theme")
	private String theme = "cupertino";

	@Column(name = "icon")
	private String icon;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public java.util.Date getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(java.util.Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTheme() {
		return this.theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
