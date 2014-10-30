package com.koolyun.mis.fragment;

import com.koolyun.mis.R;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.MyLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditUserManagerFragment extends AbstractFragment implements OnClickListener{

	EditText editUserNameEdit,editUserPassWordEdit,editUserPassWordAgainEdit;
	Button editUserAddUserBtn;
	private Account mCurrentEditAccount = new Account();
	private AccountManager accountManager;
	
	public EditUserManagerFragment(){
		accountManager = AccountManager.getInstance();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.edit_user_manager_layout, container, false);
		
		editUserNameEdit = (EditText) result.findViewById(R.id.editUserNameEdit);
		editUserPassWordEdit = (EditText) result.findViewById(R.id.editUserPassWordEdit);
		editUserPassWordAgainEdit = (EditText) result.findViewById(R.id.editUserPassWordAgainEdit);
		editUserAddUserBtn = (Button) result.findViewById(R.id.editUserAddUserBtn);
		editUserAddUserBtn.setOnClickListener(this);
		if(getArguments().containsKey("type")){
			if(getArguments().getInt("type") == 0){
				editUserNameEdit.setEnabled(true);
			}else if(getArguments().getInt("type") == 1){
				editUserNameEdit.setEnabled(false);
				
				mCurrentEditAccount = (Account) getArguments().getSerializable("account");
				editUserNameEdit.setText(mCurrentEditAccount.getAccount());
			}
		}
		return result;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.editUserAddUserBtn:
			addUser();
			break;

		}
	}
	
	private void addUser(){
		if(editUserNameEdit.getText().toString() == null || editUserNameEdit.getText().toString().trim().isEmpty()){
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_adduser_namenotnull, Toast.LENGTH_LONG).show();
			return;
		}else if(editUserPassWordEdit.getText().toString() == null || editUserPassWordEdit.getText().toString().trim().isEmpty()
				||editUserPassWordAgainEdit.getText().toString() == null || editUserPassWordAgainEdit.getText().toString().trim().isEmpty()){
			Toast.makeText(CloudPosApp.getInstance(), R.string.password_hint, Toast.LENGTH_LONG).show();
			return;
		}else if(!editUserPassWordEdit.getText().toString().trim().equals(editUserPassWordAgainEdit.getText().toString().trim())){
			Toast.makeText(CloudPosApp.getInstance(), R.string.psw_different, Toast.LENGTH_LONG).show();
			return;
		}
		mCurrentEditAccount.setFirstName(editUserNameEdit.getText().toString().trim());
		mCurrentEditAccount.setLastName(editUserNameEdit.getText().toString().trim());
		mCurrentEditAccount.setAccount(editUserNameEdit.getText().toString().trim());
		mCurrentEditAccount.setPass(editUserPassWordEdit.getText().toString().trim());
		mCurrentEditAccount.setAccountPrivilege(3);
		int result = accountManager.saveAccount(mCurrentEditAccount);
		if(result >=0){
			addSettingNewFragMent(new UserManagerFragment1());
		}else if(result == -2){
			Toast.makeText(CloudPosApp.getInstance(), R.string.user_exist, Toast.LENGTH_LONG).show();
		}else if(result == -3){
			Toast.makeText(CloudPosApp.getInstance(), R.string.passWd_length, Toast.LENGTH_LONG).show();
		}
		MyLog.i("--------------"+result);
		
	}

	
}
