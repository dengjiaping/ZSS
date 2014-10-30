package com.koolyun.mis.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.util.Common;

public class AccountDBHelper {
	private DBHelper databaseHelper;
	private Context context;
	private SQLiteDatabase sqliteDatabase;

	private final String tableName = "Account";

	public AccountDBHelper(Context context) {
		this.context = context;
	}

	/**
	 * 打开数据库连接
	 */
	public void open() {
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
	public void close() {
		sqliteDatabase.close();
	}

	/**
	 * 向Account数据库插入一条数据
	 * 
	 * @param account
	 * @param autoOpenAndCloseDB
	 *            是否需要自动open/close数据库，如果传false，需要在程序中自己手动控制open/close
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public long insertAccount(Account account, boolean autoOpenAndCloseDB) {
		if (autoOpenAndCloseDB) {
			open();
		}
		// "`accountPrivilegeID` INTEGER, `accountName` VARCHAR,`account`
		// VARCHAR,
		// `pass` VARCHAR, `operatorNo` VARCHAR, " +
		// "`accountPhoto` VARCHAR)
		ContentValues content = new ContentValues();
		content.put("accountPrivilegeID", account.getAccountPrivilege());
		content.put("firstName", account.getFirstName());
		content.put("lastName", account.getLastName());
		content.put("account", account.getAccount());
		try {
			content.put("pass", Common.SHA1.toSHA1(account.getPass()).toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		content.put("operatorNo", account.getOperatorID());

		// content为插入表中的一条记录，类似与HASHMAP，是以键值对形式存储。
		// insert方法第一参数：数据库表名，第二个参数如果CONTENT为空时则向表中插入一个NULL,第三个参数为插入的内容
		long result = sqliteDatabase.insert(tableName, null, content);
		sqliteDatabase.execSQL(String.format("UPDATE %s set %s=%s", tableName, "operatorNo", "rowid"));
		if (autoOpenAndCloseDB) {
			close();
		}

		return result;
	}

	public long insertAccountFromNet(Account account, boolean autoOpenAndCloseDB) {
		if (autoOpenAndCloseDB) {
			open();
		}
		// "`accountPrivilegeID` INTEGER, `accountName` VARCHAR,`account`
		// VARCHAR,
		// `pass` VARCHAR, `operatorNo` VARCHAR, " +
		// "`accountPhoto` VARCHAR)
		ContentValues content = new ContentValues();
		content.put("accountPrivilegeID", account.getAccountPrivilege());
		content.put("firstName", account.getFirstName());
		content.put("lastName", account.getLastName());
		content.put("account", account.getAccount());
		content.put("pass", account.getPass());
		content.put("operatorNo", account.getOperatorID());
		content.put("phoneID", account.getPhoneID());

		// content为插入表中的一条记录，类似与HASHMAP，是以键值对形式存储。
		// insert方法第一参数：数据库表名，第二个参数如果CONTENT为空时则向表中插入一个NULL,第三个参数为插入的内容
		long result = sqliteDatabase.insert(tableName, null, content);

		sqliteDatabase.execSQL(String.format("UPDATE %s set %s=%s", tableName, "operatorNo", "rowid"));
		if (autoOpenAndCloseDB) {
			close();
		}

		return result;
	}

	/**
	 * 删除所有记录
	 * 
	 * @return
	 */
	public boolean deleteAllAccounts() {
		// delete方法第一参数：数据库表名，第二个参数表示条件语句,第三个参数为条件带?的替代值
		// 返回值大于0表示删除成功
		return sqliteDatabase.delete(tableName, null, null) > 0;
	}

	/**
	 * 删除表中符合条件的记录
	 * 
	 * @param rowId
	 *            删除条件
	 * @return 是否删除成功
	 */
	public boolean deleteAccount(long rowId) {
		// //delete方法第一参数：数据库表名，第二个参数表示条件语句,第三个参数为条件带?的替代值
		// //返回值大于0表示删除成功
		// return sqliteDatabase.delete(databaseHelper.DATABASE_TABLE,KEY_ROWID
		// +"="+rowId , null)>0;
		return true;
	}

	/**
	 * 查询全部表记录
	 * 
	 * @return 返回查询的全部表记录
	 */
	public Cursor getAllAccounts() {
		// //查询表中满足条件的所有记录
		// return sqliteDatabase.query(databaseHelper.DATABASE_TABLE, new
		// String[] { KEY_ROWID, KEY_TITLE,
		// KEY_BODY, KEY_CREATED }, null, null, null, null, null);
		return null;
	}

	/**
	 * 查询带条件的记录
	 * 
	 * @param rowId
	 *            条件值
	 * @return 返回查询结果
	 * @throws SQLException
	 *             查询时异常抛出
	 */
	public Cursor getAccount(long rowId) {

		// //查询表中条件值为rowId的记录
		// Cursor mCursor =
		// sqliteDatabase.query(true, databaseHelper.DATABASE_TABLE, new
		// String[] { KEY_ROWID, KEY_TITLE,
		// KEY_BODY, KEY_CREATED }, KEY_ROWID + "=" + rowId, null, null,
		// null, null, null);
		//
		// //mCursor不等于null,将标识指向第一条记录
		// if (mCursor != null) {
		// mCursor.moveToFirst();
		// }
		// return mCursor;
		return null;
	}

	/**
	 * 更新数据库
	 * 
	 * @param rowId
	 *            行标识
	 * @param title
	 *            内容
	 * @param body
	 *            内容
	 * @return 是否更新成功
	 */
	public boolean updateAccount(long rowId, Account account) {
		// ContentValues args = new ContentValues();
		// args.put(KEY_TITLE, title);
		// args.put(KEY_BODY, body);
		// Calendar calendar = Calendar.getInstance();
		// String created = calendar.get(Calendar.YEAR) + "年"
		// + calendar.get(Calendar.MONTH) + "月"
		// + calendar.get(Calendar.DAY_OF_MONTH) + "日"
		// + calendar.get(Calendar.HOUR_OF_DAY) + "时"
		// + calendar.get(Calendar.MINUTE) + "分";
		// args.put(KEY_CREATED, created);
		//
		// //第一个参数:数据库表名,第二个参数更新的内容,第三个参数更新的条件,第四个参数条件带?号的替代者
		// return sqliteDatabase.update(databaseHelper.DATABASE_TABLE, args,
		// KEY_ROWID + "=" + rowId, null) > 0;
		return true;
	}

}
