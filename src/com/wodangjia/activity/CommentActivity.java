package com.wodangjia.activity;

import java.util.ArrayList;

import com.example.wodangjialayout.R;
import com.example.wodangjialayout.R.id;
import com.example.wodangjialayout.R.layout;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.adapter.SubmitCommentListviewAdapter;
import com.wodangjia.app.App;
import com.wodangjia.bean.Goods_Comments;
import com.wodangjia.bean.Orders;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.NoScrollListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CommentActivity extends Activity implements OnClickListener{

	private Orders orders;
	private TextView order_no;
	private NoScrollListView item_listview;
	private SubmitCommentListviewAdapter adapter;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		ActivityUtils.setActionBarLayout(getActionBar(), this,R.layout.title_bar_comment);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out); 
		initData(); 
		initView(); 
	}

	private void initData() {
		Intent intent=getIntent();
		orders=(Orders) intent.getSerializableExtra("orders");
		adapter=new SubmitCommentListviewAdapter(orders.getItems(), this);
		
	}

	private void initView() {
		order_no=(TextView) findViewById(R.id.order_no);
		order_no.setText(orders.getOrder_no());
		item_listview=(NoScrollListView) findViewById(R.id.item_listview);
		item_listview.setAdapter(adapter);
		findViewById(R.id.submit_comment).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_comment:
			submitComment(); 
			break;

		default:
			break;
		}
		
	}

	private void submitComment() {
		ArrayList<Goods_Comments> comments=adapter.getComments();
		for(int i=0;i<comments.size();i++){
			Goods_Comments comment=comments.get(i);
			if(comment.getStar()==0){
				StringUtils.showToast(this, "第"+i+"个商品评价没有打分！");
				return;
			}
			if(comment.getContent()==null||comment.getContent().equals("")){
				StringUtils.showToast(this, "第"+i+"个商品评价没有写评价内容！");
				return;
			}
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_SUBMIT_GOODS_COMMENTS);
		params.addBodyParameter(Config.KEY_ORDER_ID,""+ orders.getOrder_id());
		Gson gson=new Gson();
		params.addBodyParameter(Config.KEY_COMMENTS, gson.toJson(comments));
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(CommentActivity.this, Config.ERROR_UNCONNECTION_INTERNET);
				
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result,Config.KEY_STATUS);
				if(status==0){
					StringUtils.showToast(CommentActivity.this,"提交评价成功！");
					CommentActivity.this.setResult(RESULT_OK);
					CommentActivity.this.finish();
				}
			}
		});
		
	}

}
