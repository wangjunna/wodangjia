package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.example.wodangjialayout.R.id;
import com.example.wodangjialayout.R.layout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.MyIncomeListviewAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.Orders;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.NoScrollListView;

public class MyIncomeActivity extends BaseActivity implements OnClickListener{
	private NoScrollListView listView;
	private ArrayList<Orders> orders;
	private MyIncomeListviewAdapter adapter;
	private int page;
	private int size = 10;
	private Button btn_load_more;
	private LayoutInflater mInflater;
	private TextView all_income;

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isLoginStatus) {
			finish();
			return;
		}
		setContentView(R.layout.activity_my_income);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_my_income);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initData();
		initView();
		download();
		getAllIncome();
	}

	private void download() {
		btn_load_more.setText("正在加载...");
		btn_load_more.setEnabled(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_GET_STORE_ORDERLIST);
		params.addBodyParameter(Config.KEY_TYPE, "8");// 已完成，包括评价未评价
		params.addBodyParameter(Config.KEY_PAGE, "" + page);
		params.addBodyParameter(Config.KEY_SIZE, "" + size);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						btn_load_more.setText("加载失败");
						btn_load_more.setEnabled(true);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == 0) {
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<Orders>>() {
							}.getType();
							ArrayList<Orders> items;
							items = gson.fromJson(
									JsonUtils.getJsonArray(arg0.result,
											"orderlist").toString(), type);
							orders.addAll(items);
							adapter.setItems(orders);
							changeLoadMoreStatus(items.size());
						}

					}
				});

	}

	private void initData() {
		orders = new ArrayList<Orders>();
		adapter = new MyIncomeListviewAdapter(orders, this);
		mInflater = LayoutInflater.from(this);
	}

	private void initView() {
		all_income=(TextView)findViewById(R.id.all_income);
		findViewById(R.id.btn_back).setOnClickListener(this);
		listView = (NoScrollListView) findViewById(R.id.bill_listview);
		listView.setAdapter(adapter);
		LinearLayout footer = (LinearLayout) mInflater.inflate(
				R.layout.comment_listview_footer, null);
		btn_load_more = (Button) footer.findViewById(R.id.load_more);
		btn_load_more.setText("正在加载账单信息...");
		btn_load_more.setOnClickListener(this);
		listView.addFooterView(footer);
	}

	protected void changeLoadMoreStatus(int count) {
		// TODO Auto-generated method stub
		if (count == 0 && page == 0) {
			btn_load_more.setText("暂无账单");
		} else if (count < size) {
			btn_load_more.setText("已无更多账单");
		} else {
			page++;
			btn_load_more.setText("加载更多账单...");
			btn_load_more.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.load_more:
			download();
			break;
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;
		default:
			break;
		}
	}
	void getAllIncome(){
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_GET_ALL_INCOME);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(MyIncomeActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					double income=Double.parseDouble(JsonUtils.getString(arg0.result, "all_income"));
					all_income.setText(String.format("%.2f", income));
				}
				
			}
		});
	}
}
