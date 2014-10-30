package com.koolyun.mis.core.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSubAttribute {

	@SerializedName("cid")
	@Expose
	private int productSubAttributeId;
	@Expose
	private int productAttributeID;
	@Expose
	private String name;
	@Expose
	private String priceAffect;
	@Expose
	private int enable = 1;

	private int syncFlag = 0;

	public ProductSubAttribute(int productSubAttributeId, int productAttributeID, String name, String priceAffect,
			int enable) {

		this.productSubAttributeId = productSubAttributeId;
		this.productAttributeID = productAttributeID;
		this.name = name;
		this.priceAffect = priceAffect;
		this.enable = enable;
	}

	@Override
	public boolean equals(Object o) {
		ProductSubAttribute tmp = (ProductSubAttribute) o;
		return (this.productSubAttributeId == tmp.getProductAttributeID());
	}

	public int getProductSubAttributeId() {
		return productSubAttributeId;
	}

	public void setProductSubAttributeId(int productSubAttributeId) {
		this.productSubAttributeId = productSubAttributeId;
	}

	public int getProductAttributeID() {
		return productAttributeID;
	}

	public void setProductAttributeID(int productAttributeID) {
		this.productAttributeID = productAttributeID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPriceAffect() {
		return priceAffect;
	}

	public void setPriceAffect(String priceAffect) {
		this.priceAffect = priceAffect;
	}

	public int getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}
}
