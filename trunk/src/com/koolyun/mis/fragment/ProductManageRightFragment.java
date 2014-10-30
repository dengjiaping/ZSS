package com.koolyun.mis.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.TotalMoneyObserver;

/**
 * 购物车fragment
 */
public class ProductManageRightFragment extends AbstractFragment implements TotalMoneyObserver, OnClickListener {

	private View saleList = null;
	private TextView txt_shopping_cat_total_money;
	ShoppingCartFragment cartFragment;
	private ImageButton ib_shopping_cart_add;
	private ImageButton ib_shopping_cart_decrease;
	private Button btn_shopping_cart_prod_attribute;
	private Button ib_shopping_cart_delete;
	private Button ib_shopping_cart_put_up;
	private Button btn_shopping_cart_prod_favorable;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 注册观察者
		DealModel.getInstance().getShoppingCart().registerObs(this);
		saleList = inflater.inflate(R.layout.sale_show_list, container, false);
		txt_shopping_cat_total_money = (TextView) saleList.findViewById(R.id.txt_shopping_cat_total_money);
		ib_shopping_cart_add = (ImageButton) saleList.findViewById(R.id.ib_shopping_cart_add);
		ib_shopping_cart_decrease = (ImageButton) saleList.findViewById(R.id.ib_shopping_cart_decrease);
		btn_shopping_cart_prod_attribute = (Button) saleList.findViewById(R.id.btn_shopping_cart_prod_attribute);
		ib_shopping_cart_delete = (Button) saleList.findViewById(R.id.btn_shopping_cart_delete_item);
		ib_shopping_cart_put_up = (Button) saleList.findViewById(R.id.ib_shopping_cart_put_up);
		btn_shopping_cart_prod_favorable = (Button) saleList.findViewById(R.id.btn_shopping_cart_delete_all);

		ib_shopping_cart_add.setOnClickListener(this);
		ib_shopping_cart_decrease.setOnClickListener(this);
		btn_shopping_cart_prod_attribute.setOnClickListener(this);
		ib_shopping_cart_delete.setOnClickListener(this);
		ib_shopping_cart_put_up.setOnClickListener(this);
		btn_shopping_cart_prod_favorable.setOnClickListener(this);
		return saleList;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		txt_shopping_cat_total_money.setText("￥" + 0.00);
		cartFragment = (ShoppingCartFragment) getFragmentManager().findFragmentById(R.id.SaleProductListFragment);
	}

	@Override
	public void totalMoneyChanged(String money) {
		txt_shopping_cat_total_money.setText("￥" + money);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 注销观察者
		DealModel.getInstance().getShoppingCart().unRegisterObs(this);
	}

	@Override
	public void onClick(View v) {
		if (cartFragment.getCurrentSelectItemPosition() == -1)
			return;
		switch (v.getId()) {
		case R.id.ib_shopping_cart_add:
			cartFragment.increaseProduct();
			break;
		case R.id.ib_shopping_cart_decrease:
			cartFragment.decreaseProduct();
			break;
		case R.id.btn_shopping_cart_prod_attribute:
			cartFragment.productAttribute();
			break;
		case R.id.btn_shopping_cart_delete_item:
			cartFragment.deleteProduct();
			break;
		case R.id.ib_shopping_cart_put_up:
			cartFragment.putUpProduct();
			break;
		case R.id.btn_shopping_cart_delete_all:
			cartFragment.removeAllCart();
			break;
		}
	}
}
