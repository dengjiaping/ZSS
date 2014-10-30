package com.koolyun.mis.fragment;
 
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.koolyun.mis.R;

public class LeftBarFragment extends AbstractFragment {
	ViewFlipper leftbarviewflpper;
	int container_layout = R.id.leftbar_container;
	FragmentManager mFragmentManager = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		LeftBarDealInfoFragment mLeftBarDealInfoFragment = new LeftBarDealInfoFragment();
		mLeftBarDealInfoFragment.setmLeftBarFragment(this);
	    fragmentTransaction.add(container_layout, mLeftBarDealInfoFragment); 
		fragmentTransaction.commit();
		View mLeftBar = inflater.inflate(R.layout.pen_content, container, false);
		return mLeftBar;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void addNewFragMent(Fragment mfm)
	{
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.obj_push_left_in, 
				R.anim.obj_push_left_out,R.anim.obj_push_right_in, R.anim.obj_push_right_out); 
		fragmentTransaction.replace(container_layout,mfm);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	public void addLeftBarProductItemInfoFragment(int orderid,int type,int level){
		LeftBarProductItemInfoFragment leftbarProduct = LeftBarProductItemInfoFragment.newInstance(orderid,type,level);
		leftbarProduct.setmLeftBarFragment(this);
		addNewFragMent(leftbarProduct);
	}
  
	public void addLeftBarProductItemFragment(int orderid){
		LeftBarProductItemFragment leftbarProduct = LeftBarProductItemFragment.newInstance(orderid);
		leftbarProduct.setmLeftBarFragment(this);
		addNewFragMent(leftbarProduct);
	}
  
	public void addStatistikFragment(){
		StatistikFragment mStatistikFragment = new StatistikFragment();
		mStatistikFragment.setmLeftBarFragment(this);
		addNewFragMent(mStatistikFragment);
	}
  
	public void addLeftBarProductBilling(int orderprocessid){
		LeftBarBillingShowFragment leftbarbillingShow =LeftBarBillingShowFragment.newInstance(orderprocessid);
		leftbarbillingShow.setmLeftBarFragment(this);
		addNewFragMent(leftbarbillingShow);
	}
  
	public void addLeftBarAllBillFragment(){
		LeftBarAllBillFragment leftbarallbillfragment = new LeftBarAllBillFragment();
		leftbarallbillfragment.setmLeftBarFragment(this);
		addNewFragMent(leftbarallbillfragment);
	}
	
	public void addLeftBarHangUpFragment(){
		LeftBarHangUpFragment leftbarallbillfragment = new LeftBarHangUpFragment();
		leftbarallbillfragment.setmLeftBarFragment(this);
		addNewFragMent(leftbarallbillfragment);
	}
	
	public void addLeftSalerecordFragment(){
		LeftSaleRecordFragment leftbarallbillfragment = new LeftSaleRecordFragment();
		leftbarallbillfragment.setmLeftBarFragment(this);
		addNewFragMent(leftbarallbillfragment);
	}
	
  
	public void backBtnClicked(){
		if(mFragmentManager.getBackStackEntryCount() > 0)
			mFragmentManager.popBackStack();
	}
}
