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
import com.koolyun.mis.core.product.ProductAttribute;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.fragment.DataManagerFragment;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.MoneyView;

public class ProductAttrManagerAdapter extends BaseAdapter {
	// 运行上下文
	private List<ProductAttribute> mProdectAttr = null; // 商品信息集合
	private LayoutInflater listInflater;

	private DataManagerFragment mf = null;
	private Context mContext;

	public ProductAttrManagerAdapter(DataManagerFragment mf, List<ProductAttribute> mProdectAttr) {
		mContext = mf.getActivity();
		this.mf = mf;
		this.mProdectAttr = mProdectAttr;
		listInflater = LayoutInflater.from(mContext);
	}

	public void updateAttr(List<ProductAttribute> mProdectAttr) {
		this.mProdectAttr = mProdectAttr;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		if (mProdectAttr == null)
			return 0;

		return mProdectAttr.size();
	}

	@Override
	public Object getItem(int position) {
		return mProdectAttr.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.allproduct_item_manager_info, null);
			holder.showAttrName = (TextView) convertView.findViewById(R.id.cate_name);
//			holder.showAttrPic = (ImageView) convertView.findViewById(R.id.cate_pic);
			holder.showProductCount = (MoneyView) convertView.findViewById(R.id.product_count);
			holder.showEditButton = (Button) convertView.findViewById(R.id.cate_edit);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		holder.showAttrPic.setBackgroundResource(R.drawable.item_list);
		holder.showAttrName.setText(mProdectAttr.get(position).getName());
		int count = ProductManager.getProductCountByAttr(mProdectAttr.get(position).getProductAttributeID());
		holder.showProductCount.setText("" + count + Common.getString(R.string.term));

		final int p = position;
		holder.showEditButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mf.setEditProductAttr(mProdectAttr.get(p).getProductAttributeID());
			}
		});

		convertView.requestFocus();
		return convertView;
	}

	public class ViewHolder {
		public ImageView showAttrPic;
		public TextView showAttrName;
		public MoneyView showProductCount;
		public Button showEditButton;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}