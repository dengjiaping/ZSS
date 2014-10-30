package com.koolyun.mis.util;

import android.annotation.SuppressLint;
import java.text.NumberFormat;
import java.util.Locale;

/*
 * 
 * 
 */
public class NumberFormater {

	// 将数字以货币形式显示
	public static String currencyFormat(double d) {
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CHINA);
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);

		return numberFormat.format(d).replace(",", "");
	}

	// 将数字以货币形式显示
	public static String currencyFormat(String s) {
		if (s == null || s.isEmpty())
			s = "0.00";
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CHINA);
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);

		return numberFormat.format(Double.valueOf(s)).replace(",", "");
	}

	@SuppressLint("DefaultLocale")
	public static String twelveNumber(String number) {
		return String.format("%012d", (long) Double.parseDouble((BasicArithmetic.multi(number, "100"))));
	}

	public static String MoneyFromTwelveNumber(String money) {
		return NumberFormater.currencyFormat((BasicArithmetic.div(money, "100")));
	}

}
