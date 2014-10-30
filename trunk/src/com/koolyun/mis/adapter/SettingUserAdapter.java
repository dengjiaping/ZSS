package com.koolyun.mis.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.fragment.EditUserManagerFragment;
import com.koolyun.mis.util.CloudPosApp;

public class SettingUserAdapter extends BaseAdapter {

	LayoutInflater layoutInfalter;
	private Context context;
	private List<Account> list;
	public SettingUserAdapter(Context context){
		this.context = context;
		layoutInfalter = LayoutInflater.from(context);
		list = AccountManager.getInstance().getAccountList(true);
	//	MyLog.i("-----getAccountPrivilege------= "+list.get(0).getAccountPrivilege());
	}
	
	
	@Override
	public int getCount() {
		if(list != null && list.size() >0){
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if(convertView == null){
			convertView = layoutInfalter.inflate(R.layout.setting_user_item_layout, null);
			holderView = new HolderView();
			holderView.settingNameText = (TextView) convertView.findViewById(R.id.settingUserItemText);
			holderView.settingChangeBtn = (Button) convertView.findViewById(R.id.settingUserChangeBtn);
			holderView.SetttingDeleteBtn = (Button) convertView.findViewById(R.id.settingUserDeleteBtn);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		holderView.settingNameText.setText(""+list.get(position).getAccount());
		holderView.settingChangeBtn.setOnClickListener(new MyOnclick(position));
		holderView.SetttingDeleteBtn.setOnClickListener(new MyOnclick(position));
		
		
		return convertView;
	}

	class MyOnclick implements OnClickListener{
		private int  position;
		
		public MyOnclick(int position){
			this.position = position;
		}
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.settingUserChangeBtn:
				
				Account account = AccountManager.getInstance().getCurrentAccount();
				if(account.getAccountPrivilege() == 1){
					EditUserManagerFragment editUserManagerFragment = new EditUserManagerFragment();
					Bundle bundle = new Bundle();
					bundle.putInt("type", 1);
					bundle.putSerializable("account", list.get(position));
					editUserManagerFragment.setArguments(bundle);
					addSettingNewFragMent(editUserManagerFragment);
				}else{
					Toast.makeText(CloudPosApp.getInstance(), R.string.not_pri, Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.settingUserDeleteBtn:
				Account account1 = AccountManager.getInstance().getCurrentAccount();
				Account accountModify = list.get(position);
				if(account1.getAccountPrivilege() == 1 && accountModify.getAccountPrivilege() != 1){
					
					new AlertDialog.Builder(context).setTitle(R.string.hint)
	    			.setMessage(R.string.allow_delete)
	    			.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							AccountManager.getInstance().deleteAccount(list.get(position).getAccountID());
							list = AccountManager.getInstance().getAccountList(true);
							notifyDataSetChanged();
						}
					})
	    			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
						}
					}).show();
					
				}else{
					Toast.makeText(CloudPosApp.getInstance(), R.string.not_pri, Toast.LENGTH_LONG).show();
				}
				break;
			}
			
		}
		
	}
	
	public void addSettingNewFragMent(Fragment mfm){
		FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.obj_push_left_in, 
				R.anim.obj_push_left_out,R.anim.obj_push_right_in, R.anim.obj_push_right_out); 
		fragmentTransaction.replace(R.id.settingLayout,mfm);
		//fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	static class HolderView{
		public TextView settingNameText;
		public Button settingChangeBtn;
		public Button SetttingDeleteBtn;
	}
}
