package com.koolyun.mis.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.koolyun.mis.util.CloudPosApp;

public class LocalDatabase {

	protected SQLiteDatabase db;

	protected static LocalDatabase localDatabase;

	public static LocalDatabase getInstance() {
		CloudPosApp app = CloudPosApp.getInstance();
		boolean isExist = DBHelper.isDataBaseExist(app);
		if (isExist != true) {
			int retval = DBHelper.copyDataBase(app);
			Log.i("LocalDatabase", String.valueOf(retval));
		}

		if (localDatabase == null)
			localDatabase = new LocalDatabase(DBHelper.DATABASE_NAME, DBHelper.DATABASE_VERSION);

		return localDatabase;
	}

	protected LocalDatabase() {

	}

	private LocalDatabase(String dbname, int version) {
		try {
			db = new DBHelper(CloudPosApp.getInstance(), dbname, version).getWritableDatabase();
		} catch (SQLiteException e) {
			db = new DBHelper(CloudPosApp.getInstance(), dbname, version).getReadableDatabase();
		}

	}

	protected void finalize() {
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		// other finalization code...
		if (db != null)
			db.close();
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return db;
	}

}
