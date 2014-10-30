package com.koolyun.mis.util.update;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UpdateDBHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "download.db";
	private static final int VERSION = 1;

	public UpdateDBHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (" + "id integer primary key autoincrement, " + // id
				"downpath varchar(256), " + // 下载路径
				"threadid INTEGER, " + // 线程id
				"downlength INTEGER, " + // 已下载长度
				"version INTEGER)"); // 版本号
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS filedownlog");
		onCreate(db);
	}
}