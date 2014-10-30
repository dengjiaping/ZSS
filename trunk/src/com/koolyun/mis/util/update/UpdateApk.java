package com.koolyun.mis.util.update;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.fragment.SettingUpdateFragment;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.GetVersionCode;
import com.koolyun.mis.util.MyLog;

public class UpdateApk {
	private ProgressDialog pBar = null;
	private Handler handler = new Handler();
	private String newVerName = "";
	private static Activity mactivity;
	private Handler handler2;
	private int serverVersionCode;
	private Handler handler3 = new Uihandler();

	// handler用于往创建handler对象所在的线程所绑定的消息队列发送消息

	@SuppressLint("HandlerLeak")
	private final class Uihandler extends Handler {// 接收从子线程传来的值，注意只有主线程才能对UI控件进行控制与更新
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int size = msg.getData().getInt("size");
				pBar.setProgress(size);// 当前刻度;
				if (pBar.getProgress() == pBar.getMax()) {
					Toast.makeText(mactivity, R.string.download_complete, Toast.LENGTH_LONG).show();
					DownloadOK();
				}
				break;
			case -1:
				Toast.makeText(mactivity, R.string.download_error, Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	public UpdateApk(Activity activity, Handler handler2) {
		mactivity = activity;
		this.handler2 = handler2;
	}

	public void StartUpdateApk() {
		new File(Environment.getExternalStorageDirectory(), Config.UPDATE_SAVENAME);
		new Thread() {
			public void run() {
				handler2.sendEmptyMessage(SettingUpdateFragment.START_LOADING);
				String storeId = MySharedPreferencesEdit.getInstancePublic(CloudPosApp.getInstance()).getStoreID();
				String apkName = Config.getAppName(mactivity);
				String version = String.valueOf(Config.getVerCode(mactivity));
				if (storeId == null || storeId.equals(""))
					storeId = "0";
				UpdateGotInfo gotInfo = NetworkTool
						.getApkPath("../apkUpdate/apk_update.php", storeId, apkName, version);

				MyLog.d("StartUpdateApk", "StoreId:" + storeId + ",appName:" + apkName + ",VersionCode:" + version);
				if (gotInfo != null) {
					gotInfo.getVersion();
					newVerName = gotInfo.getVersionName();
				}
				ConnectOK(gotInfo);
				handler2.sendEmptyMessage(SettingUpdateFragment.LOAD_COMPLETE);
			}
		}.start();
	}

	private void ConnectOK(final UpdateGotInfo gotinfo) {
		handler.post(new Runnable() {
			public void run() {
				int vercode = Config.getVerCode(mactivity);
				if (gotinfo != null && gotinfo.getVersion() > vercode) {
					serverVersionCode = gotinfo.getVersion();
					doNewVersionUpdate(gotinfo.getPath());
				} else {
					notNewVersionShow();
				}
			}
		});
	}

	private void notNewVersionShow() {
		String verName = Config.getVerName(mactivity);
		StringBuffer sb = new StringBuffer();
		sb.append(Common.getString(R.string.current_version));
		sb.append(verName);
		sb.append(Common.getString(R.string.neednot_update));
		Dialog dialog = new AlertDialog.Builder(UpdateApk.mactivity).setTitle(Common.getString(R.string.update_title))
				.setMessage(sb.toString())// 设置内容
				.setPositiveButton(Common.getString(R.string.sure), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				}).create();// 创建
		dialog.setCanceledOnTouchOutside(false);
		// 显示对话框
		dialog.show();
	}

	private void doNewVersionUpdate(final String path) {
		MyLog.e("path:" + path);
		String verName = Config.getVerName(mactivity);
		StringBuffer sb = new StringBuffer();
		sb.append(Common.getString(R.string.current_version));
		sb.append(verName);
		sb.append(Common.getString(R.string.findnew_prompt));
		sb.append(newVerName);
		sb.append(Common.getString(R.string.choose_update));
		Dialog dialog = new AlertDialog.Builder(UpdateApk.mactivity)
				.setTitle(Common.getString(R.string.update_title))
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton(Common.getString(R.string.sure),// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@SuppressWarnings("deprecation")
							@Override
							public void onClick(DialogInterface dialog, int which) {
								pBar = new ProgressDialog(UpdateApk.mactivity);
								pBar.setTitle(Common.getString(R.string.downloading));
								// pBar.setMax(100);
								pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								pBar.setCanceledOnTouchOutside(false);

								// downFile(path);
								if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 判断SDcard是否存在
																												// 或者可写数据
									pBar.setButton(CloudPosApp.getInstance().getString(R.string.cancel),
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(final DialogInterface dialog, final int which) {
													dialog.dismiss();
													exit();
												}
											});
									pBar.show();
									File file = Environment.getExternalStorageDirectory();// 获取sdcard跟目录，即保存路径
									download(path, file);
								} else {
									Toast.makeText(mactivity, R.string.sdcard_error, Toast.LENGTH_LONG).show();
								}
							}
						})
				.setNegativeButton(Common.getString(R.string.notnow_update), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 点击"取消"按钮之后退出程序
						dialog.dismiss();
					}
				}).create();// 创建
		dialog.setCanceledOnTouchOutside(false);
		// 显示对话框
		dialog.show();
	}

	private void DownloadOK() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				MySharedPreferencesEdit.getInstancePublic(CloudPosApp.getInstance()).setLastUpdateVersion(
						GetVersionCode.getAppVersionCode(CloudPosApp.getInstance()));
				MySharedPreferencesEdit.getInstancePublic(CloudPosApp.getInstance()).setUpdateApkTime(
						System.currentTimeMillis());
				InstallApk();
			}
		});
	}

	private void InstallApk() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(
				Uri.fromFile(new File(Environment.getExternalStorageDirectory(), Config.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		mactivity.startActivity(intent);
	}

	private DownloadTask task;

	private void exit() {// 退出下载
		try {
			if (task != null) {
				task.exit();
				task = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	private void download(String path, File savedir) {
		try {
			if (task != null) {
				task.exit();
				task = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		task = new DownloadTask(path, savedir);
		new Thread(task).start();
	}

	private final class DownloadTask implements Runnable {
		String path;
		File savedir;
		FileDownloader loader;

		public DownloadTask(String path, File savedir) {
			this.path = path;
			this.savedir = savedir;
		}

		public void exit() {
			if (loader != null) {
				loader.exit();// 退出下载
			}
		}

		@Override
		public void run() {
			try {
				loader = new FileDownloader(mactivity, path, savedir, 1, serverVersionCode + "");
				pBar.setMax(loader.getFileSize());// 设置进度条最大刻度
				loader.download(new DownloadProgressListener() {
					@Override
					public void onDownloadSize(int size) {
						Message msg = new Message();
						msg.what = 1;// 定义消息的ID，以便区别那个消息发过来的
						msg.getData().putInt("size", size);
						handler3.sendMessage(msg);
					}
				}, serverVersionCode + "");
			} catch (Exception e) {
				e.printStackTrace();
				handler3.sendMessage(handler.obtainMessage(-1));// 发送编号为-1的空消息
			}
		}
	}
}