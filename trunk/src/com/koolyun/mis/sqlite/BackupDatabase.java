package com.koolyun.mis.sqlite;

import android.database.sqlite.SQLiteException;

import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.CynovoConstant;

public class BackupDatabase extends LocalDatabase {

	protected static LocalDatabase backupDatabase;

	public LocalDatabase getAnInstance() throws SQLiteException {

		if (backupDatabase == null)
			backupDatabase = new BackupDatabase(CynovoConstant.STORE_BACKUP_PATH + DBHelper.DATABASE_NAME,
					DBHelper.DATABASE_VERSION);

		return backupDatabase;
	}

	public BackupDatabase(String dbname, int version) throws SQLiteException {
		db = new DBHelper(CloudPosApp.getInstance(), dbname, version).getWritableDatabase();
	}

	public BackupDatabase() {

	}

	public void SetBackupDatabaseToNull() {
		backupDatabase = null;
	}

}
