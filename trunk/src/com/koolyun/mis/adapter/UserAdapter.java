package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;

public class UserAdapter extends BaseAdapter {
	Context mContext;
	List<Account> mAccountList;

	public UserAdapter(Context context) {
		this.mContext = context;
		mAccountList = AccountManager.getInstance().getAccountList(false);
	}

	public void refreshList() {
		mAccountList = AccountManager.getInstance().getAccountList(false);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mAccountList.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position < mAccountList.size())
			return mAccountList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return getItemViewType(position);
	}

	@Override
	public int getItemViewType(int position) {
		if (position < mAccountList.size())
			return 0;
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, null);
		TextView name = (TextView) retval.findViewById(R.id.user_name);
		TextView title = (TextView) retval.findViewById(R.id.user_title);

		final Button button = (Button) retval.findViewById(R.id.image);

		if (this.getItemViewType(position) == 0) {
			name.setText(mAccountList.get(position).getNameToshow());
			button.setBackgroundResource(R.drawable.name_bg_xml);
			title.setText(AccountManager.ManageLevel.valueOf(mAccountList.get(position).getAccountPrivilege()));
		} else {
			button.setBackgroundResource(R.drawable.name_add_xml);
		}

		retval.setBackgroundColor(0);
		return retval;
	}
}
