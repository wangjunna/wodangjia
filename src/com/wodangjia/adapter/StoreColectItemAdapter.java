package com.wodangjia.adapter;

import java.util.ArrayList;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.activity.StoreActivity;
import com.wodangjia.app.App;
import com.wodangjia.bean.View_CollectionStore;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.bean.View_Store;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.ImageVIewUtils;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class StoreColectItemAdapter extends BaseAdapter{
	private ArrayList<View_CollectionStore> itemlist;
	private Context context;
	private LayoutInflater mInflater;
	private BitmapUtils bitmapUtils;
	
	public StoreColectItemAdapter(Context context,ArrayList<View_CollectionStore> itemlist) {
		super();
		this.itemlist = itemlist;
		this.context=context;
		mInflater=LayoutInflater.from(context);
		bitmapUtils=new BitmapUtils(context);
	}

	public ArrayList<View_CollectionStore> getItemlist() {
		return itemlist;
	}

	public void setItemlist(ArrayList<View_CollectionStore> itemlist) {
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
			convertView= mInflater.inflate(R.layout.listview_store_collection_item, null);
			viewHolder.iv_store_img=(ImageView) convertView.findViewById(R.id.item_img);
			viewHolder.tv_store_name=(TextView) convertView.findViewById(R.id.item_store_name);
			viewHolder.tv_collect=(TextView) convertView.findViewById(R.id.item_collect);
			viewHolder.tv_delete=(TextView) convertView.findViewById(R.id.delete);
			viewHolder.store_level=(ImageView) convertView.findViewById(R.id.store_level);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final View_CollectionStore store=itemlist.get(position);
		ImageVIewUtils.setLevel(store.getStore_credit(), viewHolder.store_level);
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading);
		bitmapUtils.display(viewHolder.iv_store_img, Config.SERVER_HOST+store.getStore_img_path());
		viewHolder.tv_collect.setText("收藏 "+store.getCollections());
		viewHolder.tv_store_name.setText(store.getStore_name());
		viewHolder.tv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cancleCollect(store);
			}
		});
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
		TextView tv_store_name,tv_collect,tv_delete;
	}
	private void cancleCollect(final View_CollectionStore store) {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_CANCLE_COLLECT_STORE);
		params.addBodyParameter(Config.KEY_STORE_ID, ""+store.getStore_id());
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				StringUtils.showToast(context, Config.ERROR_UNCONNECTION_INTERNET);
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				switch (status) {
				case Config.RESULT_STATUS_SUCCESS://收藏成功  或取消成功
					itemlist.remove(store);
					notifyDataSetChanged();
					break;
				default:
					break;
				}
			}
		});
	
}
}
