package com.koolyun.mis.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.fragment.UserManagerFragment;

public class UserManagerAdapter extends BaseAdapter {
	private Account curretAccount;
	private List<Account> accountList = new ArrayList<Account>();
	private LayoutInflater listInflater;

	public static final int USER_ADMIN = 0;
	public static final int USER_NORMAL = 1;

	private UserManagerFragment mUserManagerFragment = null;

	public UserManagerAdapter(UserManagerFragment mUserManagerFragment) {
		this.mUserManagerFragment = mUserManagerFragment;
		AccountManager accountManager = AccountManager.getInstance();
		// int privilege =
		// accountManager.getCurrentAccount().getAccountPrivilege();
		// if(privilege == 3) {
		// accountList.add(accountManager.getCurrentAccount());
		// } else {
		// accountList = accountManager.getAccountListByPrivilege(privilege);
		// }
		accountList = accountManager.getAccountListByPrivilege(0);
		listInflater = LayoutInflater.from(mUserManagerFragment.getActivity());
		curretAccount = AccountManager.getInstance().getCurrentAccount();
	}

	@Override
	public int getCount() {
		if (accountList == null)
			return 0;
		return accountList.size();

	}

	@Override
	public Object getItem(int position) {
		return accountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItemViewType(position);
	}

	@Override
	public int getItemViewType(int position) {
		if (position > 1)
			return 1;
		else
			return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public void updateList(List<Account> mAccountList) {
		this.accountList = mAccountList;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.onsale_item_manager_info, null);
			holder.accountName = (TextView) convertView.findViewById(R.id.account_name);
			holder.accountPic = (ImageView) convertView.findViewById(R.id.account_pic);
			holder.accountEdit = (Button) convertView.findViewById(R.id.addount_edit);
			holder.adminText = (TextView) convertView.findViewById(R.id.admin_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.accountName.setText((accountList.get(position).getLastName() == null ? "" : accountList.get(position)
				.getLastName())
				+ (accountList.get(position).getFirstName() == null ? "" : accountList.get(position).getFirstName()));
		holder.accountPic.setBackgroundResource(R.drawable.account_pic);

		if (accountList.get(position).getAccountPrivilege() <= curretAccount.getAccountPrivilege()
				&& accountList.get(position).getAccountID() != curretAccount.getAccountID()) {
			// holder.adminText.setVisibility(View.VISIBLE);
			holder.accountEdit.setVisibility(View.GONE);
		} else {
			holder.adminText.setVisibility(View.INVISIBLE);
			holder.accountEdit.setVisibility(View.VISIBLE);
		}

		final int pos = position;
		holder.accountEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mUserManagerFragment.setEditAccountId(accountList.get(pos).getAccountID());
			}
		});

		return convertView;
	}

	public class ViewHolder {
		public TextView accountName;
		public ImageView accountPic;
		public Button accountEdit;
		public TextView adminText;
	}

}
