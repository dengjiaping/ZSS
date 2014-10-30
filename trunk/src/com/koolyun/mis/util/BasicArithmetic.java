package com.koolyun.mis.util;

import java.math.BigDecimal;

public class BasicArithmetic {
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	private BasicArithmetic() {
	}

	public static int compare(String v1, String v2) {
		double dv1 = Double.parseDouble(v1);
		double dv2 = Double.parseDouble(v2);
		if (dv1 == dv2)
			return 0;
		else if (dv1 > dv2) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * 浮点数的加法
	 * 
	 * @param v1
	 * @param v2
	 * @return 两数之和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 浮点数的加法
	 * 
	 * @param v1
	 * @param v2
	 * @return 两数之和
	 * @see com.koolyun.mis.finance#add
	 */
	public static String add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).toString();
	}

	/**
	 * 实现浮点数的减法运算功能
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return v1-v2的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	public static String sub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).toString();
	}

	/**
	 * 实现浮点数的乘法运算功能
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return v1×v2的积
	 */
	public static double multi(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	public static String multi(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).toString();
	}

	/**
	 * 实现浮点数的除法运算功能 当发生除不尽的情况时，精确到小数点以后DEF_DIV_SCALE位(默认为10位)，后面的位数进行四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return v1/v2的商
	 */
	public static double div(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static String div(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 实现浮点数的除法运算功能 当发生除不尽的情况时，精确到小数点以后scale位，后面的位数进行四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示需要精确到小数点以后几位
	 * @return v1/v2的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入功能
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}