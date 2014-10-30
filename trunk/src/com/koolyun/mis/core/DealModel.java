package com.koolyun.mis.core;

import java.lang.ref.WeakReference;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.koolyun.mis.BillingActivity;
import com.koolyun.mis.SaleActivity;
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.order.OrderData;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.core.order.OrderData.OrderStatus;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.Printer.OrderPrint;
import com.koolyun.mis.util.Printer.PrinterManager;
import com.koolyun.mis.util.pay.PayBase;
import com.koolyun.mis.util.pay.PayManager;

public class DealModel {

	static final String TAG = "DealModel";
	// 交易进行 中状态
	public static final int ORDER_PROCESS_INIT = 10;
	public static final int ORDER_PROCESS_SELECE = 11;
	public static final int ORDER_PROCESS_PAYMENT = 12;
	public static final int ORDER_PROCESS_SENDINFO = 13;

	// Final status
	// 正常状态
	public static final int ORDER_FINA_STATUS_NORMAL = 1;
	// 废单状态
	public static final int ORDER_FINA_STATUS_WASTE = 2;
	// 挂单已付款
	public static final int ORDER_FINA_STATUS_HANGUPP = 3;
	// 挂单未付款
	public static final int ORDER_FINA_STATUS_HANGUPU = 4;
	// 部分退款
	public static final int ORDER_FINA_STATUS_PART_REFUND = 5;
	// 全额退款
	public static final int ORDER_FINA_STATUS_REFUND = 6;

	// 全额付款
	public static final int PAY_ALL = 0;
	// 部分付款
	public static final int PAY_PART = 1;
	// 未付款
	public static final int PAY_NOT = 2;

	public interface Callbacks {
		public void addOrUpdateItems(OrderContentData mOrderContent);

		public void addNewItems(List<OrderContentData> mList);

		public void removeItems(OrderContentData mOrderContent);

		public void removeUnused();

		public void removeCurrent();

		public void updateItems(OrderContentData mContent);

		public void removeAllItems();

		public String getTotalAmount();
	}

	private WeakReference<Callbacks> mCallbacks;
	private OrderData OrderInfo = new OrderData();
	private int orderProcessStatus = ORDER_PROCESS_INIT;
	private final Object mLock = new Object();
	private static ShoppingCart mShoppingCart = new ShoppingCart();
	private static PayManager mPayManager = PayManager.getInstance();
	private Context mContext = null;

	// TODO: I'll change this soon later. seems not so good.
	private BillingActivity mBillingActivty = null;

	private static DealModel instance = null;

	public int getOrderProcessStatus() {
		return orderProcessStatus;
	}

	public void setOrderProcessStatus(int orderProcessStatus) {
		this.orderProcessStatus = orderProcessStatus;
	}

	private DealModel() {
		this.initialize(mShoppingCart);
		mShoppingCart.setDealModel(this);
		//同步购物车和账单中的数据项，支付，打折信息
		OrderInfo.setOrderContentList(mShoppingCart.getOrderContentList());
		OrderInfo.setPayBaseList(mPayManager.getPayList());
		OrderInfo.setOnsale(mShoppingCart.getOnsale());
	}

	public static synchronized DealModel getInstance() {
		if (instance == null) {
			instance = new DealModel();
		}
		return instance;
	}

	public OrderData getOrderData() {
		return OrderInfo;
	}

	public void setBillingActivity(BillingActivity mBillingActivty) {
		this.mBillingActivty = mBillingActivty;
	}

	public void initialize(Callbacks callbacks) {
		synchronized (mLock) {
			mCallbacks = new WeakReference<Callbacks>(callbacks);
		}
	}

	public void SetContext(Context mContext) {
		this.mContext = mContext;
	}

	public ShoppingCart getShoppingCart() {
		return DealModel.mShoppingCart;
	}

	@SuppressLint("NewApi")
	public void onOrderProcessStatusChanged(int newProcess) {
		setOrderProcessStatus(newProcess);
		Common.HideKeyboardIfExist(mContext);
		switch (newProcess) {
		case ORDER_PROCESS_INIT:
			// TODO: add some .....

			if (mContext != null) {
				Intent nextIntent = new Intent(mContext, SaleActivity.class);
				Bundle translateBundle = ActivityOptions.makeCustomAnimation(mContext, android.R.anim.fade_in,
						android.R.anim.fade_out).toBundle();
				mContext.startActivity(nextIntent, translateBundle);
				Log.i("BBBBBBB", "000000000000000000");
				if (mBillingActivty != null)
					mBillingActivty.finish();
			}
			break;
		case ORDER_PROCESS_SELECE:
			if (mContext != null) {
				Intent nextIntent = new Intent(mContext, SaleActivity.class);
				Bundle translateBundle = ActivityOptions.makeCustomAnimation(mContext, android.R.anim.fade_in,
						android.R.anim.fade_out).toBundle();
				mContext.startActivity(nextIntent, translateBundle);
				if (mBillingActivty != null)
					mBillingActivty.finish();
			}
			break;
//		case ORDER_PROCESS_PAYMENT:
//			if (mContext != null) {
//				Intent nextIntent = new Intent(mContext, BillingActivity.class);
//				Bundle translateBundle = ActivityOptions.makeCustomAnimation(mContext, android.R.anim.fade_in,
//						android.R.anim.fade_out).toBundle();
//				mContext.startActivity(nextIntent, translateBundle);
//
//			}
//
//			break;
		case ORDER_PROCESS_SENDINFO:
			if (mBillingActivty != null) {
				mBillingActivty.addSendInfoFragment();
			}

			break;
		default:
			break;
		}
	}

