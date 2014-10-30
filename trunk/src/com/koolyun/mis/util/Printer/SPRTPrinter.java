package com.koolyun.mis.util.Printer;

import java.io.UnsupportedEncodingException;

import cn.koolcloud.jni.PrinterInterface;
import android.annotation.SuppressLint;
import android.util.Log;

public class SPRTPrinter extends PrinterBase {

	private static final byte[] LF = { 0x0A };
	private static final byte[] OPENCASHBOX = { 0x1B, 0x70, 0x00, (byte) 0xC8, (byte) 0xC8 };
	byte[] SIZEMODE = { 0x1D, 0x21, 0x00 };
	byte[] DEEPMODE = { 0x1B, 0x47, 0x00 };
	byte[] HTTRUE = { 0x1B, 0x44, 0x0C, 0x18, 0x24, 0x00 };
	byte[] HTFALSE = { 0x1B, 0x44, 0x00 };
	byte[] PRINTIMAGE = { 0x1B, 0x2A, 0x21, 0x00, 0x00 };
	byte[] X0A = { 0x0A };
	byte[] BETWEEN1 = { 0x1B, 0x32 };
	byte[] BETWEEN2 = { 0x1B, 0x33, 0x00 };

	public int isPrintOK() {
		if (getIsOpen() != OPEN)
			return -1;
		return 0;
	}

	public void setHT(boolean flag) {
		if (flag)
			PrinterInterface.write(HTTRUE, HTTRUE.length);
		else
			PrinterInterface.write(HTFALSE, HTFALSE.length);
	}

	public void setDeep(byte mode) {
		byte[] tmp = DEEPMODE.clone();
		tmp[2] = mode;
		PrinterInterface.write(tmp, tmp.length);
	}

	public void setMode(byte mode) {
		byte[] tmp = SIZEMODE.clone();
		tmp[2] = mode;
		PrinterInterface.write(tmp, tmp.length);
	}

	@SuppressLint("NewApi")
	public void PrintLineStr(String str) {
		if (str == null || str.isEmpty())
			return;
		try {
			byte[] tmp = str.getBytes("gb2312");
			PrinterInterface.write(tmp, tmp.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void PrintLineFeed() {
		PrinterInterface.write(LF, LF.length);
	}

	public void openCashBox() {
		if (isOpen != OPEN) {
			openPrinter();
		}
		PrinterInterface.write(OPENCASHBOX, OPENCASHBOX.length);
	}

	public void printImage(byte[] pic, int length, int offset) {
		if (isPrintOK() < 0)
			return;
		byte[] tmpArray = new byte[length + 5];
		for (int i = 0; i < length; i++) {
			tmpArray[i + 5] = pic[offset + i];
		}

		tmpArray[0] = PRINTIMAGE[0];
		tmpArray[1] = PRINTIMAGE[1];
		tmpArray[2] = PRINTIMAGE[2];

		byte len = (byte) (length / 3);
		if (len > 255)
			len = (byte) 255;
		tmpArray[3] = len;
		tmpArray[4] = PRINTIMAGE[4];

		Log.d("------------...>>>", "" + length / 3 + "     " + (byte) (length / 3));

		write(tmpArray, tmpArray.length);
		write(BETWEEN2, BETWEEN2.length);
		write(X0A, X0A.length);
		write(BETWEEN1, BETWEEN1.length);
	}

	private int write(byte[] arryData, int nDataLength) {
		PrinterInterface.write(arryData, nDataLength);
		return 0;
	}
}
