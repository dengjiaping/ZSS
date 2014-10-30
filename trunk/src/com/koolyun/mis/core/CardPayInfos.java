package com.koolyun.mis.core;

import com.koolyun.mis.core.order.BankCard;
import com.koolyun.mis.core.order.OrderCustomerInfo;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.core.order.OrderProcess;

public class CardPayInfos {
	private OrderProcess mOrderProcess = null;
	private BankCard mBankCard = null;
	private OrderCustomerInfo mOrderCustomerInfo = null;
	private int OrderId = -1;

	public int getOrderId() {
		return OrderId;
	}

	public void setOrderId(int orderId) {
		OrderId = orderId;
	}

	public enum OrderProcessMode {
		ORDER_PROCESS_MSR_SALE(0), ORDER_PROCESS_MSR_SALEVOID(1), ORDER_PROCESS_MSR_REFUND(2), ORDER_PROCESS_EMV_SALE(3), ORDER_PROCESS_EMV_SALEVOID(
				4), ORDER_PROCESS_EMV_REFUND(5);

		private int code; // 状态码值

		OrderProcessMode(int code) { // 非public构造方法
			this.code = code;
		}

		public int toInt() {
			return code;
		}

		@Override
		public String toString() {
			return String.valueOf(code);
		}
	};

	public CardPayInfos() {
		mOrderProcess = new OrderProcess();
		mBankCard = new BankCard();
		mOrderCustomerInfo = new OrderCustomerInfo();

	}

	public OrderProcess getOrderProcess() {
		return mOrderProcess;
	}

	public void setOrderProcess(OrderProcess mOrderProcess) {
		this.mOrderProcess = mOrderProcess;
	}

	public BankCard getBankCard() {
		return mBankCard;
	}

	public void setBankCard(BankCard mBankCard) {
		this.mBankCard = mBankCard;
	}

	public OrderCustomerInfo getOrderCustomerInfo() {
		return mOrderCustomerInfo;
	}

	public void setOrderCustomerInfo(OrderCustomerInfo mOrderCustomerInfo) {
		this.mOrderCustomerInfo = mOrderCustomerInfo;
	}

	public void clearCardPayInfos() {
		mOrderProcess = null;
		mBankCard = null;
		mOrderCustomerInfo = null;
	}

	public void saveCardPayInfos() {
		long mOrderProcessId = -1;
		if (mOrderProcess != null) {
			if (OrderId != -1)
				mOrderProcess.setOrderID(OrderId);
			mOrderProcessId = OrderManager.saveOrderProcess(mOrderProcess);
		}
		if (mOrderProcessId == -1)
			return;
	}

	// TODO: remove this method ,not to be used...
	public static long setReverseInfo(OrderProcessMode mOrderProcessMode, String traceNo, String price) {
		OrderProcess mOrderProcess = new OrderProcess(-1, mOrderProcessMode.toInt(), -1, -1, null, traceNo, null, null,
				null, null, price, null, null, null);
		long ret = OrderManager.saveOrderProcess(mOrderProcess);
		return ret;
		// TODO: add a new item to indicate a congzheng
	}

}
