package com.koolyun.mis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

public class LeftBarBaseFragment extends AbstractFragment implements OnClickListener {

	LeftBarFragment mLeftBarFragment;
	ViewFlipper viewFlipper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void setmLeftBarFragment(LeftBarFragment mLeftBarFragment) {
		this.mLeftBarFragment = mLeftBarFragment;
	}

	public void setViewFlipper(ViewFlipper viewFlipper) {
		this.viewFlipper = viewFlipper;
	}

	@Override
	public void onClick(View v) {
	}
}
