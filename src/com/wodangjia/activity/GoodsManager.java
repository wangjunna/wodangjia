package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.example.wodangjialayout.R.anim;
import com.example.wodangjialayout.R.id;
import com.example.wodangjialayout.R.layout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.bean.Item;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.dialog.CustomProgressDialog;
import com.wodangjia.pultorefresh.PullToRefreshLayout;
import com.wodangjia.pultorefresh.PullToRefreshLayout.OnRefreshListener;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

public class GoodsManager extends BaseActivity implements OnClickListener{
	private TextView add_goods;
	private ListView listView;
	private ArrayList<View_Goods> itemlist;
	private LayoutInflater mInflater;
	GoodsManagerListViewAdapter adapter;
	CustomProgressDialog dialog ;
	BitmapUtils bitmapUtils;
	int page=0;
	int size=10;
	int goods_status=0;
	private boolean refresh = false;// 当前为刷新 还是加载
	private int result_size;// 最后一次获取商品的个数
	private RadioGroup radioGroup;
	PullToRefreshLayout pullToRefreshLayout;
	
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!isLoginStatus){
			finish();
			return;
		}
		setContentView(R.layout.activity_goods_manager);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_goods_manager);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initData();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		add_goods=(TextView) findViewById(R.id.iv_add_goods);
		add_goods.setOnClickListener(this);
		listView=(ListView) findViewById(R.id.goods_manager_listview);
		mInflater=LayoutInflater.from(this) ;
		listView.setAdapter(adapter);
		radioGroup=(RadioGroup) findViewById(R.id.goods_manager_title_radiogroup);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId==R.id.goods_manager_title_onsale) {
					goods_status=0;
					
				}else if (checkedId==R.id.goods_manager_title_offsale) {
					goods_status=1;
				}
				page=0;//清空列表
				itemlist.clear();
				download();
			}
		});
		download();
		pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
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
				StringUtils.showToast(GoodsManager.this, ""
						+ result_size);
				StringUtils.showToast(GoodsManager.this, ""
						+ (result_size < size));
				if (result_size < size) {
					StringUtils.showToast(GoodsManager.this,
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
		bitmapUtils=new BitmapUtils(this);
		itemlist=new ArrayList<View_Goods>();
		adapter=new GoodsManagerListViewAdapter();
		dialog=new CustomProgressDialog(this, "正在加载，请稍候", R.anim.frame);
		
		
	}
	void download(){
		dialog.show();
		RequestParams param=new RequestParams();
		param.addBodyParameter(Config.KEY_ACTION,Config.ACTION_USER_GOODS_LIST);
		param.addBodyParameter(Config.KEY_TYPE,"0");
		param.addBodyParameter(Config.KEY_PAGE, ""+page);
		param.addBodyParameter(Config.KEY_SIZE,""+size);
		param.addBodyParameter(Config.KEY_STATUS, ""+goods_status);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, param,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dialog.dismiss();
						StringUtils.showToast(GoodsManager.this,
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
						StringUtils.showToast(GoodsManager.this,
								"连接服务器成功！");
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
							adapter.notifyDataSetChanged();
							result_size = goodsList.size();
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_add_goods:
			startActivity(new Intent(this,AddGoodsActivity.class));
			break;
		default:
			break;
		}
		
	}
	class GoodsManagerListViewAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.goods_manager_item_view, null);
				viewHolder.item_rl=(RelativeLayout) convertView.findViewById(R.id.item_rl);
				viewHolder.iv_img=(ImageView) convertView.findViewById(R.id.item_img);
				viewHolder.tv_title=(TextView) convertView.findViewById(R.id.item_title);
				viewHolder.tv_price=(TextView) convertView.findViewById(R.id.item_price);
				viewHolder.tv_stock=(TextView) convertView.findViewById(R.id.item_stock);
				viewHolder.tv_addtime=(TextView) convertView.findViewById(R.id.item_addtime);
				viewHolder.tv_collect=(TextView) convertView.findViewById(R.id.item_collect);
				viewHolder.tv_salse=(TextView) convertView.findViewById(R.id.item_sales);
				viewHolder .btn_onsale_or_offsale=(Button) convertView.findViewById(R.id.onsale_or_offsale);
				viewHolder .btn_preview=(Button) convertView.findViewById(R.id.preview);
				viewHolder .btn_edit=(Button) convertView.findViewById(R.id.edit);
				viewHolder .btn_delete=(Button) convertView.findViewById(R.id.delete);
				convertView.setTag(viewHolder);
			}
			else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
			 final View_Goods item=itemlist.get(position);
			System.out.println("position"+position);
			viewHolder.tv_title.setText(item.getGoods_title());
			viewHolder.tv_price.setText("价格 "+item.getGoods_price());
			viewHolder.tv_stock.setText("库存 "+item.getGoods_stock());
			viewHolder.tv_addtime.setText("添加 "+item.getGoods_create_time());
			viewHolder.tv_collect.setText("收藏 "+item.getGoods_sales());
			viewHolder.tv_salse.setText("销量 "+item.getGoods_sales());
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading);
			bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
			bitmapUtils.display(viewHolder.iv_img, Config.SERVER_HOST+item.getGoods_imgs().get(0).getPath());
