package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.product.SaleProductInfo;

public class ProductStatisticAdapter extends BaseAdapter {
	private Context context;
	List<SaleProductInfo> mProductListInfo = null;

	public ProductStatisticAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		if (mProductListInfo == null)
			return 0;
		return mProductListInfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void updateList(List<SaleProductInfo> mProductInfoList) {
		this.mProductListInfo = mProductInfoList;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.product_statistic_item, null);
			holder.productName = (TextView) convertView.findViewById(R.id.product_name);
			holder.productCount = (TextView) convertView.findViewById(R.id.sale_product_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.productName.setText(mProductListInfo.get(position).getProductName());
		holder.productCount.setText("" + mProductListInfo.get(position).getCount() + context.getString(R.string.ge));
		return convertView;
	}

	static class ViewHolder {
		public TextView productName;
		public TextView productCount;
	}
}
