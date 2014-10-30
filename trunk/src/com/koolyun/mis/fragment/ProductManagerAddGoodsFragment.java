package com.koolyun.mis.fragment;

import java.io.File;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.koolyun.mis.R;
import com.koolyun.mis.SaleActivity;
import com.koolyun.mis.adapter.AttributeAdapter;
import com.koolyun.mis.adapter.AttributeItemAdapter;
import com.koolyun.mis.adapter.CateAdapter;
import com.koolyun.mis.core.product.ProductAttribute;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.core.product.ProductData;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.fragment.ProductCategorySelectDialogFragment.OnProductCategoryItemSelectListener;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.widget.AnyWhereDialog;

public class ProductManagerAddGoodsFragment extends AbstractFragment implements OnClickListener, OnEditorActionListener {

	private View rootView = null;
	private ViewFlipper mViewFlipper = null;
	private EditText mProductName;
	private EditText mProductPrice;
	private FragmentManager fragmentManager;
	private DataManagerFragment mPrdmgFragment;
	private ProductData mProductData = new ProductData();
	ProductCategory userSelectcategory = null;
	private Button mSave = null;
	private Button mCateBtn = null;
	private Button mPrdMgAttrBtn;
	private Button deleteProduct = null;
	private ImageView mProductPic = null;
	// private Button attrBack = null;
	// private Button addAttr = null;

	private AnyWhereDialog mDialog = null;
	private CateAdapter adapter = null;

	private String oldImagePath = null;
	private ListView mPrdmg_good_listview;
	private ListView mPrdmg_add_attr_listview;

	private AttributeAdapter mAttributeAdapter = null;
	private AttributeItemAdapter mAttributeItemAdapter = null;

