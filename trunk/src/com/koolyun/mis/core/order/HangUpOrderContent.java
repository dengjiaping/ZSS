package com.koolyun.mis.core.order;

public class HangUpOrderContent {
	OrderContentData mOrderContentdata = null;
	int isChanged = 0; // 0:not changed 1:changed
	int countChange = 0;

	public HangUpOrderContent(OrderContentData mOrderContent, int isChanged, int countChange) {
		this.mOrderContentdata = mOrderContent;
		this.isChanged = isChanged;
		this.countChange = countChange;
	}

	public OrderContentData getOrderContent() {
		return mOrderContentdata;
	}

	public void setOrderContent(OrderContentData mOrderContent) {
		this.mOrderContentdata = mOrderContent;
	}

	public int getIsChanged() {
		return isChanged;
	}

	public void setIsChanged(int isChanged) {
		this.isChanged = isChanged;
	}

	public int getCountChange() {
		return countChange;
	}

	public void setCountChange(int countChange) {
		this.countChange = countChange;
	}

}
