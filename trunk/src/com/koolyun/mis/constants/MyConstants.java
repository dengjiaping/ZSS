package com.koolyun.mis.constants;

public class MyConstants {
	/**
	 * 是否需要激活 0：需要激活 1：不需要激活
	 */
	public static int SHOULD_REGISTER = 1;
	/**
	 * 是否需要进行设备自检 0：需要自检 1：不要进行自检
	 */
	public static int SHOULD_CHECK_SELF = 1;
	/**
	 * 0：使用自有SDK进行支付或退款 1：使用通联的SDK进行支付或退款
	 */
	public static int PAY_TYPE = 1;
	
	public static final String test_HOST = "https://mis.koolyun.cn/koolmisapi"; // app test
	public static final String prod_HOST = "https://mis.koolyun.com/koolmisapi"; // product
	public static final String API_HOST = test_HOST;
}
