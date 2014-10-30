package com.koolyun.mis.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.TotalMoneyObserver;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.widget.MoneyView;

public class SaleProductAdapter extends ImageCacheBaseAdapter implements TotalMoneyObserver {
	public static final int ITEM_NORMAL = 0;
	public static final int ITEM_DISABLE = 1;
	public static final int ITEM_CLEAR = 2;
	private ShoppingCart mShoppingCart;
	private int selectItem;

	// private View mClearView = null;

	Context context;

	public SaleProductAdapter(Context context, ShoppingCart mShoppingCart) {
		super(context);
		this.context = context;
		// mShoppingCart.setSaleProductAdapter(this);
		// 注册观察者
		mShoppingCart.registerObs(this);
		setShoppingCart(mShoppingCart);
		// mClearView = listInflater.inflate(R.layout.sale_product_clear, null);
	}

	public void setShoppingCart(ShoppingCart mShoppingCart) {
		this.mShoppingCart = mShoppingCart;
	}

	public void setSelectItem(int position) {
		this.selectItem = position;
	}

	@Override
	public int getCount() {
		return mShoppingCart.getCount();

	}

	public void removePosition(int pos) {
		if (pos >= 0 && pos < mShoppingCart.getCount()) {
			mShoppingCart.remove(pos);
			setSelectItem(mShoppingCart.getCount()-1);
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getItemViewType(int position) {
		int listcount = getCount();
		if (position < mShoppingCart.getCount())
			return ITEM_NORMAL;
		else if (mShoppingCart.hasUnNewAddedManuItem()
				&& ((listcount == 1 && position == 0) || (listcount > 2 && position == listcount - 2)))
			return ITEM_DISABLE;

		return ITEM_NORMAL;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public Object getItem(int position) {
		if (getItemViewType(position) == ITEM_NORMAL)
			return mShoppingCart.getOrderContentById(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// if (getItemViewType(position) == ITEM_CLEAR)
		// return mClearView;
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.sale_product_item_info, null);
			holder.showTextViewname = (TextView) convertView.findViewById(R.id.sale_product_item_name);
			holder.gridView_attribute = (GridView) convertView.findViewById(R.id.gv_sale_product_item_attribute);
			holder.viewNumberBg = (RelativeLayout) convertView.findViewById(R.id.product_item_left);
			holder.showTextViwnumber = (TextView) convertView.findViewById(R.id.sale_product_item_number);
			holder.showTextViewprice = (MoneyView) convertView.findViewById(R.id.sale_product_item_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mShoppingCart.getOrderContentById(position).getProductId() == ShoppingCart.MANUID
				&& mShoppingCart.getOrderContentById(position).getmOrderContentRemark() != null
				&& mShoppingCart.getOrderContentById(position).getmOrderContentRemark().getRemark() != null
				&& !mShoppingCart.getOrderContentById(position).getmOrderContentRemark().getRemark().isEmpty()) {
			holder.showTextViewname.setText(mShoppingCart.getOrderContentById(position).getmOrderContentRemark()
					.getRemark());
		} else {
			holder.showTextViewname.setText(mShoppingCart.getOrderContentById(position).getOrderContentName()
					.replace('\\', ' '));
		}
		holder.showTextViwnumber.setText("" + mShoppingCart.getOrderContentById(position).getCount());
		holder.showTextViewprice.setMoney(mShoppingCart.getOrderContentById(position).getItemAmount());

		OrderContentData ocd = (OrderContentData) getItem(position);
		if (ocd.getProduct().getProductID() != ShoppingCart.MANUID) {
			List<ProductSubAttribute> data = mShoppingCart.getOrderContentById(position).getProductSubAttrList();
			AttributeAdapter aa = new AttributeAdapter(context, data);
			holder.gridView_attribute.setPressed(false);
			holder.gridView_attribute.setAdapter(aa);
			holder.gridView_attribute.setEnabled(false);
		}

		if (position != selectItem) {
			holder.showTextViwnumber.setBackgroundResource(R.color.v2_txt_shopping_cat_item_attr_bg);
			holder.viewNumberBg.setBackgroundResource(R.color.transparent_background);
		} else {
			holder.showTextViwnumber.setBackgroundResource(R.color.transparent_background);
			holder.viewNumberBg.setBackgroundResource(R.color.txt_shopping_cat_item_select);
		}
		return convertView;
	}

	@Override
	public void totalMoneyChanged(String money) {
		this.notifyDataSetChanged();
	}

	public class ViewHolder {
		public RelativeLayout viewNumberBg;
		public TextView showTextViwnumber;
		public GridView gridView_attribute;
		public TextView showTextViewname;
		public MoneyView showTextViewprice;
		// public Button deleteButton;
	}

	class AttributeAdapter extends BaseAdapter {

		private List<ProductSubAttribute> mOnsaleList;
		private LayoutInflater listInflater;
		private Context mContext;

		public AttributeAdapter(Context context, List<ProductSubAttribute> data) {
			listInflater = LayoutInflater.from(context);
			mOnsaleList = data;
			mContext = context;
		}

		@Override
		public int getCount() {
			return mOnsaleList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = new TextView(context);
				holder.OnsaleName = (TextView) convertView;
				holder.OnsaleName.setTextColor(mContext.getResources().getColor(R.color.white));
				// RelativeLayout.LayoutParams params=(LayoutParams)
				// holder.OnsaleName.getLayoutParams();
				// params.
				holder.OnsaleName.setBackgroundResource(R.color.v2_txt_shopping_cat_item_attr_bg);
				holder.OnsaleName.setGravity(Gravity.CENTER);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.OnsaleName.setClickable(false);
			holder.OnsaleName.setEnabled(false);
			holder.OnsaleName.setText(mOnsaleList.get(position).getName());
			convertView.setEnabled(false);
			convertView.setFocusable(false);
			return convertView;
		}

		class ViewHolder {
			public TextView OnsaleName;
			// public TextView OnsalePrice;
		}
	}
}
