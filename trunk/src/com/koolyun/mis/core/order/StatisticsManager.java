package com.koolyun.mis.core.order;

import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;
import android.util.Log;

import com.koolyun.mis.core.ManagerBase;
import com.koolyun.mis.core.order.OrderData.OrderStatus;
import com.koolyun.mis.core.product.SaleProductInfo;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.NumberFormater;
import com.koolyun.mis.util.pay.PayBase;

public class StatisticsManager extends ManagerBase {
	static long starttime;
	static long endtime;

	static private String getTotalAmount() {
		return getAllAmount(null);
	}

	static private String getCashAmount() {
		return getAllAmount("`paymentTypeID` = " + PayBase.PAYTYPE.PAY_BY_CASH.toInt());
	}

	static private String getCardAmount() {
		return getAllAmount("`paymentTypeID` = " + PayBase.PAYTYPE.PAY_BY_MSR.toInt());
	}

	static private String getOtherAomunt() {
		return getAllAmount("`paymentTypeID` != " + PayBase.PAYTYPE.PAY_BY_MSR.toInt() + " AND `paymentTypeID` != "
				+ PayBase.PAYTYPE.PAY_BY_CASH.toInt());
	}

	static private int getDealCount() {
		String sql = "SELECT COUNT(*) FROM `Orders` WHERE OrderStatusID=" + OrderStatus.ORDER_NORMAL.toInt()
				+ " AND `modifyTime` BETWEEN " + starttime + " AND " + endtime;
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return 0;
		}
		int count = result.getInt(0);
		result.close();
		return count;
	}

	static public StatisticsItem getStatisticsItem(String starttime1, String endtime1) {
		starttime = getStartTime(starttime1);
		endtime = getEndTime(endtime1);
		StatisticsItem mStatisticsItem = new StatisticsItem(getTotalAmount(), getCashAmount(), getCardAmount(),
				getOtherAomunt(), getDealCount());
		return mStatisticsItem;
	}

	public static List<SaleProductInfo> getSaleProductInfo(String starttime1, String endtime1) {
		starttime = getStartTime(starttime1);
		endtime = getEndTime(endtime1);
		String sql = "select oc.productID ,p.name,sum(oc.count) sum1 from OrderContent oc," + "Product p ,Orders o "
				+ "where oc.productID!=0 and oc.productID=p.productID "
				+ "and oc.orderID=o.orderID and o.OrderStatusID = " + OrderStatus.ORDER_NORMAL.toInt()
				+ " and o.modifyTime BETWEEN " + starttime + " AND " + endtime
				+ " group by p.productID order by sum1 desc";

		Cursor result = msqlitedb.rawQuery(sql, null);
		List<SaleProductInfo> saleInfoList = new LinkedList<SaleProductInfo>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productID = result.getInt(0);
			String name = result.getString(1);
			int count = result.getInt(2);

			SaleProductInfo pi = new SaleProductInfo(productID, name, count, null);
			saleInfoList.add(pi);
		}
		result.close();

		return saleInfoList;

	}

	static private long getStartTime(String startdate) {
		String tmp = startdate + ":00";
		return Common.getTimeMillis(tmp, "yyyy-MM-dd HH:mm:ss");
	}

	static private long getEndTime(String enddate) {
		String tmp = enddate + ":59";
		return Common.getTimeMillis(tmp, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getAllAmount(String whereCause) {
		String Addwhere = "";
		if (whereCause != null)
			Addwhere = " AND " + whereCause;
		String sql = "SELECT SUM(amount) FROM `OrdersToPaymentType` where `orderID` IN  (SELECT `orderID` FROM `Orders` where OrderStatusID="
				+ OrderStatus.ORDER_NORMAL.toInt()
				+ " AND `modifyTime` BETWEEN "
				+ starttime
				+ " AND "
				+ endtime
				+ ") " + Addwhere;
		Log.d("sqlite-->", sql);
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return "0.00";
		}
		String ret = result.getString(0);
		if (ret == null)
			ret = "0.00";
		result.close();

		return NumberFormater.currencyFormat(ret);
	}
}
