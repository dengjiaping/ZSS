package com.koolyun.mis.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.koolyun.mis.R;

public class SignView extends SurfaceView implements Callback, Runnable {

	public static final int TIME_IN_FRAME = 40;
	public static Bitmap msnapBitmap = null;

	public static Paint mPaint = null;
	Paint mTextPaint = null;
	SurfaceHolder mSurfaceHolder = null;

	boolean mRunning = false;

	Canvas mCanvas = null;

	Bitmap mBitmap = null;

	public boolean mIsRunning = false;

	private Path mPath = new Path();

	private float mposX, mposY;
	private Boolean mpoint = true;

	private void initobject() {

		this.setFocusable(true);

		this.setFocusableInTouchMode(true);
	}

	private void initdraw() {

		mSurfaceHolder = this.getHolder();

		mSurfaceHolder.addCallback(this);
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pay_sign_bg);

		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);

		mPaint.setAntiAlias(true);
		mPaint.setDither(true);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setFilterBitmap(true);

		mPaint.setStrokeCap(Paint.Cap.ROUND);

		mPaint.setStrokeWidth(10);
	}

	public SignView(Context context) {
		this(context, null);
	}

	public SignView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SignView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initobject();
		initdraw();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/** 鎷垮埌瑙︽懜鐨勭姸鎬� **/
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		switch (action) {

		case MotionEvent.ACTION_DOWN:

			mPath.moveTo(x, y);
			mpoint = true;
			break;

		case MotionEvent.ACTION_MOVE:

			touchMove(x, y);
			break;

		case MotionEvent.ACTION_UP:

			// mPath.reset();
			if (true == mpoint)
				mPath.quadTo(mposX, mposY, x + 1, y + 1);
			break;
		}

		mposX = x;
		mposY = y;
		return true;
	}

	public void touchMove(float x, float y) {
		float dx = Math.abs(x - mposX);
		float dy = Math.abs(y - mposY);

		if (dx >= 4.0f || dy >= 4.0f) {
			mpoint = false;
			mPath.quadTo(mposX, mposY, (x + mposX) / 2, (y + mposY) / 2);
			mposX = x;
			mposY = y;
		}
	}

	private void Draw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);
		canvas.drawPath(mPath, mPaint);
	}

	private void DrawPath(Canvas canvas) {
		canvas.drawPath(mPath, mPaint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		mIsRunning = true;
		new Thread(this).start();

		setBackgroundColor(Color.TRANSPARENT);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mIsRunning = false;
	}

	@Override
	public void run() {
		while (mIsRunning) {

			long startTime = System.currentTimeMillis();

			synchronized (mSurfaceHolder) {
				mCanvas = mSurfaceHolder.lockCanvas();
				Draw(mCanvas);
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			}

			long endTime = System.currentTimeMillis();

			int diffTime = (int) (endTime - startTime);

			while (diffTime <= TIME_IN_FRAME) {

				Thread.yield();
				diffTime = (int) (System.currentTimeMillis() - startTime);
			}

		}

	}

	public void clearSign() {
		mPath.reset();
	}

	public void snapshotSign(int w, int h) {
		msnapBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		// snapBitmap.eraseColor(Color.TRANSPARENT);
		Canvas snapCanvas = new Canvas(msnapBitmap);
		DrawPath(snapCanvas);
		snapCanvas.save(Canvas.ALL_SAVE_FLAG);
		snapCanvas.restore();
	}
}
