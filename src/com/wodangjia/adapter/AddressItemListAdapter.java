package com.wodangjia.adapter;

import io.rong.imkit.fragment.ClearConversationMsgFragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.activity.AddressActivity;
import com.wodangjia.activity.AddressEditActivity;
import com.wodangjia.app.App;
import com.wodangjia.bean.Address;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;

public class AddressItemListAdapter extends BaseAdapter {
	
	// 通过构造器获取数据来源
	List<Address> list ;
	// 接收上下文参数，以便了解是哪个activity在调用
	Context context;
	// 用来初始化布局文件，可以把一个xml布局转换为对应的android对象view
	LayoutInflater mInflater;
	ViewHolder viewHolder;

	public AddressItemListAdapter(List<Address> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public List<Address> getList() {
		return list;
	}

	public void setList(List<Address> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position,View convertView, ViewGroup parent) {
		//告知列表每一行的布局样式，同时指定每一行的数据来自哪里
		//position:列表的索引，从0开始；convertView是每一行的布局；parent每一行的父布局，没有则写null
		//这里，每一行的布局是item.xml
		
		if (convertView == null) {
			convertView=mInflater.inflate(R.layout.item_address_item, null);
			//初始化每一行的控件
			viewHolder = new ViewHolder();
			viewHolder.nameView = (TextView) convertView.findViewById(R.id.address_receiver_name);
			viewHolder.phoneView = (TextView) convertView.findViewById(R.id.address_receiver_phone);
			viewHolder.provinceView = (TextView) convertView.findViewById(R.id.address_reciver_provice);
			viewHolder.cityView = (TextView) convertView.findViewById(R.id.address_reciver_city);
			viewHolder.countyView = (TextView) convertView.findViewById(R.id.address_reciver_county);
			viewHolder.addrView = (TextView) convertView.findViewById(R.id.address_reciver_det);
			viewHolder.checkView = (ImageView) convertView.findViewById(R.id.address_moren_img);
			viewHolder.ll_default=(LinearLayout) convertView.findViewById(R.id.ll_defaull);
			viewHolder.ll_edit=(LinearLayout) convertView.findViewById(R.id.ll_edit);
			viewHolder.ll_delete=(LinearLayout) convertView.findViewById(R.id.ll_delete);
			//首次调用getView，创建viewHolder实例，初始化控件，并把这个实例缓存到界面上
			convertView.setTag(viewHolder);
		}else {
			//不是第一次调用，直接取出缓存中的viewholder即可
			viewHolder = (ViewHolder) convertView.getTag();
		}	
		//指定每一行布局对应的数据
		final Address item = list.get(position);
		String name = item.getName();
		viewHolder.nameView.setText(name);
	    String phone = item.getPhone();
		viewHolder.phoneView.setText(phone);
		String province = item.getProvince();
		viewHolder.provinceView.setText(province);
		String city = item.getCity();
		viewHolder.cityView.setText(city);
		String county =item.getCounty();
		viewHolder.countyView.setText(county);
		final String address = item.getDetail();
		viewHolder.addrView.setText(address);
		if(item.isDefaultt()){
			viewHolder.checkView.setImageResource(R.drawable.address_check);
		}else{
			viewHolder.checkView.setImageResource(R.drawable.address_uncheck);
			viewHolder.ll_default.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateAddress(2,item);
				}
			});
		}
		
		
		viewHolder.ll_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateAddress(1,item);
			}
		});
		viewHolder.ll_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context,"编辑地址", 1).show();
				Intent intent=new Intent(context, AddressEditActivity.class);
				intent.putExtra("address", item);
				AddressActivity activity=(AddressActivity) context;
				activity.startActivityForResult(intent, 1);
				
			}
		});
		return convertView;
	}
	private  class ViewHolder
    {
        TextView nameView,phoneView,provinceView,cityView,countyView,addrView;
        ImageView checkView;
        LinearLayout ll_default,ll_edit,ll_delete;
        

    }
	
	void updateAddress(final int operation ,final Address address){
		
		
		RequestParams params=new RequestParams();
		if(operation==1){//删除操作
			params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_DELETE_ADDRESS);
		}else if(operation==2){//设为默认地址
			params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_SET_DEFAULT_ADDRESS);
		}
		params.addBodyParameter(Config.KEY_ADDRESS_ID, ""+address.getId());
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(context, "操作失败");
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
				if(status==0){
					if(operation==1){
						list.remove(address);
						
					}else if(operation==2){
						ClearDefault();
						address.setDefaultt(true);
						
//						list.remove(address);
//						list.add(0, address);
					}
					notifyDataSetChanged();
				}
				
			}
		});
	}

	protected void ClearDefault() {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setDefaultt(false);
		}
	}

}
