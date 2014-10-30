package com.koolyun.mis.widget;

import com.koolyun.mis.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class MyImageButton extends ImageButton {
	Resources res = getResources();
	String productName = "出错";
	String manualInput = "出错";
	private String text = "手动输入"; // 要显示的文字
	Paint paint = new Paint();

	public MyImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		productName = res.getString(R.string.product_list);
		manualInput = res.getString(R.string.manual_input);
	}

	public void setText(int flag) {
		if (flag == 0) {
			this.text = productName;
		} else {
			this.text = manualInput;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		paint.setTextAlign(Paint.Align.CENTER);
		paint.setStrokeWidth(10);
		paint.setTextSize(25);
		paint.setColor(Color.WHITE);
		canvas.drawText(text, 200, 40, paint); // 绘制文字
	}

}
