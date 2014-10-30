package com.koolyun.mis.core.order;

public class OrderProcess {

	private int orderProcessId;
	private int OrderProcessModeId;
	private int orderID;
	private int addressID;
	private String createTime = null;
	private String traceNo = null;
	private String referenceNo = null;
	private String authorizationNo = null;
	private String clearingDate = null;
	private String ackNo = null;
	private String price = null;
	private String issuerbankId = null;
	private String acqbankId = null;
	private String batchId = null;

	public OrderProcess() {

	}

	public OrderProcess(int orderProcessId, int orderProcessModeId, int orderID, int addressID, String createTime,
			String traceNo, String referenceNo, String authorizationNo, String clearingDate, String ackNo,
			String price, String issuerbankid, String acqbankid, String batchid) {

		this.orderProcessId = orderProcessId;
		OrderProcessModeId = orderProcessModeId;
		this.orderID = orderID;
		this.addressID = addressID;
		this.createTime = createTime;
		this.traceNo = traceNo;
		this.referenceNo = referenceNo;
		this.authorizationNo = authorizationNo;
		this.clearingDate = clearingDate;
		this.ackNo = ackNo;
		this.price = price;
		this.issuerbankId = issuerbankid;
		this.acqbankId = acqbankid;
		this.batchId = batchid;
	}

	public int getOrderProcessId() {
		return orderProcessId;
	}

	public void setOrderProcessId(int orderProcessId) {
		this.orderProcessId = orderProcessId;
	}

	public int getOrderProcessModeId() {
		return OrderProcessModeId;
	}

	public void setOrderProcessModeId(int orderProcessModeId) {
		OrderProcessModeId = orderProcessModeId;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getAuthorizationNo() {
		return authorizationNo;
	}

	public void setAuthorizationNo(String authorizationNo) {
		this.authorizationNo = authorizationNo;
	}

	public String getClearingDate() {
		return clearingDate;
	}

	public void setClearingDate(String clearingDate) {
		this.clearingDate = clearingDate;
	}

	public String getAckNo() {
		return ackNo;
	}

	public void setAckNo(String ackNo) {
		this.ackNo = ackNo;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setIssuerbankId(String id) {
		this.issuerbankId = id;
	}

	public String getIssuerbankId() {
		return issuerbankId;
	}

	public void setAcqbankId(String id) {
		this.acqbankId = id;
	}

	public String getAcqbankId() {
		return acqbankId;
	}

	public void setBatchId(String id) {
		this.batchId = id;
	}

	public String getBatchId() {
		return batchId;
	}
}
