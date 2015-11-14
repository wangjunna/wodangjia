package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.ItemListAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_Store;
import com.wodangjia.dialog.CustomProgressDialog;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.ImageVIewUtils;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.NoScrollListView;

public class StoreActivity extends Activity implements OnClickListener {
	private CustomProgressDialog dialog ;
	private TextView tv_storename, tv_credit, tv_collections;
	private View_Store store;
	private ImageView logo,store_level;
	private BitmapUtils bitmapUtils;
	private int page = 0;
	private int size = 10;
	private ArrayList<View_Goods> itemlist;
	private NoScrollListView listView;
	private ItemListAdapter adapter;
	private LayoutInflater mInflater;
	private Button btn_load_more;
	private int store_user_id;
	private boolean hasCollect = false;
	private ImageView iv_collect;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg_00, this);
		initView();
		initData();
	}

	private void initData() {
		bitmapUtils = new BitmapUtils(this);
		Intent intent = getIntent();
		int user_id = intent.getIntExtra("user_id", 0);
		if (user_id == 0) {
			finish();
			return;
		}
		store_user_id = user_id;
		getStoreInfo();

	}

	private void getStroeGoods() {
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_GET_STROE_GOODS);
		params.addBodyParameter(Config.KEY_SORT, ""
				+ Config.ORDER_BY_GOODS_SALES_DESC);
		params.addBodyParameter(Config.KEY_USER_ID, "" + store_user_id);
		params.addBodyParameter(Config.KEY_PAGE, "" + page);
		params.addBodyParameter(Config.KEY_SIZE, "" + size);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						btn_load_more.setText("加载失败！");
						dialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == 0) {
							ArrayList<View_Goods> items = new ArrayList<View_Goods>();
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<View_Goods>>() {
							}.getType();
							items = gson.fromJson(
									JsonUtils.getJsonArray(arg0.result,
											Config.KEY_GOODSLIST).toString(),
									type);
							itemlist.addAll(items);
							adapter.notifyDataSetChanged();
							changeLoadMoreStatus(items.size());
							
//							page++;
						}
						dialog.dismiss();

					}
				});

	}

	void getStoreInfo() {

		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_STORE_INFO);
		params.addBodyParameter(Config.KEY_USER_ID, "" + store_user_id);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils.showToast(StoreActivity.this,
								Config.ERROR_UNCONNECTION_INTERNET);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == 0) {
							Gson gson = new Gson();
							store = gson.fromJson(
									JsonUtils.getJsonObject(arg0.result,
											Config.KEY_STORE).toString(),
									View_Store.class);
//							StringUtils.showToast(StoreActivity.this,
//									store.toString());
							updateUI();
							getStroeGoods();
							
						}
					}
				});
	}

	@SuppressLint("NewApi")
	protected void updateUI() {
		tv_storename.setText(store.getStore_name());
		tv_credit.setText("信誉度 " + store.getStore_credit());
		ImageVIewUtils.setLevel(store.getStore_credit(), store_level);
		tv_collections.setText("收藏 " + store.getCollections());

		bitmapUtils.display(logo,
				Config.SERVER_HOST + store.getStore_img_path());
	}

	private void initView() {
		dialog=new CustomProgressDialog(this, "正在加载，请稍候", R.anim.frame);
		dialog.show();
		store_level=(ImageView) findViewById(R.id.store_level);
		mInflater = LayoutInflater.from(this);
		itemlist = new ArrayList<View_Goods>();
		adapter = new ItemListAdapter(itemlist, this);
		tv_storename = (TextView) findViewById(R.id.tv_storename);
		tv_credit = (TextView) findViewById(R.id.tv_sales);
		tv_collections = (TextView) findViewById(R.id.tv_collections);
		logo = (ImageView) findViewById(R.id.logo);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		LayoutParams params = logo.getLayoutParams();
		params.height = screenWidth / 16 * 9;
		logo.setLayoutParams(params);
		adapter = new ItemListAdapter(itemlist, this);
		listView = (NoScrollListView) findViewById(R.id.item_list);
		findViewById(R.id.btn_back).setOnClickListener(this);
		iv_collect = (ImageView) findViewById(R.id.store_collect);
		iv_collect.setOnClickListener(this);
		LinearLayout footer = (LinearLayout) mInflater.inflate(
				R.layout.comment_listview_footer, null);
		btn_load_more = (Button) footer.findViewById(R.id.load_more);
		btn_load_more.setText("正在加载...");
		listView.addFooterView(footer);
		listView.setAdapter(adapter);
		btn_load_more.setOnClickListener(this);

	}

	protected void changeLoadMoreStatus(int count) {
		dialog.dismiss();
		if (page==0) {
			getCollectionStatus();
		}
		if (count == 0 && page == 0) {
			btn_load_more.setText("暂无商品");
			
		} else if (count < size) {
			btn_load_more.setText("已无更多商品");
		} else {
			btn_load_more.setText("更多商品");
			btn_load_more.setEnabled(true);
			page++;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.load_more:
			btn_load_more.setText("正在加载..");
			btn_load_more.setEnabled(false);
			getStroeGoods();
			break;
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;
		case R.id.store_collect:
			collect();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		ActivityUtils.finish(this);
	}

	private void collect() {
		if (App.user != null) {
			RequestParams params = new RequestParams();
			if (hasCollect) {
				params.addBodyParameter(Config.KEY_ACTION,
						Config.ACTION_CANCLE_COLLECT_STORE);
			} else {
				params.addBodyParameter(Config.KEY_ACTION,
						Config.ACTION_COLLECT_STORE);
			}
//			StringUtils.showToast(StoreActivity.this, ""+store);
			params.addBodyParameter(Config.KEY_USER_ID, "" + store.getUser_id());
			params.addBodyParameter(Config.KEY_STORE_ID,
					"" + store.getStore_id());
			App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							StringUtils.showToast(StoreActivity.this,
									Config.ERROR_UNCONNECTION_INTERNET);
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {

							int status = JsonUtils.getInt(arg0.result,
									Config.KEY_STATUS);
							switch (status) {
							case Config.RESULT_STATUS_SUCCESS:// 收藏成功 或取消成功
								hasCollect = !hasCollect;
								if (hasCollect) {
									StringUtils.showToast(StoreActivity.this,
											Config.SUCCESS_COLLECT_STORE);
								} else {
									StringUtils
											.showToast(
													StoreActivity.this,
													Config.SUCCESS_CANCLE_COLLECT_STORE);
								}
								setCollectionStatus();

								break;
							case Config.STATUS_NO_USER_SESSION:// 用户没有登录
								StringUtils.showToast(StoreActivity.this,
										Config.ERROR_LOGIN_TIMEOUT);
								startActivity(new Intent(StoreActivity.this,
										LoginActivity.class));
								break;
							case Config.STATUS_COLLECTIONGOODS_ERR:// 收藏店铺失败
																	// 或取消收藏失败
								if (hasCollect) {
									StringUtils.showToast(StoreActivity.this,
											Config.ERROR_CANCLE_COLLECT_GOODS);
								} else {
									StringUtils.showToast(StoreActivity.this,
											Config.ERROR_COLLECT_GOODS);
								}

								break;
							default:
								break;
							}

						}
					});
		} else {
			StringUtils.showToast(this, "没有登录，跳转登录界面");
			startActivity(new Intent(this, LoginActivity.class));
		}
	}

	private void setCollectionStatus() {
		if (hasCollect) {
			iv_collect.setImageResource(R.drawable.icon_collect_on);
		} else {
			iv_collect.setImageResource(R.drawable.icon_collect_off);
		}
	}

	private void getCollectionStatus() {
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_GET_STORE_IS_COLLECT);
		StringUtils.showToast(StoreActivity.this, ""+store.getStore_id());
		params.addBodyParameter(Config.KEY_STORE_ID, "" + store.getStore_id());
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == 0) {// 获取收藏的状态
							hasCollect = true;
							setCollectionStatus();
						}
					}
				});
	}
}