package com.koolyun.mis.util;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ViewFlipper;

public class DealOnTouch implements OnTouchListener {

	private int screenWidth = 460;
	private int screenHeight = 80;
	private int lastX, lastY;
	private int mLastX;// ,mLastY
	private int orignalPostionL;
	private int orignalPostionR;
	private int orignalPostionT;
	private int orignalPostionB;
	float desity;

	ViewFlipper mSaleListViewFlipper = null;

	int flag = 0;

	public DealOnTouch(ViewFlipper mSaleListViewFlipper, int flag) {
		super();
		this.mSaleListViewFlipper = mSaleListViewFlipper;
		this.flag = flag;

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Toast.makeText
			// (getActivity(), "Down...", Toast.LENGTH_SHORT).show();
			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();
			mLastX = lastX;
			orignalPostionL = v.getLeft();
			orignalPostionT = v.getTop();
			orignalPostionR = v.getRight();
			orignalPostionB = v.getBottom();
			// System.out.println("lastX:"+lastX+",lastY:"+lastY);
			break;
		case MotionEvent.ACTION_MOVE:
			int dx = (int) event.getRawX() - lastX;
			int dy = (int) event.getRawY() - lastY;

			int left = v.getLeft() + dx;
			int top = v.getTop() + dy;
			int right = v.getRight() + dx;
			int bottom = v.getBottom() + dy;

			// 设置挂单时向右移动
			if (flag == 1) {
				if (event.getRawX() - mLastX > 5)
					break;
			}

			// 设置不能出界
			if (left < 0) {
				left = 0;
				right = left + v.getWidth();
			}

			if (right > screenWidth) {
				right = screenWidth;
				left = right - v.getWidth();
			}

			if (top < 0) {
				top = 0;
				bottom = top + v.getHeight();
			}

			if (bottom > screenHeight) {
				bottom = screenHeight;
				top = bottom - v.getHeight();
			}
			v.layout(left, top, right, bottom);

			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();

			break;
		case MotionEvent.ACTION_UP:
			Log.i("lastX", "========" + lastX + "lastY======" + lastY);
			Log.i("orignalPostionL", "========" + orignalPostionL + ",orignalPostionT ======" + orignalPostionT
					+ ",orignalPostionR ======" + orignalPostionR + ",orignalPostionB ======" + orignalPostionB);

			if (flag == 0) {
				if ((lastX - mLastX) > 100) {

					mSaleListViewFlipper.setDisplayedChild(1);

				} else {
					v.layout(orignalPostionL, orignalPostionT, orignalPostionR, orignalPostionB);
					v.invalidate();
				}
			} else {
				if ((lastX - mLastX) < -100) {

					mSaleListViewFlipper.setDisplayedChild(0);

				} else {
					v.layout(orignalPostionL, orignalPostionT, orignalPostionR, orignalPostionB);
					v.invalidate();
				}
			}
			break;
		}
		return false;
	}

}
