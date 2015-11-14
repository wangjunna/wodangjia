package com.wodangjia.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Orders implements Serializable{
	int user_id;
	int order_id;
	String order_no;
	int order_status;
	String order_create_time;
	String order_pay_time;
	String order_send_time;
	String order_complete_time;
	double order_value;
	String order_remark;
	String order_address_name;
	String order_address_phone;
	String order_address_province;
	String order_address_city;
	String order_address_county;
	String order_address_detail;
	String order_express;
	String order_express_no;
	ArrayList<View_ShoppingCartItem> items;
	
	public ArrayList<View_ShoppingCartItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<View_ShoppingCartItem> items) {
		this.items = items;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public int getOrder_status() {
		return order_status;
	}
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}
	public String getOrder_create_time() {
		return order_create_time;
	}
	public void setOrder_create_time(String order_create_time) {
		this.order_create_time = order_create_time;
	}
	public String getOrder_pay_time() {
		return order_pay_time;
	}
	public void setOrder_pay_time(String order_pay_time) {
		this.order_pay_time = order_pay_time;
	}
	public String getOrder_send_time() {
		return order_send_time;
	}
	public void setOrder_send_time(String order_send_time) {
		this.order_send_time = order_send_time;
	}
	public String getOrder_complete_time() {
		return order_complete_time;
	}
	public void setOrder_complete_time(String order_complete_time) {
		this.order_complete_time = order_complete_time;
	}
	public double getOrder_value() {
		return order_value;
	}
	public String getOrder_remark() {
		return order_remark;
	}
	public void setOrder_remark(String order_remark) {
		this.order_remark = order_remark;
	}
	public String getOrder_address_name() {
		return order_address_name;
	}
	public void setOrder_address_name(String order_address_name) {
		this.order_address_name = order_address_name;
	}
	public String getOrder_address_phone() {
		return order_address_phone;
	}
	public void setOrder_address_phone(String order_address_phone) {
		this.order_address_phone = order_address_phone;
	}
	public String getOrder_address_province() {
		return order_address_province;
	}
	public void setOrder_address_province(String order_address_province) {
		this.order_address_province = order_address_province;
	}
	public String getOrder_address_city() {
		return order_address_city;
	}
	public void setOrder_address_city(String order_address_city) {
		this.order_address_city = order_address_city;
	}
	public String getOrder_address_county() {
		return order_address_county;
	}
	public void setOrder_address_county(String order_address_county) {
		this.order_address_county = order_address_county;
	}
	public String getOrder_address_detail() {
		return order_address_detail;
	}
	public void setOrder_address_detail(String order_address_detail) {
		this.order_address_detail = order_address_detail;
	}
	public String getOrder_express() {
		return order_express;
	}
	public void setOrder_express(String order_express) {
		this.order_express = order_express;
	}
	public String getOrder_express_no() {
		return order_express_no;
	}
	public void setOrder_express_no(String order_express_no) {
		this.order_express_no = order_express_no;
	}
	public Orders(int user_id, String order_no, int order_status,
			String order_create_time, String order_pay_time,
			String order_send_time, String order_complete_time,
			double order_value, String order_remark, String order_address_name,
			String order_address_phone, String order_address_province,
			String order_address_city, String order_address_county,
			String order_address_detail, String order_express,
			String order_express_no) {
		super();
		this.user_id = user_id;
		this.order_no = order_no;
		this.order_status = order_status;
		this.order_create_time = order_create_time;
		this.order_pay_time = order_pay_time;
		this.order_send_time = order_send_time;
		this.order_complete_time = order_complete_time;
		this.order_value = order_value;
		this.order_remark = order_remark;
		this.order_address_name = order_address_name;
		this.order_address_phone = order_address_phone;
		this.order_address_province = order_address_province;
		this.order_address_city = order_address_city;
		this.order_address_county = order_address_county;
		this.order_address_detail = order_address_detail;
		this.order_express = order_express;
		this.order_express_no = order_express_no;
	}
	public Orders(int order_id,int user_id, String order_no, int order_status,
			String order_create_time, double order_value, String order_remark,
			String order_address_name, String order_address_phone,
			String order_address_province, String order_address_city,
			String order_address_county, String order_address_detail) {
		super();
		this.order_id=order_id;
		this.user_id = user_id;
		this.order_no = order_no;
		this.order_status = order_status;
		this.order_create_time = order_create_time;
		this.order_value = order_value;
		this.order_remark = order_remark;
		this.order_address_name = order_address_name;
		this.order_address_phone = order_address_phone;
		this.order_address_province = order_address_province;
		this.order_address_city = order_address_city;
		this.order_address_county = order_address_county;
		this.order_address_detail = order_address_detail;
	}
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public void setOrder_value(double order_value) {
		this.order_value = order_value;
	}
	@Override
	public String toString() {
		return "Orders [user_id=" + user_id + ", order_id=" + order_id
				+ ", order_no=" + order_no + ", order_status=" + order_status
				+ ", order_create_time=" + order_create_time
				+ ", order_pay_time=" + order_pay_time + ", order_send_time="
				+ order_send_time + ", order_complete_time="
				+ order_complete_time + ", order_value=" + order_value
				+ ", order_remark=" + order_remark + ", order_address_name="
				+ order_address_name + ", order_address_phone="
				+ order_address_phone + ", order_address_province="
				+ order_address_province + ", order_address_city="
				+ order_address_city + ", order_address_county="
				+ order_address_county + ", order_address_detail="
				+ order_address_detail + ", order_express=" + order_express
				+ ", order_express_no=" + order_express_no + ", items=" + items
				+ "]";
	}
	
}
