package com.koolyun.mis.util;

import android.os.Parcel;
import android.os.Parcelable;

public class MoneyInputer00 implements Parcelable {
	String money;
	boolean isDecimals; // 小数标记
	int maxIntegerPartSize; // 最大整数位数

	boolean sign = true; // +:true -:false

	public MoneyInputer00() {
		money = "0";
		isDecimals = true;
		maxIntegerPartSize = 4;
	}

	public MoneyInputer00(Parcel source) {
		money = source.readString();
		isDecimals = source.readByte() == 1;
		maxIntegerPartSize = source.readInt();
	}

	private int getIntegerPartSize() {
		if (money.contains("."))
			return money.split("\\.")[0].length();

		return money.length();
	}

	private void moveDot(int n) // n > 0 右移1位
	{
		if (n == 0) {
			if (!money.contains("."))
				money += ".00";
		} else if (n > 0) {
			String IntPart = "0";
			String smallPart;
			if (money.contains(".")) {

				if (Integer.parseInt(money.split("\\.")[0]) != 0)
					IntPart = money.split("\\.")[0] + money.split("\\.")[1].charAt(0);
				else
					IntPart = "" + money.split("\\.")[1].charAt(0);
				smallPart = money.split("\\.")[1].substring(1, 3);
				money = IntPart + "." + smallPart;
			}
		} else {
			String IntPart;
			String smallPart;
			if (money.contains(".")) {
				if (getIntegerPartSize() == 1) {
					IntPart = "0";
				} else {
					IntPart = money.substring(0, getIntegerPartSize() - 1);
				}
				smallPart = "" + money.charAt(getIntegerPartSize() - 1) + money.split("\\.")[1];
				money = IntPart + "." + smallPart;
			}
		}
	}

	public String getMoney() {
		String mTmp = money;
		if (!sign)
			mTmp = "-" + mTmp;
		return NumberFormater.currencyFormat(Double.parseDouble(mTmp));
	}

	public String getNoSignMoney() {
		return NumberFormater.currencyFormat(Double.parseDouble(money));
	}

	public void setMoney(String money) {
		if (money.contains("-")) {
			this.setSign(false);
			this.money = money.substring(1);
		} else {
			this.setSign(true);
			this.money = money;
		}

	}

	public void setMaxIntegerPartSize(int maxIntegerPartSize) {
		this.maxIntegerPartSize = maxIntegerPartSize;
	}

	public void modifyMoney(char c) {
		if (c == '-')
			sign = !sign;
		if (money.isEmpty() || money == "0") {
			money = "0.00";
		}
		if (Character.isDigit(c)) {
			int isize = getIntegerPartSize();
			if (isDecimals && isize < maxIntegerPartSize) {
				if (money.contains(".")) {
					money += c;
					moveDot(1);
				}
			}
		}
	}

	public void deleteMoney() {
		/*
		 * if (money.contains(".")) { if (money.split("\\.")[1].length() == 2) {
		 * money = money.substring(0, money.length() - 1); } else if
		 * (money.split("\\.")[1].length() == 1) { money = money.substring(0,
		 * money.length() - 2); isDecimals = false; } } else { if
		 * (money.length() == 1) { money = "0"; } else { money =
		 * money.substring(0, money.length() - 1); } }
		 */
		if (money.length() == 1) {
			money = "0";
		} else {
			money = money.substring(0, money.length() - 1);
		}
		moveDot(-1);
	}

	public void clear() {
		money = "0";
		isDecimals = true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(money);
		dest.writeByte((byte) (isDecimals ? 1 : 0));
		dest.writeInt(maxIntegerPartSize);
	}

	public static final Parcelable.Creator<MoneyInputer00> CREATOR = new Creator<MoneyInputer00>() {

		@Override
		public MoneyInputer00[] newArray(int size) {
			return new MoneyInputer00[size];
		}

		@Override
		public MoneyInputer00 createFromParcel(Parcel source) {
			return new MoneyInputer00(source);
		}
	};

	public boolean getSign() {
		return sign;
	}

	public void setSign(boolean sign) {
		this.sign = sign;
	}

}
