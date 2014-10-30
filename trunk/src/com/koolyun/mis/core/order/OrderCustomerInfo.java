package com.koolyun.mis.core.order;

public class OrderCustomerInfo {

	private int orderProcessId;
	private int bankCardId;
	private String signature;
	private String telephoneNo;
	private String emailInfo;

	public OrderCustomerInfo(int orderProcessId, int bankCardId, String emailInfo, String signature, String telephoneNo) {
		// TODO Auto-generated constructor stub
		this.orderProcessId = orderProcessId;
		this.bankCardId = bankCardId;
		this.emailInfo = emailInfo;
		this.signature = signature;
		this.telephoneNo = telephoneNo;
	}

	public OrderCustomerInfo() {
		// TODO Auto-generated constructor stub
	}

	public int getOrderProcessId() {
		return orderProcessId;
	}

	public void setOrderProcessId(int orderProcessId) {
		this.orderProcessId = orderProcessId;
	}

	public int getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(int bankCardId) {
		this.bankCardId = bankCardId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getEmailInfo() {
		return emailInfo;
	}

	public void setEmailInfo(String emailInfo) {
		this.emailInfo = emailInfo;
	}
}
