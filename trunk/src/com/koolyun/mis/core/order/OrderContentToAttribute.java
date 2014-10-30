package com.koolyun.mis.core.order;

import com.google.gson.annotations.Expose;

public class OrderContentToAttribute {

	@Expose
	private int orderContentID;
	@Expose
	private int productSubAttributeID;
	@Expose
	private String price;

	@Expose
	private int productAttributeID;

	public int getProdcutAttributeId() {
		return productAttributeID;
	}

	public void setProdcutAttributeId(int prodcutAttributeId) {
		this.productAttributeID = prodcutAttributeId;
	}

	public int getOrderContentID() {
		return orderContentID;
	}

	public void setOrderContentID(int orderContentID) {
		this.orderContentID = orderContentID;
	}

	public int getProductSubAttributeId() {
		return productSubAttributeID;
	}

	public void setProductSubAttributeId(int productSubAttributeId) {
		this.productSubAttributeID = productSubAttributeId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
