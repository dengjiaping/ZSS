package com.koolyun.mis.util.communicate;

import com.koolyun.mis.line.LineFactory;
import com.koolyun.mis.util.MyLog;

public class ProductConnectTimer extends OperateTimer {

	@Override
	protected void timerJob() {
		MyLog.e("ProductConnectTimer", "enter ProductConnectTimer timerJob");
		LineFactory.getCurrentLine().productTimerJob();
	}

}
