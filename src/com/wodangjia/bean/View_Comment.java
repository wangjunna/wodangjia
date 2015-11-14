package com.wodangjia.bean;

import java.io.Serializable;

import android.R.string;

public class View_Comment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int comment_id;
	private int goods_id;
	private int user_id;
	private String content;
	private String comment_time;
	private double star;
	private String user_nickname;
	private String user_photo;

	public View_Comment(int comment_id, int goods_id, int user_id,
			String content, String comment_time, double star,String user_photo,String user_nickname) {
		super();
		this.comment_id = comment_id;
		this.goods_id = goods_id;
		this.user_id = user_id;
		this.content = content;
		this.comment_time = comment_time;
		this.star = star;
		this.user_photo=user_photo;
		this.user_nickname=user_nickname;
	}

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

	public double getStar() {
		return star;
	}

	public void setStar(double star) {
		this.star = star;
	}

	public String getUser_photo() {
		return user_photo;
	}

	public void setUser_photo(String user_photo) {
		this.user_photo = user_photo;
	}

	public String getUser_nickname() {
		return user_nickname;
	}

	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}

}
