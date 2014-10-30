package com.koolyun.mis;

import java.io.File;

import org.json.JSONObject;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.koolcloud.pos.service.IMerchService;
import cn.koolcloud.pos.service.ISecureService;

import com.koolyun.mis.bean.MerchantBean;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.fragment.ActiveFragment;
import com.koolyun.mis.fragment.LoginPageFragment;
import com.koolyun.mis.fragment.LoginUpdateFragment;
import com.koolyun.mis.sqlite.LocalDatabase;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class LoginInActivity extends AbstractFragmentActivity implements OnClickListener {
	private MySharedPreferencesEdit mySharedPreferencesEdit;
	private static LoginPageFragment loginPageFragment;
	private static ActiveFragment activeFragment;
	private static LoginUpdateFragment loginUpdateFragment;

	private EditText loginNameEdit;
	private EditText loginPassWordEdit;
	private Button loginSureBtn;

	private boolean isAIDLSuccess = true;
	MerchantBean merchantBean = null;
	protected IMerchService mIMerchService;
	private ServiceConnection merchConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mIMerchService = IMerchService.Stub.asInterface(service);
			Log.i("Client", "Bind mMerchService Success:" + mIMerchService.getClass().toString());
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mIMerchService = null;
		}
	};

	protected ISecureService mISecureService;
	private ServiceConnection secureConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mISecureService = ISecureService.Stub.asInterface(service);
			Log.i("Client", "Bind mSecureService Success:" + mISecureService.getClass().toString());
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mISecureService = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);

		setContentView(R.layout.login_layout);
		// 初始化数据库
		new InitTask().execute();
		mySharedPreferencesEdit = MySharedPreferencesEdit.getInstancePublic(this);
		// 绑定服务
		boundServices();
		loginNameEdit = (EditText) findViewById(R.id.loginNameEdit);
		loginPassWordEdit = (EditText) findViewById(R.id.loginPassWordEdit);
		loginPassWordEdit.setText("123456");
		loginSureBtn = (Button) findViewById(R.id.loginSureBtn);
		loginSureBtn.setOnClickListener(this);

		// 延迟500毫秒是为了给绑定service一个时间
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				merchantBean = new MerchantBean();
				if (mISecureService != null) {
					try {
						String userInfo = mISecureService.getUserInfo();
						if (userInfo != null) {
							MyLog.i("===1=" + userInfo);
							JSONObject jsObj = new JSONObject(userInfo);
							MyLog.i("====" + userInfo.toString());
							MyLog.i("====" + jsObj.toString());
							// MyLog.e("operator:" + jsObj.optString("userName")
							// + " gradeId:" + jsObj.optString("gradeId") +
							// " userStatus:" + jsObj.optString("userStatus"));
							merchantBean.setUserName(jsObj.optString("userName"));
							merchantBean.setGradeId(jsObj.optString("gradeId"));
							merchantBean.setUserStatus(jsObj.optString("userStatus"));
						}
					} catch (Exception e) {
						e.printStackTrace();
						isAIDLSuccess = false;
					}
				}

				if (mIMerchService != null) {
					try {
						String merchName = mIMerchService.getMerchInfo().getMerchName();
						String merchId = mIMerchService.getMerchInfo().getMerchId();
						String terminalId = mIMerchService.getMerchInfo().getTerminalId();

						MyLog.e("merchName:" + merchName + " merchId:" + merchId + " terminalId:" + terminalId);
						merchantBean.setMerchName(merchName);
						merchantBean.setMerchId(merchId);
						merchantBean.setTerminalId(terminalId);
						CloudPosApp.getInstance().setMerchantBean(merchantBean);

						mySharedPreferencesEdit.setActiveEquiment(true);
					} catch (Exception e) {
						e.printStackTrace();
						showLoginErrorDialog();
					}
				} else {
					showLoginErrorDialog();
				}
			}
		}, 500);
	}

	private void boundServices() {

		try {
			Intent mserchService = new Intent(IMerchService.class.getName());
			bindService(mserchService, merchConnection, BIND_AUTO_CREATE);

			Intent secureService = new Intent(ISecureService.class.getName());
			bindService(secureService, secureConnection, BIND_AUTO_CREATE);
		} catch (SecurityException e) {
			e.printStackTrace();
			// TODO just for test that 绑定失败 一种可能情况，远程service
			// apk升级，本程序继续绑定久的service
			secureConnection = null;
			ToastUtil.showToast(LoginInActivity.this, R.string.bindfail, true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginSureBtn:

			if (merchantBean.getMerchId() == null || merchantBean.getMerchName() == null
					&& merchantBean.getTerminalId() == null) {
				showLoginErrorDialog();
				break;
			}

			int result = (Integer) AccountManager.getInstance().checkPassword(loginNameEdit.getText().toString(),
					loginPassWordEdit.getText().toString());
			if (2 == result) {
				Intent saleintent = new Intent(LoginInActivity.this, SaleActivity.class);
				Bundle translateBundle = ActivityOptions.makeCustomAnimation(LoginInActivity.this, R.anim.push_left_in,
						R.anim.push_left_out).toBundle();
				startActivity(saleintent, translateBundle);
				DealModel.getInstance().setOrderProcessStatus(DealModel.ORDER_PROCESS_SELECE);
				finish();
			} else {

				Toast.makeText(LoginInActivity.this, R.string.phone_psw_error, Toast.LENGTH_SHORT).show();
			}
			break;

		}
	}

	private void showLoginErrorDialog() {
		new AlertDialog.Builder(LoginInActivity.this).setTitle(R.string.hint).setMessage(R.string.login_error)
				.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						LoginInActivity.this.finish();
					}
				}).setCancelable(false).show();
	}

	@Override
	protected void onDestroy() {
		MyLog.i("--LoginActivity-onDestroy-");
		unboundServices();
		super.onDestroy();
	}

	private void unboundServices() {
		if (merchConnection != null)
			unbindService(merchConnection);
		if (secureConnection != null)
			unbindService(secureConnection);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

}