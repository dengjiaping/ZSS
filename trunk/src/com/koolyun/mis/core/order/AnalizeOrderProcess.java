package com.koolyun.mis.core.order;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.koolyun.mis.core.ManagerBase;
import com.koolyun.mis.util.NumberFormater;
import com.koolyun.mis.util.pay.PayBase.PAYTYPE;

public class AnalizeOrderProcess extends ManagerBase {
	// mode 0:正常 1:撤销 2: 退货
	public static boolean getOrderAleradyPayed(int orderid, int mode, String money) {
		String sql = "select price from OrderProcess where orderID=" + orderid + " and orderProcessModeId = " + mode;
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return false;
		}
		String mm = NumberFormater.MoneyFromTwelveNumber(result.getString(0));
		result.close();
		return mm.equals(money);
	}

	public static void solveOrderAleradyPayed(int orderid, int mode, String money) {
		if (mode == 0) {
			ContentValues values = new ContentValues();
			values.put("orderID", orderid);
			values.put("paymentTypeID", PAYTYPE.PAY_BY_MSR.toInt());
			values.put("amount", money);
			msqlitedb.insertWithOnConflict("OrdersToPaymentType", null, values, SQLiteDatabase.CONFLICT_IGNORE);

			ContentValues values2 = new ContentValues();
			values2.put("orderID", orderid);
			values2.put("orderStatusID", OrderData.OrderStatus.ORDER_NORMAL.toInt());
			msqlitedb.updateWithOnConflict("Orders", values2, "orderID=" + orderid, null,
					SQLiteDatabase.CONFLICT_REPLACE);
		}
	}

	public static boolean canReverse(int orderid) {
		String sql = "select * from ReverseAttribute where reverseAttrId=" + orderid;
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return false;
		}
		result.close();
		return true;
	}
}
