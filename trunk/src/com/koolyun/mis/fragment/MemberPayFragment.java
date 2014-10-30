package com.koolyun.mis.fragment;

import com.koolyun.mis.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MemberPayFragment extends AbstractLoadingFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.member_pay, container, false);
		return v;
	}
}
