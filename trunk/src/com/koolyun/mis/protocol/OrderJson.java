package com.koolyun.mis.protocol;

import com.google.gson.annotations.Expose;

public class OrderJson {
	// Orders
	@Expose
	private String billNo;
	@Expose
	private int orderStatusID;
	@Expose
	private int OrderCreateTime;
	@Expose
	private int modifyTime;
	@Expose(serialize = false, deserialize = false)
	private int orderId;
	// OrdersToPaymentType
	@Expose
	private int paymentTypeID;
	@Expose
	private String amount;
	// OrderProcess
	@Expose
	private int OrderProcessModeId;
	@Expose(serialize = false, deserialize = false)
	private int addressID;
	@Expose
	private String processCreateTime;
	@Expose
	private String traceNo;
	@Expose
	private String referenceNo;
	@Expose
	private String authorizationNo;
	@Expose
	private String clearingDate;
	@Expose
	private String ackNo;
	@Expose
	private String processPrice;
	@Expose
	private String issuerbankid;
	@Expose
	private String acqbank;
	@Expose
	private String batchid;
	@Expose(serialize = false, deserialize = false)
	private int orderProcessId;
	// OrderCustomerInfo
	@Expose
	private String signature;
	@Expose
	private String telephoneNo;
	@Expose
	private String emailInfo;
	@Expose(serialize = false, deserialize = false)
	private int bankCardId;
	// OrderContent
	@Expose
	private int productID;
	@Expose
	private int count;
	@Expose
	private String contentPrice;
	@Expose(serialize = false, deserialize = false)
	private int orderContentID;
	// OrderContentToAttribute
	@Expose
	private int productSubAttributeId;
	@Expose
	private String attributePrice;
	// OrderContentToOnsale
	@Expose
	private int onsaleID;
	@Expose
	private String value;
	// Address
	@Expose
	private String addressInfo;
	@Expose
	private int longitude;
	@Expose
	private int latitude;
	// BankCard
	@Expose
	private String bankCardNo;
	@Expose
	private String validThrough;
	@Expose
	private String cardOwnerName;
}
