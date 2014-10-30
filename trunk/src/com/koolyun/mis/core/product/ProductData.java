package com.koolyun.mis.core.product;

import java.util.ArrayList;
import java.util.List;

public class ProductData {
	Product mProduct = null;
	ProductCategory mProductCategory = null;
	List<ProductAttribute> mAttrList = null;

	public ProductData() {
		mProduct = new Product();
		mProductCategory = new ProductCategory();
		mAttrList = new ArrayList<ProductAttribute>();
	}

	public Product getProduct() {
		return mProduct;
	}

	public void setProduct(Product mProduct) {
		this.mProduct = mProduct;
		this.mProductCategory = ProductManager.getProductCategory(mProduct.getProductCategoryID());
		this.mAttrList = ProductManager.getProductAttributeById(mProduct.getProductID(), true);
	}

	public List<ProductAttribute> getAttrList() {
		return mAttrList;
	}

	public void setAttrList(List<ProductAttribute> mAttrList) {
		this.mAttrList = mAttrList;
	}

	public ProductCategory getmProductCategory() {
		return mProductCategory;
	}

	public void setCateName(String cateName) {

	}
}
