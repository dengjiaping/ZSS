package com.koolyun.mis.core.order;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.Log;

import com.koolyun.mis.core.CardPayInfos;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.util.pay.PayBase;
import com.koolyun.mis.util.pay.PayManager;

public class OrderData {

	private Orders currentOrder = new Orders();
	private List<OrderContentData> orderContentList = null;
	private List<OrdersToPaymentType> mPayList = null;
	private List<OrderContentData> mHangUpInitList = null;
	private List<OrderToOnsale> mOrderToOnsale = null;
	private List<Onsale> mOnsale = null;
	private OrderStatus mOrderStatus = OrderStatus.ORDER_NORMAL;
	private CardPayInfos mCardPayInfos = null;
	private OrderRemark mOrderRemark = null;
	private OrderToAccount mOrderToAccount = null;

	public enum OperationType {
		OPERATION_SALE(0), OPERATION_SALEVOID(1), OPERATION_REFUND(2);
		private int code; // 状态码值

		OperationType(int code) { // 非public构造方法
			this.code = code;
		}

		public int toInt() {
			return code;
		}

		public static OperationType valueOf(int status) {
			switch (status) {
			case 0:
				return OPERATION_SALE;
			case 1:
				return OPERATION_SALEVOID;
			case 2:
				return OPERATION_REFUND;
			default:
				return null;
			}
		}

		@Override
		public String toString() {
			return String.valueOf(code);
		}
	}

	public enum OrderStatus {
		ORDER_NORMAL(0), ORDER_UNDO(1), ORDER_HANGUP(2), // 挂单，点击结算自动挂单
		ORDER_PRESTORE(3), // 刷卡交易时，预先存的状态
		ORDER_PERREFUND(4); // 撤销预先存的状态

		private int code; // 状态码值

		OrderStatus(int code) { // 非public构造方法
			this.code = code;
		}

		public int toInt() {
			return code;
		}

		public static OrderStatus valueOf(int status) {
			switch (status) {
			case 0:
				return ORDER_NORMAL;
			case 1:
				return ORDER_UNDO;
			case 2:
				return ORDER_HANGUP;
			case 3:
				return ORDER_PRESTORE;
			case 4:
				return ORDER_PERREFUND;
			default:
				return null;
			}
		}

		@Override
		public String toString() {
			return String.valueOf(code);
		}
	};

	public OrderToAccount getmOrderToAccount() {
		return mOrderToAccount;
	}

	public void setmOrderToAccount(OrderToAccount mOrderToAccount) {
		this.mOrderToAccount = mOrderToAccount;
	}

	public OrderRemark getOrderRemark() {
		return mOrderRemark;
	}

	public void setOrderRemark(OrderRemark mOrderRemark) {
		this.mOrderRemark = mOrderRemark;
	}

	public synchronized void InitWithOtherOrderData(OrderData orderdata) {
		this.currentOrder = orderdata.currentOrder;
		// orderContentList不可以更改
		// this.orderContentList = orderdata.orderContentList;
		this.mHangUpInitList = orderdata.mHangUpInitList;
		this.mPayList = orderdata.getPayList();
		this.mCardPayInfos = orderdata.getCardPayInfos();
		this.mOrderRemark = orderdata.getOrderRemark();
		this.mOrderStatus = orderdata.getmOrderStatus();
		this.mOrderToAccount = orderdata.getmOrderToAccount();
		this.mOrderToOnsale = orderdata.mOrderToOnsale;
		this.mOnsale = orderdata.mOnsale;
	}

