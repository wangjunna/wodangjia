package com.wodangjia.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.SubmitItemListviewAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.Address;
import com.wodangjia.bean.SubmitOrderInfo;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_ShoppingCartItem;
import com.wodangjia.bean.Wallet;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.NoScrollListView;
import com.zf.iosdialog.widget.CustomAlertDialog;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.SearchManager.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SubmitOrderActivity extends BaseActivity implements OnClickListener{

	ArrayList<View_ShoppingCartItem> itemlist;
	private TextView tv_name,tv_phone,tv_province,tv_city,tv_county,tv_detile,tv_allprice;
	private RelativeLayout rl_exist_default_address,rl_unexist_default_address;
	private NoScrollListView listView;
	private Address address;
	private double allprice;
	private SubmitItemListviewAdapter adapter;
	private SubmitOrderInfo submitOrderInfo;
	private ArrayList<Integer> orderids;//返回的生成的订单id
	private EditText et_remark;
	private LayoutInflater mInflater;
	private Wallet wallet;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_submit_order);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initData();
		initView();
		download();
	}

	private void download() {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_GET_DEFAULT_ADDRESS);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					Gson gson=new Gson();
					address=gson.fromJson(JsonUtils.getJsonObject(arg0.result, Config.KEY_ADDRESS).toString(), Address.class);
					submitOrderInfo.setAddress(address);
				}
				setAddress();
			}
		});
		
	}

	protected void setAddress() {
		if(address!=null){
			rl_exist_default_address.setVisibility(View.VISIBLE);
			rl_unexist_default_address.setVisibility(View.GONE);
			tv_name.setText(address.getName());
			tv_phone.setText(address.getPhone());
			tv_province.setText(address.getProvince());
			tv_city.setText(address.getCity());
			tv_detile.setText(address.getDetail());
			tv_county.setText(address.getCounty());
			
		}else{
			rl_exist_default_address.setVisibility(View.GONE);
			rl_unexist_default_address.setVisibility(View.VISIBLE);
		}
		
	}

	private void initView() {
		et_remark=(EditText) findViewById(R.id.et_remark);
		findViewById(R.id.submit).setOnClickListener(this);
		tv_allprice=(TextView) findViewById(R.id.all_price);
		tv_allprice.setText(String.format("%.2f",allprice)+"元");
		tv_name=(TextView) findViewById(R.id.tv_name);
		tv_phone=(TextView) findViewById(R.id.tv_phone);
		tv_province=(TextView) findViewById(R.id.tv_province);
		tv_city=(TextView) findViewById(R.id.tv_city);
		tv_county=(TextView) findViewById(R.id.tv_county);
		tv_detile=(TextView) findViewById(R.id.tv_detile);
		rl_exist_default_address=(RelativeLayout) findViewById(R.id.exist_default_address);
		rl_unexist_default_address=(RelativeLayout) findViewById(R.id.unexist_default_address);
		rl_exist_default_address.setOnClickListener(this);
		rl_unexist_default_address.setOnClickListener(this);
		listView=(NoScrollListView) findViewById(R.id.buy_listview);
		listView.setAdapter(adapter);
	}

	private void initData() {
		Intent intent=getIntent();
		itemlist=(ArrayList<View_ShoppingCartItem>) intent.getSerializableExtra("itemlist");
		adapter=new SubmitItemListviewAdapter(this, itemlist);
		for(int i=0;i<itemlist.size();i++){
			allprice+=itemlist.get(i).getCount()*itemlist.get(i).getItem().getGoods_price();
		}
		submitOrderInfo=new SubmitOrderInfo(address, itemlist);
		mInflater=LayoutInflater.from(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit: 
			submit();
			break;
		case R.id.exist_default_address:
		case R.id.unexist_default_address:
			Intent intent=new Intent(SubmitOrderActivity.this,AddressActivity.class);
			intent.putExtra(Config.KEY_REQUESTCODE, 1);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
		
	}

	private void submit() {
		submitOrderInfo.setRemark(et_remark.getText().toString());
		submitOrderInfo.setAddress(address);
		//提交订单
		RequestParams params= new RequestParams();
		Gson gson=new Gson();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_SUBMIT_ORDER);
		params.addBodyParameter(Config.KEY_ORDER_INFO,gson.toJson(submitOrderInfo));
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(SubmitOrderActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				
				if(status==0){
					Gson gson=new Gson();
					Type type = new TypeToken<ArrayList<Integer>>() {
					}.getType();
					orderids=gson.fromJson(JsonUtils.getJsonArray(arg0.result, "orderids").toString(), type);
					showPayDialog();
				}
			}
		});
	}

	protected void showPayDialog() {
		LinearLayout layout = (LinearLayout) mInflater.inflate(
				R.layout.dialog_pay_order, null);
		final TextView tv_pay_money = (TextView) layout
				.findViewById(R.id.tv_pay_money);
		tv_pay_money.setText(String.format("%.2f",allprice)+"元");
		final TextView tv_banlance=(TextView) layout.findViewById(R.id.tv_banlance);
		getBanlance(tv_banlance);
		final CustomAlertDialog dialog = new CustomAlertDialog(this).builder()
				.setTitle("支付订单").setView(layout)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					}
				});
		dialog.setPositiveButton("付款", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(wallet.getWallet_balance()<allprice){
					StringUtils.showToast(SubmitOrderActivity.this, "余额不足支付订单！");
					return;
				}
				payOrder(); 
			}
		});
		dialog.setCancelable(false);
		dialog.show();
		
	}

	protected void payOrder() {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_PAY_ORDER);
		Gson gson=new Gson();
		params.addBodyParameter(Config.KEY_ORDERIDS,gson.toJson(orderids));
		params.addBodyParameter("allprice", String.format("%.2f",allprice));//将总价发到服务端
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(SubmitOrderActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					StringUtils.showToast(SubmitOrderActivity.this, "支付完成！");
					finish();
				}
				
			}
		});		
	}

	private void getBanlance(final TextView tv_banlance) {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_WALLET_INFO);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(SubmitOrderActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					Gson gson=new Gson();
					wallet=gson.fromJson(JsonUtils.getJsonObject(arg0.result, "wallet").toString(), Wallet.class);
					tv_banlance.setText(String.format("%.2f",wallet.getWallet_balance())+"元");
				}
			}
		});
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode==RESULT_OK){
			switch (requestCode) {
			case 1:
				updateAddress(data); 
				break;

			default:
				break;
			}
		}
	}

	private void updateAddress(Intent data) {
		 address=(Address) data.getSerializableExtra(Config.KEY_ADDRESS);
		 setAddress() ;
	}
}
