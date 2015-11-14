package com.wodangjia.activity;

import io.rong.imkit.RongIM;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
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
import com.wodangjia.adapter.ItemCommentListAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.View_Comment;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_ShoppingCartItem;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.zf.iosdialog.widget.CustomAlertDialog;

public class ItemDetails extends Activity implements OnClickListener{

	private ViewPager item_vp_imgs;
	private List<View> imgs_view;
	private LayoutInflater mInflater;
	private BitmapUtils bitmapUtils;
	private View_Goods item;
	private ListView commentListView;
	private ArrayList<View_Comment> commentlist;
	private ItemCommentListAdapter adapter;
	private int page;//记录评论当前加载的页数
	private int size=10;//每次获取的条数
	private Button btn_load_more;
	private ImageView iv_collect;
	private boolean hasCollect;
	private int count=1;
	private TextView tv_title,tv_subtitle,tv_price,tv_collect,tv_sales;
	private RatingBar ratingBar;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_details);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.statusbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out); 
		initData();
		initView();
		download();
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.load_more:
			btn_load_more.setText("正在加载评论");
			btn_load_more.setEnabled(false);
			download();
			break;
		case R.id.ll_share:
			share();
			break;
		case R.id.rl_chat:
			if(App.user!=null){
				App.connect(App.user.getTooken());
			if (RongIM.getInstance() != null)
                RongIM.getInstance().startPrivateChat(this, ""+item.getUser_id(), "title");
			}else {
				
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.rl_store:
			Intent intent=new Intent(ItemDetails.this,StoreActivity.class);
			intent.putExtra(Config.KEY_USER_ID,item.getUser_id());
			startActivity(intent);
			break;
		case R.id.rl_collect:
			collect();
			break;
		case R.id.tv_add:
			addToShoppingCart();
			break;
		case R.id.tv_buy:
			showCountDialog();
			break;
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;
		case R.id.shopping_card:
			startActivity(new Intent(this, ShoppingCartActivity.class));
			break;
		default:
			break;
		}
		
	}

	private void share() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "商品："+item.getGoods_title()+"\n价格：￥"+item.getGoods_price()+"\n《我当家》 app下载地址："+Config.APP_DOWNLOAD_URL);
		startActivity(Intent.createChooser(intent, "商品分享"));
		
	}
	private void addToShoppingCart() {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_SHOPPING_CART);
		params.addBodyParameter(Config.KEY_GOODS_ID,""+item.getGoods_id());
		params.addBodyParameter(Config.KEY_OPERATION,""+Config.SHOPPING_CART_ADD);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(ItemDetails.this, Config.ERROR_UNCONNECTION_INTERNET);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result,Config.KEY_STATUS);
					if(status==0){
						StringUtils.showToast(ItemDetails.this, "加入购物车成功！");
					}else if(status==1){
						StringUtils.showToast(ItemDetails.this, "加入购物车失败！");
					}else if(status==-1){
						startActivity(new Intent(ItemDetails.this, LoginActivity.class));
					}
				
			}
		});
		
	}
	private void download() {
		btn_load_more.setText("正在加载评论...");
		RequestParams params =new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_GET_GOODS_COMMENT);
		params.addBodyParameter(Config.KEY_GOODS_ID, ""+item.getGoods_id());
		params.addBodyParameter(Config.KEY_PAGE, ""+page);
		params.addBodyParameter(Config.KEY_SIZE, ""+size);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>(){

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(ItemDetails.this, Config.ERROR_UNCONNECTION_INTERNET);
				btn_load_more.setEnabled(true);
				btn_load_more.setText("加载评论失败！");
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result,Config.KEY_STATUS);
				switch (status) {
				case Config.RESULT_STATUS_SUCCESS:
					StringUtils.showToast(ItemDetails.this, "连接服务器成功！");
					ArrayList<View_Comment> comments;
					Gson gson=new Gson();
					Type type =new TypeToken<ArrayList<View_Comment>>(){}.getType();
					comments=gson.fromJson(JsonUtils.getJsonArray(arg0.result, Config.KEY_COMMENTLIST).toString(), type);
					System.out.println(comments);
					commentlist.addAll(comments);
					adapter.setCommentlist(commentlist);
					changeLoadMoreStatus(comments.size());
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
			}});
		
	}

	protected void changeLoadMoreStatus(int count) {
		if(page==0&&App.user!=null){
			getCollectionStatus();
		}
		if(count==0&&page==0){
			btn_load_more.setText("暂无评论");
			
		}
		else if(count<size){
			btn_load_more.setText("已无更多评论");
		}
		else {
			btn_load_more.setText("更多评论");
			btn_load_more.setEnabled(true);
			page++;
		}
		
	}

	private void setCollectionStatus(){
		if(hasCollect){
			iv_collect.setImageResource(R.drawable.icon_collected);
		}else{
			iv_collect.setImageResource(R.drawable.my_collection);
		}
	}
	private void getCollectionStatus() {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,Config.ACTION_GET_GOODS_IS_COLLECT);
		params.addBodyParameter(Config.KEY_GOODS_ID,""+item.getGoods_id());
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status= JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){//获取收藏的状态
					hasCollect=true;
					setCollectionStatus();
				}
			}
		});
		
	}

	private void initData() {
		bitmapUtils=new BitmapUtils(this);
		mInflater=LayoutInflater.from(this);
		imgs_view=new ArrayList<View>();
		commentlist=new ArrayList<View_Comment>();
		adapter=new ItemCommentListAdapter(this, commentlist);
		Intent intent=getIntent();
		//获得上级activity传来的数据
		item=(View_Goods) intent.getSerializableExtra(Config.KEY_ITEM);
		
	}

	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.shopping_card).setOnClickListener(this);
		findViewById(R.id.ll_share).setOnClickListener(this);
		findViewById(R.id.rl_chat).setOnClickListener(this);
		findViewById(R.id.rl_store).setOnClickListener(this);
		findViewById(R.id.rl_collect).setOnClickListener(this);
		findViewById(R.id.tv_add).setOnClickListener(this);
		findViewById(R.id.tv_buy).setOnClickListener(this);
		ratingBar=(RatingBar) findViewById(R.id.star);
		iv_collect=(ImageView) findViewById(R.id.iv_collect);
		ratingBar.setRating((float) item.getStar());
		tv_title=(TextView) findViewById(R.id.item_title);
		tv_title.setText(item.getGoods_title());
		tv_subtitle=(TextView) findViewById(R.id.item_subtitle);
		tv_subtitle.setText(item.getGoods_subtitle());
		tv_price=(TextView) findViewById(R.id.item_price);
		tv_price.setText("￥"+item.getGoods_price());
		tv_collect=(TextView) findViewById(R.id.item_collect);
		tv_collect.setText("收藏 "+item.getCollections());
		tv_sales=(TextView) findViewById(R.id.item_sales);
		tv_sales.setText("销量 "+item.getGoods_sales());
		commentListView=(ListView) findViewById(R.id.comment_listview);
		commentListView.setAdapter(adapter);
		LinearLayout footer=(LinearLayout) mInflater.inflate(R.layout.comment_listview_footer, null);
		btn_load_more=(Button) footer.findViewById(R.id.load_more);
		btn_load_more.setText("正在加载评论");
		btn_load_more.setOnClickListener(this);
		commentListView.addFooterView(footer);
		item_vp_imgs=(ViewPager) findViewById(R.id.item_vp_imgs);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		LayoutParams params=item_vp_imgs.getLayoutParams();
		params.height=screenWidth;
		item_vp_imgs.setLayoutParams(params);
		
		
		for(int i=0;i<item.getGoods_imgs().size();i++){
			View view=mInflater.inflate(R.layout.item_img, null);
			ImageView imageView=(ImageView) view.findViewById(R.id.item_img);
			
			//设置需要显示的图片   加载中的图片 加载失败的图片     《需替换默认图片》
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading);
			bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
			bitmapUtils.display(imageView,Config.SERVER_HOST+item.getGoods_imgs().get(i).getPath());
			imgs_view.add(view);
		}
		
		item_vp_imgs.setAdapter(new VpAdapter());
	}
	
	class VpAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 当前界面显示的是哪一个视图
			View view = imgs_view.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imgs_view.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			// 销毁当前页视图
			View view = imgs_view.get(position);
			container.removeView(view);
		}
	}

	

	private void collect() {
		if(App.user!=null){
			RequestParams params=new RequestParams();
			if(hasCollect){
				params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_CANCLE_COLLECT_GOODS);
			}else {
				params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_COLLECT_GOODS);
			}
			
			params.addBodyParameter(Config.KEY_GOODS_ID, ""+item.getGoods_id());
			params.addBodyParameter(Config.KEY_USER_ID, ""+item.getGoods_id());
			App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					StringUtils.showToast(ItemDetails.this, Config.ERROR_UNCONNECTION_INTERNET);
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					
					int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
					switch (status) {
					case Config.RESULT_STATUS_SUCCESS://收藏成功  或取消成功
						hasCollect=!hasCollect;
						if(hasCollect){
							StringUtils.showToast(ItemDetails.this, Config.SUCCESS_COLLECT_GOODS);
						}else {
							StringUtils.showToast(ItemDetails.this, Config.SUCCESS_CANCLE_COLLECT_GOODS);
						}
						setCollectionStatus();
						
						break;
					case Config.STATUS_NO_USER_SESSION://用户没有登录
						StringUtils.showToast(ItemDetails.this, Config.ERROR_LOGIN_TIMEOUT);
						startActivity(new Intent(ItemDetails.this, LoginActivity.class));
						break;
					case Config.STATUS_COLLECTIONGOODS_ERR://收藏商品失败 或取消收藏失败
						if(hasCollect){
							StringUtils.showToast(ItemDetails.this, Config.ERROR_CANCLE_COLLECT_GOODS);
						}else {
							StringUtils.showToast(ItemDetails.this, Config.ERROR_COLLECT_GOODS);
						}
							
						break;
					default:
						break;
					}
					
				}
			});
		}else{
			StringUtils.showToast(this, "没有登录，跳转登录界面");
		}
		
	}
	private void showCountDialog() {

		RelativeLayout layout = (RelativeLayout) mInflater.inflate(
				R.layout.dialog_select_num, null);
		TextView tv_stock=(TextView) layout.findViewById(R.id.tv_stock);
		tv_stock.setText(""+item.getGoods_stock());
		final TextView tv_count=(TextView) layout.findViewById(R.id.tv_count);
		ImageView iv_des=(ImageView) layout.findViewById(R.id.iv_des);
		ImageView iv_add=(ImageView) layout.findViewById(R.id.iv_add);
		tv_count.setText(""+count);
		iv_des.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(count>1){
					count--;
				}
				tv_count.setText(""+count);
			}
		});
		iv_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(count<item.getGoods_stock()){
					count++;
				}
				tv_count.setText(""+count);
			}
		});
		final CustomAlertDialog dialog = new CustomAlertDialog(this).builder()
				.setTitle("请选择数量").setView(layout)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (App.user==null) {
					startActivity(new Intent(ItemDetails.this, LoginActivity.class));
					return;
				}
				Intent intent=new Intent(ItemDetails.this,SubmitOrderActivity.class);
				ArrayList<View_ShoppingCartItem> itemlist=new ArrayList<View_ShoppingCartItem>();
				itemlist.add(new View_ShoppingCartItem(item,App.user.getUser_id() , count));
				intent.putExtra("itemlist", itemlist);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		dialog.show();

	}
	
}
