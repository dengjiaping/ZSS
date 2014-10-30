package com.koolyun.mis.widget;

import com.koolyun.mis.R;
import com.koolyun.mis.core.order.StatisticsItem;
import com.koolyun.mis.util.NumberFormater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class StatistikListView extends LinearLayout {

	StatistikItemView mtotalAmount;
	StatistikItemView mcashAmount;
	StatistikItemView mcardAmount;
	// StatistikItemView motherAmount;
	StatistikItemView mdealCount;

	public StatistikListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.statistik_list, this, true);

		mtotalAmount = (StatistikItemView) this.findViewById(R.id.total_amount);
		mcashAmount = (StatistikItemView) this.findViewById(R.id.cash_amount);
		mcardAmount = (StatistikItemView) this.findViewById(R.id.card_amount);
		// motherAmount =
		// (StatistikItemView)this.findViewById(R.id.other_amount);
		mdealCount = (StatistikItemView) this.findViewById(R.id.total_count);

		mtotalAmount.setImageResource(R.drawable.sale);
		mcashAmount.setImageResource(R.drawable.cash);
		mcardAmount.setImageResource(R.drawable.card);
		// motherAmount.setImageResource(R.drawable.other);
		mdealCount.setImageResource(R.drawable.quantity);

		mtotalAmount.setText(R.string.consume_total);
		mcashAmount.setText(R.string.consume_cash);
		mcardAmount.setText(R.string.consume_card);
		// motherAmount.setText(R.string.consume_other);
		mdealCount.setText(R.string.consume_count);

	}

	public void setStatistics(StatisticsItem mStatisticsItem) {
		mtotalAmount.setMoney(NumberFormater.currencyFormat(mStatisticsItem.getTotalAmount()));
		mcashAmount.setMoney(NumberFormater.currencyFormat(mStatisticsItem.getCashAmount()));
		mcardAmount.setMoney(NumberFormater.currencyFormat(mStatisticsItem.getCardAmount()));
		// motherAmount.setMoney(NumberFormater.currencyFormat(mStatisticsItem.getOtherAmount()));
		mdealCount.setContent(String.valueOf(mStatisticsItem.getCount()));
	}

}
