package com.koolyun.mis.core.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderContent {

	private int orderId;
	@SerializedName("cid")
	@Expose
	private int orderContentId = -1;
	@Expose
	private int productId;
	@Expose
	private int count = 1;
	@Expose
	private String amount;// 总价
	@Expose
	private int productCategoryID;

	public int getProductCategoryID() {
		return productCategoryID;
	}

	public void setProductCategoryID(int productCategoryID) {
		this.productCategoryID = productCategoryID;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getOrderContentId() {
		return orderContentId;
	}

	public void setOrderContentId(int orderContentId) {
		this.orderContentId = orderContentId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
