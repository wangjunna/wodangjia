package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.ItemListAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.dialog.CustomProgressDialog;
import com.wodangjia.dto.SpinnerGoodsDto;
import com.wodangjia.dto.SpinnerSortDto;
import com.wodangjia.pulltorefresh.pullableview.PullableListView;
import com.wodangjia.pultorefresh.PullToRefreshLayout;
import com.wodangjia.pultorefresh.PullToRefreshLayout.OnRefreshListener;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

public class CategoryActivity extends Activity implements
		OnItemSelectedListener,OnClickListener{

	private Spinner spinnerGoods, spinnerSchool, spinnerSort;// 商品，学校，排序的spinner
	private int page;// 记录评论当前加载的页数
	private int size = 10;// 每次获取的条数
	private boolean refresh = false;// 当前为刷新 还是加载
	private boolean isFirst=true;
	private int result_size;// 最后一次获取商品的个数
	private int currentType, currentSort;
	private String currentSchool;
	private PullableListView category_item_listview;
	private ArrayList<String> schooList;
	private ArrayList<View_Goods> itemlist;
	private ItemListAdapter listviewAdapter;
	PullToRefreshLayout pullToRefreshLayout;

	ArrayAdapter<String> adapterGoods, adapterSchool, adapterSort;
	private CustomProgressDialog dialog;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_goods);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setActionBarLayout(getActionBar(), this, R.layout.title_bar_category);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		initView();
		initData();
	}

	private void initData() {
	
		System.out.println(getIntent().getIntExtra(Config.KEY_TYPE,0));
		adapterGoods = new ArrayAdapter<String>(this,
				R.layout.spinner_category_item, R.id.category_simple_item,
				SpinnerGoodsDto.getList());
		
		spinnerGoods.setAdapter(adapterGoods);
		currentType=getIntent().getIntExtra(Config.KEY_TYPE,0);
		spinnerGoods.setSelection(currentType);//传来类别
		
		spinnerGoods.setOnItemSelectedListener(this);
		adapterSort = new ArrayAdapter<String>(this,
				R.layout.spinner_category_item, R.id.category_simple_item,
				SpinnerSortDto.getList());
		spinnerSort.setAdapter(adapterSort);
		spinnerSort.setOnItemSelectedListener(this);
		schooList = new ArrayList<String>();
		schooList.add("全部学校");
		adapterSchool = new ArrayAdapter<String>(CategoryActivity.this,
				R.layout.spinner_category_item, R.id.category_simple_item,
				schooList);
		spinnerSchool.setAdapter(adapterSchool);
		spinnerSchool.setOnItemSelectedListener(this);
		loadSpinnerSchool();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		dialog=new CustomProgressDialog(this, "正在加载，请稍候", R.anim.frame);
		dialog.show();
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.buy_title_shoppingcart).setOnClickListener(this);
		spinnerGoods = (Spinner) findViewById(R.id.category_goods);
		spinnerSchool = (Spinner) findViewById(R.id.category_school);
		spinnerSort = (Spinner) findViewById(R.id.category_sort);
		category_item_listview = (PullableListView) findViewById(R.id.category_item_listview);
		itemlist = new ArrayList<View_Goods>();
		listviewAdapter = new ItemListAdapter(itemlist, this);
		category_item_listview.setAdapter(listviewAdapter);
		pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				refresh = true;
				// pullToRefreshLayout.refreshFinish(PullToRefreshLayout.REFRESHING);
				page = 0;
				itemlist.clear();
				downloadData();

			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				refresh = false;
				StringUtils.showToast(CategoryActivity.this, "" + result_size);
				StringUtils.showToast(CategoryActivity.this, ""
						+ (result_size < size));
				if (result_size < size) {
					StringUtils.showToast(CategoryActivity.this,
							Config.MSG_NO_MORE);
					pullToRefreshLayout
							.loadmoreFinish(pullToRefreshLayout.NOMORE);
				} else {
					downloadData();
				}

			}
		});
	}

	void loadSpinnerSchool() {
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_GET_SCHOOL_LIST);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils.showToast(CategoryActivity.this,
								Config.ERROR_UNCONNECTION_INTERNET);

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == 0) {
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<String>>() {
							}.getType();
							ArrayList<String> list = gson.fromJson(
									JsonUtils.getJsonArray(arg0.result,
											Config.KEY_SCHOOLLIST).toString(),
									type);
							schooList.addAll(list);
							adapterSchool.notifyDataSetChanged();
							downloadData();
							
						}

					}
				});
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		if (parent.equals(spinnerGoods)) {
//			StringUtils.showToast(this, "spinnerGoods---" + spinnerGoods.getSelectedItem().toString());
			currentType = position;
		} else if (parent.equals(spinnerSchool)) {
//			StringUtils.showToast(this, "spinnerSchool---" + position);
			currentSchool = spinnerSchool.getSelectedItem().toString();
		} else if (parent.equals(spinnerSort)) {
//			StringUtils.showToast(this, "spinnerSort---" + position);
			currentSort = position;
		}
		if(isFirst){
			return;
		}else{
			refresh = true;
			page = 0;
			itemlist.clear();
			downloadData();
		}
		

	}

	private void downloadData() {
		dialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_CATEGORY_GOODSLIST);
		params.addBodyParameter(Config.KEY_TYPE, "" + currentType);
		params.addBodyParameter(Config.KEY_SCHOOL, currentSchool);
		params.addBodyParameter(Config.KEY_SORT, "" + currentSort);
		params.addBodyParameter(Config.KEY_PAGE, "" + page);
		params.addBodyParameter(Config.KEY_SIZE, "" + size);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (refresh) {
							pullToRefreshLayout
									.refreshFinish(PullToRefreshLayout.FAIL);
						} else {
							pullToRefreshLayout
									.loadmoreFinish(PullToRefreshLayout.FAIL);
						}
						dialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == Config.RESULT_STATUS_SUCCESS) {
							ArrayList<View_Goods> goodsList;
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<View_Goods>>() {
							}.getType();
							goodsList = gson.fromJson(
									JsonUtils.getJsonArray(arg0.result,
											Config.KEY_GOODSLIST).toString(),
									type);
							itemlist.addAll(goodsList);
							listviewAdapter.notifyDataSetChanged();
							result_size = goodsList.size();
							page++;
							if (refresh) {
								pullToRefreshLayout
										.refreshFinish(PullToRefreshLayout.SUCCEED);
							} else {
								pullToRefreshLayout
										.loadmoreFinish(PullToRefreshLayout.SUCCEED);
							}
							dialog.dismiss();
							isFirst=false;
						}

					}
				});

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;
		case R.id.buy_title_shoppingcart:
			startActivity(new Intent(this, ShoppingCartActivity.class));
			break;
		default:
			break;
		}
		
	}
}
