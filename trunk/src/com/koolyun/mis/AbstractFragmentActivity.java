package com.koolyun.mis;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.MyLog;
import com.umeng.analytics.MobclickAgent;

public class AbstractFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtException());
		super.onCreate(savedInstanceState);
		CloudPosApp.getInstance().addActivity(this);
	}

	class UncaughtException implements Thread.UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread thread, Throwable throwable) {
			String errorLog = Log.getStackTraceString(throwable);
			// 友盟日志上抛，不用友盟统计的时候可不写此行代码，说明此方法仅友盟sdk4.0版才支持
			MobclickAgent.reportError(getApplicationContext(), errorLog);
			// 记录到系统错误日志中
			MyLog.e(errorLog);
			// MyLog.record(errorLog);
			// 重启app
			CloudPosApp.getInstance().restartApp(AbstractFragmentActivity.this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		CloudPosApp.getInstance().removeActivity(this);
		super.onDestroy();
	}
}
