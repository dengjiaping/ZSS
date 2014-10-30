package com.koolyun.mis.core.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductAttribute {

	@SerializedName("cid")
	@Expose
	private int productAttributeID;
	@Expose
	private String name;
	@Expose
	private int choiceType;
	@Expose
	private int defaultChoice;
	@Expose
	private int enable = 1;

	private int syncFlag = 0;

	public ProductAttribute() {

	}

	@Override
	public boolean equals(Object o) {
		ProductAttribute tmp = (ProductAttribute) o;
		return (this.productAttributeID == tmp.getProductAttributeID());
	}

	public ProductAttribute(int productAttributeID, String name, int choiceType, int defaultChoice, int enable) {
		this.productAttributeID = productAttributeID;
		this.name = name;
		this.choiceType = choiceType;
		this.defaultChoice = defaultChoice;
		this.enable = enable;
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

	public int getChoiceType() {
		return choiceType;
	}

	public void setChoiceType(int choiceType) {
		this.choiceType = choiceType;
	}

	public int getDefaultChoice() {
		return defaultChoice;
	}

	public void setDefaultChoice(int defaultChoice) {
		this.defaultChoice = defaultChoice;
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
