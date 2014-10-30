package com.koolyun.mis.core.order;

import java.util.ArrayList;
import java.util.List;

import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.product.Product;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.NumberFormater;

public class OrderContentData implements Cloneable {
	private OrderContent orderContent;
	private String OnePrice = "0.00";
	private List<ProductSubAttribute> mSubAttrList = null;
	private List<Onsale> mOnsale = new ArrayList<Onsale>();
	private Product product;
	private OrderContentRemark mOrderContentRemark = null;

	public OrderContentData() {
		orderContent = new OrderContent();
	}

	public boolean hasNoSpec() {
		boolean hasnospec = true;
		if (mOnsale == null || mOnsale.isEmpty()) {
			if ((mSubAttrList == null || mSubAttrList.isEmpty())) {
				hasnospec = true;
			} else {
				for (int i = 0; i < mSubAttrList.size(); i++) {
					hasnospec = isSubAttrDefault(mSubAttrList.get(i));
					if (!hasnospec)
						break;
				}
			}
		} else
			hasnospec = false;
		return hasnospec;
	}

	public boolean isSubAttributeSame(List<ProductSubAttribute> tmplist) {
		return true;
	}

	public boolean isOnsaleSame(List<Onsale> tmplist) {
		return true;
	}

	public OrderContentData(OrderContent orderContent) {
		this.orderContent = orderContent;
	}

	public OrderContent getOrderContent() {
		return orderContent;
	}

	public String getOrderContentName() {
		return product.getName();
	}

	/**
	 * @return 添加属性/优惠后的商品价格
	 */
	public String getOnePrice() {
		return OnePrice;
	}

	/**
	 * @param onePrice
	 *            添加属性/优惠后的商品价格
	 */
	public void setOnePrice(String onePrice) {
		OnePrice = NumberFormater.currencyFormat(onePrice);
	}

	public String getProductPrice() {
		if (product != null && product.getProductID() != ShoppingCart.MANUID)
			return product.getPrice();
		return "";
	}

	public int getOrderId() {
		return orderContent.getOrderId();
	}

	public void setOrderId(int mOrderId) {
		orderContent.setOrderId(mOrderId);
	}

	public int getOrderContentId() {
		return orderContent.getOrderContentId();
	}

	public void setOrderContentId(int mOrderContentId) {
		orderContent.setOrderContentId(mOrderContentId);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		if (product.getProductID() != ShoppingCart.MANUID) {
			this.setOnePrice(product.getPrice());
			this.setProductId(product.getProductID());
			setProductSubAttrList(ProductManager.getDefaultSubAttrList(product.getProductID()));
		} else {
			if (BasicArithmetic.compare(product.getPrice(), "0") != 0) {
				this.setOnePrice(product.getPrice());
				this.setProductId(product.getProductID());
			}
		}
	}

	private boolean isSubAttrDefault(ProductSubAttribute mProductSubAttribute) {
		if (mProductSubAttribute == null)
			return false;
		ProductSubAttribute tmp = ProductManager.getDefaultSubAttr(mProductSubAttribute.getProductAttributeID());
		if (tmp == null)
			return false;
		return tmp.getProductSubAttributeId() == mProductSubAttribute.getProductSubAttributeId();
	}

	public int getProductId() {
		return orderContent.getProductId();
	}

	public void setProductId(int id) {
		orderContent.setProductId(id);
	}

	public int getCount() {
		return orderContent.getCount();
	}

	public void setCount(int count) {
		orderContent.setCount(count);
	}

	public void addOneItem() {
		if (orderContent.getCount() < 999)
			orderContent.setCount(orderContent.getCount() + 1);
	}

	public void subOneItem() {
		if (orderContent.getCount() > 1)
			orderContent.setCount(orderContent.getCount() - 1);
	}

	public void addNItem(int count) {
		orderContent.setCount(orderContent.getCount() + count);
	}

	public void subNItem(int count) {
		if (orderContent.getCount() >= count)
			orderContent.setCount(orderContent.getCount() - count);
	}

	public String getItemAmount() {
		orderContent.setAmount(BasicArithmetic.multi(getOnePrice(), "" + getCount()));
		return orderContent.getAmount();
	}

	public void setAmount(String amount) {
		orderContent.setAmount(amount);
	}

	public String getPhoto() {
		return product.getProductPhoto();
	}

	public void setProductSubAttrList(List<ProductSubAttribute> subAttrList) {
		mSubAttrList = subAttrList;
		updateOnePrice();
	}

	public List<ProductSubAttribute> getProductSubAttrList() {
		return mSubAttrList;
	}

	public List<Onsale> getOnsale() {
		return mOnsale;
	}

	public void setOnsale(List<Onsale> mOnsale) {
		this.mOnsale = mOnsale;
		updateOnePrice();
	}

	private void updateOnePrice() {
		if (product.getProductID() == ShoppingCart.MANUID)
			return;
		String tmpPrice = getProductPrice();
		if (mSubAttrList != null) {
			for (int i = 0; i < mSubAttrList.size(); i++) {
				tmpPrice = BasicArithmetic.add(tmpPrice, mSubAttrList.get(i).getPriceAffect());
			}
		}
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
		setOnePrice(NumberFormater.currencyFormat(tmpPrice));
		orderContent.setAmount(BasicArithmetic.multi(getOnePrice(), "" + getCount()));
	}

	public OrderContentRemark getmOrderContentRemark() {
		return mOrderContentRemark;
	}

	public void setmOrderContentRemark(OrderContentRemark mOrderContentRemark) {
		this.mOrderContentRemark = mOrderContentRemark;
	}

	public OrderContentData clone() {
		OrderContentData o = null;
		try {
			o = (OrderContentData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	public void saveData() {
		saveOrderContentData();
		saveSubAttriData();
		saveOnsaleData();
	}

	public void saveOrderContentData() {

	}

	public void saveSubAttriData() {

	}

	public void saveOnsaleData() {

	}
}
