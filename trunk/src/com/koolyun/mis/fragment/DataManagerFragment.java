package com.koolyun.mis.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.ProductAttrManagerAdapter;
import com.koolyun.mis.adapter.ProductCategoryManagerAdapter;
import com.koolyun.mis.adapter.ProductManagerAdapter;
import com.koolyun.mis.adapter.ProductOnsaleManagerAdapter;
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.core.product.Product;
import com.koolyun.mis.core.product.ProductAttribute;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.core.product.ProductData;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.MyLog;

public class DataManagerFragment extends AbstractFragment implements OnClickListener, AdapterView.OnItemClickListener,
		OnCheckedChangeListener {

	private View rootView;

	ViewFlipper mViewFillper = null;
	RadioGroup radioGroup = null;
	RadioButton[] radioBtnArray = new RadioButton[3];

	ListView mProductCategaryManageListView = null;
	ProductCategoryManagerAdapter mProductCategoryManagerAdapter = null;
	ListView mProductManageListView = null;
	ProductManagerAdapter mProductInfoAdapter = null;
	ListView mProductAttrManageListView = null;
	ProductAttrManagerAdapter mProductAttrManagerAdapter = null;
	ListView mProductOnsaleManageListView = null;
	ProductOnsaleManagerAdapter mProductOnsaleManagerAdapter = null;

	private Fragment currentRightFragment = null;
	private ProductCategory mCurrentProductCategory = null;

	private View currentEditProduct;
	private int currentEditProductPosition;

	private Button btn_add;
	private Button btn_product_category;
	private TextView txt_product_name;
	// private Button btn_product_manual_input;

	private static final int PRODUCT_CATE = 0;
	private static final int PRODUCT_GOODS = 1;
	private static final int PRODUCT_ATTR = 2;
	private static final int PRODUCT_ONSALE = 3;
	private int currentIndex = PRODUCT_CATE;

	private int currentRightType = -1;
	ProductManagerAddGoodsFragment mfm = null;
	ProductManagerAddKindFragment mkm = null;
	ProductManagerAttrFragment mam = null;
	ProductManagerOnSaleFragment msm = null;

	int[] anmiArray = { R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out,
			R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out };

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.productmanagerfragment_layout, container, false);
		initAndset();
		setTitle(getResources().getString(R.string.product_list));
		showNavegateBack(false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// productManageRightFragment = (ProductManageRightFragment)
		// getFragmentManager().findFragmentById(
		// R.id.productShowItemFragment);

	}

	public void showChildIndex(int index) {
		currentIndex = index;
		Common.FlipperShowIndex(getActivity(), mViewFillper, index, anmiArray);
	}

	public int getCurrentPage() {
		return currentIndex;
	}

	public void setTitle(String title) {
		txt_product_name.setText(title);
	}

	public void showNavegateBack(boolean back) {
		if (back) {
			btn_product_category.setVisibility(View.VISIBLE);
		} else {
			btn_product_category.setVisibility(View.INVISIBLE);
		}
	}

	private void initAndset() {
		mViewFillper = (ViewFlipper) rootView.findViewById(R.id.allproductFlipper);
		radioGroup = (RadioGroup) rootView.findViewById(R.id.group);
		radioGroup.setOnCheckedChangeListener(this);
		radioBtnArray[0] = (RadioButton) rootView.findViewById(R.id.rb_product_category);
		radioBtnArray[1] = (RadioButton) rootView.findViewById(R.id.rb_product_attr);
		radioBtnArray[2] = (RadioButton) rootView.findViewById(R.id.rb_product_onsale);

		btn_product_category = (Button) rootView.findViewById(R.id.btn_product_category);
		btn_add = (Button) rootView.findViewById(R.id.btn_add);
		txt_product_name = (TextView) rootView.findViewById(R.id.txt_product_name);
		// btn_product_manual_input = (Button)
		// rootView.findViewById(R.id.btn_product_manual_input);

		btn_product_category.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		// btn_product_manual_input.setOnClickListener(this);
		mProductCategaryManageListView = (ListView) rootView.findViewById(R.id.lv_product_categary_manage);
		mProductManageListView = (ListView) rootView.findViewById(R.id.lv_product_manage);
		mProductAttrManageListView = (ListView) rootView.findViewById(R.id.lv_product_attr_listView);
		mProductOnsaleManageListView = (ListView) rootView.findViewById(R.id.lv_product_onsale_listView);

		mProductCategoryManagerAdapter = new ProductCategoryManagerAdapter(this, ProductManager.getAllProductCategory());
		mProductCategaryManageListView.setAdapter(mProductCategoryManagerAdapter);
		// mProductCategaryManageListView.setOnItemClickListener(new
		// OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// int type = (int) parent.getItemIdAtPosition(position);
		// ProductCategory mProductCategory = (ProductCategory)
		// parent.getItemAtPosition(position);
		// // cu.enterIntoProduct(type, mProductCategory);
		// }
		// });
		mProductCategaryManageListView.setOnItemClickListener(this);

		mProductAttrManagerAdapter = new ProductAttrManagerAdapter(this, ProductManager.getAllProductAttribute());
		mProductAttrManageListView.setAdapter(mProductAttrManagerAdapter);

		mProductOnsaleManagerAdapter = new ProductOnsaleManagerAdapter(this, OrderManager.getAllOnSaleList());
		mProductOnsaleManageListView.setAdapter(mProductOnsaleManagerAdapter);

		mProductAttrManageListView.setOnItemClickListener(this);
		mProductOnsaleManageListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_product_category:
			MyLog.d("btn_product_category");
			// TODO 记录上一次的类型,返回的时候直接返回到对应的类型列表
			if (currentIndex != PRODUCT_CATE) {
				showChildIndex(PRODUCT_CATE);
			}
			setTitle("");
			showNavegateBack(false);
			deleteFragment();
			break;
		case R.id.btn_add:
			if (currentIndex == PRODUCT_CATE) {
				showRightFragment(PRODUCT_CATE);
				mkm.addNew();
				setParamsFragment(mkm, true);
			} else if (currentIndex == PRODUCT_GOODS) {
				showRightFragment(PRODUCT_GOODS);
				mfm.addNew(mCurrentProductCategory.getProductCategoryId());
				setParamsFragment(mfm,true);
			} else if (currentIndex == PRODUCT_ATTR) {
				showRightFragment(PRODUCT_ATTR);
				setParamsFragment(mam,true);
			} else if (currentIndex == PRODUCT_ONSALE) {
				showRightFragment(PRODUCT_ONSALE);
				msm.addNewOnsale();
				setParamsFragment(msm, true);
			}
			break;
		default:
			break;
		}
	}

	public void enterIntoProduct(int type, ProductCategory mProductCategory) {
		// MyLog.d("type=" + type + ",categoryId=" +
		// mProductCategory.getProductCategoryId());
		// MyLog.d("getDisplayedChild=" + mViewFillper.getDisplayedChild());
		if (mViewFillper.getDisplayedChild() != 1) {
			showChildIndex(PRODUCT_GOODS);
			showNavegateBack(true);
			if (mProductCategory != null) {
				setTitle(mProductCategory.getProductCategoryName());
			} else {
				if (type == 0) {
					setTitle(getResources().getString(R.string.discountname));
				} else {
					setTitle(getResources().getString(R.string.allname));
				}
			}
			// mProductFragment.displayProduct(type, mProductCategory);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}

	private void setParamsFragment(Fragment fragment, boolean isAdd) {
		Bundle bundle = new Bundle();
		bundle.putBoolean("isAdd", isAdd);
		fragment.setArguments(bundle);
	}

	void deleteFragment() {
		if (currentRightFragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.obj_push_left_in, R.anim.obj_push_left_out,
					R.anim.obj_push_right_in, R.anim.obj_push_right_out);
			fragmentTransaction.remove(currentRightFragment);
			fragmentTransaction.commitAllowingStateLoss();
		}
	}

	public void setEditProdect(int productID) {
		ProductData mProductData = new ProductData();
		mProductData.setProduct(ProductManager.getProductByProductId(productID));
		showRightFragment(PRODUCT_GOODS);
		setParamsFragment(mfm, false);
		mfm.setProductData(mProductData);
	}

	public void setEditProductCate(int productCate) {
		ProductCategory mProductCategory = ProductManager.getProductCategory(productCate);
		showRightFragment(PRODUCT_CATE);
		mkm.setProductCategory(mProductCategory);
	}

	void showRightFragment(int fragtype) {
		currentRightType = fragtype;
		switch (fragtype) {
		case PRODUCT_CATE:
			mkm = new ProductManagerAddKindFragment();
			addNewFragment(mkm);
			
			break;
		case PRODUCT_GOODS:
			mfm = new ProductManagerAddGoodsFragment();
			addNewFragment(mfm);
			break;
		case PRODUCT_ATTR:
			mam = new ProductManagerAttrFragment();
			addNewFragment(mam);
			break;
		case PRODUCT_ONSALE:
			msm = new ProductManagerOnSaleFragment();
			addNewFragment(msm);
			break;
		default:
			break;
		}
	}

	void addNewFragment(Fragment fragment) {
		currentRightFragment = fragment;
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.obj_push_left_in, R.anim.obj_push_left_out,
				R.anim.obj_push_right_in, R.anim.obj_push_right_out);
		fragmentTransaction.replace(R.id.productShowItemFragment, fragment);
		fragmentTransaction.commit();
	}

	/**
	 * 更新产品信息
	 */
	public void updateProduct() {
		// if (currentEditProduct != null) {
		// RelativeLayout bg = (RelativeLayout)
		// currentEditProduct.findViewById(R.id.item_bg);
		// TextView name = (TextView)
		// currentEditProduct.findViewById(R.id.name);
		// ImageView icon = (ImageView)
		// currentEditProduct.findViewById(R.id.icon);
		// icon.setImageResource(R.drawable.product_manage_edit_normal);
		// bg.setBackgroundResource(R.color.v2_gridview_item_bg_nor);
		// name.setTextColor(getActivity().getResources().getColor(R.color.v2_product_display_normal));
		// }
		mProductInfoAdapter.updateProductList();
	}

	/**
	 * 更新分类信息
	 */
	public void updateCate() {
		mProductCategoryManagerAdapter.updateCategory(ProductManager.getAllProductCategory());
	}

	/**
	 * 更新属性信息
	 */
	public void updateAttr() {
		mProductAttrManagerAdapter.updateAttr(ProductManager.getAllProductAttribute());
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		MyLog.d(checkedId + "---");
	}

	public void setEditProductAttr(int productAttributeID) {
		ProductAttribute mProductAttribute = ProductManager.getProductAttributeByAttrId(productAttributeID);
		showRightFragment(PRODUCT_ATTR);
		mam.setProductAttr(mProductAttribute);
	}

	public void setEditProductOndale(int onsaleID) {
		Onsale mOnsale = ProductManager.getOnSaleByid(onsaleID);
		showRightFragment(PRODUCT_ONSALE);
		msm.setOnSale(mOnsale);
	}

	public void updateOnsale() {
		mProductOnsaleManagerAdapter.updateOnsale(OrderManager.getAllOnSaleList());
	}
}