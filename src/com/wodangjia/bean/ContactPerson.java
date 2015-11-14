package com.wodangjia.bean;

import java.io.Serializable;


public class ContactPerson implements Serializable{
	int user_id;
	String user_photo;
	String user_nickname;
	String remark_name;
	public ContactPerson(int user_id, String user_photo, String user_nickname,
			String remark_name) {
		super();
		this.user_id = user_id;
		this.user_photo = user_photo;
		this.user_nickname = user_nickname;
		this.remark_name = remark_name;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
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
	public String getRemark_name() {
		return remark_name;
	}
	public void setRemark_name(String remark_name) {
		this.remark_name = remark_name;
	}
	public String getShowName(){
		if(remark_name!=null&&remark_name.equals("")){
			return getUser_nickname();
		}else{
			return getRemark_name();
		}
	}
	

}
