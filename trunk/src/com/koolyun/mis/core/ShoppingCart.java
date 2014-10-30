package com.koolyun.mis.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.product.Product;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.NumberFormater;

public class ShoppingCart implements DealModel.Callbacks {
	public static final int INVALIDINDEX = -1;
	private List<OrderContentData> OrderContentList = new ArrayList<OrderContentData>();
	private List<TotalMoneyObserver> tmObsList = new ArrayList<TotalMoneyObserver>();
	private int currentEditIndex = INVALIDINDEX;
	public static final int MANUID = 0;
	private DealModel mDealModel = null;
	private String mTotalAmount = "0.00";
	private String mOnSaleAmount = "0.00";
	private List<Onsale> mOnsale = new ArrayList<Onsale>();
	public static final int MAXCOUNT = 50;

	public List<OrderContentData> getOrderContentList() {
		return OrderContentList;
	}

	public void registerObs(TotalMoneyObserver obs) {
		tmObsList.add(obs);
	}

	public void unRegisterObs(TotalMoneyObserver obs) {
		tmObsList.remove(obs);
	}

	@Override
	public void addOrUpdateItems(OrderContentData mOrderContent) {
		if (mOrderContent == null || mOrderContent.getProduct() == null)
			return;
		if (OrderContentList.size() >= MAXCOUNT)
			return;
		if (hasAddedManuItemAtLast() != INVALIDINDEX) {
			if (mOrderContent.getProduct().getProductID() == MANUID) {
				updateItems(mOrderContent);
				return;
			}
			removeUnused();
		}

		OrderContentData tmp = null;
		if (OrderContentList.size() > 0)
			tmp = OrderContentList.get(OrderContentList.size() - 1);

		if (tmp != null && tmp.getProductId() != MANUID && tmp.getProductId() == mOrderContent.getProductId()
				&& tmp.hasNoSpec()) {
			tmp.addOneItem();
		} else {
			OrderContentList.add(mOrderContent);
			update();
		}

	}

	public void remove(int pos) {
		if (pos >= 0 && pos < OrderContentList.size()) {
			OrderContentList.remove(pos);
			update();
		}
	}

	public void setDealModel(DealModel mDealModel) {
		this.mDealModel = mDealModel;
	}

	public DealModel getDealModel() {
		return mDealModel;
	}

	public void setCurrentEditIndex(int currentEditIndex) {
		this.currentEditIndex = currentEditIndex;
	}

	public void unsetCurrentEditIndex() {
		this.currentEditIndex = INVALIDINDEX;
	}

	public int getCurrentEditIndex() {
		return currentEditIndex;
	}

	public int getItemIndexToUpdate() {
		if (currentEditIndex == INVALIDINDEX) {
			int index = hasAddedManuItemAtLast();
			if (index != INVALIDINDEX)
				return index;
		}
		return getCurrentEditIndex();
	}

	@Override
	public void addNewItems(List<OrderContentData> mList) {
		// 判断手动添加的商品有没有完成，最后一个的价格是不是为0
		if (hasUnNewAddedManuItem())
			return;
		if (OrderContentList.size() >= MAXCOUNT)
			return;
		int count = mList.size();
		for (int i = 0; i < count; i++) {
			OrderContentData tmp = null;
			if (OrderContentList.size() > 0)
				tmp = OrderContentList.get(OrderContentList.size() - 1);
			if (tmp != null && tmp.getProductId() != MANUID && tmp.getProductId() == mList.get(i).getProductId()
					&& tmp.hasNoSpec()) {
				tmp.addNItem(mList.get(i).getCount());
			} else {
				OrderContentList.add(mList.get(i));
			}
		}
		update();

	}

	@Override
	public void removeItems(OrderContentData mOrderContent) {
		OrderContentList.remove(mOrderContent);
		update();
	}

	@Override
	public void updateItems(OrderContentData mContent) {
		OrderContentData mtmp = OrderContentList.get(getItemIndexToUpdate());
		// 去掉了判断，是手动输入也可以选择多个，同时继续编辑
		mtmp.setOnePrice(mContent.getOnePrice());
		mtmp.setAmount(mContent.getItemAmount());
		update();
	}

