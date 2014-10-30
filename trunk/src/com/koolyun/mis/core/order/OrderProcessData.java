package com.koolyun.mis.core.order;

public class OrderProcessData {
	private OrderProcess mOrderProcess = null;
	private OrderCustomerInfo mOrderCustomerInfo = null;
	private BankCard mBankCard = null;

	public OrderProcess getmOrderProcess() {
		return mOrderProcess;
	}

	public void setmOrderProcess(OrderProcess mOrderProcess) {
		this.mOrderProcess = mOrderProcess;
	}

	public OrderCustomerInfo getmOrderCustomerInfo() {
		return mOrderCustomerInfo;
	}

	public void setmOrderCustomerInfo(OrderCustomerInfo mOrderCustomerInfo) {
		this.mOrderCustomerInfo = mOrderCustomerInfo;
	}

	public BankCard getmBankCard() {
		return mBankCard;
	}

	public void setmBankCard(BankCard mBankCard) {
		this.mBankCard = mBankCard;
	}

}
