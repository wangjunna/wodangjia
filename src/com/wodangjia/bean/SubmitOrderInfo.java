package com.wodangjia.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class SubmitOrderInfo implements Serializable{
	Address address;
	ArrayList<View_ShoppingCartItem> itemlist;
	String remark;
	public SubmitOrderInfo(Address address,
			ArrayList<View_ShoppingCartItem> itemlist) {
		super();
		this.address = address;
		this.itemlist = itemlist;
	}
	
	public SubmitOrderInfo(Address address,
			ArrayList<View_ShoppingCartItem> itemlist, String remark) {
		super();
		this.address = address;
		this.itemlist = itemlist;
		this.remark = remark;
	}

	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public ArrayList<View_ShoppingCartItem> getItemlist() {
		return itemlist;
	}
	public void setItemlist(ArrayList<View_ShoppingCartItem> itemlist) {
		this.itemlist = itemlist;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
