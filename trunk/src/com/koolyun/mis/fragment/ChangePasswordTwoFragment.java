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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.CynovoHttpClient;
import com.koolyun.mis.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ValidFragment")
public class ChangePasswordTwoFragment extends AbstractLoadingFragment implements OnClickListener,
		OnEditorActionListener {
	private EditText passwordEditText;
	private EditText ensurePasswordEditText;
	private Handler handler;
	private String phone;

	public ChangePasswordTwoFragment() {
	}

	public ChangePasswordTwoFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MySharedPreferencesEdit.getInstancePublic(getActivity());
		phone = getArguments().getString("phone");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = null;

		v = inflater.inflate(R.layout.change_psw_two, null);

		Button submitPasswordButton = (Button) v.findViewById(R.id.submitPasswordButton);
		submitPasswordButton.setOnClickListener(this);
		submitPasswordButton.setText(R.string.sure);

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
			Toast.makeText(getActivity(), R.string.passworderror_prompt, Toast.LENGTH_LONG).show();
			return;
		}
		if (!passwordEditText.getText().toString().trim().equals(ensurePasswordEditText.getText().toString().trim())) {
			// 两次输入的密码不同
			Toast.makeText(getActivity(), R.string.psw_different, Toast.LENGTH_LONG).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("sid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getStoreID());
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
						Common.HideKeyboardIfExist(ChangePasswordTwoFragment.this);
						Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

						// FragmentManager fragmentManager =
						// getFragmentManager();
						// FragmentTransaction fragmentTransaction =
						// fragmentManager.beginTransaction();
						// fragmentTransaction.setCustomAnimations(R.animator.obj_push_up_in,
						// R.animator.obj_push_up_out,
						// R.animator.obj_push_down_in,
						// R.animator.obj_push_down_out);
						// fragmentManager.popBackStack(LoginPageFragment.USER_SELECT,
						// 0);
						// fragmentTransaction.commit();
						Message msg1 = new Message();
						msg1.what = UserManagerFragment.HIDE_RIGHT_LAYOUT;
						msg1.obj = ChangePasswordTwoFragment.this;
						handler.sendMessage(msg1);
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
