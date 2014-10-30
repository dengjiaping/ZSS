package com.koolyun.mis.core.order;

public class LiteOrderInfo {

	Orders order;
	String amount;
	String description;
	String orderRemark;

	public LiteOrderInfo(Orders order, String amount, String description, String orderRemark) {
		this.order = order;
		this.amount = amount;
		this.description = description;
		this.orderRemark = orderRemark;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
