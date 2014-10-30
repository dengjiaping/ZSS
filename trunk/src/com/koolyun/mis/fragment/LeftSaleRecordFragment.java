package com.koolyun.mis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.LeftBarSaleRecordAdapter;
import com.koolyun.mis.core.order.LiteOrderInfo;

public class LeftSaleRecordFragment extends LeftBarBaseFragment implements OnItemClickListener{

	private Button leftBarSaleRecordBtn;
	private ListView leftBarSaleRecordListView;
	private View leftBarSaleView;
	private LeftBarSaleRecordAdapter leftBarHandUpAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		leftBarSaleView = inflater.inflate(R.layout.leftbar_sale_record_layout, container, false);
		leftBarSaleRecordBtn  = (Button) leftBarSaleView.findViewById(R.id.leftBarSaleRecordBtn);
		leftBarSaleRecordListView = (ListView) leftBarSaleView.findViewById(R.id.leftBarSaleRecordListView);
		leftBarSaleRecordBtn.setOnClickListener(this);
		leftBarSaleRecordListView.setOnItemClickListener(this);
		leftBarHandUpAdapter = new LeftBarSaleRecordAdapter(getActivity());
		leftBarSaleRecordListView.setAdapter(leftBarHandUpAdapter);
		return leftBarSaleView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		LiteOrderInfo loi = (LiteOrderInfo) arg0.getItemAtPosition(arg2);
		if(loi != null)
			mLeftBarFragment.addLeftBarProductItemInfoFragment(loi.getOrder().getOrderID(),
					2, 1);
		
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.leftBarSaleRecordBtn:
			mLeftBarFragment.backBtnClicked();
			break;

		default:
			break;
		}
	}
	
	
	
}
