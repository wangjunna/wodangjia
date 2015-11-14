package com.wodangjia.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpinnerGoodsDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public static List<String> getList(){
		List<String> list = new ArrayList<String>();
		list.add("全部商品");
		list.add("零食");
		list.add("衣服");
		list.add("日用");
		list.add("其他");
				
		return list;
		
	}
	
}
