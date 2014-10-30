package com.koolyun.mis.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koolyun.mis.R;

public class ListViewAdapter extends BaseAdapter {

	private List<Map<String, Object>> listItems; // 商品信息集合
	private LayoutInflater listInflater;

	public ListViewAdapter(Context context, List<Map<String, Object>> listItems) {
		this.listItems = listItems;
		listInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listItems.size();
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
			convertView = listInflater.inflate(R.layout.allproduct_item_info, null);
			holder.showImageKind = (ImageView) convertView.findViewById(R.id.allproductimageview);
			holder.showTextView = (TextView) convertView.findViewById(R.id.allproducttextview);
			holder.showImageMore = (ImageView) convertView.findViewById(R.id.allproductimageviewmore);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.showImageKind.setBackgroundResource((Integer) listItems.get(position).get("image"));
		holder.showTextView.setText((CharSequence) listItems.get(position).get("catagory"));
		holder.showImageMore.setBackgroundResource(R.drawable.item_more);
		return convertView;
	}

	static class ViewHolder {
		public TextView showTextView;
		public ImageView showImageKind;
		public ImageView showImageMore;
	}
}
