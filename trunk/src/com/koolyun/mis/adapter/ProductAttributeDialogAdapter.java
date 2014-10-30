package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.product.GridViewProductAttribute;

public class ProductAttributeDialogAdapter extends BaseAdapter {

	List<GridViewProductAttribute> mOnsaleList = null;
	private LayoutInflater listInflater;
	OrderContentData mOrderContent;

	public ProductAttributeDialogAdapter(Context context, List<GridViewProductAttribute> data,
			OrderContentData orderContent) {
		listInflater = LayoutInflater.from(context);
		this.mOnsaleList = data;
		this.mOrderContent = orderContent;
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
			convertView = listInflater.inflate(R.layout.product_detail_info, null);
			holder.OnsaleName = (TextView) convertView.findViewById(R.id.prodect_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GridViewProductAttribute pa = mOnsaleList.get(position);
		if (pa.getType() == 0) {
			// 父
			holder.OnsaleName.setBackgroundResource(R.color.v2_b6e2fb);
			holder.OnsaleName.setClickable(false);
		} else {
			if (pa.getStatue() == 0) {
				holder.OnsaleName.setBackgroundResource(R.color.v2_gridview_item_bg_nor);
			} else {
				holder.OnsaleName.setBackgroundResource(R.color.v2_gridview_item_bg_sel);
			}
		}

		holder.OnsaleName.setText(pa.getName());

		return convertView;
	}

	static class ViewHolder {
		public TextView OnsaleName;
		// public TextView OnsalePrice;
	}

}
