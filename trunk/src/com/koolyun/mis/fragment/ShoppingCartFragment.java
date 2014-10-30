package com.koolyun.mis.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.SaleProductAdapter;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.widget.AnyWhereDialog;

/**
 * 购物车商品fragment
 */
public class ShoppingCartFragment extends BaseListFragment implements OnClickListener {

	private int mItemHeight = 82;
	private AnyWhereDialog mDialog = null;
	private View currentSelectItem = null;
	private int currentSelectItemPosition = -1;
	SaleProductAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listfragment_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mAdapter = new SaleProductAdapter(getActivity(), DealModel.getInstance().getShoppingCart());
		getListView().setAdapter(mAdapter);

	}

	public int getCurrentSelectItemPosition() {
		return currentSelectItemPosition;
	}

	@Override
	public void onListItemClick(final ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		MyLog.d("onListItemClick =" + position);
		if (DealModel.getInstance().getOrderProcessStatus() != DealModel.ORDER_PROCESS_SELECE)
			return;
		setSelectItem(position);
	}

	public void setSelectItem(int position) {
		this.currentSelectItemPosition = position;
		mAdapter.setSelectItem(position);
		mAdapter.notifyDataSetChanged();
//		View v = getListView().getChildAt(position);
//		if (v != currentSelectItem) {
//			if (currentSelectItem == null) {
//				currentSelectItem = v;
//			} else {
//				RelativeLayout bgView = (RelativeLayout) currentSelectItem.findViewById(R.id.product_item_left);
//				TextView number = (TextView) currentSelectItem.findViewById(R.id.sale_product_item_number);
//				number.setBackgroundResource(R.color.v2_txt_shopping_cat_item_attr_bg);
//				bgView.setBackgroundResource(R.color.transparent_background);
//				currentSelectItem = v;
//			}
//			RelativeLayout bgView = (RelativeLayout) currentSelectItem.findViewById(R.id.product_item_left);
//			TextView number = (TextView) currentSelectItem.findViewById(R.id.sale_product_item_number);
//			number.setBackgroundResource(R.color.transparent_background);
//			bgView.setBackgroundResource(R.color.txt_shopping_cat_item_select);
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clean_up_cancel_button:
			mDialog.dismiss();
			break;
		case R.id.clean_up_position:
			DealModel.getInstance().removeAll();
			mDialog.dismiss();
			break;
		}
	}

	private OrderContentData getCurrentOrderContentData() {
		if (DealModel.getInstance().getOrderProcessStatus() != DealModel.ORDER_PROCESS_SELECE)
			return null;
		Object tmp = getListView().getItemAtPosition(currentSelectItemPosition);
		OrderContentData ocd = null;
		if (tmp == null)
			return null;
		ocd = (OrderContentData) tmp;
		return ocd;
	}

	public void increaseProduct() {
		// 增加某个商品
		OrderContentData ocd = getCurrentOrderContentData();
		if (ocd != null) {
			ocd.addOneItem();
			DealModel.getInstance().getShoppingCart().update();
		}
	}

	public void decreaseProduct() {
		// 减少某个商品
		OrderContentData ocd = getCurrentOrderContentData();
		if (ocd != null) {
			ocd.subOneItem();
			DealModel.getInstance().getShoppingCart().update();
		}

	}

	public void deleteProduct() {
		OrderContentData ocd = getCurrentOrderContentData();
		if (ocd != null) {
			mAdapter.removePosition(currentSelectItemPosition);
			DealModel.getInstance().getShoppingCart().update();
//			final int count=DealModel.getInstance().getShoppingCart().getCount();
//			setSelection(count-1);
		}
	}

	public void putUpProduct() {

	}

	public void removeAllCart() {
		// 清空购物车
		mDialog = new AnyWhereDialog(getActivity(), 540, 280, 0, 0, R.layout.clean_up_shoppingcart,
				R.style.Theme_dialog1, Gravity.LEFT | Gravity.TOP, true);
		Button mCancelButton = (Button) mDialog.findViewById(R.id.clean_up_cancel_button);
		Button mPositiveButton = (Button) mDialog.findViewById(R.id.clean_up_position);
		mCancelButton.setOnClickListener(this);
		mPositiveButton.setOnClickListener(this);
		mDialog.show();
		// Object tmp =
		// getListView().getItemAtPosition(currentSelectItemPosition);
		// if (DealModel.getInstance().getOrderProcessStatus() !=
		// DealModel.ORDER_PROCESS_SELECE)
		// return;
		// if (tmp != null) {
		// OrderContentData ocd = (OrderContentData) tmp;
		// if (ocd.getProductId() != ShoppingCart.MANUID) {
		// AttributeAndFavorableDialogFragment af =
		// AttributeAndFavorableDialogFragment.newInstance(ocd
		// .getProduct().getProductID(),
		// ocd.getProduct().getProductCategoryID(), 1);
		// af.setPosition(currentSelectItemPosition -
		// getListView().getFirstVisiblePosition());
		// af.show(getFragmentManager(), "dialog");
		// af.setOrderContent(DealModel.getInstance().getShoppingCart()
		// .getOrderContentByIndex(currentSelectItemPosition));
		// DealModel.getInstance().getShoppingCart().setCurrentEditIndex(currentSelectItemPosition);
		// }
		// }
	}

	public void productAttribute() {
		Object tmp = getListView().getItemAtPosition(currentSelectItemPosition);
		if (DealModel.getInstance().getOrderProcessStatus() != DealModel.ORDER_PROCESS_SELECE)
			return;
		if (tmp != null) {
			OrderContentData ocd = (OrderContentData) tmp;
			if (ocd.getProductId() != ShoppingCart.MANUID) {
				AttributeAndFavorableDialogFragment af = AttributeAndFavorableDialogFragment.newInstance(ocd
						.getProduct().getProductID(), ocd.getProduct().getProductCategoryID(), 0);
				af.setPosition(currentSelectItemPosition - getListView().getFirstVisiblePosition());
				af.show(getFragmentManager(), "dialog");
				af.setOrderContent(DealModel.getInstance().getShoppingCart()
						.getOrderContentByIndex(currentSelectItemPosition));
				DealModel.getInstance().getShoppingCart().setCurrentEditIndex(currentSelectItemPosition);
			}
		}
	}

}
