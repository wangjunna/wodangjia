package com.wodangjia.bean;

public class Certification {

	int id;
	int user_id;
	String relname;
	String id_card;
	String add_time;
	public Certification(int user_id, String relname, String id_card,
			String add_time) {
		super();
		this.user_id = user_id;
		this.relname = relname;
		this.id_card = id_card;
		this.add_time = add_time;
	}
	public Certification(int id, int user_id, String relname, String id_card,
			String add_time) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.relname = relname;
		this.id_card = id_card;
		this.add_time = add_time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getRelname() {
		return relname;
	}
	public void setRelname(String relname) {
		this.relname = relname;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	
}
