package com.koolyun.mis.core.product;

public class SaleProductInfo {

	private int productID = -1;
	private String productName = "";
	private int count = 0;
	private String totalAmount = "0.00";

	public SaleProductInfo(int productID, String productName, int count, String totalAmount) {
		this.productID = productID;
		this.productName = productName;
		this.count = count;
		this.totalAmount = totalAmount;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTotalCount() {
		return totalAmount;
	}

	public void setTotalCount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
