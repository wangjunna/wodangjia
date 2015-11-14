package com.wodangjia.bean;

import java.util.ArrayList;


public class Order {
	
	private int order_id;
	private int goods_id;
	private int user_id;
	private String goods_title;
	private String goods_subtitle;
	private double goods_price;
	private int goods_status;
	private String status;
	private ArrayList<String> goods_imgs;
	private int pic_id;
	private int goods_number;
	private String shop_title;
	private int shop_pic;
	private double order_price;
	
	
	
	
	public Order(String goods_title, String goods_subtitle, double goods_price,
			String status, int pic_id, int goods_number, String shop_title,int shop_pic,double order_price) {
		super();
		this.goods_title = goods_title;
		this.goods_subtitle = goods_subtitle;
		this.goods_price = goods_price;
		this.status = status;
		this.pic_id = pic_id;
		this.goods_number = goods_number;
		this.shop_title = shop_title;
		this.shop_pic = shop_pic;
		this.order_price = order_price;
	}

	public Order(String goods_title, String goods_subtitle, double goods_price,
			String status, int pic_id, int goods_number, String shop_title) {
		super();
		this.goods_title = goods_title;
		this.goods_subtitle = goods_subtitle;
		this.goods_price = goods_price;
		this.status = status;
		this.pic_id = pic_id;
		this.goods_number = goods_number;
		this.shop_title = shop_title;
	}
	
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getGoods_title() {
		return goods_title;
	}
	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}
	public String getGoods_subtitle() {
		return goods_subtitle;
	}
	public void setGoods_subtitle(String goods_subtitle) {
		this.goods_subtitle = goods_subtitle;
	}
	public double getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(double goods_price) {
		this.goods_price = goods_price;
	}
	public int getGoods_status() {
		return goods_status;
	}
	public void setGoods_status(int goods_status) {
		this.goods_status = goods_status;
	}
	public ArrayList<String> getGoods_imgs() {
		return goods_imgs;
	}
	public void setGoods_imgs(ArrayList<String> goods_imgs) {
		this.goods_imgs = goods_imgs;
	}
	public int getPic_id() {
		return pic_id;
	}
	public void setPic_id(int pic_id) {
		this.pic_id = pic_id;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShop_title() {
		return shop_title;
	}
	public void setShop_title(String shop_title) {
		this.shop_title = shop_title;
	}
	
	public int getShop_pic() {
		return shop_pic;
	}

	public void setShop_pic(int shop_pic) {
		this.shop_pic = shop_pic;
	}

	
	public double getOrder_price() {
		return order_price;
	}

	public void setOrder_price(double order_price) {
		this.order_price = order_price;
	}

	public Order(int order_id, int goods_id, int user_id, String goods_title,
			String goods_subtitle, double goods_price, int goods_status,
			ArrayList<String> goods_imgs, int pic_id) {
		super();
		this.order_id = order_id;
		this.goods_id = goods_id;
		this.user_id = user_id;
		this.goods_title = goods_title;
		this.goods_subtitle = goods_subtitle;
		this.goods_price = goods_price;
		this.goods_status = goods_status;
		this.goods_imgs = goods_imgs;
		this.pic_id = pic_id;
	}
	public int getGoods_number() {
		return goods_number;
	}
	public void setGoods_number(int goods_number) {
		this.goods_number = goods_number;
	}
	
}
