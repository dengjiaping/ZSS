package com.koolyun.mis.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.koolyun.mis.R;

public class MyImageButton1 extends ImageButton {
	boolean flag = false;
	int whichFlag = 0;
	Resources res = getResources();
	String goodsname = "出错";
	String goodsattr = "出错";
	String goodsonsal = "error";
	int draw1 = 0;
	int draw2 = 0;
	int draw3 = 0;
	int draw4 = 0;
	MyImageButton1 button1 = null;
	MyImageButton1 button2 = null;
	//
	private String text = "手动输入"; // 要显示的文字
	// private int color; //文字的颜色
	Paint paint = new Paint();

	public MyImageButton1(Context context, AttributeSet attrs) {
		super(context, attrs);
		goodsname = res.getString(R.string.goodskind);
		goodsattr = res.getString(R.string.goodsattr);
		goodsonsal = res.getString(R.string.goodsonsale);
	}

	public void setText(int flag) {
		if (flag == 0) {
			this.text = goodsname;
		} else if (flag == 1) {
			this.text = goodsattr;
		} else {
			this.text = goodsonsal;
		}

	}

	public void setOtheButtonBack(MyImageButton1 button, int dr, MyImageButton1 button2, int dr2) {
		this.button1 = button;
		this.draw3 = dr;
		this.draw4 = dr2;
		this.button2 = button2;
	}

	public void setDrawable(int Donw, int On) {
		draw1 = Donw;
		draw2 = On;
	}

	public void setClearBackGround() {
		this.setBackgroundResource(draw1);
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean getFlag() {
		return this.flag;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		paint.setTextAlign(Paint.Align.CENTER);
		paint.setStrokeWidth(10);
		paint.setTextSize(25);
		paint.setColor(Color.WHITE);
		canvas.drawText(text, 150, 40, paint); // 绘制文字
	}
	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { switch
	 * (event.getAction()) { case MotionEvent.ACTION_DOWN:
	 * this.setBackgroundResource(draw1); if(!flag){ Log.i("AAAAAAAAA",
	 * "CCCCCCCCCCCCCCCCC"); button1.setBackgroundResource(draw3);
	 * button2.setBackgroundResource(draw4); button1.setFlag(false);
	 * button2.setFlag(false); // if(whichFlag == 0){ // //商品分类 // }else
	 * if(whichFlag == 1){ // //商品属性 // }else{ // //商品优惠 // } // } break; case
	 * MotionEvent.ACTION_UP: this.setBackgroundResource(draw2); this.flag =
	 * true; break; // case MotionEvent.ACTION_OUTSIDE: //
	 * this.setBackgroundResource(draw1); default: break; } return
	 * super.onTouchEvent(event); }
	 */

}
