package com.koolyun.mis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.koolyun.mis.constants.MyConstants;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.core.order.OrderProcess;
import com.koolyun.mis.fragment.CheckSelfFragment;
import com.koolyun.mis.fragment.DataManagerFragment;
import com.koolyun.mis.fragment.LeftBarDealInfoFragment;
import com.koolyun.mis.fragment.OrderRemarkFragment;
import com.koolyun.mis.fragment.SettingFragment;
import com.koolyun.mis.fragment.ShoppingFragment;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.CreatDialogClass;
import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.util.pay.SmartPosPayEx;

public class SaleActivity extends SaleBaseActivity implements OnLongClickListener, DrawerListener {
	private static int checkTimes = 0;
	private ViewFlipper mSaleListViewFlipper = null;
	private DrawerLayout mDrawerLayout = null;
	private boolean isCheckFlag = false;
	private boolean otherFlag = false;
	Handler handler = new Handler();
	private volatile boolean enableCheck = false;
	Fragment shoppingFragment = null;
	Fragment productManagerFragment = null;
	SettingFragment settingFragment = null;
	Fragment currentFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.sale_activity);

		shoppingFragment = new ShoppingFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.main_container, shoppingFragment);
		//transaction.addToBackStack(null);
		transaction.commit();
		currentFragment = shoppingFragment;
		radioGroup.setOnCheckedChangeListener(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(this);
		DealModel.getInstance().SetContext(this);

		checkTimes = 0;
		handler.postDelayed(runnableUi, 100);
		handler.postDelayed(mDeviceCheck, 10);
	}

	public Fragment getCurrentFragment() {
		return currentFragment;
	}

	private void enableButton(boolean status) {
		// billingBtn.setClickable(status);
		// hangUpBtn.setClickable(status);
	}

	Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			enableCheck = true;
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		enableButton(true);
	}

	Runnable mDeviceCheck = new Runnable() {
		@Override
		public void run() {
			if (checkTimes == 0) {
				checkTimes++;
				if (MyConstants.SHOULD_CHECK_SELF == 0) {
					SaleActivity.this.showCheckDialog();
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(runnableUi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (MyConstants.SHOULD_CHECK_SELF == 0) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.check_status, menu);
		}
		return true;
	}

	public void hideDialog() {
		mDrawerLayout.closeDrawers();
		radioBtnArray[3].setChecked(true);
	}

	protected void user_account_setup() {
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
			mDrawerLayout.closeDrawers();
		new CreatDialogClass(this).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_checkself && enableCheck
				&& !CloudPosApp.getInstance().isCheckDeviceInProcess()) {
			showCheckDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	private void showCheckDialog() {
		if (CloudPosApp.getInstance().isWorkkeyInprocess())
			return;
		CheckSelfFragment checkSelf = CheckSelfFragment.newInstance(0);
		checkSelf.show(getFragmentManager(), "dialog");
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (JavaUtil.isFastDoubleClick())
			return;
		switch (v.getId()) {
		case R.id.hangup_order_btn:
			enableButton(false);
			mSaleListViewFlipper.setDisplayedChild(0);
			DealModel.getInstance().HangUpCurrentOrder();
			break;
		case R.id.salelisttotalmoney:
			enableButton(false);
			DealModel.getInstance().beginToPayTheOrder();
			break;
		case R.id.sale_recent:
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
				mDrawerLayout.closeDrawers();
			else
				mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.productmanager:
			Intent nextIntent = new Intent(this, ProductManagerActivity.class);
			Bundle translateBundle = ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in,
					android.R.anim.fade_out).toBundle();
			this.startActivity(nextIntent, translateBundle);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.salelisttotalmoney:
		case R.id.hangup_order_btn:
			OrderRemarkFragment mOrderRemarkFragment = OrderRemarkFragment.newInstance(-1);
			mOrderRemarkFragment.show(getFragmentManager(), "dialog");
		}
		return false;
	}

	@Override
	public void onDrawerClosed(View arg0) {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				if (!otherFlag) {
					isCheckFlag = true;
					imgArray[0].setVisibility(View.VISIBLE);
					radioBtnArray[0].setChecked(false);
					if(currentFragment instanceof ShoppingFragment){
						radioBtnArray[3].setChecked(true);
					}else if(currentFragment instanceof DataManagerFragment){
						radioBtnArray[1].setChecked(true);
					}else if(currentFragment instanceof SettingFragment){
						radioBtnArray[2].setChecked(true);
					}
				} else {
					otherFlag = false;
				}
			}
		});

	}

	private boolean IsFragment(String name) {
		FragmentManager mfragmentManager = getFragmentManager();
		Fragment fragment = mfragmentManager.findFragmentById(R.id.leftbar_container);
		if (fragment != null && fragment.getClass().getSimpleName().equals(name))
			return true;
		return false;
	}

	@Override
	public void onDrawerOpened(View arg0) {
		if (IsFragment("LeftBarDealInfoFragment")) {
			LeftBarDealInfoFragment mLeftBarDealInfoFragment = (LeftBarDealInfoFragment) getFragmentManager()
					.findFragmentById(R.id.leftbar_container);
			mLeftBarDealInfoFragment.updateList();
		}
		if (!radioBtnArray[0].isChecked()) {
			radioBtnArray[0].setChecked(true);
		}
	}

	@Override
	public void onDrawerSlide(View arg0, float arg1) {
	}

	@Override
	public void onDrawerStateChanged(int arg0) {

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(currentFragment != null && group.getCheckedRadioButtonId() != R.id.recentRecord){
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.hide(currentFragment);
			transaction.commit();
		}
				
		switch (group.getCheckedRadioButtonId()) {
			case R.id.recentRecord:
				if (isCheckFlag) {
					isCheckFlag = !isCheckFlag;
					break;
				}
	
				if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
					MyLog.i("onCheckedChanged");
					mDrawerLayout.openDrawer(Gravity.LEFT);
					imgArray[0].setVisibility(View.INVISIBLE);
	
				} else {
					imgArray[0].setVisibility(View.INVISIBLE);
					imgArray[1].setVisibility(View.VISIBLE);
				}
				MyLog.i("onCheckedChanged566");
				radioBtnArray[0].setChecked(true);
				imgArray[1].setVisibility(View.VISIBLE);
				break;
			case R.id.merchantManager:
				if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
					otherFlag = true;
					mDrawerLayout.closeDrawers();
					isCheckFlag = false;
				}
				imgArray[0].setVisibility(View.INVISIBLE);
				imgArray[1].setVisibility(View.INVISIBLE);
	//			FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
	//			transaction1.hide(shoppingFragment).commit();
	//			//隐藏的settingFragment
	//			if(settingFragment != null){
	//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
	//				transaction.hide(settingFragment);
	//				transaction.commit();
	//			}
	//			//隐藏掉ShoppingFragment
	//			if(shoppingFragment != null){
	//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
	//				transaction.hide(shoppingFragment);
	//				transaction.commit();
	//			}
				
				//添加ProductmanagerFragment
				if(productManagerFragment == null){
					productManagerFragment = new DataManagerFragment();
					//Fragment shoppingFragment = new ShoppingFragment();
					
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.add(R.id.main_container, productManagerFragment);
					//transaction.addToBackStack(null);
					transaction.commit();
				}else{
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.show(productManagerFragment);
					//transaction.addToBackStack(null);
					transaction.commit();
				}
				currentFragment=productManagerFragment;
				break;
			case R.id.recentSetting:
				if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
					otherFlag = true;
					mDrawerLayout.closeDrawers();
					isCheckFlag = false;
				}
				imgArray[0].setVisibility(View.VISIBLE);
				imgArray[1].setVisibility(View.INVISIBLE);
				
	//			//把productManagerFragment 隐掉
	//			if(productManagerFragment != null){
	//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
	//				transaction.hide(productManagerFragment);
	//				transaction.commit();
	//			}
	//			//隐藏掉ShoppingFragment
	//			if(shoppingFragment != null){
	//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
	//				transaction.hide(shoppingFragment);
	//				transaction.commit();
	//			}
				
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				if(settingFragment == null){
					settingFragment = new SettingFragment();
					transaction.add(R.id.main_container, settingFragment);
				}else{
					transaction.show(settingFragment);
				}
				transaction.commit();
				currentFragment=settingFragment;
				break;
			case R.id.shoppingPage:
				//隐藏掉ProductManagerFragment
	//			if(productManagerFragment != null){
	//				FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
	//				transaction2.hide(productManagerFragment);
	//				transaction2.commit();
	//			}
	//			//隐藏掉ShoppingFragment
	//			if(settingFragment != null){
	//				FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
	//				transaction2.hide(settingFragment);
	//				transaction2.commit();
	//			}
				
				if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
					otherFlag = true;
					mDrawerLayout.closeDrawers();
					isCheckFlag = false;
				}
				FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
				transaction2.show(shoppingFragment);
				transaction2.commit();
				currentFragment=shoppingFragment;
	
				break;
			}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data == null) {
			DealModel.getInstance().setOrderProcessStatus(
					DealModel.ORDER_PROCESS_SELECE);
			DealModel.getInstance().getOrderData().removeOrderDataInDB();
			Toast.makeText(this, "支付失败！", Toast.LENGTH_LONG).show();
			return;
		}

		JSONObject json = null;
		JSONArray detailList = null;
		String bankCardID = null;
		String alipayAccount = null;
		boolean payTypeTag = false;

		String orderDetail = "------------支付详情------------\n";

		Bundle bundle = data.getExtras();
		String action = bundle.getString(SmartPosPayEx.ACTION);// 目前无实际用途
		if(!TextUtils.isEmpty(action) && action.equalsIgnoreCase(SmartPosPayEx.ACTION_PAY)){
			String result = bundle.getString("result"); // 支付结果
			MyLog.i("--------------"+result);
			String totalAmount = formatAmountStr(bundle.getString("totalAmount")); // 需支付金额
	 		String paidAmount = formatAmountStr(bundle.getString("actualAmount")); // 已支付金额
	
			try {
				detailList = new JSONArray(bundle.getString("detailList"));
				MyLog.e("detailList:\n"+detailList);
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			for (int i = 0; i < detailList.length(); i++) {
				try {
					json = detailList.getJSONObject(i);
					try {
						bankCardID = json.getString("accountNo");
					} catch (JSONException e) { 
						bankCardID = ""; 
					}
					try {
						alipayAccount = json.getString("alipayAccount");
					} catch (JSONException e) {
						alipayAccount = "";
					}
					if (!(alipayAccount.equals(""))) {
						payTypeTag = false;
					} else if (!bankCardID.equals("") && alipayAccount.equals("")) {
						payTypeTag = true;
					}
					if(!bankCardID.equals("")){
						if (payTypeTag) {
							orderDetail = orderDetail + "第" + (i + 1) + "笔：" + "支付金额："
									+ formatAmountStr(json.optString("transAmount"))
									+ "元" + "，参考号：" + json.optString("refNo")
									+ "，支付时间：" + json.optString("transTime") + "，银行卡号："
									+ json.getString("accountNo") + "，发卡行："
									+ json.optString("issuerName") + "，支付类型："
									+ json.optString("transType") + "，支付状态："
									+ json.optString("orderStateDesc") + ", txnId: " 
									+ json.optString("txnId")
									+ "\n";
						} else {
							orderDetail = orderDetail + "第" + (i + 1) + "笔：" + "支付金额："
									+ formatAmountStr(json.optString("transAmount"))
									+ "元" + "，参考号：" + json.optString("refNo")
									+ "，支付时间：" + json.optString("transTime")
									+ "，支付宝账户：" + json.optString("alipayAccount")
									+ "，PID：" + json.optString("alipayPID") + "，交易号："
									+ json.optString("alipayTransactionID") + "，NO："
									+ json.optString("orderID") + "，支付类型："
									+ json.optString("transType") + "，支付状态："
									+ json.optString("orderStateDesc") + "\n";
//							orderDetail = orderDetail + "第" + (i + 1) + "笔：" + "支付金额："
//									+ formatAmountStr(json.getString("transAmount"))
//									+ "元" + "，参考号：" + json.getString("refNo")
//									+ "，支付时间：" + json.getString("transTime")
//									+ "，支付宝账户：" + json.getString("alipayAccount")
//									+ "，PID：" + json.getString("alipayPID") + "，交易号："
//									+ json.getString("alipayTransactionID") + "，NO："
//									+ json.getString("orderID") + "，支付类型："
//									+ json.getString("transType") + "，支付状态："
//									+ json.getString("orderStateDesc") + "\n";
						}
					}
					if(result.equals("0")) {
						DealModel.getInstance().setOrderProcessStatus(
								DealModel.ORDER_PROCESS_SELECE);
						DealModel.getInstance().getOrderData().removeOrderDataInDB();
						Toast.makeText(this, "支付失败！", Toast.LENGTH_LONG).show();
						return;
					}
					MyLog.e("detailList:\n"+"成功");
					OrderProcess mOrderProcess = new OrderProcess();
					mOrderProcess.setAckNo(/*json.getString("RespCode")*/"");
					mOrderProcess.setAcqbankId(/*json.getString("Acquirer")*/"");
					mOrderProcess.setAddressID(-1);
					mOrderProcess.setAuthorizationNo(/*json.getString("AuthorizationNo")*/"");
					mOrderProcess.setBatchId(/*json.getString("BatchNo")*/"");
					mOrderProcess.setClearingDate(/*json.getString("ClearDate")*/"");
					mOrderProcess.setCreateTime(json.getString("transTime"));
					mOrderProcess.setIssuerbankId(/*json.getString("Issuer")*/"");
					mOrderProcess.setOrderID(/*DealModel.getInstance().getOrderData().
							getCurrentOrder().getOrderID()*/-1);
	//							mOrderProcess.setOrderProcessId(1);// 自增长id
					mOrderProcess.setOrderProcessModeId(3);// TODO
					mOrderProcess.setPrice(json.getString("transAmount"));
					mOrderProcess.setReferenceNo(json.getString("refNo"));
					mOrderProcess.setTraceNo(/*json.getString("TraceNo")*/"");
					
					OrderManager.saveOrderProcess(mOrderProcess);
					
					Intent nextIntent = new Intent(SaleActivity.this, BillingActivity.class);
					nextIntent.putExtra("bankCardID", bankCardID);
					nextIntent.putExtra("txnId", json.getString("txnId"));
					Bundle translateBundle = ActivityOptions.makeCustomAnimation(SaleActivity.this, android.R.anim.fade_in,
							android.R.anim.fade_out).toBundle();
					startActivity(nextIntent, translateBundle);
					
					// 交易成功，保存数据导入数据库
	//				if(!bankCardID.equals("")){
	//					PayManager.getInstance().addNewPayment(new PayByMsrCard(
	//							DealModel.getInstance().getShoppingCart().getTotalAmount()));
	//				}else{
	//					PayManager.getInstance().addNewPayment(new PayByCash(DealModel.getInstance().getShoppingCart().getTotalAmount()));
	//				}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "支付失败！", Toast.LENGTH_LONG).show();
				}
			}
	
			// 返回支付结果以及详细流水
	//		Toast.makeText(this,
	//				"action:" + action + "\n" + "------------支付结果------------\n"
	//						+ "支付标志:" + result + "\n" + "应付金额:" + totalAmount + "元"
	//						+ "\n" + "已付金额:" + paidAmount + "元" + "\n"
	//						+ orderDetail, Toast.LENGTH_LONG).show();
			MyLog.e("action:" + action + "\n" + "------------支付结果------------\n"
							+ "支付标志:" + result + "\n" + "应付金额:" + totalAmount + "元"
							+ "\n" + "已付金额:" + paidAmount + "元" + "\n"
							+ orderDetail);
			}else if (!TextUtils.isEmpty(action) && action.equalsIgnoreCase(SmartPosPayEx.ACTION_REVERSE)) {
				
				String refNo = bundle.getString("refNo");
				String reverse_status = bundle.getString("reverse_status");
				String orderStateDesc = bundle.getString("orderStateDesc");
				String transTime = bundle.getString("transTime");
				String operatorName = bundle.getString("operatorName");
				String paymentId = bundle.getString("paymentId");
				String paymentName = bundle.getString("paymentName");
				// 返回支付结果以及详细流水
				MyLog.i("action:" + action + "\n"
						+ "------------撤消结果------------\n" + "撤消标志:" + reverse_status + "\n"
						+ "撤消描述:" + orderStateDesc + "\n" + "交易时间:" + transTime  + "\n"
						+ "操作员:" + operatorName + "\n" + "支付活动号:" + paymentId  + "\n" + "支付活动名称：" + paymentName
						);
			}
	}

	// 金额单位转换 分转为元
	private String formatAmountStr(String amount) {
		String floatAmount = null;
		int length = amount.length();
		if (length == 0) {
			floatAmount = "";
		} else if (length == 1) {
			floatAmount = "0.0" + amount;
		} else if (length == 2) {
			floatAmount = "0." + amount;
		} else {
			floatAmount = amount.substring(0, length - 2) + "."
					+ amount.substring(length - 2, length);
		}
		return floatAmount;
	}

}
