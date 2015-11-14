package com.wodangjia.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPager extends android.support.v4.view.ViewPager {
	private boolean isCanScroll = false;

	public ViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isCanScroll == false) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isCanScroll == false) {
			return false;
		} else {
			return super.onInterceptTouchEvent(ev);
		}

	}

}
