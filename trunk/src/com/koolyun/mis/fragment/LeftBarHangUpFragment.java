package com.koolyun.mis.fragment;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.HandUpAdapter;
import com.koolyun.mis.core.order.LiteOrderInfo;
import com.koolyun.mis.util.MyLog;

public class LeftBarHangUpFragment extends LeftBarBaseFragment implements OnDateSetListener{

	private Button leftBarHangUpBtn;
	private ExpandableListView leftBarHandUpListView;
	private View leftBarLayout;
	private HandUpAdapter leftBarHandUpAdapter;
	private Button leftBarHandUpDateBtn ;
	Calendar calendar = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		leftBarLayout = inflater.inflate(R.layout.leftbar_hangup_layout, container, false);
		leftBarHangUpBtn = (Button) leftBarLayout.findViewById(R.id.leftBarHangUpBtn);
		leftBarHandUpListView = (ExpandableListView) leftBarLayout.findViewById(R.id.leftbar_handup_expandListview);
		//leftBarHandUpListView.setOnItemClickListener(this);
		leftBarHangUpBtn.setOnClickListener(this);
		leftBarHandUpDateBtn = (Button) leftBarLayout.findViewById(R.id.handUpDatePicker);
		leftBarHandUpDateBtn.setOnClickListener(this);
		leftBarHandUpAdapter = new HandUpAdapter(getActivity());
		leftBarHandUpListView.setAdapter(leftBarHandUpAdapter);
		calendar = Calendar.getInstance();
		leftBarHandUpListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				HandUpAdapter rsA = (HandUpAdapter) parent.getExpandableListAdapter();
				LiteOrderInfo loi = (LiteOrderInfo) rsA.getChild(groupPosition, childPosition);
				mLeftBarFragment.addLeftBarProductItemInfoFragment(loi.getOrder().getOrderID(), groupPosition, 1);
				return true;
			}
		});
		return leftBarLayout;
	}

	@Override
	public void onResume() {
		if(leftBarHandUpAdapter != null){
			leftBarHandUpAdapter.updateList();
		}
		MyLog.i("-HandUp---onResume------");
		super.onResume();
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.leftBarHangUpBtn:
			mLeftBarFragment.backBtnClicked();
			break;
		case R.id.handUpDatePicker:
			DatePickerDialog dpd = new DatePickerDialog(this.getActivity(), this, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dpd.show();
			break;
		}
	}


	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		String str = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		int index = leftBarHandUpAdapter.getGroupIndexByTime(str);
		if (index == -1) {
			Toast.makeText(this.getActivity(), R.string.no_hand_up_info, Toast.LENGTH_SHORT).show();
		} else {
			for (int i = 0; i < leftBarHandUpAdapter.getGroupCount(); i++)
				leftBarHandUpListView.collapseGroup(i);
			leftBarHandUpListView.expandGroup(index, true);
		}
		Log.d("date", str);
		
	}
	
	

}
