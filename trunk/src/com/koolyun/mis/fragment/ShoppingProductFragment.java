package com.koolyun.mis.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.OnSaleAdapter;
import com.koolyun.mis.adapter.ProductInfoAdapter;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.IProductSelector;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.product.Product;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.product.ProductPinyin;
import com.koolyun.mis.fragment.ProductListFragment.ComparatorProduct;
import com.koolyun.mis.util.MyLog;

/**
 * 商品列表界面
 */
public class ShoppingProductFragment extends AbstractFragment implements IProductSelector {

	private View root;
	List<Product> productList;
	private GridView mProductList;
	ProductInfoAdapter mProductInfoAdapter = null;
	private OnSaleAdapter onSaleAdapter = null;
	private List<ProductPinyin> pinyinList;

	private int type = 0;
	private ProductCategory category = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.shopping_product_fragment, container, false);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		productList = new ArrayList<Product>();
		pinyinList = new ArrayList<ProductPinyin>();
		mProductList = (GridView) root.findViewById(R.id.gv_product);
		onSaleAdapter = new OnSaleAdapter(getActivity());
		mProductInfoAdapter = new ProductInfoAdapter(getActivity(), productList, pinyinList);

	}

	public void displayProduct(int type, ProductCategory mProductCategory) {

		MyLog.d("---显示商品---");

		this.type = type;
		this.category = mProductCategory;

		if (type == 0) {
			mProductList.setAdapter(onSaleAdapter);
			mProductList.setOnItemClickListener(null);
		} else {
			productList.clear();
			pinyinList.clear();
			if (type == 1) {
				productList.addAll(ProductManager.getALLProduct());
				pinyinList.addAll(ProductManager.getProductPinyinList());
				Collections.sort(productList, new ComparatorProduct());
			} else {
				productList.addAll(ProductManager.getALLProductByCategory(category.getProductCategoryId()));
				pinyinList.addAll(ProductManager.getProductPinyinListByCataID(category.getProductCategoryId()));
				Collections.sort(productList, new ComparatorProduct());
			}
			mProductList.setAdapter(mProductInfoAdapter);
			mProductList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Product cproduct = (Product) parent.getItemAtPosition(position);
					if (cproduct != null) {
						List<OrderContentData> mList = new ArrayList<OrderContentData>();
						OrderContentData mOc = new OrderContentData();
						mOc.setProduct(cproduct);
						mOc.setCount(1);
						mList.add(mOc);
						addNewOrderContent(mList);
					}
				}
			});
		}
	}

	@Override
	public void addNewOrderContent(List<OrderContentData> mList) {
		DealModel.getInstance().addNewOrderContent(mList);
		
		final ShoppingCartFragment cartFragment = (ShoppingCartFragment) getFragmentManager().findFragmentById(R.id.SaleProductListFragment);
		final int count=DealModel.getInstance().getShoppingCart().getCount();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				cartFragment.setSelectItem(count-1);
			}
		}, 100);
	}
}
