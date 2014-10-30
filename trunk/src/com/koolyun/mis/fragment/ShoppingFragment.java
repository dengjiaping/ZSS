package com.koolyun.mis.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koolyun.mis.R;

/**
 * 购物车fragment
 */
public class ShoppingFragment extends AbstractFragment {

	private View shoppingView;
	private ShoppinigLeftFragment shoppinigLeftFragment;
	private ShoppingRightFragment shoppingRightFragment;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		shoppingView = inflater.inflate(R.layout.shopping_fragment, container, false);
		return shoppingView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		shoppinigLeftFragment = (ShoppinigLeftFragment) getFragmentManager().findFragmentById(R.id.shopping_left);
		shoppingRightFragment = (ShoppingRightFragment) getFragmentManager().findFragmentById(R.id.shopping_right);
	}

}
