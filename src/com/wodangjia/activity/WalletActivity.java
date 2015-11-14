package com.wodangjia.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.bean.Wallet;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.zf.iosdialog.widget.CustomAlertDialog;


public class WalletActivity extends BaseActivity implements OnClickListener{

	LinearLayout  enterInfoLayout;
	LinearLayout  chongzhiLayout;
	LinearLayout  tixianLayout;
	
	RelativeLayout myBankLayout;
	RelativeLayout psdLayout;
	RelativeLayout checkLayout;
	private LayoutInflater mInflater;
	Wallet wallet;
	private TextView tv_balance;
	
	@SuppressLint("ResourceAsColor") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
		ActivityUtils.setActionBarLayout(getActionBar(), this,R.layout.title_bar_wallet);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out);
		initData();
		initView();
	}

	private void UpdateUI() {
		tv_balance.setText(""+wallet.getWallet_balance());
	}

	private void initData() {
		mInflater=LayoutInflater.from(this);
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_WALLET_INFO);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					Gson gson=new Gson();
					wallet=gson.fromJson(JsonUtils.getJsonObject(arg0.result, "wallet").toString(), Wallet.class);
					UpdateUI();
				}
			}
		});
		
	}

	private void initView() {
//		findViewById(R.id.btn_back).setOnClickListener(this);
		tv_balance=(TextView) findViewById(R.id.tv_balance);
		
		enterInfoLayout = (LinearLayout) findViewById(R.id.wallet_balance_info);
		chongzhiLayout = (LinearLayout) findViewById(R.id.wallet_recharge);
		tixianLayout = (LinearLayout) findViewById(R.id.wallet_taken);
		psdLayout = (RelativeLayout) findViewById(R.id.wallet_list_pay_pasd);
		checkLayout = (RelativeLayout) findViewById(R.id.wallet_list_check);
		
		enterInfoLayout.setOnClickListener(this);
		chongzhiLayout.setOnClickListener(this);
		tixianLayout.setOnClickListener(this);
		psdLayout.setOnClickListener(this);
		checkLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			StringUtils.showToast(this, "单击了返回");
			ActivityUtils.finish(this);
			break;
		case R.id.wallet_balance_info:
			StringUtils.showToast(this, "单击了进入账单详情");
			break;
		case R.id.wallet_recharge:
			StringUtils.showToast(this, "单击了充值");
			break;
		case R.id.wallet_taken:
			StringUtils.showToast(this, "单击了提现");
			break;
		case R.id.wallet_list_pay_pasd:
			StringUtils.showToast(this, "单击了支付密码");
			showPayPwdDialog();
			break;
		case R.id.wallet_list_check:
			StringUtils.showToast(this, "单击了我的账单");
			break;
		default:
			break;
		}
		
	}
	private void showPayPwdDialog() {
		View view =mInflater.inflate(R.layout.dialog_update_pay_pwd, null);
		EditText et_old=(EditText) view.findViewById(R.id.et_oldpassword);
		EditText et_new=(EditText) view.findViewById(R.id.et_newpassword);
		final CustomAlertDialog dialog = new CustomAlertDialog(this).builder()
				.setTitle("修改支付密码").setView(view)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
		dialog.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		dialog.setCancelable(false);
		dialog.show();
		
	}

	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	};
	
}
