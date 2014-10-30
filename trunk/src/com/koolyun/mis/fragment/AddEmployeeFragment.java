package com.koolyun.mis.fragment;

import java.util.Locale;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.CynovoHttpClient;
import com.koolyun.mis.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ValidFragment")
public class AddEmployeeFragment extends AbstractLoadingFragment implements OnEditorActionListener, OnClickListener {
	private Account mCurrentEditAccount = new Account();
	private TextView add_phone;
	private EditText firstNameEdit;
	private EditText lastNameEdit;
	private EditText pswEdit;
	private EditText checkPswEdit;
	private Spinner mSpinner;
	private int USER_PRIVAGE = 3;
	private String[] PRAVALG1 = { Common.getString(R.string.shop_manager), Common.getString(R.string.shop_assistant) };
	private String[] PRAVALG2 = { Common.getString(R.string.shop_assistant) };
	private String[] spinArray;
	private Handler handler;
	private Button saveButton;
	private AccountManager accountManager;
	private String phone;

	public AddEmployeeFragment(Handler handler) {
		this.handler = handler;
		accountManager = AccountManager.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.add_user, null);

		phone = getArguments().getString("phone");

		add_phone = (TextView) v.findViewById(R.id.add_phone);
		add_phone.setText(phone);
		firstNameEdit = (EditText) v.findViewById(R.id.add_first_name);
		firstNameEdit.setOnEditorActionListener(this);
		lastNameEdit = (EditText) v.findViewById(R.id.add_last_name);
		lastNameEdit.setOnEditorActionListener(this);
		pswEdit = (EditText) v.findViewById(R.id.add_password);
		pswEdit.setOnEditorActionListener(this);
		checkPswEdit = (EditText) v.findViewById(R.id.add_password_check);
		checkPswEdit.setOnEditorActionListener(this);

		mSpinner = (Spinner) v.findViewById(R.id.add_privilege);
		USER_PRIVAGE = accountManager.getCurrentAccount().getAccountPrivilege();
		if (USER_PRIVAGE == 1) {
			spinArray = PRAVALG1;
		} else if (USER_PRIVAGE == 2) {
			spinArray = PRAVALG2;
		} else {
			spinArray = PRAVALG2;
		}
		ArrayAdapter<String> spCertTypeAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, spinArray);
		spCertTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(spCertTypeAdapter);
		mSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());

		saveButton = (Button) v.findViewById(R.id.addUserButton);
		saveButton.setOnClickListener(this);

		return v;
	}

	@Override
	public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event1) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event1.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (arg0.getId()) {
			case R.id.add_first_name:
				lastNameEdit.requestFocus();
				break;
			case R.id.add_last_name:
				pswEdit.requestFocus();
				break;
			case R.id.add_password:
				checkPswEdit.requestFocus();
				break;
			case R.id.add_password_check:
				Common.HideKeyboardIfExist(AddEmployeeFragment.this);
				break;
			}
		}
		return false;
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private void saveAccount() {
		if (firstNameEdit.getText() == null || firstNameEdit.getText().toString().trim().isEmpty()) {
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_adduser_namenotnull, Toast.LENGTH_LONG).show();
			return;
		} else if (lastNameEdit.getText() == null || lastNameEdit.getText().toString().trim().isEmpty()) {
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_adduser_namenotnull, Toast.LENGTH_LONG).show();
			return;
		} else if (pswEdit.getText() == null || pswEdit.getText().toString().isEmpty()
				|| checkPswEdit.getText() == null || checkPswEdit.getText().toString().trim().isEmpty()) {
			Toast.makeText(CloudPosApp.getInstance(), R.string.password_hint, Toast.LENGTH_LONG).show();
			return;
		} else if (!pswEdit.getText().toString().trim().equals(checkPswEdit.getText().toString().trim())) {
			Toast.makeText(CloudPosApp.getInstance(), R.string.psw_different, Toast.LENGTH_LONG).show();
			return;
		} else if (pswEdit.getText().toString().length() < 6 || pswEdit.getText().toString().length() > 16) {
			Toast.makeText(CloudPosApp.getInstance(), R.string.psw_lenth_hint, Toast.LENGTH_LONG).show();
			return;
		}

		mCurrentEditAccount.setFirstName(firstNameEdit.getText().toString().trim());
		mCurrentEditAccount.setLastName(lastNameEdit.getText().toString().trim());
		mCurrentEditAccount.setAccount(phone);
		mCurrentEditAccount.setPass(pswEdit.getText().toString().trim());
		if (USER_PRIVAGE < mCurrentEditAccount.getAccountPrivilege()) {
			if (mSpinner.getSelectedItemPosition() == 0 && USER_PRIVAGE == 1)
				mCurrentEditAccount.setAccountPrivilege(2);
			else
				mCurrentEditAccount.setAccountPrivilege(3);
		} else
			mCurrentEditAccount.setAccountPrivilege(USER_PRIVAGE);

		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("sid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getStoreID());
		try {
			params.put("psw", Common.SHA1.toSHA1(pswEdit.getText().toString().trim()).toUpperCase(Locale.getDefault()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.put("first_name", firstNameEdit.getText().toString().trim());
		params.put("last_name", lastNameEdit.getText().toString().trim());
		params.put("privilege", mCurrentEditAccount.getAccountPrivilege());
		MyLog.e("params:" + params);

		// CynovoHttpClient.post("api/account/add_with_date.php", params, new
		// JsonHttpResponseHandler() {
		CynovoHttpClient.post("api/account/add.php", params, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				pDialog.show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				MyLog.e("response:" + response);
				try {
					int ret = response.getInt("ret");
					if (ret == 0) {
						handler.sendEmptyMessage(UserManagerFragment.REFRESH_USER_LIST);
						Message msg = new Message();
						msg.what = UserManagerFragment.HIDE_RIGHT_LAYOUT;
						msg.obj = AddEmployeeFragment.this;
						handler.sendMessage(msg);

						if (!checkPrivilage(mCurrentEditAccount.getAccountPrivilege()))
							return;
						mCurrentEditAccount.setPhoneID(ret);
						int ret1 = accountManager.saveAccount(mCurrentEditAccount);
						if (ret1 == -1) {
							Toast.makeText(CloudPosApp.getInstance(), R.string.alert_adduser_fail, Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(CloudPosApp.getInstance(), R.string.alert_adduser_success,
									Toast.LENGTH_SHORT).show();
							Common.HideKeyboardIfExist(this);
						}
					} else {
						String msg = response.getString("msg");
						new AlertDialog.Builder(getActivity()).setTitle(R.string.hint).setMessage(msg)
								.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								}).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
				}
				super.onSuccess(statusCode, response);

				pDialog.dismiss();
			}

			@Override
			public void onFailure(Throwable e, String content) {
				super.onFailure(e, content);
				Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
				pDialog.dismiss();
				e.printStackTrace();
			}
		});
	}

	private boolean checkPrivilage(int mPrivilege) {
		return accountManager.getCurrentAccount().getAccountPrivilege() < mPrivilege;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addUserButton:
			saveAccount();
			break;
		}
	}
}