	private int defaultCateid = -1;
	boolean isAdd = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentManager = getFragmentManager();
		
//		BackStackEntry be = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1);
//		Fragment f=getFragmentManager().findFragmentById(be.getId());
//		MyLog.d("f="+f.toString());
		mPrdmgFragment = (DataManagerFragment) ((SaleActivity) getActivity()).getCurrentFragment();
		rootView = inflater.inflate(R.layout.productmanageraddgoodsproduct, container, false);
		mProductName = (EditText) rootView.findViewById(R.id.prdmg_goods_producteditinputname);
		mProductPrice = (EditText) rootView.findViewById(R.id.prdmg_goods_producteditinputprice);
		mProductPrice.setOnEditorActionListener(this);
		mCateBtn = (Button) rootView.findViewById(R.id.btn_prdmg_goods_product_cate);
		mCateBtn.setOnClickListener(this);
		mPrdMgAttrBtn = (Button) rootView.findViewById(R.id.btn_prdmg_goods_product_attr_add);
		mPrdMgAttrBtn.setOnClickListener(this);
		mSave = (Button) rootView.findViewById(R.id.btn_product_save);
		mProductPic = (ImageView) rootView.findViewById(R.id.pdtmg_show_pic_btn);
		deleteProduct = (Button) rootView.findViewById(R.id.btn_delete_product);
		deleteProduct.setOnClickListener(this);
		Bundle bundle = getArguments();
		if (bundle != null) {
			isAdd = bundle.getBoolean("isAdd");
			if(isAdd){
				deleteProduct.setVisibility(View.INVISIBLE);
			}
		}
		mSave.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (isAdd) {
			mProductName.setText("");
			mProductPrice.setText("");
		} else {
			mProductName.setText(mProductData.getProduct().getName());
			mProductName.setSelection(mProductName.getText().length());
			mProductPrice.setText(mProductData.getProduct().getPrice());
		}
		mCateBtn.setText(userSelectcategory.getProductCategoryName());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_prdmg_goods_product_cate:
			ProductCategorySelectDialogFragment pcsdf = null;
			if (userSelectcategory != null) {
				pcsdf = ProductCategorySelectDialogFragment.newInstance("", userSelectcategory);
			} else {
				pcsdf = ProductCategorySelectDialogFragment.newInstance("", mProductData.getmProductCategory());
			}
			pcsdf.setCategoryItemSelectListener(new OnProductCategoryItemSelectListener() {

				@Override
				public void onItemClick(ProductCategory oldProductCategory) {
					userSelectcategory = oldProductCategory;
					mCateBtn.setText(oldProductCategory.getProductCategoryName());
				}
			});
			pcsdf.show(getFragmentManager(), "dialog");
			break;
		case R.id.pdtmg_show_attr_pic_btn:
			mViewFlipper.setDisplayedChild(0);
			mAttributeItemAdapter.setAttrList(mProductData.getAttrList());
			// mAttributeAdapter.setCheckedList(mProductData.getAttrList());
			setListViewHeightBasedOnChildren(mPrdmg_good_listview);
			mAttributeItemAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_prdmg_goods_product_attr_add:
			ProductAttributeSelectDialogFragment pasdf = null;
			if (userSelectcategory != null) {
				pasdf = ProductAttributeSelectDialogFragment.newInstance("", userSelectcategory);
			} else {
				pasdf = ProductAttributeSelectDialogFragment.newInstance("", mProductData.getmProductCategory());
			}
			pasdf.setProductData(mProductData);
			pasdf.setCategoryItemSelectListener(null);
			pasdf.show(getFragmentManager(), "dialog");
			break;
		case R.id.pdtmg_show_pic_btn:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			// intent.addCategory(Intent.CATEGORY_APP_GALLERY);
			startActivityForResult(intent, 1);
			break;
		case R.id.btn_product_save:
			save();
			Common.HideKeyboardIfExist(this);
			break;
		case R.id.btn_delete_product:
			mDialog = new AnyWhereDialog(getActivity(), 540, 280, 0, 0, R.layout.confirm_delete, R.style.Theme_dialog1,
					Gravity.LEFT | Gravity.TOP, true);
			Button confirmBtn = (Button) mDialog.findViewById(R.id.confirm_delete);
			Button cancelBtn = (Button) mDialog.findViewById(R.id.cancel_delete);
			confirmBtn.setOnClickListener(this);
			cancelBtn.setOnClickListener(this);
			mDialog.show();
			break;
		case R.id.confirm_delete:
			delete();
			mDialog.dismiss();
			break;
		case R.id.cancel_delete:
			mDialog.dismiss();
			break;
		}
	}

	public void addNew(int protectCate) {
		this.mProductData = new ProductData();
		if (protectCate == -1)
			defaultCateid = -1;
		else
			defaultCateid = protectCate;
		userSelectcategory = ProductManager.getProductCategory(defaultCateid);
	}

	private void save() {
		if (!mProductName.getText().toString().isEmpty() && !mProductPrice.getText().toString().isEmpty()) {
			int ret = ProductManager.saveProductData(getProductData());
			for (ProductAttribute pa : mProductData.getAttrList()) {
				MyLog.d(pa.getName());
			}
			if (ret == -1) {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addgoods_fail, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addgoods_success, Toast.LENGTH_SHORT).show();
				mPrdmgFragment.updateProduct();
				mPrdmgFragment.updateCate();
				mPrdmgFragment.updateAttr();
				mPrdmgFragment.deleteFragment();
				Common.HideKeyboardIfExist(this);
			}

		} else
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addgoods_notnull, Toast.LENGTH_SHORT).show();
	}

	public ProductData getProductData() {
		if (mProductData == null) {
			mProductData = new ProductData();
		}

		mProductData.getProduct().setProductCategoryID(userSelectcategory.getProductCategoryId());
		mProductData.getProduct().setName(mProductName.getText().toString());
		mProductData.getProduct().setPrice(mProductPrice.getText().toString());

		if (oldImagePath != null) {
			File tmp = new File(oldImagePath);
			mProductData.getProduct().setProductPhoto(tmp.getName());
			JavaUtil.copyFileToDir(this.getActivity(), oldImagePath, Common.IMAGE_PATH);
		}

		return mProductData;
	}

	private void delete() {
		if (mProductData != null && mProductData.getProduct() != null)
			ProductManager.deleteProduct(mProductData.getProduct());
		mPrdmgFragment.updateProduct();
		mPrdmgFragment.updateCate();
		mPrdmgFragment.updateAttr();
		mPrdmgFragment.deleteFragment();
		Common.HideKeyboardIfExist(this);
	}

	private void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		if (listAdapter.getCount() > 0) {
			totalHeight = listAdapter.getView(0, null, listView).getMinimumHeight() * (listAdapter.getCount());
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 初始化商品的信息
	 * 
	 * @param mProductData
	 */
	public void setProductData(ProductData mProductData) {
		this.mProductData = mProductData;
		if (mProductData.getmProductCategory() != null) {
			userSelectcategory = mProductData.getmProductCategory();
			defaultCateid = mProductData.getmProductCategory().getProductCategoryId();

		} else
			defaultCateid = -1;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();

			oldImagePath = JavaUtil.getPathFromUri(uri, this.getActivity());

			ContentResolver cr = this.getActivity().getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				if (bitmap != null)
					mProductPic.setImageBitmap(Common.zoomBitmap(bitmap, 80, 80));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.prdmg_goods_producteditinputname:
				Common.HideKeyboardIfExist(ProductManagerAddGoodsFragment.this);
				mProductPrice.requestFocus();
				break;
			case R.id.prdmg_goods_producteditinputprice:
				Common.HideKeyboardIfExist(ProductManagerAddGoodsFragment.this);
				break;
			}
		}
		return false;
	}
}
