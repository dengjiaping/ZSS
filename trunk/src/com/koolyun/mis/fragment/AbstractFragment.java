package com.koolyun.mis.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.koolyun.mis.R;
import com.umeng.analytics.MobclickAgent;

public class AbstractFragment extends Fragment {
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}
	
	public void addSettingNewFragMent(Fragment mfm){
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.obj_push_left_in, 
				R.anim.obj_push_left_out,R.anim.obj_push_right_in, R.anim.obj_push_right_out); 
		fragmentTransaction.replace(R.id.settingLayout,mfm);
		//fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	

}
