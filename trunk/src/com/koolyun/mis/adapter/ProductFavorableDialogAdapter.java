package com.koolyun.mis.adapter;

import java.util.LinkedList;
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
import com.koolyun.mis.myinterface.OnUpdateOnsale;

public class ProductFavorableDialogAdapter extends BaseAdapter {
	private List<Onsale> mOnsaleList = OrderManager.getAllOnSaleList();
	private List<Onsale> selectedList = new LinkedList<Onsale>();
	private OnUpdateOnsale mOnUpdateOnsale = null;
	private LayoutInflater mInflater;
	private Context mContext;

	public ProductFavorableDialogAdapter(Context context, List<Onsale> mSelectedOnsaleList) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mOnsaleList = mOnsaleList;
		selectedList = mSelectedOnsaleList;
	}

	@Override
	public int getCount() {
		return mOnsaleList.size();
	}

	@Override
	public Object getItem(int position) {
		return mOnsaleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.product_detail_info, null);
			holder.OnsaleName = (TextView) convertView.findViewById(R.id.prodect_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Onsale os = mOnsaleList.get(position);
		holder.OnsaleName.setBackgroundResource(R.color.v2_gridview_item_bg_nor);
		for (int i = 0; i < selectedList.size(); i++) {
			if (os.getOnsaleID() == selectedList.get(i).getOnsaleID()) {
				holder.OnsaleName.setBackgroundResource(R.color.v2_gridview_item_bg_sel);
			}
		}
		holder.OnsaleName.setText(os.getOnsaleName());

		return convertView;
	}

	static class ViewHolder {
		public TextView OnsaleName;
		// public TextView OnsalePrice;
	}

}
