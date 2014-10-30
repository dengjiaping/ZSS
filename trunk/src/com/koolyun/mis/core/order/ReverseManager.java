package com.koolyun.mis.core.order;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.koolyun.mis.core.ManagerBase;

public class ReverseManager extends ManagerBase {
	// 冲正原因码
	public static final String POS_REVESAL_NO_RESPONCE = "98";
	public static final String POS_REVESAL_MACHINE_ERROR = "96";
	public static final String POS_REVESAL_MAC_ERROR = "A0";
	public static final String POS_REVESAL_OTHER = "06";

	public static String defaultReason = POS_REVESAL_NO_RESPONCE;

	public static long addToReverseTable(int orderProcessId) {
		ContentValues values = new ContentValues();
		values.put("orderProcessId", String.valueOf(orderProcessId));
		values.put("reason", POS_REVESAL_NO_RESPONCE);

		long insertId = msqlitedb.insertWithOnConflict("ReverseAttribute", null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		return insertId;
	}

	public static long addToReverseTable(int orderProcessId, String reasion) {
		ContentValues values = new ContentValues();
		values.put("orderProcessId", String.valueOf(orderProcessId));
		values.put("reason", reasion);

		long insertId = msqlitedb.insertWithOnConflict("ReverseAttribute", null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		return insertId;
	}

	public static long addToReverseTable(ReverseAttribute mReverseAttribute) {
		int opid = mReverseAttribute.getOrderProcessId();
		String reason = mReverseAttribute.getReason();

		return addToReverseTable(opid, reason);
	}

	public static void removeReverseById(int orderProcessId) {
		msqlitedb.execSQL("DELETE FROM `ReverseAttribute` where orderProcessId='" + orderProcessId + "'");
	}

	public static void removeLastReverse() {
		msqlitedb.execSQL("DELETE FROM `ReverseAttribute` where reverseAttrId="
				+ "(SELECT reverseAttrId  from `ReverseAttribute` order by reverseAttrId desc limit 1)");
	}

	public static ReverseAttribute getReverseAttributeById(int processId) {
		String sql = "SELECT * FROM `ReverseAttribute` where orderProcessId = " + String.valueOf(processId);
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return null;
		}
		String reason = result.getString(2);
		ReverseAttribute ra = new ReverseAttribute(processId, reason);
		result.close();
		return ra;
	}

	public static ReverseAttribute getLastReverse() {
		String sql = "SELECT * FROM `ReverseAttribute` where reverseAttrId=(SELECT reverseAttrId "
				+ "from `ReverseAttribute` order by reverseAttrId desc limit 1) ";
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return null;
		}
		int opid = result.getInt(1);
		String reason = result.getString(2);
		ReverseAttribute ra = new ReverseAttribute(opid, reason);
		result.close();
		return ra;
	}

}
