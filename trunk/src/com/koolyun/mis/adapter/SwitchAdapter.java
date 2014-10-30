package com.koolyun.mis.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.myinterface.OnUpdateOnsale;
import com.koolyun.mis.util.BasicArithmetic;

public class SwitchAdapter extends BaseAdapter {
	private class buttonViewHolder {
		Switch buttonClose;
		TextView textView;
	}

	private List<Onsale> mOnsaleList = OrderManager.getAllOnSaleList();
	private List<Onsale> selectedList = new LinkedList<Onsale>();
	private OnUpdateOnsale mOnUpdateOnsale = null;
	private LayoutInflater mInflater;
	private Context mContext;
	private buttonViewHolder holder;
	private String oneprice = null;

	public SwitchAdapter(Context c, OnUpdateOnsale onsaleCallBack, List<Onsale> mSelectedOnsaleList) {
		mOnUpdateOnsale = onsaleCallBack;
		InitSwitchStates(mSelectedOnsaleList);
		mContext = c;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private void InitSwitchStates(List<Onsale> mSelectedOnsaleList) {
		for (int j = 0; j < mSelectedOnsaleList.size(); j++) {
			addOnsale(mSelectedOnsaleList.get(j));
		}
	}

	void addOnsale(Onsale mOnsale) {
		if (mOnsale == null)
			return;
		for (int i = 0; i < selectedList.size(); i++) {
			if (mOnsale.getOnsaleID() == selectedList.get(i).getOnsaleID())
				return;
		}
		selectedList.add(mOnsale);
	}

	void removeOnsale(Onsale mOnsale) {
		if (mOnsale == null)
			return;
		for (int i = 0; i < selectedList.size(); i++) {
			if (mOnsale.getOnsaleID() == selectedList.get(i).getOnsaleID())
				selectedList.remove(i);
		}
	}

	boolean isOnsaleChecked(Onsale mOnsale) {
		if (mOnsale == null)
			return false;
		for (int i = 0; i < selectedList.size(); i++) {
			if (mOnsale.getOnsaleID() == selectedList.get(i).getOnsaleID())
				return true;
		}
		return false;
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

	public void removeItem(int position) {
		mOnsaleList.remove(position);
		this.notifyDataSetChanged();
	}

	private void upDateOnSaleList() {
		if (mOnUpdateOnsale != null) {
			mOnUpdateOnsale.onOnSaleChanged(selectedList);
		}
	}

	public void updateOnePrice(String price) {
		oneprice = price;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (buttonViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.dialog_onsale_listview, null);
			holder = new buttonViewHolder();
			holder.buttonClose = (Switch) convertView.findViewById(R.id.dialog_onsale_switchBtn);
			holder.textView = (TextView) convertView.findViewById(R.id.dialog_onsale_show_textview);
			holder.buttonClose.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int pos = (Integer) buttonView.getTag();
					if (isChecked) {
						addOnsale(mOnsaleList.get(pos));
					} else {
						removeOnsale(mOnsaleList.get(pos));
					}
					upDateOnSaleList();
				}
			});

			convertView.setTag(holder);
		}
		holder.buttonClose.setTag(position);
		holder.textView.setText(mOnsaleList.get(position).getOnsaleName());
		holder.buttonClose.setClickable(true);
		if (oneprice != null) {
			if (mOnsaleList.get(position).getOnsaleType() == 1) {
				String tmpPrice = BasicArithmetic.add(mOnsaleList.get(position).getValue(), oneprice);
				if (tmpPrice.startsWith("-") && !holder.buttonClose.isChecked())
					holder.buttonClose.setClickable(false);
			}
		}
		if (isOnsaleChecked(mOnsaleList.get(position))) {
			holder.buttonClose.setChecked(true);
		} else {
			holder.buttonClose.setChecked(false);
		}

		return convertView;
	}
}
