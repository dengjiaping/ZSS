package com.koolyun.mis.util;

import android.os.Parcel;
import android.os.Parcelable;

public class MoneyInputer implements Parcelable {
	String money = "0.00";
	boolean isDecimals = false; // 小数标记
	int maxIntegerPartSize; // 最大整数位数

	boolean sign = true; // +:true -:false

	public MoneyInputer() {
		money = "0";
		isDecimals = false;
		maxIntegerPartSize = 6;
		sign = true;
	}

	public MoneyInputer(Parcel source) {
		money = source.readString();
		isDecimals = source.readByte() == 1;
		maxIntegerPartSize = source.readInt();
	}

	private int getIntegerPartSize() {
		if (money.contains("."))
			return money.split("\\.")[0].length();

		return money.length();
	}

	public String getMoney() {
		String mTmp = money;
		if (!sign)
			mTmp = "-" + mTmp;
		return NumberFormater.currencyFormat(Double.parseDouble(mTmp));
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

	public String getNoSignMoney() {
		return NumberFormater.currencyFormat(Double.parseDouble(money));
	}

	public void setMaxIntegerPartSize(int maxIntegerPartSize) {
		this.maxIntegerPartSize = maxIntegerPartSize;
	}

	public void modifyMoney(char c) {
		if (c == '-')
			sign = !sign;
		if (Character.isDigit(c)) {
			if (this.isDecimals) {
				if (money.contains(".")) {
					if (money.split("\\.")[1].length() == 1)
						money += c;
				} else {
					money += "." + c;
				}
			} else {
				if (money.equals("0")) {
					money = String.valueOf(c);
				} else {
					if (getIntegerPartSize() < maxIntegerPartSize) {
						money += c;
					}
				}
			}
		} else if (c == '.') {
			this.isDecimals = true;
		}
	}

	public void deleteMoney() {
		if (money.contains(".")) {
			if (money.split("\\.")[1].length() == 2) {
				money = money.substring(0, money.length() - 1);
			} else if (money.split("\\.")[1].length() == 1) {
				money = money.substring(0, money.length() - 2);
				isDecimals = false;
			}
		} else {
			if (money.length() == 1) {
				money = "0";
			} else {
				money = money.substring(0, money.length() - 1);
			}
		}
	}

	public void clear() {
		money = "0";
		isDecimals = false;
		sign = true;
	}

	public boolean getSign() {
		return sign;
	}

	public void setSign(boolean sign) {
		this.sign = sign;
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

	public static final Parcelable.Creator<MoneyInputer> CREATOR = new Creator<MoneyInputer>() {

		@Override
		public MoneyInputer[] newArray(int size) {
			return new MoneyInputer[size];
		}

		@Override
		public MoneyInputer createFromParcel(Parcel source) {
			return new MoneyInputer(source);
		}
	};
}
