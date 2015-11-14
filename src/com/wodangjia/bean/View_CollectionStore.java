package com.wodangjia.bean;

public class View_CollectionStore {
	int store_id;
	int user_id;
	String collection_time;
	String store_name;
	int status;
	int store_credit;
	String store_img_path;
	int collections;
	public View_CollectionStore(int store_id, int user_id,
			String collection_time, String store_name, int status,
			int store_credit, String store_img_path,int collections) {
		super();
		this.store_id = store_id;
		this.user_id = user_id;
		this.collection_time = collection_time;
		this.store_name = store_name;
		this.status = status;
		this.store_credit = store_credit;
		this.store_img_path = store_img_path;
		this.collections=collections;
	}
	public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
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
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public int getCollections() {
		return collections;
	}
	public void setCollections(int collections) {
		this.collections = collections;
	}
	
}
