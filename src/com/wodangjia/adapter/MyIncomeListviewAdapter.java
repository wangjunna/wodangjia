package com.wodangjia.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.wodangjia.bean.Orders;

public class MyIncomeListviewAdapter extends BaseAdapter {

	ArrayList<Orders> items;
	Context context;
	LayoutInflater mInflater;

	ViewHolder viewHolder;

	public MyIncomeListviewAdapter(ArrayList<Orders> items, Context context) {
		super();
		this.items = items;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public ArrayList<Orders> getItems() {
		return items;
	}

	public void setItems(ArrayList<Orders> items) {
		this.items = items;
		notifyDataSetChanged();
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
			convertView = mInflater.inflate(R.layout.item_income_bill, null);
			viewHolder.order_no = (TextView) convertView
					.findViewById(R.id.order_no);
			viewHolder.order_value = (TextView) convertView
					.findViewById(R.id.order_value);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Orders order = items.get(position);
		OrderGoodsListviewAdapter adapter = new OrderGoodsListviewAdapter(
				order.getItems(), context);
		viewHolder.order_no.setText("No:"+order.getOrder_no());
		viewHolder.order_value.setText("+"+String.format("%.2f", order.getOrder_value()));

		return convertView;
	}


	private class ViewHolder {
		TextView order_no, order_value;
	}

}
