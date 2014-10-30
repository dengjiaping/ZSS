package com.koolyun.mis.fragment;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.koolyun.mis.widget.AnyWhereDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ValidFragment")
public class EditEmployeeFragment extends AbstractLoadingFragment implements OnEditorActionListener, OnClickListener,
		OnTouchListener {
	private Account editAccount;
	private TextView phoneText;
	private EditText firstNameEdit;
	private EditText lastNameEdit;
	private int id;
	private Spinner mSpinner;
	private int USER_PRIVAGE = 0;
	private String[] PRAVALG1 = { Common.getString(R.string.shop_assistant), Common.getString(R.string.shop_manager) };
	private String[] PRAVALG2 = { Common.getString(R.string.shop_assistant) };
	private String[] spinArray;
	private Handler handler;
	private AnyWhereDialog mDialog;
	private Button saveButton;
	private Button deleteButton;
	private RelativeLayout changePhoneLayout;
	private RelativeLayout changePswLayout;
	private ScrollView mUsrmg_Scrollview;
	private int privilege;
	private AccountManager accountManager;
	private Account currentAccount;

	public EditEmployeeFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountManager = AccountManager.getInstance();
		editAccount = new Account();
		currentAccount = accountManager.getCurrentAccount();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.edit_user, null);

		Bundle b = getArguments();
		if (b != null) {
			id = b.getInt("id");
		} else {
			id = 0;
		}

		editAccount = accountManager.getAccountById(id);
		privilege = editAccount.getAccountPrivilege();

		phoneText = (TextView) v.findViewById(R.id.edit_phone);
		firstNameEdit = (EditText) v.findViewById(R.id.edit_first_name1);
		firstNameEdit.setOnEditorActionListener(this);
		lastNameEdit = (EditText) v.findViewById(R.id.edit_last_name1);
		lastNameEdit.setOnEditorActionListener(this);

		phoneText.setText(editAccount.getAccount());
		firstNameEdit.setText(editAccount.getFirstName() == null ? "" : editAccount.getFirstName());
		lastNameEdit.setText(editAccount.getLastName() == null ? "" : editAccount.getLastName());

		mUsrmg_Scrollview = (ScrollView) v.findViewById(R.id.usrmg_scrollview);
		mUsrmg_Scrollview.setOnTouchListener(this);

		mSpinner = (Spinner) v.findViewById(R.id.usermg_spin);
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
		if (editAccount.getAccountPrivilege() == 2 && currentAccount.getAccountPrivilege() != 2)
			mSpinner.setSelection(1, true);

		saveButton = (Button) v.findViewById(R.id.usermsg_save_btn);
		deleteButton = (Button) v.findViewById(R.id.usrmg_delete_btn);

		saveButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);

		changePhoneLayout = (RelativeLayout) v.findViewById(R.id.changePhoneLayout);
		changePhoneLayout.setOnClickListener(this);
		changePswLayout = (RelativeLayout) v.findViewById(R.id.changePswLayout);
		changePswLayout.setOnClickListener(this);

		TextView usrmg_spinner_textview = (TextView) v.findViewById(R.id.usrmg_spinner_textview);
		Button editFirstNameButton = (Button) v.findViewById(R.id.edit_first_name_button);
		Button editLastNameButton = (Button) v.findViewById(R.id.edit_last_name_button);
		if (currentAccount.getAccountPrivilege() >= privilege) {
			deleteButton.setVisibility(View.GONE);
			mSpinner.setVisibility(View.GONE);
			usrmg_spinner_textview.setVisibility(View.GONE);
		} else {
			deleteButton.setVisibility(View.VISIBLE);
			mSpinner.setVisibility(View.VISIBLE);
			usrmg_spinner_textview.setVisibility(View.VISIBLE);
		}
		if (privilege == 1) {
			editFirstNameButton.setVisibility(View.VISIBLE);
			editFirstNameButton.setText(editAccount.getFirstName());
			editLastNameButton.setVisibility(View.VISIBLE);
			editLastNameButton.setText(editAccount.getLastName());
		} else {
			editFirstNameButton.setVisibility(View.GONE);
			editLastNameButton.setVisibility(View.GONE);
		}

		return v;
	}

	@Override
	public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event1) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event1.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (arg0.getId()) {
			case R.id.edit_first_name1:
				lastNameEdit.requestFocus();
				break;
			case R.id.edit_last_name1:
				Common.HideKeyboardIfExist(EditEmployeeFragment.this);
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
			Toast.makeText(CloudPosApp.getInstance(), R.string.first_name_hint, Toast.LENGTH_SHORT).show();
			return;
		}
		if (lastNameEdit.getText() == null || lastNameEdit.getText().toString().trim().isEmpty()) {
			Toast.makeText(CloudPosApp.getInstance(), R.string.last_name_hint, Toast.LENGTH_SHORT).show();
			return;
		}

		editAccount.setFirstName(firstNameEdit.getText().toString().trim());
		editAccount.setLastName(lastNameEdit.getText().toString().trim());
		editAccount.setAccount(editAccount.getAccount());

		if (USER_PRIVAGE < editAccount.getAccountPrivilege()) {
			if (mSpinner.getSelectedItemPosition() == 1 && USER_PRIVAGE == 1)
				editAccount.setAccountPrivilege(2);
			else
				editAccount.setAccountPrivilege(3);
		} else
			editAccount.setAccountPrivilege(USER_PRIVAGE);

		if (!checkPrivilage(editAccount))
			return;

		RequestParams params = new RequestParams();
		params.put("phone", editAccount.getAccount());
		params.put("sid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getStoreID());
		params.put("first_name", editAccount.getFirstName());
		params.put("last_name", editAccount.getLastName());
		params.put("privilege", editAccount.getAccountPrivilege());

		CynovoHttpClient.post("api/account/change_name_privilege.php", params, new JsonHttpResponseHandler() {
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
						msg.obj = EditEmployeeFragment.this;
						handler.sendMessage(msg);

						int ret1 = accountManager.saveAccount(editAccount);
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

	private void deleteAccount() {
		if (editAccount == null
				|| !(accountManager.getCurrentAccount().getAccountPrivilege() < editAccount.getAccountPrivilege())) {
			return;
		}

		RequestParams params = new RequestParams();
		params.put("phone", editAccount.getAccount());
		params.put("sid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getStoreID());

		CynovoHttpClient.post("api/account/delete.php", params, new JsonHttpResponseHandler() {
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
						Message msg = new Message();
						msg.what = UserManagerFragment.HIDE_RIGHT_LAYOUT;
						msg.obj = EditEmployeeFragment.this;
						handler.sendMessage(msg);
						handler.sendEmptyMessage(UserManagerFragment.REFRESH_USER_LIST);

						Toast.makeText(CloudPosApp.getInstance(), R.string.delete_employee_success, Toast.LENGTH_SHORT)
								.show();
						if (editAccount.getAccountPrivilege() > 1)
							accountManager.deleteAccount(editAccount.getAccountID());
						Common.HideKeyboardIfExist(this);
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

	private boolean checkPrivilage(Account editAccount) {
		return accountManager.getCurrentAccount().getAccountPrivilege() < editAccount.getAccountPrivilege()
				|| editAccount.getAccountID() == accountManager.getCurrentAccount().getAccountID();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.usermsg_save_btn:
			saveAccount();
			break;
		case R.id.usrmg_delete_btn:
			mDialog = new AnyWhereDialog(getActivity(), 540, 280, 0, 0, R.layout.confirm_delete, R.style.Theme_dialog1,
					Gravity.LEFT | Gravity.TOP, true);
			Button confirmBtn = (Button) mDialog.findViewById(R.id.confirm_delete);
			Button cancelBtn = (Button) mDialog.findViewById(R.id.cancel_delete);
			confirmBtn.setOnClickListener(this);
			cancelBtn.setOnClickListener(this);
			mDialog.show();
			break;
		case R.id.confirm_delete:
			deleteAccount();
			mDialog.dismiss();
			break;
		case R.id.cancel_delete:
			mDialog.dismiss();
			break;
		case R.id.changePhoneLayout: {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			OriginalPhoneFragment emf = new OriginalPhoneFragment(handler);
			Bundle bundle = new Bundle();
			bundle.putInt("id", id);
			bundle.putString("phone", editAccount.getAccount());
			emf.setArguments(bundle);
			ft.replace(R.id.userManagerFragmentLayout, emf);
			ft.commit();
			break;
		}
		case R.id.changePswLayout: {
			FragmentManager fm1 = getFragmentManager();
			FragmentTransaction ft1 = fm1.beginTransaction();
			ChangePasswordOneFragment emf1 = new ChangePasswordOneFragment(handler);
			Bundle bundle1 = new Bundle();
			bundle1.putInt("id", id);
			bundle1.putString("phone", editAccount.getAccount());
			emf1.setArguments(bundle1);
			ft1.replace(R.id.userManagerFragmentLayout, emf1);
			ft1.commit();
			break;
		}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.usrmg_scrollview:
			int y = (int) v.getY();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				InputMethodManager inputManager = (InputMethodManager) mUsrmg_Scrollview.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(mUsrmg_Scrollview.getWindowToken(), 0);
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (v.getY() - y > 0) {
					InputMethodManager inputManager = (InputMethodManager) mUsrmg_Scrollview.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(mUsrmg_Scrollview.getWindowToken(), 0);
				}
			}
			break;
		}
		return false;
	}
}
