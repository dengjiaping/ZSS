package com.koolyun.mis.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ImageCacheBaseAdapter extends BaseAdapter {

	protected LayoutInflater listInflater;
	static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	// Use 1/8th of the available memory for this memory cache.
	static final int cacheSize = maxMemory / 8;

	static protected LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
		@Override
		protected int sizeOf(String key, Bitmap bitmap) {
			// The cache size will be measured in kilobytes rather than
			// number of items.
			return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
		}
	};
	Context mContext = null;
	protected BitmapFactory.Options mBitmapOption;

	public ImageCacheBaseAdapter() {

	}

	public ImageCacheBaseAdapter(Context context) {
		mContext = context;
		listInflater = LayoutInflater.from(context);

		mBitmapOption = new BitmapFactory.Options();
		mBitmapOption.inSampleSize = 1;
	}

	protected void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	protected Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
