package com.koolyun.mis.util;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.koolyun.mis.bean.MerchantBean;
import com.koolyun.mis.core.setting.Settings;
import com.koolyun.mis.core.setting.SettingsManager;

public class CloudPosApp extends Application {

	private static CloudPosApp globalVar = null;

	private volatile boolean workkeyInprocess = false;
	private volatile boolean isWorkKeyDownload = false;
	private volatile boolean alreadyInit = false;
	private volatile boolean checkDeviceInProcess = false;
	private volatile MerchantBean merchantBean = null;

	public boolean isCheckDeviceInProcess() {
		return checkDeviceInProcess;
	}

	public void setCheckDeviceInProcess(boolean checkDeviceInProcess) {
		this.checkDeviceInProcess = checkDeviceInProcess;
	}

	public boolean isWorkkeyInprocess() {
		return workkeyInprocess;
	}

	public void setWorkkeyInprocess(boolean workkeyInprocess) {
		this.workkeyInprocess = workkeyInprocess;
	}

	public boolean isWorkKeyDownload() {
		return isWorkKeyDownload;
	}

	public void setWorkKeyDownload(boolean isWorkKeyDownload) {
		this.isWorkKeyDownload = isWorkKeyDownload;
	}

	public boolean isAlreadyInit() {
		return alreadyInit;
	}

	public void setAlreadyInit(boolean alreadyInit) {
		this.alreadyInit = alreadyInit;
	}

	private static Stack<Activity> activityStack;

	/**
	 * 添加Activity到堆栈
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 * 
	 * @return
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 * 
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
		closeInterface();
	}

	public void restartApp(final Context context) {
		try {
			closeInterface();
			/** 启动应用 */
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent launch = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
					launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(launch);
					System.exit(0);
				}
			}, 100);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * 关闭可能暂未关闭的底层接口
	 */
	private void closeInterface() {
		// HostlinkInterface.close();
		// PbocInterface.close();
		// PinPadInterface.close();
	}

	public static CloudPosApp getInstance() {
		if (null == globalVar) {
			globalVar = new CloudPosApp();
		}
		return globalVar;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		globalVar = this;
		Common.acquireWakeLock(this);
	}

	@Override
	public void onTerminate() {
		// HostlinkInterface.close();
		Common.releaseWakeLock();
		super.onTerminate();
	}

	public Settings getSettings() {
		return SettingsManager.querySettings();
	}

	/**
	 * @return the merchantBean
	 */
	public MerchantBean getMerchantBean() {
		return merchantBean;
	}

	/**
	 * @param merchantBean
	 *            the merchantBean to set
	 */
	public void setMerchantBean(MerchantBean merchantBean) {
		this.merchantBean = merchantBean;
	}

}
