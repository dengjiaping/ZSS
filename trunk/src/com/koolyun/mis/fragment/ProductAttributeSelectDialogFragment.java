package com.koolyun.mis.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.AttributeAdapter;
import com.koolyun.mis.adapter.AttributeItemAdapter;
import com.koolyun.mis.adapter.CateAdapter;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.core.product.ProductData;
import com.koolyun.mis.widget.AnyWhereDialog;

public class ProductAttributeSelectDialogFragment extends AbstractDialogFragment {
	private AnyWhereDialog mDialog;
	private GridView gv_product_category;
	private AttributeAdapter mAttributeAdapter = null;
	private ProductCategory oldProductCategory;
	private OnProductCategoryItemSelectListener categoryItemSelectListener;
	private ProductData mProductData =null;
	public static ProductAttributeSelectDialogFragment newInstance(String title, ProductCategory oldProductCategory) {
		ProductAttributeSelectDialogFragment fragment = new ProductAttributeSelectDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putSerializable("category", oldProductCategory);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		oldProductCategory = (ProductCategory) getArguments().getSerializable("category");
		mDialog = new AnyWhereDialog(getActivity(), 710, 570, 0, 0, R.layout.product_category_select,
				R.style.Theme_dialog1, Gravity.CENTER, true);
		
		mAttributeAdapter = new AttributeAdapter(this.getActivity());
		mAttributeAdapter.setCheckedList(mProductData.getAttrList());
		gv_product_category = (GridView) mDialog.findViewById(R.id.gv_product_category);
		gv_product_category.setAdapter(mAttributeAdapter);
		gv_product_category.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
//				ProductCategory pc = (ProductCategory) parent.getItemAtPosition(position);
//				ProductCategory oldPc=adapter.getOldProductCategory();
//				if(pc.getProductCategoryId()!=oldPc.getProductCategoryId()){
				mAttributeAdapter.setonItemSelect(position);
//					if (categoryItemSelectListener != null)
//						categoryItemSelectListener.onItemClick(pc);
//				}
				
			}
		});
		return mDialog;
	}


	public void setCategoryItemSelectListener(OnProductCategoryItemSelectListener categoryItemSelectListener) {
		this.categoryItemSelectListener = categoryItemSelectListener;
	}

	public interface OnProductCategoryItemSelectListener {
		void onItemClick(ProductCategory oldProductCategory);
	}

	public void setProductData(ProductData productData) {
		mProductData=productData;
	}
}
