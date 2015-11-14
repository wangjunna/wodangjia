package com.wodangjia.bean;

import java.io.Serializable;

public class User implements Serializable{
	
	private int user_id;
	private String user_phone;
	private String user_password;
	private String user_photo;
	private String user_nickname;
	private boolean user_gender;
	private String user_school;
	private int user_credit;
	private int user_status;
	private String user_create_time;
	private String user_last_login_time;
	private Store store;
	public User(int user_id, String user_phone, String user_password,
			String user_photo, String user_nickname, boolean user_gender,
			String user_school, int user_credit, int user_status,
			String user_create_time, String user_last_login_time) {
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
		this.user_create_time = user_create_time;
		this.user_last_login_time = user_last_login_time;
	}
	public User(String user_phone, String user_password) {
		super();
		this.user_phone = user_phone;
		this.user_password = user_password;
	}
	public User(String user_phone2, String user_password2, String user_photo2,
			String user_nickname2, boolean user_gender2, String user_school2,
			int user_credit2, int user_statu, String user_create_time2,
			String user_last_login_time2) {
		// TODO Auto-generated constructor stub
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
	public String getuser_nickname() {
		return user_nickname;
	}
	public void setuser_nickname(String user_nickname) {
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
	
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_phone=" + user_phone
				+ ", user_password=" + user_password + ", user_photo="
				+ user_photo + ", user_nickname=" + user_nickname
				+ ", user_gender=" + user_gender + ", user_school="
				+ user_school + ", user_credit=" + user_credit
				+ ", user_status=" + user_status + ", user_create_time="
				+ user_create_time + ", user_last_login_time="
				+ user_last_login_time + "]";
	}
	
	
}
