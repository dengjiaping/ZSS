package com.koolyun.mis.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ListView;

public class TEListView extends ListView {

	public TEListView(Context context) {
		super(context);

	}

	public TEListView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public TEListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d("TEListView", "dispatchKeyEvent ACTION : " + event.getAction());
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d("TEListView", "onTouchEvent ACTION : " + ev.getAction());
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.d("TEListView", "onInterceptTouchEvent ACTION : " + ev.getAction());
		return super.onInterceptTouchEvent(ev);
	}

}
