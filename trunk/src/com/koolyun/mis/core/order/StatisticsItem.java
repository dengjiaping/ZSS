package com.koolyun.mis.core.order;

public class StatisticsItem {
	String totalAmount;
	String cashAmount;
	String cardAmount;
	String otherAmount;
	int count;

	public StatisticsItem(String totalAmount, String cashAmount, String cardAmount, String otherAmount, int count) {
		this.totalAmount = totalAmount;
		this.cashAmount = cashAmount;
		this.cardAmount = cardAmount;
		this.otherAmount = otherAmount;
		this.count = count;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(String cashAmount) {
		this.cashAmount = cashAmount;
	}

	public String getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(String cardAmount) {
		this.cardAmount = cardAmount;
	}

	public String getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(String otherAmount) {
		this.otherAmount = otherAmount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
