package br.unicamp.fe.thinksim.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Icon implements Serializable, EntityModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String img;

	public Icon(Long id, String img) {
		this.id = id;
		this.img = img;
	}

	public Icon() {

	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public static List<Icon> getIcons() {
		List<Icon> icons = new ArrayList<Icon>();
		for (int i = 0; i < 28; i++) {
			Icon icon = new Icon(new Long(i), "images/user" + i + ".png");
			icons.add(icon);
		}

		return icons;

	}

	public String getImg() {
		return this.img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "Icon [id=" + this.id + ", img=" + this.img + "]";
	}

}
