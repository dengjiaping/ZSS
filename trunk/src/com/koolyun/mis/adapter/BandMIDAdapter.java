package com.koolyun.mis.adapter;

import java.util.ArrayList;

import com.koolyun.mis.R;
import com.koolyun.mis.bean.MIDBean;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BandMIDAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MIDBean> list;

	public BandMIDAdapter(Context context, ArrayList<MIDBean> list) {
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = LayoutInflater.from(context).inflate(R.layout.select_mid_item, null);
		}
		TextView mNameText = (TextView) arg1.findViewById(R.id.mNameText);
		TextView mIDText = (TextView) arg1.findViewById(R.id.mIDText);

		mNameText.setText(list.get(arg0).getmName());
		mIDText.setText(list.get(arg0).getmID());

		if (arg0 % 2 == 0) {
			arg1.setBackgroundColor(Color.TRANSPARENT);
		} else {
			arg1.setBackgroundColor(context.getResources().getColor(R.color.gray));
		}

		return arg1;
	}
}
