package com.wodangjia.bean;

import java.io.Serializable;

public class Store implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int store_id;
	private int user_id;
	private boolean is_certification;
	private int store_credit;
	private String store_name;
	private boolean store_delivery;
	private int store_status;
	private String store_creat_time;

	public Store() {
	}
	public Store(int store_id, int user_id, boolean is_certification,
			int store_credit, String store_name, boolean store_delivery,
			int store_status, String store_creat_time) {
		super();
		this.store_id = store_id;
		this.user_id = user_id;
		this.is_certification = is_certification;
		this.store_credit = store_credit;
		this.store_name = store_name;
		this.store_delivery = store_delivery;
		this.store_status = store_status;
		this.store_creat_time = store_creat_time;
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
	public boolean isIs_certification() {
		return is_certification;
	}
	public void setIs_certification(boolean is_certification) {
		this.is_certification = is_certification;
	}
	public int getStore_credit() {
		return store_credit;
	}
	public void setStore_credit(int store_credit) {
		this.store_credit = store_credit;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public boolean isStore_delivery() {
		return store_delivery;
	}
	public void setStore_delivery(boolean store_delivery) {
		this.store_delivery = store_delivery;
	}
	public int getStore_status() {
		return store_status;
	}
	public void setStore_status(int store_status) {
		this.store_status = store_status;
	}
	public String getStore_creat_time() {
		return store_creat_time;
	}
	public void setStore_creat_time(String store_creat_time) {
		this.store_creat_time = store_creat_time;
	}
	@Override
	public String toString() {
		return "Store [store_id=" + store_id + ", user_id=" + user_id
				+ ", is_certification=" + is_certification + ", store_credit="
				+ store_credit + ", store_name=" + store_name
				+ ", store_delivery=" + store_delivery + ", store_status="
				+ store_status + ", store_creat_time=" + store_creat_time + "]";
	}
	

}
