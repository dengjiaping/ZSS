package com.koolyun.mis.util.Printer;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.koolyun.mis.R;
import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.order.OrderChangeInfo;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.order.OrderData;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.core.setting.SettingsManager;
import com.koolyun.mis.core.store.StoreManager;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.MoneyInputer;
import com.koolyun.mis.util.Printer.PrinterHelper.PrintPare;
import com.koolyun.mis.util.pay.PayBase;
import com.koolyun.mis.util.pay.PayManager;

public class OrderPrint {

	OrderData mOrderData = null;
	public static final int NORMAL_MODE = 0;
	public static final int ADDTIONAL_MODE = 1;
	public static final int HANGUP_MODE = 2;
	public static final int REFUND_MODE = 3;
	public static final int ALREADYUND_MODE = 4;
	public static final int SALENEEDREVERSE = 5;
	public static final int SALEVOIDNEEDREVERSE = 6;

	void printOrder(OrderData mOrderData) {
		// PrinterHelper mPrinterHelper = new PrinterHelper();
		// SPRTPrinter mPrinter = new SPRTPrinter();
		// mPrinter.openPrinter();
		// if(mPrinter.isPrinterUsable() != 0)
		// {
		// mPrinter.closePrinter();
		// return;
		// }
		// mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("<>",
		// 16),PrinterHelper.alignCenter,false));
		// mPrinter.PrintLineFeed();
		// mPrinter.closePrinter();
		printOrder(mOrderData, NORMAL_MODE);
		return;
	}

