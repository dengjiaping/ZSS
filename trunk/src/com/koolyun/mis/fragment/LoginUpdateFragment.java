package com.koolyun.mis.fragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.network.NetworkUtil;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.GetVersionCode;
import com.koolyun.mis.util.update.UpdateApk;

public class LoginUpdateFragment extends AbstractSupportV4Fragment implements OnClickListener {
	private Handler handler;
	private Button checkUpdate;
	public static final int START_LOADING = 0;
	public static final int LOAD_COMPLETE = 1;
	private View result = null;
	private TextView mVersionText = null;
	private TextView mUpdateText = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		result = inflater.inflate(R.layout.login_check_update, container, false);
		checkUpdate = (Button) result.findViewById(R.id.checkupdate_btn);
		checkUpdate.setOnClickListener(this);
		mVersionText = (TextView) result.findViewById(R.id.checkupdate_text_show);
		mUpdateText = (TextView) result.findViewById(R.id.checkbackup_time_show);
		mVersionText.setText(GetVersionCode.getAPPVersionName(getActivity()));

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case START_LOADING:
					pDialog.show();
					break;
				case LOAD_COMPLETE:
					pDialog.dismiss();
					break;
				}
				return false;
			}
		});

		RelativeLayout loginUpdateLayout = (RelativeLayout) result.findViewById(R.id.loginUpdateLayout);
		loginUpdateLayout.setBackgroundResource(R.drawable.repeat_bg);

		mUpdateText = (TextView) result.findViewById(R.id.checkupdate_time_show);
		if (MySharedPreferencesEdit.getInstancePublic(getActivity()).getLastUpdateVersion() < GetVersionCode
				.getAppVersionCode(getActivity())) {
			if (MySharedPreferencesEdit.getInstancePublic(getActivity()).getUpdateApkTime() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(Common.DATETIMEFORMAT, Locale.getDefault());
				Date date = new Date(MySharedPreferencesEdit.getInstancePublic(getActivity()).getUpdateApkTime());
				mUpdateText.setText(sdf.format(date));
			}
		}

		return result;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkupdate_btn:
			if (NetworkUtil.isConnected()) {
				UpdateApk update = new UpdateApk(this.getActivity(), handler);
				update.StartUpdateApk();
			} else {
				Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
}
