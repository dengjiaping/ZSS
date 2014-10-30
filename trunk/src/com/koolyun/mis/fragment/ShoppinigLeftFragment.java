package com.koolyun.mis.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.koolyun.mis.R;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.MyLog;

/**
 * 商品列表界面
 */
public class ShoppinigLeftFragment extends AbstractFragment implements OnClickListener {

	ViewFlipper mViewFillper = null;
	View rootView = null;
	ShoppingProductCategaryFragment mShoppingProductCategaryFragment = null;
	ShoppingProductFragment mProductFragment = null;
	ManualInputFragment mManuIFragment = null;

	private Button btn_product_category;
	private TextView txt_product_name;
	private Button btn_product_manual_input;

	public static final int CATEGORY = 0;
	public static final int PRODUCT = 1;
	public static final int MANUPAGE = 2;

	private int currentIndex = CATEGORY;
	int[] anmiArray = { R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out,
			R.anim.push_left_in, R.anim.push_left_out };

	public void showChildIndex(int index) {
		currentIndex = index;
		Common.FlipperShowIndex(getActivity(), mViewFillper, index, anmiArray);
	}

	public int getCurrentPage() {
		return currentIndex;
	}

	public void setTitle(String title) {
		txt_product_name.setText(title);
	}

	public void showNavegateBack(boolean back) {
		if (back) {
			btn_product_category.setVisibility(View.VISIBLE);
		} else {
			btn_product_category.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showNavegateBack(false);
		setTitle("");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 防止fragment重复添加
		if (rootView != null) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null)
				parent.removeView(rootView);
		}
		rootView = inflater.inflate(R.layout.all_product_info, container, false);
		initAndset();
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MyLog.d("ShoppinigLeftFragment onDestroyView ");
		Fragment f0 =  getFragmentManager().findFragmentById(R.id.shoppingCategaryFragment);
		Fragment f =  getFragmentManager().findFragmentById(R.id.shoppingProductFragment);
		Fragment f1 =  getFragmentManager().findFragmentById(R.id.ManualInputFragment);
		if (f0 != null)
			getFragmentManager().beginTransaction().remove(f0).commit();
		if (f != null)
			getFragmentManager().beginTransaction().remove(f).commit();
		if (f1 != null)
			getFragmentManager().beginTransaction().remove(f1).commit();
	}

	private void initAndset() {
		mViewFillper = (ViewFlipper) rootView.findViewById(R.id.allproductFlipper);
		btn_product_category = (Button) rootView.findViewById(R.id.btn_product_category);
		txt_product_name = (TextView) rootView.findViewById(R.id.txt_product_name);
		btn_product_manual_input = (Button) rootView.findViewById(R.id.btn_product_manual_input);

		btn_product_category.setOnClickListener(this);
		btn_product_manual_input.setOnClickListener(this);

		FragmentManager fragmentManager = getFragmentManager();
		mShoppingProductCategaryFragment = (ShoppingProductCategaryFragment) fragmentManager
				.findFragmentById(R.id.shoppingCategaryFragment);
		mProductFragment = (ShoppingProductFragment) fragmentManager.findFragmentById(R.id.shoppingProductFragment);
		mManuIFragment = (ManualInputFragment) fragmentManager.findFragmentById(R.id.ManualInputFragment);
	}

	@Override
	public void onClick(View v) {
		setTitle("");
		switch (v.getId()) {
		case R.id.btn_product_category:
			MyLog.d("btn_product_category");
			// TODO 记录上一次的类型,返回的时候直接返回到对应的类型列表
			productListButton();
			// showChildIndex(CATEGORY);
			showNavegateBack(false);
			break;
		case R.id.btn_product_manual_input:
			MyLog.d("btn_product_manual_input");
			manualInput();
			showNavegateBack(true);
			break;
		default:
			break;
		}
	}

	private void productListButton() {
		if (currentIndex != CATEGORY) {
			if (mManuIFragment != null)
				mManuIFragment.InputisHidden();
			showChildIndex(CATEGORY);
		} else {
			// if (mProductList != null)
			// mProductList.all_back();
		}
	}

	private void manualInput() {
		if (currentIndex != MANUPAGE) {
			if (mManuIFragment != null)
				mManuIFragment.InputisShowen();
			showChildIndex(MANUPAGE);
		}
	}

	public void enterIntoProduct(int type, ProductCategory mProductCategory) {
		// MyLog.d("type=" + type + ",categoryId=" +
		// mProductCategory.getProductCategoryId());
		// MyLog.d("getDisplayedChild=" + mViewFillper.getDisplayedChild());
		if (mViewFillper.getDisplayedChild() != 1) {
			showChildIndex(PRODUCT);
			showNavegateBack(true);
			if (mProductCategory != null) {
				setTitle(mProductCategory.getProductCategoryName());
			} else {
				if (type == 0) {
					setTitle(getResources().getString(R.string.discountname));
				} else {
					setTitle(getResources().getString(R.string.allname));
				}
			}
			mProductFragment.displayProduct(type, mProductCategory);
		}
	}
}
