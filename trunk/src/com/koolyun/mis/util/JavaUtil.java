package com.koolyun.mis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class JavaUtil {

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isInterface(Class c, String szInterface) {
		Class[] face = c.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].getName().equals(szInterface)) {
				return true;
			} else {
				Class[] face1 = face[i].getInterfaces();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].getName().equals(szInterface)) {
						return true;
					} else if (isInterface(face1[x], szInterface)) {
						return true;
					}
				}
			}
		}
		if (null != c.getSuperclass()) {
			return isInterface(c.getSuperclass(), szInterface);
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isClass(Class c, String szInterface) {
		Class[] face = c.getClasses();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].getName().contains(szInterface)) {
				return true;
			} else {
				Class[] face1 = face[i].getClasses();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].getName().equals(szInterface)) {
						return true;
					} else if (isClass(face1[x], szInterface)) {
						return true;
					}
				}
			}
		}
		if (null != c.getSuperclass()) {
			return isClass(c.getSuperclass(), szInterface);
		}
		return false;
	}

	public static void copyStream(InputStream is, OutputStream os) throws IOException {

		byte[] buffer = new byte[1024 * 4];
		int count = 0;
		while ((count = is.read(buffer)) != -1) {
			os.write(buffer, 0, count);
		}
		os.flush();
		is.close();
		os.close();
	}

	// 复制文件夹
	public static void copyAssetsDir(Context context, String assetsPath, String targetDir) throws IOException {

		File dir = new File(targetDir);
		if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
			dir.mkdirs();

		String[] files = context.getAssets().list(assetsPath);
		for (int i = 0; i < files.length; i++) {
			InputStream is = context.getAssets().open(assetsPath + "/" + files[i]);
			FileOutputStream os = null;
			String databaseFilenames = targetDir + "/" + files[i];
			try {
				os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			copyStream(is, os);
		}
	}

	// 复制文件夹
	public static void copyFileToDir(Context context, String fileName, String targetDir) {

		File dir = new File(targetDir);
		if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
			dir.mkdirs();
		File file = new File(fileName);
		String filen = file.getName();
		if (file.getAbsolutePath().startsWith(targetDir))
			return;

		InputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream os = null;
		String tfile = targetDir + "/" + filen;
		try {
			os = new FileOutputStream(tfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			copyStream(is, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static String getPathFromUri(Uri mUri, Activity mActivity) {

		String[] proj = { MediaStore.Images.Media.DATA };

		Cursor cursor = mActivity.managedQuery(mUri, proj, null, null, null);

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		if (cursor.moveToFirst())
			return cursor.getString(column_index);
		else
			return null;

	}

}