//			viewHolder.item_rl.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent=new Intent(GoodsManager.this,ItemDetails.class);
//					intent.putExtra(Config.KEY_ITEM, (View_Goods)itemlist.get(position));
//					GoodsManager.this.startActivityForResult(intent,Config.REQUEST_CODE_EDIT);
//				}
//			});
			if(item.getGoods_status()==0){
				viewHolder.btn_onsale_or_offsale.setText("下架");
			}else{
				viewHolder.btn_onsale_or_offsale.setText("上架");
			}
			viewHolder.btn_onsale_or_offsale.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(item.getGoods_status()==0){
						updateGoods(item,Config.OPERATION__OFFSALE,position);
					}else {
						updateGoods(item,Config.OPERATION__ONSALE,position);
					}
				}
			});
			viewHolder.btn_preview.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(GoodsManager.this, ItemDetails.class);
					intent.putExtra("preview",true);
					intent.putExtra(Config.KEY_ITEM,item);
					startActivity(intent);
					
				}
			});
			viewHolder.btn_edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(GoodsManager.this, EditGoodsActivity.class);
					intent.putExtra(Config.KEY_ITEM,item);
					startActivity(intent);
				}
			});
			viewHolder.btn_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateGoods(item, Config.OPERATION__DELETE,position);
				}
			});
			
			
			return convertView;
		}
		
		void updateGoods(final View_Goods item,final String operation,final int index){
			RequestParams params=new RequestParams();
			params.addBodyParameter(Config.KEY_OPERATION,operation);
			params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_UPDATE_GOOD);
			params.addBodyParameter(Config.KEY_GOODS_ID, ""+item.getGoods_id());
			App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					StringUtils.showToast(GoodsManager.this, Config.ERROR_UNCONNECTION_INTERNET);
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
					if(status==0){
						switch (operation) {
						case Config.OPERATION__ONSALE:
							itemlist.remove(index);
							break;
						case Config.OPERATION__OFFSALE:
							itemlist.remove(index);
							break;
						case Config.OPERATION__DELETE:
							itemlist.remove(index);
							break;
						default:
							break;
						}
						adapter.notifyDataSetChanged();
					}else {
						StringUtils.showToast(GoodsManager.this, "操作失败");
					}
				}
			});
		}

	
		
	}
	class ViewHolder{
		RelativeLayout item_rl;
		ImageView iv_img;
		TextView tv_title;
		TextView tv_price;
		TextView tv_salse;
		TextView tv_stock;
		TextView tv_addtime;
		TextView tv_collect;
		Button btn_onsale_or_offsale;
		Button btn_preview;
		Button btn_edit;
		Button btn_delete;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	public void onBackPressed() {
			ActivityUtils.finish(this);
		
	};

}
