package com.wodangjia.adapter;

import java.util.ArrayList;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.wodangjia.bean.Goods_Comments;
import com.wodangjia.bean.View_Comment;
import com.wodangjia.utils.Config;
import com.wodangjia.view.CircleImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemCommentListAdapter extends BaseAdapter {

	ArrayList<View_Comment> commentlist;
	Context context;
	LayoutInflater mInflater;
	BitmapUtils bitmapUtils;

	public ItemCommentListAdapter(Context context,
			ArrayList<View_Comment> commentlist) {
		super();
		this.commentlist = commentlist;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHoder viewHoder;
		if (convertView == null) {
			viewHoder = new ViewHoder();
			convertView = mInflater.inflate(R.layout.item_comment_view, null);
			viewHoder.civ_photo = (CircleImageView) convertView
					.findViewById(R.id.photo);
			viewHoder.tv_nickname = (TextView) convertView
					.findViewById(R.id.usernickname);
			viewHoder.tv_content = (TextView) convertView
					.findViewById(R.id.content);
			viewHoder.tv_time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(viewHoder);
		} else {
			viewHoder = (ViewHoder) convertView.getTag();
		}
		View_Comment comment = commentlist.get(position);
		final CircleImageView civ=viewHoder.civ_photo;
//		viewHoder.civ_photo.setImageBitmap(bm);
		bitmapUtils.display(viewHoder.civ_photo,Config.SERVER_HOST + comment.getUser_photo(), new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View arg0, String arg1, Bitmap arg2,
					BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
				// TODO Auto-generated method stub
				civ.setImageBitmap(arg2);
			}

			@Override
			public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
				// TODO Auto-generated method stub
				
			}
		});
		System.out.println(Config.SERVER_HOST + comment.getUser_photo());
//		bitmapUtils.display(viewHoder.civ_photo,Config.SERVER_HOST + comment.getUser_photo());
		viewHoder.tv_nickname.setText(comment.getUser_nickname());
		viewHoder.tv_content.setText(comment.getContent());
		viewHoder.tv_time.setText(comment.getComment_time());
		return convertView;
	}

	class ViewHoder {
		CircleImageView civ_photo;
		TextView tv_nickname;
		TextView tv_content;
		TextView tv_time;
	}

	public ArrayList<View_Comment> getCommentlist() {
		return commentlist;
	}

	public void setCommentlist(ArrayList<View_Comment> commentlist) {
		this.commentlist = commentlist;
	}

}
