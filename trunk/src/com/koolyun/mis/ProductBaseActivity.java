package com.koolyun.mis;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.koolyun.mis.R;

public class ProductBaseActivity extends AbstractActivity implements OnClickListener {

	View titalButton = null;
	Button mBack = null;
	ActionBar actionbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionbar = getActionBar();

		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayOptions(1, ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		actionbar.setDisplayOptions(1, ActionBar.DISPLAY_SHOW_CUSTOM);
		actionbar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_title_back:
			finish();
			break;
		default:
			break;
		}
	}

	protected void user_account_setup() {

	}
}
