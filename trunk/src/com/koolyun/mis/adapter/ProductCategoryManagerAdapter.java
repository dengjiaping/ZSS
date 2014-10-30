package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.fragment.DataManagerFragment;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.MoneyView;

	public class ProductCategoryManagerAdapter extends BaseAdapter {
		// 运行上下文
		private List<ProductCategory> mCategoryList; // 商品信息集合
		private LayoutInflater listInflater;

		private static int ALL_PRODUCT_PIC = R.drawable.item_all;
		private static int PRODUCT_CATE_PIC = R.drawable.item_list;

		public static final int PRODUCT_TYPE_ALL = 0;
		public static final int PRODUCT_TYPE_CATE = 1;

		private DataManagerFragment mf = null;
		private Context mContext;

		public ProductCategoryManagerAdapter(DataManagerFragment mf, List<ProductCategory> categoryList) {
			mContext = mf.getActivity();
			this.mf = mf;
			this.mCategoryList = categoryList;
			listInflater = LayoutInflater.from(mContext);
		}

		public void updateCategory(List<ProductCategory> categoryList) {
			this.mCategoryList = categoryList;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return (mCategoryList != null && mCategoryList.size() > 0) ? mCategoryList.size() + 1 : 1;
		}

		@Override
		public Object getItem(int position) {
			if (position > 0) {
				return mCategoryList.get(position - 1);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return getItemViewType(position);
		}

		@Override
		public int getItemViewType(int position) {
			if (position > 0)
				return 1;
			else
				return position;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = listInflater.inflate(R.layout.allproduct_item_manager_info, null);
				holder.showCateName = (TextView) convertView.findViewById(R.id.cate_name);
//				holder.showCatePic = (ImageView) convertView.findViewById(R.id.cate_pic);
				holder.showProductCount = (MoneyView) convertView.findViewById(R.id.product_count);
				holder.showEditButton = (Button) convertView.findViewById(R.id.cate_edit);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position > 0) {
//				holder.showCatePic.setBackgroundResource(PRODUCT_CATE_PIC);
				holder.showCateName.setText(mCategoryList.get(position - 1).getProductCategoryName());
				int count = ProductManager.getProductCountByCategory(mCategoryList.get(position - 1)
						.getProductCategoryId());
				holder.showProductCount.setText("" + count + Common.getString(R.string.term));
				// Log.d("======>","---count---"+count);
			} else if (position == 0) {
//				holder.showCatePic.setBackgroundResource(ALL_PRODUCT_PIC);
				holder.showCateName.setText(mContext.getString(R.string.allname));
				holder.showEditButton.setVisibility(View.INVISIBLE);
				int count = ProductManager.getProductCountByCategory(0);
				holder.showProductCount.setText("" + count + Common.getString(R.string.term));
			}
			final int p = position - 1;
			holder.showEditButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					mf.setEditProductCate(mCategoryList.get(p).getProductCategoryId());
				}
			});

			convertView.requestFocus();
			return convertView;
		}

		public class ViewHolder {
			public TextView showCateName;
//			public ImageView showCatePic;
			public MoneyView showProductCount;
			public Button showEditButton;
		}

	}
