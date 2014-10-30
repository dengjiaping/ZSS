package com.koolyun.mis.fragment;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.koolyun.mis.R;
import com.umeng.analytics.MobclickAgent;

public class AbstractDialogFragment extends DialogFragment {
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage(getActivity().getResources().getString(R.string.is_loading));
		pDialog.setCancelable(false);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}
}
