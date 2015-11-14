package com.wodangjia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.activity.StroeManager;
import com.wodangjia.activity.SubmitOrderActivity;
import com.wodangjia.app.App;
import com.wodangjia.bean.Order;
import com.wodangjia.bean.Orders;
import com.wodangjia.bean.Wallet;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.NoScrollListView;
import com.zf.iosdialog.widget.CustomAlertDialog;

import android.R.integer;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoreOrderListviewAdapter extends BaseAdapter {

	ArrayList<Orders> items;
	Context context;
	LayoutInflater mInflater;

	ViewHolder viewHolder;

	public StoreOrderListviewAdapter(ArrayList<Orders> items, Context context) {
		super();
		this.items = items;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public ArrayList<Orders> getItems() {
		return items;
	}

	public void setItems(ArrayList<Orders> items) {
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

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.order_listview_item, null);
			viewHolder.orderItemOrderNo = (TextView) convertView
					.findViewById(R.id.order_no);
			viewHolder.order_goods_listview = (NoScrollListView) convertView
					.findViewById(R.id.order_goods_listview);
			viewHolder.btn_order_detail = (Button) convertView
					.findViewById(R.id.btn_order_detail);
			viewHolder.btn_1 = (Button) convertView.findViewById(R.id.button1);
			viewHolder.btn_2 = (Button) convertView.findViewById(R.id.button2);
			viewHolder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_allcount = (TextView) convertView
					.findViewById(R.id.label_count);
			viewHolder.tv_allprice = (TextView) convertView
					.findViewById(R.id.all_price);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Orders order = items.get(position);
		OrderGoodsListviewAdapter adapter = new OrderGoodsListviewAdapter(
				order.getItems(), context);
		viewHolder.order_goods_listview.setAdapter(adapter);
		viewHolder.orderItemOrderNo.setText(order.getOrder_no());
		viewHolder.tv_time.setText(order.getOrder_create_time());

		double allprice = 0;// 合计
		int allcount = 0;// 总数量
		for (int i = 0; i < order.getItems().size(); i++) {
			allcount += order.getItems().get(i).getCount();
			allprice += order.getItems().get(i).getCount()
					* order.getItems().get(i).getItem().getGoods_price();
		}
		viewHolder.tv_allcount.setText("共 " + allcount + " 件");
		viewHolder.tv_allprice.setText(String.format("%.2f", allprice) + "元  ");
		viewHolder.btn_order_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		if (order.getOrder_status() == 0) {
			viewHolder.btn_1.setVisibility(View.VISIBLE);
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.btn_1.setText("关闭订单");
			viewHolder.tv_status.setText("等待付款");
			viewHolder.btn_1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateOrderStatus(order, 5);
				}
			});
			
		} else if (order.getOrder_status() == 1) {
			viewHolder.btn_1.setVisibility(View.VISIBLE);
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.btn_1.setText("立即发货");
			viewHolder.tv_status.setText("等待发货");
			viewHolder.btn_1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					showSendDialog(order);
					updateOrderStatus(order, 2);
				}
			});
		} else if (order.getOrder_status() == 2) {
			viewHolder.btn_1.setVisibility(View.GONE);
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.tv_status.setText("等待收货");
		} else if (order.getOrder_status() == 3) {
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.btn_1.setVisibility(View.GONE);
			viewHolder.tv_status.setText("已完成");
		} else if (order.getOrder_status() == 4) {
			viewHolder.btn_1.setVisibility(View.VISIBLE);
			viewHolder.btn_1.setText("同意退款");
			viewHolder.btn_1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateOrderStatus(order,6);
				}
			});
			viewHolder.btn_2.setVisibility(View.VISIBLE);
			viewHolder.btn_2.setText("拒绝退款");
			viewHolder.btn_2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateOrderStatus(order,1);
				}
			});
			viewHolder.tv_status.setText("退款中");

		} else if (order.getOrder_status() == 5) {
			viewHolder.btn_1.setVisibility(View.GONE);
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.tv_status.setText("已关闭");
		}else if (order.getOrder_status() == 7) {
			viewHolder.btn_1.setVisibility(View.GONE);
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.tv_status.setText("已完成");
		}
		return convertView;
	}

	protected void showSendDialog(Orders order) {
		LinearLayout layout = (LinearLayout) mInflater.inflate(
				R.layout.dialog_send_goods, null);
//		final EditText et_storename = (EditText) layout
//				.findViewById(R.id.et_storename);
//		et_storename.setText(App.user.getStore_name());
		final CustomAlertDialog dialog = new CustomAlertDialog(context).builder()
				.setTitle("店铺名称").setView(layout)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}

	private class ViewHolder {
		TextView orderItemOrderNo, tv_status, tv_time, tv_allcount,
				tv_allprice;
		NoScrollListView order_goods_listview;
		Button btn_order_detail, btn_1, btn_2;
	}


	void updateOrderStatus(final Orders order, final int order_status) {

		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION,
				Config.ACTION_UPDATE_ORDER_STATUS);
		params.addBodyParameter("order_id", "" + order.getOrder_id());
		params.addBodyParameter("order_status", "" + order_status);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils.showToast(context,
								Config.ERROR_UNCONNECTION_INTERNET);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == 0) {
							// items.remove(order);
							if(order_status==6){
								order.setOrder_status(5);
								StringUtils.showToast(context, ""+order_status);
							}else{
								order.setOrder_status(order_status);
							}
							
							notifyDataSetChanged();
							return;
						}
						StringUtils.showToast(context, "操作失败！");
					}
				});
	}

}
