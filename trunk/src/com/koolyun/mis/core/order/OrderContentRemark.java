package com.koolyun.mis.core.order;

public class OrderContentRemark {
	int orderContentID = -1;
	String remark = "";

	public int getOrderContentId() {
		return orderContentID;
	}

	public void setOrderContentId(int orderContentId) {
		this.orderContentID = orderContentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public OrderContentRemark() {

	}

	public OrderContentRemark(int orderContentId, String remark) {
		this.orderContentID = orderContentId;
		this.remark = remark;
	}

}
