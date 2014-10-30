package com.koolyun.mis.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.core.store.Store;
import com.koolyun.mis.core.store.StoreManager;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.CynovoHttpClient;
import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.util.MyLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SettingMerchantFragment extends AbstractLoadingFragment implements OnClickListener, OnEditorActionListener {
	private EditText changeNicknameEdit;
	private EditText changeAddressEdit;
	private Button changeStoreInfoButton;
	ImageView mIageLogo = null;

	public static String nickName;
	public static String companyName;
	public static String address;
	public static String companyAddress;

	String oldImagePath = null;

	private TextView companyNameText;
	private TextView mIDText;
	private TextView tIDText;
	private TextView companyAddressText; // 公司地址，但是现在数据库没有存这个字段
	private TextView cpuIDText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.update_merchant, container, false);
		mIageLogo = (ImageView) v.findViewById(R.id.update_merchant_logo);
		mIageLogo.setOnClickListener(this);
		// Button changeNicknameButton = (Button)
		// v.findViewById(R.id.changeNicknameButton);
		// changeNicknameButton.setOnClickListener(this);
		// Button changeAddressButton = (Button)
		// v.findViewById(R.id.changeAddressButton);
		// changeAddressButton.setOnClickListener(this);
		changeNicknameEdit = (EditText) v.findViewById(R.id.changeNicknameEdit);
		changeNicknameEdit.setOnEditorActionListener(this);
		changeAddressEdit = (EditText) v.findViewById(R.id.changeAddressEdit);
		changeAddressEdit.setOnEditorActionListener(this);

		companyNameText = (TextView) v.findViewById(R.id.companyNameText);
		mIDText = (TextView) v.findViewById(R.id.mIDText);
		tIDText = (TextView) v.findViewById(R.id.tIDText);
		companyAddressText = (TextView) v.findViewById(R.id.companyAddressText);
		cpuIDText = (TextView) v.findViewById(R.id.cpuIDText);

		// byte[] mid = new byte[15];
		// byte[] tid = new byte[8];
		// SafeInterface.open();
		// SafeInterface.safe_readID(mid, tid);
		// SafeInterface.close();
		// mIDText.setText(new String(mid));
		// tIDText.setText(new String(tid));

		mIDText.setText(MySharedPreferencesEdit.getInstancePublic(getActivity()).getMerchantNo());
		tIDText.setText(MySharedPreferencesEdit.getInstancePublic(getActivity()).getTerminalNo());

		cpuIDText.setText(MySharedPreferencesEdit.getInstancePublic(getActivity()).getCpuId());
		if (nickName != null && nickName.length() > 0 && !nickName.equals("null")) {
			changeNicknameEdit.setText(nickName);
		}
		if (address != null && address.length() > 0 && !address.equals("null")) {
			changeAddressEdit.setText(address);
		}
		if (companyName != null && companyName.length() > 0 && !companyName.equals("null")) {
			companyNameText.setText(companyName);
		}

		changeStoreInfoButton = (Button) v.findViewById(R.id.changeStoreInfoButton);
		changeStoreInfoButton.setOnClickListener(this);

		if (nickName == null || companyName == null || address == null)
			getStoreInfo();

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		Store store = StoreManager.getStore();
		changeNicknameEdit.setText((store.getNickName() == null || store.getNickName().length() == 0 || store
				.getNickName().equals("null")) ? "" : store.getNickName());
		changeAddressEdit.setText((store.getAddress() == null || store.getAddress().length() == 0 || store.getAddress()
				.equals("null")) ? "" : store.getAddress());

		companyNameText.setText(store.getCompanyName());
		companyAddressText.setText(store.getCompanyAddress());
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Store mStore = StoreManager.getStorePic();
		String loadPic = null;
		if (mStore != null) {
			loadPic = mStore.getStorePhoto();
		}
		String filePath = Common.getRealImagePath(loadPic);
		File file = new File(filePath);
		if (Common.checkValidImageName(filePath) && file.exists()) {
			Bitmap bitmap;
			try {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = 1;
				bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, opt);

				if (bitmap != null) {
					mIageLogo.setImageBitmap(Common.zoomBitmapBetter(bitmap, 100, 100));
				} else {
					mIageLogo.setBackgroundResource(R.drawable.logo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			mIageLogo.setBackgroundResource(R.drawable.logo);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.changeNicknameButton:
			changeNickname();
			break;
		case R.id.changeAddressButton:
			changeAddress();
			break;
		case R.id.update_merchant_logo:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 1);
			break;
		case R.id.changeStoreInfoButton:
			changeStoreInfo();
			break;
		}
	}

	private void changeStoreInfo() {
		if (changeAddressEdit.getText() == null || changeAddressEdit.getText().toString().trim().isEmpty()) {
			Toast.makeText(getActivity(), R.string.address_cannot_null, Toast.LENGTH_LONG).show();
			return;
		}
		if (changeNicknameEdit.getText() == null || changeNicknameEdit.getText().toString().trim().isEmpty()) {
			Toast.makeText(getActivity(), R.string.nick_name_cannot_null, Toast.LENGTH_LONG).show();
			return;
		}

		// Store store = StoreManager.getStore();
		//
		// store.setAddress(changeAddressEdit.getText().toString().trim());
		// store.setNickName(changeNicknameEdit.getText().toString().trim());
		StoreManager.updateStoreInfo(changeNicknameEdit.getText().toString().trim(), companyName, changeAddressEdit
				.getText().toString().trim(), companyAddress);
		Toast.makeText(getActivity(), R.string.change_success, Toast.LENGTH_LONG).show();

		RequestParams params = new RequestParams();
		params.put("sid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getStoreID());
		params.put("nickName", changeNicknameEdit.getText().toString().trim());
		params.put("address", changeAddressEdit.getText().toString().trim());

		CynovoHttpClient.post("api/store/change_store_info.php", params, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				MyLog.e("response:" + response);
				try {
					int ret = response.getInt("ret");
					if (ret == 0) {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, response);
			}

			@Override
			public void onFailure(Throwable e, String content) {
				super.onFailure(e, content);
				e.printStackTrace();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			final String fileName;
			Uri uri = data.getData();
			oldImagePath = JavaUtil.getPathFromUri(uri, this.getActivity());

			ContentResolver cr = this.getActivity().getContentResolver();
			try {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = 1;
				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri), null, opt);
				if (bitmap != null) {
					mIageLogo.setImageBitmap(Common.zoomBitmapBetter(bitmap, 150, 150));
				} else {
					mIageLogo.setBackgroundResource(R.drawable.logo);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			fileName = oldImagePath.substring(oldImagePath.lastIndexOf('/') + 1);
			if (Common.checkValidImageName(fileName)) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						try {
							StoreManager.updateStoreLogo(fileName);
							MyLog.e("UpdateMerchantFragment", "" + oldImagePath);
							JavaUtil.copyFileToDir(getActivity(), oldImagePath, Common.IMAGE_PATH);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						return null;
					}

				}.execute();
			}
		}
	}

	/**
	 * 店铺信息
	 */
	private void getStoreInfo() {
		RequestParams params = new RequestParams();
		params.put("sid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getStoreID());

		CynovoHttpClient.post("api/store/get_store_info.php", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				MyLog.e("response:" + response);
				try {
					int ret = response.getInt("ret");
					if (ret == 0) {
						nickName = response.getString("nick_name");
						address = response.getString("store_address");
						companyAddress = response.getString("company_address");
						companyName = response.getString("company_name");
						changeNicknameEdit.setText((nickName == null || nickName.equals("null")) ? "" : nickName);
						changeAddressEdit.setText((address == null || address.equals("null")) ? "" : address);
						companyAddressText.setText((companyAddress == null || companyAddress.equals("null")) ? ""
								: companyAddress);
						companyNameText.setText((companyName == null || companyName.equals("null")) ? "" : companyName);

						StoreManager.updateStoreInfo(nickName, companyName, address, companyAddress);
					}
				} catch (Exception e) {
					e.printStackTrace();
					// Toast.makeText(getActivity(), R.string.network_error,
					// Toast.LENGTH_LONG).show();
				}
				super.onSuccess(statusCode, response);
			}

			@Override
			public void onFailure(Throwable e, String content) {
				super.onFailure(e, content);
				e.printStackTrace();
			}
		});
	}

	/**
	 * 修改地址
	 */
	private void changeAddress() {
		if (changeAddressEdit.getText() == null || changeAddressEdit.getText().toString().trim().isEmpty()) {
			Toast.makeText(getActivity(), R.string.address_cannot_null, Toast.LENGTH_LONG).show();
			return;
		}
		if (address.equals(changeAddressEdit.getText().toString().trim())) {
			// 先判断一下信息是否有改变
			Toast.makeText(getActivity(), R.string.no_change_info, Toast.LENGTH_LONG).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("sid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getStoreID());
		params.put("address", changeAddressEdit.getText().toString().trim());

		CynovoHttpClient.post("api/store/change_address.php", params, new JsonHttpResponseHandler() {
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
						address = changeAddressEdit.getText().toString().trim();
						Toast.makeText(getActivity(), R.string.change_success, Toast.LENGTH_LONG).show();
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

	/**
	 * 修改昵称
	 */
	private void changeNickname() {
		if (changeNicknameEdit.getText() == null || changeNicknameEdit.getText().toString().trim().isEmpty()) {
			Toast.makeText(getActivity(), R.string.nick_name_cannot_null, Toast.LENGTH_LONG).show();
			return;
		}
		if (nickName.equals(changeNicknameEdit.getText().toString().trim())) {
			// 先判断一下信息是否有改变
			Toast.makeText(getActivity(), R.string.no_change_info, Toast.LENGTH_LONG).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("sid", MySharedPreferencesEdit.getInstancePublic(getActivity()).getStoreID());
		params.put("nickName", changeNicknameEdit.getText().toString().trim());

		CynovoHttpClient.post("api/store/change_nick_name.php", params, new JsonHttpResponseHandler() {
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
						nickName = changeNicknameEdit.getText().toString().trim();
						Toast.makeText(getActivity(), R.string.change_success, Toast.LENGTH_LONG).show();
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
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.changeNicknameEdit: // 修改昵称
				changeAddressEdit.requestFocus();
				break;
			case R.id.changeAddressEdit: // 修改门店地址
				Common.HideKeyboardIfExist(SettingMerchantFragment.this);
				break;
			}
		}
		return false;
	}
}
