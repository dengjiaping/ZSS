package com.koolyun.mis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.koolyun.mis.R;
import com.koolyun.mis.widget.MSignView;

public class SignFragment extends BillingFragmentBase implements OnClickListener {
	Button SignCancel;
	Button SignComfirm;
	private MSignView signView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.sign_fragment, container, false);

		signView = (MSignView) v.findViewById(R.id.signnameArea);
		SignCancel = (Button) v.findViewById(R.id.resign_btn);
		SignCancel.setOnClickListener(this);
		SignComfirm = (Button) v.findViewById(R.id.sign_confirm);
		SignComfirm.setOnClickListener(this);
		return v;
	}

	@Override
	public void onStop() {
		signView.clearSign();
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.resign_btn:
			if (signView != null)
				signView.clearSign();
			break;
		case R.id.sign_confirm:
			if (mBilling != null)
				mBilling.addSendInfoFragment();
			break;
		}

	}
}
