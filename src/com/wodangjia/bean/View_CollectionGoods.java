package com.wodangjia.bean;

import java.io.Serializable;

public class View_CollectionGoods implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int goods_id;
	int user_id;
	String collection_time;
	View_Goods item;
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
	public String getCollection_time() {
		return collection_time;
	}
	public void setCollection_time(String collection_time) {
		this.collection_time = collection_time;
	}
	public View_Goods getItem() {
		return item;
	}
	public void setItem(View_Goods item) {
		this.item = item;
	}
	public View_CollectionGoods(int goods_id, int user_id,
			String collection_time, View_Goods item) {
		super();
		this.goods_id = goods_id;
		this.user_id = user_id;
		this.collection_time = collection_time;
		this.item = item;
	}
	
}
