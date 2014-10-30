package com.koolyun.mis.txt;

import java.io.IOException;

import com.koolyun.mis.R;
import com.koolyun.mis.util.Common;

public class SalesTxt extends TxtBase {

	private String storeName = null;
	private String fromDate = null;
	private String toDate = null;
	private String totalSales = null;
	private String cash = null;
	private String creditCard = null;
	private String transactions = null;
	private String path = null;

	public SalesTxt(String txtPath) throws IOException {
		super(txtPath);
		// TODO Auto-generated constructor stub
		path = txtPath;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(String totalSales) {
		this.totalSales = totalSales;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getTransactions() {
		return transactions;
	}

	public void setTransactions(String transactions) {
		this.transactions = transactions;
	}

	public void RecordToSalesTxt() throws IOException {
		printString = Common.getString(R.string.sales_statistics) + "\n";
		printString += Common.getString(R.string.merchant_name) + ":";
		addWriteContent(storeName);
		printString += Common.getString(R.string.begin_time) + ":";
		addWriteContent(fromDate);
		printString += Common.getString(R.string.end_time) + ":";
		addWriteContent(toDate);
		printString += Common.getString(R.string.consume_total) + ":";
		addWriteContent(totalSales);
		printString += Common.getString(R.string.consume_cash) + ":";
		addWriteContent(cash);
		printString += Common.getString(R.string.consume_card) + ":";
		addWriteContent(creditCard);
		printString += Common.getString(R.string.consume_count) + ":";
		addWriteContent(transactions);
		overwriteTxt(path, printString);
	}

}
