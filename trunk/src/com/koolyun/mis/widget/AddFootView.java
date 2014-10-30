package com.koolyun.mis.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.koolyun.mis.R;
import com.koolyun.mis.fragment.LeftBarFragment;
import com.koolyun.mis.util.FileManager;

public class AddFootView extends LinearLayout {

	Context mContext = null;
	int mId = 0;
	LeftBarFragment leftbarDealFlipper = null;

	public AddFootView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;

	}

	public AddFootView(Context context, LeftBarFragment leftbarDealFlipper, String name, int picture) {
		super(context);
		mContext = context;
		this.leftbarDealFlipper = leftbarDealFlipper;
		getButton(name, picture);
	}

	public AddFootView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

	}

	public void setId(int id) {
		this.mId = id;
	}

	private void getButton(String name, final int resId) {
		AbsListView.LayoutParams layoutParamsImageMain = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, 60);
		Button button = new Button(mContext);
		BitmapDrawable bitmapleft = FileManager.readBitMapDrawable(mContext, resId);
		BitmapDrawable bitmapright = FileManager.readBitMapDrawable(mContext, R.drawable.recent_more);
		bitmapleft.setBounds(0, 0, 50, 50);
		bitmapright.setBounds(0, 0, 50, 50);
		button.setCompoundDrawables(bitmapleft, null, bitmapright, null);
		button.setBackgroundResource(R.drawable.digtalnumber_button_xml);

		button.setGravity(Gravity.CENTER);
		button.setText(name);
		button.setPadding(10, 0, 10, 0);
		button.setTextSize(20);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (resId == R.drawable.recent_all)
						leftbarDealFlipper.addLeftBarAllBillFragment();
					else
						leftbarDealFlipper.addStatistikFragment();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		setPadding(0, 10, 0, 20);
		addView(button, layoutParamsImageMain);
	}
}
