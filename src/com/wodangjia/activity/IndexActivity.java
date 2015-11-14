package com.wodangjia.activity;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import krelve.view.Kanner;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.ItemListAdapter;
import com.wodangjia.adapter.ViewPagerAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.CarouselFigure;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_Store;
import com.wodangjia.fragment.ContactsFragment;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.CircleImageView;
import com.wodangjia.view.NoScrollListView;

public class IndexActivity extends FragmentActivity implements OnClickListener {
	private RadioGroup radioGroup;
	private ViewPager viewPager;
	private android.support.v4.view.ViewPager chatViewPager;
	private ArrayList<View> viewsList;
	private Kanner kanner;
	private View buyView, saleView, chatView, myView;
	private TextView index_my_tv_nickname;
	private Fragment messageFragment, contactsFragment;
	private RadioGroup chatRadioGroup;
	private int chat_index;
	NoScrollListView likelistview;
	private LayoutInflater mInflater;
	int page=0;
	int  size=10;
	ItemListAdapter adapter;
	private Button btn_load_more;
	private BitmapUtils bitmapUtils;
	private TextView tv_today_money,tv_today_order,tv_refund_order;
	ArrayList<CarouselFigure> cfList;//轮播图集合
	private final static long TIME_DIFF = 2 * 1000;//再次点击返回退出的间隔时间
	private long mExitTime;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	private List<View_Goods> itemlist;
	CircleImageView iv_photo;
	int[] radiobutton;
	private boolean chatInit=false;
	private ArrayList<ImageView> storeImageViews;
	private ArrayList<View_Store> storelist;
	PullToRefreshScrollView buyPullToRefreshScrollView,salePullToRefreshScrollView;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		
		ActivityUtils.setActionBarLayout(getActionBar(), IndexActivity.this,
				R.layout.title_bar_index_buy);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		init(); 
		initView();
		initData();
	}

	private void init() {
		storeImageViews=new ArrayList<ImageView>();
		bitmapUtils=new BitmapUtils(this);
		radiobutton=new int[]{R.id.buy,R.id.sale,R.id.chat,R.id.my};
		fragmentManager = getFragmentManager();
		itemlist=new ArrayList<View_Goods>();
		storelist=new ArrayList<View_Store>();
	}

	private void initData() {
		page=0;
		storelist.clear();
		itemlist.clear();
		downloadItemlist();
		if(cfList==null||cfList.size()==0){
			loadCarouselFigure();
		}
		downloadStroeList();

	}

	private void downloadStroeList() {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,Config.ACTION_GET_INDEX_STORE);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(IndexActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					Type type=new TypeToken<ArrayList<View_Store>>(){}.getType();
					Gson gson=new Gson();
					storelist=gson.fromJson(JsonUtils.getJsonArray(arg0.result, Config.KEY_STORELIST).toString(),type );
					if(storelist!=null){
						setStoreToUI();
					}
				}
			}
		});
		
	}

	protected void setStoreToUI() {
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
		for(int i=0;i<storeImageViews.size()&&i<3;i++){
			bitmapUtils.display(storeImageViews.get(i), Config.SERVER_HOST+storelist.get(i).getStore_img_path());
			final int postion=i;
			storeImageViews.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(IndexActivity.this, StoreActivity.class);
					intent.putExtra(Config.KEY_USER_ID,storelist.get(postion).getUser_id());
					startActivity(intent);
				}
			});
		}
	}

	private void downloadItemlist() {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_CATEGORY_GOODSLIST);
		params.addBodyParameter(Config.KEY_SORT, "" + Config.ORDER_BY_GOODS_SALES_DESC);
		params.addBodyParameter(Config.KEY_PAGE, "" + page);
		params.addBodyParameter(Config.KEY_SIZE, "" + size);
		params.addBodyParameter(Config.KEY_STATUS, ""+0);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				btn_load_more.setEnabled(true);
				btn_load_more.setText("加载失败");
				buyPullToRefreshScrollView.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					ArrayList<View_Goods> items= new ArrayList<View_Goods>();
					Gson gson=new Gson();
					Type type = new TypeToken<ArrayList<View_Goods>>() {
					}.getType();
					items = gson.fromJson(JsonUtils.getJsonArray(arg0.result,Config.KEY_GOODSLIST).toString(),type);
					itemlist.addAll(items);
					adapter.notifyDataSetChanged();
					changeLoadMoreStatus(items.size());
					buyPullToRefreshScrollView.onRefreshComplete();
				}
				
			}
		});
		
	}

	private void initView() {

		radioGroup = (RadioGroup) findViewById(R.id.bottom_radiogroup);
		viewsList = new ArrayList<View>();
		viewPager = (ViewPager) findViewById(R.id.content);

		mInflater = LayoutInflater.from(IndexActivity.this);
		buyView = mInflater.inflate(R.layout.index_buy, null);
		saleView = mInflater.inflate(R.layout.index_sale, null);
		chatView = mInflater.inflate(R.layout.index_chat, null);
		myView = mInflater.inflate(R.layout.index_my, null);
		viewsList.add(buyView);
		viewsList.add(saleView);
		viewsList.add(chatView);
		viewsList.add(myView);
		// 初始化saleview中的view
		tv_today_money=(TextView) saleView.findViewById(R.id.today_money);
		tv_today_order=(TextView) saleView.findViewById(R.id.today_order);
		tv_refund_order=(TextView) saleView.findViewById(R.id.refund_order);
		salePullToRefreshScrollView=(PullToRefreshScrollView)saleView.findViewById(R.id.sale_pulltorefreshscrollview);
		salePullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				updateStoreOrder();
			}
		});
		saleView.findViewById(R.id.index_sale_today_money).setOnClickListener(
				this);
		saleView.findViewById(R.id.index_sale_today_refund).setOnClickListener(
				this);
		saleView.findViewById(R.id.index_sale_today_order).setOnClickListener(
				this);
		saleView.findViewById(R.id.index_sale_store_manager)
				.setOnClickListener(this);
		saleView.findViewById(R.id.index_sale_order_manager)
				.setOnClickListener(this);
		saleView.findViewById(R.id.index_sale_goods_manager)
				.setOnClickListener(this);
		saleView.findViewById(R.id.index_sale_store_income).setOnClickListener(
				this);
		saleView.findViewById(R.id.index_sale_wait_send).setOnClickListener(
				this);
		saleView.findViewById(R.id.index_sale_wait_pay)
				.setOnClickListener(this);
		saleView.findViewById(R.id.index_sale_has_send)
				.setOnClickListener(this);
		saleView.findViewById(R.id.index_sale_wait_evaluation)
				.setOnClickListener(this);

		
		// 初始化chatview中的view
		//聊天页面中的pagerview
		chatViewPager = (android.support.v4.view.ViewPager) chatView
				.findViewById(R.id.chat_viewpager);
		if(App.user!=null){
			App.connect(App.user.getTooken());
			chatViewPager.setAdapter(new ChatFragmentPagerAdapter(getSupportFragmentManager()));
			chatInit=true;
		}
		
		chatViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				chat_index=arg0;
				if (arg0==0) {
					chatRadioGroup.check(R.id.chat_title_message);
				
				}
				else{
					chatRadioGroup.check(R.id.chat_title_contacts);
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// 初始化buyview中的view
		buyPullToRefreshScrollView=(PullToRefreshScrollView)buyView.findViewById(R.id.index_buy_scrollview);
		buyPullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				initData();
				
			}
		});
		likelistview=(NoScrollListView) buyView.findViewById(R.id.wo_all_like_listview);
		buyView.findViewById(R.id.type_eat).setOnClickListener(this);
		buyView.findViewById(R.id.type_clothes).setOnClickListener(this);
		buyView.findViewById(R.id.type_daily).setOnClickListener(this);
		buyView.findViewById(R.id.type_other).setOnClickListener(this);
		storeImageViews.add((ImageView)buyView.findViewById(R.id.store_no1));
		storeImageViews.add((ImageView)buyView.findViewById(R.id.store_no2));
		storeImageViews.add((ImageView)buyView.findViewById(R.id.store_no3));
		
		// 初始化myview中的view
		
		index_my_tv_nickname = (TextView) myView
				.findViewById(R.id.index_my_tv_nickname);
		index_my_tv_nickname.setOnClickListener(this);
		iv_photo=(CircleImageView) myView.findViewById(R.id.index_my_iv_photo);
		iv_photo.setOnClickListener(this);
		myView.findViewById(R.id.index_my_rl_address).setOnClickListener(this);
		myView.findViewById(R.id.index_my_rl_buyedgoods).setOnClickListener(
				this);
		myView.findViewById(R.id.index_my_rl_collection).setOnClickListener(
				this);
