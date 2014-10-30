package com.koolyun.mis.widget;

import com.koolyun.mis.R;
import com.koolyun.mis.util.BasicArithmetic;
import com.koolyun.mis.util.NumberFormater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PriceEdit extends EditText implements View.OnKeyListener {

	public static final String MAXPRICE = "999999.99";
	public static final int Negative = 0;
	public static final int Positive = 1;
	public static final int BothNP = 2;

	private int type = BothNP;

	private String maxPrice = MAXPRICE;
	private String currentPrice = "0.00";

	public PriceEdit(Context context) {
		super(context);
		InitConfig();
	}

	public PriceEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitConfig();
	}

	public PriceEdit(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		InitConfig();
	}

	private void InitConfig() {
		this.setOnKeyListener(this);
		this.setSingleLine();
	}

	public int getType() {
		return type;
	}

	public void setType(int mtype) {
		this.type = mtype;
		if (type == Negative && !getText().toString().startsWith("-")) {
			this.setText("-" + getText().toString());
		} else if (type != Negative && getText().toString().startsWith("-")) {
			this.setText(getText().toString().replace("-", ""));
		}
		currentPrice = NumberFormater.currencyFormat(getText().toString());
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
		if (BasicArithmetic.compare(getNoSignString(), maxPrice) >= 0) {
			if (type == Negative)
				this.setText("-");
			else
				this.setText("");
		}
	}

	public String getCurrentPrice() {
		return currentPrice;
	}

	private String getNoSignString() {
		if (type == Negative)
			return NumberFormater.currencyFormat(getText().toString().replace("-", ""));
		else
			return NumberFormater.currencyFormat(getText().toString());
	}

	// Do not assume a software input method has to be key-based;
	// even if it is, it may use key presses in a different way than you expect,
	// so there is no way to reliably catch soft input key presses.

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_0 <= keyCode && keyCode <= KeyEvent.KEYCODE_9) {
			// text edit 的值大于等于最高限制，返回失败，并提示
			if (BasicArithmetic.compare(getNoSignString(), maxPrice) >= 0) {
				Toast.makeText(getContext(), R.string.exceed_limit, Toast.LENGTH_SHORT).show();
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_DEL) {
			if (type == Negative && getText().toString().equals("-"))
				return true;
		}
		currentPrice = NumberFormater.currencyFormat(getText().toString());
		return false;
	}
}
