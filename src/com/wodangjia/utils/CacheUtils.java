package com.wodangjia.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class CacheUtils {
	public static String getCachedUser(Context context) {
		return context
				.getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE)
				.getString(Config.KEY_USER, null);
	}

	public static void setCachedToken(Context context, String userJsonObj) {
		Editor e = context.getSharedPreferences(Config.APP_ID,
				Context.MODE_PRIVATE).edit();
		e.putString(Config.KEY_USER, userJsonObj);
		e.commit();

	}

	public static String getCachedPhoneNum(Context context) {
		return context
				.getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE)
				.getString(Config.KEY_PHONE, null);
	}

	public static void setCachedPhoneNum(Context context, String phoneNum) {
		Editor e = context.getSharedPreferences(Config.APP_ID,
				Context.MODE_PRIVATE).edit();
		e.putString(Config.KEY_PHONE, phoneNum);
		e.commit();
	}
}
