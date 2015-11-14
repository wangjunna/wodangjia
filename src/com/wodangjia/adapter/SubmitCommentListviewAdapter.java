package com.wodangjia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.activity.SubmitOrderActivity;
import com.wodangjia.app.App;
import com.wodangjia.bean.Goods_Comments;
import com.wodangjia.bean.Order;
import com.wodangjia.bean.Orders;
import com.wodangjia.bean.View_ShoppingCartItem;
import com.wodangjia.bean.Wallet;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.NoScrollListView;
import com.zf.iosdialog.widget.CustomAlertDialog;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SubmitCommentListviewAdapter extends BaseAdapter {

	ArrayList<View_ShoppingCartItem> items;
	ArrayList<Goods_Comments> comments;
	Context context;
	LayoutInflater mInflater;
	BitmapUtils bitmapUtils;
	ViewHolder viewHolder;

	public SubmitCommentListviewAdapter(ArrayList<View_ShoppingCartItem> items, Context context) {
		super();
		this.items = items;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		bitmapUtils=new BitmapUtils(context);
		comments=new ArrayList<Goods_Comments>();
		initGoods_Comments();
	}

	private void initGoods_Comments() {
		for(int i=0;i<items.size();i++){
			comments.add(new Goods_Comments(items.get(i).getItem().getGoods_id(), App.user.getUser_id(), "", "", 0));
		}
		
	}

	public ArrayList<View_ShoppingCartItem> getItems() {
		return items;
	}

	public ArrayList<Goods_Comments> getComments() {
		return comments;
	}
	
	public void setItems(ArrayList<View_ShoppingCartItem> items) {
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

	@SuppressLint("ResourceAsColor") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listview_submit_comment, null);
			viewHolder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
			viewHolder.item_title = (TextView) convertView.findViewById(R.id.item_title);
			viewHolder.item_price=(TextView) convertView.findViewById(R.id.item_price);
			viewHolder.item_count=(TextView) convertView.findViewById(R.id.item_count);
			viewHolder.ratingbar_star=(RatingBar) convertView.findViewById(R.id.ratingbar_star);
			viewHolder.et_comment=(EditText) convertView.findViewById(R.id.et_comment);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final View_ShoppingCartItem item = items.get(position);
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading);
		bitmapUtils.display(viewHolder.item_img, Config.SERVER_HOST+item.getItem().getGoods_imgs().get(0).getPath());
		viewHolder.item_title.setText(item.getItem().getGoods_title());
		viewHolder.item_price.setText("￥"+item.getItem().getGoods_price());
		viewHolder.item_count.setText("x "+item.getCount());
		viewHolder.ratingbar_star.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				comments.get(position).setStar(rating);
			}
		});
		viewHolder.et_comment.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				StringUtils.showToast(context, s.toString());
				comments.get(position).setContent(s.toString());
			}
		});
		
		
		return convertView;
	}

	private class ViewHolder {
		ImageView item_img;
		TextView item_title,item_price,item_count;
		RatingBar ratingbar_star;
		EditText et_comment;
	}
	

}
