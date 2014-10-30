package com.koolyun.mis.fragment;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.OnSaleAdapter;
import com.koolyun.mis.adapter.ProductCategoryAdapter;
import com.koolyun.mis.adapter.ProductInfoAdapter;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.product.ProductPinyin;
import com.koolyun.mis.fragment.ProductListFragment.ComparatorProduct;
import com.koolyun.mis.widget.TEListView;

/**
 * 商品列表界面
 */
public class ProductCategaryManageFragment extends AbstractFragment {

	private TEListView categoryList = null;
	ProductCategoryAdapter mProductCategoryAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		categoryList = new com.koolyun.mis.widget.TEListView(getActivity());
		return categoryList;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		expand_listview_item();
	}

	private void expand_listview_item() {
		mProductCategoryAdapter = new ProductCategoryAdapter(getActivity(), ProductManager.getAllProductCategory());
		categoryList.setAdapter(mProductCategoryAdapter);

		final ShoppinigLeftFragment fragment = (ShoppinigLeftFragment) getFragmentManager().findFragmentById(
				R.id.shopping_left);
		categoryList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int type = (int) parent.getItemIdAtPosition(position);
				ProductCategory mProductCategory = (ProductCategory) parent.getItemAtPosition(position);
				fragment.enterIntoProduct(type, mProductCategory);
			}
		});
	}

}
