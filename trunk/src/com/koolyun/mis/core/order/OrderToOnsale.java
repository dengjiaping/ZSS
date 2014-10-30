package com.koolyun.mis.core.order;

public class OrderToOnsale {
	private int orderID;
	private int onsaleID;
	private String value;

	public OrderToOnsale(int orderID, int onsaleID, String value) {
		this.orderID = orderID;
		this.onsaleID = onsaleID;
		this.value = value;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getOnsaleID() {
		return onsaleID;
	}

	public void setOnsaleID(int onsaleID) {
		this.onsaleID = onsaleID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
