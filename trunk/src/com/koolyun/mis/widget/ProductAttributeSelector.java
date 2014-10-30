package com.koolyun.mis.widget;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;

import com.koolyun.mis.core.product.ProductAttribute;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.util.JavaUtil;

public class ProductAttributeSelector extends ButtonGroupBox implements OnClickListener {

	ProductAttribute mAttribute;
	List<ProductSubAttribute> mSubAttribute;
	ISimpleCallBack mCallBack = null;
	int mDefaultIndex = -1;
	int defaultSubID = 0;

	private int mCellWidth = 200;
	private int mCellHeight = 80;

	@SuppressLint("UseSparseArrays")
	public ProductAttributeSelector(Context context) {
		super(context);
	}

	public ProductAttributeSelector(Context context, ProductAttribute mAttribute,
			List<ProductSubAttribute> mSubAttribute, List<ProductSubAttribute> mSelectSubAttribute) {
		super(context, getGType(mAttribute.getChoiceType()), mSubAttribute.size());
		this.mAttribute = mAttribute;
		this.mSubAttribute = mSubAttribute;
		this.setHeight(100);
		for (int i = 0; i < count; i++) {
			mButtonList.get(i).setText(mSubAttribute.get(i).getName());
			mButtonList.get(i).setTag(mSubAttribute.get(i));
			mButtonList.get(i).setOnClickListener(this);
			if (mSelectSubAttribute == null && i == mDefaultIndex
					&& JavaUtil.isClass(mButtonList.get(i).getClass(), "android.widget.CompoundButton")) {
				CompoundButton mb = (CompoundButton) mButtonList.get(i);
				defaultSubID = mSubAttribute.get(i).getProductSubAttributeId();// TODO:
				mb.setChecked(true);
			}
		}
	}

	public void setISimpleCallBack(ISimpleCallBack mCallBack) {
		this.mCallBack = mCallBack;
	}

	static ButtonGroupBox.GroupType getGType(int cType) {
		switch (cType) {
		case 0:
			return ButtonGroupBox.GroupType.SINGLECHECK;
		case 1:
			return ButtonGroupBox.GroupType.MULTYCHECK;
		default:
			return ButtonGroupBox.GroupType.SINGLECHECK;
		}
	}

	public List<ProductSubAttribute> getSelectedSubArrri() {
		List<ProductSubAttribute> pTmpList = new LinkedList<ProductSubAttribute>();
		for (int i = 0; i < count; i++) {
			if (JavaUtil.isClass(mButtonList.get(i).getClass(), "android.widget.CompoundButton")) {
				CompoundButton mb = (CompoundButton) mButtonList.get(i);
				if (mb.isChecked())
					pTmpList.add((ProductSubAttribute) mb.getTag());
			}
		}

		return pTmpList;
	}

	public void InitButtonState(List<ProductSubAttribute> mSAList) {
		if (mSAList == null || mSAList.isEmpty())
			return;
		for (int i = 0; i < mSAList.size(); i++) {
			if (mSAList.get(i).getProductAttributeID() == mAttribute.getProductAttributeID()) {
				if (getSubAttrIndex(mSAList.get(i).getProductSubAttributeId()) >= 0) {
					CompoundButton mb = (CompoundButton) mButtonList.get(getSubAttrIndex(mSAList.get(i)
							.getProductSubAttributeId()));
					mb.setChecked(true);
				}
			}
		}
	}

	private int getSubAttrIndex(int mAttriID) {
		for (int i = 0; i < mSubAttribute.size(); i++) {
			if (mAttriID == mSubAttribute.get(i).getProductSubAttributeId())
				return i;
		}
		return -1;
	}

	@Override
	public void onClick(View v) {

		if (mCallBack != null)
			mCallBack.SimpleCallback();
	}
}
