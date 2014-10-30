package com.koolyun.mis.adapter;

import java.util.ArrayList;

import com.koolyun.mis.R;
import com.koolyun.mis.bean.TIDBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BandTIDAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<TIDBean> list;

	public BandTIDAdapter(Context context, ArrayList<TIDBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		if (v == null) {
			v = LayoutInflater.from(context).inflate(R.layout.select_tid_item, null);
		}

		TextView tidText = (TextView) v.findViewById(R.id.tidText);
		TextView sidText = (TextView) v.findViewById(R.id.sidText);
		tidText.setText(list.get(position).gettID());
		sidText.setText(list.get(position).getsID());

		return v;
	}
}
