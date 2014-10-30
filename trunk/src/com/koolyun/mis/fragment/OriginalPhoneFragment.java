package com.koolyun.mis.fragment;

import java.util.Locale;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.CynovoHttpClient;
import com.koolyun.mis.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ValidFragment")
public class OriginalPhoneFragment extends AbstractLoadingFragment implements OnClickListener, OnEditorActionListener {
	private Button originalReturnButton;
	private Button checkAccountButton;
	private TextView originalPhoneText;
	private EditText originalPasswordEdittext;
	private Handler handler;
	private int id;
	private String originalPhone;

	public OriginalPhoneFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getArguments();
		if (b != null) {
			id = b.getInt("id");
			originalPhone = b.getString("phone");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.original_phone, null);

		originalReturnButton = (Button) v.findViewById(R.id.originalReturnButton);
		originalReturnButton.setOnClickListener(this);
		checkAccountButton = (Button) v.findViewById(R.id.checkAccountButton);
		checkAccountButton.setOnClickListener(this);
		originalPhoneText = (TextView) v.findViewById(R.id.originalPhoneText);
		// originalPhoneEditext.setOnEditorActionListener(this);
		originalPasswordEdittext = (EditText) v.findViewById(R.id.originalPasswordEdittext);
		originalPasswordEdittext.setOnEditorActionListener(this);

		if (originalPhone != null) {
			originalPhoneText.setText(originalPhone);
		}

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.changePhoneLayout:
		// originalPhoneEditext.setText("");
		// originalPasswordEdittext.setText("");
		// break;
		case R.id.originalReturnButton: //
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			EditEmployeeFragment emf = new EditEmployeeFragment(handler);
			Bundle bundle = new Bundle();
			bundle.putInt("id", id);
			emf.setArguments(bundle);
			ft.replace(R.id.userManagerFragmentLayout, emf);
			ft.commit();
			break;
		case R.id.checkAccountButton:
			if (originalPasswordEdittext.getText() != null
					&& originalPasswordEdittext.getText().toString().trim().length() > 0) {
				checkAccount(originalPhone.trim(), originalPasswordEdittext.getText().toString().trim());
			} else {
				Toast.makeText(getActivity(), R.string.phone_psw_error, Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	private void checkAccount(final String phone, String psw) {
		RequestParams params = new RequestParams();
		params.put("phone", phone);
		try {
			params.put("psw", Common.SHA1.toSHA1(psw).toUpperCase(Locale.getDefault()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		CynovoHttpClient.post("api/change_phone/old_phone_number.php", params, new JsonHttpResponseHandler() {
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
						FragmentManager fm = getFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						NewPhoneFragment emf = new NewPhoneFragment(handler);
						Bundle bundle = new Bundle();
						bundle.putString("phone", phone);
						bundle.putInt("id", id);
						emf.setArguments(bundle);
						ft.replace(R.id.userManagerFragmentLayout, emf);
						ft.addToBackStack(null);
						ft.commit();
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

	@Override
	public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event1) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event1.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (arg0.getId()) {
			// case R.id.originalPhoneEditext:
			// originalPasswordEdittext.requestFocus();
			// break;
			case R.id.originalPasswordEdittext:
				if (originalPasswordEdittext.getText() != null
						&& originalPasswordEdittext.getText().toString().trim().length() > 0) {
					checkAccount(originalPhone.trim(), originalPasswordEdittext.getText().toString().trim());
				} else {
					Toast.makeText(getActivity(), R.string.phone_psw_error, Toast.LENGTH_LONG).show();
				}
				break;
			}
		}
		return false;
	}
}
