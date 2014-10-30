package com.koolyun.mis.core.order;

public class OrderRemark {
	int orderID = -1;
	String remark = null;
	String sitIndex = null;

	@Override
	protected OrderRemark clone() throws CloneNotSupportedException {
		OrderRemark mOrderRemark = new OrderRemark();
		mOrderRemark.orderID = this.orderID;
		mOrderRemark.remark = this.remark;
		mOrderRemark.sitIndex = this.sitIndex;
		return mOrderRemark;

	}

	public String getSitIndex() {
		return sitIndex;
	}

	public void setSitIndex(String sitIndex) {
		this.sitIndex = sitIndex;
	}

	public int getOrderId() {
		return orderID;
	}

	public void setOrderId(int orderId) {
		this.orderID = orderId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public OrderRemark() {

	}

	public OrderRemark(int orderId, String remark, String sitIndex) {
		this.orderID = orderId;
		this.remark = remark;
		this.sitIndex = sitIndex;
	}

}