	@Override
	public void removeAllItems() {

		Iterator<OrderContentData> iterator = OrderContentList.iterator();
		while (iterator.hasNext()) {
			// OrderContentData mc = iterator.next();
			// if(!(mc.getProduct().getProductID() == MANUID &&
			// BasicArithmetic.compare(mc.getProduct().getPrice(), "0.00") == 0
			// && BasicArithmetic.compare(mc.getOnePrice(), "0.00") == 0))
			iterator.next();
			iterator.remove();
		}
		mOnsale.clear();
		update();
	}

	@Override
	public String getTotalAmount() {
		return mOnSaleAmount;
	}

	public void dividItem(int itemIndex) {
		int count = OrderContentList.get(itemIndex).getCount();
		if (count > 1) {
			OrderContentData mTd = OrderContentList.get(itemIndex);
			mTd.subOneItem();
			OrderContentData mTd2 = mTd.clone();
			mTd2.setCount(1);
			OrderContentList.add(itemIndex, mTd2);
		}
		update();

	}

	public int getCount() {
		int size = OrderContentList.size();
		return size;
	}

	public boolean hasUnNewAddedManuItem() {
		int size = getCount();
		if (size > 0) {
			if (Double.parseDouble(OrderContentList.get(size - 1).getItemAmount()) == 0
					&& OrderContentList.get(size - 1).getProductId() == MANUID)
				return true;
		}
		return false;
	}

	public int hasAddedManuItemAtLast() {
		int size = getCount();
		if (size > 0) {
			Product product = OrderContentList.get(size - 1).getProduct();
			if (product.getProductID() == MANUID) {
				return size - 1;
			}
		}
		return INVALIDINDEX;
	}

	public OrderContentData getOrderContentByIndex(int id) {
		return OrderContentList.get(id);
	}

	public OrderContentData getOrderContentById(int id) {
		return OrderContentList.get(id);
	}

	@Override
	public void removeUnused() {

		Iterator<OrderContentData> iterator = OrderContentList.iterator();
		while (iterator.hasNext()) {
			OrderContentData mc = iterator.next();
			if ((mc.getProduct().getProductID() == MANUID
					&& BasicArithmetic.compare(mc.getProduct().getPrice(), "0.00") == 0 && BasicArithmetic.compare(
					mc.getOnePrice(), "0.00") == 0)) {
				iterator.remove();
			}
		}
		update();
	}

	public void update() {

		Iterator<OrderContentData> iterator = OrderContentList.iterator();
		mTotalAmount = "0";
		while (iterator.hasNext()) {
			OrderContentData mc = iterator.next();
			mTotalAmount = BasicArithmetic.add(mc.getItemAmount(), mTotalAmount);
		}
		updateAmount();
		notifyObsers();
	}

	public List<Onsale> getOnsale() {
		return mOnsale;
	}

	public void setOnsale(List<Onsale> mOnsale) {
		this.mOnsale = mOnsale;
		updateAmount();
		notifyObsers();
	}

	private void updateAmount() {
		String tmpPrice = mTotalAmount;
		if (mOnsale != null) {
			for (int i = 0; i < mOnsale.size(); i++) {
				int mode = mOnsale.get(i).getOnsaleType();
				if (mode == 1) {
					tmpPrice = BasicArithmetic.add(tmpPrice, mOnsale.get(i).getValue());
				} else if (mode == 2) {
					double rate = 1 + Double.parseDouble(BasicArithmetic.div(mOnsale.get(i).getValue(), "100"));
					tmpPrice = BasicArithmetic.multi(tmpPrice, String.valueOf(rate));
				}

			}
		}
		mOnSaleAmount = NumberFormater.currencyFormat(tmpPrice);
	}

	private void notifyObsers() {
		Iterator<TotalMoneyObserver> iterator = tmObsList.iterator();
		while (iterator.hasNext()) {
			TotalMoneyObserver mc = iterator.next();
			mc.totalMoneyChanged(mOnSaleAmount);
		}
	}

	@Override
	public void removeCurrent() {
		OrderContentList.remove(currentEditIndex);
		unsetCurrentEditIndex();
		update();

	}
}
