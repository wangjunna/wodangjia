package com.wodangjia.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter{

	ArrayList<View> views;
	
	public ViewPagerAdapter(ArrayList<View> views) {
		super();
		this.views = views;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 当前界面显示的是哪一个视图
		View view = views.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position,
			Object object) {
		// 销毁当前页视图
		View view = views.get(position);
		container.removeView(view);
	}

}
