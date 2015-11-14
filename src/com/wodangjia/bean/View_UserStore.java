package com.wodangjia.bean;

import java.io.Serializable;

public class View_UserStore implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int user_id;
	private String user_phone;
	private String user_password;
	private String user_photo;
	private String user_nickname;
	private boolean user_gender;
	private String user_school;
	private int user_credit;
	private int user_status;
	private String tooken;
	private String user_create_time;
	private String user_last_login_time;
	private int store_id;
	private boolean is_certification;
	private int store_credit;
	private String store_name;
	private boolean store_delivery;
	private int store_status;
	private String store_creat_time;
	private int collections;
	private String store_img_path;
	public View_UserStore(int user_id, String user_phone, String user_password,
			String user_photo, String user_nickname, boolean user_gender,
			String user_school, int user_credit, int user_status,
			String tooken, String user_create_time,
			String user_last_login_time, int store_id,
			boolean is_certification, int store_credit, String store_name,
			boolean store_delivery, int store_status, String store_creat_time,
			int collections, String store_img_path) {
		super();
		this.user_id = user_id;
		this.user_phone = user_phone;
		this.user_password = user_password;
		this.user_photo = user_photo;
		this.user_nickname = user_nickname;
		this.user_gender = user_gender;
		this.user_school = user_school;
		this.user_credit = user_credit;
		this.user_status = user_status;
		this.tooken = tooken;
		this.user_create_time = user_create_time;
		this.user_last_login_time = user_last_login_time;
		this.store_id = store_id;
		this.is_certification = is_certification;
		this.store_credit = store_credit;
		this.store_name = store_name;
		this.store_delivery = store_delivery;
		this.store_status = store_status;
		this.store_creat_time = store_creat_time;
		this.collections = collections;
		this.store_img_path = store_img_path;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
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
	public boolean isUser_gender() {
		return user_gender;
	}
	public void setUser_gender(boolean user_gender) {
		this.user_gender = user_gender;
	}
	public String getUser_school() {
		return user_school;
	}
	public void setUser_school(String user_school) {
		this.user_school = user_school;
	}
	public int getUser_credit() {
		return user_credit;
	}
	public void setUser_credit(int user_credit) {
		this.user_credit = user_credit;
	}
	public int getUser_status() {
		return user_status;
	}
	public void setUser_status(int user_status) {
		this.user_status = user_status;
	}
	public String getTooken() {
		return tooken;
	}
	public void setTooken(String tooken) {
		this.tooken = tooken;
	}
	public String getUser_create_time() {
		return user_create_time;
	}
	public void setUser_create_time(String user_create_time) {
		this.user_create_time = user_create_time;
	}
	public String getUser_last_login_time() {
		return user_last_login_time;
	}
	public void setUser_last_login_time(String user_last_login_time) {
		this.user_last_login_time = user_last_login_time;
	}
	public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
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
	public int getCollections() {
		return collections;
	}
	public void setCollections(int collections) {
		this.collections = collections;
	}
	public String getStore_img_path() {
		return store_img_path;
	}
	public void setStore_img_path(String store_img_path) {
		this.store_img_path = store_img_path;
	}
	@Override
	public String toString() {
		return "View_UserStore [user_id=" + user_id + ", user_phone="
				+ user_phone + ", user_password=" + user_password
				+ ", user_photo=" + user_photo + ", user_nickname="
				+ user_nickname + ", user_gender=" + user_gender
				+ ", user_school=" + user_school + ", user_credit="
				+ user_credit + ", user_status=" + user_status + ", tooken="
				+ tooken + ", user_create_time=" + user_create_time
				+ ", user_last_login_time=" + user_last_login_time
				+ ", store_id=" + store_id + ", is_certification="
				+ is_certification + ", store_credit=" + store_credit
				+ ", store_name=" + store_name + ", store_delivery="
				+ store_delivery + ", store_status=" + store_status
				+ ", store_creat_time=" + store_creat_time + ", collections="
				+ collections + ", store_img_path=" + store_img_path + "]";
	}
	
	
}
