package com.koolyun.mis.fragment;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.aidl.IPaySDK;
import com.koolyun.mis.myinterface.OnActiveCompleteListener;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;

public class ActiveFragment extends Fragment implements OnClickListener {
	private String payResult;
	private IPaySDK iPay;
	private OnActiveCompleteListener onActiveCompleteListener;

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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent();
		intent.setAction("com.cynovo.sirius.Service.permission.CLOUDPAY");
		getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.active, null);

		Button activeButton1 = (Button) v.findViewById(R.id.activeButton1);
		activeButton1.setOnClickListener(this);

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onActiveCompleteListener = (OnActiveCompleteListener) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activeButton1:
			try {
				JSONObject jsonObject = new JSONObject();
				// TODO
				jsonObject.put("AppName", getActivity().getPackageName()); // test
																			// data
				jsonObject.put("ReqTransDate", "0212"); // test data

				payResult = iPay.registerPOS(jsonObject.toString());
				Log.e("ok", "Active Result:" + payResult);
				if (payResult != null && payResult.length() > 0) {
					Toast.makeText(getActivity(), payResult, Toast.LENGTH_LONG).show();
					Log.e("payResult", payResult);
					JSONObject json = new JSONObject(payResult);

					MySharedPreferencesEdit mySharedPreferencesEdit = MySharedPreferencesEdit
							.getInstancePublic(getActivity());
					mySharedPreferencesEdit.setIsDownloadSecretKey(true);
					mySharedPreferencesEdit.setMerchantNo(json.getString("mid"));
					mySharedPreferencesEdit.setTerminalNo(json.getString("tid"));
					mySharedPreferencesEdit.setCpuId(json.getString("cpuid"));
					mySharedPreferencesEdit.setStoreID(json.getString("storeid"));

					onActiveCompleteListener.OnActiveComplete();
				} else {
					Toast.makeText(getActivity(), R.string.device_active_error, Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), R.string.device_active_error, Toast.LENGTH_SHORT).show();
			}
			break;
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
