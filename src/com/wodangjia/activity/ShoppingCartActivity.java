package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.ShoppingCartAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.Item;
import com.wodangjia.bean.ShoppingCartItem;
import com.wodangjia.bean.View_Comment;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_ShoppingCartItem;
import com.wodangjia.pulltorefresh.pullableview.PullableListView;
import com.wodangjia.pultorefresh.PullToRefreshLayout;
import com.wodangjia.pultorefresh.PullToRefreshLayout.OnRefreshListener;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.NoScrollListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ShoppingCartActivity extends BaseActivity implements OnClickListener {
	NoScrollListView cartItemListView;
	ArrayList<View_ShoppingCartItem> itemlist;
	private ShoppingCartAdapter adapter;
	private CheckBox checkBoxAll;// 全选按钮
	private TextView checkBoxAllPrice;// 所有被选中商品的总金额
	private int page = 0;// 记录当前加载的页数
	private int size = 10;// 每次获取的条数
	private Button btn_load_more;
	private LayoutInflater mInflater;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!isLoginStatus){
			finish();
			return;
		}
		setContentView(R.layout.activity_shopping_cart);
		
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_shopping_cart);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);

		initView();
		initData();
		downloadData();
	}

	private void initView() {
		mInflater=LayoutInflater.from(this);
		LinearLayout footer=(LinearLayout) mInflater.inflate(R.layout.comment_listview_footer, null);
		btn_load_more=(Button) footer.findViewById(R.id.load_more);
		btn_load_more.setText("正在加载商品信息...");
		btn_load_more.setOnClickListener(this);
		cartItemListView = (NoScrollListView) findViewById(R.id.shopping_cart_listview);
		cartItemListView.addFooterView(footer);
		findViewById(R.id.btn_back).setOnClickListener(this);
		checkBoxAll = (CheckBox) findViewById(R.id.shopping_cart_all_choose);
		checkBoxAllPrice = (TextView) findViewById(R.id.shopping_cart_all_price);
		checkBoxAll.setOnClickListener(this);
		findViewById(R.id.shopping_cart_delete).setOnClickListener(this);
		findViewById(R.id.tv_settlement).setOnClickListener(this);

	}

	private void initData() {
		itemlist = new ArrayList<View_ShoppingCartItem>();
		adapter = new ShoppingCartAdapter(ShoppingCartActivity.this, itemlist);
		cartItemListView.setAdapter(adapter);

	}

	private void downloadData() {
		btn_load_more.setText("正在加载购物车商品...");
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_SHOPPING_CART_INFO);
		params.addBodyParameter(Config.KEY_PAGE, "" + page);
		params.addBodyParameter(Config.KEY_SIZE, "" + size);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils.showToast(ShoppingCartActivity.this,
								Config.ERROR_UNCONNECTION_INTERNET);
						btn_load_more.setEnabled(true);
						btn_load_more.setText("加载购物车失败！");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						System.out.println(arg0.result);
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						switch (status) {
						case Config.RESULT_STATUS_SUCCESS:
							StringUtils.showToast(ShoppingCartActivity.this,
									"连接服务器成功！");
							ArrayList<View_ShoppingCartItem> items;
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<View_ShoppingCartItem>>() {
							}.getType();
							items = gson.fromJson(
									JsonUtils.getJsonArray(arg0.result,
											Config.KEY_CARTITEMLIST).toString(),
									type);
							System.out.println(items);
							itemlist.addAll(items);
							adapter.setList(itemlist);
							changeLoadMoreStatus(items.size());
							adapter.notifyDataSetChanged();
							break;
//						case -1:
//							StringUtils.showToast(ShoppingCartActivity.this, "登录过期，请重新登录！");
//							btn_load_more.setText("加载商品失败！");
//							btn_load_more.setEnabled(true);
//							startActivity(new Intent(ShoppingCartActivity.this,LoginActivity.class));
//							break;
						default:
							btn_load_more.setText("加载商品失败！");
							btn_load_more.setEnabled(true);
							break;
						}
					}
				});
	}

	protected void changeLoadMoreStatus(int count) {
		// TODO Auto-generated method stub
		if (count == 0 && page == 0) {
			btn_load_more.setText("暂无商品");
			// refreshListView.showFooterView();

		} else if (count < size) {
			btn_load_more.setText("已无更多商品");
			// refreshListView.showFooterView();
		} else {
			page++;
			btn_load_more.setText("加载更多..");
			btn_load_more.setEnabled(true);
		}
	}

	private void deleteShoppingDoods(final View_ShoppingCartItem item) {
		
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,Config.ACTION_UPADE_SHOPPING_CART);
		params.addBodyParameter(Config.KEY_GOODS_ID,""+item.getItem().getGoods_id());
		params.addBodyParameter(Config.KEY_OPERATION,""+Config.SHOPPING_CART_DELETE);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						adapter.getList().remove(item);
						adapter.notifyDataSetChanged();
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.load_more:
			btn_load_more.setText("正在加载购物车...");
			btn_load_more.setEnabled(false);
			downloadData();
			break;
		case R.id.btn_back:
			ActivityUtils.finish(ShoppingCartActivity.this);
			break;
		case R.id.shopping_cart_all_choose:
			for (int i = 0; i < itemlist.size(); i++) {
				itemlist.get(i).setChecked(checkBoxAll.isChecked());
			}
			adapter.changeCheckBoxAllPrice();
			adapter.notifyDataSetChanged();
			break;
		case R.id.shopping_cart_delete:
			for (int i = 0; i < itemlist.size(); i++) {
				if (itemlist.get(i).isChecked()) {
					deleteShoppingDoods(itemlist.get(i));
				}
			}
			break;
			
		case R.id.tv_settlement://结算
			settlement();
			break;
		default:
			break;
		}

	}

	private void settlement() {
		ArrayList<View_ShoppingCartItem> alllist=adapter.getList();
		ArrayList<View_ShoppingCartItem> checklist=new ArrayList<View_ShoppingCartItem>();
		for (int i = 0; i < alllist.size(); i++) {
			if(alllist.get(i).isChecked()){
				checklist.add(alllist.get(i));
			}
		}
		if(alllist.size()==0){
			StringUtils.showToast(this,"没有选中任何商品");
			return;
		}
		Intent intent=new Intent(this,SubmitOrderActivity.class);
		intent.putExtra("itemlist", checklist);
		startActivity(intent);
	}

	public CheckBox getCheckBoxAll() {
		return checkBoxAll;
	}

	public TextView getCheckBoxAllPrice() {
		return checkBoxAllPrice;
	}
	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	}
}
