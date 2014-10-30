package com.koolyun.mis.sqlite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.util.MyLog;

/*
 * SQLite数据库生成函数
 * 
 */

public class DBHelper extends SQLiteOpenHelper {

	static String packagename;
	public static final String DATABASE_NAME = "cloud_pos_par10.db"; // SQLite本地数据库名称
	/**
	 * 4: 5:添加会员卡、会员2张表
	 */
	public static final int DATABASE_VERSION = 6; // SQLite本地数据库版本号
	private static String databaseFilename;
	private Context context;

	/**
	 * accountID 账户主键 accountPrivilegeID 账号权限主键 accountName account pass
	 * operatorNo 操作员代码 accountPhoto
	 */
	// private static final String TABLE_ACCOUNT =
	// "CREATE TABLE IF NOT EXISTS `Account` (`accountID` INTEGER primary key autoincrement, "
	// +
	// "`accountPrivilegeID` INTEGER, `accountName` VARCHAR,`account` VARCHAR, `pass` VARCHAR, `operatorNo` VARCHAR, "
	// +
	// "`accountPhoto` VARCHAR)";
	private static final String TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS `Account` (`accountID` INTEGER PRIMARY KEY ,"
			+ "`accountPrivilegeID` INTEGER,`firstName` VARCHAR DEFAULT (null) ,`account` VARCHAR,`pass` VARCHAR,"
			+ "`operatorNo` VARCHAR,`accountPhoto` VARCHAR, `lastName` VARCHAR, `sync` BOOL DEFAULT true, `phoneID` INTEGER)";

	/**
	 * accountPrivilegeID 账号权限主键 name value
	 */
	private static final String TABLE_ACCOUNTPRIVILEGE = "CREATE TABLE IF NOT EXISTS `AccountPrivilege` (`accountPrivilegeID` INTEGER primary key, `name` VARCHAR, "
			+ "`value` INTEGER )";
	/**
	 * storeID 门店主键 storeName storeAddressID storePhoto companyAddressID
	 * companyName companyPhoto nickName address companyAddress
	 */
	// private static final String TABLE_STORE =
	// "CREATE TABLE IF NOT EXISTS `Store` (`storeID` INTEGER," +
	// "`storeName` VARCHAR, `storeAddressID` INTEGER, `storePhoto` INTEGER, `companyAddressID` INTEGER,"
	// +
	// "`companyName` VARCHAR, `companyPhoto` VARCHAR)";
	private static final String TABLE_STORE = "CREATE TABLE IF NOT EXISTS `Store` (`storeID` INTEGER,`storeName` VARCHAR, "
			+ "`storeAddressID` INTEGER, `storePhoto` INTEGER, `companyAddressID` INTEGER,"
			+ "`companyName` VARCHAR, `companyPhoto` VARCHAR, `nickName` VARCHAR, `address` VARCHAR,`companyAddress` VARCHAR)";
	// TODO : remove ,`serialID` CHAR ,`merchantCode` CHAR,`terminalCode` CHAR
	/**
	 * cynovoServerDomain 矽鼎域名 cynovoServerPort 矽鼎端口号 cardInfoLinkIP 迅联ip
	 * cardInfoLinkPort 迅联端口号 serialNumber 小票流水号 traceNo 迅联流水号 serialID
	 * merchantCode terminalCode
	 */
	private static final String TABLE_SETTINGS = "CREATE TABLE IF NOT EXISTS `Settings` (`cynovoServerDomain` CHAR, "
			+ "`cynovoServerPort` CHAR, `cardInfoLinkIP` CHAR, `cardInfoLinkPort` CHAR, `serialNumber` CHAR, `traceNo` CHAR)";
	/**
	 * addressID 商家地址主键 addressInfo longitude latitude
	 */
	private static final String TABLE_ADDRESS = "CREATE TABLE IF NOT EXISTS `Address` (`addressID` INTEGER primary key autoincrement, "
			+ "`addressInfo` VARCHAR, `longitude` INTEGER ,`latitude` INTEGER)";
	/**
	 * orderID 订单主键 billNo orderStatusID createTime modifyTime syncFlag
	 */
	private static final String TABLE_ORDERS = "CREATE TABLE IF NOT EXISTS `Orders` (`orderID` INTEGER PRIMARY KEY autoincrement, `billNo` VARCHAR ,"
			+ "`orderStatusID` INTEGER, `createTime` INTEGER , `modifyTime` INTEGER , `syncFlag` INTEGER,`txnId` VARCHAR)";
	/**
	 * orderContentID 订单内容主键 productID orderID count price
	 */
	private static final String TABLE_ORDER_CONTENT = "CREATE TABLE IF NOT EXISTS `OrderContent` (`orderContentID` INTEGER primary key autoincrement, `productID` INTEGER, "
			+ "`orderID` INTEGER, `count` INTEGER, `price` VARCHAR)";
	/**
	 * orderContentID productSubAttributeId price
	 */
	// 联合主键
	private static final String TABLE_ORDER_CONTENT_TO_ATTR = "CREATE TABLE IF NOT EXISTS `OrderContentToAttribute` (`orderContentID` INTEGER, `productSubAttributeId` INTEGER, `price` VARCHAR)";

	/**
	 * productID 产品主键 name productPhoto price productCategoryID enable syncFlag
	 */
	private static final String TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS `Product` (`productID` INTEGER primary key, "
			+ "`name` VARCHAR, `productPhoto` VARCHAR, `price` VARCHAR, `productCategoryID` INTEGER, `enable` INTEGER, `syncFlag` INTEGER)";
	/**
	 * productAttributeID 产品属性主键 name choiceType defaultChoice enable syncFlag
	 */
	private static final String TABLE_PRODUCT_ATTRIBUTE = "CREATE TABLE IF NOT EXISTS `ProductAttribute` (`productAttributeID` INTEGER primary key, "
			+ " `name` VARCHAR, `choiceType` INTEGER ,`defaultChoice` INTEGER , `enable` INTEGER, `syncFlag` INTEGER)";
	/**
	 * productSubAttributeId 产品子属性主键 productAttributeID name priceAffect
	 * 价格影响？？？？？？？？？？？？？ enable syncFlag
	 */
	private static final String TABLE_PRODUCT_SUBATTRIBUTE = "CREATE TABLE IF NOT EXISTS `ProductSubAttribute` (`productSubAttributeId` INTEGER primary key not null, "
			+ "`productAttributeID` INTEGER, `name` VARCHAR, `priceAffect` VARCHAR, `enable` INTEGER, `syncFlag` INTEGER)";
	/**
	 * productCategoryID 产品分类主键 name enable syncFlag
	 */
	private static final String TABLE_PRODUCT_CATEGORY = "CREATE TABLE IF NOT EXISTS `ProductCategory` (`productCategoryID` INTEGER primary key not null, `name` VARCHAR , `enable` INTEGER, `syncFlag` INTEGER)";
	/**
	 * orderProcessId 订单处理主键 OrderProcessModeId orderID addressID createTime
	 * traceNo 受卡方系统跟踪号 referenceNo 检索参考号 authorizationNo 授权码 clearingDate 清算日期
	 * ackNo price issuerbankid ？？？？？？？？？？？ acqbank ？？？？？？？？？？？ batchid
	 * ？？？？？？？？？？？
	 */
	private static final String TABLE_ORDER_PROCESS = "CREATE TABLE IF NOT EXISTS `OrderProcess` (`orderProcessId` INTEGER primary key autoincrement, `OrderProcessModeId` INTEGER,"
			+ " `orderID` INTEGER, `addressID` INTEGER , `createTime` VARCHAR , "
			+ "`traceNo` VARCHAR,  `referenceNo` VARCHAR, `authorizationNo` VARCHAR, `clearingDate` VARCHAR, `ackNo` VARCHAR, "
			+ "`price` VARCHAR, `issuerbankid` VARCHAR, `acqbank` VARCHAR, `batchid` VARCHAR)";
	/**
	 * orderStatusID 订单状态主键 -------------------- name value
	 */
	private static final String TABLE_ORDER_STATUS = "CREATE TABLE IF NOT EXISTS `OrderStatus` (`orderStatusID` INTEGER primary key, `name` VARCHAR, "
			+ "`value` INTEGER)";
	/**
	 * OrderProcessModeId -------------- name value
	 */
	private static final String TABLE_ORDER_PROCESS_MODE = "CREATE TABLE IF NOT EXISTS `OrderProcessMode` (`OrderProcessModeId` INTEGER primary key autoincrement, `name` VARCHAR, "
			+ "`value` INTEGER)";
	/**
	 * paymentTypeID 支付类型主键 name value
	 */
	private static final String TABLE_PAYMENT_TYPE = "CREATE TABLE IF NOT EXISTS `PaymentType` (`paymentTypeID` INTEGER primary key autoincrement, `name` VARCHAR , `value` VARCHAR)";
	/**
	 * orderID 订单主键、外键 paymentTypeID amount 金额
	 */
	private static final String TABLE_ORDERS_TO_PAYMENT_TYPE = "CREATE TABLE IF NOT EXISTS `OrdersToPaymentType` (`orderID` INTEGER primary key, `paymentTypeID` INTEGER, `amount` VARCHAR)";
	/**
	 * orderProcessId 订单处理主键、外键 bankCardId signature telephoneNo emailInfo
	 */
	private static final String TABLE_ORDER_CUSTOMERINFO = "CREATE TABLE IF NOT EXISTS `OrderCustomerInfo` (`orderProcessId` INTEGER primary key, `bankCardId` INTEGER, `signature` VARCHAR, "
			+ "`telephoneNo` VARCHAR, `emailInfo` VARCHAR )";
	/**
	 * bankCardId bankCardNo validThrough cardOwnerName
	 */
	private static final String TABLE_BANKCARD = "CREATE TABLE IF NOT EXISTS `BankCard` (`bankCardId` INTEGER primary key autoincrement, `bankCardNo` VARCHAR, `validThrough` VARCHAR, "
			+ "`cardOwnerName` VARCHAR )";
	/**
	 * onsaleID 折扣主键 onsaleType onsaleName value enable syncFlag
	 */
	private static final String TABLE_ONSALE = "CREATE TABLE IF NOT EXISTS `Onsale` (`onsaleID` INTEGER primary key autoincrement, `onsaleType` INTEGER, `onsaleName` VARCHAR, "
			+ "`value` VARCHAR , `enable` INTEGER, `syncFlag` INTEGER)";
	/**
	 * orderContentID 订单内容主键、外键 onsaleID 折扣主键、外键 value
	 */
	private static final String TABLE_ORDERCONTENT_TO_ONSALE = "CREATE TABLE IF NOT EXISTS `OrderContentToOnsale` (`orderContentID` INTEGER , `onsaleID` INTEGER, `value` VARCHAR )";
	/**
	 * productID ----------------- productAttributeID
	 */
	private static final String TABLE_PRODUCT_TO_ATTRIBUTE = "CREATE TABLE IF NOT EXISTS `ProductToAttribute` (`productID` INTEGER , `productAttributeID` VARCHAR ,`enable` INTEGER,primary key (productID,productAttributeID))";
	/**
	 * reverseAttrId 冲正主键 ？？？？？？？？？？？？？ OrderProcessModeId traceNo
	 * authorizationNo price reason
	 */
	// private static final String TABLE_REVERSE_ATTRIBUTE =
	// "CREATE TABLE IF NOT EXISTS `ReverseAttribute` (`reverseAttrId` INTEGER primary key autoincrement, `OrderProcessModeId` INTEGER , "
	// +
	// "`traceNo` VARCHAR , `authorizationNo` VARCHAR, `price` VARCHAR, `reason` VARCHAR)";
	private static final String TABLE_REVERSE_ATTRIBUTE = "CREATE TABLE IF NOT EXISTS `ReverseAttribute` (`reverseAttrId` INTEGER primary key autoincrement, "
			+ "`OrderProcessModeId` INTEGER ,`traceNo` VARCHAR , `authorizationNo` VARCHAR, "
			+ "`price` VARCHAR, `reason` VARCHAR, `icf55` BLOB, `icf55len` INTEGER, `icpan` VARCHAR, "
			+ "`icdate` VARCHAR, `iccsn` VARCHAR)";
	/**
	 * orderContentID 订单内容主键 remark 备注信息
	 */
	private static final String TABLE_REMARK = "CREATE TABLE IF NOT EXISTS `OrderContentRemark` (`orderContentID` INTEGER , `remark` VARCHAR )";
	/**
	 * orderID remark
	 */
	// private static final String TABLE_ORDER_REMARK =
	// "CREATE TABLE IF NOT EXISTS `OrderRemark` (`orderID` INTEGER , `remark` VARCHAR )";
	private static final String TABLE_ORDER_REMARK = "CREATE TABLE IF NOT EXISTS `OrderRemark` (`orderID` INTEGER , `remark` VARCHAR , `sitindex` VARCHAR)";
	/**
	 * ErrorCode 代码 sense 意义 category 类别 reason 原因 display 显示
	 */
	private static final String TABLE_YL_RETURN_CODE = "CREATE TABLE IF NOT EXISTS `ReturnCode` (`ErrorCode` CHAR , `sense` VARCHAR , `category` VARCHAR, `reason` VARCHAR, `display` VARCHAR)";

	private static final String TABLE_ORDER_TO_ACCOUNT = "CREATE TABLE IF NOT EXISTS `OrderToAccount` (`orderID` INTEGER PRIMARY KEY  NOT NULL  UNIQUE , `accountName` VARCHAR)";

	private static final String TABLE_ORDER_TO_ONSALE = "CREATE TABLE IF NOT EXISTS `OrderToOnsale` (`orderID` INTEGER,`onsaleID` INTEGER, `value` VARCHAR )";

	/**
	 * 会员卡
	 */
	private static final String TABLE_CARD = "CREATE TABLE IF NOT EXISTS `card` ("
			+ "`card_no` VARCHAR PRIMARY KEY NOT NULL , "
			+ "`member_no` INTEGER NOT NULL , `type_id` INTEGER, `balance` FLOAT, `onsale` FLOAT, "
			+ "`dead_line` DATETIME, `remark` VARCHAR)";

	private static final String TABLE_MEMBERSHIP = "CREATE TABLE IF NOT EXISTS `membership` ("
			+ "`id` INTEGER PRIMARY KEY autoincrement, `shop_id` INTEGER NOT NULL , `name` VARCHAR NOT NULL , "
			+ "`phone` VARCHAR NOT NULL)";

	private static String[] tableList = { TABLE_ACCOUNT, TABLE_ACCOUNTPRIVILEGE, TABLE_STORE, TABLE_SETTINGS,
			TABLE_ADDRESS, TABLE_ORDERS, TABLE_ORDER_CONTENT, TABLE_ORDER_CONTENT_TO_ATTR, TABLE_PRODUCT,
			TABLE_PRODUCT_ATTRIBUTE, TABLE_PRODUCT_SUBATTRIBUTE, TABLE_PRODUCT_CATEGORY, TABLE_ORDER_PROCESS,
			TABLE_ORDER_STATUS, TABLE_ORDER_PROCESS_MODE, TABLE_PAYMENT_TYPE, TABLE_ORDERS_TO_PAYMENT_TYPE,
			TABLE_ORDER_CUSTOMERINFO, TABLE_BANKCARD, TABLE_ONSALE, TABLE_ORDERCONTENT_TO_ONSALE,
			TABLE_PRODUCT_TO_ATTRIBUTE, TABLE_REVERSE_ATTRIBUTE, TABLE_REMARK, TABLE_ORDER_REMARK,
			TABLE_YL_RETURN_CODE, TABLE_ORDER_TO_ACCOUNT, TABLE_ORDER_TO_ONSALE, TABLE_CARD, TABLE_MEMBERSHIP };
	// private static String[] tableNameList = { "Account", "AccountPrivilege",
	// "Store", "Settings", "Address",
	// "Orders", "OrderContent", "OrderContentToAttribute", "Product",
	// "ProductAttribute", "ProductSubAttribute",
	// "ProductCategory", "OrderProcess", "OrderStatus", "OrderProcessMode",
	// "PaymentType", "OrdersToPaymentType",
	// "OrderCustomerInfo", "BankCard"
	// ,"Onsale","OrderContentToOnsale","ProductToAttribute",
	// "ReverseAttribute","OrderContentRemark","OrderRemark","ReturnCode"};

	private static final String INSERT_SETTING = "INSERT INTO `Settings` VALUES('192.168.1.20','80',"
			+ "'211.147.64.198','5801','000001','000001')";
	private static final String INSERT_STORE = "INSERT INTO `Store` VALUES(0,'',0,'',0,'','','','','')";

	public DBHelper(Context context, String name, int version) {
		super(context, name, null, version);
		this.context = context;
		// // 加上这一句的原因 http://www.cnblogs.com/javawebsoa/p/3237018.html
		// if(Build.VERSION.SDK_INT >= 11){
		// getWritableDatabase().enableWriteAheadLogging();
		// }
	}

	// 当本地没有此数据库时回调
	@Override
	public void onCreate(SQLiteDatabase db) {
		MyLog.d("SQLiteOpenHelper", "onCreate---------------------------------------------");

		db.beginTransaction();
		try {
			for (int i = 0; i < tableList.length; i++) {
				db.execSQL(tableList[i]);
			}
			for (int i = 0; i < BankErrorCode.tableList.length; i++) {
				db.execSQL(BankErrorCode.tableList[i]);
			}
			for (int i = 0; i < CardIssuersCode.tableList.length; i++) {
				db.execSQL(CardIssuersCode.tableList[i]);
			}
			db.execSQL(INSERT_SETTING);
			db.execSQL(INSERT_STORE);
			db.setTransactionSuccessful();

			InputStream is = null;
			BufferedReader br = null;

			try {
				// 中文
				is = context.getAssets().open("cloud_pos_par10.sql");
				// 英文
				// is = context.getAssets().open("cloud_pos_par10_en.sql");
				br = new BufferedReader(new InputStreamReader(is));
				String str;
				while ((str = br.readLine()) != null) {
					db.execSQL(str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
						br = null;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					if (is != null) {
						is.close();
						is = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			db.endTransaction();
		}
	}

	// 当本地数据库版本号不一样时回调
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MyLog.d("SQLiteOpenHelper", "onUpgrade---------------------------------------------");

		db.beginTransaction();
		try {
			if (oldVersion <= 5) {
				db.execSQL("DROP TABLE card"); // 会员
				db.execSQL("DROP TABLE membership"); // 会员卡
				db.execSQL(TABLE_MEMBERSHIP); // 会员
				db.execSQL(TABLE_CARD); // 会员卡
			}

			if (oldVersion <= 4) {
				db.execSQL(TABLE_MEMBERSHIP); // 会员
				db.execSQL(TABLE_CARD); // 会员卡
			}

			if (oldVersion <= 3) {
				for (int i = 0; i < CardIssuersCode.tableList.length; i++) {
					db.execSQL(CardIssuersCode.tableList[i]);
				}
			}
			if (oldVersion <= 2) {
				db.execSQL("DROP TABLE IF EXISTS `ProductToAttribute` ");
				db.execSQL(TABLE_PRODUCT_TO_ATTRIBUTE);
			}
			if (oldVersion <= 1) {
				db.execSQL(TABLE_ORDER_TO_ONSALE);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	@SuppressLint("SdCardPath")
	public static boolean isDataBaseExist(Context context) {

		packagename = context.getPackageName();
		databaseFilename = "/data/data/" + packagename + "/databases/" + DATABASE_NAME;

		File dir = new File(databaseFilename);
		if (dir.exists())// 判断文件夹是否存在
			return true;
		else
			return false;
	}

	public static String getDataBaseFilePath() {
		return databaseFilename;
	}

	@SuppressLint("SdCardPath")
	public static int copyDataBase(Context context) {

		File dir = new File("/data/data/" + packagename + "/databases/");
		if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
			dir.mkdirs();
		FileOutputStream os = null;
		String databaseFilenames = "/data/data/" + packagename + "/databases/" + DATABASE_NAME;
		try {
			os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流
		} catch (FileNotFoundException e) {
			return -1;
		}

		InputStream is;
		try {
			is = context.getAssets().open(DATABASE_NAME);
		} catch (IOException e) {
			// TODO:
			return -1;
		}// 得到数据库文件的数据流

		try {
			JavaUtil.copyStream(is, os);
		} catch (IOException e) {
			return -1;
		}

		return 0;
	}

	@SuppressLint("SdCardPath")
	public static int copyImage(Context context, String assetsPath) {
		String destDir = "/mnt/sdcard/DCIM/" + assetsPath;
		File dir = new File(destDir);
		if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
			dir.mkdirs();
		else {
			// 已经存在就覆盖
		}

		try {
			JavaUtil.copyAssetsDir(context, assetsPath, destDir);
		} catch (IOException e) {
			return -1;
		}
		return 0;
	}
}
