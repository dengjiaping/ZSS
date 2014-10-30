package com.koolyun.mis.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.aidl.IPaySDK;
import com.koolyun.mis.constants.MyConstants;
import com.koolyun.mis.core.CardPayInfos;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.CardPayInfos.OrderProcessMode;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.core.order.OrderProcess;
import com.koolyun.mis.core.store.StoreManager;
import com.koolyun.mis.finance.PosData;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.util.NumberFormater;
import com.koolyun.mis.util.QuickMoney;
import com.koolyun.mis.util.pay.PayByCash;
import com.koolyun.mis.util.pay.PayByMsrCard;
import com.koolyun.mis.util.pay.PayManager;
import com.koolyun.mis.util.pay.SmartPosPayEx;
import com.koolyun.mis.widget.QuickMoneySelector;

public class BeginPayFragment extends BillingFragmentBase implements OnClickListener, TextWatcher {
	Button swapcard;
	EditText moneyInput;
	Button mInputBtn;
	LinearLayout quickLayout;
	QuickMoneySelector mQuickMoneySelector;
	private String payResult;
	private IPaySDK iPay;

	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			iPay = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// IPaySDK.Stub.asInterface，获取接口
			iPay = IPaySDK.Stub.asInterface(service);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		intent.setAction("com.cynovo.sirius.Service.permission.CLOUDPAY");
		getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.begin_pay_fragment, container, false);
		swapcard = (Button) v.findViewById(R.id.swap_card_btn);
		swapcard.setOnClickListener(this);
		mInputBtn = (Button) v.findViewById(R.id.money_input_btn);
		mInputBtn.setFocusable(false);
		mInputBtn.setOnClickListener(this);
		mInputBtn.setClickable(false);

		quickLayout = (LinearLayout) v.findViewById(R.id.quick_money);
		List<Double> tmpList = QuickMoney.getQuickMoneyList(Double.parseDouble(DealModel.getInstance()
				.getShoppingCart().getTotalAmount()));
		mQuickMoneySelector = new QuickMoneySelector(this.getActivity(), tmpList);
		quickLayout.addView(mQuickMoneySelector);

		moneyInput = (EditText) v.findViewById(R.id.money_input);
		moneyInput.addTextChangedListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		if (JavaUtil.isFastDoubleClick())
			return;
		switch (v.getId()) {
		case R.id.money_input_btn:
			if (moneyInput.getText().toString() != null && !moneyInput.getText().toString().isEmpty()) {
				Common.HideKeyboardIfExist(this);
				SmartPosPayEx.startPay(getActivity(), DealModel.getInstance().getShoppingCart().getTotalAmount()
						.replace(".", ""));
				//PayManager.getInstance().addNewPayment(new PayByCash(moneyInput.getText().toString()));
			}
			break;
		case R.id.swap_card_btn:
			try {
				if (MyConstants.PAY_TYPE == 1) {
					// 支付，未指定“支付机构”和“支付方式”
					SmartPosPayEx.startPay(getActivity(), DealModel.getInstance().getShoppingCart().getTotalAmount()
							.replace(".", ""));
					// 支付两分钱，另外一种调用方式，未指定“支付机构”和“支付方式”，
					// SmartPosPayEx.startPay(DemoPayActivity.this, null, "2",
					// null, null);
				} else if (MyConstants.PAY_TYPE == 0) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("ReqTransAmount", DealModel.getInstance().getShoppingCart().getTotalAmount());
					SimpleDateFormat sdf1 = new SimpleDateFormat("MMdd", Locale.getDefault());
					SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss", Locale.getDefault());
					jsonObject.put("ReqTransDate", sdf1.format(new Date()));
					jsonObject.put("ReqTransTime", sdf2.format(new Date()));
					jsonObject.put("ReqTransOperator", "1");
					jsonObject.put("ReqsTransDate", sdf1.format(new Date()));
					jsonObject.put("ReqsTraceNo", "1");
					jsonObject.put("ReqTraceNo", ""
							+ MySharedPreferencesEdit.getInstancePublic(getActivity()).getTraceNO()); // test
																										// data
					MySharedPreferencesEdit.getInstancePublic(getActivity()).setTraceNO(
							MySharedPreferencesEdit.getInstancePublic(getActivity()).getTraceNO() + 1); // test
																										// data
					jsonObject.put("ReqTransType", 0); // test data
					jsonObject.put("cardType", 0); // test data
					jsonObject.put("skinType", 0); // test data

					payResult = iPay.payCash(jsonObject.toString());
					if (payResult != null && payResult.length() > 0) {
						Toast.makeText(getActivity(), payResult, Toast.LENGTH_LONG).show();
						Log.e("payResult", payResult);
						// 交易成功，保存数据导入数据库
						PayManager.getInstance().addNewPayment(
								new PayByMsrCard(DealModel.getInstance().getShoppingCart().getTotalAmount()));

						JSONObject json = new JSONObject(payResult);
						OrderProcess mOrderProcess = new OrderProcess();
						mOrderProcess.setAckNo(json.getString("RespCode"));
						mOrderProcess.setAcqbankId(json.getString("Acquirer"));
						mOrderProcess.setAddressID(-1);
						mOrderProcess.setAuthorizationNo(json.getString("AuthorizationNo"));
						mOrderProcess.setBatchId(json.getString("BatchNo"));
						mOrderProcess.setClearingDate(json.getString("ClearDate"));
						mOrderProcess.setCreateTime(json.getString("RespTransDate") + json.getString("RespTransTime"));
						mOrderProcess.setIssuerbankId(json.getString("Issuer"));
						mOrderProcess.setOrderID(DealModel.getInstance().getOrderData().getCurrentOrder().getOrderID());
						// mOrderProcess.setOrderProcessId(1);// 自增长id
						mOrderProcess.setOrderProcessModeId(3);// TODO
						mOrderProcess.setPrice(json.getString("ReqTransAmount"));
						mOrderProcess.setReferenceNo(json.getString("ReferenceNo"));
						mOrderProcess.setTraceNo(json.getString("TraceNo"));

						OrderManager.saveOrderProcess(mOrderProcess);
					} else {
						Toast.makeText(getActivity(), R.string.pay_error, Toast.LENGTH_SHORT).show();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public int saveDealData(OrderProcess mOrderProcess) {
		CardPayInfos mCardPayInfos = DealModel.getInstance().getOrderData().AddCardPayInfos();
		mCardPayInfos.setOrderProcess(new OrderProcess(-1, OrderProcessMode.ORDER_PROCESS_EMV_SALE.toInt(),
				mOrderProcess.getOrderID(), StoreManager.getAddress().getAddressID(), PosData.getPosDataInstance()
						.getDate() + PosData.getPosDataInstance().getTime(), PosData.getPosDataInstance().getTraceNo(),
				PosData.getPosDataInstance().getReferencNo(), PosData.getPosDataInstance().getAuthorizationNo(),
				PosData.getPosDataInstance().getClearingDate(), PosData.getPosDataInstance().getAckNo(), PosData
						.getPosDataInstance().getAmount(), PosData.getPosDataInstance().getIssuer(), PosData
						.getPosDataInstance().getAcq(), PosData.getPosDataInstance().getBatchid()));

		mCardPayInfos.saveCardPayInfos();
		return 0;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (BasicArithmetic.compare(NumberFormater.currencyFormat(moneyInput.getText().toString()), DealModel
				.getInstance().getOrderData().getAmount()) >= 0) {
			mInputBtn.setClickable(true);
			mInputBtn.setBackgroundResource(R.drawable.btn_bg_xml);
			mInputBtn.setTextColor(0xffffffff);
		} else {
			mInputBtn.setClickable(false);
			mInputBtn.setBackgroundResource(R.drawable.green_btn);
			mInputBtn.setTextColor(0xff818181);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void onDestroy() {
		if (conn != null) {
			getActivity().unbindService(conn);
		}
		iPay = null;
		super.onDestroy();
	}
}
