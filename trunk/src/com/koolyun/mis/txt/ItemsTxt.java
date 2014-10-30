package com.koolyun.mis.txt;

import java.io.IOException;
import java.util.List;

import com.koolyun.mis.R;
import com.koolyun.mis.core.product.SaleProductInfo;
import com.koolyun.mis.util.Common;

public class ItemsTxt extends TxtBase {

	private String storeName = null;
	private String fromDate = null;
	private String toDate = null;
	private String path = null;

	public ItemsTxt(String txtPath) throws IOException {
		super(txtPath);
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

	public void RecordToItemsTxt(List<SaleProductInfo> saleProductList) throws IOException {
		printString = Common.getString(R.string.sales_statistics) + "\n";
		printString += Common.getString(R.string.merchant_name) + ":";
		addWriteContent(storeName);
		printString += Common.getString(R.string.begin_time) + ":";
		addWriteContent(fromDate);
		printString += Common.getString(R.string.end_time) + ":";
		addWriteContent(toDate);
		if (saleProductList.size() == 0)
			printString += "没有商品销售记录\n";
		for (int i = 0; i < saleProductList.size(); i++) {
			printString += saleProductList.get(i).getProductName() + ":";
			printString += saleProductList.get(i).getCount() + "\n";
		}
		overwriteTxt(path, printString);
	}

}
