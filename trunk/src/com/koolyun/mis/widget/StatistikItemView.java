package com.koolyun.mis.widget;

import com.koolyun.mis.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatistikItemView extends RelativeLayout {

	ImageView imageLeft;
	TextView itemText;
	MoneyView itemContent;

	public StatistikItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.statistik_item, this, true);
		imageLeft = (ImageView) this.findViewById(R.id.s_image);
		itemText = (TextView) this.findViewById(R.id.s_text);
		itemContent = (MoneyView) this.findViewById(R.id.s_content);
	}

	public void setImageResource(int id) {
		imageLeft.setBackgroundResource(id);
	}

	public void setText(String str) {
		itemText.setText(str);
	}

	public void setText(int str) {
		itemText.setText(str);
	}

	public void setContent(String str) {
		itemContent.setText(str);
	}

	public void setMoney(String money) {
		itemContent.setMoney(money);
	}

}
