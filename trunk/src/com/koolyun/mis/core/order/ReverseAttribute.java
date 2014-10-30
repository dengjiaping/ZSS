package com.koolyun.mis.core.order;

public class ReverseAttribute {

	int orderProcessId;
	String reason;

	public ReverseAttribute(int orderProcessId, String reason) {
		this.orderProcessId = orderProcessId;
		this.reason = reason;
	}

	public int getOrderProcessId() {
		return orderProcessId;
	}

	public void setOrderProcessId(int orderProcessId) {
		this.orderProcessId = orderProcessId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
