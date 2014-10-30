package com.koolyun.mis.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.koolyun.mis.R;
import com.koolyun.mis.util.Common;

public class MemberFragment extends AbstractLoadingFragment implements OnClickListener {
	private Button memberManageButton;
	private Button memberPayButton;
	private Handler handler;
	public static final int SWITCH_TO_PAY_FRAGMENT = 0;
	public static final int HIDE_KEYBOARD = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case SWITCH_TO_PAY_FRAGMENT: // 切换到会员卡充值界面

					break;
				case HIDE_KEYBOARD:
					Common.HideKeyboardIfExist(MemberFragment.this);
					break;
				}

				return false;
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.member, container, false);

		initViews(v);

		FragmentManager fm = getActivity().getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.memberLeftLayout, new MemberManageFragment(handler));
		ft.commitAllowingStateLoss();

		return v;
	}

	private void initViews(View v) {
		memberManageButton = (Button) v.findViewById(R.id.memberManageButton);
		memberManageButton.setOnClickListener(this);
		memberPayButton = (Button) v.findViewById(R.id.memberPayButton);
		memberPayButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.memberManageButton: { // 会员管理
			FragmentManager fm = getActivity().getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.memberLeftLayout, new MemberManageFragment(handler));
			ft.commitAllowingStateLoss();

			memberManageButton.setBackgroundResource(R.drawable.goods_list_button1);
			memberPayButton.setBackgroundResource(R.drawable.keyboard_button1);
			break;
		}
		case R.id.memberPayButton: { // 会员卡充值
			FragmentManager fm = getActivity().getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.memberLeftLayout, new MemberPayFragment());
			ft.commitAllowingStateLoss();

			memberManageButton.setBackgroundResource(R.drawable.goods_list_button0);
			memberPayButton.setBackgroundResource(R.drawable.keyboard_button0);
			break;
		}
		}
	}
}
