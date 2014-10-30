package com.koolyun.mis.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

public class AndroidSecurity {

	void disableDebugFlag(Context mcon) {
		if ((mcon.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
			Log.d("===>", "程序被修改为可调试状态。。");
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	public int getSignature(Context mcon, String packageName) {
		PackageManager pm = mcon.getPackageManager();
		PackageInfo pi = null;
		int sig = 0;
		try {
			pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			Signature[] s = pi.signatures;
			sig = s[0].hashCode();
		} catch (Exception e1) {
			sig = 0;
			e1.printStackTrace();
		}
		return sig;
	}

	public void checkSignature(Context mcon, String packageName) {
		int sig = getSignature(mcon, packageName);
		if (sig != 1231231231) {
			Log.d("==>", "签名不一致");
			android.os.Process.killProcess(android.os.Process.myPid());
		} else {
			Log.d("==>", "签名一致");
		}
	}

	public boolean checkCRC(Context mcon) {
		boolean beModified = false;
		// long crc = Long.parseLong(getString(R.string.crc));
		// ZipFile zf;
		// try{
		// zf = new ZipFile(mcon.getApplicationContext().getPackageCodePath());
		// ZipEntry ze = zf.getEntry("class.dex");
		// Log.d("===>",String.valueOf(ze.getCrc()));
		// if(ze.getCrc() == crc){
		// beModified = true;
		// }
		// }
		// catch(IOException e)
		// {
		// e.printStackTrace();
		// beModified = false;
		// }
		return beModified;
	}
}
