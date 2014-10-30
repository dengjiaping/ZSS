package com.koolyun.mis.widget;

import java.util.HashMap;

import com.koolyun.mis.util.MoneyInputer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MoneyView extends TextView {

	public enum CashType {
		RMB, // ¥
		DOLLAR// $
	}

	@SuppressWarnings("serial")
	HashMap<CashType, String> map = new HashMap<CashType, String>() {
		{
			put(CashType.RMB, "¥");
			put(CashType.DOLLAR, "$");
		}
	};

	private String preString = "";
	private CashType mCashType = CashType.RMB;
	private MoneyInputer mInputer = new MoneyInputer();

	public void setCashType(CashType mCashType)

	{
		this.mCashType = mCashType;
	}

	public void setPreString(String mps) {
		preString = mps;
	}

	public CashType getCashType() {
		return this.mCashType;
	}

	public MoneyView(Context context) {
		super(context);

	}

	public MoneyView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public String getMoney() {
		return mInputer.getMoney();
	}

	public String getNoSignMoney() {
		return mInputer.getNoSignMoney();
	}

	public void setMoney(String money) {
		mInputer.setMoney(money);
		update();
	}

	public void setMaxIntegerPartSize(int maxIntegerPartSize) {
		mInputer.setMaxIntegerPartSize(maxIntegerPartSize);
	}

	public void modifyMoney(char c) {
		mInputer.modifyMoney(c);
		update();
	}

	public void deleteMoney() {
		mInputer.deleteMoney();
		update();
	}

	public void clear() {
		mInputer.clear();
		mInputer.setSign(true);
		update();
	}

	private void update() {
		if (getMoney().isEmpty()) {
			this.setText("0.00");
		} else {
			String tmp = mInputer.getSign() ? "" : "-";
			this.setText(preString + tmp + map.get(mCashType) + getNoSignMoney());
		}
	}

}
