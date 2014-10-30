package com.koolyun.mis.util.Printer;

import android.os.Handler;
import android.os.HandlerThread;

public class PrinterHandler {
	private static final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
	static {
		sWorkerThread.start();
	}

	private static final Handler sWorker = new Handler(sWorkerThread.getLooper());

	public static final Handler getHandler() {
		return sWorker;
	}
}
