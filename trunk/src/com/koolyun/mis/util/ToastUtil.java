package com.koolyun.mis.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * <p>
 * Title: ToastUtil.java
 * </p>
 * <p>
 * Description: Toast helper
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: All In Pay
 * </p>
 * 
 * @author Teddy
 * @date 2013-10-30
 * @version
 */
public class ToastUtil {

	private static Toast mToast;
	private static Handler mHandler;
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	private static void showToast(Context mContext, String text, int duration) {
		mHandler = new Handler(mContext.getMainLooper());
		mHandler.removeCallbacks(r);

		if (mToast != null) {
			mToast.setText(text);
		} else {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
		}
		mHandler.postDelayed(r, duration);

		mToast.show();
	}

	public static void showToast(Context mContext, int resId, int durationCancel) {
		showToast(mContext, mContext.getResources().getString(resId), durationCancel);
	}

	public static void showToast(Context mContext, int resId) {
		showToast(mContext, mContext.getResources().getString(resId), 2000);
	}

	public static void showToast(Context mContext, int resId, boolean isNew) {
		if (isNew) {
			Toast toast = Toast.makeText(mContext, mContext.getResources().getString(resId), 3000);
			toast.show();
		} else {
			showToast(mContext, mContext.getResources().getString(resId), 2000);
		}
	}
}
