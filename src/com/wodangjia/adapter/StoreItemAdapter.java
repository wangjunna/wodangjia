package com.wodangjia.adapter;

import java.util.ArrayList;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.BitmapUtils;
import com.wodangjia.activity.StoreActivity;
import com.wodangjia.bean.View_Store;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.ImageVIewUtils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class StoreItemAdapter extends BaseAdapter{
	private ArrayList<View_Store> itemlist;
	private Context context;
	private LayoutInflater mInflater;
	private BitmapUtils bitmapUtils;
	
	public StoreItemAdapter(Context context,ArrayList<View_Store> itemlist) {
		super();
		this.itemlist = itemlist;
		this.context=context;
		mInflater=LayoutInflater.from(context);
		bitmapUtils=new BitmapUtils(context);
	}

	public ArrayList<View_Store> getItemlist() {
		return itemlist;
	}

	public void setItemlist(ArrayList<View_Store> itemlist) {
		this.itemlist = itemlist;
		notifyDataSetChanged();
	}

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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView= mInflater.inflate(R.layout.listview_store_item, null);
			viewHolder.iv_store_img=(ImageView) convertView.findViewById(R.id.item_img);
			viewHolder.tv_store_name=(TextView) convertView.findViewById(R.id.item_store_name);
			viewHolder.tv_collect=(TextView) convertView.findViewById(R.id.item_collect);
			viewHolder.store_level=(ImageView) convertView.findViewById(R.id.store_level);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final View_Store store=itemlist.get(position);
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading);
		bitmapUtils.display(viewHolder.iv_store_img, Config.SERVER_HOST+store.getStore_img_path());
		viewHolder.tv_collect.setText("收藏 "+store.getCollections());
		ImageVIewUtils.setLevel(store.getStore_credit(), viewHolder.store_level);
		viewHolder.tv_store_name.setText(store.getStore_name());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,StoreActivity.class);
				intent.putExtra(Config.KEY_USER_ID,store.getUser_id());
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	class ViewHolder{
		ImageView iv_store_img,store_level;
		TextView tv_store_name,tv_collect;
	}

}
