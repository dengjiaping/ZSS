package com.koolyun.mis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.RecentSalesAdapter;
import com.koolyun.mis.core.order.LiteOrderInfo;
import com.koolyun.mis.widget.AddFootView;

public class LeftBarDealInfoFragment extends LeftBarBaseFragment {
	private View leftShowlayout =null;
	private ExpandableListView mExpandableListView = null;
	private RecentSalesAdapter mRecentSalesAdapter = null;
	private Button leftBarSalesStatistic = null;
	private Button leftBarAllRecord = null;
	private Button leftBarHangUpRecord = null;
	private Button leftBarSaleRecord = null;
	@Override
	public void onResume() {
		updateList();
		super.onResume();
	}
	
	public void updateList() {
		if(mRecentSalesAdapter != null) {
			mRecentSalesAdapter.updateList();
			mRecentSalesAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		leftShowlayout = inflater.inflate(R.layout.leftbardealinfo, container, false);

		leftBarSalesStatistic = (Button) leftShowlayout.findViewById(R.id.leftBarSalesStatistic);
		leftBarSalesStatistic.setOnClickListener(this);
		leftBarAllRecord = (Button) leftShowlayout.findViewById(R.id.leftBarAllRecord);
		leftBarAllRecord.setOnClickListener(this);
		leftBarHangUpRecord = (Button) leftShowlayout.findViewById(R.id.leftBarHangUpRecord);
		leftBarHangUpRecord.setOnClickListener(this);
		leftBarSaleRecord = (Button) leftShowlayout.findViewById(R.id.leftBarSaleRecord);
		leftBarSaleRecord.setOnClickListener(this);
		
	    mExpandableListView = (ExpandableListView) leftShowlayout.findViewById(R.id.leftbarexpandListview);

	    mExpandableListView.addFooterView(new AddFootView(getActivity(), mLeftBarFragment,
	    		getString(R.string.all_record), R.drawable.recent_all));
	    mExpandableListView.addFooterView(new AddFootView(getActivity(), mLeftBarFragment,
	    		getString(R.string.sales_statistics), R.drawable.sale));
	    mRecentSalesAdapter = new RecentSalesAdapter(getActivity());
	    
	    mExpandableListView.setAdapter(mRecentSalesAdapter);
	 	mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				RecentSalesAdapter rsA = (RecentSalesAdapter)parent.getExpandableListAdapter();
				LiteOrderInfo loi = (LiteOrderInfo)rsA.getChild(groupPosition, childPosition);
				if(loi != null)
					mLeftBarFragment.addLeftBarProductItemInfoFragment(loi.getOrder().getOrderID(),
							rsA.getPositionInList(groupPosition), 1);
				return true;
			}
		});
		return leftShowlayout;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.leftBarSalesStatistic:
			mLeftBarFragment.addStatistikFragment();
			break;
		case R.id.leftBarAllRecord:
			mLeftBarFragment.addLeftBarAllBillFragment();
			break;
		case R.id.leftBarHangUpRecord:
			mLeftBarFragment.addLeftBarHangUpFragment();
			break;
		case R.id.leftBarSaleRecord:
			mLeftBarFragment.addLeftSalerecordFragment();
			break;
		}
	}
	
	
}
