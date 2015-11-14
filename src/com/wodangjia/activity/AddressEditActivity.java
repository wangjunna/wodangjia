package com.wodangjia.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wodangjialayout.R;
import com.gghl.view.wheelcity.AddressData;
import com.gghl.view.wheelcity.OnWheelChangedListener;
import com.gghl.view.wheelcity.WheelView;
import com.gghl.view.wheelcity.adapters.AbstractWheelTextAdapter;
import com.gghl.view.wheelcity.adapters.ArrayWheelAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.bean.Address;
import com.wodangjia.dialog.CustomProgressDialog;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.zf.iosdialog.widget.CustomAlertDialog;

public class AddressEditActivity extends Activity implements OnClickListener {
	// 市，自治区集合
	CustomProgressDialog dialog ;
	private String cityTxt;
	private String v_province,v_city,v_county;
	EditText nameEdit;
	EditText phoneEdit;
	EditText addrEdit;
	Button submitButton;
	LinearLayout ll_address_select;
	TextView tv_province,tv_city,tv_county;
	private Address address;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_edit);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_address_edit);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initData(); 
		initView();

	}

	private void initData() {
		Intent intent=getIntent();
		address=(Address) intent.getSerializableExtra("address");
		v_province=address.getProvince();
		v_city=address.getCity();
		v_county=address.getCounty();
		dialog=new CustomProgressDialog(this, "正在加载，请稍候", R.anim.frame);
	}

	public void editAddress() {
		if(!StringUtils.checkAddress(this, nameEdit,phoneEdit, tv_county, addrEdit)){
			return;
		}
		dialog.show();
		address.setName(nameEdit.getText().toString());
		address.setPhone(phoneEdit.getText().toString());
		address.setProvince(v_province);
		address.setCity(v_city);
		address.setCounty(v_county);
		address.setDetail(addrEdit.getText().toString());
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,Config.ACTION_UPDATE_ADDRESS);
		params.addBodyParameter("address_id",""+address.getId());
		params.addBodyParameter("name", nameEdit.getText().toString());
		params.addBodyParameter("phone", phoneEdit.getText().toString());
		params.addBodyParameter("province", v_province);
		params.addBodyParameter("city", v_city);
		params.addBodyParameter("county", v_county);
		params.addBodyParameter("detail", addrEdit.getText().toString());
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils.showToast(AddressEditActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
						dialog.dismiss();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						int status=JsonUtils.getInt(arg0.result,Config.KEY_STATUS);
						if(status==0){
							StringUtils.showToast(AddressEditActivity.this, "增加收货地址成功！");
							Intent intent=new Intent();
							intent.putExtra("address",address);
							AddressEditActivity.this.setResult(RESULT_OK, intent);
							finish();
						}
						dialog.dismiss();
					}
				});
	}

	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		ll_address_select = (LinearLayout) findViewById(R.id.ll_address_select);
		ll_address_select.setOnClickListener(this);
		nameEdit = (EditText) findViewById(R.id.add_receiver_name);
		nameEdit.setText(address.getName());
		phoneEdit = (EditText) findViewById(R.id.add_receiver_phonenum);
		phoneEdit.setText(address.getPhone());
		addrEdit = (EditText) findViewById(R.id.add_receiver_detaddress);
		addrEdit.setText(address.getDetail());
		tv_province=(TextView) findViewById(R.id.tv_province);
		tv_province.setText(address.getProvince());
		tv_city=(TextView) findViewById(R.id.tv_city);
		tv_city.setText(address.getCity());
		tv_county=(TextView) findViewById(R.id.tv_county);
		tv_county.setText(address.getCounty());

		submitButton = (Button) findViewById(R.id.address_submit_btn);
		submitButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_back:
			ActivityUtils.finish(this);
			StringUtils.showToast(this, "单击了返回");
			break;
		case R.id.address_submit_btn:
			StringUtils.showToast(this, "单击了保存");
			editAddress();
			break;
		case R.id.ll_address_select:
			showAddressDialog();
			break;

		default:
			break;
		}

	}

	private void showAddressDialog() {
		// TODO Auto-generated method stub View view = dialogm();
		View view = dialogm();
		final CustomAlertDialog dialog1 = new CustomAlertDialog(this)
				.builder().setTitle("请选择收货地址")
				.setView(view).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog1.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), cityTxt, 1).show();
				tv_province.setText(v_province);
				tv_city.setText(v_city);
				tv_county.setText(v_county);
				dialog1.dismiss();
			}
		});
		dialog1.show();

	}

	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	}
	private View dialogm() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.wheelcity_cities_layout, null);
		final WheelView country = (WheelView) contentView
				.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(this));

		final String cities[][] = AddressData.CITIES;
		final String ccities[][][] = AddressData.COUNTIES;
		final WheelView city = (WheelView) contentView
				.findViewById(R.id.wheelcity_city);
		city.setVisibleItems(0);

		// 地区选择
		final WheelView ccity = (WheelView) contentView
				.findViewById(R.id.wheelcity_ccity);
		ccity.setVisibleItems(0);// 不限城市

		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(city, cities, newValue);
				v_province=AddressData.PROVINCES[country.getCurrentItem()];
				v_city=AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()];
				v_county=AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(ccity, ccities, country.getCurrentItem(),
						newValue);
				v_province=AddressData.PROVINCES[country.getCurrentItem()];
				v_city=AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()];
				v_county=AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				v_province=AddressData.PROVINCES[country.getCurrentItem()];
				v_city=AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()];
				v_county=AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		country.setCurrentItem(1);// 设置北京
		city.setCurrentItem(1);
		ccity.setCurrentItem(1);
		return contentView;
	}
	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
		private String countries[] = AddressData.PROVINCES;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}

	/**
	 * Updates the city wheel
	 */
	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	/**
	 * Updates the ccity wheel
	 */
	private void updatecCities(WheelView city, String ccities[][][], int index,
			int index2) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				ccities[index][index2]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

}
