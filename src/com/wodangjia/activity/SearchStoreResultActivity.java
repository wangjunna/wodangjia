package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.StoreItemAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_Store;
import com.wodangjia.dialog.CustomProgressDialog;
import com.wodangjia.pulltorefresh.pullableview.PullableListView;
import com.wodangjia.pultorefresh.PullToRefreshLayout;
import com.wodangjia.pultorefresh.PullToRefreshLayout.OnRefreshListener;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

public class SearchStoreResultActivity extends Activity implements OnClickListener{
	private String keyword;
	private TextView tv_keyword;
	private CustomProgressDialog dialog;
	private PullToRefreshLayout pullToRefreshLayout;
	private PullableListView listView;
	private ArrayList<View_Store> itemlist;
	private int page;
	private int size=10;
	private boolean refresh = false;// 当前为刷新 还是加载
	private int result_size;// 最后一次获取商品的个数
	private StoreItemAdapter adapter;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_store_result);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_serach_cart);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initData();
		initView(); 
		download();
	}

	private void download() {
		dialog.show();
		Intent intent = getIntent();
		keyword = intent.getStringExtra(Config.KEY_KEYWORD);
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_KEYWORD, keyword);
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_SEARCH_STORE);
		params.addBodyParameter(Config.KEY_SIZE, "" + size);
		params.addBodyParameter(Config.KEY_PAGE, "" + page);
//		params.addBodyParameter(Config.KEY_SORT, "" + sort);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dialog.dismiss();
						StringUtils.showToast(SearchStoreResultActivity.this,
								"连接服务器失败！");
						if (refresh) {
							pullToRefreshLayout
									.refreshFinish(PullToRefreshLayout.REFRESHING);
						} else {
							pullToRefreshLayout
									.loadmoreFinish(PullToRefreshLayout.LOADING);
						}
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						dialog.dismiss();
						StringUtils.showToast(SearchStoreResultActivity.this,
								"连接服务器成功！");
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == Config.RESULT_STATUS_SUCCESS) {
							ArrayList<View_Store> stores;
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<View_Store>>() {
							}.getType();
							stores = gson.fromJson(
									JsonUtils.getJsonArray(arg0.result,
											Config.KEY_STORELIST).toString(),
									type);
							itemlist.addAll(stores);
							adapter.setItemlist(itemlist);
							result_size = stores.size();
							page++;
							if (refresh) {
								pullToRefreshLayout
										.refreshFinish(PullToRefreshLayout.SUCCEED);
							} else {
								pullToRefreshLayout
										.loadmoreFinish(PullToRefreshLayout.SUCCEED);
							}
						}

					}
				});
	}

	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.rl_search).setOnClickListener(this);
		findViewById(R.id.btn_cart).setOnClickListener(this);
		pullToRefreshLayout=(PullToRefreshLayout) findViewById(R.id.refresh_view);
		listView=(PullableListView) findViewById(R.id.search_item_listview);
		listView.setAdapter(adapter);
		pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				refresh = true;
				// pullToRefreshLayout.refreshFinish(PullToRefreshLayout.REFRESHING);
				page = 0;
				itemlist.clear();
				download();

			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				refresh = false;
				StringUtils.showToast(SearchStoreResultActivity.this, ""
						+ result_size);
				StringUtils.showToast(SearchStoreResultActivity.this, ""
						+ (result_size < size));
				if (result_size < size) {
					StringUtils.showToast(SearchStoreResultActivity.this,
							Config.MSG_NO_MORE);
					pullToRefreshLayout
							.loadmoreFinish(pullToRefreshLayout.NOMORE);
				} else {
					download();
				}

			}
		});
	}

	private void initData() {
		Intent intent=getIntent();
		keyword=intent.getStringExtra(Config.KEY_KEYWORD);
		itemlist=new ArrayList<View_Store>();
		dialog = new CustomProgressDialog(this, "正在搜索...", R.anim.frame);
		adapter=new StoreItemAdapter(this, itemlist);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;
		case R.id.rl_search:
			finish();
//			startActivity(new Intent(this, SearchActivity.class));
			break;
		case R.id.btn_cart:
			startActivity(new Intent(this, ShoppingCartActivity.class));
			break;

		default:
			break;
		}
		
	}

	
}
