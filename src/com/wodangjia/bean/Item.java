package com.wodangjia.bean;

import java.io.Serializable;
import java.util.ArrayList;


public class Item implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int goods_id;
	private int user_id;
	private int goods_type;
	private String goods_title;
	private String goods_subtitle;
	private int goods_stock;
	private double goods_price;
	private int goods_status;
	private String goods_create_time;
	private String goods_modify_time;
	private int goods_sales;
	private ArrayList<GoodsPic> goods_imgs;
	
	
	
	public Item(int goods_id, int user_id, int goods_type, String goods_title,
			String goods_subtitle, int goods_stock, double goods_price,
			int goods_status, String goods_create_time,
			String goods_modify_time, int goods_sales,
			ArrayList<GoodsPic> goods_imgs) {
		super();
		this.goods_id = goods_id;
		this.user_id = user_id;
		this.goods_type = goods_type;
		this.goods_title = goods_title;
		this.goods_subtitle = goods_subtitle;
		this.goods_stock = goods_stock;
		this.goods_price = goods_price;
		this.goods_status = goods_status;
		this.goods_create_time = goods_create_time;
		this.goods_modify_time = goods_modify_time;
		this.goods_sales = goods_sales;
		this.goods_imgs = goods_imgs;
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
	public int getGoods_type() {
		return goods_type;
	}
	public void setGoods_type(int goods_type) {
		this.goods_type = goods_type;
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
	public int getGoods_stock() {
		return goods_stock;
	}
	public void setGoods_stock(int goods_stock) {
		this.goods_stock = goods_stock;
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
	public String getGoods_create_time() {
		return goods_create_time;
	}
	public void setGoods_create_time(String goods_create_time) {
		this.goods_create_time = goods_create_time;
	}
	public String getGoods_modify_time() {
		return goods_modify_time;
	}
	public void setGoods_modify_time(String goods_modify_time) {
		this.goods_modify_time = goods_modify_time;
	}
	public int getGoods_sales() {
		return goods_sales;
	}
	public void setGoods_sales(int goods_sales) {
		this.goods_sales = goods_sales;
	}
	public ArrayList<GoodsPic> getGoods_imgs() {
		return goods_imgs;
	}

	public void setGoods_imgs(ArrayList<GoodsPic> goods_imgs) {
		this.goods_imgs = goods_imgs;
	}
	
}