	@Override
	public OrderData clone() throws CloneNotSupportedException {
		OrderData mOrderDataClone = new OrderData();
		mOrderDataClone.currentOrder = this.currentOrder.clone();
		// 深copy payList
		if (getPayList() != null) {
			mOrderDataClone.mPayList = new LinkedList<OrdersToPaymentType>();
			for (int i = 0; i < getPayList().size(); i++) {
				mOrderDataClone.mPayList.add(getPayList().get(i));
			}
		} else
			mOrderDataClone.mPayList = null;

		// 深copy mOrderToAccount
		if (mOrderToAccount != null) {
			mOrderDataClone.mOrderToAccount = this.mOrderToAccount.clone();
		} else
			mOrderDataClone.mOrderToAccount = null;

		// 深copy mOrderRemark
		if (mOrderRemark != null) {
			mOrderDataClone.mOrderRemark = this.mOrderRemark.clone();
		} else
			mOrderDataClone.mOrderRemark = null;

		// 深copy orderContentList
		Log.d("ORDERCONTENT", "COUNT:" + orderContentList.size());
		if (orderContentList != null) {
			mOrderDataClone.orderContentList = new LinkedList<OrderContentData>();
			for (int i = 0; i < orderContentList.size(); i++) {
				mOrderDataClone.orderContentList.add(orderContentList.get(i));
			}
		} else
			mOrderDataClone.orderContentList = null;

		// 深copy mHangUpInitList
		if (mHangUpInitList != null) {
			mOrderDataClone.mHangUpInitList = new LinkedList<OrderContentData>();
			for (int i = 0; i < mHangUpInitList.size(); i++) {
				mOrderDataClone.mHangUpInitList.add(mHangUpInitList.get(i));
			}
		} else
			mOrderDataClone.mHangUpInitList = null;

		// 深copy mOrderToOnsale
		if (mOrderToOnsale != null) {
			mOrderDataClone.mOrderToOnsale = new LinkedList<OrderToOnsale>();
			for (int i = 0; i < mOrderToOnsale.size(); i++) {
				mOrderDataClone.mOrderToOnsale.add(mOrderToOnsale.get(i));
			}
		} else
			mOrderDataClone.mOrderToOnsale = null;

		// 深copy mOnsale
		if (mOnsale != null) {
			mOrderDataClone.mOnsale = new LinkedList<Onsale>();
			for (int i = 0; i < mOnsale.size(); i++) {
				mOrderDataClone.mOnsale.add(mOnsale.get(i));
			}
		} else
			mOrderDataClone.mOnsale = null;

		mOrderDataClone.mOrderStatus = this.mOrderStatus;

		return mOrderDataClone;

	}

	public void setInitOlderHangUpList(List<OrderContentData> tmpOrderContentList) {
		this.mHangUpInitList = tmpOrderContentList;
	}

	public List<OrderContentData> getInitOlderHangUpList() {
		return mHangUpInitList;
	}

	public List<OrderChangeInfo> getOrderChanged() {
		if (mHangUpInitList == null)
			return null;
		return OrderContentListCompare.compare(mHangUpInitList, orderContentList);
	}

	public CardPayInfos AddCardPayInfos() {
		if (mCardPayInfos == null) {
			mCardPayInfos = new CardPayInfos();
		}
		return mCardPayInfos;
	}

	public CardPayInfos getCardPayInfos() {
		return mCardPayInfos;
	}

	public void setCardPayInfos(CardPayInfos mCardPayInfos) {
		this.mCardPayInfos = mCardPayInfos;
	}

	public void savePayList() {
		if (mPayList != null) {
			Iterator<OrdersToPaymentType> iterator = mPayList.iterator();
			while (iterator.hasNext()) {
				OrdersToPaymentType mc = iterator.next();
				mc.setOrderID(currentOrder.getOrderID());
				OrderManager.saveOrderToPayment(mPayList);
			}
		}
	}

	public synchronized void saveCardPayInfo() {
		if (mCardPayInfos != null && currentOrder.getOrderID() != -1) {
			mCardPayInfos.setOrderId(currentOrder.getOrderID());
			mCardPayInfos.saveCardPayInfos();
		}
	}

	public void saveOrderToOnsale() {
		if (mOnsale != null && currentOrder.getOrderID() != -1) {
			getOrderToOnsale();
			Iterator<OrderToOnsale> iterator = mOrderToOnsale.iterator();
			while (iterator.hasNext()) {
				OrderToOnsale mc = iterator.next();
				mc.setOrderID(currentOrder.getOrderID());
				OrderManager.saveOrderToOnsale(mc);
			}
		}
	}

