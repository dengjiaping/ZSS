package com.koolyun.mis.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.koolyun.mis.R;

public class AbstractLoadingFragment extends AbstractFragment {
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage(getActivity().getResources().getString(R.string.is_loading));
		pDialog.setCancelable(false);
	}
}
