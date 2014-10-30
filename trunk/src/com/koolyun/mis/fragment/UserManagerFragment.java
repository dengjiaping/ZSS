package com.koolyun.mis.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.UserManagerAdapter;
import com.koolyun.mis.core.user.AccountManager;

@SuppressLint("CommitTransaction")
public class UserManagerFragment extends AbstractLoadingFragment implements OnClickListener {
	private View v = null;
	private ListView mAccountList = null;
	private UserManagerAdapter mUserManagerAdapter = null;
	private Handler handler2;
	public static final int REFRESH_USER_LIST = 0;
	public static final int HIDE_RIGHT_LAYOUT = 1;
	private Button mAddAccountButton;
	private RelativeLayout userManagerFragmentLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler2 = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case REFRESH_USER_LIST:
					mUserManagerAdapter.updateList(AccountManager.getInstance().getAccountList(true));
					break;
				case HIDE_RIGHT_LAYOUT:
//					userManagerFragmentLayout.setVisibility(View.GONE);
					if(msg.obj != null && msg.obj instanceof Fragment) {
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.setCustomAnimations(R.anim.obj_push_left_in,
								R.anim.obj_push_left_out, R.anim.obj_push_right_in,
								R.anim.obj_push_right_out); 
						fragmentTransaction.remove((Fragment) msg.obj);
						fragmentTransaction.commitAllowingStateLoss();
					}
					break;
				}
				return false;
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.usermanager_layout, container, false);

		mAddAccountButton = (Button) v.findViewById(R.id.usermg_add_btn);
		mAddAccountButton.setOnClickListener(this);
		
		if(AccountManager.getInstance().getCurrentAccount().getAccountPrivilege() == 3) {
			mAddAccountButton.setVisibility(View.GONE); // 店员不可以添加员工
		}
		
		userManagerFragmentLayout = (RelativeLayout) v.findViewById(R.id.userManagerFragmentLayout);
		
		mAccountList = (ListView) v.findViewById(R.id.usermg_listview);
		mUserManagerAdapter = new UserManagerAdapter(this);
		mAccountList.setAdapter(mUserManagerAdapter);
		
//		changePhoneButton = (Button) v.findViewById(R.id.changePhoneButton);
//		changePhoneButton.setOnClickListener(this);

		return v;
	}


	public void setEditAccountId(int id) {
		userManagerFragmentLayout.setVisibility(View.VISIBLE);
//		int privilege = AccountManager.getAccountById(id).getAccountPrivilege();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		EditEmployeeFragment emf = new EditEmployeeFragment(handler2);
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		emf.setArguments(bundle);
		ft.setCustomAnimations(R.anim.obj_push_left_in, 
				R.anim.obj_push_left_out,R.anim.obj_push_right_in, R.anim.obj_push_right_out); 
		ft.replace(R.id.userManagerFragmentLayout, emf);
		ft.commit();
	}
	
	private void addNew() {
		userManagerFragmentLayout.setVisibility(View.VISIBLE);
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		VerifyPhoneFragment emf = new VerifyPhoneFragment(handler2);
		ft.setCustomAnimations(R.anim.obj_push_left_in, 
				R.anim.obj_push_left_out,R.anim.obj_push_right_in, R.anim.obj_push_right_out); 
		ft.replace(R.id.userManagerFragmentLayout, emf);
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.usermg_add_btn:
			addNew();
			break;
		}
	}
	
}
