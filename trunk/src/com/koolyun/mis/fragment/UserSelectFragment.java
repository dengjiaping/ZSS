package com.koolyun.mis.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.UserAdapter;
import com.koolyun.mis.constants.MyConstants;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.sqlite.AccountDBHelper;
import com.koolyun.mis.sqlite.ClearAllDBData;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.CynovoHttpClient;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.widget.HorizontalListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ValidFragment")
public class UserSelectFragment extends AbstractLoadingFragment implements OnItemClickListener {
	private HorizontalListView scroll;
	private Handler handler;
	private static boolean hasUpdateEmployeeList;
	private View v;
	private UserAdapter userAdapter;

	public UserSelectFragment() {
	}

	public UserSelectFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hasUpdateEmployeeList = false;
		userAdapter = new UserAdapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.user_selector, container, false);
		if (MySharedPreferencesEdit.getInstancePublic(getActivity()).getMerchantNo() == null
				|| MySharedPreferencesEdit.getInstancePublic(getActivity()).getMerchantNo().trim().length() == 0) {
			return v;
		}
		scroll = (HorizontalListView) v.findViewById(R.id.user_bar);
		scroll.setOnItemClickListener(this);
		scroll.setAdapter(userAdapter);
		userAdapter.refreshList();
		if (!hasUpdateEmployeeList) {
			if (MyConstants.SHOULD_REGISTER == 0) {
				getEmployeeListFromNet(v);
			}
		}

		try {
			handler.sendEmptyMessage(LoginPageFragment.HIDE_BACK_BUTTON);
			Message msg = new Message();
			msg.obj = getString(R.string.select_user);
			msg.what = LoginPageFragment.CHANGE_HINT_TEXT;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.animator.obj_push_up_in, R.animator.obj_push_up_out,
				R.animator.obj_push_down_in, R.animator.obj_push_down_out);
		UserLoginFragment userNameFragment = new UserLoginFragment(handler);
		if (arg3 == 0) {
			Account mAccount = (Account) arg0.getItemAtPosition(arg2);
			if (mAccount != null) {
				Bundle bundle = new Bundle();
				bundle.putString("phone", mAccount.getAccount());
				userNameFragment.setArguments(bundle);
			}
		}
		fragmentTransaction.replace(R.id.fragmentLayout, userNameFragment, LoginPageFragment.USER_LOGIN);
		fragmentTransaction.addToBackStack(LoginPageFragment.USER_SELECT);
		fragmentTransaction.commitAllowingStateLoss();

		handler.sendEmptyMessage(LoginPageFragment.SHOW_BACK_BUTTON);
	}

	private void getEmployeeListFromNet(final View v) {
		RequestParams params = new RequestParams();
		params.put("mid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getMerchantNo().trim());
		params.put("cpuid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getCpuId());
		MyLog.e("params:" + params);

		CynovoHttpClient.post("api/login/get_employee_list_with_phoneid.php", params, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				pDialog.show();
				v.setVisibility(View.GONE);
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				MyLog.e("response:" + response);
				try {
					int ret = response.getInt("ret");
					if (ret == 0) { // success!
						v.setVisibility(View.VISIBLE);
						JSONArray jsonArray = response.getJSONArray("data");
						AccountDBHelper dbHelper = new AccountDBHelper(getActivity());

						if (jsonArray != null && jsonArray.length() > 0) {
							dbHelper.open(); // open database!
							boolean deleteOK = dbHelper.deleteAllAccounts();
							MyLog.e("deleteOK:" + deleteOK);
							for (int i = 0; i < jsonArray.length(); i++) {
								Account account = new Account();
								JSONObject tmpJsonObject = jsonArray.getJSONObject(i);
								account.setAccount(tmpJsonObject.getString("TELEPHONENO"));
								account.setFirstName((tmpJsonObject.getString("FIRSTNAME") == null ? "" : tmpJsonObject
										.getString("FIRSTNAME")));
								account.setLastName((tmpJsonObject.getString("LASTNAME") == null ? "" : tmpJsonObject
										.getString("LASTNAME")));
								account.setAccountPrivilege(tmpJsonObject.getInt("VALUE"));
								account.setPass(tmpJsonObject.getString("PASS"));
								account.setPhoneID(tmpJsonObject.getInt("PHONEID"));

								dbHelper.insertAccountFromNet(account, false);
							}
							// close db!
							dbHelper.close();
							hasUpdateEmployeeList = true;

							userAdapter.refreshList();
							userAdapter.notifyDataSetChanged();
						}
					} else if (ret == -1) {
						// 设备被回收
						String msg = response.getString("msg");
						// 清空激活信息
						MySharedPreferencesEdit mySharedPreferencesEdit = MySharedPreferencesEdit
								.getInstancePublic(getActivity());
						mySharedPreferencesEdit.setIsDownloadSecretKey(false);
						// 清空本地数据库
						ClearAllDBData cad = new ClearAllDBData(getActivity());
						cad.clear();

						new AlertDialog.Builder(getActivity()).setTitle(R.string.hint).setMessage(msg)
								.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										// exit
										CloudPosApp.getInstance().finishAllActivity();
									}
								}).show();
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
			public void onFailure(Throwable error, String content) {
				error.printStackTrace();
				super.onFailure(error, content);
				try {
					Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
					pDialog.dismiss();
					v.setVisibility(View.VISIBLE);
					userAdapter.refreshList();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
}
