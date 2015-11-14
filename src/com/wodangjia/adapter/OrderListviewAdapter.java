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
import com.wodangjia.activity.BuyOrderActivity;
import com.wodangjia.activity.CommentActivity;
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
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderListviewAdapter extends BaseAdapter {

	ArrayList<Orders> items;
	Context context;
	LayoutInflater mInflater;

	ViewHolder viewHolder;

	public OrderListviewAdapter(ArrayList<Orders> items, Context context) {
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

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
		order.setOrder_value(allprice);
		viewHolder.tv_allcount.setText("共 " + allcount + " 件");
		viewHolder.tv_allprice.setText(String.format("%.2f", allprice) + "元  ");
		viewHolder.btn_order_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		if (order.getOrder_status() == 0) {
			viewHolder.btn_1.setVisibility(View.VISIBLE);
			viewHolder.btn_2.setVisibility(View.VISIBLE);
			viewHolder.btn_1.setText("立即付款");
			viewHolder.btn_2.setText("取消订单");
			viewHolder.tv_status.setText("待付款");
			viewHolder.btn_1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showPayDialog(order);
				}
			});
			viewHolder.btn_2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateOrderStatus(order, 5);// 关闭订单

				}
			});
		} else if (order.getOrder_status() == 1) {
			viewHolder.btn_1.setVisibility(View.VISIBLE);
			viewHolder.btn_1.setText("申请退款");
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.tv_status.setText("待发货");
			viewHolder.btn_1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateOrderStatus(order, 4);
				}
			});
		} else if (order.getOrder_status() == 2) {
			viewHolder.btn_1.setVisibility(View.VISIBLE);
			viewHolder.btn_1.setText("确认收货");
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.tv_status.setText("待收货");
			viewHolder.btn_1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateOrderStatus(order, 3);
				}
			});
		} else if (order.getOrder_status() == 3) {
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.btn_1.setVisibility(View.VISIBLE);
			viewHolder.btn_1.setText("评价");
			viewHolder.btn_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, CommentActivity.class);
					intent.putExtra("orders", order);
					BuyOrderActivity activity = (BuyOrderActivity) context;
					activity.startActivityForResult(intent,
							Config.REQUEST_CODE_COMMENT);
				}
			});
			viewHolder.tv_status.setText("已完成");
		} else if (order.getOrder_status() == 4) {
			viewHolder.btn_1.setVisibility(View.VISIBLE);
			viewHolder.btn_1.setText("取消申请");
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.tv_status.setText("退款中");
			viewHolder.btn_1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateOrderStatus(order, 1);
				}
			});

		} else if (order.getOrder_status() == 5) {
			viewHolder.btn_1.setVisibility(View.GONE);
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.tv_status.setText("已关闭");
		} else if (order.getOrder_status() == 7) {
			viewHolder.btn_1.setVisibility(View.GONE);
			viewHolder.btn_2.setVisibility(View.GONE);
			viewHolder.tv_status.setText("已完成");
		}

		return convertView;
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
							order.setOrder_status(order_status);
							notifyDataSetChanged();
							return;
						}
						StringUtils.showToast(context, "操作失败！");
					}
				});
	}

	protected void showPayDialog(final Orders orders) {
		LinearLayout layout = (LinearLayout) mInflater.inflate(
				R.layout.dialog_pay_order, null);
		final TextView tv_pay_money = (TextView) layout
				.findViewById(R.id.tv_pay_money);

		tv_pay_money.setText(String.format("%.2f", orders.getOrder_value())
				+ "元");
		final TextView tv_banlance = (TextView) layout
				.findViewById(R.id.tv_banlance);
		getBanlance(tv_banlance);
		final CustomAlertDialog dialog = new CustomAlertDialog(context)
				.builder().setTitle("支付订单").setView(layout)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("付款", new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if(wallet.getWallet_balance()<allprice){
				// StringUtils.showToast(context, "余额不足支付订单！");
				// return;
				// }

				payOrder(orders, dialog);
			}
		});
		dialog.setCancelable(false);
		dialog.show();

	}

	protected void payOrder(final Orders order, final CustomAlertDialog dialog) {
		ArrayList<Integer> orderids = new ArrayList<Integer>();
		orderids.add(order.getOrder_id());
		RequestParams params = new RequestParams();

		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_PAY_ORDER);
		Gson gson = new Gson();
		params.addBodyParameter(Config.KEY_ORDERIDS, gson.toJson(orderids));
		params.addBodyParameter("allprice",
				String.format("%.2f", order.getOrder_value()));// 将总价发到服务端
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
							StringUtils.showToast(context, "支付完成！");
							order.setOrder_status(1);
							notifyDataSetChanged();
							dialog.dismiss();
						}

					}
				});
	}

	private void getBanlance(final TextView tv_banlance) {
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_WALLET_INFO);
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
							Gson gson = new Gson();
							Wallet wallet = gson.fromJson(JsonUtils
									.getJsonObject(arg0.result, "wallet")
									.toString(), Wallet.class);
							tv_banlance.setText(String.format("%.2f",
									wallet.getWallet_balance())
									+ "元");
						}
					}
				});

	}

}
