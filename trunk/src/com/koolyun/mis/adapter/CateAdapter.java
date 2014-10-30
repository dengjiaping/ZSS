package com.koolyun.mis.adapter;

import java.util.List;

import com.koolyun.mis.R;
import com.koolyun.mis.core.product.ProductCategory;
import com.koolyun.mis.core.product.ProductManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CateAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private List<ProductCategory> list = ProductManager.getAllProductCategory();
	@SuppressWarnings("unused")
	private Context context = null;
	private ProductCategory oldProductCategory;
	public CateAdapter(Context context,ProductCategory oldProductCategory) {
		this.context = context;
		this.oldProductCategory=oldProductCategory;
		listInflater = LayoutInflater.from(context);
		list.add(0, new ProductCategory(0, context.getString(R.string.nocate), 1));
	}

	
	public void setOldProductCategory(ProductCategory oldProductCategory) {
		this.oldProductCategory = oldProductCategory;
	}
	


	public ProductCategory getOldProductCategory() {
		return oldProductCategory;
	}


	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if (list == null)
			return 0;
		return list.get(position);
	}

	public int getPositionById(int cateId) {
		if (list == null)
			return -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getProductCategoryId() == cateId)
				return i;
		}
		return -1;
	}

	@Override
	public long getItemId(int position) {
		if (list == null)
			return 0;
		return list.get(position).getProductCategoryId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.spinner_item_layout, null);
			holder.image=(ImageView) convertView.findViewById(R.id.icon);
			holder.showTextView = (TextView) convertView.findViewById(R.id.name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ProductCategory pc=(ProductCategory) getItem(position);
		if(pc.getProductCategoryId()==oldProductCategory.getProductCategoryId()){
			holder.image.setVisibility(View.VISIBLE);
		}else{
			holder.image.setVisibility(View.INVISIBLE);
		}
		holder.showTextView.setText(list.get(position).getProductCategoryName());
		return convertView;

	}

	public class ViewHolder {
		public TextView showTextView;
		public ImageView image;
	}

}
