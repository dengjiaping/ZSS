package com.koolyun.mis.adapter;

import java.util.LinkedList;
import java.util.List;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.LiteOrderInfo;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.core.order.OrderData.OrderStatus;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.MoneyView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class RecentSalesAdapter extends BaseExpandableListAdapter {

	List<List<LiteOrderInfo>> mListGroup = null;

	List<LiteOrderInfo> mProblemList = null;
	List<LiteOrderInfo> mHangupList = null;
	List<LiteOrderInfo> mRecentList = null;
	int[] groupName = { R.string.need_reverse, R.string.pause_deal, R.string.resent_sale };
	int[] hasData = { 0, 0, 0 };
	private LayoutInflater listInflater;

	int problemCount = 0;
	int hangupCount = 0;
	int normalCount = 0;

	public RecentSalesAdapter(Context context) {

		listInflater = LayoutInflater.from(context);
		updateList();
	}

	public void updateList() {
		mListGroup = new LinkedList<List<LiteOrderInfo>>();
		mHangupList = OrderManager.getOrdersByStatus(OrderStatus.ORDER_HANGUP.toInt());
		mRecentList = OrderManager.getRecentOrders();

		mProblemList = OrderManager.getOrdersByStatus(OrderStatus.ORDER_PERREFUND.toInt());
		List<LiteOrderInfo> tmp2 = OrderManager.getOrdersByStatus(OrderStatus.ORDER_PRESTORE.toInt());
		mProblemList.addAll(tmp2);

		problemCount = mProblemList.size();
		hangupCount = mHangupList.size();
		normalCount = mRecentList.size();

		hasData[0] = problemCount;
		hasData[1] = hangupCount;
		hasData[2] = normalCount;

		mListGroup.add(mProblemList);
		mListGroup.add(mHangupList);
		mListGroup.add(mRecentList);
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

		LiteOrderInfo tmp = mListGroup.get(getPositionInList(groupPosition)).get(childPosition);
		holder.orderTime.setText(Common.getDateTimeString(tmp.getOrder().getModifyTime()));

		int orderStatus = mListGroup.get(getPositionInList(groupPosition)).get(childPosition).getOrder()
				.getOrderStatusID();
		if (orderStatus == OrderStatus.ORDER_HANGUP.toInt()) {
			holder.notPay.setText(R.string.notpay);
			holder.orderTime.setText(Common.getSerialNumberFromBillNo(mListGroup.get(getPositionInList(groupPosition))
					.get(childPosition).getOrder().getBillNo()));
		} else if (orderStatus == OrderStatus.ORDER_UNDO.toInt()) {
			holder.notPay.setText(R.string.alreadyrepeal);
		} else {
			holder.notPay.setText("");
		}

		if (tmp.getOrderRemark() != null && tmp.getOrderRemark() != null && !tmp.getOrderRemark().isEmpty())
			holder.orderDesp.setText(tmp.getOrderRemark());
		else
			holder.orderDesp.setText(tmp.getDescription());
		holder.orderAmount.setMoney(tmp.getAmount());

		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View mtmp = listInflater.inflate(R.layout.simple_ordergroup_item, null);
		TextView tvt = (TextView) mtmp.findViewById(R.id.order_group_item);
		tvt.setText(groupName[getPositionInList(groupPosition)]);
		return mtmp;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mListGroup.get(getPositionInList(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mListGroup.get(getPositionInList(groupPosition));
	}

	public int getPositionInList(int groupPosition) {
		// 找第groupPosition个非0值

		int index = 0;
		for (int i = 0; i < 3; i++) {
			if (hasData[i] > 0)
				index++;
			if (index == groupPosition + 1)
				return i;
		}

		return index;

	}

	@Override
	public int getGroupCount() {
		int count = 0;

		if (problemCount > 0)
			count++;
		if (hangupCount > 0)
			count++;
		if (normalCount > 0)
			count++;
		return count;
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

		return mListGroup.get(getPositionInList(groupPosition)).get(childPosition);
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
