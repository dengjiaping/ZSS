package com.koolyun.mis.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProductBaseShowItemFragment extends AbstractFragment {

	ProductShowItemFragment mPrdSwImFg = null;

	public ProductShowItemFragment getmPrdSwImFg() {
		return mPrdSwImFg;
	}

	public void setmPrdSwImFg(ProductShowItemFragment mPrdSwImFg) {
		this.mPrdSwImFg = mPrdSwImFg;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

}
