package com.wodangjia.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class JsonUtils {
	public static int getInt(String jsonString,String name) {
		int num=-1;
		try {
			JSONObject jsonObject=new JSONObject(jsonString);
			num = jsonObject.getInt(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return num;
	}
	public static double getDouble (String jsonString,String name) {
		double num=0;
		try {
			JSONObject jsonObject=new JSONObject(jsonString);
			num = jsonObject.getDouble(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return num;
	}
	public static String getString(String jsonString,String name) {
		String value=null;
		try {
			JSONObject jsonObject=new JSONObject(jsonString);
			value = jsonObject.getString(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}
	public static Object getJsonObject(String jsonString ,String name) {
		System.out.println("jsonString"+jsonString);
		JSONObject jsonObject2=null;
		try {
			JSONObject jsonObject=new JSONObject(jsonString);
			jsonObject2=jsonObject.getJSONObject(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject2;
	}
	public static JSONArray getJsonArray(String jsonString ,String name) {
		System.out.println("jsonString"+jsonString);
		JSONArray jsArray=null;
		try {
			JSONObject jsonObject=new JSONObject(jsonString);
			jsArray=jsonObject.getJSONArray(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("jsArray===="+jsArray);
		return jsArray;
	}
	public static JSONObject stringToJsonObject() {
		
		return null;
	}
	public static Gson  getListFromGson() {
	
		return null;
	}
}
