package com.koolyun.mis.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ListView;

public class LeftBarListViewDeal extends ListView {

	public LeftBarListViewDeal(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyEvent(event);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
		// MeasureSpec.AT_MOST);
		// super.onMeasure(widthMeasureSpec, expandSpec);
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// float x = event.getX();
	// float y = event.getY();
	//
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// Log.i("AAAAAAAAAAAAAAA", "control parent focus....");
	// LeftBarProductItemInfoFragment.getScrollView().requestDisallowInterceptTouchEvent(true);
	// // /
	// //();
	// break;
	// case MotionEvent.ACTION_MOVE:
	//
	// LeftBarProductItemInfoFragment.getScrollView().requestDisallowInterceptTouchEvent(true);
	// // invalidate();
	// break;
	// case MotionEvent.ACTION_UP:
	//
	// // invalidate();
	//
	// case MotionEvent.ACTION_CANCEL:
	// LeftBarProductItemInfoFragment.getScrollView().requestDisallowInterceptTouchEvent(false);
	// break;
	// }
	// return false;
	// }
	//
	//
}
