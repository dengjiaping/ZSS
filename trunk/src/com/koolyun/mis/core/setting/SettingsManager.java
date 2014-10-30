package com.koolyun.mis.core.setting;

import android.database.Cursor;

import com.koolyun.mis.core.ManagerBase;

public class SettingsManager extends ManagerBase {

	public static Settings querySettings() {

		Cursor result = msqlitedb.rawQuery("SELECT * FROM `Settings`", null);
		boolean retval = result.moveToFirst();
		Settings settings = null;
		if (retval) {
			settings = new Settings(result.getString(0), result.getInt(1), result.getString(2), result.getInt(3),
					result.getString(4), result.getString(5));
		}
		result.close();
		return settings;
	}

	// public static String queryMerchantCode() {
	//
	// Cursor result = msqlitedb.rawQuery("SELECT merchantCode FROM `Settings`",
	// null);
	// String merchantNo = "";
	// boolean retval = result.moveToFirst();
	// if(retval == false)
	// return null;
	// merchantNo = result.getString(0);
	// result.close();
	// return merchantNo;
	// }
	//
	// public static String queryTerminalCode() {
	//
	// Cursor result = msqlitedb.rawQuery("SELECT terminalCode FROM `Settings`",
	// null);
	// String terminalNo = "";
	// boolean retval = result.moveToFirst();
	// if(retval == false)
	// return null;
	// terminalNo = result.getString(0);
	// result.close();
	// return terminalNo;
	// }

	public static void updateTraceNo(String traceNo) {
		msqlitedb.execSQL("UPDATE `Settings` SET traceNo='" + traceNo + "'");
	}

	public static void updateSerialNumber(String serialNumber) {
		msqlitedb.execSQL("UPDATE `Settings` SET serialNumber='" + serialNumber + "'");
	}
}
