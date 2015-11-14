package com.wodangjia.bean;

import java.io.Serializable;

public class Goods_Type implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int type_id;
	private String type_title ;
	private String type_pic ;
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public String getType_title() {
		return type_title;
	}
	public void setType_title(String type_title) {
		this.type_title = type_title;
	}
	public String getType_pic() {
		return type_pic;
	}
	public void setType_pic(String type_pic) {
		this.type_pic = type_pic;
	}
	public Goods_Type(int type_id, String type_title, String type_pic) {
		super();
		this.type_id = type_id;
		this.type_title = type_title;
		this.type_pic = type_pic;
	}
	
}
