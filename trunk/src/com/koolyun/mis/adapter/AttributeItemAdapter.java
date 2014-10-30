package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.product.ProductAttribute;

public class AttributeItemAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private List<ProductAttribute> list = null;

	public AttributeItemAdapter(Context context) {
		listInflater = LayoutInflater.from(context);

	}

	public void setAttrList(List<ProductAttribute> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return list.size();
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
			convertView = listInflater.inflate(R.layout.productmanager_attr_item_layout, null);

			holder.showTextView = (TextView) convertView.findViewById(R.id.attr_name);
			holder.deletBtn = (Button) convertView.findViewById(R.id.attr_remove);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.deletBtn.setVisibility(View.INVISIBLE);
		// final int pos = position;
		// holder.deletBtn.setOnClickListener(new OnClickListener(){
		// @Override
		// public void onClick(View v) {
		// list.remove(list.get(pos));
		// AttributeItemAdapter.this.notifyDataSetChanged();
		// }
		//
		// });
		holder.showTextView.setText(list.get(position).getName());

		return convertView;

	}

	public class ViewHolder {
		public TextView showTextView;
		public Button deletBtn;
	}

}
