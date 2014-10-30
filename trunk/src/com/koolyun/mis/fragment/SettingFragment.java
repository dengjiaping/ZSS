package com.koolyun.mis.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.koolyun.mis.R;

public class SettingFragment extends AbstractFragment implements OnCheckedChangeListener {
	private Integer[] name = { R.string.merchant_message, R.string.check_update, R.string.copy_function };
 
	private FragmentManager fragmentManager;
	private RadioGroup settingRadioGp;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.setting_manager, container, false);
		settingRadioGp = (RadioGroup) result.findViewById(R.id.settingRadioGp);
		fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.settingLayout, new UserManagerFragment1());
		transaction.commit();
		settingRadioGp.setOnCheckedChangeListener(this);
//		fragmentManager = getFragmentManager();
//		marketTabHost = (FragmentTabHost) result.findViewById(android.R.id.tabhost); 
//		marketTabHost.setup(getActivity(), fragmentManager, R.id.settingLayout);
//		View indicator0 = getIndicatorView(R.string.merchant_message, R.layout.setting_tabhost_layout, 0);
//		View indicator1 = getIndicatorView(R.string.merchant_message, R.layout.setting_tabhost_layout, 1);
//		marketTabHost.addTab(marketTabHost.newTabSpec("tab0").setIndicator(indicator0),
//				UserManagerFragment1.class, null);
//		marketTabHost.addTab(marketTabHost.newTabSpec("tab1").setIndicator(indicator1),
//				UserManagerFragment1.class, null);
		return result;
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getCheckedRadioButtonId()) {
		case R.id.settingUserManage:
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.settingLayout, new UserManagerFragment1());
			transaction.commit();
			break;
		case R.id.settingProductDir:
			FragmentTransaction transaction1 = fragmentManager.beginTransaction();
			transaction1.replace(R.id.settingLayout, new SettingProductDirFragment());
			transaction1.commit();
			break;
		}
	}


}
