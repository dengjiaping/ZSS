package com.koolyun.mis.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.koolyun.mis.core.store.StoreManager;

public class ClearAllDBData {
	private DBHelper databaseHelper;
	private Context context;
	private SQLiteDatabase sqliteDatabase;
	private String[] dbNames = { "Account", "Orders", "OrderContent", "OrderContentToAttribute", "Product",
			"ProductAttribute", "ProductSubAttribute", "ProductCategory", "OrderProcess", "OrderStatus",
			"OrderProcessMode", "OrdersToPaymentType", "OrderCustomerInfo", "Onsale", "OrderContentToOnsale",
			"ProductToAttribute", "ReverseAttribute", "OrderContentRemark", "OrderRemark", "OrderToAccount",
			"OrderToOnsale", "TABLE_CARD", "TABLE_MEMBERSHIP" };

	public ClearAllDBData(Context context) {
		this.context = context;
	}

	/**
	 * 打开数据库连接
	 */
	private void open() {
		databaseHelper = new DBHelper(context, DBHelper.DATABASE_NAME, DBHelper.DATABASE_VERSION);

		try {
			sqliteDatabase = databaseHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			ex.printStackTrace();
			sqliteDatabase = databaseHelper.getReadableDatabase();
		}
	}

	/**
	 * 关闭数据库连接
	 */
	private void close() {
		sqliteDatabase.close();
	}

	/**
	 * 删除数据库中表的数据 注意：！！！！！ 不要删除表结构
	 */
	public void clear() {
		// delete方法第一参数：数据库表名，第二个参数表示条件语句,第三个参数为条件带?的替代值
		// 返回值大于0表示删除成功
		open();
		for (String str : dbNames) {
			try {
				sqliteDatabase.delete(str, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
		try {
			// 更新store表的信息
			StoreManager.updateStoreInfo("", "", "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
