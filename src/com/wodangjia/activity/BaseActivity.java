package com.wodangjia.activity;

import com.wodangjia.app.App;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity{
	boolean isLoginStatus=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(App.user==null){
			isLoginStatus=false;
			Intent intent =new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
