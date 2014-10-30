package com.koolyun.mis.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;

public class MyLog {
	private static boolean loggable = Configs.IsLoggable;
	private final static String tag = "mylog";

	private static String logfilename = null;

	/**
	 * 
	 * @param lvl
	 * @param action
	 */
	static private void record(String s) {
		try {
			File root;
			if (logfilename == null)
				logfilename = Configs.MyLogFile;
			if (Configs.IsRecordInExternalStorage) {
				root = Environment.getExternalStorageDirectory();
			} else {
				root = new File("/");
			}
			if (root.canWrite()) {
				File gpxfile = new File(root, logfilename);
				if (!gpxfile.exists())
					gpxfile.createNewFile();
				FileWriter gpxwriter = new FileWriter(gpxfile, true);
				BufferedWriter out = new BufferedWriter(gpxwriter);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
				Date date = new Date(System.currentTimeMillis());
				out.write(sdf.format(date) + "---->");
				out.write(s + "\n");
				out.close();
			}
		} catch (IOException e) {
			MyLog.e("Could not write file " + e.getMessage());
		}
	}

	public static void i(String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.i(tag, msg);
		if (Configs.IsLogInFile)
			record(msg);

	}

	public static void withTime(String msg) {
		if (null == msg)
			return;
		i(msg + " Time: " + (int) Math.floor(System.currentTimeMillis() / 1000));
	}

	public static void d(String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.d(tag, msg);
		if (Configs.IsLogInFile)
			record(msg);

	}

	public static void e(String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.e(tag, msg);
		if (Configs.IsLogInFile)
			record(msg);

	}

	public static void v(String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.v(tag, msg);
		if (Configs.IsLogInFile)
			record(msg);
	}

	public static void w(String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.w(tag, msg);
		if (Configs.IsLogInFile)
			record(msg);
	}

	public static void i(String tag1, String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.i(tag + tag1, msg);
		if (Configs.IsLogInFile)
			record(msg);
	}

	public static void d(String tag1, String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.d(tag + tag1, msg);
		if (Configs.IsLogInFile)
			record(msg);
	}

	public static void e(String tag1, String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.e(tag + tag1, msg);
		if (Configs.IsLogInFile)
			record(msg);
	}

	public static void v(String tag1, String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.v(tag + tag1, msg);
		if (Configs.IsLogInFile)
			record(msg);
	}

	public static void w(String tag1, String msg) {
		if (null == msg)
			return;
		if (loggable == false)
			return;
		Log.w(tag + tag1, msg);
		if (Configs.IsLogInFile)
			record(msg);
	}
}
