package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.core.order.OrderManager;

public class OnSaleAdapter extends BaseAdapter {

	private List<Onsale> mOnsaleList = OrderManager.getAllOnSaleList();

	private LayoutInflater listInflater;

	public OnSaleAdapter(Context context) {
		listInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mOnsaleList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.onsale_item_info, null);
			holder.OnsaleName = (TextView) convertView.findViewById(R.id.onsale_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Onsale mOnsale = mOnsaleList.get(position);
		holder.OnsaleName.setText(mOnsale.getOnsaleName());
		// if (mOnsale.getOnsaleType() == 1) {
		// String tmps = mOnsale.getValue().substring(1);
		// holder.OnsalePrice.setText("-￥ " + tmps);
		// } else if (mOnsale.getOnsaleType() == 2) {
		// holder.OnsalePrice.setText(mOnsale.getValue() + " %");
		// }

		return convertView;
	}

	static class ViewHolder {
		public TextView OnsaleName;
		// public TextView OnsalePrice;
	}

}
