package com.wodangjia.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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

public class FeedBackActivity extends Activity implements OnClickListener{
	Button btn_submit;
	EditText et_feedback_context,et_contact;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_feedback);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initView(); 
	}

	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		et_feedback_context=(EditText) findViewById(R.id.et_feedback_context);
		
		et_contact=(EditText) findViewById(R.id.et_contact);
		btn_submit=(Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;
		case R.id.btn_submit:
			submitFeedback(); 
			break;
		default:
			break;
		}
		
	}
	private void submitFeedback() {
		
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_SUBMIT_FEEDBACK);
		params.addBodyParameter(Config.KEY_CONTACT, et_contact.getText().toString());
		params.addBodyParameter(Config.KEY_FEEDBACK, et_feedback_context.getText().toString());
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(FeedBackActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result,Config.KEY_STATUS);
				if (status==0) {
					StringUtils.showToast(FeedBackActivity.this, Config.SUCCESS_FEEDBACK);
					et_contact.setText("");
					et_feedback_context.setText("");
				}else {
					StringUtils.showToast(FeedBackActivity.this, "提交反馈失败，请您重试！");
				}
			}
		});
		
	}

	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	}
	

}
