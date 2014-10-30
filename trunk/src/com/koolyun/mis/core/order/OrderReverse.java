package com.koolyun.mis.core.order;

public class OrderReverse {

	private int orderProcessId;
	private String reasonId;

	public int getOrderProcessId() {
		return orderProcessId;
	}

	public void setOrderProcessId(int orderProcessId) {
		this.orderProcessId = orderProcessId;
	}

	public String getOrderReverseReason() {
		return reasonId;
	}

	public void setOrderReverseReason(String orderReverseReason) {
		reasonId = orderReverseReason;
	}
}
