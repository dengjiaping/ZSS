package com.koolyun.mis.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koolyun.mis.R;

public class ProductShowItemFragment extends AbstractFragment {

	View result = null;
	FragmentManager mFragmentManager = null;
	FragmentTransaction fragmentTransaction = null;
	int container_layout = R.id.pdtmg_item_container;

	// ProductManagerAddKindFragment mPdMgAdGdFragment=null;
	// public FrameLayout framelayout = null;
	// public ViewFlipper mViewFlipper = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// mFragmentManager = getFragmentManager();
		// fragmentTransaction = mFragmentManager.beginTransaction();
		// mPdMgAdGdFragment = new ProductManagerAddKindFragment();

		// mPdMgAdGdFragment.setmPrdSwImFg(this);
		result = inflater.inflate(R.layout.productshowitemfragment_layout, container, false);
		// mViewFlipper = (ViewFlipper)
		// result.findViewById(R.id.pdtmg_item_top_viewflipper);
		// framelayout = (FrameLayout)
		// result.findViewById(R.id.pdtmg_item_container);

		// fragmentTransaction.add(container_layout, mPdMgAdGdFragment);
		// fragmentTransaction.commit();
		return result;
	}

	void addNewFragMent(Fragment mfm) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.obj_push_left_in, R.anim.obj_push_left_out,
				R.anim.obj_push_right_in, R.anim.obj_push_right_out);
		fragmentTransaction.replace(container_layout, mfm);
		fragmentTransaction.commit();
	}
}
