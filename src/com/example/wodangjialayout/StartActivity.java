package com.example.wodangjialayout;

import com.wodangjia.activity.IndexActivity;
import com.wodangjia.utils.ActivityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends Activity {

	Handler handler;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strart);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg_00, this);
	}
	@Override
	protected void onStart() {
		super.onStart();
		handler=new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(StartActivity.this, IndexActivity.class));
				StartActivity.this.finish();
			}
		}, 3000);
	}
}
