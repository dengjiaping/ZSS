package com.koolyun.mis.util.communicate;

import com.google.gson.annotations.Expose;

public class Period {

	@Expose
	private int all;
	@Expose
	private int order;
	@Expose
	private int product;

	public int getAll() {
		return all;
	}

	public void setAll(int setall) {
		all = setall;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int setorder) {
		order = setorder;
	}

	public int getProduct() {
		return product;
	}

	public void setProduct(int setproduct) {
		product = setproduct;
	}

}
