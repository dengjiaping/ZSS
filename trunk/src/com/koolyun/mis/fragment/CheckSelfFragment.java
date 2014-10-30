package com.koolyun.mis.fragment;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.CheckSelfAdapter;
import com.koolyun.mis.aidl.IPaySDK;
import com.koolyun.mis.core.CheckSelfItemInfo;
import com.koolyun.mis.devices.DeviceID;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.widget.AnyWhereDialog;

public class CheckSelfFragment extends AbstractDialogFragment implements OnClickListener {
	private AnyWhereDialog mDialog = null;
	// private BroadcastReceiver receiver;
	private Handler handler = new Handler();
	private RadioButton mCheckStatus = null;
	private Button mReCheck = null;
	private ListView mCheckList = null;
	private CheckSelfAdapter mCheckSelfAdapter = null;
	private boolean deviceStatus = true;
	private String checkResult;
	private IPaySDK iPay;

	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			iPay = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// IPaySDK.Stub.asInterface，获取接口
			iPay = IPaySDK.Stub.asInterface(service);
		}
	};

	public static CheckSelfFragment newInstance(int mode) {
		// FIXME:设置设备检测中标志位
		CloudPosApp.getInstance().setCheckDeviceInProcess(true);

		CheckSelfFragment frag = new CheckSelfFragment();
		Bundle args = new Bundle();
		args.putInt("mode", mode);
		frag.setArguments(args);
		return frag;
	}

	public CheckSelfFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent();
		intent.setAction("com.cynovo.sirius.Service.permission.CLOUDPAY");
		getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	// @Override
	// public void onDestroy() {
	// if (receiver != null)
	// this.getActivity().unregisterReceiver(receiver);
	// super.onDestroy();
	// }

	@SuppressLint("ResourceAsColor")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mDialog = new AnyWhereDialog(getActivity(), 774, 406, 0, 0, R.layout.check_self, R.style.Theme_dialog,
				Gravity.CENTER, true);
		mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mDialog.setCanceledOnTouchOutside(false);
		InitView();
		beginCheck();
		return mDialog;
	}

	private void beginCheck() {
		// new Thread() {
		// @Override
		// public void run() {
		// DeviceManager.getInstance().ChangeDeviceStatus();
		// }
		// }.start();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					checkResult = iPay.getHardWareState();
					Log.e("ok", "Active Result:" + checkResult);
					if (checkResult != null && checkResult.length() > 0) {
						Log.e("checkResult", checkResult);
						JSONObject json = new JSONObject(checkResult);
						boolean emv = json.getBoolean("emv");
						setItemInfo(DeviceID.DEVICE_ID_EMV, emv, R.string.self_ic);
						boolean msr = json.getBoolean("msr");
						setItemInfo(DeviceID.DEVICE_ID_MSR, msr, R.string.self_msr);
						boolean pinpad = json.getBoolean("pinpad");
						setItemInfo(DeviceID.DEVICE_ID_HARDPINPAD, pinpad, R.string.self_hard_pin);
						boolean printer = json.getBoolean("printer");
						setItemInfo(DeviceID.DEVICE_ID_PRINTER, printer, R.string.self_printer);
						boolean network = json.getBoolean("network");
						setItemInfo(DeviceID.DEVICE_ID_NETLINK, network, R.string.self_netlink);

						showResult();
					} else {
						mDialog.setCanceledOnTouchOutside(true);
						Toast.makeText(getActivity(), R.string.device_info_error, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					mDialog.setCanceledOnTouchOutside(true);
					e.printStackTrace();
					Toast.makeText(getActivity(), R.string.device_info_error, Toast.LENGTH_SHORT).show();
				}
			}
		}, 800);
	}

	public void InitView() {
		mCheckStatus = (RadioButton) mDialog.findViewById(R.id.check_result_icon);
		mReCheck = (Button) mDialog.findViewById(R.id.re_check_btn);
		mReCheck.setOnClickListener(this);
		mCheckList = (ListView) mDialog.findViewById(R.id.check_result_list);
		mCheckSelfAdapter = new CheckSelfAdapter(this.getActivity());
		mCheckList.setAdapter(mCheckSelfAdapter);
		mCheckList.setClickable(false);
	}

	private void setItemInfo(final int deviceid, final boolean flag, final int infoid) {
		setItemInfo(deviceid, flag, getString(infoid));
	}

	private void setItemInfo(final int deviceid, final boolean flag, final String info) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				mCheckSelfAdapter.addItemInfo(new CheckSelfItemInfo(deviceid, "", flag, info));
			}
		});
	}

	private void showResult() {
		mReCheck.setClickable(true);
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (deviceStatus) {
					mCheckStatus.setText(R.string.self_check_ok);
					mCheckStatus.setChecked(false);
				} else {
					mCheckStatus.setText(R.string.self_check_notok);
					mCheckStatus.setChecked(true);
				}
				mDialog.setCanceledOnTouchOutside(true);
			}
		});
	}

	// private void checkStatus(final int deviceid, final boolean flag) {
	// if (!flag)
	// deviceStatus = false;
	//
	// if (deviceid == DeviceID.DEVICE_ID_NETLINK) {
	// setItemInfo(deviceid, flag, R.string.self_netlink);
	// } else if (deviceid == DeviceID.DEVICE_ID_CASHBOX) {
	// setItemInfo(deviceid, flag, R.string.self_box);
	// } else if (deviceid == DeviceID.DEVICE_ID_EMV) {
	// setItemInfo(deviceid, flag, R.string.self_ic);
	// } else if (deviceid == DeviceID.DEVICE_ID_HARDPINPAD) {
	// setItemInfo(deviceid, flag, R.string.self_hard_pin);
	// } else if (deviceid == DeviceID.DEVICE_ID_MSR) {
	// setItemInfo(deviceid, flag, R.string.self_msr);
	// } else if (deviceid == DeviceID.DEVICE_ID_NFC) {
	// setItemInfo(deviceid, flag, R.string.self_nfc);
	// } else if (deviceid == DeviceID.DEVICE_ID_SOFTPINPAD) {
	// setItemInfo(deviceid, flag, R.string.self_soft_pin);
	// } else if (deviceid == DeviceID.DEVICE_ID_PRINTER) {
	// setItemInfo(deviceid, flag, R.string.self_printer);
	// }
	// }

	// private void installReceiver() {
	// IntentFilter filter = new IntentFilter();
	// filter.addAction(DeviceBase.ACTION_DEVICE_STATUS);
	// filter.addAction(DeviceBase.ACTION_DEVICE_CHECKEND);
	// receiver = new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// if (intent.getAction().equals(DeviceBase.ACTION_DEVICE_STATUS)) {
	// int device = intent.getExtras().getInt(DeviceBase.DEVICE);
	// boolean status = intent.getExtras().getBoolean(
	// DeviceBase.STATUS);
	// Log.d("DEVICESTATUS:", "=============: " + device + "   "
	// + status);
	// checkStatus(device, status);
	// } else if (intent.getAction().equals(
	// DeviceBase.ACTION_DEVICE_CHECKEND)) {
	// showResult();
	// }
	// }
	// };
	// this.getActivity().registerReceiver(receiver, filter);
	// }

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		// FIXME:设置设备检测中标志位
		CloudPosApp.getInstance().setCheckDeviceInProcess(false);
	}

	@Override
	public void onClick(View v) {
		if (JavaUtil.isFastDoubleClick())
			return;
		if (v.getId() == R.id.re_check_btn) {
			if (!CloudPosApp.getInstance().isWorkkeyInprocess()) {
				CloudPosApp.getInstance().setCheckDeviceInProcess(true);
				mReCheck.setClickable(false);
				deviceStatus = true;
				mCheckStatus.setText(R.string.self_in_process);
				mCheckStatus.setChecked(false);

				mCheckSelfAdapter.removeAllInfo();
				beginCheck();
				mDialog.setCanceledOnTouchOutside(false);
			}
		}
	}

	@Override
	public void onDestroy() {
		if (conn != null) {
			getActivity().unbindService(conn);
		}
		iPay = null;
		super.onDestroy();
	}
}
