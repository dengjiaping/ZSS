package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.LiteOrderInfo;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.core.order.OrderData.OrderStatus;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.MoneyView;

public class AllSalesAdapter extends BaseExpandableListAdapter {

	List<List<LiteOrderInfo>> mListGroup = OrderManager.getOrderListDailyByStatus();

	private LayoutInflater listInflater;
	private Context context = null;

	public AllSalesAdapter(Context context) {
		this.context = context;
		listInflater = LayoutInflater.from(context);
		mListGroup = OrderManager.getOrderListDailyByStatus();
	}

	public void updateList() {
		mListGroup = OrderManager.getOrderListDailyByStatus();
		this.notifyDataSetChanged();
	}

	public int getGroupIndexByTime(String dateStr) {
		for (int i = 0; i < mListGroup.size(); i++) {
			String tmpDatestr = Common.getDateTimeString(mListGroup.get(i).get(0).getOrder().getModifyTime(),
					"yyyy-MM-dd");
			Log.d("STRCMP", dateStr + "|   |" + tmpDatestr + "|" + dateStr.equals(tmpDatestr));
			if (dateStr.equals(tmpDatestr))
				return i;
		}
		return -1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = listInflater.inflate(R.layout.simple_order_item, null);
			holder = new ViewHolder();
			holder.orderTime = (TextView) convertView.findViewById(R.id.order_time);
			holder.orderDesp = (TextView) convertView.findViewById(R.id.order_depcription);
			holder.notPay = (TextView) convertView.findViewById(R.id.notpaynote);
			holder.orderAmount = (MoneyView) convertView.findViewById(R.id.order_amount);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LiteOrderInfo tmp = mListGroup.get(groupPosition).get(childPosition);
		holder.orderTime.setText(Common.getAllDateTimeString(tmp.getOrder().getModifyTime()));
		holder.orderAmount.setMoney(tmp.getAmount());

		if (tmp.getOrderRemark() != null && tmp.getOrderRemark() != null && !tmp.getOrderRemark().isEmpty())
			holder.orderDesp.setText(tmp.getOrderRemark());
		else {
			String description = OrderManager.getDescriptionById(tmp.getOrder().getOrderID());
			holder.orderDesp.setText(description);
		}

		int orderStatus = mListGroup.get(groupPosition).get(childPosition).getOrder().getOrderStatusID();
		if (orderStatus == OrderStatus.ORDER_HANGUP.toInt()) {
			holder.notPay.setText(context.getString(R.string.notpay));
		} else if (orderStatus == OrderStatus.ORDER_UNDO.toInt()) {
			holder.notPay.setText(context.getString(R.string.alreadyrepeal));
		} else {
			holder.notPay.setText("");
		}
		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View mtmp = listInflater.inflate(R.layout.simple_ordergroup_item, null);
		TextView tvt = (TextView) mtmp.findViewById(R.id.order_group_item);
		String datestr = Common.getDateTimeString(mListGroup.get(groupPosition).get(0).getOrder().getModifyTime(),
				"  yyyy-MM-dd");
		tvt.setText(datestr);
		return mtmp;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mListGroup.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mListGroup.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mListGroup.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		return mListGroup.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	public class ViewHolder {

		public TextView orderTime;
		public TextView notPay;
		public TextView orderDesp;
		public MoneyView orderAmount;

	}
}
