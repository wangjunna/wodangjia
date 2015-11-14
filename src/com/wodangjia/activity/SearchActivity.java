package com.wodangjia.activity;

import com.example.wodangjialayout.R;
import com.example.wodangjialayout.R.anim;
import com.example.wodangjialayout.R.color;
import com.example.wodangjialayout.R.id;
import com.example.wodangjialayout.R.layout;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SearchActivity extends Activity implements OnClickListener{

	private RadioGroup radiogroup;
	private EditText et_keyword;
	private boolean selectGoods=true;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ActivityUtils.setActionBarLayout(getActionBar(), this,R.layout.title_bar_serach);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,R.anim.anim_right_to_left_out);
		initView();
	}

	private void initView() {
		findViewById(R.id.btn_search).setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);
		et_keyword=(EditText) findViewById(R.id.keyword);

		radiogroup=(RadioGroup) findViewById(R.id.radiogroup);
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.rb_goods){
					selectGoods=true;
				}else {
					selectGoods=false;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			search();
			break;
		case R.id.btn_back:
			ActivityUtils.finish(this);
			break;

		default:
			break;
		}
		
	}

	private void search() {
		if(et_keyword.getText().length()==0){
			StringUtils.showToast(this,"请输入您要查询的商品、店铺！");
			return;
		}
		Intent intent;
		if (selectGoods) {
			intent =new Intent(this,SearchGoodsResultActivity.class);
			intent.putExtra("keyword", et_keyword.getText().toString());
		}
		else  {
			intent=new Intent(this, SearchStoreResultActivity.class);
			intent.putExtra("keyword", et_keyword.getText().toString());
		}
		startActivity(intent);
//		finish();
		
	}
	@Override
	public void onBackPressed() {
		ActivityUtils.finish(this);
	}

}
