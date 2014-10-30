package com.koolyun.mis.util.Printer;

import cn.koolcloud.jni.PrinterInterface;

public abstract class PrinterBase {

	public static final int OPEN = 0;
	public static final int CLOSE = 1;
	public static final int ERROR = 2;

	public static final byte NORMAL_SIZE = 0x00;
	public static final byte DOUBLE_WIDTH = 0x10;
	public static final byte DOUBLE_HEIGHT = 0x01;
	public static final byte DOUBLE_WIDTHANDHEIGHT = 0x11;

	public static final byte DEEP_NORMAL = 0x00;
	public static final byte DEEP_DOUBLE = 0x01;

	int isOpen = OPEN;

	public void openPrinter() {
		int nResult;
		nResult = PrinterInterface.open();
		if (nResult < 0) {
			isOpen = CLOSE;
			return;
		}
		nResult = PrinterInterface.begin();
		if (nResult < 0) {
			isOpen = CLOSE;
			return;
		}
		isOpen = OPEN;
	}

	public void setPrinter(int printer) {
		int nResult;
		nResult = PrinterInterface.set(printer);
		if (nResult < 0) {
			return;
		}
	}

	public int isPrinterUsable() {
		return 0;
	}

	public void closePrinter() {
		int nResult;
		nResult = PrinterInterface.end();
		if (nResult < 0) {
			isOpen = ERROR;
			return;
		}
		nResult = PrinterInterface.close();
		if (nResult < 0) {
			isOpen = ERROR;
			return;
		}
		isOpen = CLOSE;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public abstract void setMode(byte mode);

	public abstract void PrintLineStr(String str);

	public abstract void PrintLineFeed();

	public abstract void openCashBox();
}