//		myView.findViewById(R.id.index_my_rl_looked).setOnClickListener(this);
		myView.findViewById(R.id.index_my_rl_password).setOnClickListener(this);
		myView.findViewById(R.id.index_my_rl_phone).setOnClickListener(this);
		myView.findViewById(R.id.index_my_rl_wallet).setOnClickListener(this);
		myView.findViewById(R.id.index_my_rl_userinfo).setOnClickListener(this);

		viewPager.setAdapter(new ViewPagerAdapter(viewsList));
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.buy:
					loadBuyTitleBarLayout();
					break;
				case R.id.sale:
					loadSaleTitleBarLayout();
					break;
				case R.id.chat:
					loadChatTitleBarLayout();
					break;
				case R.id.my:
					loadMyTitleBarLayout();
					break;
				}
			}
		});
		RadioButton rbBuy = (RadioButton) findViewById(R.id.buy);
		rbBuy.setChecked(true);
		LinearLayout footer=(LinearLayout) mInflater.inflate(R.layout.comment_listview_footer, null);
		btn_load_more=(Button) footer.findViewById(R.id.load_more);
		btn_load_more.setText("正在加载...");
		btn_load_more.setOnClickListener(this);
		likelistview.addFooterView(footer);
		kanner = (Kanner) buyView.findViewById(R.id.kanner);
		adapter=new ItemListAdapter(itemlist, this);
		likelistview.setAdapter(adapter);
	}

	protected void loadMyTitleBarLayout() {
		ActivityUtils.setActionBarLayout(getActionBar(), IndexActivity.this,
				R.layout.title_bar_index_my);
		viewPager.setCurrentItem(3, false);
		findViewById(R.id.my_title_setting).setOnClickListener(this);

	}

	protected void loadChatTitleBarLayout() {
		
		if(App.user==null){
			startActivity(new Intent(this, LoginActivity.class));
			radioGroup.check(radiobutton[viewPager.getCurrentItem()]);
			return;
		}
		
		ActivityUtils.setActionBarLayout(getActionBar(), IndexActivity.this,
				R.layout.title_bar_index_chat);
		viewPager.setCurrentItem(2, false);
		ImageView chat_title_photo=(ImageView) findViewById(R.id.chat_title_photo);
		chat_title_photo.setOnClickListener(this);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_head_defaualt);
		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_head_defaualt);
		bitmapUtils.display(chat_title_photo,Config.SERVER_HOST+App.user.getUser_photo());
		findViewById(R.id.chat_title_plus).setOnClickListener(this);
		chatRadioGroup = (RadioGroup) findViewById(R.id.chat_title_radiogroup);
		if(chat_index==1){
			chatRadioGroup.check(R.id.chat_title_contacts);
		}else{
			chatRadioGroup.check(R.id.chat_title_message);
		}
		chatRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.chat_title_message:// 选中消息时
							chatViewPager.setCurrentItem(0);
							break;
						case R.id.chat_title_contacts:// 选中联系人时
							chatViewPager.setCurrentItem(1);
							break;
						default:
							break;
						}
					}
				});

	}

	protected void loadSaleTitleBarLayout() {
		// TODO Auto-generated method stub
		ActivityUtils.setActionBarLayout(getActionBar(), IndexActivity.this,
				R.layout.title_bar_index_sale);
		viewPager.setCurrentItem(1, false);
		updateStoreOrder();
	
	}

	private void updateStoreOrder() {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,Config.ACTION_GET_TODAY_INFO);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				salePullToRefreshScrollView.onRefreshComplete();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result,Config.KEY_STATUS);
				if(status==0){
					int todayordercount=JsonUtils.getInt(arg0.result, "todayordercount");
					double todayturnvolume=JsonUtils.getDouble(arg0.result, "todayturnvolume");
					int todayorderreturncount=JsonUtils.getInt(arg0.result, "todayorderreturncount");
					tv_today_money.setText(String.format("%.2f", todayturnvolume));
					tv_today_order.setText(""+todayordercount);
					tv_refund_order.setText(""+todayorderreturncount);
				}else {
					tv_today_money.setText(""+0.0);
					tv_today_order.setText(""+0);
					tv_refund_order.setText(""+0);
				}
				salePullToRefreshScrollView.onRefreshComplete();
			}
		});

		
	}

	protected void loadBuyTitleBarLayout() {
		ActivityUtils.setActionBarLayout(getActionBar(), IndexActivity.this,
				R.layout.title_bar_index_buy);
		viewPager.setCurrentItem(0, false);
		findViewById(R.id.buy_title_shoppingcart).setOnClickListener(this);
		findViewById(R.id.buy_rl_search).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.index_my_iv_photo:// 我的页面里的用户头像
		case R.id.index_my_tv_nickname:
				startActivity(new Intent(this, UserInfoActivity.class));
			break;
		case R.id.load_more:
			btn_load_more.setText("正在加载..");
			btn_load_more.setEnabled(false);
			downloadItemlist();
			break;
		case R.id.chat_title_photo:// 聊天顶部用户头像
			StringUtils.showToast(this, "聊天顶部用户头像");
			startActivity(new Intent(IndexActivity.this,UserInfoActivity.class));
			break;
		case R.id.chat_title_plus:// 聊天顶部加号
			StringUtils.showToast(this, "聊天顶部加号");
			break;
		case R.id.buy_title_shoppingcart:// 买页面顶部购物车点击
			StringUtils.showToast(this, "买页面顶部购物车点击");
			startActivity(new Intent(this, ShoppingCartActivity.class));
			break;
		case R.id.buy_rl_search:// 买页面顶部 搜索框点击
			StringUtils.showToast(this, "买页面顶部 搜索框点击");
			startActivity(new Intent(this, SearchActivity.class));
			break;
		case R.id.my_title_setting:// 我的界面设置点击
			StringUtils.showToast(this, "我的界面设置点击");
			startActivity(new Intent(this, SettingActivity.class));

			break;
		case R.id.index_my_rl_address:// 我的界面收货地址被点击
			StringUtils.showToast(this, "我的界面收货地址被点击");
			startActivity(new Intent(this, AddressActivity.class));
			break;
		case R.id.index_my_rl_buyedgoods:// 我的界面已买到的商品被点击
			StringUtils.showToast(this, "我的界面已买到的商品被点击");
			startActivityForResult(new Intent(this, BuyOrderActivity.class),Config.REQUEST_CODE_BUY_ORDER);
			break;
		case R.id.index_my_rl_collection:// 我的界面我的手藏被点击
			StringUtils.showToast(this, "我的界面我的收藏被点击");
			startActivity(new Intent(IndexActivity.this, CollectionActivity.class));
			break;
//		case R.id.index_my_rl_looked:// 我的界面浏览记录被点击
//			StringUtils.showToast(this, "我的界面浏览记录被点击");
//			break;
		case R.id.index_my_rl_password:// 我的界面登录密码被点击
			StringUtils.showToast(this, "我的界面登录密码被点击");
			startActivity(new Intent(this, ChangePasswordActivity.class));
			break;
		case R.id.index_my_rl_phone:// 我的界面绑定手机被点击
			StringUtils.showToast(this, "我的界面绑定手机被点击");
			startActivity(new Intent(this, BundingActivity.class));
			break;
		case R.id.index_my_rl_userinfo:// 我的界面个人资料被点击
			StringUtils.showToast(this, "我的界面个人资料被点击");
			startActivity(new Intent(this, UserInfoActivity.class));
			break;
		case R.id.index_my_rl_wallet:// 我的界面钱包被点击
			StringUtils.showToast(this, "我的界面钱包被点击");
			startActivity(new Intent(this, WalletActivity.class));
			break;
		case R.id.type_eat:// 买页面零食
			StringUtils.showToast(this, "买页面零食");
			Intent intent=new Intent(this, CategoryActivity.class);
			intent.putExtra(Config.KEY_TYPE,1);
			startActivity(intent);
			break;
		case R.id.type_clothes:// 买页面衣服
			StringUtils.showToast(this, "买页面衣服");
			Intent intent1=new Intent(this, CategoryActivity.class);
			intent1.putExtra(Config.KEY_TYPE,2);
			startActivity(intent1);
			break;
		case R.id.type_daily:// 买页面日用
			StringUtils.showToast(this, "买页面日用");
			Intent intent2=new Intent(this, CategoryActivity.class);
			intent2.putExtra(Config.KEY_TYPE,3);
			startActivity(intent2);
			break;
		case R.id.type_other:// 买页面其他
			StringUtils.showToast(this, "买页面其他");
			Intent intent3=new Intent(this, CategoryActivity.class);
			intent3.putExtra(Config.KEY_TYPE,4);
			startActivity(intent3);
			break;
		case R.id.index_sale_today_money:// 卖 主页 点击今日成交额
			StringUtils.showToast(this, " 卖  主页 点击今日成交额");
			break;
		case R.id.index_sale_today_order:// 卖 主页 点击今日订单数
			StringUtils.showToast(this, "卖  主页 点击今日订单数");
			Intent intent7=new Intent(this, StoreOrderManagerActivity.class);
			intent7.putExtra(Config.KEY_TYPE, -1);
			startActivity(intent7);
			break;
		case R.id.index_sale_today_refund:// 卖 主页 点击 退款中
			StringUtils.showToast(this, "卖  主页 点击 退款中");
			Intent intent4=new Intent(this, StoreOrderManagerActivity.class);
			intent4.putExtra(Config.KEY_TYPE, 4);
			startActivity(intent4);
			break;
		case R.id.index_sale_store_manager:// 卖 主页 点击 店铺管理
			StringUtils.showToast(this, "卖  主页 点击 店铺管理");
			startActivity(new Intent(IndexActivity.this,StroeManager.class));
			break;
		case R.id.index_sale_order_manager:// 卖 主页 点击 订单管理
			StringUtils.showToast(this, " 卖  主页 点击 订单管理");
			startActivity(new Intent(this, StoreOrderManagerActivity.class));
			break;
		case R.id.index_sale_goods_manager:// 卖 主页 点击 商品管理
			StringUtils.showToast(this, "卖  主页 点击 商品管理");
			startActivity(new Intent(this, GoodsManager.class));
			break;
		case R.id.index_sale_store_income:// 点击我的收入
			StringUtils.showToast(this, "点击我的收入");
			startActivity(new Intent(this,MyIncomeActivity.class));
			break;
		case R.id.index_sale_wait_send:// 卖 主页 点击 待发货
			StringUtils.showToast(this, " 卖  主页 点击 待发货");
			Intent intent5=new Intent(this, StoreOrderManagerActivity.class);
			intent5.putExtra(Config.KEY_TYPE, 1);
			startActivity(intent5);
			break;
		case R.id.index_sale_wait_pay:// 卖 主页 点击 待付款
			StringUtils.showToast(this, "卖  主页 点击 待付款");
			Intent intent6=new Intent(this, StoreOrderManagerActivity.class);
			intent6.putExtra(Config.KEY_TYPE, 0);
			startActivity(intent6);
			break;
		case R.id.index_sale_has_send:// 卖 主页 点击 已发货
			StringUtils.showToast(this, "卖  主页 点击 已发货");
			Intent intent8=new Intent(this, StoreOrderManagerActivity.class);
			intent8.putExtra(Config.KEY_TYPE, 2);
			startActivity(intent8);
			break;
		case R.id.index_sale_wait_evaluation:// 卖 主页 点击 待评价
			StringUtils.showToast(this, "卖  主页 点击 待评价");
			Intent intent9=new Intent(this, StoreOrderManagerActivity.class);
			intent9.putExtra(Config.KEY_TYPE, 3);
			startActivity(intent9);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (App.user != null) {
			StringUtils.showToast(this, "已经是登录状态");
			if(!chatInit){
				App.connect(App.user.getTooken());
			}
			updateUI();
		}else {
			updateUInoUser();
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Config.REQUEST_CODE_LOGIN:
				break;
			case Config.REQUEST_CODE_BUY_ORDER:
				radioGroup.check(radiobutton[0]);
				break;

			default:
				break;
			}
		}

	}

	private void updateUI() {
		// 登录成功后更新视图
		
		index_my_tv_nickname.setText(App.user.getUser_nickname());
		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_head_defaualt);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_head_defaualt);
		bitmapUtils.display(iv_photo, Config.SERVER_HOST+App.user.getUser_photo());
		if(!chatInit){
			App.connect(App.user.getTooken());
			chatViewPager.setAdapter(new ChatFragmentPagerAdapter(getSupportFragmentManager()));
		}

	}
	private void updateUInoUser() {
		// 登录成功后更新视图
		index_my_tv_nickname.setText("请点击登录");
		iv_photo.setImageResource(R.drawable.ic_head_defaualt);
//		bitmapUtils.display(iv_photo, Config.SERVER_HOST+App.user.getUser_photo());
		
	}

	private class ChatFragmentPagerAdapter extends
			android.support.v4.app.FragmentPagerAdapter {

		public ChatFragmentPagerAdapter(
				android.support.v4.app.FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;
			switch (i) {
			case 0:
				// TODO
				if (messageFragment == null||chatInit==false) {
					io.rong.imkit.fragment.ConversationListFragment conversationListFragment = ConversationListFragment
							.getInstance();
					Uri uri = Uri
							.parse("rong://" + getApplicationInfo().packageName)
							.buildUpon()
							.appendPath("conversationlist")
							.appendQueryParameter(
									Conversation.ConversationType.PRIVATE
											.getName(),
									"false") // 设置私聊会话是否聚合显示
							.appendQueryParameter(
									Conversation.ConversationType.GROUP
											.getName(),
									"false")// 群组
							.appendQueryParameter(
									Conversation.ConversationType.DISCUSSION
											.getName(),
									"false")// 讨论组
							.appendQueryParameter(
									Conversation.ConversationType.APP_PUBLIC_SERVICE
											.getName(), "false")// 应用公众服务。
							.appendQueryParameter(
									Conversation.ConversationType.PUBLIC_SERVICE
											.getName(), "false")// 公共服务号
							.appendQueryParameter(
									Conversation.ConversationType.SYSTEM
											.getName(),
									"true")// 系统
							.build();
					conversationListFragment.setUri(uri);
					fragment = conversationListFragment;
				} else {
					fragment = messageFragment;
				}
				break;
			case 1:
				if (contactsFragment == null||chatInit==false) {
					contactsFragment = new ContactsFragment();
				}

				fragment = contactsFragment;

				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

	}
	
	

    protected void changeLoadMoreStatus(int count) {
		if(count==0&&page==0){
			btn_load_more.setText("暂无商品");
			
		}
		else if(count<size){
			btn_load_more.setText("已无更多商品");
		}
		else {
			btn_load_more.setText("更多商品");
			btn_load_more.setEnabled(true);
			page++;
		}
	}
    void loadCarouselFigure(){//加载轮播图数据
    	
    	RequestParams params=new RequestParams();
    	params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_CAROUSEL_FIGURE);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(IndexActivity.this, "加载轮播图信息失败！");
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					Gson gson=new Gson();
					Type type = new TypeToken<ArrayList<CarouselFigure>>() {
					}.getType();
					cfList=gson.fromJson(JsonUtils.getJsonArray(arg0.result, "carouselfigure").toString(), type);
					if(cfList!=null){
						kanner=(Kanner) findViewById(R.id.kanner);
						kanner.setImagesUrl(cfList);
						buyPullToRefreshScrollView.onRefreshComplete();
					}
				}
			}
		});
    }
    @Override
    public void onBackPressed() {
    	if((System.currentTimeMillis() - mExitTime) > TIME_DIFF){
    		mExitTime=System.currentTimeMillis();
    		StringUtils.showToast(this, "再按一次退出程序");
    	}else{
    		finish();
    	}
    }

}
