package com.koolyun.mis.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.SettingUserAdapter;
import com.koolyun.mis.fragment.AbstractFragment;
import com.koolyun.mis.util.MyLog;

public class UserManagerFragment1 extends AbstractFragment implements OnClickListener{

	private Button settingAddUserBtn;
	private ListView settingUserManagerListView;
	private SettingUserAdapter setUserAdapter;
 
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.setting_user_manager_layout, container, false);
		
		settingUserManagerListView = (ListView) result.findViewById(R.id.settingUserManagerListView);
		View foodView = LayoutInflater.from(getActivity()).inflate(R.layout.setting_user_foot_add_layout, null);
		settingAddUserBtn = (Button) foodView.findViewById(R.id.settingAddUserBtn);
		settingAddUserBtn.setOnClickListener(this);
		settingUserManagerListView.addFooterView(foodView);
		setUserAdapter = new SettingUserAdapter(getActivity());
		settingUserManagerListView.setAdapter(setUserAdapter);
		return result;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settingAddUserBtn:
			EditUserManagerFragment editUserManagerFragment = new EditUserManagerFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("type", 0);
			editUserManagerFragment.setArguments(bundle);
			addSettingNewFragMent(editUserManagerFragment);
			break;

		default:
			break;
		}
	}

	
}
