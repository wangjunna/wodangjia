package com.wodangjia.bean;

import java.io.Serializable;

public class GoodsPic implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3882317288504801891L;
	private int id ;
	private int goods_id ;
	private String  path ;
	private String  upload_time ;
	private int  sort ;
	public GoodsPic(int id, int goods_id, String path, String upload_time,
			int sort) {
		super();
		this.id = id;
		this.goods_id = goods_id;
		this.path = path;
		this.upload_time = upload_time;
		this.sort = sort;
	}
	public GoodsPic(int goods_id, String path, String upload_time,
			int sort) {
		super();
		this.goods_id = goods_id;
		this.path = path;
		this.upload_time = upload_time;
		this.sort = sort;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUpload_time() {
		return upload_time;
	}
	public void setUpload_time(String upload_time) {
		this.upload_time = upload_time;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@Override
	public String toString() {
		return "GoodsPic [id=" + id + ", goods_id=" + goods_id + ", path="
				+ path + ", upload_time=" + upload_time + ", sort=" + sort
				+ "]";
	}
	
	
}
