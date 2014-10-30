package com.koolyun.mis.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.koolyun.mis.R;

public class MembershipCardListFragment extends AbstractLoadingFragment implements OnClickListener {
	private ImageButton addMemberShipCardButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.membership_card_list, container, false);

		addMemberShipCardButton = (ImageButton) v.findViewById(R.id.addMemberShipCardButton);
		addMemberShipCardButton.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addMemberShipCardButton:
			FragmentManager fm = getActivity().getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.memberRightLayout, new MembershipCardInfoFragment());
			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
			break;
		}
	}
}
