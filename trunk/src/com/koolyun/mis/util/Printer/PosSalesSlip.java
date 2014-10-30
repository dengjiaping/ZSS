package com.koolyun.mis.util.Printer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

import com.koolyun.mis.core.store.StoreManager;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.finance.CardIssuersManager;
import com.koolyun.mis.finance.PosData;
import com.koolyun.mis.util.BankCardNoFormater;
import com.koolyun.mis.util.NumberFormater;

public class PosSalesSlip {

	private PrinterBase mprinter;
	private String doubletabspace = "";
	private String horizontalline = "------------------------------";

	public PosSalesSlip(PrinterBase printer) {
		mprinter = printer;
	}

	private void PrintCouple(String name, String value) {
		if (name == null || value == null)
			return;
		name = name.trim();
		value = value.trim();
		PrinterHelper ph = new PrinterHelper();
		mprinter.PrintLineStr(ph.getPrintStringWithFormat(name, value, PrinterHelper.alignSide, false));
		mprinter.PrintLineFeed();
	}

	private void ForSignature() {
		PrinterHelper ph = new PrinterHelper();
		mprinter.PrintLineStr(ph.getPrintStringWithFormat(horizontalline, 1, false));
		mprinter.PrintLineFeed();
		mprinter.PrintLineStr(ph.getPrintStringWithFormat("持卡人签名CARD HOLDER SIGNATURE", 1, false));
		mprinter.PrintLineFeed();
		mprinter.PrintLineStr(ph.getPrintStringWithFormat("本人确认以上交易，同意将其记入本卡账户", 1, false));
		mprinter.PrintLineFeed();
		mprinter.PrintLineStr(ph.getPrintStringWithFormat(
				"I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICE", 1, false));
		mprinter.PrintLineFeed();
		mprinter.PrintLineStr(doubletabspace + horizontalline);
		mprinter.PrintLineFeed();
		mprinter.PrintLineStr(ph.getPrintStringWithFormat("商户存根       MERCHANT COPY", 1, false));
		mprinter.PrintLineFeed();
		mprinter.PrintLineFeed();
	}

	private String getYear() {
		Calendar ca = Calendar.getInstance();
		ca.setTime(new java.util.Date());
		String year = "" + ca.get(Calendar.YEAR);
		return year;
	}

	@SuppressLint("SimpleDateFormat")
	private String FormateTime(String date) throws ParseException {
		DateFormat format = new SimpleDateFormat("HHmmss");
		Date dDate = format.parse(date);
		DateFormat format2 = new SimpleDateFormat("HH:mm:ss");
		String reTime = format2.format(dDate);
		return reTime;
	}

