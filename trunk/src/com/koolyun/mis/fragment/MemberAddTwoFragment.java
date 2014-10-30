package com.koolyun.mis.fragment;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
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

public class MemberAddTwoFragment extends AbstractDialogFragment implements OnClickListener, OnEditorActionListener,
		OnDateSetListener {
	private EditText memberAddRebateEdit;
	private Dialog dialog;
	private MemberManageFragment memberManageFragment;
	private EditText memberAddCardNoEdit;
	private Button memberCreateAccountButton;
	private Button memberAddDeadlineButton;
	private String name;
	private String phone;

	public MemberAddTwoFragment() {
	}

	public MemberAddTwoFragment(MemberManageFragment memberManageFragment, String name, String phone) {
		this.memberManageFragment = memberManageFragment;
		this.name = name;
		this.phone = phone;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.member_add2);
		memberAddCardNoEdit = (EditText) dialog.findViewById(R.id.memberAddCardNoEdit);
		memberAddCardNoEdit.setOnEditorActionListener(MemberAddTwoFragment.this);
		memberAddRebateEdit = (EditText) dialog.findViewById(R.id.memberAddRebateEdit);
		memberAddRebateEdit.setOnEditorActionListener(MemberAddTwoFragment.this);
		memberCreateAccountButton = (Button) dialog.findViewById(R.id.memberCreateAccountButton);
		memberCreateAccountButton.setOnClickListener(this);
		memberAddDeadlineButton = (Button) dialog.findViewById(R.id.memberAddDeadlineButton);
		memberAddDeadlineButton.setOnClickListener(this);

		Calendar calendar = Calendar.getInstance();
		String str = String.format("%04d/%02d/%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		memberAddDeadlineButton.setText(str);

		return dialog;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.memberAddCardNoEdit:
				memberAddRebateEdit.requestFocus();
				break;
			case R.id.memberAddRebateEdit:
				MyLog.e("Common.HideKeyboardIfExist");
				Common.HideKeyboardIfExist(MemberAddTwoFragment.this);
				break;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.memberCreateAccountButton: // 添加一张会员卡
			addMember();
			break;
		case R.id.memberAddDeadlineButton: // 有效期
			Calendar calendar = Calendar.getInstance();
			DatePickerDialog dpd = new DatePickerDialog(this.getActivity(), this, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dpd.show();
			break;
		}
	}

	private void addMember() {
		Common.HideKeyboardIfExist(MemberAddTwoFragment.this);
		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("cpuid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getCpuId());
		params.put("name", name);
		params.put("cardno", memberAddCardNoEdit.getText().toString().trim());
		params.put("cardtype", "1"); // 目前只有实体卡这一种会员卡
		params.put("onsale", memberAddRebateEdit.getText().toString().trim());
		params.put("deadline", memberAddDeadlineButton.getText().toString().trim() + " 00:00:00");
		MyLog.e("params:" + params);

		CynovoHttpClient.post("api/membership/add_membership.php", params, new JsonHttpResponseHandler() {
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
						dialog.dismiss();
						memberManageFragment.refreshList();
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
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		String str = String.format("%04d/%02d/%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		memberAddDeadlineButton.setText(str);
	}
}
