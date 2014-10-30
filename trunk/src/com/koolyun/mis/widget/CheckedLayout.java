package com.koolyun.mis.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.koolyun.mis.R;

public class CheckedLayout extends RelativeLayout implements Checkable {

	private boolean isChecked = false;
	public CheckedLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean isChecked() {
		return false;
	}

	@Override
	public void setChecked(boolean checked) {
 
		this.isChecked = checked;
		if(isChecked){
			setBackgroundResource(R.drawable.setting_dir_btn_blue);
		}else{
			setBackgroundResource(R.drawable.setting_dir_btn_white);
		}
	}

	@Override
	public void toggle() {
	}

}
