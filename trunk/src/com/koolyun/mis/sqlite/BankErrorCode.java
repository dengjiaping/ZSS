package com.koolyun.mis.sqlite;

public class BankErrorCode {

	public static final String TABLE_BR_AA = "DROP TABLE IF EXISTS `ReturnCode`";
	public static final String TABLE_BR_00 = "CREATE TABLE `ReturnCode` (`ErrorCode` CHAR, `sense` VARCHAR, `category` VARCHAR, `reason` VARCHAR, `display` VARCHAR)";
	public static final String TABLE_BR_01 = "INSERT INTO `ReturnCode` VALUES('00','承兑或交易成功','A','承兑或交易成功','交易成功')";
	public static final String TABLE_BR_02 = "INSERT INTO `ReturnCode` VALUES('01','查发卡行','C','查发卡行','交易失败，请联系发卡行')";
	public static final String TABLE_BR_03 = "INSERT INTO `ReturnCode` VALUES('02','查发卡行的特殊条件','C','可电话向发卡行查询','交易失败，请联系发卡行')";
	public static final String TABLE_BR_04 = "INSERT INTO `ReturnCode` VALUES('03','无效商户','C','商户需要在银行或中心登记','商户未登记')";
	public static final String TABLE_BR_05 = "INSERT INTO `ReturnCode` VALUES('04','没收卡','D','操作员没收卡','没收卡，请联系收单行')";
	public static final String TABLE_BR_06 = "INSERT INTO `ReturnCode` VALUES('05','不予承兑','C','发卡不予承兑','交易失败，请联系发卡行')";
	public static final String TABLE_BR_07 = "INSERT INTO `ReturnCode` VALUES('06','出错','E','发卡行故障','交易失败，请联系发卡行')";
	public static final String TABLE_BR_08 = "INSERT INTO `ReturnCode` VALUES('07','特殊条件下没收卡','D','特殊条件下没收卡','没收卡，请联系收单行')";
	public static final String TABLE_BR_09 = "INSERT INTO `ReturnCode` VALUES('09','请求正在处理中','B','重新提交交易请求','交易失败，请重试')";
	public static final String TABLE_BR_10 = "INSERT INTO `ReturnCode` VALUES('12','无效交易','C','发卡行不支持的交易','交易失败，请重试')";
	public static final String TABLE_BR_11 = "INSERT INTO `ReturnCode` VALUES('13','无效金额','B','金额为0或太大','交易金额超限，请重试')";
	public static final String TABLE_BR_12 = "INSERT INTO `ReturnCode` VALUES('14','无效卡号','B','卡种未在中心登记或读卡号有误','无效卡号，请联系发卡行')";
	public static final String TABLE_BR_13 = "INSERT INTO `ReturnCode` VALUES('15','无此发卡行','C','此发卡行未与中心开通业务','此卡不能受理')";
	public static final String TABLE_BR_14 = "INSERT INTO `ReturnCode` VALUES('19','重新送入交易','C','刷卡读取数据有误，可重新刷卡','交易失败，请联系发卡行')";
	public static final String TABLE_BR_15 = "INSERT INTO `ReturnCode` VALUES('20','无效应答','C','无效应答','交易失败，请联系发卡行')";
	public static final String TABLE_BR_16 = "INSERT INTO `ReturnCode` VALUES('21','不做任何处理','C','不做任何处理','交易失败，请联系发卡行')";
	public static final String TABLE_BR_17 = "INSERT INTO `ReturnCode` VALUES('22','怀疑操作有误','C','POS状态与中心不符，可重新签到','操作有误，请重试')";
	public static final String TABLE_BR_18 = "INSERT INTO `ReturnCode` VALUES('23','不可接受的交易费','C','不可接受的交易费','交易失败，请联系发卡行')";
	public static final String TABLE_BR_19 = "INSERT INTO `ReturnCode` VALUES('25','未能找到文件上记录','C','发卡行未能找到有关记录','交易失败，请联系发卡行')";
	public static final String TABLE_BR_20 = "INSERT INTO `ReturnCode` VALUES('30','格式错误','C','格式错误','交易失败，请重试')";
	public static final String TABLE_BR_21 = "INSERT INTO `ReturnCode` VALUES('31','银联不支持的银行','C','此发卡方未与中心开通业务','此卡不能受理')";
	public static final String TABLE_BR_22 = "INSERT INTO `ReturnCode` VALUES('33','过期的卡','D','过期的卡，操作员可以没收','过期卡，请联系发卡行')";
	public static final String TABLE_BR_23 = "INSERT INTO `ReturnCode` VALUES('34','有作弊嫌疑','D','有作弊嫌疑的卡，操作员可以没收','没收卡，请联系收单行')";
	public static final String TABLE_BR_24 = "INSERT INTO `ReturnCode` VALUES('35','受卡方与安全保密部门联系','D','有作弊嫌疑的卡，操作员可以没收','没收卡，请联系收单行')";
	public static final String TABLE_BR_25 = "INSERT INTO `ReturnCode` VALUES('36','受限制的卡','D','有作弊嫌疑的卡，操作员可以没收','此卡有误，请换卡重试')";
	public static final String TABLE_BR_26 = "INSERT INTO `ReturnCode` VALUES('37','受卡方呼受理方安全保密部门(没收卡)','D','有作弊嫌疑的卡，操作员可以没收','没收卡，请联系收单行')";
	public static final String TABLE_BR_27 = "INSERT INTO `ReturnCode` VALUES('38','超过允许的PIN试输入','D','密码错次数超限，操作员可以没收','密码错误次数超限')";
	public static final String TABLE_BR_28 = "INSERT INTO `ReturnCode` VALUES('39','无此信用卡账户','C','可能刷卡操作有误','交易失败，请联系发卡行')";
	public static final String TABLE_BR_29 = "INSERT INTO `ReturnCode` VALUES('40','请求的功能尚不支持','C','发卡行不支持的交易类型','交易失败，请联系发卡行')";
	public static final String TABLE_BR_30 = "INSERT INTO `ReturnCode` VALUES('41','丢失卡','D','挂失的卡，操作员可以没收','没收卡，请联系收单行')";
	public static final String TABLE_BR_31 = "INSERT INTO `ReturnCode` VALUES('42','无此账户','B','发卡行找不到此账户','交易失败，请联系发卡方')";
	public static final String TABLE_BR_32 = "INSERT INTO `ReturnCode` VALUES('43','被窃卡','D','被窃卡，操作员可以没收','没收卡，请联系收单行')";
	public static final String TABLE_BR_33 = "INSERT INTO `ReturnCode` VALUES('44','无此投资账户','C','可能刷卡操作有误','交易失败，请联系发卡行')";
	public static final String TABLE_BR_34 = "INSERT INTO `ReturnCode` VALUES('51','无足够的存款','C','账户内余额不足','余额不足，请查询')";
	public static final String TABLE_BR_35 = "INSERT INTO `ReturnCode` VALUES('52','无此支票账户','C','无此支票账户','交易失败，请联系发卡行')";
	public static final String TABLE_BR_36 = "INSERT INTO `ReturnCode` VALUES('53','无此储蓄卡账户','C','无此储蓄卡账户','交易失败，请联系发卡行')";
	public static final String TABLE_BR_37 = "INSERT INTO `ReturnCode` VALUES('54','过期的卡','C','过期的卡','过期卡，请联系发卡行')";
	public static final String TABLE_BR_38 = "INSERT INTO `ReturnCode` VALUES('55','不正确的PIN','C','密码输错','密码错，请重试')";
	public static final String TABLE_BR_39 = "INSERT INTO `ReturnCode` VALUES('56','无此卡记录','C','发卡行找不到此账户','交易失败，请联系发卡行')";
	public static final String TABLE_BR_40 = "INSERT INTO `ReturnCode` VALUES('57','不允许持卡人进行的交易','C','不允许持卡人进行的交易','交易失败，请联系发卡行')";
	public static final String TABLE_BR_41 = "INSERT INTO `ReturnCode` VALUES('58','不允许终端进行的交易','C','该商户不允许进行的交易','终端无效，请联系收单行或银联')";
	public static final String TABLE_BR_42 = "INSERT INTO `ReturnCode` VALUES('59','有作弊嫌疑','C',NULL,'交易失败，请联系发卡行')";
	public static final String TABLE_BR_43 = "INSERT INTO `ReturnCode` VALUES('60','受卡方与安全保密部门联系','C',NULL,'交易失败，请联系发卡行')";
	public static final String TABLE_BR_44 = "INSERT INTO `ReturnCode` VALUES('61','超出取款金额限制','C','一次交易的金额太大','金额太大')";
	public static final String TABLE_BR_45 = "INSERT INTO `ReturnCode` VALUES('62','受限制的卡','C',NULL,'交易失败，请联系发卡行')";
	public static final String TABLE_BR_46 = "INSERT INTO `ReturnCode` VALUES('63','违反安全保密规定','C','违反安全保密规定','交易失败，请联系发卡行')";
	public static final String TABLE_BR_47 = "INSERT INTO `ReturnCode` VALUES('64','原始金额不正确','C','原始金额不正确','交易失败，请联系发卡行')";
	public static final String TABLE_BR_48 = "INSERT INTO `ReturnCode` VALUES('65','超出取款次数限制','C','超出取款次数限制','超出取款次数限制')";
	public static final String TABLE_BR_49 = "INSERT INTO `ReturnCode` VALUES('66','受卡方呼受理方安全保密部门','C','受卡方呼受理方安全保密部门','交易失败，请联系收单行或银联')";
	public static final String TABLE_BR_50 = "INSERT INTO `ReturnCode` VALUES('67','捕捉（没收卡）','C','捕捉（没收卡）','没收卡')";
	public static final String TABLE_BR_51 = "INSERT INTO `ReturnCode` VALUES('68','收到的回答太迟','C','发卡行规定时间内没有回答','交易超时，请重试')";
	public static final String TABLE_BR_52 = "INSERT INTO `ReturnCode` VALUES('75','允许的输入PIN次数超限','C','允许的输入PIN次数超限','密码错误次数超限')";
	public static final String TABLE_BR_53 = "INSERT INTO `ReturnCode` VALUES('77','需要向网络中心签到','D','POS批次与网络中心不一致','请向网络中心签到')";
	public static final String TABLE_BR_54 = "INSERT INTO `ReturnCode` VALUES('79','脱机交易对账不平','C','POS终端上传的脱机数据对账不平','POS终端重传脱机数据')";
	public static final String TABLE_BR_55 = "INSERT INTO `ReturnCode` VALUES('90','日期切换正在处理','C','日期切换正在处理','交易失败，请稍后重试')";
	public static final String TABLE_BR_56 = "INSERT INTO `ReturnCode` VALUES('91','发卡行或银联不能操作','C','电话查询发卡方或银联，可重作','交易失败，请稍后重试')";
	public static final String TABLE_BR_57 = "INSERT INTO `ReturnCode` VALUES('92','金融机构或中间网络设施找不到或无法达到','C','电话查询发卡方或网络中心，可重作','交易失败，请稍后重试')";
	public static final String TABLE_BR_58 = "INSERT INTO `ReturnCode` VALUES('93','交易违法、不能完成','C','交易违法、不能完成','交易失败，请联系发卡行')";
	public static final String TABLE_BR_59 = "INSERT INTO `ReturnCode` VALUES('94','重复交易','C','查询网络中心，可重新签到作交易','交易失败，请稍后重试')";
	public static final String TABLE_BR_60 = "INSERT INTO `ReturnCode` VALUES('95','调节控制错','C','调节控制错','交易失败，请稍后重试')";
	public static final String TABLE_BR_61 = "INSERT INTO `ReturnCode` VALUES('96','系统异常、失效','C','发卡方或网络中心出现故障','交易失败，请稍后重试')";
	public static final String TABLE_BR_62 = "INSERT INTO `ReturnCode` VALUES('97','POS终端号找不到','D','终端未在中心或银行登记','终端未登记，请联系收单行或银联')";
	public static final String TABLE_BR_63 = "INSERT INTO `ReturnCode` VALUES('98','银联收不到发卡行应答','E','银联收不到发卡行应答','交易超时，请重试')";
	public static final String TABLE_BR_64 = "INSERT INTO `ReturnCode` VALUES('99','PIN格式错','B','可重新签到作交易','校验错，请重新签到')";
	public static final String TABLE_BR_65 = "INSERT INTO `ReturnCode` VALUES('A0','MAC校验错','B','可重新签到作交易','校验错，请重新签到')";
	public static final String TABLE_BR_66 = "INSERT INTO `ReturnCode` VALUES('OT','金额过小','B','金额过小','金额过小')";

