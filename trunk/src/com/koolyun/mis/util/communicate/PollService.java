package com.koolyun.mis.util.communicate;

import java.util.LinkedList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.koolyun.mis.util.MyLog;

public class PollService extends Service {

	String TAG = "PollService";
	private PollTimer poll = null;

	private MyBinder mybinder = new MyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		MyLog.i(TAG, "PollService onBind");
		return mybinder;
	}

	@Override
	public void onRebind(Intent intent) {
		MyLog.i(TAG, "PollService onRebind");
		super.onRebind(intent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		MyLog.i(TAG, "PollService onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MyLog.i(TAG, "PollService onCreate");
		poll = new PollTimer();
		List<OperateTimer> ot = new LinkedList<OperateTimer>();
		ot.add(new OrderConnectTimer());
		ot.add(new ProductConnectTimer());
		poll.setOperateTimer(ot);
		// 设置poll时间间隔,秒
		poll.reSchedule(120, 240);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		poll.timerCancel();
		MyLog.i(TAG, "PollService onDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MyLog.i(TAG, "PollService onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	public class MyBinder extends Binder {
		public PollService getService() {
			return PollService.this;
		}
	}

}
