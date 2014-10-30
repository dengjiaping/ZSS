package com.koolyun.mis.widget;

import com.koolyun.mis.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SendButton extends Button implements OnClickListener {

	public interface BeingToSentInterface {
		public void BeginToSend();
	}

	static final int BtnStateNormal = 0;
	static final int BtnStateWait = 1;
	static final int BtnStateSuccess = 2;
	static final int BtnStateFail = 3;

	int ButtonState = BtnStateNormal;
	BeingToSentInterface btsInterface;

	AnimationDrawable frameAnimation;

	public SendButton(Context context) {
		super(context);
		this.setOnClickListener(this);
		changeState(BtnStateNormal);
	}

	public SendButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public SendButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOnClickListener(this);
		changeState(BtnStateNormal);

	}

	public void setInterface(BeingToSentInterface ifs) {
		btsInterface = ifs;
	}

	@SuppressLint("NewApi")
	void changeState(int state) {
		ButtonState = state;
		switch (state) {
		case BtnStateNormal:
			this.setBackground(this.getResources().getDrawable(R.drawable.button_xml));
			this.setText(getResources().getString(R.string.send_info));
			break;
		case BtnStateWait:
			this.setBackground(this.getResources().getDrawable(R.drawable.swip_btn_cover));
			this.setText(getResources().getString(R.string.during_send_info));
			frameAnimation = (AnimationDrawable) this.getBackground();
			frameAnimation.start();
			break;
		case BtnStateSuccess:
			if (frameAnimation != null)
				frameAnimation.stop();
			this.setBackground(this.getResources().getDrawable(R.drawable.green_btn_bg_xml));
			this.setText(getResources().getString(R.string.send_info_success));
			break;
		case BtnStateFail:
			if (frameAnimation != null)
				frameAnimation.stop();
			this.setBackground(this.getResources().getDrawable(R.drawable.red_btn_bg_xml));
			this.setText(getResources().getString(R.string.send_info_fail));
			break;
		default:
			break;

		}
	}

	@Override
	public void onClick(View v) {
		switch (ButtonState) {
		case BtnStateNormal:
			ButtonState = BtnStateWait;
			changeState(ButtonState);
			if (btsInterface != null)
				btsInterface.BeginToSend();
			break;
		case BtnStateWait:

			break;
		case BtnStateSuccess:

			break;
		case BtnStateFail:
			ButtonState = BtnStateNormal;
			changeState(ButtonState);
			break;
		default:
			break;

		}
	}
}
