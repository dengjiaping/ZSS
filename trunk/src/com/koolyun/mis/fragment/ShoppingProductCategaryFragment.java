package com.koolyun.mis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.ProductCategoryAdapter;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.core.product.ProductManager;

/**
 * 商品列表界面
 */
public class ShoppingProductCategaryFragment extends AbstractFragment {
	private View root;
	private GridView categoryList = null;
	ProductCategoryAdapter mProductCategoryAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root =inflater.inflate(R.layout.shopping_product_fragment, container, false);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		categoryList = (GridView) root.findViewById(R.id.gv_product);
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
