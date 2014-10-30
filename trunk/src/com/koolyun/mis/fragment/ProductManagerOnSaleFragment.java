package com.koolyun.mis.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.AnyWhereDialog;

public class ProductManagerOnSaleFragment extends AbstractFragment implements OnCheckedChangeListener, OnClickListener,
		OnKeyListener, OnEditorActionListener {

	private View result = null;
	private Button mSaveButton = null;
	private Button mDeleteButton = null;
	private EditText mOnsaleName = null;
	private EditText mOnsaleEdit = null;
	private RadioGroup mRadioGroup = null;
	private Onsale mOnsale = new Onsale(-1, 0, "", "0", 1);
	private RadioButton mRadioMoney = null;
	private RadioButton mRadioPersent = null;
	private FragmentManager fragmentManager;
	private DataManagerFragment mPrdmgFragment;
	private AnyWhereDialog mDialog = null;
	
	boolean isAdd = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		result = inflater.inflate(R.layout.productmanager_onsal_layout, container, false);

		fragmentManager = getFragmentManager();
		mPrdmgFragment = (DataManagerFragment) fragmentManager.findFragmentById(R.id.main_container);

		mSaveButton = (Button) result.findViewById(R.id.btn_save_or_update);
		mDeleteButton = (Button) result.findViewById(R.id.btn_delete);
		mOnsaleName = (EditText) result.findViewById(R.id.prdmg_onsale_name_edittext);
		mOnsaleName.setOnEditorActionListener(this);
		mOnsaleEdit = (EditText) result.findViewById(R.id.prdmg_onsale_kind_edittext);
		mOnsaleEdit.setOnEditorActionListener(this);

		mOnsaleEdit.setText("-");
		mOnsaleEdit.setSelection(1);
		mOnsaleEdit.setOnKeyListener(this);
		mRadioGroup = (RadioGroup) result.findViewById(R.id.btn_group);

		mRadioMoney = (RadioButton) result.findViewById(R.id.radio_money);
		mRadioPersent = (RadioButton) result.findViewById(R.id.radio_percent);

		Bundle bundle = getArguments();
		if (bundle != null) {
			isAdd = bundle.getBoolean("isAdd");
			if(isAdd)
				mDeleteButton.setVisibility(View.INVISIBLE);
		}

		mSaveButton.setOnClickListener(this);
		mDeleteButton.setOnClickListener(this);

		mRadioGroup.setOnCheckedChangeListener(this);

		return result;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (mOnsale.getOnsaleID() != -1) {
			mOnsaleName.setText(mOnsale.getOnsaleName());
			mOnsaleEdit.setText(mOnsale.getValue());
			if (mOnsale.getValue() != null)
				mOnsaleEdit.setSelection(mOnsale.getValue().length());
			if (mOnsale.getOnsaleType() == 1) { // ¥
				mRadioMoney.setChecked(true);
			} else if (mOnsale.getOnsaleType() == 2) { // %
				mRadioPersent.setChecked(true);
			}
		}
	}

	public void setOnSale(Onsale mOnsale) {
		this.mOnsale = mOnsale;
	}

	public void addNewOnsale() {
		mOnsale = new Onsale(-1, 1, "", "0", 1);
	}

	public void save() {
		String name = mOnsaleName.getText().toString();
		String price = mOnsaleEdit.getText().toString();

		if (price.equals("-")) {
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addonsale_wrong, Toast.LENGTH_SHORT).show();
			return;
		}
		if (mOnsale.getOnsaleType() == 1) { // ¥
			double tmp = Double.parseDouble(price);
			if (tmp > 0) {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addonsale_wrong, Toast.LENGTH_SHORT).show();
				return;
			}
		} else if (mOnsale.getOnsaleType() == 2) { // %
			double tmp = Double.parseDouble(price);
			if (tmp > 0 || tmp < -100) {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addonsale_wrong, Toast.LENGTH_SHORT).show();
				return;
			}
		} else
			return;

		if (!name.isEmpty() && !price.isEmpty()) {
			mOnsale.setOnsaleName(name);

			if (mRadioMoney.isChecked()) {
				mOnsale.setOnsaleType(1);
			} else if (mRadioPersent.isChecked()) {
				mOnsale.setOnsaleType(2);
			}
			mOnsale.setValue(price);
			mOnsale.setEnable(1);
			int ret = ProductManager.saveOnSale(mOnsale);
			if (ret == -1) {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addonsale_fail, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addonsale_success, Toast.LENGTH_SHORT).show();
				addNewOnsale();
				Common.HideKeyboardIfExist(this);
				 mPrdmgFragment.updateOnsale();
				 mPrdmgFragment.deleteFragment();
			}
		} else {
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addonsale_notnull, Toast.LENGTH_SHORT).show();
		}
	}

	public void delete() {
		ProductManager.deleteOnsale(mOnsale);
		 mPrdmgFragment.updateOnsale();
		Common.HideKeyboardIfExist(this);
		 mPrdmgFragment.deleteFragment();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_money:
			mOnsale.setOnsaleType(1);
			if (!mOnsaleEdit.getText().toString().startsWith("-"))
				mOnsaleEdit.setText("-");
			mOnsaleEdit.setSelection(mOnsaleEdit.getText().length());
			break;
		case R.id.radio_percent:
			mOnsale.setOnsaleType(2);
			if (!mOnsaleEdit.getText().toString().startsWith("-"))
				mOnsaleEdit.setText("-");
			mOnsaleEdit.setSelection(mOnsaleEdit.getText().length());
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save_or_update:
			save();
			break;
		case R.id.btn_delete:
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

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
			switch (v.getId()) {
			case R.id.prdmg_onsale_kind_edittext:
				if (mOnsaleEdit.getText().toString().equals("-"))
					return true;
				break;
			}
		}
		return false;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.prdmg_onsale_name_edittext:
				mOnsaleEdit.requestFocus();
				break;
			case R.id.prdmg_onsale_kind_edittext:
				Common.HideKeyboardIfExist(ProductManagerOnSaleFragment.this);
				break;
			}
		}
		return false;
	}
}
