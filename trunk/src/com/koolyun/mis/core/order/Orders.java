package com.koolyun.mis.core.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Orders {

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
	private int orderStatusID;

	private String txnId;
	
	private int syncFlag = 0;

	public Orders(int orderID, String billNo, long createTime, long modifyTime, int OrderStatusID,String txnId) {
		this.orderID = orderID;
		this.billNo = billNo;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.orderStatusID = OrderStatusID;
		this.txnId =  txnId;
	}

	public Orders(int orderID, String billNo, long createTime, long modifyTime, int OrderStatusID, int syncflag,String txnId) {
		this.orderID = orderID;
		this.billNo = billNo;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.orderStatusID = OrderStatusID;
		this.syncFlag = syncflag;
		this.txnId = txnId;
	}

	@Override
	public Orders clone() throws CloneNotSupportedException {
		Orders mOrder = new Orders();
		mOrder.orderID = this.orderID;
		mOrder.billNo = this.billNo;
		mOrder.createTime = this.createTime;
		mOrder.modifyTime = this.modifyTime;
		mOrder.orderStatusID = this.orderStatusID;
		mOrder.syncFlag = this.syncFlag;
		mOrder.txnId = this.txnId;
		return mOrder;
	}

	public int getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
	}

	public Orders() {

	}

	public void reInit() {
		createTime = 0;
		modifyTime = 0;
		orderStatusID = -1;
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
		return orderStatusID;
	}

	public void setOrderStatusID(int orderStatusID) {
		this.orderStatusID = orderStatusID;
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

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	
}
