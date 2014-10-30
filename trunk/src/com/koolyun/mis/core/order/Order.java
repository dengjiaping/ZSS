package com.koolyun.mis.core.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {
	@SerializedName("cid")
	@Expose
	private int orderID = -1;
	@Expose
	private String billNo = null;
	@Expose
	private long createTime = 0;
	@Expose
	private long modifyTime = 0;
	@Expose
	private int OrderStatusID;
	@Expose
	private int syncFlag = 0;

	public Order(int orderID, String billNo, long createTime, long modifyTime, int OrderStatusID) {
		this.orderID = orderID;
		this.billNo = billNo;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.OrderStatusID = OrderStatusID;
	}

	public Order(int orderID, String billNo, long createTime, long modifyTime, int OrderStatusID, int syncflag) {
		this.orderID = orderID;
		this.billNo = billNo;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.OrderStatusID = OrderStatusID;
		this.syncFlag = syncflag;
	}

	public int getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
	}

	public Order() {

	}

	public void reInit() {
		createTime = 0;
		modifyTime = 0;
		OrderStatusID = -1;
		billNo = null;
		orderID = -1;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public int getOrderID() {
		return orderID;
	}

	public int getOrderStatusID() {
		return OrderStatusID;
	}

	public void setOrderStatusID(int orderStatusID) {
		OrderStatusID = orderStatusID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

}
