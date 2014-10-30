package com.koolyun.mis.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class ClickTextScroll extends HorizontalScrollView {

	public ClickTextScroll(Context context) {
		super(context);

	}

	public ClickTextScroll(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public ClickTextScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d("ClickTextScroll", "dispatchKeyEvent ACTION : " + event.getAction());
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d("ClickTextScroll", "onTouchEvent ACTION : " + ev.getAction());
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.d("ClickTextScroll", "onInterceptTouchEvent ACTION : " + ev.getAction());
		return super.onInterceptTouchEvent(ev);
	}

}
