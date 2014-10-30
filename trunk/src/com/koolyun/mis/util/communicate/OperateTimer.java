package com.koolyun.mis.util.communicate;

import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koolyun.mis.network.NetworkCommunication;
import com.koolyun.mis.util.MyLog;

public abstract class OperateTimer {

	protected Period mperiod = null;
	static protected int allTime = 0;
	// peroid : seconds
	private int lastperiod = -1;
	private final int minperiod = 60 * 10;
	protected Timer timer = null;
	protected MyTimerTask task = null;

	// period : seconds
	public void reSchedule(long delay, long period) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		if (task != null) {
			task.cancel(); // 将原任务从队列中移除
			task = null;
		}

		if (timer == null && task == null) {
			timer = new Timer();
			task = new MyTimerTask();
			if (timer != null && task != null)
				timer.schedule(task, delay * 1000, period * 1000);
		}
	}

	public Period getPeriod() {
		// 与服务器通信
		try {
			String jsonString = "all=0" + "&order=0" + "&product=0";
			String ret = NetworkCommunication.postRequest("settings/recycle_period.php", jsonString);
			if (ret == null)
				return null;
			MyLog.e("postRequest", ret);
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			mperiod = gson.fromJson(ret, Period.class);
			if (mperiod != null)
				allTime = mperiod.getAll();
			return mperiod;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getPeriodTime() {
		int time = allTime;
		return time;
	}

	public void reScheduleDefault(int period) {
		if (period == lastperiod)
			return;

		if (period == -1) {
			timerCancel();
			lastperiod = period;
			return;
		}

		if (period >= minperiod) {
			reSchedule(10, period);
			lastperiod = period;
			MyLog.e("OperateTimer", "reSchedule to period");
			return;
		} else {
			// default,milliseconds
			reSchedule(10, minperiod);
			lastperiod = period;
			MyLog.e("OperateTimer", "reSchedule to minperiod");
			return;
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

	abstract protected void timerJob();

	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			MyLog.i("OperateTimer", "timerJob run");
			OperateTimer.this.timerJob();
		}
	}
}
