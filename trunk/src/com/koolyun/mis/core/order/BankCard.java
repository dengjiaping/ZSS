package com.koolyun.mis.core.order;

public class BankCard {
	private int bankCardId;
	private String bankCardNo;
	private String validThrough;
	private String cardOwnerName;

	public BankCard(int bankCardId, String bankCardNo, String cardOwnerName, String validThrough) {
		this.bankCardId = bankCardId;
		this.bankCardNo = bankCardNo;
		this.cardOwnerName = cardOwnerName;
		this.validThrough = validThrough;
	}

	public BankCard() {
	}

	public int getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(int bankCardId) {
		this.bankCardId = bankCardId;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getTrueThrough() {
		return validThrough;
	}

	public void setTrueThrough(String validThrough) {
		this.validThrough = validThrough;
	}

	public String getCardOwnerName() {
		return cardOwnerName;
	}

	public void setCardOwnerName(String cardOwnerName) {
		this.cardOwnerName = cardOwnerName;
	}
}