	void printOrder(OrderData mOrderData, int mode) {

		// PrintImage mPrintImage = new PrintImage();
		// mPrintImage.printBitMap(BitmapFactory.decodeResource(CloudPosApp.getInstance().getResources(),
		// R.drawable.test2));

		if (mode == HANGUP_MODE) {
			printHangupOrder(mOrderData, mode);
			return;
		}

		PrinterHelper mPrinterHelper = new PrinterHelper();
		SPRTPrinter mPrinter = new SPRTPrinter();
		mPrinter.openPrinter();
		if (mPrinter.isPrinterUsable() != 0) {
			mPrinter.closePrinter();
			return;
		}
		if (mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_CASH.toInt()
				&& BasicArithmetic.compare(mOrderData.getAmount(), "0.00") != 0
				&& (mode == NORMAL_MODE || mode == REFUND_MODE)) {
			mPrinter.openCashBox();
		}
		mPrinter.setMode(SPRTPrinter.DOUBLE_WIDTHANDHEIGHT);
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(StoreManager.getStoreName(),
				PrinterHelper.alignCenter, true));
		mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		if (mode == HANGUP_MODE) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ", Common.getString(R.string.hangup),
					PrinterHelper.alignSide, false));
			mPrinter.PrintLineFeed();
		} else if (mode == ADDTIONAL_MODE) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ", Common.getString(R.string.re_app),
					PrinterHelper.alignSide, false));
			mPrinter.PrintLineFeed();
		} else if (mode == REFUND_MODE) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ", Common.getString(R.string.me_salevoid),
					PrinterHelper.alignSide, false));
			mPrinter.PrintLineFeed();
		} else if (mode == ALREADYUND_MODE) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",
					Common.getString(R.string.voidandrefund), PrinterHelper.alignSide, false));
			mPrinter.PrintLineFeed();
		} else if (mode == SALENEEDREVERSE) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",
					Common.getString(R.string.sale_need_reverse), PrinterHelper.alignSide, false));
			mPrinter.PrintLineFeed();
		} else if (mode == SALEVOIDNEEDREVERSE) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",
					Common.getString(R.string.void_need_reverse), PrinterHelper.alignSide, false));
			mPrinter.PrintLineFeed();
		}

		if (mOrderData.getOrderRemark() != null) {
			if (!mOrderData.getOrderRemark().getSitIndex().isEmpty()) {
				mPrinter.PrintLineStr(Common.getString(R.string.sit_index)
						+ ": "
						+ mOrderData.getOrderRemark().getSitIndex().replace("\r\n", "\r      ")
								.replace("\r", "\r      ").replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
			if (!mOrderData.getOrderRemark().getRemark().isEmpty()) {
				mPrinter.PrintLineStr(Common.getString(R.string.remark)
						+ ": "
						+ mOrderData.getOrderRemark().getRemark().replace("\r\n", "\r      ").replace("\r", "\r      ")
								.replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
		}

		printColumeName(mPrinterHelper, mPrinter);

		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		MoneyInputer mMoneyInputer = new MoneyInputer();
		for (int i = 0; i < mOrderData.getOrderContentList().size(); i++) {
			String name = mOrderData.getOrderContentList().get(i).getProduct().getName();

			if (mOrderData.getOrderContentList().get(i).getProductId() == ShoppingCart.MANUID
					&& mOrderData.getOrderContentList().get(i).getmOrderContentRemark() != null
					&& mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark() != null
					&& !mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark().isEmpty()) {
				name = mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark();
			}
			String subattrList = getSubAttriStr(mOrderData.getOrderContentList().get(i));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(name + "" + subattrList,
					PrinterHelper.alignLeft, false));
			mPrinter.PrintLineFeed();

			List<PrintPare> tmplist2 = new LinkedList<PrintPare>();
			tmplist2.add(mPrinterHelper.new PrintPare(" ", 8));
			tmplist2.add(mPrinterHelper.new PrintPare(String
					.valueOf(mOrderData.getOrderContentList().get(i).getCount()), 8));

			mMoneyInputer.setMoney(mOrderData.getOrderContentList().get(i).getOnePrice());
			String price = mMoneyInputer.getMoney();
			tmplist2.add(mPrinterHelper.new PrintPare(price, 8));
			mMoneyInputer.setMoney(mOrderData.getOrderContentList().get(i).getItemAmount());
			String amount = mMoneyInputer.getMoney();
			tmplist2.add(mPrinterHelper.new PrintPare(amount, 8));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist2, PrinterHelper.alignCenter, false));
			mPrinter.PrintLineFeed();

		}

		if (mode == HANGUP_MODE || mode == ALREADYUND_MODE) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),
					PrinterHelper.alignCenter, false));
			String serialNo = "";
			if (Common.checkValidString(mOrderData.getCurrentOrder().getBillNo())) {
				serialNo = Common.getSerialNumberFromBillNo(mOrderData.getCurrentOrder().getBillNo());
			} else {
				serialNo = SettingsManager.querySettings().getTraceNo();
			}

			mPrinter.PrintLineStr(Common.getString(R.string.r_number) + ":  " + serialNo);
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.closePrinter();
			return;
		}

		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();

		mMoneyInputer.setMoney(mOrderData.getAmount());
		String total = mMoneyInputer.getMoney();
		mPrinter.setMode(SPRTPrinter.DOUBLE_HEIGHT);
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.totalprice) + ":",
				total, PrinterHelper.alignSide, false));

		if (mode == NORMAL_MODE) {
			// 现金支付并且金额不为0
			if (mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_CASH.toInt()
					&& BasicArithmetic.compare(mOrderData.getAmount(), "0.00") != 0) {
				mPrinter.PrintLineFeed();
				mMoneyInputer.setMoney(PayManager.getInstance().getMoneyGot());
				String cash = mMoneyInputer.getMoney();
				mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_cash) + ":",
						cash, PrinterHelper.alignSide, false));
				mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
				mPrinter.PrintLineFeed();
				mMoneyInputer.setMoney(PayManager.getInstance().getChange());
				String change = mMoneyInputer.getMoney();
				mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(
						Common.getString(R.string.p_change) + ":", change, PrinterHelper.alignSide, false));
			} else if (mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_MSR.toInt()) {
				mPrinter.PrintLineFeed();
				mMoneyInputer.setMoney(PayManager.getInstance().getMoneyGot());
				String cash = mMoneyInputer.getMoney();
				mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.p_card) + ":",
						cash, PrinterHelper.alignSide, false));
				mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
			} else {
				mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
			}
		}
		mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		mPrinter.setMode(SPRTPrinter.DOUBLE_HEIGHT);

		String serialNo = "";
		if (Common.checkValidString(mOrderData.getCurrentOrder().getBillNo())) {
			serialNo = Common.getSerialNumberFromBillNo(mOrderData.getCurrentOrder().getBillNo());
		} else {
			serialNo = SettingsManager.querySettings().getTraceNo();
		}
		mPrinter.PrintLineStr(Common.getString(R.string.r_number) + ":  " + serialNo);
		mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		if (AccountManager.getInstance().getCurrentAccount() != null)
			mPrinter.PrintLineStr(Common.getString(R.string.cashier) + ":  "
					+ AccountManager.getInstance().getCurrentAccount().getOperatorID());
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getCurrentDateTimeString(),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("<>", 16),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.saddress) + ": "
				+ StoreManager.getStoreAddress(), PrinterHelper.alignLeft, false));

		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.closePrinter();

	}

	private String getSubAttriStr(OrderContentData mdata) {
		StringBuilder retStr = new StringBuilder();
		List<ProductSubAttribute> tmpSubAttrlist = mdata.getProductSubAttrList();
		if (tmpSubAttrlist != null && !tmpSubAttrlist.isEmpty()) {
			retStr.append("(");
			for (int i = 0; i < tmpSubAttrlist.size(); i++) {
				retStr.append(tmpSubAttrlist.get(i).getName());
				if (i != tmpSubAttrlist.size() - 1)
					retStr.append("/");
			}
			retStr.append(")");
		}
		return retStr.toString();
	}

	private void printColumeName(PrinterHelper mPrinterHelper, SPRTPrinter mPrinter) {
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		List<PrintPare> tmplist = new LinkedList<PrintPare>();
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_name), 8));
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_count), 8));
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_price), 8));
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_amount), 8));
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist, PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
	}

	private void printColumeNameTwo(PrinterHelper mPrinterHelper, SPRTPrinter mPrinter) {
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		List<PrintPare> tmplist = new LinkedList<PrintPare>();
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_name), 16));
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_count), 16));
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist, PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
	}

	private void printHangupOrder(OrderData mOrderData, int mode) {
		if (mOrderData.getOrderChanged() != null) {
			printOrderChange(mOrderData);
			return;
		}

		PrinterHelper mPrinterHelper = new PrinterHelper();
		SPRTPrinter mPrinter = new SPRTPrinter();
		mPrinter.openPrinter();
		if (mPrinter.isPrinterUsable() != 0) {
			mPrinter.closePrinter();
			return;
		}
		mPrinter.setMode(SPRTPrinter.DOUBLE_WIDTHANDHEIGHT);
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(StoreManager.getStoreName(),
				PrinterHelper.alignCenter, true));
		mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		if (mode == HANGUP_MODE) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ", Common.getString(R.string.hangup),
					PrinterHelper.alignSide, false));
			mPrinter.PrintLineFeed();
		}

		if (mOrderData.getOrderRemark() != null) {
			if (!mOrderData.getOrderRemark().getSitIndex().isEmpty()) {
				mPrinter.PrintLineStr(Common.getString(R.string.sit_index)
						+ ": "
						+ mOrderData.getOrderRemark().getSitIndex().replace("\r\n", "\r      ")
								.replace("\r", "\r      ").replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
			if (!mOrderData.getOrderRemark().getRemark().isEmpty()) {
				mPrinter.PrintLineStr(Common.getString(R.string.remark)
						+ ": "
						+ mOrderData.getOrderRemark().getRemark().replace("\r\n", "\r      ").replace("\r", "\r      ")
								.replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
		}

		printColumeNameTwo(mPrinterHelper, mPrinter);

		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),
				PrinterHelper.alignCenter, false));
		mPrinter.PrintLineFeed();
		for (int i = 0; i < mOrderData.getOrderContentList().size(); i++) {
			String name = mOrderData.getOrderContentList().get(i).getProduct().getName();

			if (mOrderData.getOrderContentList().get(i).getProductId() == ShoppingCart.MANUID
					&& mOrderData.getOrderContentList().get(i).getmOrderContentRemark() != null
					&& mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark() != null
					&& !mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark().isEmpty()) {
				name = mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark();
			}
			String subattrList = getSubAttriStr(mOrderData.getOrderContentList().get(i));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(name + "" + subattrList,
					PrinterHelper.alignLeft, false));
			mPrinter.PrintLineFeed();

			List<PrintPare> tmplist2 = new LinkedList<PrintPare>();
			tmplist2.add(mPrinterHelper.new PrintPare(" ", 16));
			tmplist2.add(mPrinterHelper.new PrintPare(String
					.valueOf(mOrderData.getOrderContentList().get(i).getCount()), 16));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist2, PrinterHelper.alignCenter, false));
			mPrinter.PrintLineFeed();

		}

		if (mode == HANGUP_MODE) {
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),
					PrinterHelper.alignCenter, false));
			String serialNo = "";
			if (Common.checkValidString(mOrderData.getCurrentOrder().getBillNo())) {
				serialNo = Common.getSerialNumberFromBillNo(mOrderData.getCurrentOrder().getBillNo());
			} else {
				serialNo = SettingsManager.querySettings().getTraceNo();
			}

			mPrinter.PrintLineStr(Common.getString(R.string.r_number) + ":  " + serialNo);
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.closePrinter();
			return;
		}

	}

	private void printOrderChange(OrderData mOrderData) {
		PrinterHelper mPrinterHelper = new PrinterHelper();
		SPRTPrinter mPrinter = new SPRTPrinter();
		mPrinter.openPrinter();
		if (mPrinter.isPrinterUsable() != 0) {
			mPrinter.closePrinter();
			return;
		}
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ", Common.getString(R.string.addinfo),
				PrinterHelper.alignSide, false));
		mPrinter.PrintLineFeed();

		if (mOrderData.getOrderRemark() != null) {
			if (!mOrderData.getOrderRemark().getSitIndex().isEmpty()) {
				mPrinter.PrintLineStr(Common.getString(R.string.sit_index)
						+ ": "
						+ mOrderData.getOrderRemark().getSitIndex().replace("\r\n", "\r      ")
								.replace("\r", "\r      ").replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
			if (!mOrderData.getOrderRemark().getRemark().isEmpty()) {
				mPrinter.PrintLineStr(Common.getString(R.string.remark)
						+ ": "
						+ mOrderData.getOrderRemark().getRemark().replace("\r\n", "\r      ").replace("\r", "\r      ")
								.replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
		}

		printColumeNameTwo(mPrinterHelper, mPrinter);
		List<OrderChangeInfo> mChangeList = mOrderData.getOrderChanged();
		for (OrderChangeInfo mOrderChangeInfo : mChangeList) {
			Log.d("=========>", mOrderChangeInfo.toString());
			if (mOrderChangeInfo.isAllSame())
				continue;

			String name = mOrderData.getOrderContentData(mOrderChangeInfo).getProduct().getName();

			if (mOrderData.getOrderContentData(mOrderChangeInfo).getProductId() == ShoppingCart.MANUID
					&& mOrderData.getOrderContentData(mOrderChangeInfo).getmOrderContentRemark() != null
					&& mOrderData.getOrderContentData(mOrderChangeInfo).getmOrderContentRemark().getRemark() != null
					&& !mOrderData.getOrderContentData(mOrderChangeInfo).getmOrderContentRemark().getRemark().isEmpty()) {
				name = mOrderData.getOrderContentData(mOrderChangeInfo).getmOrderContentRemark().getRemark();
			}
			String subattrList = getSubAttriStr(mOrderData.getOrderContentData(mOrderChangeInfo));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(name + "" + subattrList,
					PrinterHelper.alignLeft, false));
			mPrinter.PrintLineFeed();

			List<PrintPare> tmplist = new LinkedList<PrintPare>();
			if (mOrderChangeInfo.isOnlyCountChange() || mOrderChangeInfo.getChangeFlag() == OrderChangeInfo.ADD) {
				if (mOrderChangeInfo.getCountChanged() > 0)
					tmplist.add(mPrinterHelper.new PrintPare("+", 16));
				else if (mOrderChangeInfo.getCountChanged() < 0)
					tmplist.add(mPrinterHelper.new PrintPare("-", 16));
				else
					tmplist.add(mPrinterHelper.new PrintPare("!", 16));
			} else if (mOrderChangeInfo.getChangeFlag() == OrderChangeInfo.CHANGE) {
				if (mOrderChangeInfo.getCountChanged() > 0)
					tmplist.add(mPrinterHelper.new PrintPare("+!", 16));
				else if (mOrderChangeInfo.getCountChanged() < 0)
					tmplist.add(mPrinterHelper.new PrintPare("-!", 16));
				else
					tmplist.add(mPrinterHelper.new PrintPare("!", 16));
			} else if (mOrderChangeInfo.getChangeFlag() == OrderChangeInfo.DELETE) {
				tmplist.add(mPrinterHelper.new PrintPare("-", 16));
			}
			tmplist.add(mPrinterHelper.new PrintPare(String.valueOf(mOrderChangeInfo.getCountChanged()), 16));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist, PrinterHelper.alignCenter, false));
			mPrinter.PrintLineFeed();
		}

		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),
				PrinterHelper.alignCenter, false));
		String serialNo = "";
		if (Common.checkValidString(mOrderData.getCurrentOrder().getBillNo())) {
			serialNo = Common.getSerialNumberFromBillNo(mOrderData.getCurrentOrder().getBillNo());
		} else {
			serialNo = SettingsManager.querySettings().getTraceNo();
		}

		mPrinter.PrintLineStr(Common.getString(R.string.r_number) + ":  " + serialNo);

		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.closePrinter();
	}
}
