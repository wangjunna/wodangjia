package com.wodangjia.activity;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.example.wodangjialayout.R;
import com.example.wodangjialayout.R.anim;
import com.example.wodangjialayout.R.color;
import com.example.wodangjialayout.R.layout;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.activity.RegisterActivity.DownTimer;
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
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BundingActivity extends BaseActivity implements OnClickListener{
	
	EditText phone;
	EditText code;
	Button getCode;
	Button next;
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case Config.MSG_WHAT_SHOWTOAST:
				StringUtils.showToast(BundingActivity.this, message.getData().getString(Config.KEY_MSG));
				break;
			default:
				break;
			}
		}
		
	};
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SMSSDK.initSDK(this, Config.SMSSDK_APPKEY, Config.SMSSDK_APPSECRET);
		SMSSDK.registerEventHandler(eh); // 注册短信回调
		setContentView(R.layout.activity_bundling);
		ActivityUtils.setActionBarLayout(getActionBar(), this,R.layout.title_bar_bundling);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out);
		
		initView();
	}

	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		phone = (EditText) findViewById(R.id.now_et_phone);
		phone.setEnabled(false);
		if(App.user!=null){
			phone.setText(App.user.getUser_phone());
		}else {
			return;
		}
		code = (EditText) findViewById(R.id.now_et_code);
		getCode = (Button) findViewById(R.id.now_btn_getCode);
		next = (Button) findViewById(R.id.next_btn);
		
		getCode.setOnClickListener(this);
		next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			StringUtils.showToast(this, "单击了返回");
			ActivityUtils.finish(this);
			break;
		case R.id.now_btn_getCode:
			getCode();
			break;
		case R.id.next_btn:
			Submit();
			break;
		default:
			break;
		}
	}
	private void Submit() {
		if (!StringUtils.isVerCode(BundingActivity.this, code)) {
			return;
		}
		
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_CHECK_VERIFICATION_CODE);
		params.addBodyParameter(Config.KEY_PHONE, App.user.getUser_phone());
		params.addBodyParameter(Config.KEY_CODE,code.getText().toString()) ;
		App.httpclient.send(HttpMethod.POST,Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(BundingActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					startActivity(new Intent(BundingActivity.this,BundingNewActivity.class));
					finish();
				}else {
					StringUtils.showToast(BundingActivity.this,"验证码错误！");
				}
				
			}
		});
		
	}

	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	}
EventHandler eh = new EventHandler() {
		
		@Override
		public void afterEvent(int event, int result, Object data) {

			if (result == SMSSDK.RESULT_COMPLETE) {
				// 回调完成
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					// 提交验证码成功
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					// 获取验证码成功
					System.out.println("获取验证码成功");
					Message message=new Message();
					Bundle bundle=new Bundle();
					bundle.putString(Config.KEY_MSG, Config.SUCCESS_SEND_CODE);
					message.setData(bundle);
					handler.sendMessage(message);
					
				} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					// 返回支持发送验证码的国家列表
				}
			} else {
				((Throwable) data).printStackTrace();
				System.err.println("------获取验证码出错---------");
				Message message=new Message();
				message.what=Config.MSG_WHAT_SHOWTOAST;
				Bundle bundle=new Bundle();
				bundle.putString(Config.KEY_MSG, Config.ERROR_SEND_CODE);
				message.setData(bundle);
				handler.sendMessage(message);
			}
		}
	};
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
}
