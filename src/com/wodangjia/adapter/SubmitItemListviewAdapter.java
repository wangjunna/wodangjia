package com.wodangjia.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.BitmapUtils;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_ShoppingCartItem;
import com.wodangjia.utils.Config;

public class SubmitItemListviewAdapter extends BaseAdapter {

	Context context;
	ArrayList<View_ShoppingCartItem> list;
	private LayoutInflater layoutInflater;
	BitmapUtils bimaUtils;

	public SubmitItemListviewAdapter(Context context, ArrayList<View_ShoppingCartItem> list) {
		super();
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		bimaUtils = new BitmapUtils(context);
	}

	public List<View_ShoppingCartItem> getList() {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.listview_submit_order, null);
			viewHolder = new ViewHolder();
			viewHolder.buy_goods_image = (ImageView) convertView
					.findViewById(R.id.buy_goods_img);
			viewHolder.buy_goods_title = (TextView) convertView
					.findViewById(R.id.buy_goods_title);
			viewHolder.buy_goods_content = (TextView) convertView
					.findViewById(R.id.buy_goods_content);
			viewHolder.buy_goods_price = (TextView) convertView
					.findViewById(R.id.buy_goods_price);
			viewHolder.buy_goods_count = (TextView) convertView
					.findViewById(R.id.buy_goods_count);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		View_Goods item = list.get(position).getItem();
		viewHolder.buy_goods_title.setText(item.getGoods_title());
		viewHolder.buy_goods_content.setText(item.getGoods_subtitle());
		viewHolder.buy_goods_price.setText("￥" + item.getGoods_price() + " 元");
		viewHolder.buy_goods_count.setText(list.get(position).getCount() + "");
		bimaUtils.configDefaultLoadingImage(R.drawable.loading);
		bimaUtils.configDefaultLoadFailedImage(R.drawable.loading);
		bimaUtils.display(viewHolder.buy_goods_image, Config.SERVER_HOST
				+ item.getGoods_imgs().get(0).getPath());

		return convertView;
	}

	class ViewHolder {
		public ImageView buy_goods_image;
		public TextView buy_goods_title, buy_goods_content, buy_goods_count,
				buy_goods_price;
	}
}
