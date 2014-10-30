package com.koolyun.mis.fragment;

import java.util.LinkedList;
import java.util.List;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.PriceAdapte;
import com.koolyun.mis.core.product.ProductAttribute;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.AnyWhereDialog;

/**
 * 商品属性编辑界面
 * 
 * @author ycb
 */
public class ProductManagerAttrFragment extends AbstractFragment implements OnClickListener, OnItemClickListener,
		OnCheckedChangeListener, OnEditorActionListener {
	private View result = null;
	private ListView mListView = null;
	private Button mPrdmg_attr_add_btn;
	private EditText mAttrName = null;
	private Button mAttrDeleteBtn = null;
	private FragmentManager fragmentManager;
	private DataManagerFragment mPrdmgFragment;
	private AnyWhereDialog mDelDialog = null;
	private RadioGroup mRadioGroup = null;
	private RadioButton mRadioSingle = null;
	private RadioButton mRadioMulti = null;
	private ProductAttribute mProductAttribute = new ProductAttribute();
	private List<ProductSubAttribute> mProductSubAttributeList = new LinkedList<ProductSubAttribute>();
	private Button mSaveBtn = null;
	private PriceAdapte mAttrAdapter = null;
	private AnyWhereDialog mDialog = null;
	private ImageView mBelowLine = null;
	boolean isAdd = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentManager = getFragmentManager();
		mPrdmgFragment = (DataManagerFragment) fragmentManager.findFragmentById(R.id.main_container);
		result = inflater.inflate(R.layout.productmanagerattr_layout, container, false);
		mListView = (ListView) result.findViewById(R.id.prdmg_attr_listView);
		mPrdmg_attr_add_btn = (Button) result.findViewById(R.id.prdmg_attr_add_btn);
		mAttrDeleteBtn = (Button) result.findViewById(R.id.prdmg_attr_delete_btn);
		mAttrName = (EditText) result.findViewById(R.id.prdmg_attr_add_inputedit);
		mAttrName.setOnEditorActionListener(this);
		mPrdmg_attr_add_btn.setOnClickListener(this);
		mAttrDeleteBtn.setOnClickListener(this);

		mRadioGroup = (RadioGroup) result.findViewById(R.id.check_btn_group);
		mRadioSingle = (RadioButton) result.findViewById(R.id.radio_single);
		mRadioMulti = (RadioButton) result.findViewById(R.id.radio_multi);
		mRadioGroup.setOnCheckedChangeListener(this);

		mBelowLine = (ImageView) result.findViewById(R.id.prdmg_attr_imageview_below);

		mSaveBtn = (Button) result.findViewById(R.id.btn_save_or_update);

		Bundle bundle = getArguments();
		if (bundle != null) {
			isAdd = bundle.getBoolean("isAdd");
			if(isAdd)
				mAttrDeleteBtn.setVisibility(View.INVISIBLE);
		}

		mSaveBtn.setOnClickListener(this);
		mAttrAdapter = new PriceAdapte(this, mProductSubAttributeList);
		mListView.setAdapter(mAttrAdapter);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListView.setItemsCanFocus(false);
		mListView.setClickable(true);
		mListView.setOnItemClickListener(this);
		mListView.setTextFilterEnabled(true);
		return result;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.check_btn_group:

			break;
		case R.id.radio_multi:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setListViewHeightBasedOnChildren(mListView);
	}

	public void setProductAttr(ProductAttribute mProductAttribute) {
		this.mProductAttribute = mProductAttribute;
	}

	public void hideKeyBoard() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Common.HideKeyboardIfExist(ProductManagerAttrFragment.this);
			}
		}, 500);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (mProductAttribute.getChoiceType() == 0) {
			mRadioSingle.setChecked(true);

		} else if (mProductAttribute.getChoiceType() == 1) {
			mRadioMulti.setChecked(true);
		}
		mAttrName.setText(mProductAttribute.getName());
		mProductSubAttributeList = ProductManager.getProductSubAttributeByAttrId(mProductAttribute
				.getProductAttributeID());
		mAttrAdapter.refresh(mProductSubAttributeList);
		int def = mProductAttribute.getDefaultChoice();
		for (int i = 0; i < mProductSubAttributeList.size(); i++) {
			mListView.setItemChecked(i, false);
		}
		for (int i = 0; i < mProductSubAttributeList.size(); i++) {
			if (mProductSubAttributeList.get(i).getProductSubAttributeId() == def) {
				mListView.setItemChecked(i, true);
				break;
			}
		}
		setListViewHeightBasedOnChildren(mListView);
	}

	public void resizeList() {
		setListViewHeightBasedOnChildren(mListView);
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

	public void save() {
		String attrNameStr = mAttrName.getText().toString();

		if (attrNameStr != null && !attrNameStr.isEmpty()) {
			mProductAttribute.setName(attrNameStr);
			mProductAttribute.setChoiceType(mRadioSingle.isChecked() ? 0 : 1);

			int index = mListView.getCheckedItemPosition();
			if (index != AdapterView.INVALID_POSITION) {
				int defaultSubId = (int) mAttrAdapter.getItemId(index);
				mProductAttribute.setDefaultChoice(defaultSubId);
			}

			int id = ProductManager.saveProductAttr(mProductAttribute);
			if (id == -1) {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addattr_fail, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addattr_success, Toast.LENGTH_SHORT).show();

				if (mProductSubAttributeList != null) {
					mProductAttribute.setProductAttributeID(id);
					for (int i = 0; i < mProductSubAttributeList.size(); i++) {
						mProductSubAttributeList.get(i).setProductAttributeID(id);
						mProductSubAttributeList.get(i).setEnable(1);
						int subid = ProductManager.saveProductSubAttr(mProductSubAttributeList.get(i));
						if (i == mListView.getCheckedItemPosition() && subid > 0) {
							ProductManager.updateProductDefaultSub(mProductAttribute, subid);
						}
					}

					mAttrAdapter.refresh(mProductSubAttributeList);
					setListViewHeightBasedOnChildren(mListView);
				}
				 mPrdmgFragment.deleteFragment();
				 mPrdmgFragment.updateAttr();
				Common.HideKeyboardIfExist(this);
			}

		} else {
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addattr_notnull, Toast.LENGTH_SHORT).show();
		}
	}

	public void remove() {
		if (mProductAttribute != null && mProductAttribute.getProductAttributeID() > 0) {
			ProductManager.deleteProductAttr(mProductAttribute);
			mAttrAdapter.refresh(null);
			 mPrdmgFragment.updateAttr();
			setListViewHeightBasedOnChildren(mListView);
		}
		 mPrdmgFragment.deleteFragment();
		Common.HideKeyboardIfExist(this);
	}

	public void showListBelowLine(boolean isseen) {
		if (isseen)
			mBelowLine.setVisibility(View.VISIBLE);
		else
			mBelowLine.setVisibility(View.INVISIBLE);
	}

	public void addNewSubAttr(ProductSubAttribute mProductSubAttribute) {
		if (mProductSubAttributeList.size() > 8) {
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addattr_toomany, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!checkSameInSubList(mProductSubAttribute)) {
			mProductSubAttributeList.add(mProductSubAttribute);
			mAttrAdapter.refresh(mProductSubAttributeList);
			setListViewHeightBasedOnChildren(mListView);
		} else {
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addattr_alreadyin, Toast.LENGTH_LONG).show();
		}
	}

	public boolean checkSameInSubList(ProductSubAttribute sub) {
		if (mProductSubAttributeList == null || sub == null)
			return false;
		for (int i = 0; i < mProductSubAttributeList.size(); i++) {
			if (mProductSubAttributeList.get(i).getName().equals(sub.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prdmg_attr_add_btn:
			// init();
			mDialog = new AnyWhereDialog(getActivity(), 540, 280, -100, -70, R.layout.attribute_factor_layout,
					R.style.Theme_dialog1, Gravity.LEFT | Gravity.TOP, true);
			mDialog.setProductManagerAtrrrFragment(this);
			mDialog.show();
			break;
		case R.id.prdmg_attr_delete_btn:
			mDelDialog = new AnyWhereDialog(getActivity(), 540, 280, 0, 0, R.layout.confirm_delete,
					R.style.Theme_dialog1, Gravity.LEFT | Gravity.TOP, true);
			Button confirmBtn = (Button) mDelDialog.findViewById(R.id.confirm_delete);
			Button cancelBtn = (Button) mDelDialog.findViewById(R.id.cancel_delete);
			confirmBtn.setOnClickListener(this);
			cancelBtn.setOnClickListener(this);
			mDelDialog.show();
			break;
		case R.id.confirm_delete:
			mDelDialog.dismiss();
			remove();
			break;
		case R.id.cancel_delete:
			mDelDialog.dismiss();
			break;
		case R.id.btn_save_or_update:
			save();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		setListItemChecked(arg2, true);
	}

	public void setListItemChecked(int index, boolean flag) {
		if (index >= 0 && index < mListView.getCount())
			mListView.setItemChecked(index, flag);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.prdmg_attr_add_inputedit:
				Common.HideKeyboardIfExist(ProductManagerAttrFragment.this);
				break;
			}
		}
		return false;
	}
}
