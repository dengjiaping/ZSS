package com.koolyun.mis.util;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class FileManager {

	/**
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// opt.inSampleSize = 99;

		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);

		// TypedValue value = new TypedValue();
		// context.getResources().openRawResource(resId, value);
		// BitmapFactory.Options opts = new BitmapFactory.Options();
		// opts.inTargetDensity = value.density;
		// return BitmapFactory.decodeResource(context.getResources(), resId,
		// opts);
	}

	@SuppressWarnings("deprecation")
	public static BitmapDrawable readBitMapDrawable(Context context, int resId) {
		return new BitmapDrawable(readBitMap(context, resId));
	}

	// private Bitmap decodeResource(Resources resources, int id) {
	// TypedValue value = new TypedValue();
	// resources.openRawResource(id, value);
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inTargetDensity = value.density;
	// return BitmapFactory.decodeResource(resources, id, opts);
	// }
}
