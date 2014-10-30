package com.koolyun.mis.core;

import android.database.sqlite.SQLiteDatabase;

import com.koolyun.mis.sqlite.LocalDatabase;

public class ManagerBase {

	protected static SQLiteDatabase msqlitedb;
	protected static LocalDatabase mlocaldb;

	static {
		mlocaldb = LocalDatabase.getInstance();
		msqlitedb = mlocaldb.getSQLiteDatabase();
	}

}
