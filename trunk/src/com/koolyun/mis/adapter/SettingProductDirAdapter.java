package com.koolyun.mis.adapter;

import com.koolyun.mis.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingProductDirAdapter extends BaseAdapter {
	LayoutInflater layoutInfalter;
	
	public SettingProductDirAdapter(Context context){
		layoutInfalter = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if(convertView == null){
			holderView = new HolderView();
			convertView = layoutInfalter.inflate(R.layout.setting_product_item_layout, null);
			holderView.settingProductDirItemText = (TextView) convertView.findViewById(R.id.settingProductDirItemText);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();		
		}
		return convertView;
	}
	
	static class HolderView{
		public TextView settingProductDirItemText;
	}

}
