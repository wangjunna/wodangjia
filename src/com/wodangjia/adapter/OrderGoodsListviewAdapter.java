package com.wodangjia.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.BitmapUtils;
import com.wodangjia.bean.View_ShoppingCartItem;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.StringUtils;

public class OrderGoodsListviewAdapter extends BaseAdapter {

	ArrayList<View_ShoppingCartItem> items;
	Context context;
	LayoutInflater mInflater;
	BitmapUtils bitmapUtils;
	ViewHolder viewHolder;

	public OrderGoodsListviewAdapter(ArrayList<View_ShoppingCartItem> items, Context context) {
		super();
		this.items = items;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		bitmapUtils=new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listview_order_goods_item, null);
			viewHolder.goodsTitle = (TextView) convertView
					.findViewById(R.id.item_title);
			viewHolder.goodsImg = (ImageView) convertView
					.findViewById(R.id.item_img);
			viewHolder.goodsPrice = (TextView) convertView
					.findViewById(R.id.item_price);
			viewHolder.goodsCount = (TextView) convertView
					.findViewById(R.id.item_count);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		StringUtils.showToast(context, ""+items);
//		System.out.println("====="+viewHolder.goodsTitle);
		viewHolder.goodsTitle.setText(items.get(position).getItem().getGoods_title());
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading);
		bitmapUtils.display(viewHolder.goodsImg, Config.SERVER_HOST+items.get(position).getItem().getGoods_imgs().get(0).getPath());
		viewHolder.goodsCount.setText("x"+items.get(position).getCount());
		viewHolder.goodsPrice.setText("￥"+String.format("%.2f",items.get(position).getItem().getGoods_price()));
		return convertView;
	}

	private class ViewHolder {
		TextView goodsTitle, goodsPrice, goodsCount;
		ImageView goodsImg;

	}

}
