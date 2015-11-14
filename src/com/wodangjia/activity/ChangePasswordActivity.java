package com.wodangjia.activity;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChangePasswordActivity extends BaseActivity implements
		OnClickListener {

	private EditText old_password, new_password, re_new_password;
	private Button submit;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_change_password);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initView();
	}

	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		old_password = (EditText) findViewById(R.id.old_password);
		new_password = (EditText) findViewById(R.id.new_password);
		re_new_password = (EditText) findViewById(R.id.re_new_password);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;
		case R.id.submit:
			submit();
			break;
		default:
			break;
		}

	}

	private void submit() {
		if(!StringUtils.isPassword(this, old_password)){
			return;
		}
		if(StringUtils.isPassword(this, new_password)&&StringUtils.isPassword(this, re_new_password)){
			if(!new_password.getText().toString().equals(re_new_password.getText().toString())){
				StringUtils.showToast(this,"新密码输入不一致！");
				return;
			}
		}else {
			return;
		}
		
		if(!StringUtils.MD5(old_password.getText().toString()).equals(App.user.getUser_password())){
			StringUtils.showToast(ChangePasswordActivity.this, "原始密码错误！");
			return;
		}
		
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_CHANGE_PASSWORD);
		params.addBodyParameter(Config.KEY_PASSWORD_MD5,StringUtils.MD5(new_password.getText().toString()));
		App.httpclient.send(HttpMethod.POST,Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(ChangePasswordActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					StringUtils.showToast(ChangePasswordActivity.this, "修改密码成功！");
					App.user.setUser_password(StringUtils.MD5(new_password.getText().toString()));
					ActivityUtils.finish(ChangePasswordActivity.this);
				}else {
					StringUtils.showToast(ChangePasswordActivity.this, "修改密码失败！");
				}
				
			}
		});
		
	}

	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	}
}
