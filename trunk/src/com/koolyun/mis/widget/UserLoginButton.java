package com.koolyun.mis.widget;

import com.koolyun.mis.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserLoginButton extends LinearLayout {

	ImageView userpic;
	TextView username;

	public UserLoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.user_button, this, true);
		userpic = (ImageView) findViewById(R.id.user_pic);
		username = (TextView) findViewById(R.id.user_name);
	}

	public void setImage(int resId) {
		userpic.setImageResource(resId);
	}

	public void setText(String name) {
		username.setText(name);
	}

}