	public boolean beginToPayTheOrder() {
		if (Double.parseDouble(mShoppingCart.getTotalAmount()) >= 0 && mShoppingCart.getCount() > 0) {
			mPayManager.setMoneyToPay(mShoppingCart.getTotalAmount());
			OrderInfo.setmOrderStatus(OrderStatus.ORDER_HANGUP);
			onOrderProcessStatusChanged(ORDER_PROCESS_PAYMENT);
			OrderInfo.saveOrderData();
			OrderInfo.saveOrderContent();
			return true;
		} else
			return false;

	}

	public void BeginSelectProduct() {

		onOrderProcessStatusChanged(ORDER_PROCESS_SELECE);
	}

	public void OrderHasbeenPayed() {
		// TODO:need check
		if (OrderInfo.getCurrentOrder().getOrderID() == -1) {
			OrderInfo.setmOrderStatus(OrderStatus.ORDER_NORMAL);
			OrderInfo.setPayBaseList(PayManager.getInstance().getPayList());
			OrderInfo.setOnsale(mShoppingCart.getOnsale());
			OrderInfo.saveOrderData();
			OrderInfo.saveOrderContent();
			OrderInfo.saveOrderToOnsale();
			OrderInfo.savePayList();
		} else if (OrderInfo.getmOrderStatus() == OrderStatus.ORDER_HANGUP
				|| OrderInfo.getmOrderStatus() == OrderStatus.ORDER_NORMAL) {
			OrderInfo.setmOrderStatus(OrderStatus.ORDER_NORMAL);
			OrderInfo.setPayBaseList(PayManager.getInstance().getPayList());
			OrderInfo.setOnsale(mShoppingCart.getOnsale());
			OrderInfo.saveOrderData();
			OrderInfo.saveOrderToOnsale();
			OrderInfo.savePayList();
		} else
			return;

		try {
			PrinterManager.getInstance().printOrder(OrderInfo.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		//打印Pos信息+
		if (OrderInfo.getPayType() == PayBase.PAYTYPE.PAY_BY_MSR.toInt()) {
			//PrinterManager.getInstance().PrintPosSlip(true, 0);
			//PrinterManager.getInstance().PrintPosSlip(false, 8000);
		}

		onOrderProcessStatusChanged(ORDER_PROCESS_SENDINFO);
	}

	public void INITOrder() {
		OrderInfo.reInitOrderData();
		this.OrderInfo.setOrderContentList(mShoppingCart.getOrderContentList());
		// onOrderProcessStatusChanged(ORDER_PROCESS_INIT);
		onOrderProcessStatusChanged(ORDER_PROCESS_SELECE);
	}

	// 挂起当前订单
	public void HangUpCurrentOrder() {
		if (BasicArithmetic.compare(mShoppingCart.getTotalAmount(), "0.00") > 0) {
			OrderInfo.setmOrderStatus(OrderStatus.ORDER_HANGUP);
			OrderInfo.saveOrderData();
			OrderInfo.saveOrderContent();

			try {
				PrinterManager.getInstance().printOrder(OrderInfo.clone(), OrderPrint.HANGUP_MODE);
			} catch (CloneNotSupportedException e) {
				Log.d(TAG, "ERROR IN OrderInfo clone...");
			}

			OrderInfo.reInitOrderData();
		}
	}

	/*
	 * 恢复订单
	 */
	public void RecoverOrderData(OrderData mOrderData) {
		this.OrderInfo.InitWithOtherOrderData(mOrderData);
		this.OrderInfo.setInitOlderHangUpList(OrderManager.getOrderDataById(mOrderData.getCurrentOrder().getOrderID())
				.getOrderContentList());
		this.OrderInfo.setOrderContentList(mShoppingCart.getOrderContentList());
		mShoppingCart.removeAllItems();
		mShoppingCart.addNewItems(mOrderData.getOrderContentList());
		this.OrderInfo.setOrderContentList(mShoppingCart.getOrderContentList());
		OrderManager.deleteOrderInfoById(mOrderData.getCurrentOrder().getOrderID());
	}

	// 实现payBase的接口
	public void dealCanceled() {
	}

	// 实现payBase的接口
	public void dealConfirmed() {
	}

	public void addNewOrderContent(List<OrderContentData> mList) {
		if (mShoppingCart != null)
			mShoppingCart.addNewItems(mList);
	}

	public void addOrUpdateOrderContent(OrderContentData mOrderContent) {
		if (mShoppingCart != null)
			mShoppingCart.addOrUpdateItems(mOrderContent);
	}

	public void setOnsale(List<Onsale> mOnsale) {
		OrderInfo.setOnsale(mOnsale);
		if (mShoppingCart != null)
			mShoppingCart.setOnsale(mOnsale);
	}

	public void removeUnused() {
		final Callbacks cbk = mCallbacks.get();
		cbk.removeUnused();
	}

	public void removeCurrent() {
		final Callbacks cbk = mCallbacks.get();
		cbk.removeCurrent();
	}

	public void removeAll() {
		getOrderData().setInitOlderHangUpList(null);
		getOrderData().setOrderRemark(null);
		final Callbacks cbk = mCallbacks.get();
		cbk.removeAllItems();
	}

}
