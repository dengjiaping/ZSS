package com.koolyun.mis.core.order;

public class OrderToAccount {

	private int orderID = -1;
	private String accountName = "";

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public OrderToAccount(int orderID, String accountName) {
		this.orderID = orderID;
		this.accountName = accountName;
	}

	@Override
	protected OrderToAccount clone() throws CloneNotSupportedException {
		OrderToAccount mOrderToAccount = new OrderToAccount();
		mOrderToAccount.orderID = this.orderID;
		mOrderToAccount.accountName = this.accountName;
		return mOrderToAccount;
	}

	public OrderToAccount() {

	}

}
