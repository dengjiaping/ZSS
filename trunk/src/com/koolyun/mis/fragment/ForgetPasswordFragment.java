package com.koolyun.mis.fragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.CynovoHttpClient;
import com.koolyun.mis.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ValidFragment")
public class ForgetPasswordFragment extends AbstractLoadingFragment implements OnClickListener, OnEditorActionListener {
	private String comingPhone;
	private EditText userPhoneEditText11;
	private EditText activeNumberEditText11;
	private TextView regainActiveNumberText11;
	private int sec;
	private Button getActiveNumberButton11;
	private Timer verificationTimer;
	private int curretStep;
	private String phone;
	private Handler handler2;

	public ForgetPasswordFragment() {
	}

	public ForgetPasswordFragment(Handler handler2) {
		this.handler2 = handler2;
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message arg0) {
			sec--;
			if (sec == 0) {
				verificationTimer.cancel();
				regainActiveNumberText11.setVisibility(View.GONE);
				getActiveNumberButton11.setVisibility(View.VISIBLE);
			} else {
				regainActiveNumberText11.setText(getString(R.string.regain_active_number_hint, sec));
			}
			return true;
		}
	});

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		try {
			if (bundle != null) {
				comingPhone = bundle.getString("phone");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		curretStep = 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.input_phone_and_active_number_2, null);

		if (handler2 != null) {
			Message msg = new Message();
			msg.obj = getString(R.string.forget_password);
			msg.what = LoginPageFragment.CHANGE_HINT_TEXT;
			handler2.sendMessage(msg);
		}

		Button sureButton1 = (Button) v.findViewById(R.id.sureButton1);
		sureButton1.setOnClickListener(this);

		userPhoneEditText11 = (EditText) v.findViewById(R.id.userPhoneEditText11);
		if (comingPhone != null) {
			userPhoneEditText11.setText(comingPhone);
		}
		activeNumberEditText11 = (EditText) v.findViewById(R.id.activeNumberEditText11);
		userPhoneEditText11.setOnEditorActionListener(this);
		activeNumberEditText11.setOnEditorActionListener(this);

		regainActiveNumberText11 = (TextView) v.findViewById(R.id.regainActiveNumberText11);
		getActiveNumberButton11 = (Button) v.findViewById(R.id.getActiveNumberButton11);
		getActiveNumberButton11.setOnClickListener(this);

		Bundle bundle = getArguments();
		if (bundle != null) {
			phone = bundle.getString("phone");
			userPhoneEditText11.setText(phone);
			userPhoneEditText11.setFocusable(false);
		}

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sureButton1:
			submitActiveNumber();
			break;
		case R.id.getActiveNumberButton11:
			getActiveNumber();
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.userPhoneEditText11:
				getActiveNumber();
				break;
			case R.id.activeNumberEditText11:
				submitActiveNumber();
				break;
			}
		}
		return false;
	}

	private void getActiveNumber() {
		if (userPhoneEditText11.getText() != null && userPhoneEditText11.getText().toString().trim().length() > 0) {
			if (!Common.isMobilePhoneNO(userPhoneEditText11.getText().toString().trim())) {
				Toast.makeText(getActivity(), R.string.error_phone_type, Toast.LENGTH_LONG).show();
				return;
			}
			AccountManager accountManager = AccountManager.getInstance();
			boolean isStoreAccount = false;
			List<Account> list = accountManager.getAccountListByPrivilege(0);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getAccount().equals(userPhoneEditText11.getText().toString().trim())) {
					isStoreAccount = true;
					break;
				}
			}
			if (!isStoreAccount) {
				Toast.makeText(getActivity(), R.string.not_store_account_phone, Toast.LENGTH_LONG).show();
				return;
			}

			RequestParams params = new RequestParams();
			params.put("phone", userPhoneEditText11.getText().toString().trim());

			CynovoHttpClient.post("api/active_device/password/active_number.php", params,
					new JsonHttpResponseHandler() {
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
								if (ret == 0) { // success!
									// TODO
									resetTimer();
									curretStep = 1;
									activeNumberEditText11.requestFocus();
									phone = userPhoneEditText11.getText().toString().trim();
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
		} else {
			Toast.makeText(getActivity(), R.string.error_phone_type, Toast.LENGTH_LONG).show();
		}
	}

	private void resetTimer() {
		regainActiveNumberText11.setVisibility(View.VISIBLE);
		getActiveNumberButton11.setVisibility(View.GONE);
		verificationTimer = new Timer();
		sec = 60;
		regainActiveNumberText11.setText(getString(R.string.regain_active_number_hint, sec));
		verificationTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}

	private void submitActiveNumber() {
		if (curretStep == 0) {
			Toast.makeText(getActivity(), R.string.get_active_number_first, Toast.LENGTH_LONG).show();
			return;
		}

		if (activeNumberEditText11.getText() == null
				|| activeNumberEditText11.getText().toString().trim().length() != 6) {
			// 输入框为空
			Toast.makeText(getActivity(), R.string.active_number_null, Toast.LENGTH_LONG).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("activeNumber", activeNumberEditText11.getText().toString().trim());
		params.put("phone", phone);

		CynovoHttpClient.post("api/active_device/password/verify_active_number.php", params,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						pDialog.show();
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						MyLog.e("response:" + response);
						pDialog.dismiss();
						try {
							int ret = response.getInt("ret");
							if (ret == 0) {
								Common.HideKeyboardIfExist(ForgetPasswordFragment.this);

								FragmentManager fragmentManager = getFragmentManager();
								FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
								PasswordFragment userNameFragment = new PasswordFragment(handler2);
								Bundle bundle = new Bundle();
								bundle.putInt(PasswordFragment.FROM_TYPE, PasswordFragment.IS_FROM_FORGET);
								bundle.putString("phone", phone);
								userNameFragment.setArguments(bundle);
								fragmentTransaction.replace(R.id.fragmentLayout, userNameFragment);
								fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
								fragmentTransaction.addToBackStack(LoginPageFragment.FORGET_PSW);
								fragmentTransaction.commitAllowingStateLoss();

								handler2.sendEmptyMessage(LoginPageFragment.SHOW_BACK_BUTTON);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (verificationTimer != null) {
			verificationTimer.cancel();
		}
	}
}
