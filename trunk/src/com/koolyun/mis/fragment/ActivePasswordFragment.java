package com.koolyun.mis.fragment;

import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.CynovoHttpClient;
import com.koolyun.mis.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ValidFragment")
public class ActivePasswordFragment extends AbstractSupportV4Fragment implements OnClickListener,
		OnEditorActionListener {
	public static final String FROM_TYPE = "fromType";
	public static final int IS_FROM_ACTIVE = 0;
	public static final int IS_FROM_FORGET = 1;
	private int fromPage;
	private EditText passwordEditText;
	private EditText ensurePasswordEditText;
	private Handler handler;
	private String phone;

	public ActivePasswordFragment() {
	}

	public ActivePasswordFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Bundle bundle = getArguments();
			phone = bundle.getString("phone");
			fromPage = bundle.getInt(FROM_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MySharedPreferencesEdit.getInstancePublic(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = null;
		if (fromPage == IS_FROM_FORGET) {
			v = inflater.inflate(R.layout.active_change_password, container, false);

			Message msg = new Message();
			msg.obj = getString(R.string.input_pass);
			msg.what = LoginPageFragment.CHANGE_HINT_TEXT;
			handler.sendMessage(msg);
		} else {
			v = inflater.inflate(R.layout.password, null);
		}

		Button submitPasswordButton = (Button) v.findViewById(R.id.submitPasswordButton);
		submitPasswordButton.setOnClickListener(this);
		if (fromPage == IS_FROM_FORGET) {
			submitPasswordButton.setText(R.string.sure);
		}

		passwordEditText = (EditText) v.findViewById(R.id.passwordEditText);
		passwordEditText.requestFocus();
		ensurePasswordEditText = (EditText) v.findViewById(R.id.ensurePasswordEditText);
		passwordEditText.setOnEditorActionListener(this);
		ensurePasswordEditText.setOnEditorActionListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitPasswordButton: { // 提交
			changePassword();
			break;
		}
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.passwordEditText:
				ensurePasswordEditText.requestFocus();
				break;
			case R.id.ensurePasswordEditText:
				changePassword();
				break;
			}
		}
		return false;
	}

	private void changePassword() {
		if (passwordEditText.getText() == null || passwordEditText.getText().toString().trim().length() == 0
				|| ensurePasswordEditText.getText() == null
				|| ensurePasswordEditText.getText().toString().trim().length() == 0) {
			// 输入框为空
			Toast.makeText(getActivity(), R.string.active_number_null, Toast.LENGTH_LONG).show();
			return;
		}
		if (!passwordEditText.getText().toString().trim().equals(ensurePasswordEditText.getText().toString().trim())) {
			// 两次输入的密码不同
			Toast.makeText(getActivity(), R.string.psw_different, Toast.LENGTH_LONG).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("phone", phone);
		try {
			params.put("psw",
					Common.SHA1.toSHA1(passwordEditText.getText().toString().trim()).toUpperCase(Locale.getDefault()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		CynovoHttpClient.post("api/active_device/password/change.php", params, new JsonHttpResponseHandler() {
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
					String msg = response.getString("msg");
					if (ret == 0) {
						Common.HideKeyboardIfExist(ActivePasswordFragment.this);
						Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentManager.popBackStack("UserNameFragment", 0);
						fragmentTransaction.commitAllowingStateLoss();

						Account account = null;
						List<Account> list = AccountManager.getInstance().getAccountListByPrivilege(0);
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getAccount().equals(phone)) {
								account = list.get(i);
								break;
							}
						}
						if (account != null) {
							account.setPass(Common.SHA1.toSHA1(passwordEditText.getText().toString().trim())
									.toUpperCase(Locale.getDefault()));
							AccountManager.getInstance().saveAccount(account);
						}
					} else {
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
}
