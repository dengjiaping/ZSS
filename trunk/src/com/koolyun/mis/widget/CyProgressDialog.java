package com.koolyun.mis.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.koolyun.mis.R;

public class CyProgressDialog extends Dialog {

	public CyProgressDialog(Context context, String tip) {
		super(context, R.style.Theme_dialog);

		setContentView(R.layout.progress_dialog);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		setCanceledOnTouchOutside(false);
		TextView tipTextView = (TextView) findViewById(R.id.tipTextView);
		tipTextView.setText(tip);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView loadingImageView = (ImageView) findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) loadingImageView.getBackground();
		animationDrawable.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 返回按键不可用
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
