package com.mulu.widget;


import java.util.Comparator;

import com.wodangjia.bean.ContactPerson;


public class PinyinComparator implements Comparator<ContactPerson> {

	@Override
	public int compare(ContactPerson o1, ContactPerson o2) {
		String name1=o1.getShowName();
		String name2=o2.getShowName();
			
		
		if (name1.equals("☆")) {
			return -1;
		} else if (name2.equals("☆")) {
			return 1;
		} else if (name1.equals("#")) {
			return -1;
		} else if (name2.equals("#")) {
			return 1;
		} else {
			return name1.compareTo(name2);
		}
	}


}
