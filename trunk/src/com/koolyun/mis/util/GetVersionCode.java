package com.koolyun.mis.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class GetVersionCode {
	public static int getAppVersionCode(Context context) {
		int versionName = 0;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	public static String getAPPVersionName(Context context) {
		String versionName = "1.0";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
}
