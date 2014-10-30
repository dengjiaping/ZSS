package com.koolyun.mis.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.ProductAttributeDialogAdapter;
import com.koolyun.mis.adapter.ProductFavorableDialogAdapter;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.product.GridViewProductAttribute;
import com.koolyun.mis.core.product.ProductAttribute;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.widget.AnyWhereDialog;
import com.koolyun.mis.widget.ISimpleCallBack;
import com.koolyun.mis.widget.ProductAttributeSelector;

@SuppressLint("HandlerLeak")
public class AttributeAndFavorableDialogFragment extends AbstractDialogFragment implements OnCheckedChangeListener,
		ISimpleCallBack {

	private int position = 0;
	private int defaultSelect;
	private AnyWhereDialog mDialog;
	RadioGroup radioGroup = null;
	RadioButton[] radioBtnArray = new RadioButton[2];
	private ImageView mArrowNo;
	private OrderContentData mOrderContent;
	private ViewFlipper mDialogViewFlipper;
	// private ListView lv_attr;
	// private ListView lv_favorable;
	private GridView gv_product_attr;
	private List<GridViewProductAttribute> product_attr_list = new ArrayList<GridViewProductAttribute>();
	private ProductAttributeDialogAdapter mProductAttributeDialogAdapter = null;
	private GridView gv_product_favorable;
	private List<Onsale> mOnSaleList = null;
	private List<ProductAttributeSelector> mProductAttributeSelectorList = new LinkedList<ProductAttributeSelector>();
	private ProductFavorableDialogAdapter mProductFavorableDialogAdapter = null;

	/**
	 * 创建Fragment对话框实例
	 *
	 * @param title
	 *            ：指定对话框的标题。
	 * @return：Fragment对话框实例。
	 */
	public static AttributeAndFavorableDialogFragment newInstance(int productid, int productCata, int defaultSelect) {
		AttributeAndFavorableDialogFragment frag = new AttributeAndFavorableDialogFragment();
		Bundle args = new Bundle();
		// 自定义的标题
		args.putInt("productID", productid);
		args.putInt("productCata", productCata);
		args.putInt("defaultSelect", defaultSelect);
		frag.setArguments(args);
		return frag;
	}

	/**
	 * 覆写Fragment类的onCreateDialog方法，在DialogFragment的show方法执行之后， 系统会调用这个回调方法。
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDialog = new AnyWhereDialog(getActivity(), 730, 560, -200, 0, R.layout.attribute_and_favorable_dialog,
				R.style.Theme_dialog1, Gravity.LEFT | Gravity.TOP, true);
		this.defaultSelect = getArguments().getInt("defaultSelect");
		initView();
		MyLog.d("position=" + position);
		setShowPositon(position);
		radioGroup.setOnCheckedChangeListener(this);
		if (this.defaultSelect == 0) {
			radioBtnArray[0].setChecked(true);
			radioBtnArray[1].setChecked(false);
		} else {
			radioBtnArray[0].setChecked(false);
			radioBtnArray[1].setChecked(true);
		}

		int productID = getArguments().getInt("productID");

		if (productID != ShoppingCart.MANUID) {
			List<ProductAttribute> mProductAttributeList = ProductManager.getProductAttributeById(productID, true);

			for (int i = 0; i < mProductAttributeList.size(); i++) {
				String pname = mProductAttributeList.get(i).getName();
				int ptype = 0;
				int pid = mProductAttributeList.get(i).getProductAttributeID();
				int ppid = -1;
				int pselectType = mProductAttributeList.get(i).getChoiceType();
				int pstatue = 0;
				GridViewProductAttribute attrParent = new GridViewProductAttribute(ptype, pname, pid, ppid,
						pselectType, pstatue);
				product_attr_list.add(attrParent);
				// ShoppingCart mShoppingCart
				// =DealModel.getInstance().getShoppingCart();
				List<ProductSubAttribute> mProductSubAttributeList = ProductManager
						.getProductSubAttributeByAttrId(mProductAttributeList.get(i).getProductAttributeID());
				for (int j = 0; j < mProductSubAttributeList.size(); j++) {
					String cname = mProductSubAttributeList.get(j).getName();
					int ctype = 1;
					int cid = mProductSubAttributeList.get(j).getProductSubAttributeId();
					int ccpid = mProductSubAttributeList.get(j).getProductAttributeID();
					int cselectType = pselectType;
					int cstatue = 0;
					List<ProductSubAttribute> temp = mOrderContent.getProductSubAttrList();
					for (ProductSubAttribute productSubAttribute : temp) {
						if (productSubAttribute.getProductSubAttributeId() == cid) {
							cstatue = 1;
						}
					}
					GridViewProductAttribute cttrParent = new GridViewProductAttribute(ctype, cname, cid, ccpid,
							cselectType, cstatue);

					// List<ProductSubAttribute> data =
					// mShoppingCart.getOrderContentById(position).getProductSubAttrList();
					cttrParent.setAttchSubAttribute(mProductSubAttributeList.get(j));
					product_attr_list.add(cttrParent);
				}

				// ProductAttributeSelector mAttrSelector = new
				// ProductAttributeSelector(this.getActivity(),
				// mProductAttributeList.get(i), mProductSubAttributeList,
				// null);
				// mAttrSelector.setISimpleCallBack(this);
				// mProductAttributeSelectorList.add(mAttrSelector);
				// layoutAttribute.addView(mAttrSelector);
			}
			mProductAttributeDialogAdapter = new ProductAttributeDialogAdapter(getActivity(), product_attr_list,
					mOrderContent);
			gv_product_attr.setAdapter(mProductAttributeDialogAdapter);
			gv_product_attr.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					GridViewProductAttribute pa = (GridViewProductAttribute) parent.getItemAtPosition(position);
					// 子控件可以选中
					if (pa.getType() != 0) {
						// 判断该子控件是不是已经选中了，原来没有选中
						if (pa.getStatue() == 0) {

							if (pa.getSelectType() == 0) {// 父控件是单选的
								// 单选的话，清空原来的选中状态
								for (GridViewProductAttribute productAttribute : product_attr_list) {
									if (productAttribute.getPid() == pa.getPid()) {
										productAttribute.setStatue(0);
									}
								}
							}
							pa.setStatue(1);
						} else {// 原来选中了
							pa.setStatue(0);
						}

					}
					List<ProductSubAttribute> mTmp = new LinkedList<ProductSubAttribute>();
					for (int i = 0; i < product_attr_list.size(); i++) {
						GridViewProductAttribute gvpa = product_attr_list.get(i);
						if (gvpa.getStatue() == 1) {
							mTmp.add(gvpa.getAttchSubAttribute());
						}
					}
					mOrderContent.setProductSubAttrList(mTmp);
					mProductAttributeDialogAdapter.notifyDataSetChanged();
					DealModel.getInstance().getShoppingCart().update();
				}
			});

		}
		mOnSaleList = mOrderContent.getOnsale();
		mProductFavorableDialogAdapter = new ProductFavorableDialogAdapter(getActivity(), mOnSaleList);
		gv_product_favorable.setAdapter(mProductFavorableDialogAdapter);
		gv_product_favorable.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Onsale onsale = (Onsale) parent.getItemAtPosition(position);
				// 子控件可以选中
				// 判断该子控件是不是已经选中了，原来没有选中
				boolean isSlected = false;
				for (int i = 0; i < mOnSaleList.size(); i++) {
					// 如果是选中的，点击了，就去掉选中
					if (onsale.getOnsaleID() == mOnSaleList.get(i).getOnsaleID()) {
						isSlected = true;
						mOnSaleList.remove(i);
					}
				}
				if (!isSlected) {
					mOnSaleList.add(onsale);
				}
				mOrderContent.setOnsale(mOnSaleList);
				DealModel.getInstance().getShoppingCart().update();
				mProductFavorableDialogAdapter.notifyDataSetChanged();
				// List<ProductSubAttribute> mTmp = new
				// LinkedList<ProductSubAttribute>();
				// for (int i = 0; i < product_attr_list.size(); i++) {
				// GridViewProductAttribute gvpa = product_attr_list.get(i);
				// if (gvpa.getStatue() == 1) {
				// mTmp.add(gvpa.getAttchSubAttribute());
				// }
				// }
				// mOrderContent.setProductSubAttrList(mTmp);

			}
		});
		return mDialog;
	}

	private void setShowPositon(int position) {
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mArrowNo.getLayoutParams();
		params.setMargins(-7, 90 + position * 83, 0, 0);
		mArrowNo.setLayoutParams(params);
	}

	public void setPosition(int position) {
		this.position = position;
	}

	private void initView() {
		radioGroup = (RadioGroup) mDialog.findViewById(R.id.group);
		radioBtnArray[0] = (RadioButton) mDialog.findViewById(R.id.product_attr);
		radioBtnArray[1] = (RadioButton) mDialog.findViewById(R.id.product_favorable);
		mArrowNo = (ImageView) mDialog.findViewById(R.id.iv_attr_Favorable_arrow);
		mDialogViewFlipper = (ViewFlipper) mDialog.findViewById(R.id.dialog_viewflipper);
		mDialogViewFlipper.setDisplayedChild(0);
		mDialogViewFlipper.setAnimation(null);
		gv_product_attr = (GridView) mDialog.findViewById(R.id.gv_product_attr);
		gv_product_favorable = (GridView) mDialog.findViewById(R.id.gv_product_favorable);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		MyLog.d(checkedId + "---");
		if (checkedId == R.id.product_attr) {
			mDialogViewFlipper.setDisplayedChild(0);
		} else {
			mDialogViewFlipper.setDisplayedChild(1);
		}

	}

	public void setOrderContent(OrderContentData orderContent) {
		this.mOrderContent = orderContent;
	}

	@Override
	public void SimpleCallback() {
		// TODO Auto-generated method stub

	}
}
