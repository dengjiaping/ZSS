package com.koolyun.mis.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.koolyun.mis.R;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.FileManager;
import com.koolyun.mis.widget.MyImageButton;

/**
 * 商品列表界面
 */
public class ShowAllProductFragment_bak extends AbstractFragment implements OnClickListener {

	ViewFlipper mViewFillper = null;

	MyImageButton mProductListButton = null;
	View allProduct = null;
	MyImageButton mManual_input = null;
	ManualInputFragment mManuIF = null;
	ProductListFragment mProductList = null;
	private static final int PRODUCTPAGE = 0;
	private static final int MANUPAGE = 1;

	private int currentIndex = PRODUCTPAGE;
	int[] anmiArray = { R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out, };

	private void showChildIndex(int index) {
		currentIndex = index;
		Common.FlipperShowIndex(getActivity(), mViewFillper, index, anmiArray);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		allProduct = inflater.inflate(R.layout.all_product_info, container, false);
		initAndset();
		return allProduct;
	}

	private void initAndset() {
		mViewFillper = (ViewFlipper) allProduct.findViewById(R.id.allproductFlipper);
		mProductListButton = (MyImageButton) allProduct.findViewById(R.id.product_list);
		mManual_input = (MyImageButton) allProduct.findViewById(R.id.manual_input);
		mProductListButton.setOnClickListener(this);
		mManual_input.setOnClickListener(this);
		inti_status_button();

		FragmentManager fragmentManager = getFragmentManager();
		mManuIF = (ManualInputFragment) fragmentManager.findFragmentById(R.id.ManualInputFragment);
		// mProductList = (ProductListFragment)
		// fragmentManager.findFragmentById(R.id.ProductListFragment);
	}

	@SuppressWarnings("deprecation")
	private void inti_status_button() {
		mProductListButton.setText(0);
		mProductListButton.setBackgroundDrawable(FileManager.readBitMapDrawable(getActivity(),
				R.drawable.goods_list_button1));
		mManual_input.setBackgroundDrawable(FileManager.readBitMapDrawable(getActivity(), R.drawable.keyboard_button1));

		mProductListButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mManual_input.setBackgroundDrawable(FileManager.readBitMapDrawable(getActivity(),
							R.drawable.keyboard_button1));
					mProductListButton.setBackgroundDrawable(FileManager.readBitMapDrawable(getActivity(),
							R.drawable.goods_list_button0));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					mProductListButton.setBackgroundDrawable(FileManager.readBitMapDrawable(getActivity(),
							R.drawable.goods_list_button1));
				}
				return false;
			}
		});

		mManual_input.setText(1);
		mManual_input.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mProductListButton.setBackgroundDrawable(FileManager.readBitMapDrawable(getActivity(),
							R.drawable.goods_list_button0));
					mManual_input.setBackgroundDrawable(FileManager.readBitMapDrawable(getActivity(),
							R.drawable.keyboard_button1));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					mManual_input.setBackgroundDrawable(FileManager.readBitMapDrawable(getActivity(),
							R.drawable.keyboard_button0));
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_list:
			productListButton();
			break;
		case R.id.manual_input:
			manualInput();
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void productListButton() {
		mProductListButton.setBackgroundDrawable(FileManager.readBitMapDrawable(getActivity(),
				R.drawable.goods_list_button1));
		if (currentIndex != PRODUCTPAGE) {
			if (mManuIF != null)
				mManuIF.InputisHidden();
			showChildIndex(PRODUCTPAGE);
		} else {
			if (mProductList != null)
				mProductList.all_back();
		}
	}

	private void manualInput() {
		if (currentIndex != MANUPAGE) {
			if (mManuIF != null)
				mManuIF.InputisShowen();
			showChildIndex(MANUPAGE);
		}
	}
}
