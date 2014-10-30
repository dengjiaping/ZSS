package com.koolyun.mis.fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.MyViewPagerAdapter;
import com.koolyun.mis.adapter.SwitchAdapter;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.core.order.OrderRemark;
import com.koolyun.mis.myinterface.OnUpdateOnsale;
import com.koolyun.mis.widget.AnyWhereDialog;

public class OrderRemarkFragment extends AbstractDialogFragment implements TextWatcher, DialogInterface.OnKeyListener,
		OnUpdateOnsale {

	AnyWhereDialog mDialog = null;
	EditText mRemarkText = null;
	EditText mSitIndex = null;
	private ViewPager viewPager;
	private View view1, view2;// 各个页卡
	private List<View> views;// Tab页面列表
	private Button button1, button2;
	private ListView orderOnsaleList = null;
	SwitchAdapter mSwitchAdapter = null;

	public static OrderRemarkFragment newInstance(int orderId) {
		OrderRemarkFragment frag = new OrderRemarkFragment();
		Bundle args = new Bundle();
		args.putInt("orderID", orderId);
		frag.setArguments(args);
		return frag;
	}

	public OrderRemarkFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mDialog = new AnyWhereDialog(getActivity(), 580, 770, -110, 100, R.layout.order_moreinfo_dialog,
				R.style.Theme_dialog1, Gravity.CENTER, true);
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mDialog.setOnKeyListener(this);
		viewPager = (ViewPager) mDialog.findViewById(R.id.orderMoreInfoPager);
		views = new ArrayList<View>();
		view1 = inflater.inflate(R.layout.order_remark_dialog, null);
		view2 = inflater.inflate(R.layout.order_onsale_dialog, null);
		views.add(view1);
		views.add(view2);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mRemarkText = (EditText) view1.findViewById(R.id.remark_content);
		mRemarkText.addTextChangedListener(this);
		mSitIndex = (EditText) view1.findViewById(R.id.edit_sit_index);
		mSitIndex.addTextChangedListener(this);

		orderOnsaleList = (ListView) view2.findViewById(R.id.order_onsale_listview);
		mSwitchAdapter = new SwitchAdapter(getActivity(), this, DealModel.getInstance().getShoppingCart().getOnsale());
		orderOnsaleList.setAdapter(mSwitchAdapter);
		mSwitchAdapter.updateOnePrice(DealModel.getInstance().getShoppingCart().getTotalAmount());

		button1 = (Button) mDialog.findViewById(R.id.order_more_info);
		button2 = (Button) mDialog.findViewById(R.id.order_onsale_info);
		String[] array = getActivity().getResources().getStringArray(R.array.order_detail_title_array);
		button1.setText(array[0]);
		button2.setText(array[1]);
		button1.setOnClickListener(new MyOnClickListener(0));
		button2.setOnClickListener(new MyOnClickListener(1));

		OrderRemark mOrderRemark = DealModel.getInstance().getOrderData().getOrderRemark();
		if (mOrderRemark != null && !mOrderRemark.getRemark().isEmpty()) {
			mRemarkText.setText(mOrderRemark.getRemark());
		}
		if (mOrderRemark != null && !mOrderRemark.getSitIndex().isEmpty()) {
			mSitIndex.setText(mOrderRemark.getSitIndex());
			mSitIndex.setSelectAllOnFocus(true);
		}
		return mDialog;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		DealModel.getInstance().getOrderData()
				.setOrderRemark(new OrderRemark(-1, mRemarkText.getText().toString(), mSitIndex.getText().toString()));
	}

	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		return false;
	}

	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0: // page 0
				button1.setTextColor(getActivity().getResources().getColor(android.R.color.black));
				button2.setTextColor(getActivity().getResources().getColor(android.R.color.darker_gray));
				button1.setBackgroundResource(R.drawable.statistics_selector2);
				button2.setBackgroundResource(R.drawable.statistics_selector);
				break;
			case 1: // page 1
				button2.setTextColor(getActivity().getResources().getColor(android.R.color.black));
				button1.setTextColor(getActivity().getResources().getColor(android.R.color.darker_gray));
				button2.setBackgroundResource(R.drawable.statistics_selector2);
				button1.setBackgroundResource(R.drawable.statistics_selector);
				if (mSwitchAdapter != null) {
					mSwitchAdapter.updateOnePrice(DealModel.getInstance().getShoppingCart().getTotalAmount());
				}
				break;
			}
		}
	}

	@Override
	public void onOnSaleChanged(List<Onsale> mOnsaleList) {
		DealModel.getInstance().setOnsale(mOnsaleList);
		if (mSwitchAdapter != null) {
			mSwitchAdapter.updateOnePrice(DealModel.getInstance().getShoppingCart().getTotalAmount());
		}
	}
}
