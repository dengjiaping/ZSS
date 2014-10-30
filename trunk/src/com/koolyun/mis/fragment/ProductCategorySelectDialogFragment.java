package com.koolyun.mis.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.CateAdapter;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.widget.AnyWhereDialog;

public class ProductCategorySelectDialogFragment extends AbstractDialogFragment {
	private AnyWhereDialog mDialog;
	private GridView gv_product_category;
	private CateAdapter adapter = null;
	private ProductCategory oldProductCategory;
	private OnProductCategoryItemSelectListener categoryItemSelectListener;

	public static ProductCategorySelectDialogFragment newInstance(String title, ProductCategory oldProductCategory) {
		ProductCategorySelectDialogFragment fragment = new ProductCategorySelectDialogFragment();
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
		adapter = new CateAdapter(this.getActivity(), oldProductCategory);

		gv_product_category = (GridView) mDialog.findViewById(R.id.gv_product_category);
		gv_product_category.setAdapter(adapter);
		gv_product_category.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				ProductCategory pc = (ProductCategory) parent.getItemAtPosition(position);
				ProductCategory oldPc=adapter.getOldProductCategory();
				if(pc.getProductCategoryId()!=oldPc.getProductCategoryId()){
					adapter.setOldProductCategory(pc);
					if (categoryItemSelectListener != null)
						categoryItemSelectListener.onItemClick(pc);
					adapter.notifyDataSetChanged();
				}
				
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
}
