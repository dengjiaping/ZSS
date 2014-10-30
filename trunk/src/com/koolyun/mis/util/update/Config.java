package com.koolyun.mis.util.update;

import android.content.Context;

import com.koolyun.mis.R;

public class Config {
	public static final String UPDATE_SAVENAME = "UpdateApkPAR10.apk";

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verName;
	}

	public static String getAppName(Context context) {
		String verName = context.getResources().getText(R.string.app_name).toString();
		return verName;
	}
}
