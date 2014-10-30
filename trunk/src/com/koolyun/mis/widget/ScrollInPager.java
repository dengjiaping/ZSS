package com.koolyun.mis.widget;

import com.koolyun.mis.LoginInActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class ScrollInPager extends HorizontalScrollView {

	public ScrollInPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:

			// LoginInActivity.getViewPager().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			// LoginInActivity.getViewPager().requestDisallowInterceptTouchEvent(false);
			break;
		}
		return false;
	}
}
