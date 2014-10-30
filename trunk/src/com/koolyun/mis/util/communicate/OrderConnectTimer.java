package com.koolyun.mis.util.communicate;

import com.koolyun.mis.line.LineFactory;
import com.koolyun.mis.util.MyLog;

public class OrderConnectTimer extends OperateTimer {

	@Override
	protected void timerJob() {
		MyLog.e("OrderConnectTimer", "enter OrderConnectTimer timerJob");
		LineFactory.getCurrentLine().orderTimerJob();
	}

}
