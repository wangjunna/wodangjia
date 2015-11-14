package com.wodangjia.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.wodangjia.utils.ActivityUtils;

public class ForgetPwdActivity extends Activity{
	private TextView action_bar_title, action_bar_to;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActivityUtils.setActionBarLayout(getActionBar(),this, R.layout.title_bar_fastlogin);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg,ForgetPwdActivity.this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out); 
	    initView();
	}
	private void initView() {
		action_bar_title=(TextView) findViewById(R.id.action_bar_title);
		Button btn_reSetPwd=(Button) findViewById(R.id.btn_register);
		btn_reSetPwd.setText("重设密码");
		action_bar_title.setText("找回密码");
		findViewById(R.id.register_rule_tv).setVisibility(View.INVISIBLE);
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtils.finish(ForgetPwdActivity.this);
			}
		});
	
	}
	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	};

}