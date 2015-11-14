package com.wodangjia.bean;

import java.io.Serializable;


public class View_Store implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int store_id;
	public View_Store(int store_id, int user_id, int store_credit,
			String store_img_path, String store_name, int store_status,
			int collections) {
		super();
		this.store_id = store_id;
		this.user_id = user_id;
		this.store_credit = store_credit;
		this.store_img_path = store_img_path;
		this.store_name = store_name;
		this.store_status = store_status;
		this.collections = collections;
	}
	
	int user_id;
	int store_credit;
	String store_img_path;
	String store_name;
	int store_status;
	int collections;
	public View_Store(int user_id, int store_credit, String store_img_path,
			String store_name, int store_status, int collections) {
		super();
		this.user_id = user_id;
		this.store_credit = store_credit;
		this.store_img_path = store_img_path;
		this.store_name = store_name;
		this.store_status = store_status;
		this.collections = collections;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getStore_credit() {
		return store_credit;
	}
	public void setStore_credit(int store_credit) {
		this.store_credit = store_credit;
	}
	public String getStore_img_path() {
		return store_img_path;
	}
	public void setStore_img_path(String store_img_path) {
		this.store_img_path = store_img_path;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public int getStore_status() {
		return store_status;
	}
	public void setStore_status(int store_status) {
		this.store_status = store_status;
	}
	public int getCollections() {
		return collections;
	}
	public void setCollections(int collections) {
		this.collections = collections;
	}
	@Override
	public String toString() {
		return "View_Store [store_id=" + store_id + ", user_id=" + user_id
				+ ", store_credit=" + store_credit + ", store_img_path="
				+ store_img_path + ", store_name=" + store_name
				+ ", store_status=" + store_status + ", collections="
				+ collections + "]";
	}
	public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	
}
