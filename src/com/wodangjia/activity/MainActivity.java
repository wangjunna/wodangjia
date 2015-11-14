/*
 * Copyright (C) 2013 readyState Software Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wodangjia.activity;

import java.util.ArrayList;
import java.util.List;

import krelve.view.Kanner;

import com.example.wodangjialayout.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wodangjia.adapter.ItemListAdapter;
import com.wodangjia.bean.Item;

import android.R.integer;
import android.R.string;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;



public class MainActivity extends Activity { 
	private Kanner kanner;
	private List<Item> items;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_buy);
	
		kanner = (Kanner) findViewById(R.id.kanner);
		 kanner.setImagesUrl(new String[] {
		 "http://img04.muzhiwan.com/2015/06/16/upload_557fd293326f5.jpg",
		 "http://img03.muzhiwan.com/2015/06/05/upload_557165f4850cf.png",
		 "http://img02.muzhiwan.com/2015/06/11/upload_557903dc0f165.jpg",
		 "http://img04.muzhiwan.com/2015/06/05/upload_5571659957d90.png",
		 "http://img03.muzhiwan.com/2015/06/16/upload_557fd2a8da7a3.jpg" });
		 
		 initData();
	

		
		
//		SystemBarTintManager tintManager = new SystemBarTintManager(this);
//		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setStatusBarTintResource(R.color.actionbar_bg);

	}

	private void initData() {
		// TODO Auto-generated method stub
//		items=new ArrayList<Item>();
//		ArrayList<String> imgs=new ArrayList<String>();
//		imgs.add("http://img04.muzhiwan.com/2015/06/16/upload_557fd293326f5.jpg");
//		items.add(new Item(1, 1, 1, "1111111", "111111111", 12121, 111.0, 1, "1212", "1212", 1212 ,imgs));
//		ListView listView=(ListView) findViewById(R.id.item_list);
//		listView.setAdapter(new ItemListAdapter(items, MainActivity.this));
	}

	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	public void setActionBarLayout( int layoutId ){
		ActionBar actionBar = getActionBar( );
		if( null != actionBar ){
			actionBar.setDisplayShowHomeEnabled( false );
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);

			LayoutInflater inflator = LayoutInflater.from(MainActivity.this);
			View v = inflator.inflate(layoutId, null);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(v,layout);
		}
	}
}
