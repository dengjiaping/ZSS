package com.koolyun.mis.fragment;

import android.os.Bundle;

import com.koolyun.mis.BillingActivity;

public class BillingFragmentBase extends AbstractFragment {

	BillingActivity mBilling;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void setBillingActivity(BillingActivity mbm) {
		mBilling = mbm;
	}

}
