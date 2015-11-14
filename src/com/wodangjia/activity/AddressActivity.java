package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.AddressItemListAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.Address;
import com.wodangjia.dialog.CustomProgressDialog;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

public class AddressActivity extends BaseActivity implements OnClickListener {
	CustomProgressDialog dialog ;
	ListView listView;
	LayoutInflater mInflater;
	AddressItemListAdapter adapter;
	Button addButton;
	ImageView checkView, editView, deleteView;
	HttpUtils httpUtils;
	int requestCode;
	ArrayList<Address> addresslist;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!isLoginStatus){
			finish();
			return;
		}
		setContentView(R.layout.activity_address);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_address_main);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initData();
		initView();
		getData();

	}

	private void initData() {
		addresslist = new ArrayList<Address>();
		Intent intent = getIntent();
		requestCode = intent.getIntExtra(Config.KEY_REQUESTCODE, 0);
		dialog=new CustomProgressDialog(this, "正在加载，请稍候", R.anim.frame);
		dialog.show();
		
	}

	private void getData() {
		dialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_ADDRESS_LIST);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils.showToast(AddressActivity.this,
								Config.ERROR_UNCONNECTION_INTERNET);
						dialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						int status = JsonUtils
								.getInt(result, Config.KEY_STATUS);
						if (status == 0) {
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<Address>>() {
							}.getType();
							ArrayList<Address> addresses = gson.fromJson(
									JsonUtils.getJsonArray(result,
											Config.KEY_ADDRESSLIST).toString(),
									type);
							addresslist.addAll(addresses);
							adapter.notifyDataSetChanged();
						} else {
							StringUtils.showToast(AddressActivity.this,
									"获取收货地址列表失败！");
						}
						dialog.dismiss();
					}
					
				});

	}

	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.address_listview);
		addButton = (Button) findViewById(R.id.address_add_btn);
		addButton.setOnClickListener(this);
		// 创建自定义适配器实例
		adapter = new AddressItemListAdapter(addresslist, AddressActivity.this);
		listView.setAdapter(adapter);
		if (requestCode == 1) {
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent();
					intent.putExtra(Config.KEY_ADDRESS,
							addresslist.get(position));
					setResult(RESULT_OK, intent);
					ActivityUtils.finish(AddressActivity.this);
				}
			});
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_back:
			StringUtils.showToast(this, "单击了返回");
			ActivityUtils.finish(this);
			break;
		case R.id.address_add_btn:
			StringUtils.showToast(this, "单击了添加收货地址");
			startActivityForResult(new Intent(this, AddressAddActivity.class),1);
			break;
		case R.id.address_moren_img:
			StringUtils.showToast(this, "单击了选择收货地址");
			break;
		case R.id.address_editor_img:
			StringUtils.showToast(this, "单击了修改收货地址");
			break;
		case R.id.address_delete_img:
			StringUtils.showToast(this, "单击了删除收货地址");
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode==RESULT_OK){
			addresslist.clear();
			getData();
			adapter.setList(addresslist);
		}
	}
}
