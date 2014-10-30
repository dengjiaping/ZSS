package com.koolyun.mis;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ScrollView;

import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.fragment.BeginPayFragment;
import com.koolyun.mis.fragment.PayTopBarFragment;
import com.koolyun.mis.fragment.SentTradeInfoFragment;
import com.koolyun.mis.fragment.ShoppingCartFragment;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.util.pay.PayByCash;
import com.koolyun.mis.util.pay.PayByMsrCard;
import com.koolyun.mis.util.pay.PayManager;

public class BillingActivity extends SaleBaseActivity {
	PayTopBarFragment paytopFragment;
	BeginPayFragment beginPayfragment;
	SentTradeInfoFragment sendInfoFragment;
	ShoppingCartFragment saleProductListFragment;
	static ScrollView scrollView = null;
	static int FragmentContainer = R.id.billing_fragmnet_container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DealModel.getInstance().setBillingActivity(this);
		this.setContentView(R.layout.billing_activity);
		scrollView = (ScrollView) findViewById(R.id.billscrolling);

		paytopFragment = (PayTopBarFragment) fragmentManager
				.findFragmentById(R.id.pay_top_bar);
		paytopFragment.setBillingActivity(this);

		saleProductListFragment = (ShoppingCartFragment) fragmentManager
				.findFragmentById(R.id.SaleProductListFragment);

		if (BasicArithmetic.compare(PayManager.getInstance().getMoneyToPay(),
				"0.00") == 0) {
			if (BasicArithmetic.compare(PayManager.getInstance()
					.getMoneyToPay(), "0.00") == 0) {
				PayManager.getInstance().addNewPayment(new PayByCash("0.00"));
			}
		} else {
			
			String bankCardID = getIntent().getStringExtra("bankCardID");
			String txnId = getIntent().getStringExtra("txnId");
			MyLog.i("我的卡号==="+bankCardID);
			if(!bankCardID.equals("")){
				MyLog.i("-----txnId---222---------"+txnId);
				if(txnId != null && !TextUtils.isEmpty(txnId))
				DealModel.getInstance().getOrderData().getCurrentOrder().setTxnId(txnId);
				MyLog.i("-----txnId----111---"+DealModel.getInstance().getOrderData().getCurrentOrder().getTxnId());
				PayManager.getInstance().addNewPayment(new PayByMsrCard(
						DealModel.getInstance().getShoppingCart().getTotalAmount()));
			}else{
				PayManager.getInstance().addNewPayment(new PayByCash(DealModel.getInstance().getShoppingCart().getTotalAmount()));
			}
//			FragmentTransaction fragmentTransaction = fragmentManager
//					.beginTransaction();
//
//			beginPayfragment = new BeginPayFragment();
//			fragmentTransaction.add(FragmentContainer, beginPayfragment);
//			fragmentTransaction.commit();
		}
	}
	
	void addNewFragMent(Fragment mfm) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(FragmentContainer, mfm);

//		fragmentTransaction.setCustomAnimations(R.anim.push_home_page_right_in,
//				R.anim.push_home_page_right_in);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
	}

