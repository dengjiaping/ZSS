package com.koolyun.mis.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import com.koolyun.mis.R;

public class MemberManageFragment extends AbstractLoadingFragment implements OnClickListener, OnItemClickListener {
	private Handler handler;
	private ImageButton addMemberShipButton;

	public MemberManageFragment() {

	}

	public MemberManageFragment(Handler handler) {
		this.handler = handler;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.member_manage, container, false);

		addMemberShipButton = (ImageButton) v.findViewById(R.id.addMemberShipButton);
		addMemberShipButton.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addMemberShipButton:
			MemberAddOneFragment memberAddOneFragment = new MemberAddOneFragment(MemberManageFragment.this);
			memberAddOneFragment.show(getFragmentManager(), "addone");
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		FragmentManager fm = getActivity().getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.memberRightLayout, new MembershipCardListFragment());
		ft.commitAllowingStateLoss();
	}

	public void showCreateMemberDialog(String name, String phone) {
		MemberAddTwoFragment memberAddTwoFragment = new MemberAddTwoFragment(MemberManageFragment.this, name, phone);
		memberAddTwoFragment.show(getFragmentManager(), "addone");
	}

	public void refreshList() {
		// TODO
	}
}
