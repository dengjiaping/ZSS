package com.koolyun.mis.fragment;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.koolyun.mis.R;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.CynovoHttpClient;
import com.koolyun.mis.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint("ValidFragment")
public class NewPhoneFragment extends AbstractLoadingFragment implements OnEditorActionListener, OnClickListener {
	private String phone;
	private String oldPhone;
	private Button newReturnButton;
	private int sec;
	private Timer verificationTimer;
	private int curretStep;
	private TextView regainActiveNumberText;
	private EditText newPhoneEditext;
	private Button submitActiveNumberButton;
	private EditText newActiveNumberEdittext;
	private Button getActiveNumberButton;
	private Handler handler2;
	private int id;

	public NewPhoneFragment(Handler handler2) {
		this.handler2 = handler2;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.new_phone, null);
		curretStep = 0;

		newReturnButton = (Button) v.findViewById(R.id.newReturnButton);
		newReturnButton.setOnClickListener(this);
		regainActiveNumberText = (TextView) v.findViewById(R.id.regainActiveNumberText);
		regainActiveNumberText.setVisibility(View.GONE);
		newPhoneEditext = (EditText) v.findViewById(R.id.newPhoneEditext);
		newPhoneEditext.setOnEditorActionListener(this);
		submitActiveNumberButton = (Button) v.findViewById(R.id.submitActiveNumberButton);
		submitActiveNumberButton.setOnClickListener(this);
		newActiveNumberEdittext = (EditText) v.findViewById(R.id.newActiveNumberEdittext);
		newActiveNumberEdittext.setOnEditorActionListener(this);
		getActiveNumberButton = (Button) v.findViewById(R.id.getActiveNumberButton);
		getActiveNumberButton.setOnClickListener(this);

		Bundle bundle = getArguments();
		oldPhone = bundle.getString("phone");
		id = bundle.getInt("id");

		return v;
	}

	private void getActiveNumber(final String phone) {
		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("sid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getStoreID());
		// CynovoHttpClient.post("api/active_device/password/active_number.php",
		// params,
		CynovoHttpClient.post("api/active_device/password/verify_new_phone.php", params, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				pDialog.show();
				super.onStart();
			}

			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				MyLog.e("response:" + response);
				try {
					int ret = response.getInt("ret");
					if (ret == 0) {
						resetTimer();
						curretStep = 1;
						NewPhoneFragment.this.phone = phone;
						newActiveNumberEdittext.requestFocus();
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
				pDialog.dismiss();
			}

			@Override
			public void onFailure(Throwable e, String content) {
				super.onFailure(e, content);
				e.printStackTrace();
				Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
				pDialog.dismiss();
			}
		});
	}

	private void submitActiveNumber() {
		if (curretStep == 0) {
			Toast.makeText(getActivity(), R.string.get_active_number_first, Toast.LENGTH_LONG).show();
			return;
		}
		if (newActiveNumberEdittext.getText() == null
				|| newActiveNumberEdittext.getText().toString().trim().length() != 6) {
			// 输入框为空
			Toast.makeText(getActivity(), R.string.active_number_null, Toast.LENGTH_LONG).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("activeNumber", newActiveNumberEdittext.getText().toString().trim());
		params.put("phone", phone);
		params.put("oldPhone", oldPhone);
		MyLog.e("params:" + params.toString());

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
						try {
							int ret = response.getInt("ret");
							if (ret == 0) { // success!
								Common.HideKeyboardIfExist(NewPhoneFragment.this);

								String msg = response.getString("msg");
								Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

								if (verificationTimer != null) {
									verificationTimer.cancel();
								}
								AccountManager accountManager = AccountManager.getInstance();
								Account acc = accountManager.getAccountById(id);
								acc.setAccount(phone);
								accountManager.saveAccount(acc);
								Message msg1 = new Message();
								msg1.what = UserManagerFragment.HIDE_RIGHT_LAYOUT;
								msg1.obj = NewPhoneFragment.this;
								handler2.sendMessage(msg1);
								handler2.sendEmptyMessage(UserManagerFragment.REFRESH_USER_LIST);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (verificationTimer != null) {
			verificationTimer.cancel();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getActiveNumberButton:
			if (newPhoneEditext.getText() != null
					&& Common.isMobilePhoneNO(newPhoneEditext.getText().toString().trim())) {
				getActiveNumber(newPhoneEditext.getText().toString().trim());
			} else {
				Toast.makeText(getActivity(), R.string.error_phone_type, Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.submitActiveNumberButton:
			submitActiveNumber();
			break;
		case R.id.newReturnButton:
			if (verificationTimer != null) {
				verificationTimer.cancel();
			}
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			fm.popBackStack();
			ft.commit();
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event1) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event1.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.newPhoneEditext:
				if (newPhoneEditext.getText() != null
						&& Common.isMobilePhoneNO(newPhoneEditext.getText().toString().trim())) {
					getActiveNumber(newPhoneEditext.getText().toString().trim());
				} else {
					Toast.makeText(getActivity(), R.string.error_phone_type, Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.newActiveNumberEdittext:
				Common.HideKeyboardIfExist(NewPhoneFragment.this);
				submitActiveNumber();
				break;
			}
		}
		return false;
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message arg0) {
			sec--;
			if (sec == 0) {
				verificationTimer.cancel();
				regainActiveNumberText.setVisibility(View.GONE);
				getActiveNumberButton.setVisibility(View.VISIBLE);
			} else {
				regainActiveNumberText.setText(getString(R.string.regain_active_number_hint, sec));
			}
			return true;
		}
	});

	private void resetTimer() {
		regainActiveNumberText.setVisibility(View.VISIBLE);
		getActiveNumberButton.setVisibility(View.GONE);
		verificationTimer = new Timer();
		sec = 60;
		regainActiveNumberText.setText(getString(R.string.regain_active_number_hint, sec));
		verificationTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}
}
