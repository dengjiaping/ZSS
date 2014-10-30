package com.koolyun.mis.fragment;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.OrderData;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.util.Printer.OrderPrint;
import com.koolyun.mis.util.Printer.PrinterManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LeftBarProductItemFragment extends LeftBarBaseFragment {

	static final String TAG = "LeftBarProductItemFragment";
	View layout = null;
	Button leftbar_detail_back_to1_btn = null;
	Button printerButton = null;
	OrderData mOrderData = null;
	int orderid;

	public static LeftBarProductItemFragment newInstance(int orderid) {
		LeftBarProductItemFragment frag = new LeftBarProductItemFragment();
		Bundle args = new Bundle();
		args.putInt("orderid", orderid);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orderid = getArguments().getInt("orderid");
		mOrderData = OrderManager.getOrderDataById(orderid);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.leftbarproductiteminfo2, container, false);
		leftbar_detail_back_to1_btn = (Button) layout.findViewById(R.id.leftbar_detail_back_to1);
		printerButton = (Button) layout.findViewById(R.id.leftbarproductitem_printer);
		printerButton.setOnClickListener(this);
		leftbar_detail_back_to1_btn.setOnClickListener(this);
		return layout;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.leftbar_detail_back_to1:
			mLeftBarFragment.backBtnClicked();
			break;
		case R.id.leftbarproductitem_printer:
			if (mOrderData != null) {
				try {
					PrinterManager.getInstance().printOrder(mOrderData.clone(), OrderPrint.ADDTIONAL_MODE);
				} catch (CloneNotSupportedException e) {
					Log.d(TAG, "ERROR IN OrderInfo clone...");
				}
			}
			break;
		default:
			break;
		}
	}

}
