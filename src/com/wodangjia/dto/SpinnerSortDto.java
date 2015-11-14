package com.wodangjia.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpinnerSortDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		list.add("人气优先");
		list.add("好评优先");
		list.add("销量优先");
		list.add("价格最低");
		list.add("价格最高");
		return list;

	}
}
