package com.koolyun.mis.adapter;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.TotalMoneyObserver;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.product.Product;
import com.koolyun.mis.core.product.ProductPinyin;
import com.koolyun.mis.util.Common;

@SuppressLint("NewApi")
public class ProductInfoAdapter extends ImageCacheBaseAdapter implements TotalMoneyObserver {
	// 运行上下文
	private List<Product> mPList; // 商品信息集合
	private HashMap<String, Integer> pinyinHash = new HashMap<String, Integer>();
	private List<ProductPinyin> pinyinList = null;
	private ShoppingCart mShoppingCart = null;
	private Context mContext;

	public ProductInfoAdapter(Context context, List<Product> mList, List<ProductPinyin> pinyinList) {
		super(context);
		mContext = context;
		this.mPList = mList;
		this.pinyinList = pinyinList;
		InitPinYinList();
		mShoppingCart = DealModel.getInstance().getShoppingCart();
		mShoppingCart.registerObs(this);

	}

	public void updateList(List<Product> mList, List<ProductPinyin> pinyinList) {
		this.mPList = mList;
		this.pinyinList = pinyinList;
		InitPinYinList();
		this.notifyDataSetChanged();
	}

	@SuppressLint("DefaultLocale")
	public int GetPositionByPinyin(String str) {
		str = str.toUpperCase();
		int pos = -1;
		if (pinyinHash.get(str) != null)
			pos = pinyinHash.get(str);
		return pos;
	}

	private void InitPinYinList() {
		pinyinHash.clear();
		for (int i = 0; i < pinyinList.size(); i++) {
			String str = pinyinList.get(i).getFirstChar();
			if (pinyinHash.get(str) == null) {
				pinyinHash.put(str, i);
			}
		}
		return;
	}

	@Override
	public int getCount() {
		return mPList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mPList.get(position).getProductID();
	}

	@Override
	public int getItemViewType(int position) {
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.product_detail_info, null);
			holder.productName = (TextView) convertView.findViewById(R.id.prodect_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String name = mPList.get(position).getName();
		holder.productName.setText(name.replace('\\', (char) 0x0a));
//		holder.productName.setTextColor(mContext.getResources().getColor(R.color.v2_product_display_normal));
//		holder.productName.setBackgroundResource(R.color.v2_gridview_item_bg_nor);
//		int id = mPList.get(position).getProductID();
//		List<OrderContentData> orders = mShoppingCart.getOrderContentList();
//		if (orders.size() > 0) {
//			for (OrderContentData ocd : orders) {
//				if (ocd.getProductId() == id) {
//					holder.productName.setTextColor(mContext.getResources().getColor(R.color.white));
//					holder.productName.setBackgroundResource(R.color.v2_gridview_item_bg_sel);
//				}
//			}
//		}

//		setImageView(holder, position);
		return convertView;
	}

	protected void setImageView(ViewHolder viewHolder, int position) {
		String imageName = mPList.get(position).getProductPhoto();

		if (Common.checkValidImageName(imageName)) {
			Bitmap bitmap = getBitmapFromMemCache(imageName);
			if (bitmap == null) {
				Bitmap tmp = BitmapFactory.decodeFile(Common.getRealImagePath(imageName), mBitmapOption);
				if (tmp != null) {
					bitmap = Common.zoomBitmap(tmp, 100, 100);
					addBitmapToMemoryCache(imageName, bitmap);
				}
			}
			if (bitmap != null) {
				// viewHolder.productImage.setImageBitmap(bitmap);
			} else {
				// viewHolder.productImage.setImageResource(R.drawable.default_product);
			}
		} else {
			// viewHolder.productImage.setImageResource(R.drawable.default_product);
		}
	}

	class ViewHolder {
		// public ImageView productImage;
		public TextView productName;
		// public MoneyView productPrice;
	}

	@Override
	public void totalMoneyChanged(String money) {
		this.notifyDataSetChanged();
	}

}
