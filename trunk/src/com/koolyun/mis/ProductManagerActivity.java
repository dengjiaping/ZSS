package com.koolyun.mis;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;

import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.fragment.BackFragment;
import com.koolyun.mis.fragment.DataManagerFragment;
import com.koolyun.mis.fragment.SettingFragment;
import com.koolyun.mis.fragment.SettingMerchantFragment;
import com.koolyun.mis.fragment.UserManagerFragment;

public class ProductManagerActivity extends ProductBaseActivity {

	int container_layout = R.id.productactivity;
	FragmentManager mFragmentMgr = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productmanager_layout);

		// ActionBar actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab backFagment = actionbar.newTab().setText(getResources().getText(R.string.back));
		backFagment.setIcon(R.drawable.detail_back);
		MyTabListener backListener = new MyTabListener(new BackFragment());
		backFagment.setTabListener(backListener);
		actionbar.addTab(backFagment, 0, false);

		Tab goodsManager = actionbar.newTab().setText(getResources().getText(R.string.goodsmanager));
		goodsManager.setIcon(R.drawable.product_manager_title);
		MyTabListener goodsListener = new MyTabListener(new DataManagerFragment());
		goodsManager.setTabListener(goodsListener);
		if (AccountManager.getInstance().getCurrentAccount().getAccountPrivilege() < 3) {
			actionbar.addTab(goodsManager, 1, true);
		}

		Tab userManager = actionbar.newTab().setText(getResources().getText(R.string.usermanager));
		userManager.setIcon(R.drawable.user_manager_title);
		MyTabListener userListener = new MyTabListener(new UserManagerFragment());
		userManager.setTabListener(userListener);
		if (AccountManager.getInstance().getCurrentAccount().getAccountPrivilege() < 3) {
			actionbar.addTab(userManager, 2, false);
		} else {
			actionbar.addTab(userManager, 1, true);
		}

		Tab updateApkTab = actionbar.newTab().setText(getResources().getText(R.string.settoperation));
		updateApkTab.setIcon(R.drawable.setting);
		MyTabListener updateListener = new MyTabListener(new SettingFragment());
		updateApkTab.setTabListener(updateListener);
		if (AccountManager.getInstance().getCurrentAccount().getAccountPrivilege() < 3) {
			actionbar.addTab(updateApkTab, 3, false);
		} else {
			actionbar.addTab(updateApkTab, 2, false);
		}

		// Tab membershipTab =
		// actionbar.newTab().setText(getResources().getText(R.string.membership_system));
		// membershipTab.setIcon(R.drawable.setting);
		// MyTabListener membershipListener = new MyTabListener(new
		// MemberFragment());
		// membershipTab.setTabListener(membershipListener);
		// if(AccountManager.getInstance().getCurrentAccount().getAccountPrivilege()
		// < 3) {
		// actionbar.addTab(membershipTab, 4, false);
		// } else {
		// actionbar.addTab(membershipTab, 3, false);
		// }
	}

	class MyTabListener implements TabListener {
		// 接收每个Tab对应的Fragment，操作
		private Fragment fragment;

		public MyTabListener(Fragment fragment) {
			this.fragment = fragment;
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {

		}

		// 当Tab被选中的时候添加对应的Fragment
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (fragment instanceof BackFragment) {
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				return;
			}
			ft.add(container_layout, fragment, null);
		}

		// 当Tab没被选中的时候删除对应的此Tab对应的Fragment
		@SuppressLint("CommitTransaction")
		@SuppressWarnings("unused")
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			FragmentManager mFragmentManager = null;
			FragmentTransaction fragmentTransaction = null;
			mFragmentManager = getFragmentManager();
			fragmentTransaction = mFragmentManager.beginTransaction();
			ft.remove(fragment);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SettingMerchantFragment.address = null;
		SettingMerchantFragment.companyName = null;
		SettingMerchantFragment.nickName = null;
	}
}
