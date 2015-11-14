package com.wodangjia.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.activity.FastLoginActivity.DownTimer;
import com.wodangjia.app.App;
import com.wodangjia.bean.View_UserStore;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

public class RegisterActivity extends Activity implements OnClickListener{
	private TextView action_bar_title;
	private Button btn_getCode;
	private EditText et_phone,et_code,et_password;
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case Config.MSG_WHAT_SHOWTOAST:
				StringUtils.showToast(RegisterActivity.this, message.getData().getString(Config.KEY_MSG));
				break;
			default:
				break;
			}
		}
		
	};
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SMSSDK.initSDK(this, Config.SMSSDK_APPKEY, Config.SMSSDK_APPSECRET);
		SMSSDK.registerEventHandler(eh); // 注册短信回调
		setContentView(R.layout.activity_register);
		ActivityUtils.setActionBarLayout(getActionBar(), this,R.layout.title_bar_register);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg,RegisterActivity.this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out);
		initView();
	}

	private void initView() {
		btn_getCode=(Button) findViewById(R.id.btn_getCode);
		btn_getCode.setOnClickListener(this);
		et_phone=(EditText) findViewById(R.id.et_phone);
		et_code=(EditText) findViewById(R.id.et_code);
		et_password=(EditText) findViewById(R.id.et_password);
		action_bar_title = (TextView) findViewById(R.id.action_bar_title);
		Button btn_reSetPwd = (Button) findViewById(R.id.btn_register);
		btn_reSetPwd.setText("立即注册");
		btn_reSetPwd.setOnClickListener(this);
		
		findViewById(R.id.btn_back).setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	};

	class DownTimer extends CountDownTimer {

		public DownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_getCode.setText(millisUntilFinished / 1000 + "秒");
		}

		@Override
		public void onFinish() {
			btn_getCode.setText("重新获取");
			btn_getCode.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;
		case R.id.btn_getCode:
			getCode();
			break;
		case R.id.btn_register:
			register();
			break;
		default:
			break;
		}

	}
private void register() {
		if(!StringUtils.registerCheck(this, et_phone, et_code, et_password)){
			return;
		}
		RequestParams params= new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_REGISTER);
		params.addBodyParameter(Config.KEY_PHONE, et_phone.getText().toString());
		params.addBodyParameter(Config.KEY_CODE, et_code.getText().toString());
		params.addBodyParameter(Config.KEY_PASSWORD_MD5, StringUtils.MD5(et_password.getText().toString()));
		
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(RegisterActivity.this,Config.ERROR_UNCONNECTION_INTERNET);
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				switch (status) {
				case Config.RESULT_STATUS_SUCCESS://注册成功
					Gson gson=new Gson();
					View_UserStore user=gson.fromJson(JsonUtils.getJsonObject(arg0.result,Config.KEY_USER).toString(),View_UserStore.class );
					App.saveLoginStatus(user);
					finish();
					break;
				case 1://注册失败
					StringUtils.showToast(RegisterActivity.this,"注册失败！");
					break;
				default:
					break;
				}
				
			}
		});
		
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
	private void getCode() {
		if (!StringUtils.isPhone(this, et_phone)) {
			return;
		}
		String phone = et_phone.getText().toString();
		
		SMSSDK.getVerificationCode(Config.COUNTRY_CODE_CHINA,phone);
		btn_getCode.setEnabled(false);
		new DownTimer(60000, 1000).start();
	
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
}