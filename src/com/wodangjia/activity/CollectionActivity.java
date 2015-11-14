package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.ItemCollectListviewAdapter;
import com.wodangjia.adapter.ItemListAdapter;
import com.wodangjia.adapter.StoreColectItemAdapter;
import com.wodangjia.adapter.StoreItemAdapter;
import com.wodangjia.adapter.ViewPagerAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.Address;
import com.wodangjia.bean.View_CollectionGoods;
import com.wodangjia.bean.View_CollectionStore;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_Store;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;

public class CollectionActivity extends BaseActivity implements OnClickListener{

	private RadioGroup rag_collect;
	private RadioButton select_goods, select_store;
	private ViewPager viewPager;
	private int gpage, spage;
	LayoutInflater mInflater;
	private int size = 10;
	ArrayList<View> viewlist;
	ArrayList<View_Goods> goodslist;
	ArrayList<View_CollectionStore> storelist;
	ListView goodsListView, storelListView;
	ItemCollectListviewAdapter goodsAdapter;
	StoreColectItemAdapter storeadAdapter;
	private Button goods_load_more, store_load_more;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!isLoginStatus){
			finish();
			return;
		}
		setContentView(R.layout.activity_collection);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_collection);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out); 
		initData();
		initView();
		downloadGoods();
		downloadStore();
	}

	private void downloadGoods() {
		goods_load_more.setText("正在加载...");
		goods_load_more.setEnabled(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_GET_COLLECTION_GOODS);
		params.addBodyParameter(Config.KEY_PAGE, "" + gpage);
		params.addBodyParameter(Config.KEY_SIZE, "" + size);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						goods_load_more.setText("加载失败");
						goods_load_more.setEnabled(true);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == 0) {
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<View_CollectionGoods>>() {
							}.getType();
							ArrayList<View_CollectionGoods> goods = gson
									.fromJson(
											JsonUtils.getJsonArray(arg0.result,
													Config.KEY_GOODSLIST)
													.toString(), type);
							for (int i = 0; i < goods.size(); i++) {
								goodslist.add(goods.get(i).getItem());
							}
							goodsAdapter.setItems(goodslist);
							changeLoadMoreStatus(0, goodslist.size());
						}

					}
				});

	}

	private void downloadStore() {
		store_load_more.setText("正在加载...");
		store_load_more.setEnabled(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_GET_COLLECTION_STORE);
		params.addBodyParameter(Config.KEY_PAGE, "" + spage);
		params.addBodyParameter(Config.KEY_SIZE, "" + size);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						store_load_more.setText("加载失败");
						store_load_more.setEnabled(true);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == 0) {
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<View_CollectionStore>>() {
							}.getType();
							ArrayList<View_CollectionStore> stores = gson.fromJson(
									JsonUtils.getJsonArray(arg0.result,
											Config.KEY_STORELIST).toString(),
									type);
							storelist.addAll(stores);
							changeLoadMoreStatus(1, stores.size());
						}

					}
				});

	}

	private void initData() {
		viewlist = new ArrayList<View>();
		mInflater = LayoutInflater.from(this);
		goodslist = new ArrayList<View_Goods>();
		storelist = new ArrayList<View_CollectionStore>();
		goodsAdapter = new ItemCollectListviewAdapter(goodslist, this);
		storeadAdapter = new StoreColectItemAdapter(this, storelist);
	}

	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		rag_collect = (RadioGroup) findViewById(R.id.rag_collect);
		select_goods = (RadioButton) findViewById(R.id.select_goods);
		select_store = (RadioButton) findViewById(R.id.select_store);
		viewPager = (ViewPager) findViewById(R.id.collect_viewpager);
		View viewgoods = mInflater.inflate(R.layout.collect_listview, null);
		goodsListView = (ListView) viewgoods.findViewById(R.id.listview);
		goodsListView.setAdapter(goodsAdapter);

		View viewstore = mInflater.inflate(R.layout.collect_listview, null);
		storelListView = (ListView) viewstore.findViewById(R.id.listview);
		storelListView.setAdapter(storeadAdapter);
		viewlist.add(viewgoods);
		viewlist.add(viewstore);
		LinearLayout footer1 = (LinearLayout) mInflater.inflate(
				R.layout.comment_listview_footer, null);
		goods_load_more = (Button) footer1.findViewById(R.id.load_more);
		goods_load_more.setText("正在加载...");
		goods_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadGoods();
			}
		});
		goodsListView.addFooterView(footer1);
		LinearLayout footer2 = (LinearLayout) mInflater.inflate(
				R.layout.comment_listview_footer, null);
		store_load_more = (Button) footer2.findViewById(R.id.load_more);
		store_load_more.setText("正在加载...");
		store_load_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadStore();
			}
		});
		storelListView.addFooterView(footer2);

		viewPager.setAdapter(new ViewPagerAdapter(viewlist));
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					select_goods.setChecked(true);
				} else {
					select_store.setChecked(true);
				}
			}

		});
		rag_collect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.select_goods:
//					downloadGoods();
//					goodslist.clear();
					viewPager.setCurrentItem(0);
					break;
				default:
//					downloadStore();
//					storelist.clear();
					viewPager.setCurrentItem(1);
					break;
				}
			}
		});
		select_goods.setChecked(true);
	}

	protected void changeLoadMoreStatus(int type, int count) {
		if (type == 0) {
			if (count == 0 && spage == 0) {
				goods_load_more.setText("暂无收藏商品");
			} else if (count < size) {
				goods_load_more.setText("已无更多商品");
			} else {
				goods_load_more.setText("更多收藏商品");
				goods_load_more.setEnabled(true);
				gpage++;
			}
		} else {
			if (count == 0 && spage == 0) {
				store_load_more.setText("暂无收藏店铺");
			} else if (count < size) {
				store_load_more.setText("已无更多店铺");
			} else {
				store_load_more.setText("更多收藏店铺");
				store_load_more.setEnabled(true);
				spage++;
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;

		default:
			break;
		}
		
	}
	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	}
}
