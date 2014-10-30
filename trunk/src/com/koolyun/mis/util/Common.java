package com.koolyun.mis.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.koolyun.mis.core.setting.SettingsManager;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;

public class Common {

	public static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm";
	public static final String DATE_TIME_FORMAT_WITH_SECONDS = "yyyy-MM-dd HH:mm:ss";
	// 本地数据库表
	public static final String[] DB_TABLES = new String[] { "user", "onsale", "payment_type", "order_status",
			"product", "product_category", "product_category_type", "product_category_type_to_spec", "product_spec",
			"product_spec_info" };

	public static final String DBCHANGED_INTENT = "com.cynovo.sirius.intent.action.database.changed";
	public static final String EMAIL_FORMAT = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	// true: 不使用网络；false：使用网络
	public static final boolean NO_NETWORK = false;

	// true：不使用银联接口；false：使用银联接口
	public static final boolean NO_UNIONPAY = false;

	// true: 连接讯联生产服务器， 连接讯联测试服务器
	public static final boolean PRODUCTING = true;

	public static final String SIGNATURE_PATH = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DCIM).toString()
			+ "/";
	public static final String IMAGE_FOLDER = "DB_IMAGE/";

	public static final String getSqlString(String str) {
		return str.replace("'", "''");
	}

	public static final String IMAGE_PATH = SIGNATURE_PATH + IMAGE_FOLDER;

	public static boolean checkValidString(String string) {
		return string != null && !string.isEmpty() && !string.equals("null");
	}

	public static String getString(int resid) {
		return CloudPosApp.getInstance().getString(resid);
	}

	public static String getString(int resid, Object... formatArgs) {
		return CloudPosApp.getInstance().getString(resid, formatArgs);
	}

	// public static void hostlinkInit()
	// {
	// if(PRODUCTING)
	// {
	// Settings settings = SettingsManager.querySettings();
	// HostlinkInterface.setIP(settings.getCardlinkIP());
	// HostlinkInterface.setPort((short)5801);
	// HostlinkInterface.setNetworkTimeout(15, 0);
	// HostlinkInterface.setTPDU("6001010000");
	// HostlinkInterface.setCurrencyCode("156");
	// HostlinkInterface.setEnableLog(false);
	// }
	// else
	// {
	// HostlinkInterface.setIP("140.207.50.238");
	// HostlinkInterface.setPort((short) 5711);
	// HostlinkInterface.setTPDU("6000080000");
	// HostlinkInterface.setNetworkTimeout(60, 0);
	// HostlinkInterface.setCurrencyCode("826");
	// HostlinkInterface.setEnableLog(true);
	// }
	// }

	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			// 提取汉字的首字母
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	static private WakeLock mWakeLock = null;

	@SuppressLint("Wakelock")
	static public void acquireWakeLock(Context context) {

		if (mWakeLock == null) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, context.getPackageName());
			mWakeLock.acquire();
		}
	}

	static public void releaseWakeLock() {
		if (null != mWakeLock) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	public static void addShortcutToDesktop(Context context, int appName, int appIcon) {

		if (isShortcutInstalled(context, appName))
			return;
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra("duplicate", false);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(appName));
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(context, appIcon));

		Intent intent = new Intent(context, context.getClass());
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		context.sendBroadcast(shortcut);
		MySharedPreferencesEdit mySharedPreferencesEdit = MySharedPreferencesEdit.getInstancePublic(CloudPosApp
				.getInstance());
		mySharedPreferencesEdit.setIsShortCutInstall(true);
	}

	/**
	 * 快捷方式信息是保存在com.android.launcher的launcher.db的favorites表中
	 * 
	 * @return
	 */
	private static boolean isShortcutInstalled(Context context, int appName) {
		/*
		 * boolean isInstallShortcut = false; final ContentResolver cr =
		 * context.getContentResolver(); //
		 * 2.2系统是”com.android.launcher2.settings
		 * ”,网上见其他的为"com.android.launcher.settings" String AUTHORITY = null;
		 * if(getSystemVersion()>=8){ AUTHORITY =
		 * "com.android.launcher2.settings"; }else{ AUTHORITY =
		 * "com.android.launcher.settings"; }
		 * 
		 * Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY +
		 * "/favorites?notify=true"); Cursor c = cr.query(CONTENT_URI, new
		 * String[] { "title", "iconResource" }, "title=?", new String[] {
		 * getString(appName) }, null); if (c != null && c.getCount() > 0) {
		 * isInstallShortcut = true; } c.close(); return isInstallShortcut;
		 */
		MySharedPreferencesEdit mySharedPreferencesEdit = MySharedPreferencesEdit.getInstancePublic(CloudPosApp
				.getInstance());
		return mySharedPreferencesEdit.getIsShortCutInstall();
	}

	/**
	 * 获取系统的SDK版本号
	 * 
	 * @return
	 */
	private static int getSystemVersion() {
		return Build.VERSION.SDK_INT;
	}

	public static String getFirstPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length() && j < 1; j++) {
			char word = str.charAt(j);
			// 提取汉字的首字母
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert.toUpperCase(Locale.CHINESE);
	}

	public static String getCnASCII(String cnStr) {
		StringBuffer strBuf = new StringBuffer();
		// 将字符串转换成字节序列
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			// System.out.println(Integer.toHexString(bGBK[i] & 0xff));
			// 将每个字符转换成ASCII码
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
		}
		return strBuf.toString();
	}

	public static String getPinYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		// System.out.println(t1.length);
		String[] t2 = new String[t1.length];
		// System.out.println(t2.length);
		// 设置汉字拼音输出的格式
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断能否为汉字字符
				// System.out.println(t1[i]);
				if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
					t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
				} else {
					// 如果不是汉字字符，间接取出字符并连接到字符串t4后
					t4 += Character.toString(t1[i]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return t4;
	}

	public static void FlipperShowIndex(Context context, ViewFlipper viewf, int index, int[] anmiArray) {
		if (viewf.getChildCount() != anmiArray.length / 2)
			return;
		if (viewf.getDisplayedChild() != index) {
			viewf.setInAnimation(context, anmiArray[2 * index]);
			viewf.setOutAnimation(context, anmiArray[2 * index + 1]);
			viewf.setDisplayedChild(index);
		}

	}

	public static String bcd2Str(byte[] bcd, int len) {
		StringBuilder sb = new StringBuilder(len * 2);
		for (int i = 0; i < len; i++) {
			sb.append(String.format("%02x", bcd[i]));
		}
		return sb.toString();
	}

	public static String hex2Str(byte[] hex, int len) {
		char tmp[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuilder sb = new StringBuilder(len * 2);
		for (int i = 0; i < len; i++) {
			sb.append(tmp[hex[i] / 16]);
			sb.append(tmp[hex[i] % 16]);
		}
		return sb.toString();
	}

	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	public static String getRealImagePath(String name) {
		return SIGNATURE_PATH + IMAGE_FOLDER + name;
	}

	public static long getTimeMillis(String timestr, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
		try {
			return formatter.parse(timestr).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDateTimeString(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date(time);
		return formatter.format(curDate);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getAllDateTimeString(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Date curDate = new Date(time);
		return formatter.format(curDate);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDateTimeString(long time, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date curDate = new Date(time);
		return formatter.format(curDate);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDateTimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}

	public static String getCurrentDateTimeString(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}

	/**
	 * 获得开始时间
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrentDateStartTimeString(String format, Context context) {
		MySharedPreferencesEdit mySharedPreferencesEdit = MySharedPreferencesEdit.getInstancePublic(context);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, mySharedPreferencesEdit.getStartHour());
		calendar.set(Calendar.MINUTE, mySharedPreferencesEdit.getStartMinute());
		if (mySharedPreferencesEdit.getStartToEndDays() == 0
				&& (mySharedPreferencesEdit.getStartHour() * 60 + mySharedPreferencesEdit.getStartMinute() > mySharedPreferencesEdit
						.getEndHour() * 60 + mySharedPreferencesEdit.getEndMinute())) {
			// 开始时间晚于结束时间，将开始时间向前移一天
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		} else {
			calendar.add(Calendar.DAY_OF_MONTH, -mySharedPreferencesEdit.getStartToEndDays());
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
		Date curDate = new Date(calendar.getTimeInMillis());
		return formatter.format(curDate);
	}

	/**
	 * 获得结束时间
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrentDateEndTimeString(String format, Context context) {
		MySharedPreferencesEdit mySharedPreferencesEdit = MySharedPreferencesEdit.getInstancePublic(context);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, mySharedPreferencesEdit.getEndHour());
		calendar.set(Calendar.MINUTE, mySharedPreferencesEdit.getEndMinute());
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
		Date curDate = new Date(calendar.getTimeInMillis());
		return formatter.format(curDate);
	}

	public static String getBillNumber() {
		String billno = null;
		billno = AccountManager.getInstance().getmBaseInfo().getmTerminalCode();
		billno += getCurrentDateTimeString("yyMMdd");
		billno += SettingsManager.querySettings().incAndGetSerialNumber();
		return billno;
	}

	public static String getSerialNumberFromBillNo(String BillNo) {
		if (BillNo != null && BillNo.length() == 20)
			return BillNo.substring(14, 20);
		else
			return "";
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param obj
	 *            Activity或Fragment或View的实例
	 * 
	 * @Feel添加 Fragment有可能是
	 *         android.app.Fragment，也有可能是android.support.v4.app.Fragment
	 */
	public static void HideKeyboardIfExist(Object obj) {
		if (obj == null)
			return;
		InputMethodManager inputMgr = (InputMethodManager) CloudPosApp.getInstance().getSystemService(
				Context.INPUT_METHOD_SERVICE);

		if (JavaUtil.isClass(obj.getClass(), "Activity")) {
			Activity aTmp = (Activity) obj;
			if (aTmp.getCurrentFocus() != null)
				inputMgr.hideSoftInputFromWindow(aTmp.getCurrentFocus().getWindowToken(), 0);
		} else if (JavaUtil.isClass(obj.getClass(), "Fragment")) {
			try {
				Fragment fTmp = (Fragment) obj;
				if (fTmp.getActivity().getCurrentFocus() != null)
					inputMgr.hideSoftInputFromWindow(fTmp.getActivity().getCurrentFocus().getWindowToken(), 0);
			} catch (Exception e) {
				android.support.v4.app.Fragment fTmp = (android.support.v4.app.Fragment) obj;
				if (fTmp.getActivity().getCurrentFocus() != null)
					inputMgr.hideSoftInputFromWindow(fTmp.getActivity().getCurrentFocus().getWindowToken(), 0);
				// e.printStackTrace();
			}
		} else if (JavaUtil.isClass(obj.getClass(), "View")) {
			View vTmp = (View) obj;
			inputMgr.hideSoftInputFromWindow(vTmp.getWindowToken(), 0);
		}
	}

	/**
	 * 检测网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetwork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
		return (activeNetwork != null && activeNetwork.isConnected());
	}

	/**
	 * 提示框
	 * 
	 * @param context
	 * @param str
	 */
	public static void toastShown(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 检测用户名是否合法
	 * 
	 * @param context
	 * @param userName
	 * @return
	 */
	public static boolean testUsername(Context context, String userName) {
		if (isInputEmpty(userName)) {
			toastShown(context, "用户名或密码为空");
			return false;
		}
		if (!isEmailValid(userName)) {
			Common.toastShown(context, "用户名不合法");
			return false;
		}
		return true;
	}

	/**
	 * 检测密码是否合法
	 * 
	 * @param context
	 * @param password
	 * @return
	 */
	public static boolean testPassword(Context context, String password) {
		if (isInputEmpty(password)) {
			toastShown(context, "用户名或密码为空");
			return false;
		}
		return true;
	}

	/**
	 * md5信息摘要
	 * 
	 * @param string
	 * @return
	 */
	public static String md5(String string) {
		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * 检测输入是否为空
	 * 
	 * @param input
	 * @return
	 */
	private static boolean isInputEmpty(String input) {
		if (input.equals("")) {
			return true;
		}
		return false;
	}

	// 检测email地址是否合法
	private static boolean isEmailValid(String email) {
		if (!email.matches(EMAIL_FORMAT)) {
			return false;
		}
		return true;
	}

	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	@SuppressLint("DefaultLocale")
	public static boolean checkValidImageName(String imageName) {
		String endfix[] = { ".png", ".jpg" };
		if (imageName == null || imageName.isEmpty())
			return false;
		String tmp = imageName.toLowerCase();
		for (int i = 0; i < endfix.length; i++) {
			if (tmp.endsWith(endfix[i]))
				return true;
		}
		return false;
	}

	public static class SHA1 {
		private static String convertToHex(byte[] data) {
			StringBuilder buf = new StringBuilder();
			for (byte b : data) {
				int halfbyte = (b >>> 4) & 0x0F;
				int two_halfs = 0;
				do {
					buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte)
							: (char) ('a' + (halfbyte - 10)));
					halfbyte = b & 0x0F;
				} while (two_halfs++ < 1);
			}
			return buf.toString();
		}

		public static String toSHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			byte[] sha1hash = md.digest();
			return convertToHex(sha1hash);
		}
	}

	/**
	 * 检查输入的手机号格式是否正确
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobilePhoneNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^(1[0-9]{10})$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/** 等比例压缩，取按较小的比例 **/
	public static Bitmap zoomBitmapBetter(Bitmap bitmap, int width, int height) {
		if (bitmap == null) {
			return null;
		}

		Bitmap signedbmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
		Paint paint = new Paint();
		;
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setFilterBitmap(true);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(10);
		Bitmap basebmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(basebmp);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		canvas.drawBitmap(signedbmp, 0, 0, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		@SuppressWarnings("deprecation")
		BitmapDrawable drawable = new BitmapDrawable(basebmp);
		drawable.setAntiAlias(true);
		drawable.setFilterBitmap(true);
		drawable.setDither(true);

		return drawable.getBitmap();
	}
}
