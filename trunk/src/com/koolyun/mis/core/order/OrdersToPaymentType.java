package com.koolyun.mis.core.order;

import com.google.gson.annotations.Expose;

public class OrdersToPaymentType {

	@Expose
	private int orderID;
	@Expose
	private int paymentTypeID;
	@Expose
	private String amount;

	public OrdersToPaymentType(int orderID, int paymentTypeID, String amount) {
		this.orderID = orderID;
		this.paymentTypeID = paymentTypeID;
		this.amount = amount;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getPaymentTypeID() {
		return paymentTypeID;
	}

	public void setPaymentTypeID(int paymentTypeID) {
		this.paymentTypeID = paymentTypeID;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
