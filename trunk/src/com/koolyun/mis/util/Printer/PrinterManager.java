package com.koolyun.mis.util.Printer;

import java.text.ParseException;
import java.util.List;

import com.koolyun.mis.core.order.OrderData;
import com.koolyun.mis.core.order.StatisticsItem;
import com.koolyun.mis.core.product.SaleProductInfo;
import com.koolyun.mis.util.MyLog;

public class PrinterManager {

	static PrinterManager mPrinterManager = null;

	static public PrinterManager getInstance() {
		if (mPrinterManager == null)
			mPrinterManager = new PrinterManager();
		return mPrinterManager;
	}

	public void PrintPosSlip(final boolean isSignature, int delay) {
		PrinterHandler.getHandler().postDelayed(new Runnable() {
			public void run() {
				PosSalesSlip mPosSalesSlip = new PosSalesSlip(new SPRTPrinter());
				try {
					mPosSalesSlip.Print(isSignature);
				} catch (ParseException e) {
					MyLog.d("PRINTPOS", "IF YOU SEE THIS IN THE LOG,THE POSSLIP WILLNOT BE PRINT!!!!!");
					e.printStackTrace();
				}
			}
		}, delay);
	}

	public void printStatistik(final StatisticsItem mStatisticsItem, final String startTime, final String endTime) {
		PrinterHandler.getHandler().post(new Runnable() {
			public void run() {
				StatistikPrinter.printStatistik(mStatisticsItem, startTime, endTime);
			}
		});
	}

	public void printProductCount(final List<SaleProductInfo> saleProductList, final String startTime,
			final String endTime) {
		PrinterHandler.getHandler().post(new Runnable() {
			public void run() {
				StatistikPrinter.printProductCount(saleProductList, startTime, endTime);
			}
		});
	}

	public void printOrder(final OrderData mOrderData) {
		PrinterHandler.getHandler().post(new Runnable() {
			public void run() {
				OrderPrint op = new OrderPrint();
				op.printOrder(mOrderData);
			}
		});
	}

	public void printOrder(final OrderData mOrderData, final int mode) {
		PrinterHandler.getHandler().post(new Runnable() {
			public void run() {
				OrderPrint op = new OrderPrint();
				op.printOrder(mOrderData, mode);
			}
		});
	}

}
