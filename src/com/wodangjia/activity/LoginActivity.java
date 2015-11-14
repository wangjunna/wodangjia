package com.wodangjia.activity;

import java.io.Serializable;

import org.apache.http.client.CookieStore;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.bean.User;
import com.wodangjia.bean.View_UserStore;
import com.wodangjia.utils.ACache;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

public class LoginActivity extends Activity implements OnClickListener,Serializable {
	private EditText et_phone, et_password;
	private TextView action_bar_title, action_bar_to;
	private ImageView activity_login_iv_eye;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		ActivityUtils.setActionBarLayout(getActionBar(), this,R.layout.title_bar_login);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out); 
		initView();

	}

	private void initView() {
		action_bar_title = (TextView) findViewById(R.id.action_bar_title);
		action_bar_title.setText("用户登录");
		action_bar_to = (TextView) findViewById(R.id.action_bar_to);
		action_bar_to.setText("注册");
		action_bar_to.setOnClickListener(this);

		et_phone = (EditText) findViewById(R.id.et_phone);
		et_password = (EditText) findViewById(R.id.et_password);

		activity_login_iv_eye = (ImageView) findViewById(R.id.activity_login_iv_eye);
		activity_login_iv_eye.setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.activity_login_tv_forget).setOnClickListener(this);
		findViewById(R.id.activity_login_tv_fast).setOnClickListener(this);

	}

	private void changePwdDisplay() {
		if (et_password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
			et_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			activity_login_iv_eye.setImageResource(R.drawable.login_eye_open);
		} else {
			et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			activity_login_iv_eye.setImageResource(R.drawable.login_eye_close);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_back:// action bar 左上角返回
			ActivityUtils.finish(this);
			break;
		case R.id.action_bar_to:// action bar 右上角注册
			Intent intent=new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.activity_login_tv_forget:// 忘记密码
			Intent intent2=new Intent(this, ForgetPwdActivity.class);
			startActivity(intent2);
			break;
		case R.id.activity_login_tv_fast://快速登录
			Intent intent3=new Intent(this, FastLoginActivity.class);
			startActivity(intent3);
			break;
		case R.id.btn_login:// 登录button
			if (StringUtils.loginCheck(this, et_phone, et_password)) {
				login();// 验证通过开始登录！
			}
			break;
		case R.id.activity_login_iv_eye:// 眼睛 imageview
			changePwdDisplay();
			break;
		default:
			break;
		}
	}


	private void login() {
		
		
		
		StringUtils.showToast(this, "验证通过，开始登录~！");
		HttpUtils httpclient =App.httpclient;
		httpclient.configTimeout(2000);
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "login");
		params.addBodyParameter("phone", et_phone.getText().toString());
		params.addBodyParameter("password_md5", StringUtils.MD5(et_password.getText().toString()));
		httpclient.send(HttpMethod.POST, Config.API_URL, params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(LoginActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				System.out.println(arg0.result);
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==Config.RESULT_STATUS_SUCCESS){
					StringUtils.showToast(LoginActivity.this, "登录成功");
					JSONObject json_user=(JSONObject) JsonUtils.getJsonObject(arg0.result,Config.KEY_USER);
					Gson gson=new Gson();
					View_UserStore user=gson.fromJson(json_user.toString(), View_UserStore.class);
					System.out.println(user.getUser_id());
					App.saveLoginStatus(user);
					System.out.println("user.getUser_id()----------->"+user.getUser_id());
					setResult(RESULT_OK);
					App.connect(App.user.getTooken());
					ActivityUtils.finish(LoginActivity.this);
					
				}else if(status==Config.RESULT_STATUS_FAIL_LOGIN){
					System.out.println( Config.ERROR_LOGIN_FAIL);
					StringUtils.showToast(LoginActivity.this, Config.ERROR_LOGIN_FAIL);
					
				}
				
			}
		});
		
	}
	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	};
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(App.user!=null){
			finish();
		}
	}

}
