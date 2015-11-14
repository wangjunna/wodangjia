package com.wodangjia.utils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.example.wodangjialayout.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ActivityUtils {
	
	@TargetApi(19) 
	public static void setTranslucentStatus(Window win,boolean on) {
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
	
	public static void setActionBarLayout(ActionBar actionBar,Context context,int layoutId) {
		if( null != actionBar ){
			actionBar.setDisplayShowHomeEnabled( false );
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);

			LayoutInflater inflator = LayoutInflater.from(context);
			View v = inflator.inflate(layoutId, null);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(v,layout);
		}
	}
	
	public static void setStatusBarColor(int res,Activity activity) {
		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(res);
	}
	public static void setStatusBarNull(Activity activity) {
		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		tintManager.setStatusBarTintEnabled(true);
		
		
	}
	public static void finish(Activity activity) {
		activity.finish();
		activity.overridePendingTransition(R.anim.anim_left_to_right_in,R.anim.anim_left_to_right_out); 
	}
}
