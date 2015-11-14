package com.wodangjia.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.bean.Version;
import com.wodangjia.dialog.BaseDialog.OnConfirmListener;
import com.wodangjia.dialog.LoadingDialog;
import com.wodangjia.dialog.SelectionDialog;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.CacheManager;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.FileCalculateAsyncTask;
import com.wodangjia.utils.FileDeleteAsyncTask;
import com.wodangjia.utils.FileUtils;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.OnResponseListener;
import com.wodangjia.utils.StringUtils;

public class SettingActivity extends Activity implements OnClickListener {

	TextView logout,tv_version;
	String version;
	/**
	 * SD卡缓存大小
	 */
	private long mExternCacheSize;

	/**
	 * 外部缓存目录
	 */
	private File mExternalCacheFile;

	/**
	 * 缓存数据View
	 */
	private TextView mTvCacheSize;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_setting);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initData();
		initView();
		UpdateUI();
	}

	private void initData() {
		App app = (App) getApplication();
		version = app.getVersion();
	}

	private void UpdateUI() {
		// TODO Auto-generated method stub
		getCacheSize();
	}

	private void initView() {
		findViewById(R.id.rl_update).setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.feedback).setOnClickListener(this);
		findViewById(R.id.setting_clean).setOnClickListener(this);
		findViewById(R.id.setting_share).setOnClickListener(this);
		findViewById(R.id.setting_about).setOnClickListener(this);
		tv_version=(TextView) findViewById(R.id.tv_version);
		tv_version.setText(version);
		logout = (TextView) findViewById(R.id.logout);
		logout.setOnClickListener(this);
		mTvCacheSize = (TextView) findViewById(R.id.iv_cache_value);
		if (App.user == null) {
			logout.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;
		case R.id.setting_clean:
			clearCache();
			break;
		case R.id.setting_share:
			share();
			break;
		case R.id.feedback:
			FeedBack();
			break;
		case R.id.setting_about:
			startActivity(new Intent(SettingActivity.this, AboutActivity.class));
			break;
		case R.id.logout:
			App.logout();
			startActivity(new Intent(SettingActivity.this, LoginActivity.class));
			finish();
			break;
		case R.id.rl_update:
			update();
			break;
		default:
			break;
		}

	}

	private void update() {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,Config.ACTION_GET_NEWEST_VERSION);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(SettingActivity.this,Config.ERROR_UNCONNECTION_INTERNET);
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					Gson gson= new Gson();
					Version version=gson.fromJson(JsonUtils.getJsonObject(arg0.result, "version").toString(), Version.class);
					App app=(App) getApplication();
					if(version.getVersion().equals(app.getVersion())){
						StringUtils.showToast(SettingActivity.this, "当前为最新版本！");
					}else{
						StringUtils.showToast(SettingActivity.this, "发现新版本");
						Uri uri = Uri.parse(Config.APP_DOWNLOAD_URL);  
						SettingActivity.this.startActivity(new Intent(Intent.ACTION_VIEW,uri));  
					}
				
				}else {
					StringUtils.showToast(SettingActivity.this, "获取版本信息失败！");
				}
			}
		});
	}

	private void FeedBack() {
		Intent intent = new Intent(this, FeedBackActivity.class);
		startActivity(intent);
	}

	private void share() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "《我当家》，非常好用，赶紧去各大应用市场下载吧。 官方下载地址："
				+ Config.APP_DOWNLOAD_URL);
		startActivity(Intent.createChooser(intent, "我当家应用分享！"));

	}

	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	};

	private void getCacheSize() {
		FileCalculateAsyncTask task = new FileCalculateAsyncTask(this);
		mExternalCacheFile = new File(
				CacheManager.getExternalCachePath(SettingActivity.this));
		// mExternalCacheFile = SettingActivity.this.getExternalCacheDir();
		task.execute(mExternalCacheFile);
		task.setOnResponseListener(new OnResponseListener() {

			@Override
			public void onResponse(String resultString) {
				// TODO Auto-generated method stub
				try {
					mExternCacheSize = Long.valueOf(resultString);
					mTvCacheSize.setText(FileUtils.formatSize(mExternCacheSize));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 清除缓存
	 */
	private void clearCache() {
		// TODO Auto-generated method stub
		SelectionDialog dialog = new SelectionDialog(this, "清除缓存", "确定清除"
				+ FileUtils.formatSize(mExternCacheSize) + "缓存吗？");
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onConfirm(String result) {
				// TODO Auto-generated method stub

				final LoadingDialog dialog = new LoadingDialog(
						SettingActivity.this, "正在清理缓存");
				dialog.show();

				FileDeleteAsyncTask task = new FileDeleteAsyncTask(
						SettingActivity.this);
				task.execute(mExternalCacheFile);
				task.setOnResponseListener(new OnResponseListener() {

					@Override
					public void onResponse(String resultString) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						StringUtils.showToast(SettingActivity.this, "清理成功");
						UpdateUI();
					}
				});
			}
		});
		dialog.show();
	}

}
