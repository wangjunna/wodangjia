package com.wodangjia.adapter;

import java.util.List;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.BitmapUtils;

import com.wodangjia.activity.ItemDetails;
import com.wodangjia.bean.Item;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.utils.Config;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter {

	// 通过构造器获取数据来源
	List<View_Goods> items;
	// 接收上下文参数，以便了解是哪个activity在调用
	Context context;
	// 用来初始化布局文件，可以把一个xml布局转换为对应的android对象view
	LayoutInflater mInflater;
	BitmapUtils bitmapUtils;

	public ItemListAdapter(List<View_Goods> items, Context context) {
		super();
		this.items = items;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		bitmapUtils=new BitmapUtils(context);
	}
	
	public List<View_Goods> getItems() {
		return items;
	}

	public void setItems(List<View_Goods> items) {
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
		
		 ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_view, null);
			viewHolder.itemImg=(ImageView) convertView.findViewById(R.id.item_img);
			viewHolder.itemTitle= (TextView) convertView.findViewById(R.id.item_title);
			viewHolder.itemSubtitle= (TextView) convertView.findViewById(R.id.item_subtitle);
			viewHolder.itemStar=(RatingBar) convertView.findViewById(R.id.ratingbar_star);
			viewHolder.itemSales= (TextView) convertView.findViewById(R.id.item_sales);
			viewHolder.itemPrice= (TextView) convertView.findViewById(R.id.item_price);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final View_Goods item=items.get(position);
		if(item!=null){
//			System.out.println(Config.SERVER_HOST+ item.getGoods_imgs().get(0).getPath());
			bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading);
			bitmapUtils.display(viewHolder.itemImg,Config.SERVER_HOST+ item.getGoods_imgs().get(0).getPath());
			viewHolder.itemStar.setRating((float) item.getStar());
			viewHolder.itemTitle.setText(item.getGoods_title());
			viewHolder.itemSubtitle.setText(item.getGoods_subtitle());
			viewHolder.itemPrice.setText("￥"+item.getGoods_price());
			viewHolder.itemSales.setText("已售 "+item.getGoods_sales());
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(context,ItemDetails.class);
					intent.putExtra(Config.KEY_ITEM, item);
					context.startActivity(intent);
				}
			});
		}
		return convertView;
	}
	private  class ViewHolder
    {
		RatingBar itemStar;
		ImageView itemImg;
        TextView itemTitle;
        TextView itemSubtitle;
        TextView itemPrice;
        TextView itemSales;
    }

}
