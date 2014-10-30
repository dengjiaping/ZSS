package com.koolyun.mis.fragment;

import com.koolyun.mis.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MembershipCardInfoFragment extends AbstractLoadingFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.membership_card_info, container, false);
		return v;
	}
}
