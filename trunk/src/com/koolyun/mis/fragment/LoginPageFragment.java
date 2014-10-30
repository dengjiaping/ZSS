package com.koolyun.mis.fragment;

import java.io.File;

import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.SaleActivity;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.store.StoreManager;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.sqlite.LocalDatabase;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.LoginHelpBar;

public class LoginPageFragment extends AbstractSupportV4Fragment implements TextView.OnEditorActionListener,
		OnClickListener, AdapterView.OnItemClickListener {
	private LoginHelpBar bar;
	private Button backbtn;
	private EditText mUserName = null;
	private EditText mUserPass = null;
	private Button loginButton = null;
	private static final int LOGIN_INPUT = 1;
	int[] anmiArray = { R.anim.push_up_in, R.anim.push_up_out, R.anim.push_down_in, R.anim.push_down_out, };
	// 以下是找回密码
	private ProgressDialog pDialog;
	private UserSelectFragment userNameFragment;

	private Handler handler;
	public static final int SHOW_BACK_BUTTON = 0;
	public static final int HIDE_BACK_BUTTON = 1;
	public static final int CHANGE_HINT_TEXT = 2;
	public static final String USER_SELECT = "USER_SELECT";
	public static final String USER_LOGIN = "USER_LOGIN";
	public static final String FORGET_PSW = "FORGET_PSW";
	public static final String NEW_PSW = "NEW_PSW";
	private TextView have_not_active_device;
	private static boolean disableLogin = false;

	private void showChildIndex(int index) {
		// // Common.FlipperShowIndex(getActivity(), userViewFlipper, index,
		// anmiArray);
		// userViewFlipper.setInAnimation(getActivity(), anmiArray[2*index]);
		// userViewFlipper.setOutAnimation(getActivity(), anmiArray[2*index+1]);
		// userViewFlipper.setDisplayedChild(index);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new InitTask().execute();

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case SHOW_BACK_BUTTON:
					// 显示返回按钮
					backbtn.setVisibility(View.VISIBLE);
					break;
				case HIDE_BACK_BUTTON:
					// 隐藏返回按钮
					backbtn.setVisibility(View.INVISIBLE);
					break;
				case CHANGE_HINT_TEXT:
					// 改变右上角提示文字
					bar.setRightText(msg.obj.toString());
					break;
				}
				return false;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		bar.setButtonVisibility(View.VISIBLE);
		showChildIndex(LOGIN_INPUT);
		bar.setRightText(R.string.user_login_in);
		if (arg3 == 0) {
			Account mAccount = (Account) arg0.getItemAtPosition(arg2);
			if (mAccount != null) {
				mUserName.setText(mAccount.getAccount());
				mUserPass.setText("");

			}
		} else {
			mUserName.setText("");
			mUserPass.setText("");
		}
	}

	public void refresh(Context context) {
		bar.setVisibility(View.VISIBLE);
		try {
			have_not_active_device.setVisibility(View.GONE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			FragmentManager fragmentManager = getActivity().getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			userNameFragment = new UserSelectFragment(handler);
			fragmentTransaction.replace(R.id.fragmentLayout, userNameFragment, USER_SELECT);
			fragmentTransaction.addToBackStack(USER_SELECT);
			fragmentTransaction.commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
			CloudPosApp.getInstance().restartApp(getActivity());
			return;
		}

		try {
			if (StoreManager.getStore().getNickName() != null && StoreManager.getStore().getNickName().length() > 0
					&& !StoreManager.getStore().getNickName().equals("null")) {
				bar.setLeftText(StoreManager.getStore().getNickName());
			} else if (StoreManager.getStore().getCompanyName() != null
					&& StoreManager.getStore().getCompanyName().length() > 0
					&& !StoreManager.getStore().getCompanyName().equals("null")) {
				bar.setLeftText(StoreManager.getStore().getCompanyName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.login_page, container, false);

		bar = (LoginHelpBar) v.findViewById(R.id.help_bar);
		if (StoreManager.getStore().getNickName() != null && StoreManager.getStore().getNickName().length() > 0
				&& !StoreManager.getStore().getNickName().equals("null")) {
			bar.setLeftText(StoreManager.getStore().getNickName());
		} else if (StoreManager.getStore().getCompanyName() != null
				&& StoreManager.getStore().getCompanyName().length() > 0
				&& !StoreManager.getStore().getCompanyName().equals("null")) {
			bar.setLeftText(StoreManager.getStore().getCompanyName());
		}
		bar.setRightText(R.string.select_user);

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage(getActivity().getResources().getString(R.string.is_loading));
		//
		backbtn = (Button) v.findViewById(R.id.login_bak);
		backbtn.setOnClickListener(this);

		MySharedPreferencesEdit mySharedPreferencesEdit = MySharedPreferencesEdit.getInstancePublic(getActivity());
		if (!mySharedPreferencesEdit.getIsDownloadSecretKey()) {
			have_not_active_device = (TextView) v.findViewById(R.id.have_not_active_device);
			have_not_active_device.setVisibility(View.VISIBLE);
			bar.setVisibility(View.GONE);
			return v;
		}

		FragmentManager fragmentManager = getActivity().getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		userNameFragment = new UserSelectFragment(handler);
		fragmentTransaction.add(R.id.fragmentLayout, userNameFragment, USER_SELECT);
		fragmentTransaction.addToBackStack(USER_SELECT);
		fragmentTransaction.commitAllowingStateLoss();
		return v;
	}

	private class InitTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			LocalDatabase.getInstance();
			ProductManager.InitPinyinTable();
			File signaturePath = new File(Common.SIGNATURE_PATH);
			if (!signaturePath.exists()) {
				signaturePath.mkdirs();
			}

			return null;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.user_name_input:
				if (!mUserName.getText().toString().isEmpty())
					mUserPass.requestFocus();
				break;
			case R.id.user_pass_input:
				loginIdFun();
				break;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			loginIdFun();
			break;
		case R.id.login_bak:
			// bar.setRightText(getText(R.string.select_user).toString());
			// bar.setButtonVisibility(View.INVISIBLE);
			// showChildIndex(LOGIN_SELECT);
			FragmentManager fragmentManager = getActivity().getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentManager.popBackStack();
			fragmentTransaction.commit();
			// fragmentManager.executePendingTransactions();
			break;
		case R.id.forgetPasswordButton:
			// // Intent intent = new Intent(getActivity(),
			// ForgetPasswordActivity.class);
			// // if(mUserName.getText() != null &&
			// mUserName.getText().toString().trim().length() == 11) {
			// // intent.putExtra("phone",
			// mUserName.getText().toString().trim());
			// // }
			// // getActivity().startActivity(intent);
			// userViewFlipper.setInAnimation(getActivity(),anmiArray[0]);
			// userViewFlipper.setOutAnimation(getActivity(),anmiArray[1]);
			// userViewFlipper.setDisplayedChild(2);
			break;
		}
	}

	private void loginIdFun() {
		if (disableLogin)
			return;
		disableLogin = true;
		loginButton.setClickable(false);
		int result = (Integer) AccountManager.getInstance().checkPassword(
				LoginPageFragment.this.mUserName.getText().toString(),
				LoginPageFragment.this.mUserPass.getText().toString());

		if (2 == result) {
			Intent saleintent = new Intent(LoginPageFragment.this.getActivity(), SaleActivity.class);
			Bundle translateBundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in,
					R.anim.push_left_out).toBundle();
			getActivity().startActivity(saleintent, translateBundle);
			DealModel.getInstance().setOrderProcessStatus(DealModel.ORDER_PROCESS_SELECE);
			getActivity().finish();
		} else {
			loginButton.setClickable(true);
			disableLogin = false;
			Toast.makeText(getActivity(), R.string.login_in_error, Toast.LENGTH_SHORT).show();
		}
	}
}
