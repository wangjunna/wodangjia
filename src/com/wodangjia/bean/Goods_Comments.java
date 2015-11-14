package com.wodangjia.bean;

import java.io.Serializable;

public class Goods_Comments implements Serializable{
	private int comment_id;
	private int goods_id;
	private int user_id;
	private String content;
	private String comment_time;
	private double star;
	
	public Goods_Comments(int goods_id, int user_id, String content,
			String comment_time, double star) {
		super();
		this.goods_id = goods_id;
		this.user_id = user_id;
		this.content = content;
		this.comment_time = comment_time;
		this.star = star;
	}
	public Goods_Comments(int comment_id, int goods_id, int user_id,
			String content, String comment_time, double star) {
		super();
		this.comment_id = comment_id;
		this.goods_id = goods_id;
		this.user_id = user_id;
		this.content = content;
		this.comment_time = comment_time;
		this.star = star;
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
	
	
}
