package com.koolyun.mis.util.pay;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.NumberFormater;

public class PayManager {
	public enum PayMode {
		PAY_ALL, PAY_PART
	};

	private PayMode mMode = PayMode.PAY_ALL;
	private String moneyToPay = "0.00";
	private String moneyHasGot = "0.00";
	private List<PayBase> mPayList = new LinkedList<PayBase>();
	private static PayManager instance = null;

	public static synchronized PayManager getInstance() {
		if (instance == null) {
			instance = new PayManager();
		}
		return instance;
	}

	public void ReInit() {
		Iterator<PayBase> iterator = mPayList.iterator();
		while (iterator.hasNext()) {
			PayBase pb = iterator.next();
			pb.payCancel();
			iterator.remove();
		}
	}

	public boolean addNewPayment(PayBase mPay) {
		switch (mMode) {
		case PAY_ALL:
			if (BasicArithmetic.compare(mPay.getPayMoney(), getNotPayed()) >= 0) {
				moneyHasGot = mPay.getPayMoney();
				mPay.setPayMoney(getNotPayed());
				mPayList.add(mPay);
				checkIsPayEnough();
				return true;
			} else
				return false;
		case PAY_PART:
			mPayList.add(mPay);
			checkIsPayEnough();
			return true;
		default:
			return false;
		}
	}

	private void checkIsPayEnough() {
		if (BasicArithmetic.compare(getAlreadyPayed(), getMoneyToPay()) >= 0) {
			PayedEnough();
		}
	}

	public PayMode getMode() {
		return mMode;
	}

	public void setMode(PayMode mMode) {
		this.mMode = mMode;
	}

	public String getMoneyToPay() {
		return NumberFormater.currencyFormat(Double.parseDouble(moneyToPay));
	}

	public void setMoneyToPay(String moneyToPay) {
		this.moneyToPay = moneyToPay;
		// if(BasicArithmetic.compare(moneyToPay,"0.00") == 0)
		// {
		// PayedEnough();
		// }
	}

	public List<PayBase> getPayList() {
		return mPayList;
	}

	public String getNotPayed() {
		return NumberFormater
				.currencyFormat(Double.parseDouble(BasicArithmetic.sub(getMoneyToPay(), getAlreadyPayed())));
	}

	public String getAlreadyPayed() {
		String already = "0.00";
		Iterator<PayBase> iterator = mPayList.iterator();
		while (iterator.hasNext()) {
			PayBase pb = iterator.next();
			already = BasicArithmetic.add(already, pb.getPayMoney());
		}
		return already;
	}

	public void PayCancel() {
		Iterator<PayBase> iterator = mPayList.iterator();
		while (iterator.hasNext()) {
			PayBase pb = iterator.next();
			pb.payCancel();
			iterator.remove();
		}
	}

	private void PayedEnough() {
		if (BasicArithmetic.compare(getAlreadyPayed(), getMoneyToPay()) > 0)
			DealChange();
		DealModel.getInstance().OrderHasbeenPayed();
		Log.d("=============================>", "PayedEnough()PayedEnough()PayedEnough()PayedEnough()");
	}

	public String getChange() {
		return NumberFormater.currencyFormat(Double.parseDouble(BasicArithmetic.sub(moneyHasGot, moneyToPay)));
	}

	public String getMoneyGot() {
		return NumberFormater.currencyFormat(Double.parseDouble(moneyHasGot));
	}

	// TODO: may FIX later
	public int getPayType() {
		if (mPayList != null && mPayList.size() > 0) {
			return (mPayList.get(0).getmType().toInt());
		}
		return -1;
	}

	private void DealChange() {

	}

}
