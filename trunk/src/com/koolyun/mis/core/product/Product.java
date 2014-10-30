package com.koolyun.mis.core.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

	@SerializedName("cid")
	@Expose
	private int productID = -1;
	@Expose
	private int productCategoryID = 0;
	@Expose
	private String name = null;
	@Expose
	private String price = null;
	@Expose
	private String photo = null;
	@Expose
	private int enable = 1;

	private int syncFlag = 0;

	public Product() {

	}

	@Override
	public boolean equals(Object o) {
		Product tmp = (Product) o;
		return (this.productID == tmp.getProductID());
	}

	public Product(int productID, int productCategoryID, String name, String price, String productPhoto, int enable,
			int syncFlag) {

		this.productID = productID;
		this.productCategoryID = productCategoryID;
		this.name = name;
		this.price = price;
		this.photo = productPhoto;
		this.enable = enable;
		this.syncFlag = syncFlag;
	}

	public Product(int productID, int productCategoryID, String name, String price, String productPhoto, int enable) {

		this.productID = productID;
		this.productCategoryID = productCategoryID;
		this.name = name;
		this.price = price;
		this.photo = productPhoto;
		this.enable = enable;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getProductCategoryID() {
		return productCategoryID;
	}

	public void setProductCategoryID(int productCategoryID) {
		this.productCategoryID = productCategoryID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProductPhoto() {
		return photo;
	}

	public void setProductPhoto(String productPhoto) {
		this.photo = productPhoto;
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
