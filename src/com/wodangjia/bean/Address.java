package com.wodangjia.bean;

import java.io.Serializable;

public class Address implements Serializable{
	int id;
	int user_id;
	String name;
	String phone;
	String province;
	String city;
	String county;
	String detail;
	String create_time;
	boolean defaultt;
	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public boolean isDefaultt() {
		return defaultt;
	}

	public void setDefaultt(boolean isDefault) {
		this.defaultt = isDefault;
	}

	public Address(int user_id, String name, String phone, String province,
			String city, String county, String detail, String create_time) {
		super();
		this.user_id = user_id;
		this.name = name;
		this.phone = phone;
		this.province = province;
		this.city = city;
		this.county = county;
		this.detail = detail;
		this.create_time = create_time;
	}
	
	public Address(int id, int user_id, String name, String phone,
			String province, String city, String county, String detail,
			String create_time,boolean isDefault) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.name = name;
		this.phone = phone;
		this.province = province;
		this.city = city;
		this.county = county;
		this.detail = detail;
		this.create_time = create_time;
		this.defaultt=isDefault;
	}

	public Address(int id, int user_id, String name, String phone,
			String province, String city, String county, String detail
			) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.name = name;
		this.phone = phone;
		this.province = province;
		this.city = city;
		this.county = county;
		this.detail = detail;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String get_create_time() {
		return create_time;
	}
	public void set_create_time(String _create_time) {
		this.create_time = _create_time;
	}

	
}
