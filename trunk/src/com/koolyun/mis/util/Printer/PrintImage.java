package com.koolyun.mis.util.Printer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class PrintImage {

	private static final int VSIZE = 3;
	private static final int pixPerByte = 8;

	public void printBitMap(Bitmap mBitmap) {
		boolean[][] boolArray = bitmapToArray(mBitmap);
		// dumpBoolArray(boolArray);
		int width = boolArray.length;
		if (width < 1)
			return;
		int height = boolArray[0].length;
		int batch = (height / (VSIZE * pixPerByte)) + 1;
		byte[] printArray = boolArrayToPrintArray(boolArray);
		SPRTPrinter mSPRTPrinter = new SPRTPrinter();
		mSPRTPrinter.openPrinter();
		if (mSPRTPrinter.isPrinterUsable() != 0) {
			mSPRTPrinter.closePrinter();
			return;
		}
		for (int i = 0; i < batch; i++) {
			mSPRTPrinter.printImage(printArray, VSIZE * width, VSIZE * width * i);
		}
		mSPRTPrinter.closePrinter();

	}

	private boolean[][] bitmapToArray(Bitmap mBitmap) {
		return bitmapToArray(mBitmap, 140);
	}

	private boolean[][] bitmapToArray(Bitmap mBitmap, int thresholdvalue) {
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		boolean[][] imageArray = new boolean[width][height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int color = mBitmap.getPixel(j, i);

				int r = Color.red(color);
				int g = Color.green(color);
				int b = Color.blue(color);
				int a = Color.alpha(color);
				if (a < 170) {
					imageArray[j][i] = false;
				} else {
					int ret = ((int) (0.7 * r) + (int) (0.2 * g) + (int) (0.1 * b));
					boolean tmp = (thresholdvalue >= ret);
					imageArray[j][i] = tmp;
				}
			}
		}
		return imageArray;
	}

	@SuppressWarnings("unused")
	private void dumpBoolArray(boolean[][] array) {
		int width = array.length;
		if (width < 1)
			return;
		int height = array[0].length;

		for (int i = 0; i < height; i++) {
			StringBuilder m = new StringBuilder();
			for (int j = 0; j < width; j++) {
				if (array[j][i]) {
					m.append("+");
				} else {
					m.append("-");
				}
			}
			Log.d(" ", m.toString());
		}
	}

	private byte[] boolArrayToPrintArray(boolean[][] boolArray) {

		if (boolArray.length < 1)
			return null;
		int width = boolArray.length;
		int height = boolArray[0].length;
		int batch = (height / (VSIZE * pixPerByte)) + 1;
		if (height % (VSIZE * pixPerByte) == 0)
			batch--;
		byte[] printArray = new byte[batch * width * VSIZE];

		for (int i = 0; i < printArray.length; i++) {
			printArray[i] = 0;
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int index = VSIZE * i + (j / pixPerByte) % VSIZE + (j / VSIZE / pixPerByte) * VSIZE * width;
				if (i == 0)
					Log.d("", "" + (j / VSIZE / pixPerByte) + "  " + (j / VSIZE / pixPerByte) * VSIZE * width);
				if (boolArray[i][j]) {
					printArray[index] <<= 1;
					printArray[index] |= 0x01;
				} else {
					printArray[index] <<= 1;
					printArray[index] &= 0xfe;
				}
			}
		}

		return printArray;
	}

}
