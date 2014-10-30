package com.koolyun.mis.widget;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.koolyun.mis.R;
import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.util.NumberFormater;
import com.koolyun.mis.util.pay.PayByCash;
import com.koolyun.mis.util.pay.PayManager;

public class QuickMoneySelector extends ButtonGroupBox implements OnClickListener {
	public QuickMoneySelector(Context context) {
		super(context);
	}

	public QuickMoneySelector(Context context, List<Double> moneyList) {
		super(context, GroupType.SINGLECLICK, moneyList.size());
		this.setHeight(85);
		if (count > 0) {
			LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(808 / count, LayoutParams.MATCH_PARENT, 1);
			for (int i = 0; i < count; i++) {
				mButtonList.get(i).setText(NumberFormater.currencyFormat(moneyList.get(i)));
				mButtonList.get(i).setBackgroundResource(R.drawable.button_xml);
				mButtonList.get(i).setOnClickListener(this);
				mButtonList.get(i).setLayoutParams(clp);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (JavaUtil.isFastDoubleClick())
			return;
		Button btn = (Button) v;
		PayManager.getInstance().addNewPayment(new PayByCash(btn.getText().toString()));
	}

	public void setClickable(boolean flag) {
		for (int i = 0; i < count; i++) {
			mButtonList.get(i).setEnabled(flag);
		}
	}

}
