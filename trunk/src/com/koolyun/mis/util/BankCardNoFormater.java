package com.koolyun.mis.util;

public class BankCardNoFormater {
	public static String formate(String cardNo) {

		StringBuffer sb = new StringBuffer(cardNo);

		if (sb.length() > 8) {
			sb.replace(sb.length() - 8, sb.length() - 4, "****");
		}

		if (sb.length() == 16) {
			sb.insert(4, ' ');
			sb.insert(9, ' ');
			sb.insert(14, ' ');
			return sb.toString();
		} else if (sb.length() == 19) {
			sb.insert(6, ' ');
			return sb.toString();
		}

		return cardNo;
	}
}
