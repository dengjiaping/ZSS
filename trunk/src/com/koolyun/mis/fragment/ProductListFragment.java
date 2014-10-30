package com.koolyun.mis.fragment;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.OnSaleAdapter;
import com.koolyun.mis.adapter.ProductCategoryAdapter;
import com.koolyun.mis.adapter.ProductInfoAdapter;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.IProductSelector;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.product.Product;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.product.ProductPinyin;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.MySideBar;

/**
 * 所有商品和分类商品
 */
public class ProductListFragment extends AbstractFragment implements View.OnClickListener, IProductSelector,
		AdapterView.OnItemClickListener, com.koolyun.mis.widget.MySideBar.OnTouchingLetterChangedListener {

	ListView mProductCateList = null;
	ListView mProductList = null;

	List<Product> productList;
	private int productCateIndex = -1;

	Button mSearchButton = null;
	ViewFlipper mViewTopFillper = null;
	ViewFlipper mListViewFillper = null;
	Button search_back = null;

	View allProduct = null;
	TextView mProductFirstTextView = null;
	TextView mProductSecondTextView = null;
	Button all_back = null;
	TextView overlay;

	ProductCategoryAdapter mProductCategoryAdapter = null;
	ProductInfoAdapter mProductInfoAdapter = null;
	private static final int PRODUCTCATE = 0;
	private static final int PRODUCTDETAIL = 1;

	int[] anmiArray = { R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out, };

	private void showProductLevel(int index) {
		Common.FlipperShowIndex(getActivity(), mListViewFillper, index, anmiArray);
	}

	//
	private com.koolyun.mis.widget.MySideBar myView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		allProduct = inflater.inflate(R.layout.produc_list, container, false);
		initAndset();
		return allProduct;
	}

	@Override
	public void onResume() {
		mProductCategoryAdapter.udateList(ProductManager.getAllProductCategory());
		if (productCateIndex >= 0) {
			List<ProductPinyin> pinyinList;
			if (productCateIndex == 0) {
				productList = ProductManager.getALLProduct();
				pinyinList = ProductManager.getProductPinyinList();
				Collections.sort(productList, new ComparatorProduct());
				mProductFirstTextView.setText(ProductListFragment.this.getActivity().getString(R.string.product_list));
			} else {
				productList = ProductManager.getALLProductByCategory(productCateIndex);
				pinyinList = ProductManager.getProductPinyinListByCataID(productCateIndex);
				Collections.sort(productList, new ComparatorProduct());
			}

			if (mViewTopFillper.getDisplayedChild() != 0)
				mViewTopFillper.setDisplayedChild(0);
			mProductInfoAdapter = new ProductInfoAdapter(getActivity(), productList, pinyinList);
			mProductList.setAdapter(mProductInfoAdapter);
		}
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void initAndset() {
		mProductCateList = (ListView) allProduct.findViewById(R.id.allproductinfolistview);
		mProductList = (ListView) allProduct.findViewById(R.id.allonsalelistview);

		expand_listview_item();
		mViewTopFillper = (ViewFlipper) allProduct.findViewById(R.id.producttopViewFlipper);

		mListViewFillper = (ViewFlipper) allProduct.findViewById(R.id.allKindProductFlipper);

		mProductFirstTextView = (TextView) allProduct.findViewById(R.id.productfirstsummary);
		mProductSecondTextView = (TextView) allProduct.findViewById(R.id.productsecondsummary);
		mSearchButton = (Button) allProduct.findViewById(R.id.searchbuttonfirst);
		search_back = (Button) allProduct.findViewById(R.id.search_back);
		all_back = (Button) allProduct.findViewById(R.id.all_back);
		mSearchButton.setOnClickListener(this);
		search_back.setOnClickListener(this);
		all_back.setOnClickListener(this);
		myView = (MySideBar) allProduct.findViewById(R.id.myView);
		overlay = (TextView) allProduct.findViewById(R.id.tvLetter);
		overlay.setVisibility(View.INVISIBLE);
		myView.setOnTouchingLetterChangedListener(this);

	}

	private void expand_listview_item() {
		mProductCategoryAdapter = new ProductCategoryAdapter(getActivity(), ProductManager.getAllProductCategory());
		mProductCateList.setAdapter(mProductCategoryAdapter);
		mProductCateList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (mListViewFillper.getDisplayedChild() != 1) {
					showProductLevel(PRODUCTDETAIL);
					int type = (int) arg0.getItemIdAtPosition(arg2);
					if (type == 0) {
						if (mViewTopFillper.getDisplayedChild() != 0)
							mViewTopFillper.setDisplayedChild(0);

						mProductList.setAdapter(new OnSaleAdapter(getActivity()));
						mProductList.setOnItemClickListener(null);
						mProductFirstTextView.setText(ProductListFragment.this.getActivity().getString(
								R.string.discountname));
					} else {
						List<ProductPinyin> pinyinList;
						if (type == 1) {
							productList = ProductManager.getALLProduct();
							productCateIndex = 0;
							pinyinList = ProductManager.getProductPinyinList();
							Collections.sort(productList, new ComparatorProduct());
							mProductFirstTextView.setText(ProductListFragment.this.getActivity().getString(
									R.string.allname));
						} else {
							ProductCategory mProductCategory = (ProductCategory) arg0.getItemAtPosition(arg2);
							productCateIndex = mProductCategory.getProductCategoryId();
							productList = ProductManager.getALLProductByCategory(mProductCategory
									.getProductCategoryId());
							pinyinList = ProductManager.getProductPinyinListByCataID(mProductCategory
									.getProductCategoryId());
							Collections.sort(productList, new ComparatorProduct());
							mProductFirstTextView.setText(mProductCategory.getProductCategoryName());
						}

						if (mViewTopFillper.getDisplayedChild() != 0)
							mViewTopFillper.setDisplayedChild(0);
						mProductInfoAdapter = new ProductInfoAdapter(getActivity(), productList, pinyinList);
						mProductList.setAdapter(mProductInfoAdapter);
						mProductList.setOnItemClickListener(ProductListFragment.this);
					}
					all_back.setVisibility(View.VISIBLE);

				}
			}
		});
	}

	public static final class ComparatorProduct implements Comparator<Product> {
		@SuppressWarnings("unchecked")
		@Override
		public int compare(Product p1, Product p2) {
			@SuppressWarnings("rawtypes")
			Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
			return cmp.compare(p1.getName(), p2.getName());
		}

	}

	@Override
	public void addNewOrderContent(List<OrderContentData> mList) {
		DealModel.getInstance().addNewOrderContent(mList);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Product cproduct = (Product) arg0.getItemAtPosition(arg2);

		if (cproduct != null) {
			List<OrderContentData> mList = new ArrayList<OrderContentData>();
			OrderContentData mOc = new OrderContentData();
			mOc.setProduct(cproduct);
			mOc.setCount(1);
			mList.add(mOc);
			addNewOrderContent(mList);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchbuttonfirst:
			searchButton();
			break;
		case R.id.search_back:
			search_back();
			break;
		case R.id.manual_input:
			// manualInput();
			break;
		case R.id.all_back:
			all_back();
			break;
		default:
			break;
		}
	}

	private void searchButton() {
		// mViewTopFillper.setDisplayedChild(1);

		if (mListViewFillper.getDisplayedChild() == 0) {

			showProductLevel(PRODUCTDETAIL);
			search_back.setVisibility(View.GONE);
			if (mListViewFillper.getDisplayedChild() == 0) {
				mProductSecondTextView.setText(R.string.allname);
			} else if (mListViewFillper.getDisplayedChild() == 1) {
				mProductSecondTextView.setText(R.string.allname);
			}

		} else if (mListViewFillper.getDisplayedChild() == 1) {
			search_back.setVisibility(View.VISIBLE);
			showProductLevel(PRODUCTDETAIL);
			mProductSecondTextView.setText(R.string.discountname);
		}

	}

	private void search_back() {
		mViewTopFillper.setDisplayedChild(0);
		showProductLevel(PRODUCTCATE);
		all_back.setVisibility(View.GONE);
		productCateIndex = -1;
	}

	public void all_back() {
		if (mListViewFillper.getDisplayedChild() != 0) {
			showProductLevel(PRODUCTCATE);
			productCateIndex = -1;
			mViewTopFillper.setDisplayedChild(0);
			mProductFirstTextView.setText(R.string.product_list);
			all_back.setVisibility(View.GONE);
		}
	}

	@Override
	public void onTouchingLetterChanged(String s) {
		overlay.setText(s);
		if (mProductInfoAdapter != null) {
			int pos = mProductInfoAdapter.GetPositionByPinyin(s);
			if (pos >= 0) {
				mProductList.smoothScrollToPositionFromTop(pos, 0);
			}
		}

	}

	@Override
	public void onTouchingTextViewChanged(boolean flag) {
		if (flag == true) {
			overlay.setVisibility(View.VISIBLE);
		} else {
			overlay.setVisibility(View.GONE);
		}

	}
}
