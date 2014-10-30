package com.koolyun.mis.finance;

public class PosData {
	private String ackNo = null;
	private String referencNo = null;
	private String authorizationNo = null;
	private String traceNo = null;
	private String clearingDate = null;
	private String amount = null;
	private String date = null;
	private String time = null;
	private String issuer = null;
	private String acq = null;
	private String batchid = null;
	private String messagetype = null;
	private String accountNo = null;
	private String merchantCode = null;
	private String terminalCode = null;
	private String additionalData = null;
	private String autherName = null;

	static PosData mPosdata = new PosData();

	public static PosData getPosDataInstance() {
		return mPosdata;
	}

	private PosData() {

	}

	public String getAutherName() {
		return autherName;
	}

	public void setAutherName(String autherName) {
		this.autherName = autherName;
	}

	public String getAckNo() {
		return ackNo;
	}

	public String getReferencNo() {
		return referencNo;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getMessageType() {
		return messagetype;
	}

	public void setMessageType(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getAuthorizationNo() {
		return authorizationNo;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public String getClearingDate() {
		return clearingDate;
	}

	public String getAmount() {
		return amount;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getAcq() {
		return acq;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setAckNo(String ackNo) {
		this.ackNo = ackNo;
	}

	public void setReferencNo(String referencNo) {
		this.referencNo = referencNo;
	}

	public void setAuthorizationNo(String authorizationNo) {
		this.authorizationNo = authorizationNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public void setClearingDate(String clearingDate) {
		this.clearingDate = clearingDate;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public void setAcq(String acq) {
		this.acq = acq;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	// public void InitWithHostLink()
	// {
	// if (HostlinkInterface.getAckNo() != null) {
	// this.setAckNo(new String(HostlinkInterface.getAckNo()));
	// }
	//
	// if (HostlinkInterface.getRefenceNo() != null) {
	// this.setReferencNo(new String(HostlinkInterface.getRefenceNo()));
	// }
	//
	// if (HostlinkInterface.getAuthorizationNo() != null) {
	// this.setAuthorizationNo(new String(HostlinkInterface
	// .getAuthorizationNo()));
	// }
	//
	// if (HostlinkInterface.getTraceNo() != null) {
	// this.setTraceNo(new String(HostlinkInterface.getTraceNo()));
	// }
	//
	// if (HostlinkInterface.getSettlementDate() != null) {
	// this.setClearingDate(new String(HostlinkInterface
	// .getSettlementDate()));
	// }
	//
	// if (HostlinkInterface.getAmount() != null) {
	// this.setAmount(new String(HostlinkInterface.getAmount()));
	// }
	//
	// if (HostlinkInterface.getDate() != null) {
	// this.setDate(new String(HostlinkInterface.getDate()));
	// }
	//
	// if (HostlinkInterface.getTime() != null) {
	// this.setTime(new String(HostlinkInterface.getTime()));
	// }
	//
	// if (HostlinkInterface.getMessageType() != null) {
	// this.setMessageType(new String(HostlinkInterface.getMessageType()));
	// }
	//
	// if (HostlinkInterface.getAccountNo() != null) {
	// this.setAccountNo(new String(HostlinkInterface.getAccountNo()));
	// }
	//
	// if (HostlinkInterface.getMerchantCode() != null) {
	// this.setMerchantCode(new String(HostlinkInterface.getMerchantCode()));
	// }
	//
	// if (HostlinkInterface.getTerminalCode() != null) {
	// this.setTerminalCode(new String(HostlinkInterface.getTerminalCode()));
	// }
	//
	// if (HostlinkInterface.getAdditionalData() != null) {
	// this.setAdditionalData(new
	// String(HostlinkInterface.getAdditionalData()));
	// }
	//
	// byte[] num = HostlinkInterface.getAdditionalData();
	// if (num != null) {
	// byte[] issuer = new byte[11];
	// System.arraycopy(num, 0, issuer, 0, issuer.length);
	// if (issuer != null)
	// this.setIssuer(new String(issuer).trim());
	//
	// num = HostlinkInterface.getAdditionalData();
	// System.arraycopy(num, 11, issuer, 0, issuer.length);
	// if (issuer != null)
	// this.setAcq(new String(issuer).trim());
	// }
	//
	// if (HostlinkInterface.getBatchNo() != null) {
	// this.setBatchid(new String(HostlinkInterface.getBatchNo()));
	// }
	// }

	public void ClearData() {
		ackNo = null;
		referencNo = null;
		authorizationNo = null;
		traceNo = null;
		clearingDate = null;
		amount = null;
		date = null;
		time = null;
		issuer = null;
		acq = null;
		batchid = null;
		autherName = null;
	}

}
