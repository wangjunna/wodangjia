package com.wodangjia.activity;

import cn.smssdk.SMSSDK;

import com.example.wodangjialayout.R;
import com.example.wodangjialayout.R.anim;
import com.example.wodangjialayout.R.color;
import com.example.wodangjialayout.R.id;
import com.example.wodangjialayout.R.layout;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.activity.BundingActivity.DownTimer;
import com.wodangjia.app.App;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BundingNewActivity extends BaseActivity implements OnClickListener{
	
	EditText phone;
	EditText code;
	Button getCode;
	Button next;

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bunding_new);
		ActivityUtils.setActionBarLayout(getActionBar(), this,R.layout.title_bar_bundling);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out);
		
		init();
	}

	private void init() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		phone = (EditText) findViewById(R.id.new_et_phone);
		code = (EditText) findViewById(R.id.new_et_code);
		getCode = (Button) findViewById(R.id.new_btn_getCode);
		next = (Button) findViewById(R.id.new_submit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			StringUtils.showToast(this, "单击了返回");
			ActivityUtils.finish(this);
			break;
		case R.id.new_btn_getCode:
			getCode();
//			StringUtils.showToast(this, "验证码已发送，请注意查收");
			break;
		case R.id.new_submit:
			submit();
			break;
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
	private void getCode() {
		if (!StringUtils.isPhone(this, phone)) {
			return;
		}
		String phonestr = phone.getText().toString();
		
		SMSSDK.getVerificationCode(Config.COUNTRY_CODE_CHINA,phonestr);
		getCode.setEnabled(false);
		new DownTimer(60000, 1000).start();
	
	}

	class DownTimer extends CountDownTimer {

		public DownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			getCode.setText(millisUntilFinished / 1000 + "秒");
		}

		@Override
		public void onFinish() {
			getCode.setText("重新获取");
			getCode.setEnabled(true);
		}
	}
	private void submit() {
		if (!(StringUtils.isPhone(this, phone)&&StringUtils.isVerCode(BundingNewActivity.this, code))) {
			return;
		}
		
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_CHANGE_PHONE);
		params.addBodyParameter(Config.KEY_PHONE, App.user.getUser_phone());
		params.addBodyParameter(Config.KEY_CODE,code.getText().toString()) ;
		App.httpclient.send(HttpMethod.POST,Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(BundingNewActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					StringUtils.showToast(BundingNewActivity.this, "修改绑定手机成功!");
					finish();
				}else {
					StringUtils.showToast(BundingNewActivity.this,"验证码错误！");
				}
				
			}
		});
		
	}
}
