package com.wodangjia.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class View_ShoppingCartItem  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private View_Goods item;
	private int user_id;
	private int count;
	private boolean checked;
	public View_ShoppingCartItem(View_Goods item, int user_id, int count) {
		super();
		this.item = item;
		this.user_id = user_id;
		this.count = count;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public View_Goods getItem() {
		return item;
	}
	public void setItem(View_Goods item) {
		this.item = item;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
}