//	public void addSignFragment() {
//	}
//
//	public void addBeginPayFragment() {
//		paytopFragment.setBtnMode(PayTopBarFragment.SHOW_BAK);
//		beginPayfragment = new BeginPayFragment();
//		beginPayfragment.setBillingActivity(this);
//		addNewFragMent(beginPayfragment);
//
//	}

	public void addSendInfoFragment() {
		paytopFragment.setBtnMode(PayTopBarFragment.SHOW_NEW);
		sendInfoFragment = new SentTradeInfoFragment();
		sendInfoFragment.setBillingActivity(this);
		addNewFragMent(sendInfoFragment);
	}

	public void backBtnClicked() {
		Common.HideKeyboardIfExist(this);
		if (fragmentManager.getBackStackEntryCount() > 0)
			fragmentManager.popBackStack();
		else {
			DealModel.getInstance().setOrderProcessStatus(
					DealModel.ORDER_PROCESS_SELECE);
			DealModel.getInstance().getOrderData().removeOrderDataInDB();
			this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	}

	public void newDeal() {
		DealModel.getInstance().INITOrder();
		this.finish();
	}

	public static ScrollView getScrollView() {
		return scrollView;
	}

	public void setScrollView(ScrollView scrollView) {
		BillingActivity.scrollView = scrollView;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		if (data == null) {
//			Toast.makeText(this, "支付失败！", Toast.LENGTH_LONG).show();
//			return;
//		}
//
//		JSONObject json = null;
//		JSONArray detailList = null;
//		String bankCardID = null;
//		String alipayAccount = null;
//		boolean payTypeTag = false;
//
//		String orderDetail = "------------支付详情------------\n";
//
//		Bundle bundle = data.getExtras();
//		String action = bundle.getString(SmartPosPayEx.ACTION);// 目前无实际用途
//		String result = bundle.getString("result"); // 支付结果
//		MyLog.i("--------------"+result);
//		String totalAmount = formatAmountStr(bundle.getString("totalAmount")); // 需支付金额
// 		String paidAmount = formatAmountStr(bundle.getString("actualAmount")); // 已支付金额
//
//		try {
//			detailList = new JSONArray(bundle.getString("detailList"));
//			MyLog.e("detailList:\n"+detailList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		for (int i = 0; i < detailList.length(); i++) {
//			try {
//				json = detailList.getJSONObject(i);
//				try {
//					bankCardID = json.getString("bankCardNum");
//				} catch (JSONException e) {
//					bankCardID = "";
//				}
//				try {
//					alipayAccount = json.getString("alipayAccount");
//				} catch (JSONException e) {
//					alipayAccount = "";
//				}
//				if (!(alipayAccount.equals(""))) {
//					payTypeTag = false;
//				} else if (!bankCardID.equals("") && alipayAccount.equals("")) {
//					payTypeTag = true;
//				}
//				if(!bankCardID.equals("")){
//					if (payTypeTag) {
//						orderDetail = orderDetail + "第" + (i + 1) + "笔：" + "支付金额："
//								+ formatAmountStr(json.getString("transAmount"))
//								+ "元" + "，参考号：" + json.getString("refNo")
//								+ "，支付时间：" + json.getString("payTime") + "，银行卡号："
//								+ json.getString("bankCardNum") + "，发卡行："
//								+ json.getString("issuerName") + "，支付类型："
//								+ json.getString("payTypeDesc") + "，支付状态："
//								+ json.getString("orderStateDesc") + "\n";
//					} else {
//						orderDetail = orderDetail + "第" + (i + 1) + "笔：" + "支付金额："
//								+ formatAmountStr(json.getString("transAmount"))
//								+ "元" + "，参考号：" + json.getString("refNo")
//								+ "，支付时间：" + json.getString("transTime")
//								+ "，支付宝账户：" + json.getString("alipayAccount")
//								+ "，PID：" + json.getString("alipayPID") + "，交易号："
//								+ json.getString("alipayTransactionID") + "，NO："
//								+ json.getString("orderID") + "，支付类型："
//								+ json.getString("transType") + "，支付状态："
//								+ json.getString("orderStateDesc") + "\n";
//					}
//				}
//				if(result.equals("0")) {
//					Toast.makeText(this, "支付失败！", Toast.LENGTH_LONG).show();
//					return;
//				}
//				MyLog.e("detailList:\n"+"成功");
//				OrderProcess mOrderProcess = new OrderProcess();
//				mOrderProcess.setAckNo(/*json.getString("RespCode")*/"");
//				mOrderProcess.setAcqbankId(/*json.getString("Acquirer")*/"");
//				mOrderProcess.setAddressID(-1);
//				mOrderProcess.setAuthorizationNo(/*json.getString("AuthorizationNo")*/"");
//				mOrderProcess.setBatchId(/*json.getString("BatchNo")*/"");
//				mOrderProcess.setClearingDate(/*json.getString("ClearDate")*/"");
//				mOrderProcess.setCreateTime(json.getString("transTime"));
//				mOrderProcess.setIssuerbankId(/*json.getString("Issuer")*/"");
//				mOrderProcess.setOrderID(/*DealModel.getInstance().getOrderData().
//						getCurrentOrder().getOrderID()*/-1);
////							mOrderProcess.setOrderProcessId(1);// 自增长id
//				mOrderProcess.setOrderProcessModeId(3);// TODO
//				mOrderProcess.setPrice(json.getString("transAmount"));
//				mOrderProcess.setReferenceNo(json.getString("refNo"));
//				mOrderProcess.setTraceNo(/*json.getString("TraceNo")*/"");
//				
//				OrderManager.saveOrderProcess(mOrderProcess);
//				// 交易成功，保存数据导入数据库
//				if(!bankCardID.equals("")){
//					PayManager.getInstance().addNewPayment(new PayByMsrCard(
//							DealModel.getInstance().getShoppingCart().getTotalAmount()));
//				}else{
//					PayManager.getInstance().addNewPayment(new PayByCash(DealModel.getInstance().getShoppingCart().getTotalAmount()));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				Toast.makeText(this, "支付失败！", Toast.LENGTH_LONG).show();
//			}
//		}
//
//		// 返回支付结果以及详细流水
////		Toast.makeText(this,
////				"action:" + action + "\n" + "------------支付结果------------\n"
////						+ "支付标志:" + result + "\n" + "应付金额:" + totalAmount + "元"
////						+ "\n" + "已付金额:" + paidAmount + "元" + "\n"
////						+ orderDetail, Toast.LENGTH_LONG).show();
//		MyLog.e("action:" + action + "\n" + "------------支付结果------------\n"
//						+ "支付标志:" + result + "\n" + "应付金额:" + totalAmount + "元"
//						+ "\n" + "已付金额:" + paidAmount + "元" + "\n"
//						+ orderDetail);
//	}
//
//	// 金额单位转换 分转为元
//	private String formatAmountStr(String amount) {
//		String floatAmount = null;
//		int length = amount.length();
//		if (length == 0) {
//			floatAmount = "";
//		} else if (length == 1) {
//			floatAmount = "0.0" + amount;
//		} else if (length == 2) {
//			floatAmount = "0." + amount;
//		} else {
//			floatAmount = amount.substring(0, length - 2) + "."
//					+ amount.substring(length - 2, length);
//		}
//		return floatAmount;
//	}
}