	@SuppressLint("SimpleDateFormat")
	private String FormateDate(String date) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date dDate = format.parse(date);
		DateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
		String reTime = format2.format(dDate);
		return reTime;
	}

	private String GetDate() throws ParseException {
		String numstr = getYear();
		numstr += PosData.getPosDataInstance().getDate();

		if (numstr.length() == 8)
			numstr = FormateDate(numstr);
		else
			return new String("");

		return numstr;
	}

	private String GetTime() throws ParseException {
		String numstr = PosData.getPosDataInstance().getTime();
		if (numstr.length() == 6)
			numstr = FormateTime(numstr);
		else
			return new String("");
		return numstr;
	}

	private String getAuthorizationNo() {
		String numstr = PosData.getPosDataInstance().getAuthorizationNo();
		return numstr;
	}

	private String getCardNo() {
		String numstr = PosData.getPosDataInstance().getAccountNo();
		return BankCardNoFormater.formate(numstr);
	}

	private String getName() {
		String numstr = PosData.getPosDataInstance().getAutherName();
		return numstr;
	}

	private String getReferenceNo() {
		String numstr = PosData.getPosDataInstance().getReferencNo();
		return numstr;
	}

	private String getMerchantNo() {
		String numstr = PosData.getPosDataInstance().getMerchantCode();
		return numstr;
	}

	private String getTerminalNo() {
		String numstr = PosData.getPosDataInstance().getTerminalCode();
		return numstr;
	}

	private String getIssuerNo() {

		String numstr = PosData.getPosDataInstance().getAdditionalData();
		if (numstr != null && !numstr.isEmpty() && numstr.length() >= 11)
			numstr = numstr.substring(0, 11).trim();
		return numstr;
	}

	private String getAcqNo() {
		String numstr = PosData.getPosDataInstance().getAdditionalData();
		if (numstr != null && !numstr.isEmpty() && numstr.length() >= 11)
			numstr = numstr.substring(11).trim();
		return numstr;
	}

	private String getTraceNo() {
		String numstr = PosData.getPosDataInstance().getTraceNo();
		return numstr;
	}

	private String getBatchNo() {
		String numstr = PosData.getPosDataInstance().getBatchid();
		return numstr;
	}

	private String getAmount() {
		String numstr = PosData.getPosDataInstance().getAmount();
		return numstr;
	}

	private String getTradeType() {
		String numstr = PosData.getPosDataInstance().getMessageType();
		if (numstr.equals("22"))
			numstr = "消费/SALE";
		else if (numstr.equals("23"))
			numstr = "消费撤销/VOID";
		else if (numstr.equals("25"))
			numstr = "退货/REFUND";

		return numstr;
	}

	private String getSign() {
		String numstr = PosData.getPosDataInstance().getMessageType();
		if (numstr.equals("22"))
			return "";
		else
			return "-";
	}

	public void Print(boolean isSignature) throws ParseException {
		mprinter.openPrinter();
		if (mprinter.isPrinterUsable() != 0) {
			mprinter.closePrinter();
			return;
		}
		PrinterHelper ph = new PrinterHelper();
		mprinter.setMode(PrinterBase.DOUBLE_HEIGHT);
		mprinter.PrintLineStr(ph.getPrintStringWithFormat("POS 签购单", 1, false));
		mprinter.PrintLineFeed();
		mprinter.PrintLineStr(ph.getPrintStringWithFormat("POS SALES SLIP", 1, false));
		mprinter.PrintLineFeed();
		mprinter.setMode(PrinterBase.NORMAL_SIZE);
		String storename = StoreManager.getStoreName();
		PrintCouple("商户名(MER NAME)：", storename);
		String numstr = getMerchantNo();
		PrintCouple("商户号(MER NO)：", numstr);
		numstr = getTerminalNo();
		PrintCouple("终端号(TER NO)：", numstr);
		String operatorid = AccountManager.getInstance().getCurrentAccount().getOperatorID();
		PrintCouple("操作员号(OP NO)：", operatorid);
		numstr = getCardNo();
		PrintCouple("卡号(CARD)：", numstr);

		numstr = getName();
		if (numstr != null && !numstr.isEmpty() && !numstr.equals("null"))
			PrintCouple("姓名(Name)：", numstr);

		mprinter.PrintLineStr(ph.getPrintStringWithFormat(horizontalline, 1, false));
		mprinter.PrintLineFeed();
		numstr = getIssuerNo();
		String cardIssName = CardIssuersManager.getCardIssuersAttributeByID(numstr).getCardIssuersName();
		if (cardIssName != null && !cardIssName.isEmpty() && !cardIssName.equals("null"))
			numstr = cardIssName;
		PrintCouple("发卡行(ISS)：", numstr);
		numstr = getAcqNo();
		PrintCouple("收单行号(ACQ NO)：", numstr);
		numstr = getTradeType();
		PrintCouple("类别(TXN TYPE)：", numstr);
		// PrintCouple("有效期(EXP DATE)：", "");
		numstr = getBatchNo();
		PrintCouple("批次号(BATCH NO)：", numstr);
		numstr = getTraceNo();
		PrintCouple("凭证号(VOUCHER NO)：", numstr);
		numstr = getAuthorizationNo();
		PrintCouple("授权码(AUTH NO)：", numstr);
		numstr = getReferenceNo();
		PrintCouple("参考号(REF NO)：", numstr);
		numstr = GetDate();
		PrintCouple("日期(DATE)：", numstr);
		numstr = GetTime();
		PrintCouple("时间(TIME)：", numstr);
		numstr = getSign() + NumberFormater.MoneyFromTwelveNumber(getAmount());
		PrintCouple("金额(AMOUT)：", "RMB " + numstr);
		// PrintCouple("小费(TIPS)：", "");
		// PrintCouple("总计(TOTAL)：", "");
		mprinter.PrintLineStr(ph.getPrintStringWithFormat(horizontalline, 1, false));
		if (isSignature) {
			mprinter.PrintLineFeed();
			mprinter.PrintLineFeed();
			mprinter.PrintLineFeed();
			mprinter.PrintLineFeed();
			ForSignature();
		} else {
			mprinter.PrintLineFeed();
			mprinter.PrintLineFeed();
		}
		mprinter.PrintLineFeed();
		mprinter.PrintLineFeed();
		mprinter.PrintLineFeed();
		mprinter.closePrinter();
	}
}
