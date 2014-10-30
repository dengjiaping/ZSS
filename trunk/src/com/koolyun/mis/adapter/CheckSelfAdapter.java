package com.koolyun.mis.adapter;

import java.util.LinkedList;
import java.util.List;

import com.koolyun.mis.R;
import com.koolyun.mis.core.CheckSelfItemInfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckSelfAdapter extends BaseAdapter {

	List<CheckSelfItemInfo> mcheckResult = new LinkedList<CheckSelfItemInfo>();
	LayoutInflater listInflater = null;
	private Drawable mRight;
	private Drawable mWrong;

	public void addItemInfo(CheckSelfItemInfo mCheckSelfItemInfo) {
		mcheckResult.add(mCheckSelfItemInfo);
		this.notifyDataSetChanged();
	}

	public void removeAllInfo() {
		mcheckResult.clear();
		this.notifyDataSetChanged();
	}

	public CheckSelfAdapter(Context context) {
		listInflater = LayoutInflater.from(context);
		mRight = context.getResources().getDrawable(R.drawable.yes);
		mWrong = context.getResources().getDrawable(R.drawable.no);
	}

	@Override
	public int getCount() {
		return mcheckResult.size();
	}

	@Override
	public Object getItem(int position) {
		return mcheckResult.get(position);
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
			convertView = listInflater.inflate(R.layout.check_self_item, null);
			holder.checkIcon = (ImageView) convertView.findViewById(R.id.check_icon_tv);
			holder.checkInfo = (TextView) convertView.findViewById(R.id.check_info_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mcheckResult.get(position).isDevStatus())
			holder.checkIcon.setBackground(mRight);
		else
			holder.checkIcon.setBackground(mWrong);
		holder.checkInfo.setText(mcheckResult.get(position).getDevInfo());
		return convertView;
	}

	static class ViewHolder {
		public ImageView checkIcon;
		public TextView checkInfo;
	}
}
