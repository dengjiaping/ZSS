package com.koolyun.mis.sqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public class MySharedPreferencesEdit {
	private static SharedPreferences sPreferences;
	private SharedPreferences.Editor editor;
	private static MySharedPreferencesEdit _instancePublic = null;

	@SuppressLint("WorldWriteableFiles")
	private MySharedPreferencesEdit(Context context) {
		sPreferences = context.getSharedPreferences("MySharedPreferencesEdit", Context.MODE_WORLD_WRITEABLE);
		editor = sPreferences.edit();
	}

	/**
	 * @param ct
	 * @return
	 */
	public static MySharedPreferencesEdit getInstancePublic(Context ct) {
		if (_instancePublic == null)
			_instancePublic = new MySharedPreferencesEdit(ct);
		return _instancePublic;
	}

	// /**
	// * 是否已经输入过用户名和密码
	// * @param IsSetPassword
	// */
	// public void setIsSetPassword(boolean IsSetPassword) {
	// editor.putBoolean("IsSetPassword", IsSetPassword).commit();
	// }
	// /**
	// * 是否已经输入过用户名和密码
	// * @return
	// */
	// public boolean getIsSetPassword() {
	// return sPreferences.getBoolean("IsSetPassword", false);
	// }

	/**
	 * 是否绑定过设备信息
	 * 
	 * @param IsBandDeviceInfo
	 */
	// public void setIsBandDeviceInfo(boolean IsBandDeviceInfo) {
	// editor.putBoolean("IsBandDeviceInfo", IsBandDeviceInfo).commit();
	// }
	// /**
	// * 是否绑定过设备信息
	// * @return
	// */
	// public boolean getIsBandDeviceInfo() {
	// // TODO
	// return sPreferences.getBoolean("IsBandDeviceInfo", false);
	// // return sPreferences.getBoolean("IsBandDeviceInfo", true);
	// }

	/**
	 * 是否下载过主密钥
	 * 
	 * @param IsDownloadSecretKey
	 */
	public void setIsDownloadSecretKey(boolean IsDownloadSecretKey) {
		editor.putBoolean("IsDownloadSecretKey", IsDownloadSecretKey).commit();
	}

	/**
	 * 是否下载过主密钥
	 * 
	 * @return
	 */
	public boolean getIsDownloadSecretKey() {
		// TODO
		return sPreferences.getBoolean("IsDownloadSecretKey", false);
		// return sPreferences.getBoolean("IsDownloadSecretKey", true);
	}

	/**
	 * 商家手机号
	 * 
	 * @param OwnerPhoneNumber
	 */
	public void setOwnerPhoneNumber(String OwnerPhoneNumber) {
		editor.putString("OwnerPhoneNumber", OwnerPhoneNumber).commit();
	}

	/**
	 * 商家手机号
	 * 
	 * @return
	 */
	public String getOwnerPhoneNumber() {
		// TODO
		// return sPreferences.getString("OwnerPhoneNumber", "18906193057");
		return sPreferences.getString("OwnerPhoneNumber", null);
	}

	/**
	 * 数据库中的商家编号
	 * 
	 * @param MerchantAccountID
	 */
	// public void setMerchantAccountID(String MerchantAccountID) {
	// editor.putString("MerchantAccountID", MerchantAccountID).commit();
	// }
	// /**
	// * 数据库中的商家编号
	// * @return
	// */
	// public String getMerchantAccountID() {
	// // TODO
	// return sPreferences.getString("MerchantAccountID", null);
	// // return sPreferences.getString("MerchantAccountID", "1001");
	// }

	public void setStoreID(String StoreID) {
		editor.putString("StoreID", StoreID).commit();
	}

	public String getStoreID() {
		// TODO
		// return sPreferences.getString("StoreID", "1");
		return sPreferences.getString("StoreID", null);
	}

	/**
	 * mid
	 * 
	 * @param MerchantNo
	 */
	public void setMerchantNo(String MerchantNo) {
		editor.putString("MerchantNo", MerchantNo).commit();
	}

	/**
	 * mid
	 * 
	 * @return
	 */
	public String getMerchantNo() {
		return sPreferences.getString("MerchantNo", null);
	}

	public void setTerminalNo(String TerminalNo) {
		editor.putString("TerminalNo", TerminalNo).commit();
	}

	public String getTerminalNo() {
		return sPreferences.getString("TerminalNo", null);
	}

	public void setCpuId(String CpuId) {
		editor.putString("CpuId", CpuId).commit();
	}

	public String getCpuId() {
		return sPreferences.getString("CpuId", null);
	}

	public void setAppStartTime(String AppStartTime) {
		editor.putString("AppStartTime", AppStartTime).commit();
	}

	public String getAppStartTime() {
		return sPreferences.getString("AppStartTime", null);
	}

	public void setStartHour(int StartHour) {
		editor.putInt("StartHour", StartHour).commit();
	}

	public int getStartHour() {
		return sPreferences.getInt("StartHour", 0);
	}

	public void setStartMinute(int StartMinute) {
		editor.putInt("StartMinute", StartMinute).commit();
	}

	public int getStartMinute() {
		return sPreferences.getInt("StartMinute", 0);
	}

	public void setEndHour(int EndHour) {
		editor.putInt("EndHour", EndHour).commit();
	}

	public int getEndHour() {
		return sPreferences.getInt("EndHour", 23);
	}

	public void setEndMinute(int EndMinute) {
		editor.putInt("EndMinute", EndMinute).commit();
	}

	public int getEndMinute() {
		return sPreferences.getInt("EndMinute", 59);
	}

	public void setStartToEndDays(int StartToEndDays) {
		editor.putInt("StartToEndDays", StartToEndDays).commit();
	}

	public int getStartToEndDays() {
		return sPreferences.getInt("StartToEndDays", 0);
	}

	public void setDBBackupTime(String DBBackupTime) {
		editor.putString("DBBackupTime", DBBackupTime).commit();
	}

	public String getDBBackupTime() {
		return sPreferences.getString("DBBackupTime", null);
	}

	/**
	 * 上次下载apk成功时间
	 * 
	 * @param UpdateApkTime
	 */
	public void setUpdateApkTime(long UpdateApkTime) {
		editor.putLong("UpdateApkTime", UpdateApkTime).commit();
	}

	public long getUpdateApkTime() {
		return sPreferences.getLong("UpdateApkTime", 0);
	}

	/**
	 * 上次下载
	 * 
	 * @param LastUpdateVersion
	 */
	public void setLastUpdateVersion(int LastUpdateVersion) {
		editor.putInt("LastUpdateVersion", LastUpdateVersion).commit();
	}

	public int getLastUpdateVersion() {
		return sPreferences.getInt("LastUpdateVersion", 0);
	}

	/**
	 * 第一次运行程序时安装桌面快捷方式
	 * 
	 * @param IsShortCutInstall
	 */
	public void setIsShortCutInstall(boolean IsShortCutInstall) {
		editor.putBoolean("IsShortCutInstall", IsShortCutInstall).commit();
	}

	public boolean getIsShortCutInstall() {
		return sPreferences.getBoolean("IsShortCutInstall", false);
	}

	/**
	 * just for ces use!!!!!!!!!
	 * 
	 * @param CESEnProduct
	 */
	public void setCESEnProduct(boolean CESEnProduct) {
		// TODO just for ces use!!!!!!!!!
		editor.putBoolean("CESEnProduct", CESEnProduct).commit();
	}

	public boolean getCESEnProduct() {
		// TODO just for ces use!!!!!!!!!
		return sPreferences.getBoolean("CESEnProduct", false);
	}

	public void setTraceNO(int TraceNO) {
		editor.putInt("TraceNO", TraceNO).commit();
	}

	public int getTraceNO() {
		return sPreferences.getInt("TraceNO", 1);
	}

	public void setActiveEquiment(boolean result) {
		editor.putBoolean("activeEq", result).commit();
	}

	public boolean getActivieEquiment() {
		return sPreferences.getBoolean("activeEq", false);
	}
}
