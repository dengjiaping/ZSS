package com.koolyun.mis.fragment;

import android.app.ListFragment;

import com.umeng.analytics.MobclickAgent;

public class AbstractListFragment extends ListFragment {
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
}
