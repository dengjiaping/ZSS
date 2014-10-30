package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koolyun.mis.R;

public class UpdateAdapter extends BaseAdapter {
	List<Integer> dataList = null;
	Context context = null;

	public UpdateAdapter(Context context, List<Integer> dataList) {
		super();
		this.dataList = dataList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void notifyhas() {
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckView holder = new CheckView(context, position);
		holder.showTextView.setText(dataList.get(position));
		return holder;
	}

	public class CheckView extends RelativeLayout implements Checkable {
		boolean isChecked = false;
		public TextView showTextView;
		public TextView showTextPrice;
		public ImageView mImageVIew;
		public Button deletBtn;
		View result = null;

		public CheckView(Context context, int position) {
			super(context);
			LayoutInflater.from(context).inflate(R.layout.update_listview_item, this, true);
			showTextView = (TextView) this.findViewById(R.id.update_show_name);

			mImageVIew = (ImageView) this.findViewById(R.id.update_bg_sub);
			AbsListView.LayoutParams clp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 100);
			this.setLayoutParams(clp);
			this.setMinimumHeight(100);
			this.setGravity(Gravity.CENTER);
			if (isChecked) {
				mImageVIew.setVisibility(View.VISIBLE);
			} else {
				mImageVIew.setVisibility(View.INVISIBLE);
			}
			if (position == 0)
				mImageVIew.setVisibility(View.VISIBLE);
			else
				mImageVIew.setVisibility(View.GONE);
		}

		public CheckView(Context context, AttributeSet attrs) {
			super(context, attrs);
			LayoutInflater.from(context).inflate(R.layout.update_listview_item, this, true);
			showTextView = (TextView) this.findViewById(R.id.update_show_name);
			mImageVIew = (ImageView) this.findViewById(R.id.update_bg_sub);
			this.setMinimumHeight(100);
			this.setGravity(Gravity.CENTER);

			if (isChecked) {
				mImageVIew.setVisibility(View.VISIBLE);
			} else {
				mImageVIew.setVisibility(View.INVISIBLE);
			}

		}

		@Override
		public boolean isChecked() {
			return isChecked;
		}

		@Override
		public void setChecked(boolean checked) {
			isChecked = checked;
			if (isChecked) {
				mImageVIew.setVisibility(View.VISIBLE);
			} else {
				mImageVIew.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void toggle() {
			isChecked = !isChecked;
			if (isChecked) {
				mImageVIew.setVisibility(View.VISIBLE);
			} else {
				mImageVIew.setVisibility(View.INVISIBLE);
			}
		}

	}

}
