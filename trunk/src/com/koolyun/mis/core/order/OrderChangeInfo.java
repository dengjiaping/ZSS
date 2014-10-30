package com.koolyun.mis.core.order;

public class OrderChangeInfo {

	int productID;
	String onePrice;
	int oldIndex;
	int newIndex;
	boolean isAllSame;
	boolean onlyCountChange;
	int countChanged;
	int changeFlag;// 0:not,1:change,2:add,3:remove
	String amountChange = "0.00";

	public static final int NORMAL = 0;
	public static final int CHANGE = 1;
	public static final int ADD = 2;
	public static final int DELETE = 3;

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getOnePrice() {
		return onePrice;
	}

	public void setOnePrice(String onePrice) {
		this.onePrice = onePrice;
	}

	public String getAmountChange() {
		return amountChange;
	}

	public void setAmountChange(String amountChange) {
		this.amountChange = amountChange;
	}

	public OrderChangeInfo(int productID, String onePrice, int oldIndex, int newIndex, boolean isAllSame,
			boolean onlyCountChange, int countChanged, int changeFlag, String amountChange) {
		this.productID = productID;
		this.onePrice = onePrice;
		this.oldIndex = oldIndex;
		this.newIndex = newIndex;
		this.isAllSame = isAllSame;
		this.onlyCountChange = onlyCountChange;
		this.countChanged = countChanged;
		this.changeFlag = changeFlag;
		this.amountChange = amountChange;
	}

	public int getOldIndex() {
		return oldIndex;
	}

	public void setOldIndex(int oldIndex) {
		this.oldIndex = oldIndex;
	}

	public int getNewIndex() {
		return newIndex;
	}

	public void setNewIndex(int newIndex) {
		this.newIndex = newIndex;
	}

	public boolean isAllSame() {
		return isAllSame;
	}

	public void setAllSame(boolean isAllSame) {
		this.isAllSame = isAllSame;
	}

	public boolean isOnlyCountChange() {
		return onlyCountChange;
	}

	public void setOnlyCountChange(boolean onlyCountChange) {
		this.onlyCountChange = onlyCountChange;
	}

	public int getCountChanged() {
		return countChanged;
	}

	public void setCountChanged(int countChanged) {
		this.countChanged = countChanged;
	}

	public int getChangeFlag() {
		return changeFlag;
	}

	public void setChangeFlag(int changeFlag) {
		this.changeFlag = changeFlag;
	}

	@Override
	public String toString() {
		return "OrderChangeInfo [productID=" + productID + ", onePrice=" + onePrice + ", oldIndex=" + oldIndex
				+ ", newIndex=" + newIndex + ", isAllSame=" + isAllSame + ", onlyCountChange=" + onlyCountChange
				+ ", countChanged=" + countChanged + ", changeFlag=" + changeFlag + ", amountChange=" + amountChange
				+ "]";
	}

}
