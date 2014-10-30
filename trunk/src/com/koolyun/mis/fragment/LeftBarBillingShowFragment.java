package com.koolyun.mis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.core.order.OrderProcess;
import com.koolyun.mis.core.store.StoreManager;

public class LeftBarBillingShowFragment extends LeftBarBaseFragment {

	View layout = null;
	Button button_back;
	int orderid;
	OrderProcess orderprocess;

	public static LeftBarBillingShowFragment newInstance(int orderprocessid) {
		LeftBarBillingShowFragment frag = new LeftBarBillingShowFragment();
		Bundle args = new Bundle();
		args.putInt("orderid", orderprocessid);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orderid = getArguments().getInt("orderid");
		orderprocess = OrderManager.getOrderProcessByProcessId(orderid);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.leftbarproduct_billing, container, false);
		button_back = (Button) layout.findViewById(R.id.leftbar_product_billing);
		button_back.setOnClickListener(this);

		TextView storename = (TextView) layout.findViewById(R.id.layRefer);
		storename.setText(StoreManager.getStoreName());

		TextView merchantid = (TextView) layout.findViewById(R.id.textMerchant2_text);
		// TODO:
		// merchantid.setText(SettingsManager.queryMerchantCode());
		// TODO:
		TextView terminateid = (TextView) layout.findViewById(R.id.textDeviceCode2_text);
		// terminateid.setText(SettingsManager.queryTerminalCode());

		TextView cardno = (TextView) layout.findViewById(R.id.cardNoTextView2_text);
		cardno.setText("Imbak");

		TextView issuer = (TextView) layout.findViewById(R.id.issuingBankTextView2_text);
		issuer.setText(orderprocess.getIssuerbankId());

		// TextView acqno =
		// (TextView)layout.findViewById(R.id.acqnoTextView2_text);
		// acqno.setText(orderprocess.getAcqbankId());

		TextView tradetype = (TextView) layout.findViewById(R.id.paymentTypeTextView2_text);
		tradetype.setText(getTradeType(orderprocess.getOrderProcessModeId()));

		// TextView batchid =
		// (TextView)layout.findViewById(R.id.batchNoTextView2_text);
		// batchid.setText(orderprocess.getBatchId());

		TextView tradeno = (TextView) layout.findViewById(R.id.certificatenum_text);
		tradeno.setText(orderprocess.getTraceNo());

		TextView authorid = (TextView) layout.findViewById(R.id.authorizationNoTextView2_text);
		authorid.setText(orderprocess.getAuthorizationNo());

		TextView referenceno = (TextView) layout.findViewById(R.id.referenceNoTextVeiew2_text);
		referenceno.setText(orderprocess.getReferenceNo());

		String datetime = orderprocess.getCreateTime();
		String date = datetime.substring(0, 10);
		String time = datetime.substring(10);

		TextView dealdate = (TextView) layout.findViewById(R.id.dealdate_text);
		dealdate.setText(date);

		TextView dealtime = (TextView) layout.findViewById(R.id.dealtime_text);
		dealtime.setText(time);

		return layout;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.leftbar_product_billing:
			mLeftBarFragment.backBtnClicked();
			break;

		default:
			break;
		}
	}

	private String getTradeType(int modeid) {

		String numstr = String.valueOf(modeid);
		// TODO: need fix?
		if (numstr.equals("22"))
			numstr = "消费/SALE";
		else if (numstr.equals("23"))
			numstr = "消费撤销/VOID";
		else if (numstr.equals("25"))
			numstr = "退货/REFUND";

		return numstr;
	}

}
