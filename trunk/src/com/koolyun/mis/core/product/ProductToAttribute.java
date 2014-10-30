package com.koolyun.mis.core.product;

public class ProductToAttribute {
	int productID;
	int productAttributeID;

	public ProductToAttribute(int productID2, int productAttributeID2) {
		this.productID = productID2;
		this.productAttributeID = productAttributeID2;
	}

	public int getProductCategoryID() {
		return productID;
	}

	public void setProductCategoryID(int productCategoryID) {
		this.productID = productCategoryID;
	}

	public int getProductAttributeID() {
		return productAttributeID;
	}

	public void setProductAttributeID(int productAttributeID) {
		this.productAttributeID = productAttributeID;
	}

}
