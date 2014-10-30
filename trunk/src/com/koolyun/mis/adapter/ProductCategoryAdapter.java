package com.koolyun.mis.adapter;

import java.util.List;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.ProductInfoAdapter.ViewHolder;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.product.ProductCategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 商品列表适配器
 */
public class ProductCategoryAdapter extends BaseAdapter {
	// 运行上下文
	private List<ProductCategory> mCategoryList; // 商品信息集合
	private LayoutInflater listInflater;

	private static int ALL_ONSALE_PIC = R.drawable.item_discount;
	private static int ALL_PRODUCT_PIC = R.drawable.item_all;
	private static int PRODUCT_CATE_PIC = R.drawable.item_list;

	public static final int PRODUCT_TYPE_ALL = 0;
	public static final int PRODUCT_TYPE_ONSALE = 1;
	public static final int PRODUCT_TYPE_CATE = 2;

	private Context mContext;

	public ProductCategoryAdapter(Context context, List<ProductCategory> categoryList) {
		mContext = context;
		this.mCategoryList = categoryList;
		listInflater = LayoutInflater.from(context);
	}

	public void udateList(List<ProductCategory> categoryList) {
		this.mCategoryList = categoryList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (mCategoryList != null && mCategoryList.size() > 0) ? mCategoryList.size() + 2 : 2;
	}

	@Override
	public Object getItem(int position) {
		if (position > 1) {
			return mCategoryList.get(position - 2);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItemViewType(position);
	}

	@Override
	public int getItemViewType(int position) {
		if (position > 1)
			return 2;
		else
			return position;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.product_detail_info, null);
			holder.name = (TextView) convertView.findViewById(R.id.prodect_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position > 1) {
			String name = mCategoryList.get(position - 2).getProductCategoryName();
			holder.name.setText(name.replace('\\', (char) 0x0a));
		} else if (position == 0) {
			holder.name.setText(mContext.getString(R.string.discountname));
		} else if (position == 1) {
			holder.name.setText(mContext.getString(R.string.allname));
		}
		
		return convertView;
	}

	public class ViewHolder {
		public TextView name;
	}

}
