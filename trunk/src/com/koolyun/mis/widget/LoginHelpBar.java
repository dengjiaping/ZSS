package com.koolyun.mis.widget;

import com.koolyun.mis.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginHelpBar extends RelativeLayout {
	Button backbtn;
	TextView leftText;
	TextView rightText;

	public LoginHelpBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.user_help_bar, this, true);
		backbtn = (Button) this.findViewById(R.id.login_bak);
		leftText = (TextView) this.findViewById(R.id.left_text);
		rightText = (TextView) this.findViewById(R.id.right_text);
	}

	public void setButtonVisibility(int flag) {
		backbtn.setVisibility(flag);
	}

	public void setLeftText(String left) {
		leftText.setText(left);
	}

	public void setLeftText(int left) {
		leftText.setText(left);
	}

	public void setRightText(String right) {
		rightText.setText(right);
	}

	public void setRightText(int right) {
		rightText.setText(right);
	}

}
