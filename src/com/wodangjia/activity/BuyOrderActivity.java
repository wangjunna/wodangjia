package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.example.wodangjialayout.R;
import com.example.wodangjialayout.R.color;
import com.example.wodangjialayout.R.id;
import com.example.wodangjialayout.R.layout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.OrderGoodsListviewAdapter;
import com.wodangjia.adapter.OrderListviewAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.Orders;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.dialog.CustomProgressDialog;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.NoScrollListView;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class BuyOrderActivity extends BaseActivity implements OnClickListener {
	private CustomProgressDialog dialog;
	private RadioGroup radioGroup;
	private NoScrollListView listView;
	private ArrayList<Orders> orderlist;
	private OrderListviewAdapter adapter;
	private int type;
	private int page;
	private int size = 10;
	private Button btn_load_more;
	RadioButton rb_all;
	LayoutInflater mInflater;
	
	private LinearLayout ll_empty;
	private TextView tv_seemore;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!isLoginStatus){
			finish();
			return;
		}
		setContentView(R.layout.activity_buy_order);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_buy_order);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		initData();
		initView();

	}

	private void download() {
		dialog.show();
		btn_load_more.setText("正在加载订单信息...");
		btn_load_more.setEnabled(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_GET_ORDERLIST);
		params.addBodyParameter(Config.KEY_TYPE, "" + type);
		params.addBodyParameter(Config.KEY_PAGE, "" + page);
		params.addBodyParameter(Config.KEY_SIZE, "" + size);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils.showToast(BuyOrderActivity.this,
								Config.ERROR_UNCONNECTION_INTERNET);
						dialog.dismiss();
						btn_load_more.setEnabled(true);
						btn_load_more.setText("加载订单失败！");
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
							orderlist.addAll(items);
							adapter.setItems(orderlist);
							changeLoadMoreStatus(items.size());
							// StringUtils.showToast(BuyOrderActivity.this,
							// arg0.result);

						}
						dialog.dismiss();
					}
				});

	}

	private void initData() {
		orderlist = new ArrayList<Orders>();
		adapter = new OrderListviewAdapter(orderlist, this);
		dialog = new CustomProgressDialog(this, "正在加载订单…", R.anim.frame);
		mInflater = LayoutInflater.from(this);
	}

	private void initView() {
		tv_seemore=(TextView)findViewById(R.id.tv_seemore);
		tv_seemore.setOnClickListener(this);
		ll_empty=(LinearLayout)findViewById(R.id.ll_empty);
		radioGroup = (RadioGroup) findViewById(R.id.order_radiogroup);
		rb_all = (RadioButton) findViewById(R.id.rb_all);

		LinearLayout footer = (LinearLayout) mInflater.inflate(
				R.layout.comment_listview_footer, null);
		btn_load_more = (Button) footer.findViewById(R.id.load_more);
		btn_load_more.setText("正在加载商品信息...");
		btn_load_more.setOnClickListener(this);
		listView = (NoScrollListView) findViewById(R.id.order_listview);
		listView.setAdapter(adapter);
		listView.addFooterView(footer);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_all:
					type = -1;
					break;
				case R.id.rb_wait_pay:
					type = 0;
					break;
				case R.id.rb_wait_send:
					type = 1;
					break;
				case R.id.rb_hassend:
					type = 2;
					break;
				case R.id.rb_wait_comment:
					type = 3;
					break;
				case R.id.rb_close:
					type = 4;
					break;

				default:
					break;
				}
				page = 0;
				orderlist.clear();
				download();

			}
		});
		rb_all.setChecked(true);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.load_more:
			download();
			break;
		case R.id.tv_seemore:
			setResult(RESULT_OK);
			ActivityUtils.finish(this);
			break;

		default:
			break;
		}
		
	}
	protected void changeLoadMoreStatus(int count) {
		// TODO Auto-generated method stub
		if (count == 0 && page == 0) {
			btn_load_more.setText("暂无相关订单");
			btn_load_more.setVisibility(View.GONE);
			ll_empty.setVisibility(View.VISIBLE);

		} else if (count < size) {
			btn_load_more.setText("已无更多订单");
			btn_load_more.setVisibility(View.VISIBLE);
			ll_empty.setVisibility(View.GONE);
		} else {
			page++;
			btn_load_more.setText("加载更多订单..");
			btn_load_more.setEnabled(true);
			btn_load_more.setVisibility(View.VISIBLE);
			ll_empty.setVisibility(View.GONE);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			switch (requestCode) {
			case Config.REQUEST_CODE_COMMENT:
				page=0;
				orderlist.clear();
				download();
				break;

			default:
				break;
			}
		}
	}
}
