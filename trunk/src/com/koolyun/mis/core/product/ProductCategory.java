package com.koolyun.mis.core.product;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCategory implements Serializable {
	@SerializedName("cid")
	@Expose
	private int productCategoryId;
	@Expose
	private String name;
	@Expose
	private int enable = 1;

	private int syncFlag = 0;

	public ProductCategory() {

	}

	public ProductCategory(int productCategoryId, String productCategoryName, int enable) {
		this.productCategoryId = productCategoryId;
		this.name = productCategoryName;
		this.enable = enable;
	}

	public int getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(int productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategoryName() {
		return name;
	}

	public void setProductCategoryName(String name) {
		this.name = name;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public int getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
	}

}
