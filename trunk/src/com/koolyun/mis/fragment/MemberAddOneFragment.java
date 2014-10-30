package com.koolyun.mis.fragment;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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

public class MemberAddOneFragment extends AbstractDialogFragment implements OnClickListener, OnEditorActionListener {
	private Dialog dialog;
	private MemberManageFragment memberManageFragment;
	private EditText memberAddNameEdit;
	private String name;
	private String phone;
	private EditText memberAddPhoneEdit;

	public MemberAddOneFragment() {

	}

	public MemberAddOneFragment(MemberManageFragment memberManageFragment) {
		this.memberManageFragment = memberManageFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.member_add);
		Button button = (Button) dialog.findViewById(R.id.memberAddAccountButton);
		button.setOnClickListener(MemberAddOneFragment.this);
		memberAddNameEdit = (EditText) dialog.findViewById(R.id.memberAddNameEdit);
		memberAddNameEdit.setOnEditorActionListener(MemberAddOneFragment.this);
		memberAddPhoneEdit = (EditText) dialog.findViewById(R.id.memberAddPhoneEdit);
		memberAddPhoneEdit.setOnEditorActionListener(MemberAddOneFragment.this);

		return dialog;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.memberAddNameEdit:
				memberAddPhoneEdit.requestFocus();
				break;
			case R.id.memberAddPhoneEdit:
				checkPhoneNo();
				break;
			}
		}
		return false;
	}

	private void checkPhoneNo() {
		if (memberAddNameEdit.getText() == null || memberAddNameEdit.getText().toString().trim().length() == 0
				|| memberAddPhoneEdit.getText() == null || memberAddPhoneEdit.getText().toString().trim().length() == 0) {
			Toast.makeText(getActivity(), R.string.phone_and_name_null, Toast.LENGTH_LONG).show();
			return;
		}
		if (memberAddPhoneEdit.getText().toString().trim().length() != 11
				|| !Common.isMobilePhoneNO(memberAddPhoneEdit.getText().toString().trim())) {
			Toast.makeText(getActivity(), R.string.error_phone_type, Toast.LENGTH_LONG).show();
			return;
		}
		name = memberAddNameEdit.getText().toString().trim();
		phone = memberAddPhoneEdit.getText().toString().trim();

		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("cpuid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getCpuId());
		MyLog.e("params:" + params);

		CynovoHttpClient.post("api/membership/check_phone_no.php", params, new JsonHttpResponseHandler() {
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
					if (ret == 0) { // 该手机号还没有在该商家注册过
						Common.HideKeyboardIfExist(MemberAddOneFragment.this);
						dialog.dismiss();
						memberManageFragment.showCreateMemberDialog(name, phone);
					} else if (ret == 1) { // 该手机号已经在该商家注册过
						// TODO

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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.memberAddAccountButton: // 添加一张会员卡
			checkPhoneNo();
			break;
		}
	}
}
