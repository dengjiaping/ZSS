package com.koolyun.mis.widget;

import com.koolyun.mis.BillingActivity;
import com.koolyun.mis.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MSignView extends View {

	public static Bitmap msnapBitmap = null;

	private Bitmap mBgBitmap;
	private Bitmap mBitmap;
	private Canvas mBgCanvas;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mPaint;

	// private static final int width = 1198;
	// private static final int height = 1198;

	private void initdraw() {
		mPath = new Path();
		mPaint = new Paint(Paint.DITHER_FLAG);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFF000000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(8);
	}

	public MSignView(Context context) {
		this(context, null);
	}

	public MSignView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MSignView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initdraw();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mBgBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mBgCanvas = new Canvas(mBgBitmap);
		NinePatchDrawable nd = (NinePatchDrawable) (getResources().getDrawable(R.drawable.pay_sign_bg));
		nd.setBounds(0, 0, w, h);
		nd.draw(mBgCanvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBgBitmap, 0, 0, mPaint);
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);
		canvas.drawPath(mPath, mPaint);
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			BillingActivity.getScrollView().requestDisallowInterceptTouchEvent(true);
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:

			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();

		case MotionEvent.ACTION_CANCEL:
			BillingActivity.getScrollView().requestDisallowInterceptTouchEvent(false);
			break;
		}
		return true;
	}

	public void clearSign() {
		mPath.reset();
		mBitmap.eraseColor(0x00000000);
		invalidate();
	}

	public void snapshotSign(int w, int h) {
		msnapBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		// snapBitmap.eraseColor(Color.TRANSPARENT);
		Canvas snapCanvas = new Canvas(msnapBitmap);
		snapCanvas.drawPath(mPath, mPaint);
		snapCanvas.save(Canvas.ALL_SAVE_FLAG);
		snapCanvas.restore();
	}
}
