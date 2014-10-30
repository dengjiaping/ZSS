package com.koolyun.mis;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.koolyun.mis.R;

public class SaleBaseActivity extends AbstractActivity implements OnClickListener, OnCheckedChangeListener {
	Button useraccountBtn = null;
	Button unlockButton = null;
	View titalButton = null;
	View adminBtn = null;
	Button changeaccountBtn = null;
	FragmentManager fragmentManager;
	RadioButton[] radioBtnArray = new RadioButton[4];
	RadioGroup radioGroup = null;
	protected ImageView[] imgArray = new ImageView[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActionBar bar = getActionBar();

		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_USE_LOGO);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		bar.setDisplayOptions(1, ActionBar.DISPLAY_SHOW_CUSTOM);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_USE_LOGO);

		fragmentManager = getFragmentManager();
		titalButton = getLayoutInflater().inflate(R.layout.sale_title_buttons, null);
		useraccountBtn = (Button) titalButton.findViewById(R.id.sale_user_name);

		useraccountBtn.setOnClickListener(this);
		radioGroup = (RadioGroup) titalButton.findViewById(R.id.group);
		radioGroup.setOnCheckedChangeListener(this);
		radioBtnArray[0] = (RadioButton) titalButton.findViewById(R.id.recentRecord);
		radioBtnArray[1] = (RadioButton) titalButton.findViewById(R.id.merchantManager);
		radioBtnArray[2] = (RadioButton) titalButton.findViewById(R.id.recentSetting);
		radioBtnArray[3] = (RadioButton) titalButton.findViewById(R.id.shoppingPage);
		imgArray[0] = (ImageView) titalButton.findViewById(R.id.actionbarline1);
		imgArray[1] = (ImageView) titalButton.findViewById(R.id.actionbarline2);
		bar.setCustomView(titalButton, new ActionBar.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		bar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sale_user_name:
			user_account_setup();
			break;
		case R.id.userDialogchangeaccount:
			change_useraccount();
			break;
		default:
			break;
		}
	}

	protected void user_account_setup() {

	}

	private void change_useraccount() {
		Intent intent = new Intent();
		intent.setClass(SaleBaseActivity.this, LoginInActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}

}
