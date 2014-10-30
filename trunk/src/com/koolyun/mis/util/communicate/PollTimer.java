package com.koolyun.mis.util.communicate;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.koolyun.mis.line.LineFactory;
import com.koolyun.mis.util.MyLog;

public class PollTimer {

	private List<OperateTimer> mOt = new LinkedList<OperateTimer>();
	protected Timer timer = null;
	protected TimerTask task = new TimerTask() {
		@Override
		public void run() {
			// 在这里添加轮询用的代码，定时去询问后台配置情况
			MyLog.i("PollTimer", "PollTimer run");
			LineFactory.getCurrentLine().pollTimerJob(mOt);
		}
	};

	// period : seconds
	public void reSchedule(long delay, long period) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		if (timer == null) {
			timer = new Timer();
			timer.schedule(task, delay * 1000, period * 1000);
		}
	}

	public void timerCancel() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		if (task != null) {
			task.cancel();
			task = null;
		}
	}

	public void setOperateTimer(List<OperateTimer> ot) {
		mOt = ot;
	}

}
