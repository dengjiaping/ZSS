package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.LiteOrderInfo;
import com.koolyun.mis.core.order.OrderData.OrderStatus;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.MoneyView;

public class LeftBarSaleRecordAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private Context context;
	List<LiteOrderInfo> mRecentList = null;
	
	public LeftBarSaleRecordAdapter(Context context){
		listInflater = LayoutInflater.from(context);
		this.context = context;
		mRecentList = OrderManager.getRecentOrders();
	}
	
	public void updateList(){
		mRecentList = OrderManager.getRecentOrders();
	}
	
	@Override
	public int getCount() {
		if(OrderManager.getRecentOrders() != null){
			return	mRecentList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mRecentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = listInflater.inflate(R.layout.simple_order_item, null);
			holder = new ViewHolder();
			holder.orderTime = (TextView) convertView.findViewById(R.id.order_time);
			holder.orderDesp = (TextView) convertView.findViewById(R.id.order_depcription);
			holder.notPay = (TextView) convertView.findViewById(R.id.notpaynote);
			holder.orderAmount = (MoneyView) convertView.findViewById(R.id.order_amount);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		LiteOrderInfo tmp = mRecentList.get(position);
		holder.orderTime.setText(Common.getDateTimeString(tmp.getOrder().getModifyTime()));
		int orderStatus = mRecentList.get(position).getOrder().getOrderStatusID();
		if(orderStatus == OrderStatus.ORDER_HANGUP.toInt()){
			holder.notPay.setText(R.string.notpay);
			holder.orderTime.setText(Common.getSerialNumberFromBillNo(mRecentList.get(position).getOrder().getBillNo()));
		}
		else if(orderStatus == OrderStatus.ORDER_UNDO.toInt()){
			holder.notPay.setText(R.string.alreadyrepeal);
		}
		else{
			holder.notPay.setText("");
		}
		if(tmp.getOrderRemark() != null && tmp.getOrderRemark() != null && !tmp.getOrderRemark().isEmpty())
			holder.orderDesp.setText(tmp.getOrderRemark());
		else
			holder.orderDesp.setText(tmp.getDescription());
		holder.orderAmount.setMoney(tmp.getAmount());
		return convertView;
	}
	
	public class ViewHolder {
		
		 public TextView orderTime;
		 public TextView notPay;
		 public TextView orderDesp;
		 public MoneyView orderAmount;

	}

}
