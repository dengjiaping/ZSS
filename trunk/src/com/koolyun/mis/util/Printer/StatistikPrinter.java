package com.koolyun.mis.util.Printer;

import java.util.List;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.StatisticsItem;
import com.koolyun.mis.core.product.SaleProductInfo;
import com.koolyun.mis.core.store.StoreManager;
import com.koolyun.mis.util.Common;

public class StatistikPrinter {

	static void printStatistik(StatisticsItem mStatisticsItem, String startTime, String endTime) {
		PrinterHelper mPrinterHelper = new PrinterHelper();
		SPRTPrinter mPrinter = new SPRTPrinter();
		mPrinter.openPrinter();
		if (mPrinter.isPrinterUsable() != 0) {
			mPrinter.closePrinter();
			return;
		}
		mPrinter.setMode(PrinterBase.DOUBLE_WIDTHANDHEIGHT);
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.sales_statistics),
				PrinterHelper.alignCenter, true));
		mPrinter.setMode(PrinterBase.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_mname),
				StoreManager.getStoreName(), PrinterHelper.alignSide, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_start_date),
				startTime, PrinterHelper.alignSide, false));
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_end_date), endTime,
				PrinterHelper.alignSide, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_total_amount),
				mStatisticsItem.getTotalAmount(), PrinterHelper.alignSide, false));
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_cash_amount),
				mStatisticsItem.getCashAmount(), PrinterHelper.alignSide, false));
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_card_amount),
				mStatisticsItem.getCardAmount(), PrinterHelper.alignSide, false));
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_total_count),
				String.valueOf(mStatisticsItem.getCount()), PrinterHelper.alignSide, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.closePrinter();
	}

	static void printProductCount(List<SaleProductInfo> saleProductList, String startTime, String endTime) {
		if (saleProductList == null)
			return;
		PrinterHelper mPrinterHelper = new PrinterHelper();
		SPRTPrinter mPrinter = new SPRTPrinter();
		mPrinter.openPrinter();
		mPrinter.setMode(PrinterBase.DOUBLE_WIDTHANDHEIGHT);
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.sales_statistics),
				PrinterHelper.alignCenter, true));
		mPrinter.setMode(PrinterBase.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_mname),
				StoreManager.getStoreName(), PrinterHelper.alignSide, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_start_date),
				startTime, PrinterHelper.alignSide, false));
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_end_date), endTime,
				PrinterHelper.alignSide, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		if (saleProductList.size() == 0) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_no_record),
					PrinterHelper.alignLeft, true));
			mPrinter.PrintLineFeed();
		}
		for (int i = 0; i < saleProductList.size(); i++) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(saleProductList.get(i).getProductName(), ""
					+ saleProductList.get(i).getCount(), PrinterHelper.alignSide, false));
		}
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.closePrinter();

	}
}
