package com.koolyun.mis.core.order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.koolyun.mis.R;
import com.koolyun.mis.core.CardPayInfos;
import com.koolyun.mis.core.ManagerBase;
import com.koolyun.mis.core.order.OrderData.OrderStatus;
import com.koolyun.mis.core.product.Product;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.MyLog;

public class OrderManager extends ManagerBase {

	public static List<OrderContent> getOrderContentByOrderId(int id) {

		String sql = "SELECT * FROM `OrderContent` where orderID = " + String.valueOf(id);
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<OrderContent> orderlist = new LinkedList<OrderContent>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			OrderContent order = new OrderContent();
			order.setOrderContentId(result.getInt(0));
			order.setProductId(result.getInt(1));
			order.setOrderId(result.getInt(2));
			order.setCount(result.getInt(3));
			order.setAmount(result.getString(4));
			orderlist.add(order);
		}
		result.close();

		return orderlist;
	}

	public static List<OrderContent> getOrderContentByOrderId1(int id) {

		String sql = "SELECT oc.*, p.productCategoryID " + "FROM `OrderContent` oc, `Product` p " + "where orderID = "
				+ id + " and oc.productID = p.productID";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<OrderContent> orderlist = new LinkedList<OrderContent>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			OrderContent order = new OrderContent();
			order.setOrderContentId(result.getInt(0));
			order.setProductId(result.getInt(1));
			order.setOrderId(result.getInt(2));
			order.setCount(result.getInt(3));
			order.setAmount(result.getString(4));
			order.setProductCategoryID(result.getInt(5));
			orderlist.add(order);
		}
		result.close();

		return orderlist;
	}

	public static List<ProductSubAttribute> getProductSubAttributeByOrderContentId(int id) {

		String sql = "SELECT * FROM `ProductSubAttribute` where productSubAttributeId IN "
				+ "(SELECT productSubAttributeId FROM `OrderContentToAttribute` where orderContentID = "
				+ String.valueOf(id) + ")";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductSubAttribute> plist = new LinkedList<ProductSubAttribute>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productSubAttributeId = result.getInt(0);
			int productAttributeID = result.getInt(1);
			String name = result.getString(2);
			String priceAffect = result.getString(3);
			int enable = result.getInt(4);
			ProductSubAttribute psa = new ProductSubAttribute(productSubAttributeId, productAttributeID, name,
					priceAffect, enable);
			plist.add(psa);
		}
		result.close();

		return plist;
	}

	public static List<Onsale> getOnsaleByOrderContentId(int id) {
		String sql = "SELECT Onsale.*  FROM Onsale,OrderContentToOnsale  where OrderContentToOnsale.OrderContentID="
				+ String.valueOf(id)
				+ " and OrderContentToOnsale.onsaleID=Onsale.onsaleID  order by OrderContentToOnsale.rowid ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Onsale> plist = new ArrayList<Onsale>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int onsaleID = result.getInt(0);
			int onsaleType = result.getInt(1);
			String onsaleName = result.getString(2);
			String value = result.getString(3);
			int enable = result.getInt(4);
			Onsale psa = new Onsale(onsaleID, onsaleType, onsaleName, value, enable);
			plist.add(psa);

		}
		result.close();

		return plist;

	}

	public static OrderContentRemark getOrderContentRemarkById(int id) {
		String sql = "SELECT remark FROM `OrderContentRemark` where orderContentID = " + String.valueOf(id);
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return new OrderContentRemark(-1, "");
		}
		OrderContentRemark remark = new OrderContentRemark(id, result.getString(0));
		result.close();
		return remark;
	}

	public static OrderRemark getOrderRemarkById(int id) {
		String sql = "SELECT * FROM `OrderRemark` where orderID = " + String.valueOf(id);
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return new OrderRemark(-1, "", "");
		}

		String str = result.getString(1);
		String sit = result.getString(2);

		result.close();
		if ((str == null || str.isEmpty()) && (sit == null || sit.isEmpty()))
			return new OrderRemark(-1, "", "");
		OrderRemark remark = new OrderRemark(id, str, sit);

		return remark;
	}

	public static void addOrUpdateOrderRemark(OrderRemark mOrderRemark) {
		if (mOrderRemark == null)
			return;

		ContentValues values = new ContentValues();
		values.put("orderID", mOrderRemark.getOrderId());
		values.put("remark", mOrderRemark.getRemark());
		values.put("sitindex", mOrderRemark.getSitIndex());

		String sql = "SELECT * FROM `OrderRemark` where orderID = " + String.valueOf(mOrderRemark.getOrderId());
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			// 以前没有remark

			msqlitedb.insertWithOnConflict("OrderRemark", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		} else {
			String[] whereArgs = { String.valueOf(mOrderRemark.getOrderId()) };
			msqlitedb.updateWithOnConflict("OrderRemark", values, "orderID=?", whereArgs,
					SQLiteDatabase.CONFLICT_REPLACE);
		}
		result.close();

	}

	public static List<OrderContentData> getOrderContentDataByOrderId(int id) {
		List<OrderContent> orderlist = getOrderContentByOrderId(id);
		List<OrderContentData> orderdatalist = new LinkedList<OrderContentData>();

		for (int i = 0; i < orderlist.size(); i++) {

			OrderContent oc = orderlist.get(i);
			OrderContentData orderdata = new OrderContentData(oc);

			int productid = oc.getProductId();
			Product p;
			if (productid == 0)
				p = ProductManager.getCustomProduct("0.00");
			else
				p = ProductManager.getProductByProductId(productid, false);
			orderdata.setProduct(p);

			String oneprice = BasicArithmetic.div(oc.getAmount(), String.valueOf(oc.getCount()));
			orderdata.setOnePrice(oneprice);

			int id1 = orderdata.getOrderContentId();
			List<ProductSubAttribute> psalist = getProductSubAttributeByOrderContentId(id1);
			orderdata.setProductSubAttrList(psalist);

			List<Onsale> onsalelist = getOnsaleByOrderContentId(id1);
			orderdata.setOnsale(onsalelist);

			OrderContentRemark remark = getOrderContentRemarkById(id1);
			orderdata.setmOrderContentRemark(remark);

			orderdatalist.add(orderdata);

		}

		return orderdatalist;
	}

	public static Orders getOrderById(int id) {
		String sql = "SELECT * FROM `Orders` where orderID = " + String.valueOf(id);
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return null;
		}
		int orderID = result.getInt(0);
		String billNo = result.getString(1);
		int OrderStatusID = result.getInt(2);
		long createTime = result.getLong(3);
		long modifyTime = result.getLong(4);
		String txnId = result.getString(6);
		Orders order = new Orders(orderID, billNo, createTime, modifyTime, OrderStatusID,txnId);
		result.close();
		return order;
	}

	public static List<Onsale> getAllOnSaleList() {
		String sql = "SELECT * FROM `Onsale` WHERE enable=1 ORDER BY onsaleName ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Onsale> otplist = new LinkedList<Onsale>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int onsaleID = result.getInt(0);
			int onsaleType = result.getInt(1);
			String onsaleName = result.getString(2);
			String value = result.getString(3);
			int enable = result.getInt(4);
			Onsale op = new Onsale(onsaleID, onsaleType, onsaleName, value, enable);
			otplist.add(op);
		}
		result.close();

		return otplist;
	}

	public static List<Onsale> getAllOnSaleListForBackup(SQLiteDatabase sqlitedb) {
		String sql = "SELECT * FROM `Onsale` where `enable`=1";
		Cursor result = sqlitedb.rawQuery(sql, null);
		List<Onsale> otplist = new LinkedList<Onsale>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int onsaleID = result.getInt(0);
			int onsaleType = result.getInt(1);
			String onsaleName = result.getString(2);
			String value = result.getString(3);
			int enable = result.getInt(4);
			Onsale op = new Onsale(onsaleID, onsaleType, onsaleName, value, enable);
			otplist.add(op);
		}
		result.close();

		return otplist;
	}

	public static List<OrdersToPaymentType> getOrdersPaymentTypeById(int id) {
		String sql = "SELECT * FROM `OrdersToPaymentType` where orderID = " + String.valueOf(id);
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<OrdersToPaymentType> otplist = new LinkedList<OrdersToPaymentType>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int orderID = result.getInt(0);
			int paymentTypeID = result.getInt(1);
			String amount = result.getString(2);

			OrdersToPaymentType op = new OrdersToPaymentType(orderID, paymentTypeID, amount);
			otplist.add(op);
		}
		result.close();

		return otplist;
	}

	public static String getOrderContentAmountByID(int orderContentId) {
		return null;
	}

	public static String getOrderAmountByID(int orderContentId) {
		return null;
	}

	public static CardPayInfos getCardPayInfosById(int orderid) {

		OrderProcess mOrderProcess = getOrderProcessById(orderid);
		if (mOrderProcess != null) {
			CardPayInfos mCardPayInfos = new CardPayInfos();
			mCardPayInfos.setOrderProcess(mOrderProcess);
			// TODO: add bank info

			return mCardPayInfos;
		} else {
			return null;
		}
	}

	public static OrderProcess getOrderProcessByProcessId(int orderprocessid) {
		String sql = "SELECT * FROM `OrderProcess` where orderProcessId = " + orderprocessid;
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (result.moveToFirst()) {
			int orderProcessId = result.getInt(0);
			int OrderProcessModeId = result.getInt(1);
			int orderID = result.getInt(2);
			int addressID = result.getInt(3);
			String createTime = result.getString(4);
			String traceNo = result.getString(5);
			String referenceNo = result.getString(6);
			String authorizationNo = result.getString(7);
			String clearingDate = result.getString(8);
			String ackNo = result.getString(9);
			String price = result.getString(10);
			String issue = result.getString(11);
			String acq = result.getString(12);
			String batchid = result.getString(13);

			OrderProcess mOrderProcess = new OrderProcess(orderProcessId, OrderProcessModeId, orderID, addressID,
					createTime, traceNo, referenceNo, authorizationNo, clearingDate, ackNo, price, issue, acq, batchid);
			result.close();
			return mOrderProcess;
		} else {
			result.close();
			return null;
		}
	}

	public static List<OrderProcess> getOrderProcessById1(int orderid) {
		String sql = "SELECT * FROM `OrderProcess` where orderID =" + orderid;
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<OrderProcess> orderprocess = new ArrayList<OrderProcess>();
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int orderProcessId = result.getInt(0);
			int OrderProcessModeId = result.getInt(1);
			int orderID = result.getInt(2);
			int addressID = result.getInt(3);
			String createTime = result.getString(4);
			String traceNo = result.getString(5);
			String referenceNo = result.getString(6);
			String authorizationNo = result.getString(7);
			String clearingDate = result.getString(8);
			String ackNo = result.getString(9);
			String price = result.getString(10);
			String issue = result.getString(11);
			String acq = result.getString(12);
			String batchid = result.getString(13);
			OrderProcess mOrderProcess = new OrderProcess(orderProcessId, OrderProcessModeId, orderID, addressID,
					createTime, traceNo, referenceNo, authorizationNo, clearingDate, ackNo, price, issue, acq, batchid);
			orderprocess.add(mOrderProcess);
		}
		result.close();
		return orderprocess;
	}

	public static OrderProcess getOrderProcessById(int orderid) {
		// TODO: need to be fixed , here may be not only one
		String sql = "SELECT * FROM `OrderProcess` where orderID = " + orderid + " ORDER BY createTime DESC limit 1";
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (result.moveToFirst()) {
			int orderProcessId = result.getInt(0);
			int OrderProcessModeId = result.getInt(1);
			int orderID = result.getInt(2);
			int addressID = result.getInt(3);
			String createTime = result.getString(4);
			String traceNo = result.getString(5);
			String referenceNo = result.getString(6);
			String authorizationNo = result.getString(7);
			String clearingDate = result.getString(8);
			String ackNo = result.getString(9);
			String price = result.getString(10);
			String issue = result.getString(11);
			String acq = result.getString(12);
			String batchid = result.getString(13);

			OrderProcess mOrderProcess = new OrderProcess(orderProcessId, OrderProcessModeId, orderID, addressID,
					createTime, traceNo, referenceNo, authorizationNo, clearingDate, ackNo, price, issue, acq, batchid);
			result.close();
			return mOrderProcess;
		} else {
			result.close();
			return null;
		}
	}

	public static OrderData getOrderDataById(int id) {
		OrderData od = new OrderData();
		od.setCurrentOrder(getOrderById(id));
		od.setOrderContentList(getOrderContentDataByOrderId(id));
		od.setPayList(getOrdersPaymentTypeById(id));
		od.setCardPayInfos(getCardPayInfosById(id));
		od.setOrderRemark(getOrderRemarkById(id));
		od.setmOrderToAccount(getAccountNameByOrderId(id));
		od.setOnsale(getOnsaleListByOrderID(id));
		return od;
	}

	public static List<Onsale> getOnsaleListByOrderID(int orderID) {
		String sql = "SELECT os.* FROM `Onsale` os,`OrderToOnsale` otos WHERE "
				+ "os.onsaleID=otos.onsaleID AND otos.orderID=" + orderID;
		Cursor reCursor = msqlitedb.rawQuery(sql, null);
		List<Onsale> onsaleList = new LinkedList<Onsale>();

		for (reCursor.moveToFirst(); !reCursor.isAfterLast(); reCursor.moveToNext()) {
			Onsale os = new Onsale();
			os.setOnsaleID(reCursor.getInt(0));
			os.setOnsaleType(reCursor.getInt(1));
			os.setOnsaleName(reCursor.getString(2));
			os.setValue(reCursor.getString(3));
			onsaleList.add(os);
		}
		reCursor.close();
		return onsaleList;
	}

	public static List<Orders> getAllOrders1() {

		String sql = "SELECT * FROM `Orders` where OrderStatusID = " + OrderStatus.ORDER_NORMAL.toInt()
				+ " OR OrderStatusID = " + OrderStatus.ORDER_UNDO.toInt() + " ORDER BY modifyTime DESC";

		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Orders> odlist = new ArrayList<Orders>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int orderID = result.getInt(0);
			String billNo = result.getString(1);
			int OrderStatusID = result.getInt(2);
			long createTime = result.getLong(3);
			long modifyTime = result.getLong(4);
			String txnId = result.getString(6);
			Orders order = new Orders(orderID, billNo, createTime, modifyTime, OrderStatusID,txnId);
			odlist.add(order);
		}

		result.close();

		return odlist;
	}

	public static String getAmountById(int id) {
		String sql = "SELECT amount FROM `OrdersToPaymentType` where orderID = " + String.valueOf(id);
		Cursor result = msqlitedb.rawQuery(sql, null);
		String amount = "0.00";

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			String temp = result.getString(0);
			amount = BasicArithmetic.add(amount, temp);
		}

		result.close();
		return amount;
	}

	public static String getToPayAmountById(int id) {
		String sql = "SELECT price FROM `OrderContent` where orderID = " + String.valueOf(id);
		Cursor result = msqlitedb.rawQuery(sql, null);
		String amount = "0.00";

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			String temp = result.getString(0);
			amount = BasicArithmetic.add(amount, temp);
		}

		result.close();
		return amount;
	}

	public static String getDescriptionById(int id) {
		String sql = "SELECT name FROM `Product` WHERE productID IN "
				+ "(SELECT productID FROM `OrderContent` where orderID = " + String.valueOf(id) + ")";
		Cursor result = msqlitedb.rawQuery(sql, null);
		String description = "";

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			String temp = result.getString(0);
			if (!result.isLast())
				temp += "/";
			description += temp.replace("\\", " ");
		}

		result.close();
		return description;
	}

	public static List<LiteOrderInfo> getAllOrders() {

		List<Orders> orderlist = getAllOrders1();
		List<LiteOrderInfo> loilist = new ArrayList<LiteOrderInfo>();

		for (int i = 0; i < orderlist.size(); i++) {
			Orders obj = orderlist.get(i);
			int objid = obj.getOrderID();
			String amount = getAmountById(objid);
			String description = getDescriptionById(objid);
			String remark = getOrderRemarkString(objid);
			LiteOrderInfo liteorderinfo = new LiteOrderInfo(obj, amount, description, remark);
			loilist.add(liteorderinfo);
		}

		return loilist;
	}

	public static List<Orders> getRecentOrders1() {

		String sql = "SELECT * FROM `Orders` where OrderStatusID != " + OrderStatus.ORDER_HANGUP.toInt()
				+ " and OrderStatusID != " + OrderStatus.ORDER_PERREFUND.toInt() + " and OrderStatusID != "
				+ OrderStatus.ORDER_PRESTORE.toInt() + " ORDER BY modifyTime DESC limit 30";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Orders> odlist = new ArrayList<Orders>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int orderID = result.getInt(0);
			String billNo = result.getString(1);
			int OrderStatusID = result.getInt(2);
			long createTime = result.getLong(3);
			long modifyTime = result.getLong(4);
			String txnId = result.getString(6);
			Orders order = new Orders(orderID, billNo, createTime, modifyTime, OrderStatusID,txnId);
			odlist.add(order);
		}

		result.close();

		return odlist;
	}

	public static List<Orders> getOrdersByStatus1(int status) {
		String sql = "SELECT * FROM `Orders` where orderStatusID = " + String.valueOf(status)
				+ " ORDER BY modifyTime DESC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Orders> odlist = new ArrayList<Orders>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int orderID = result.getInt(0);
			String billNo = result.getString(1);
			int OrderStatusID = result.getInt(2);
			long createTime = result.getLong(3);
			long modifyTime = result.getLong(4);
			String txnId = result.getString(6);
			Orders order = new Orders(orderID, billNo, createTime, modifyTime, OrderStatusID,txnId);
			odlist.add(order);
		}

		result.close();

		return odlist;
	}

	List<List<LiteOrderInfo>> mListGroup = new ArrayList<List<LiteOrderInfo>>();

	private static String getOrderRemarkString(int objid) {
		OrderRemark orm = getOrderRemarkById(objid);
		String remark = orm.getRemark();
		String sti = orm.getSitIndex();
		StringBuilder sb = new StringBuilder();
		if (sti != null && !sti.isEmpty())
			sb.append(Common.getString(R.string.sit_index)).append(":").append(sti).append("   ");
		sb.append(remark);
		return sb.toString();
	}

	public static List<List<LiteOrderInfo>> getOrderListDailyByStatus() {
		List<LiteOrderInfo> list = getAllOrdersInfoList();
		String date = "";
		String lastdate = "";
		List<List<LiteOrderInfo>> mListGroup = new ArrayList<List<LiteOrderInfo>>();
		List<LiteOrderInfo> tmpList = null;
		for (int i = 0; i < list.size(); i++) {
			date = Common.getDateTimeString(list.get(i).getOrder().getModifyTime(), "yyMMdd");
			if ((date.equals(lastdate) && tmpList != null)) {
				tmpList.add(list.get(i));
			} else {
				if (tmpList != null)
					mListGroup.add(tmpList);
				tmpList = new ArrayList<LiteOrderInfo>();
				tmpList.add(list.get(i));
			}
			if (i == list.size() - 1)
				mListGroup.add(tmpList);
			lastdate = date;
		}
		return mListGroup;
	}
	//根据天数，得到所有的挂单数据
	public static List<List<LiteOrderInfo>> getOrderListDailHandUpyByStatus() {
		List<LiteOrderInfo> list =  OrderManager.getOrdersByStatus(OrderStatus.ORDER_HANGUP.toInt());
		
		String date = "";
		String lastdate = "";
		List<List<LiteOrderInfo>> mListGroup = new ArrayList<List<LiteOrderInfo>>();
		List<LiteOrderInfo> tmpList = null;
		for (int i = 0; i < list.size(); i++) {
			date = Common.getDateTimeString(list.get(i).getOrder().getModifyTime(), "yyMMdd");
			if ((date.equals(lastdate) && tmpList != null)) {
				tmpList.add(list.get(i));
			} else {
				if (tmpList != null)
					mListGroup.add(tmpList);
				tmpList = new ArrayList<LiteOrderInfo>();
				tmpList.add(list.get(i));
			}
			if (i == list.size() - 1)
				mListGroup.add(tmpList);
			lastdate = date;
		}
		return mListGroup;
	}
	

	public static List<LiteOrderInfo> getAllOrdersInfoList() {
		Log.d("TIME", Common.getDateTimeString(System.currentTimeMillis(), "ss:SSS"));
		String sql = "SELECT o.* ,otp.amount,ork.* FROM `Orders` o "
				+ "join `OrdersToPaymentType` otp on (o.orderID = otp.orderID) "
				+ "left Outer join `OrderRemark` ork on (o.orderID = ork.orderID ) "
				+ "where  OrderStatusID <= 1  ORDER BY modifyTime DESC";
		Log.d("TIME", sql);
		Cursor result = msqlitedb.rawQuery(sql, null);

		List<LiteOrderInfo> loilist = new LinkedList<LiteOrderInfo>();
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int orderID = result.getInt(0);
			String billNo = result.getString(1);
			int OrderStatusID = result.getInt(2);
			long createTime = result.getLong(3);
			long modifyTime = result.getLong(4);
			// order syncflag 5
			String txnId = result.getString(6);
			Orders order = new Orders(orderID, billNo, createTime, modifyTime, OrderStatusID,txnId);
			String amount = result.getString(7);

			String remark = result.getString(9);
			String sti = result.getString(10);
			StringBuilder sb = new StringBuilder();
			if (sti != null && !sti.isEmpty() && !sti.equals("null"))
				sb.append(Common.getString(R.string.sit_index)).append(":").append(sti).append("   ");
			if (remark != null && !remark.isEmpty() && !remark.equals("null"))
				sb.append(remark);

			LiteOrderInfo liteorderinfo = new LiteOrderInfo(order, amount, "", sb.toString());
			loilist.add(liteorderinfo);
		}
		// Log.d("TIME",Common.getDateTimeString(System.currentTimeMillis(),"ss:SSS"));
		result.close();
		return loilist;
	}

	public static List<LiteOrderInfo> getOrdersByStatus(int status) {
		List<Orders> orderlist;
		// Log.d("TIME",Common.getDateTimeString(System.currentTimeMillis(),"ss:SSS"));
		if (status == -1) // -1 默认搜索所有
		{
			orderlist = getAllOrders1();
		} else {
			orderlist = getOrdersByStatus1(status);
		}
		// Log.d("TIME",Common.getDateTimeString(System.currentTimeMillis(),"ss:SSS"));
		List<LiteOrderInfo> loilist = new LinkedList<LiteOrderInfo>();

		for (int i = 0; i < orderlist.size(); i++) {
			Orders obj = orderlist.get(i);
			int objid = obj.getOrderID();
			String amount = null;
			if (status == OrderStatus.ORDER_HANGUP.toInt()) {
				amount = getToPayAmountById(objid);
			} else {
				amount = getAmountById(objid); // 1s
			}

			String description = getDescriptionById(objid);// 2.5s

			String remark = getOrderRemarkString(objid);// 1.2s

			LiteOrderInfo liteorderinfo = new LiteOrderInfo(obj, amount, description, remark);
			loilist.add(liteorderinfo);
		}
		// Log.d("TIME",Common.getDateTimeString(System.currentTimeMillis(),"ss:SSS"));
		return loilist;
	}

	public static List<LiteOrderInfo> getRecentOrders() {

		List<Orders> orderlist = getRecentOrders1();
		List<LiteOrderInfo> loilist = new ArrayList<LiteOrderInfo>();

		for (int i = 0; i < orderlist.size(); i++) {
			Orders obj = orderlist.get(i);
			int objid = obj.getOrderID();
			String amount = getAmountById(objid);
			String description = getDescriptionById(objid);
			String remark = getOrderRemarkString(objid);
			LiteOrderInfo liteorderinfo = new LiteOrderInfo(obj, amount, description, remark);
			loilist.add(liteorderinfo);
		}

		return loilist;
	}

	public static boolean updateOrderById(int orderId,String txnId){
		ContentValues values = new ContentValues();
		values.put("txnId", txnId);

		String[] whereArgs = { String.valueOf(orderId) };
		int result = msqlitedb.updateWithOnConflict("Orders", values, "orderID=?", whereArgs,
				SQLiteDatabase.CONFLICT_REPLACE);
		MyLog.i("--------result--------"+result);
		return false;
	}
	
	public static long saveOrder(Orders order) {

		ContentValues values = new ContentValues();
		int orderstatusid = order.getOrderStatusID();
		if (order.getOrderID() >= 0)
			values.put("orderID", String.valueOf(order.getOrderID()));
		values.put("billNo", String.valueOf(order.getBillNo()));
		values.put("orderStatusID", String.valueOf(orderstatusid));
		values.put("createTime", order.getCreateTime());
		values.put("modifyTime", order.getModifyTime());
		order.setSyncFlag(orderstatusid);
		values.put("syncFlag", order.getSyncFlag());
		values.put("txnId", "");

		long insertId = msqlitedb.insertWithOnConflict("Orders", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		return insertId;
	}

	public static void saveOrderToPayment(List<OrdersToPaymentType> mOtptList) {
		for (int i = 0; i < mOtptList.size(); i++) {
			ContentValues values = new ContentValues();
			values.put("orderID", mOtptList.get(i).getOrderID());
			values.put("paymentTypeID", mOtptList.get(i).getPaymentTypeID());
			values.put("amount", mOtptList.get(i).getAmount());
			msqlitedb.insertWithOnConflict("OrdersToPaymentType", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		}
	}

	public static long saveOrderCustomerInfo(OrderCustomerInfo mMci) {
		ContentValues values = new ContentValues();
		values.put("orderProcessId", String.valueOf(mMci.getOrderProcessId()));
		values.put("bankCardId", String.valueOf(mMci.getBankCardId()));
		values.put("signature", mMci.getSignature());
		values.put("telephoneNo", mMci.getTelephoneNo());
		values.put("emailInfo", mMci.getEmailInfo());

		long insertId = msqlitedb.insertWithOnConflict("OrderCustomerInfo", null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		return insertId;
	}

	public static long saveBankCard(BankCard mBc) {
		ContentValues values = new ContentValues();
		values.put("bankCardId", mBc.getBankCardId());
		values.put("bankCardNo", mBc.getBankCardNo());
		values.put("validThrough", mBc.getTrueThrough());
		values.put("cardOwnerName", mBc.getCardOwnerName());

		long insertId = msqlitedb.insertWithOnConflict("BankCard", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		return insertId;
	}

	public static long saveOrderProcess(OrderProcess mOrderProcess) {
		ContentValues values = new ContentValues();
		int modeid = mOrderProcess.getOrderProcessModeId();
		values.put("OrderProcessModeId", String.valueOf(modeid));

		int orderid = mOrderProcess.getOrderID();
		values.put("orderID", orderid);

		int addid = mOrderProcess.getAddressID();
		values.put("addressID", addid);

		values.put("createTime", mOrderProcess.getCreateTime());
		values.put("traceNo", mOrderProcess.getTraceNo());
		values.put("referenceNo", mOrderProcess.getReferenceNo());
		values.put("authorizationNo", mOrderProcess.getAuthorizationNo());
		values.put("clearingDate", mOrderProcess.getClearingDate());
		values.put("ackNo", mOrderProcess.getAckNo());
		values.put("price", mOrderProcess.getPrice());
		values.put("issuerbankid", mOrderProcess.getIssuerbankId());
		values.put("acqbank", mOrderProcess.getAcqbankId());
		values.put("batchid", mOrderProcess.getBatchId());
		// int id =
		// msqlitedb.updateWithOnConflict("OrderProcess",values,"orderID = -1 AND traceNo = "+mOrderProcess.getTraceNo(),null,SQLiteDatabase.CONFLICT_REPLACE);
		String[] whereArgs = { String.valueOf(mOrderProcess.getTraceNo()) };
		int id = msqlitedb.updateWithOnConflict("OrderProcess", values, "traceNo=?", whereArgs,
				SQLiteDatabase.CONFLICT_REPLACE);
		if (id == 0) {
			return msqlitedb.insertWithOnConflict("OrderProcess", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		} else
			return id;
	}

	public static long saveOrderContent(OrderContent orderContent) {
		ContentValues values = new ContentValues();

		if (orderContent.getOrderContentId() >= 0) {
			values.put("orderContentId", String.valueOf(orderContent.getOrderContentId()));
		}
		int productid = orderContent.getProductId();
		values.put("productID", String.valueOf(productid));

		int orderid = orderContent.getOrderId();
		values.put("orderID", String.valueOf(orderid));

		int countid = orderContent.getCount();
		values.put("count", String.valueOf(countid));

		String amount = orderContent.getAmount();
		values.put("price", amount);

		long insertId = msqlitedb.insertWithOnConflict("OrderContent", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		return insertId;
	}

	public static long saveOrderContentToAttribute(OrderContentToAttribute orderContentToAttr) {
		ContentValues values = new ContentValues();

		int ocid = orderContentToAttr.getOrderContentID();
		values.put("orderContentID", String.valueOf(ocid));

		int psaid = orderContentToAttr.getProductSubAttributeId();
		values.put("productSubAttributeId", String.valueOf(psaid));

		String price = orderContentToAttr.getPrice();
		values.put("price", price);

		long insertId = msqlitedb.insertWithOnConflict("OrderContentToAttribute", null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		return insertId;
	}

	public static long saveOrderContentToOnSale(OrderContentToOnsale onsale) {
		ContentValues values = new ContentValues();

		int gocid = onsale.getOrderContentID();
		values.put("orderContentID", String.valueOf(gocid));

		int osid = onsale.getOnsaleID();
		values.put("onsaleID", String.valueOf(osid));

		String price = onsale.getValue();
		values.put("value", price);

		long insertId = msqlitedb.insertWithOnConflict("OrderContentToOnsale", null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		return insertId;
	}

	public static void deleteOrderInfoById(int orderId) {
		List<OrderContentData> morderContentList = getOrderContentDataByOrderId(orderId);

		msqlitedb.beginTransaction();
		try {
			for (int i = 0; i < morderContentList.size(); i++) {
				int ordercontentid = morderContentList.get(i).getOrderContent().getOrderContentId();
				msqlitedb.delete("OrderContentToAttribute", "orderContentID = " + ordercontentid, null);
				msqlitedb.delete("OrderContentToOnsale", "orderContentID = " + ordercontentid, null);
				msqlitedb.delete("OrderContent", "orderContentID = " + ordercontentid, null);
				msqlitedb.delete("OrderContentRemark", "orderContentID = " + ordercontentid, null);
			}
			msqlitedb.delete("OrderRemark", "orderID = " + orderId, null);
			msqlitedb.delete("OrderToAccount", "orderID = " + orderId, null);
			msqlitedb.delete("Orders", "orderID = " + orderId, null);
			msqlitedb.delete("OrdersToPaymentType", "orderID = " + orderId, null);
			removeOrderToOnsale(orderId);
			msqlitedb.setTransactionSuccessful();
		} finally {
			msqlitedb.endTransaction();
		}
	}

	public static void saveOrUpdateOrderContentRemark(OrderContentRemark mOrderContentRemark) {
		ContentValues values = new ContentValues();
		values.put("orderContentID", mOrderContentRemark.getOrderContentId());
		values.put("remark", mOrderContentRemark.getRemark());
		String[] whereArgs = { String.valueOf(mOrderContentRemark.getOrderContentId()) };
		int id = msqlitedb.updateWithOnConflict("OrderContentRemark", values, "orderContentID=?", whereArgs,
				SQLiteDatabase.CONFLICT_REPLACE);
		if (id == 0) {
			msqlitedb.insertWithOnConflict("OrderContentRemark", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		}
	}

	// 回收Orders
	public static List<Orders> getOrdersForSync(int syncFlag) {
		String sql = "SELECT * FROM `Orders` where syncFlag = " + syncFlag;
		// + String.valueOf(syncflags);
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Orders> orderlist = new LinkedList<Orders>();
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			Orders orders = new Orders();
			orders.setOrderID(result.getInt(0));
			orders.setBillNo(result.getString(1));
			orders.setOrderStatusID(result.getInt(2));
			orders.setCreateTime(result.getLong(3));
			orders.setModifyTime(result.getLong(4));
			orders.setSyncFlag(result.getInt(5));
			orderlist.add(orders);
		}
		result.close();
		return orderlist;
	}

	public static void orderSynced(int id) {
		msqlitedb.execSQL("UPDATE `Orders` SET syncFlag = 2 WHERE orderID = " + id);
	}

	public static List<OrderCustomerInfo> getOrderCustomerInfoByProcessId1(int orderProcessid) {
		String sql = "SELECT *FROM `OrderCustomerInfo` where orderProcessId= " + String.valueOf(orderProcessid);
		Cursor result1 = msqlitedb.rawQuery(sql, null);
		List<OrderCustomerInfo> orderlist = new LinkedList<OrderCustomerInfo>();
		for (result1.moveToFirst(); !result1.isAfterLast(); result1.moveToNext()) {
			OrderCustomerInfo oci = new OrderCustomerInfo();
			oci.setOrderProcessId(result1.getInt(0));
			oci.setBankCardId(result1.getInt(1));
			oci.setSignature(result1.getString(2));
			oci.setTelephoneNo(result1.getString(3));
			oci.setEmailInfo(result1.getString(4));
			orderlist.add(oci);
		}
		result1.close();
		return orderlist;
	}

	public static OrderCustomerInfo getOrderCustomerInfoByProcessId(int orderProcessid) {
		String sql = "SELECT * FROM `OrderCustomerInfo` where orderProcessId= " + orderProcessid;
		// + " ORDER BY createTime DESC limit 1";
		Log.i("orderProcessid", "orderProcessid" + orderProcessid);
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (result.moveToFirst()) {
			int OrderProcessId = (result.getInt(0));
			int BankCardId = (result.getInt(1));
			String EmailInfo = (result.getString(2));
			String Signature = (result.getString(3));
			String TelephoneNo = (result.getString(4));
			OrderCustomerInfo mOrderCustomerInfo = new OrderCustomerInfo(OrderProcessId, BankCardId, EmailInfo,
					Signature, TelephoneNo);
			result.close();
			return mOrderCustomerInfo;
		} else {
			result.close();
			return null;
		}
	}

	public static BankCard getBankCardById(int bankCardid) {
		String sql = "SELECT * FROM `BankCard` where bankCardId = " + String.valueOf(bankCardid);
		Cursor result2 = msqlitedb.rawQuery(sql, null);
		if (result2.moveToFirst()) {
			int BankCardId = result2.getInt(0);
			String BankCardNo = result2.getString(1);
			String validThrough = result2.getString(2);
			String CardOwnerName = result2.getString(3);
			BankCard bankCard = new BankCard(BankCardId, BankCardNo, CardOwnerName, validThrough);
			result2.close();
			return bankCard;
		} else {
			result2.close();
			return null;
		}

	}

	// public static List<BankCard> getBankCardById(int bankCardid)
	// {
	// String sql
	// ="SELECT *FROM `BankCard` where bankCardId="+String.valueOf(bankCardid);
	// Cursor result2 = msqlitedb.rawQuery(sql, null);
	// List<BankCard> orderlist = new LinkedList<BankCard>();
	// for(result2.moveToFirst();!result2.isAfterLast();result2.moveToNext()){
	// BankCard bc = new BankCard();
	// bc.setBankCardId(result2.getInt(0));
	// bc.setBankCardNo(result2.getString(1));
	// bc.setCardOwnerName(result2.getString(2));
	// bc.setTrueThrough(result2.getString(3));
	// orderlist.add(bc);
	// }
	// return orderlist;
	// }
	public static List<OrderContentToAttribute> getOrderContentToAttributesByOrderContentId(int OrderContentId) {
		String sql = "SELECT octa.*, psa.productAttributeID "
				+ "FROM `OrderContentToAttribute` octa, ProductSubAttribute psa " + "WHERE octa.orderContentID = "
				+ String.valueOf(OrderContentId) + " AND octa.productSubAttributeID = psa.productSubAttributeID ";
		Cursor resuCursor = msqlitedb.rawQuery(sql, null);
		List<OrderContentToAttribute> orderlist = new LinkedList<OrderContentToAttribute>();
		for (resuCursor.moveToFirst(); !resuCursor.isAfterLast(); resuCursor.moveToNext()) {
			OrderContentToAttribute octa = new OrderContentToAttribute();
			octa.setOrderContentID(resuCursor.getInt(0));
			octa.setProductSubAttributeId(resuCursor.getInt(1));
			octa.setPrice(resuCursor.getString(2));
			octa.setProdcutAttributeId(resuCursor.getInt(3));
			orderlist.add(octa);
		}

		resuCursor.close();
		return orderlist;
	}

	public static List<OrderContentToOnsale> getOrderContentToOnsalesByOrderContentId(int OrderContentId) {
		String sql = "SELECT * FROM `OrderContentToOnsale` where OrderContentId=" + String.valueOf(OrderContentId);
		Cursor reCursor = msqlitedb.rawQuery(sql, null);
		List<OrderContentToOnsale> orderlist = new LinkedList<OrderContentToOnsale>();

		for (reCursor.moveToFirst(); !reCursor.isAfterLast(); reCursor.moveToNext()) {
			OrderContentToOnsale octo = new OrderContentToOnsale();
			octo.setOrderContentID(reCursor.getInt(0));
			octo.setOnsaleID(reCursor.getInt(1));
			octo.setValue(reCursor.getString(2));
			orderlist.add(octo);
		}

		reCursor.close();
		return orderlist;
	}

	public static OrderToAccount getAccountNameByOrderId(int orderid) {
		String sql = "SELECT * FROM `OrderToAccount` where orderID = " + String.valueOf(orderid);
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return null;
		}
		OrderToAccount ota = new OrderToAccount();
		ota.setOrderID(result.getInt(0));
		ota.setAccountName(result.getString(1));
		result.close();
		return ota;
	}

	public static int saveOrderToAccount(OrderToAccount ota) {
		ContentValues values = new ContentValues();
		values.put("orderID", ota.getOrderID());
		values.put("accountName", ota.getAccountName());
		msqlitedb.insertWithOnConflict("OrderToAccount", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		return 0;
	}

	public static int removeOrderToAccount(int orderID) {
		return msqlitedb.delete("OrderToAccount", "orderID=" + orderID, null);
	}

	public static int removeOrderToOnsale(int orderID) {
		return msqlitedb.delete("OrderToOnsale", "orderID=" + orderID, null);
	}

	public static int saveOrderToOnsale(OrderToOnsale ota) {
		ContentValues values = new ContentValues();
		values.put("orderID", ota.getOrderID());
		values.put("onsaleID", ota.getOnsaleID());
		values.put("value", ota.getValue());
		msqlitedb.insertWithOnConflict("OrderToOnsale", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		return 0;
	}
	// 回收Attribute 和 Onsale
}
