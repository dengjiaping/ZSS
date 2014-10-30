package com.koolyun.mis.core.product;

public class ProductPinyin {
	int productId;
	int productCategoryID;
	String productName;
	String firstChar;
	String firstCharStr;
	String allStr;

	public ProductPinyin(int productId, int productCategoryID, String productName, String firstChar,
			String firstCharStr, String allStr) {
		this.productId = productId;
		this.productCategoryID = productCategoryID;
		this.productName = productName;
		this.firstChar = firstChar;
		this.firstCharStr = firstCharStr;
		this.allStr = allStr;
	}

	public int getProductCategoryID() {
		return productCategoryID;
	}

	public void setProductCategoryID(int productCategoryID) {
		this.productCategoryID = productCategoryID;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getFirstChar() {
		return firstChar;
	}

	public void setFirstChar(String firstChar) {
		this.firstChar = firstChar;
	}

	public String getFirstCharStr() {
		return firstCharStr;
	}

	public void setFirstCharStr(String firstCharStr) {
		this.firstCharStr = firstCharStr;
	}

	public String getAllStr() {
		return allStr;
	}

	public void setAllStr(String allStr) {
		this.allStr = allStr;
	}

}