	public void saveOrderContent() {
		Iterator<OrderContentData> iterator = orderContentList.iterator();

		while (iterator.hasNext()) {

			OrderContentData mc = iterator.next();
			mc.setOrderId(currentOrder.getOrderID());
			if (mc.getProductId() == ShoppingCart.MANUID && BasicArithmetic.compare(mc.getOnePrice(), "0") == 0)
				continue;
			long orderContentId = OrderManager.saveOrderContent(mc.getOrderContent());
			if (orderContentId != -1) {
				List<ProductSubAttribute> tmpSubAttrList = mc.getProductSubAttrList();
				List<Onsale> tmpOnsale = mc.getOnsale();
				OrderContentRemark mOrderContentRemark = mc.getmOrderContentRemark();
				if (tmpSubAttrList != null) {
					Iterator<ProductSubAttribute> iterator1 = tmpSubAttrList.iterator();
					OrderContentToAttribute octa = new OrderContentToAttribute();
					while (iterator1.hasNext()) {
						ProductSubAttribute psa = iterator1.next();
						octa.setOrderContentID((int) orderContentId);
						octa.setProductSubAttributeId(psa.getProductSubAttributeId());
						octa.setPrice(psa.getPriceAffect());
						OrderManager.saveOrderContentToAttribute(octa);
					}
				}
				if (tmpOnsale != null) {
					Iterator<Onsale> iterator2 = tmpOnsale.iterator();
					OrderContentToOnsale octo = new OrderContentToOnsale();
					while (iterator2.hasNext()) {
						Onsale os = iterator2.next();
						octo.setOnsaleID(os.getOnsaleID());
						octo.setOrderContentID((int) orderContentId);
						octo.setValue(os.getValue());
						OrderManager.saveOrderContentToOnSale(octo);
					}
				}
				if (mOrderContentRemark != null) {
					mOrderContentRemark.setOrderContentId((int) orderContentId);
					OrderManager.saveOrUpdateOrderContentRemark(mOrderContentRemark);
				}
			}
		}
	}

	public void removeOrderDataInDB() {
		// 数据库中删除order信息
		OrderManager.deleteOrderInfoById(currentOrder.getOrderID());

		// 把内存中的orderid，ordercontentid设为-1

		currentOrder.setOrderID(-1);
		if (orderContentList != null) {
			Iterator<OrderContentData> iterator = orderContentList.iterator();
			while (iterator.hasNext()) {
				OrderContentData mc = iterator.next();
				mc.setOrderId(-1);
				mc.setOrderContentId(-1);
			}
		}
		if (mPayList != null) {
			Iterator<OrdersToPaymentType> iterator2 = mPayList.iterator();
			while (iterator2.hasNext()) {
				OrdersToPaymentType mc = iterator2.next();
				mc.setOrderID(-1);
			}
		}
		mOrderToOnsale = null;
		mOnsale = null;
	}

	@SuppressLint("SimpleDateFormat")
	public synchronized int saveOrderData() {
		if (!Common.checkValidString(currentOrder.getBillNo())) {
			currentOrder.setBillNo(Common.getBillNumber());
		}

		if (currentOrder.getCreateTime() == 0)
			currentOrder.setCreateTime(System.currentTimeMillis());
		currentOrder.setModifyTime(System.currentTimeMillis());

		int orderId = (int) OrderManager.saveOrder(currentOrder);
		MyLog.i("Orderdata ==getTxnId---- "+currentOrder.getTxnId());
		if(currentOrder.getTxnId() != null && currentOrder.getTxnId().trim().length() != 0){
			OrderManager.updateOrderById(currentOrder.getOrderID(), currentOrder.getTxnId());
		}
		currentOrder.setOrderID(orderId);

		if (orderId != -1) {
			if (mOrderRemark != null) {
				mOrderRemark.setOrderId((int) orderId);
				OrderManager.addOrUpdateOrderRemark(mOrderRemark);
			}
			if (mOrderToAccount == null) {
				mOrderToAccount = new OrderToAccount();
			}
			mOrderToAccount.setOrderID((int) orderId);
			mOrderToAccount.setAccountName(AccountManager.getInstance().getCurrentAccount().getNameToshow());
			OrderManager.saveOrderToAccount(mOrderToAccount);
		}
		return (int) orderId;
	}

