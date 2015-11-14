package com.wodangjia.bean;

import java.io.Serializable;
import java.util.List;

public class ShoppingCartItem  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int goods_id;
	private String goods_title;
	private String goods_subtitle;
	private double goods_price;
	private int count;
	private int shopping_cart_count;
	private boolean isChecked;
	private List<String> goodsPicList;
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<String> getGoodsPicList() {
		return goodsPicList;
	}
	public void setGoodsPicList(List<String> goodsPicList) {
		this.goodsPicList = goodsPicList;
	}
	public int getShopping_cart_count() {
		return shopping_cart_count;
	}
	public void setShopping_cart_count(int shopping_cart_count) {
		this.shopping_cart_count = shopping_cart_count;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	} 
	
}
