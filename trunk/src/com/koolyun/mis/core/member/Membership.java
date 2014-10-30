package com.koolyun.mis.core.member;

public class Membership {
	private int cardNo;
	private int shopId;
	private String name;
	private String phone;

	public int getCardNo() {
		return cardNo;
	}

	public void setCardNo(int cardNo) {
		this.cardNo = cardNo;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Membership(int cardNo, int shopId, String name, String phone) {
		this.cardNo = cardNo;
		this.name = name;
		this.phone = phone;
		this.shopId = shopId;
	}
}
