package com.koolyun.mis.fragment;

import java.util.Calendar;
import android.annotation.SuppressLint;
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
import com.koolyun.mis.adapter.AllSalesAdapter;
import com.koolyun.mis.core.order.LiteOrderInfo;
import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.widget.MyExpandableListView;

/**
 * 全部记录fragment
 */
public class LeftBarAllBillFragment extends LeftBarBaseFragment implements OnDateSetListener {

	MyExpandableListView mMyExpandableListView = null;
	Button back_Btn = null;
	Button datePickerBtn = null;
	Calendar calendar = null;
	AllSalesAdapter mAllSalesAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.leftbar_allbill_fragment, container, false);
		back_Btn = (Button) layout.findViewById(R.id.leftbar_alldeal_back);
		datePickerBtn = (Button) layout.findViewById(R.id.date_picker);
		mMyExpandableListView = (MyExpandableListView) layout.findViewById(R.id.leftbar_all_expandListview);
		mAllSalesAdapter = new AllSalesAdapter(getActivity());
		mMyExpandableListView.setAdapter(mAllSalesAdapter);

		back_Btn.setOnClickListener(this);
		datePickerBtn.setOnClickListener(this);
		calendar = Calendar.getInstance();

		mMyExpandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				AllSalesAdapter rsA = (AllSalesAdapter) parent.getExpandableListAdapter();
				LiteOrderInfo loi = (LiteOrderInfo) rsA.getChild(groupPosition, childPosition);
				mLeftBarFragment.addLeftBarProductItemInfoFragment(loi.getOrder().getOrderID(), groupPosition, 2);
				return true;
			}
		});

		return layout;
	}

	@Override
	public void onResume() {
		if (mAllSalesAdapter != null)
			mAllSalesAdapter.updateList();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		if (JavaUtil.isFastDoubleClick())
			return;
		switch (v.getId()) {
		case R.id.leftbar_alldeal_back:
			mLeftBarFragment.backBtnClicked();
			break;
		case R.id.date_picker:
			DatePickerDialog dpd = new DatePickerDialog(this.getActivity(), this, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dpd.show();
			break;
		default:
			break;
		}
		super.onClick(v);
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		String str = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		int index = mAllSalesAdapter.getGroupIndexByTime(str);
		if (index == -1) {
			Toast.makeText(this.getActivity(), R.string.no_sale_info, Toast.LENGTH_SHORT).show();
		} else {
			for (int i = 0; i < mAllSalesAdapter.getGroupCount(); i++)
				mMyExpandableListView.collapseGroup(i);
			mMyExpandableListView.expandGroup(index, true);
		}
		Log.d("date", str);
	}

}