	public void reInitOrderData() {
		DealModel.getInstance().removeAll();
		PayManager.getInstance().ReInit();
		mPayList = null;
		mOrderStatus = OrderStatus.ORDER_NORMAL;
		if (mCardPayInfos != null) {
			mCardPayInfos.clearCardPayInfos();
			mCardPayInfos = null;
		}
		currentOrder.reInit();
		mOrderRemark = null;
		mHangUpInitList = null;
		mOrderToAccount = null;
		mOrderToOnsale = null;
		mOnsale = null;
	}

	public Orders getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(Orders currentOrder) {
		this.currentOrder = currentOrder;
	}

	public List<OrderContentData> getOrderContentList() {
		return orderContentList;
	}

	public void setOrderContentList(List<OrderContentData> orderContentList) {
		this.orderContentList = orderContentList;
	}

	public List<OrdersToPaymentType> getPayList() {
		return mPayList;
	}

	public void setPayList(List<OrdersToPaymentType> mPayList) {
		this.mPayList = mPayList;
	}

	public void setPayBaseList(List<PayBase> mPayBaseList) {
		if (mPayList == null) {
			mPayList = new LinkedList<OrdersToPaymentType>();
		} else {
			mPayList.clear();
		}
		Iterator<PayBase> iterator = mPayBaseList.iterator();
		while (iterator.hasNext()) {
			PayBase pb = iterator.next();
			mPayList.add(new OrdersToPaymentType(0, pb.getmType().toInt(), pb.getPayMoney()));
		}
	}

	public OrderContentData getOrderContentData(OrderChangeInfo minfo) {
		int oldindex = minfo.getOldIndex();
		int newindex = minfo.getNewIndex();
		if (newindex >= 0 && newindex < orderContentList.size()) {
			return orderContentList.get(newindex);
		} else if (oldindex >= 0 && oldindex < mHangUpInitList.size()) {
			return mHangUpInitList.get(oldindex);
		} else
			return null;
	}

	public OrderStatus getmOrderStatus() {
		return OrderStatus.valueOf(currentOrder.getOrderStatusID());
	}

	public void setmOrderStatus(OrderStatus mOrderStatus) {
		this.mOrderStatus = mOrderStatus;
		currentOrder.setOrderStatusID(mOrderStatus.toInt());
	}

	public String getAmount() {
		String mTotalAmount = "0";
		if (orderContentList != null) {
			Iterator<OrderContentData> iterator = orderContentList.iterator();

			while (iterator.hasNext()) {
				OrderContentData mc = iterator.next();
				mTotalAmount = BasicArithmetic.add(mc.getItemAmount(), mTotalAmount);
			}
			if (mOnsale != null) {
				for (int i = 0; i < mOnsale.size(); i++) {
					int mode = mOnsale.get(i).getOnsaleType();
					if (mode == 1) {
						mTotalAmount = BasicArithmetic.add(mTotalAmount, mOnsale.get(i).getValue());
					} else if (mode == 2) {
						double rate = 1 + Double.parseDouble(BasicArithmetic.div(mOnsale.get(i).getValue(), "100"));
						mTotalAmount = BasicArithmetic.multi(mTotalAmount, String.valueOf(rate));
					}

				}
			}
		}
		return mTotalAmount;
	}

	// TODO: may FIX later
	public int getPayType() {
		if (mPayList != null && mPayList.size() > 0) {
			return (mPayList.get(0).getPaymentTypeID());
		}
		return -1;
	}

	public void setOnsale(List<Onsale> mOnsale) {
		this.mOnsale = mOnsale;
	}

	private void getOrderToOnsale() {
		if (mOrderToOnsale == null) {
			mOrderToOnsale = new LinkedList<OrderToOnsale>();
		} else {
			mOrderToOnsale.clear();
		}
		Iterator<Onsale> iterator = mOnsale.iterator();
		while (iterator.hasNext()) {
			Onsale os = iterator.next();
			mOrderToOnsale.add(new OrderToOnsale(-1, os.getOnsaleID(), os.getValue()));
		}
	}
}
