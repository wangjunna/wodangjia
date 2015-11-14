package com.wodangjia.utils;

import android.R.integer;
import android.widget.ImageView;

import com.example.wodangjialayout.R;

public class ImageVIewUtils {
	
	public static void setLevel(int credit,ImageView iv_level){
		if (credit <= 0) {
			iv_level.setImageResource(R.drawable.ic_user_growth_0);
		} else if (credit <= 5) {
			iv_level.setImageResource(R.drawable.ic_user_growth_1);
		} else if (credit <= 15) {
			iv_level.setImageResource(R.drawable.ic_user_growth_2);
		} else if (credit <= 45) {
			iv_level.setImageResource(R.drawable.ic_user_growth_3);
		} else if (credit <= 135) {
			iv_level.setImageResource(R.drawable.ic_user_growth_4);
		} else if (credit <= 405) {
			iv_level.setImageResource(R.drawable.ic_user_growth_5);
		} else {
			iv_level.setImageResource(R.drawable.ic_user_growth_6);
		}
	}
}