	public static String[] tableList = { TABLE_BR_AA, TABLE_BR_00, TABLE_BR_01, TABLE_BR_02, TABLE_BR_03, TABLE_BR_04,
			TABLE_BR_05, TABLE_BR_06, TABLE_BR_07, TABLE_BR_08, TABLE_BR_09, TABLE_BR_10, TABLE_BR_11, TABLE_BR_12,
			TABLE_BR_13, TABLE_BR_14, TABLE_BR_15, TABLE_BR_16, TABLE_BR_17, TABLE_BR_18, TABLE_BR_19, TABLE_BR_20,
			TABLE_BR_21, TABLE_BR_22, TABLE_BR_23, TABLE_BR_24, TABLE_BR_25, TABLE_BR_26, TABLE_BR_27, TABLE_BR_28,
			TABLE_BR_29, TABLE_BR_30, TABLE_BR_31, TABLE_BR_32, TABLE_BR_33, TABLE_BR_34, TABLE_BR_35, TABLE_BR_36,
			TABLE_BR_37, TABLE_BR_38, TABLE_BR_39, TABLE_BR_40, TABLE_BR_41, TABLE_BR_42, TABLE_BR_43, TABLE_BR_44,
			TABLE_BR_45, TABLE_BR_46, TABLE_BR_47, TABLE_BR_48, TABLE_BR_49, TABLE_BR_50, TABLE_BR_51, TABLE_BR_52,
			TABLE_BR_53, TABLE_BR_54, TABLE_BR_55, TABLE_BR_56, TABLE_BR_57, TABLE_BR_58, TABLE_BR_59, TABLE_BR_60,
			TABLE_BR_61, TABLE_BR_62, TABLE_BR_63, TABLE_BR_64, TABLE_BR_65, TABLE_BR_66 };
}
