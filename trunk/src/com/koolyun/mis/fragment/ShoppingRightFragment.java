package com.koolyun.mis.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.TotalMoneyObserver;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.util.pay.SmartPosPayEx;

/**
 * 购物车fragment
 */
public class ShoppingRightFragment extends AbstractFragment implements TotalMoneyObserver, OnClickListener {

	private View saleList = null;
	private RelativeLayout rl_go_to_pay;
	private TextView txt_shopping_cat_total_money;
	ShoppingCartFragment cartFragment;
	private ImageButton ib_shopping_cart_add;
	private ImageButton ib_shopping_cart_decrease;
	private Button btn_shopping_cart_prod_attribute;
	private Button btn_shopping_cart_delete_item;
	private Button ib_shopping_cart_put_up;
	private Button btn_shopping_cart_delete_all;

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
		btn_shopping_cart_delete_item = (Button) saleList.findViewById(R.id.btn_shopping_cart_delete_item);
		ib_shopping_cart_put_up = (Button) saleList.findViewById(R.id.ib_shopping_cart_put_up);
		btn_shopping_cart_delete_all = (Button) saleList.findViewById(R.id.btn_shopping_cart_delete_all);
		rl_go_to_pay = (RelativeLayout) saleList.findViewById(R.id.rl_go_to_pay);

		rl_go_to_pay.setOnClickListener(this);
		ib_shopping_cart_add.setOnClickListener(this);
		ib_shopping_cart_decrease.setOnClickListener(this);
		btn_shopping_cart_prod_attribute.setOnClickListener(this);
		btn_shopping_cart_delete_item.setOnClickListener(this);
		ib_shopping_cart_put_up.setOnClickListener(this);
		btn_shopping_cart_delete_all.setOnClickListener(this);
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

		if (v.getId() == R.id.ib_shopping_cart_put_up) {
			DealModel.getInstance().HangUpCurrentOrder();
			return;
		}
		if (v.getId() == R.id.rl_go_to_pay) {
			DealModel.getInstance().beginToPayTheOrder();
			MyLog.i("--DealModel.getInstance().getOrderData().getCurrentOrder().getBillNo()--"+DealModel.getInstance().getOrderData().getCurrentOrder().getBillNo());
			SmartPosPayEx.startPay(getActivity(), DealModel.getInstance().getShoppingCart().getTotalAmount()
					.replace(".", ""),getActivity().getPackageName(),DealModel.getInstance().getOrderData().getCurrentOrder().getBillNo(),"");
			return;
		}

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
		case R.id.btn_shopping_cart_delete_all:
			cartFragment.removeAllCart();
			break;
		}
	}
}
