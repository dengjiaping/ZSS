package com.koolyun.mis.core.order;

public class PaymentType {

	private int paymentTypeID;
	private String name;
	private int value;

	public void setPaymentTypeID(int paymentTypeID) {
		this.paymentTypeID = paymentTypeID;
	}

	public int getPaymentTypeID() {
		return paymentTypeID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
