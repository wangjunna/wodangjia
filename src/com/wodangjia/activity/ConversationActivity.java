package com.wodangjia.activity;

import java.util.Locale;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ConversationActivity extends FragmentActivity {

	  /**
	   * 目标 Id
	   */
	    private String mTargetId;

	    /**
	     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
	     */
	    private String mTargetIds;

	    /**
	     * 会话类型
	     */
	    private Conversation.ConversationType mConversationType;

	    @SuppressLint("ResourceAsColor") @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_conversation);
	        Intent intent = getIntent();
	        getIntentDate(intent);
	        ActivityUtils.setActionBarLayout(getActionBar(), this,
					R.layout.title_bar_chatting);
			ActivityUtils.setTranslucentStatus(getWindow(), true);
			ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
			overridePendingTransition(R.anim.anim_right_to_left_in,
					R.anim.anim_right_to_left_out);
	    }

	    /**
	     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
	     */
	    private void getIntentDate(Intent intent) {

	        mTargetId = intent.getData().getQueryParameter("targetId");
	        System.out.println("mTargetId--"+mTargetId);
	        changeActionBar();
//	        mTargetId="123";
	        
//	        mTargetIds = intent.getData().getQueryParameter("targetIds");
	        
//	        intent.getData().getLastPathSegment();//获得当前会话类型
//	        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
	        mConversationType=ConversationType.PRIVATE;
	        enterFragment(mConversationType, mTargetId);
	    }

	    private void changeActionBar() {
			RequestParams params=new RequestParams();
//			params.addBodyParameter(Config.KEY_ACTION, value)
			App.httpclient.send(HttpMethod.POST, Config.API_URL, params,new RequestCallBack<String>(){

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}

		/**
	     * 加载会话页面 ConversationFragment
	     *
	     * @param mConversationType 会话类型
	     * @param mTargetId 目标 Id
	     */
	    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

	        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

	        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
	                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
	                .appendQueryParameter("targetId", mTargetId).build();

	        fragment.setUri(uri);
	    }

	}