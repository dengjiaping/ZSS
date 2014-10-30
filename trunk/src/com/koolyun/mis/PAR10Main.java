package com.koolyun.mis;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.koolyun.mis.R;
import com.koolyun.mis.sqlite.DBHelper;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.communicate.PollService;

public class PAR10Main extends AbstractActivity {

	@SuppressWarnings("unused")
	private PollService pollService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Common.addShortcutToDesktop(this, R.string.app_name, R.drawable.kivvi_icon);
		this.setContentView(R.layout.main_activity);
		new InitTask().execute();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent nextIntent = new Intent(PAR10Main.this, LoginInActivity.class);
				Bundle translateBundle = ActivityOptions.makeCustomAnimation(PAR10Main.this, android.R.anim.fade_in,
						android.R.anim.fade_out).toBundle();
				PAR10Main.this.startActivity(nextIntent, translateBundle);
			}
		}, 10);

		Intent bindIntent = new Intent(PAR10Main.this, PollService.class);
		bindService(bindIntent, conn, Context.BIND_AUTO_CREATE);
	}

	private class InitTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			CloudPosApp app = CloudPosApp.getInstance();
			int retval = DBHelper.copyImage(app, "DB_IMAGE");
			Log.i("copyImage", String.valueOf(retval));
			return null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}

	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pollService = ((PollService.MyBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			pollService = null;
		}

	};

}
