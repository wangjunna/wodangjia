package com.wodangjia.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.activity.ShoppingCartActivity;
import com.wodangjia.app.App;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_ShoppingCartItem;
import com.wodangjia.utils.Config;

@SuppressLint("UseSparseArrays")
public class ShoppingCartAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private ArrayList<View_ShoppingCartItem> list;
	private BitmapUtils bitmapUtils;
	private Context context;
	private CheckBox checkBoxAll;// 全选按钮
	private TextView checkBoxAllPrice;// 所有被选中商品的总金额
	ShoppingCartActivity shoppingCartActivity;

	public ShoppingCartAdapter(Context context,
			ArrayList<View_ShoppingCartItem> list) {
		layoutInflater = LayoutInflater.from(context);
		bitmapUtils = new BitmapUtils(context);
		this.list = list;
		this.context = context;
		shoppingCartActivity = (ShoppingCartActivity) context;
		checkBoxAll = shoppingCartActivity.getCheckBoxAll();
		checkBoxAllPrice = shoppingCartActivity.getCheckBoxAllPrice();
	}

	public ArrayList<View_ShoppingCartItem> getList() {
		return list;

	}

	public void setList(ArrayList<View_ShoppingCartItem> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.listview_shopping_cart, null);
			viewHolder = new ViewHolder();
			viewHolder.shopping_cart_image = (ImageView) convertView
					.findViewById(R.id.shopping_cart_image);
			viewHolder.shopping_cart_title = (TextView) convertView
					.findViewById(R.id.shopping_cart_title);
			viewHolder.shopping_cart_content = (TextView) convertView
					.findViewById(R.id.shopping_cart_content);
			viewHolder.shopping_cart_price = (TextView) convertView
					.findViewById(R.id.shopping_cart_price);
			viewHolder.shopping_cart_count = (TextView) convertView
					.findViewById(R.id.shopping_cart_count);
			viewHolder.shopping_cart_sum = (TextView) convertView
					.findViewById(R.id.shopping_cart_sum);
			convertView.setTag(viewHolder);
			viewHolder.shopping_cart_add = (ImageButton) convertView
					.findViewById(R.id.shopping_cart_add);
			viewHolder.shopping_cart_des = (ImageButton) convertView
					.findViewById(R.id.shopping_cart_des);
			viewHolder.shopping_cart_choose = (CheckBox) convertView
					.findViewById(R.id.shopping_cart_choose);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final int curposition=position;
		final View_ShoppingCartItem cartItem = list.get(position);
		final View_Goods item = list.get(position).getItem();
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading);
		bitmapUtils.display(viewHolder.shopping_cart_image, Config.SERVER_HOST+item.getGoods_imgs().get(0).getPath());

		viewHolder.shopping_cart_title.setText(item.getGoods_title());
		viewHolder.shopping_cart_content.setText(item.getGoods_subtitle());
		viewHolder.shopping_cart_price.setText("￥" + item.getGoods_price()
				+ " 元");
		viewHolder.shopping_cart_count.setText(cartItem.getCount() + "");
		viewHolder.shopping_cart_sum.setText(String.format("%.2f",cartItem.getCount()
				* item.getGoods_price()));
		viewHolder.shopping_cart_choose.setChecked(cartItem.isChecked());
//		convertView.findViewById(R.id.goods_info).setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent(context, ItemDetails.class);
//						intent.putExtra(Config.KEY_ITEM, list.get(position));
//						context.startActivity(intent);
//					}
//				});

		viewHolder.shopping_cart_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int count = cartItem.getCount() + 1;
				cartItem.setCount(count);
				viewHolder.shopping_cart_count.setText(count + "");
				viewHolder.shopping_cart_sum.setText(String.format("%.2f",count
						* item.getGoods_price()));
				changeToCart(Config.SHOPPING_CART_ADD,item);
				changeCheckBoxAllPrice();
			}
		});

		viewHolder.shopping_cart_des.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int count = cartItem.getCount();
				if (count > 1) {
					count--;
					cartItem.setCount(count);
					viewHolder.shopping_cart_count.setText(count + "");
					viewHolder.shopping_cart_sum.setText(String.format("%.2f",count
							* item.getGoods_price()) );
					changeToCart(Config.SHOPPING_CART_SUB,item);
					changeCheckBoxAllPrice();
				}

			}
		});

		viewHolder.shopping_cart_choose
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						CheckBox checkBox = (CheckBox) v;
						list.get(curposition).setChecked(checkBox.isChecked());
						changeCheckBoxAll();
						changeCheckBoxAllPrice();
					}
				});
		return convertView;
	}

	public void changeCheckBoxAllPrice() {
		// TODO Auto-generated method stub
		double allPrice = 0;// 总价（全部商品）
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isChecked()) {
				allPrice += list.get(i).getCount()
						* list.get(i).getItem().getGoods_price();
			}
		}
		
		
//		checkBoxAllPrice.setText("金额: " + new java.text.DecimalFormat("#.00").format(allPrice));
		checkBoxAllPrice.setText("金额: " + String.format("%.2f",allPrice));
	}

	public void changeCheckBoxAll() {
		if (list.size() == 0) {
			checkBoxAll.setChecked(false);
		}
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).isChecked()) {
				checkBoxAll.setChecked(false);
				return;
			} else {
				checkBoxAll.setChecked(true);
			}
		}

	}

	private void changeToCart( int operation,View_Goods item) {

		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,Config.ACTION_UPADE_SHOPPING_CART);
		params.addBodyParameter(Config.KEY_GOODS_ID,""+item.getGoods_id());
		params.addBodyParameter(Config.KEY_OPERATION,""+operation);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

					}
				});
	}

	class ViewHolder {
		public ImageView shopping_cart_image;
		public ImageButton shopping_cart_add, shopping_cart_des;
		public TextView shopping_cart_title, shopping_cart_content,
				shopping_cart_price, shopping_cart_count, shopping_cart_sum;
		public CheckBox shopping_cart_choose;
	}

}
