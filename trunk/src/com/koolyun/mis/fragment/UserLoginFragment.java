package com.koolyun.mis.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import com.koolyun.mis.SaleActivity;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.user.AccountManager;

@SuppressLint("ValidFragment")
public class UserLoginFragment extends Fragment implements OnClickListener, OnEditorActionListener {
	private EditText mUserName = null;
	private EditText mUserPass = null;
	private Button loginButton = null;
	private Handler handler;
	private volatile boolean disableLogin = false;
	private String phone;

	public UserLoginFragment() {
	}

	public UserLoginFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.user_input, container, false);

		Message msg = new Message();
		msg.obj = getString(R.string.user_login_in);
		msg.what = LoginPageFragment.CHANGE_HINT_TEXT;
		handler.sendMessage(msg);

		loginButton = (Button) v.findViewById(R.id.login_btn);
		loginButton.setOnClickListener(this);
		mUserName = (EditText) v.findViewById(R.id.user_name_input);
		mUserPass = (EditText) v.findViewById(R.id.user_pass_input);

		mUserName.setOnEditorActionListener(this);
		mUserPass.setOnEditorActionListener(this);

		Bundle bundle = getArguments();
		if (bundle != null) {
			phone = (bundle.getString("phone") == null || bundle.getString("phone").trim().length() == 0) ? "" : bundle
					.getString("phone").trim();
			mUserName.setText(phone);
			mUserName.setFocusable(false);
			mUserPass.requestFocus();
		}

		Button forgetPasswordButton = (Button) v.findViewById(R.id.forgetPasswordButton);
		forgetPasswordButton.setOnClickListener(this);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		mUserPass.setText("");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			loginIdFun();
			break;
		case R.id.forgetPasswordButton:
			FragmentManager fragmentManager = getActivity().getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.animator.obj_push_up_in, R.animator.obj_push_up_out,
					R.animator.obj_push_down_in, R.animator.obj_push_down_out);
			ForgetPasswordFragment userNameFragment = new ForgetPasswordFragment(handler);
			if (phone != null && phone.length() > 0) {
				Bundle bundle = new Bundle();
				bundle.putString("phone", phone);
				userNameFragment.setArguments(bundle);
			}
			fragmentTransaction.replace(R.id.fragmentLayout, userNameFragment);
			fragmentTransaction.addToBackStack(LoginPageFragment.USER_LOGIN);
			fragmentTransaction.commitAllowingStateLoss();

			handler.sendEmptyMessage(LoginPageFragment.SHOW_BACK_BUTTON);
			break;
		}
	}

	private void loginIdFun() {
		if (disableLogin)
			return;
		disableLogin = true;
		loginButton.setClickable(false);
		int result = (Integer) AccountManager.getInstance().checkPassword(mUserName.getText().toString(),
				mUserPass.getText().toString());
		disableLogin = true;
		if (2 == result) {
			Intent saleintent = new Intent(getActivity(), SaleActivity.class);
			Bundle translateBundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in,
					R.anim.push_left_out).toBundle();
			getActivity().startActivity(saleintent, translateBundle);
			DealModel.getInstance().setOrderProcessStatus(DealModel.ORDER_PROCESS_SELECE);
			getActivity().finish();
		} else {
			loginButton.setClickable(true);
			disableLogin = false;
			Toast.makeText(getActivity(), R.string.phone_psw_error, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.user_name_input:
				if (!mUserName.getText().toString().isEmpty()) {
					mUserPass.requestFocus();
				}
				break;
			case R.id.user_pass_input:
				if (!disableLogin)
					loginIdFun();
				break;
			}
		}
		return false;
	}
}
