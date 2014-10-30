package com.koolyun.mis.core.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderContentToOnsale {

	@SerializedName("cid")
	@Expose
	private int orderContentID;
	@Expose
	private int onsaleID;
	@Expose
	private String value;

	public int getOrderContentID() {
		return orderContentID;
	}

	public void setOrderContentID(int orderContentID) {
		this.orderContentID = orderContentID;
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
