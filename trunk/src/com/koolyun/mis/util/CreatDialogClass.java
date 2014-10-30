package com.koolyun.mis.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.koolyun.mis.LoginInActivity;
import com.koolyun.mis.R;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.widget.UserDialog;

public class CreatDialogClass implements OnClickListener {

	Activity mContex;
	UserDialog dialog = null;

	Button useraccountBtn = null;
	PopupWindow popup = null;
	Button userDialoglockaccount = null;
	Button unlockButton = null;
	View titalButton = null;
	Button unlock_saleactivityu_btn = null;
	Button changeaccountBtn = null;

	TextView mUseName = null;

	EditText unlock_saleactivity_edittext;
	View root = null;
	View titleView;

	public CreatDialogClass(Activity mContex) {
		super();
		this.mContex = mContex;
		titalButton = LayoutInflater.from(mContex).inflate(R.layout.sale_title_buttons, null);
		// munlockUI = (RelativeLayout)
		// titalButton.findViewById(R.id.useraccountshadebackground);

	}

	@SuppressLint("NewApi")
	public void show() {
		dialog = new UserDialog(mContex, 321, 245, -540, -200, R.layout.useraccount, R.style.Theme_dialog1,
				Gravity.LEFT, true);

		userDialoglockaccount = (Button) dialog.findViewById(R.id.userDialoglockaccount);
		changeaccountBtn = (Button) dialog.findViewById(R.id.userDialogchangeaccount);

		userDialoglockaccount = (Button) dialog.findViewById(R.id.userDialoglockaccount);
		changeaccountBtn = (Button) dialog.findViewById(R.id.userDialogchangeaccount);
		userDialoglockaccount.setOnClickListener(this);
		changeaccountBtn.setOnClickListener(this);

		mUseName = (TextView) dialog.findViewById(R.id.userDialogusername);

		if (AccountManager.getInstance().getCurrentAccount() != null) {
			mUseName.setText(AccountManager.getInstance().getCurrentAccount().getNameToshow());
		}

		View root = LayoutInflater.from(mContex).inflate(R.layout.lock_user_account, null);
		RelativeLayout lockshade = (RelativeLayout) root.findViewById(R.id.popup_user_account);
		unlock_saleactivity_edittext = (EditText) root.findViewById(R.id.unlock_user_account_edittext);
		// 注册editText编辑事件
		input_edit_password();

		lockshade.getBackground().setAlpha(150);
		// edit = (EditText)
		// root.findViewById(R.id.unlock_user_account_edittext);
		// munlockUI.setFocusable(true);
		unlockButton = (Button) root.findViewById(R.id.unlock_user_account_botton);
		unlockButton.setOnClickListener(this);
		popup = new PopupWindow(root, 1280, 55);
		if (!dialog.isShowing())
			dialog.show();
	}

	public void cancel() {
		dialog.cancel();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.userDialoglockaccount:
			cancel();
			popup.setFocusable(true);
			popup.showAtLocation(titalButton, Gravity.TOP, -100, -100);
			break;
		case R.id.userDialogchangeaccount:
			AccountManager.getInstance().logout();
			Intent loginintent = new Intent(mContex, LoginInActivity.class);
			mContex.startActivity(loginintent);
			dialog.dismiss();
			mContex.finish();
			break;
		case R.id.unlock_user_account_botton:
			input_unlock_user_account_botton();
			break;
		default:
			break;
		}

	}

	private void input_unlock_user_account_botton() {
		unlockButton.setVisibility(View.GONE);
		unlock_saleactivity_edittext.setVisibility(View.VISIBLE);
		unlock_saleactivity_edittext.requestFocus();
		unlock_saleactivity_edittext.setFocusableInTouchMode(true);
		InputMethodManager inputManager = (InputMethodManager) unlock_saleactivity_edittext.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(unlock_saleactivity_edittext, 0);
	}

	public void input_edit_password() {
		unlock_saleactivity_edittext.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					/*
					 * 在这判断输入密码是否正确
					 */
					if (AccountManager.getInstance().checkCurrentPassword(
							unlock_saleactivity_edittext.getText().toString())) {
						unlock_saleactivity_edittext.setVisibility(View.GONE);
						unlockButton.setVisibility(View.VISIBLE);
						InputMethodManager inputMgr = (InputMethodManager) mContex
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMgr.hideSoftInputFromWindow(unlock_saleactivity_edittext.getWindowToken(), 0);
						popup.setFocusable(false);
						popup.update();
						popup.dismiss();
					}
				}
				unlock_saleactivity_edittext.setText("");
				return false;
			}
		});
	}
}
