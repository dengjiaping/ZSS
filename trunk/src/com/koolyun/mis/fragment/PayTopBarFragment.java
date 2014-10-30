package com.koolyun.mis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.util.ViewHelper;
import com.koolyun.mis.widget.MoneyView;

public class PayTopBarFragment extends BillingFragmentBase implements OnClickListener {
	Button topBarBack;
	Button topBarNew;
	TextView mStoreInfo;
	MoneyView mTotalAmount;
	public static final int SHOW_BAK = 0;
	public static final int SHOW_NEW = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.pay_top_fragment, container, false);
		topBarBack = (Button) v.findViewById(R.id.pay_bak_btn);
		topBarBack.setOnClickListener(this);
		topBarNew = (Button) v.findViewById(R.id.new_order);
		topBarNew.setOnClickListener(this);

		mTotalAmount = (MoneyView) v.findViewById(R.id.total_money_text);
		mStoreInfo = (TextView) v.findViewById(R.id.user_info);

		return v;
	}

	public void setBtnMode(int mode) {
		if (mode == SHOW_BAK) {
			topBarBack.setVisibility(View.VISIBLE);
			topBarNew.setVisibility(View.INVISIBLE);
		} else if (mode == SHOW_NEW) {
			//topBarBack.setVisibility(View.INVISIBLE);
			//topBarNew.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewHelper.setStoreInfo(this.getActivity(), mStoreInfo);
		setTotalMoney();
	}

	@Override
	public void onClick(View v) {
		if (JavaUtil.isFastDoubleClick())
			return;
		switch (v.getId()) {
		case R.id.pay_bak_btn:
			mBilling.newDeal();
			//mBilling.finish();
			//mBilling.backBtnClicked();
			break;
		case R.id.new_order:
			mBilling.newDeal();
			break;
		}
	}

	private void setTotalMoney() {
		if (mTotalAmount != null)
			mTotalAmount.setMoney(DealModel.getInstance().getShoppingCart().getTotalAmount());
	}
}
