package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.product.ProductAttribute;
import com.koolyun.mis.core.product.ProductManager;

public class AttributeAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private List<ProductAttribute> list = ProductManager.getAllProductAttribute();
	private List<ProductAttribute> checkedList = null;

	public AttributeAdapter(Context context) {
		listInflater = LayoutInflater.from(context);
	}

	public void setCheckedList(List<ProductAttribute> mcheckedList) {
		this.checkedList = mcheckedList;
		this.notifyDataSetChanged();
	}

	private void setItemChecked(ProductAttribute mProductAttribute, boolean selected) {
		if (checkedList == null)
			return;
		if (selected) {
			for (int i = 0; i < checkedList.size(); i++) {
				if (checkAttr(checkedList.get(i), mProductAttribute))
					return;
			}
			checkedList.add(mProductAttribute);
		} else {
			for (int i = 0; i < checkedList.size(); i++) {
				if (checkAttr(checkedList.get(i), mProductAttribute)) {
					checkedList.remove(i);
					break;
				}
			}
		}
	}

	private boolean getChecked(int attrid) {
		if (checkedList == null)
			return false;
		for (int i = 0; i < checkedList.size(); i++) {
			if (checkedList.get(i).getProductAttributeID() == attrid)
				return true;
		}
		return false;

	}

	private boolean checkAttr(ProductAttribute attr1, ProductAttribute attr2) {
		if (attr1.getProductAttributeID() == attr2.getProductAttributeID() && attr1.getName().equals(attr2.getName())
				&& attr1.getChoiceType() == attr2.getChoiceType()
				&& attr1.getDefaultChoice() == attr2.getDefaultChoice() && attr1.getEnable() == attr2.getEnable())
			return true;
		else
			return false;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
			convertView = listInflater.inflate(R.layout.productmanager_attr_layout, null);

			holder.showTextView = (TextView) convertView.findViewById(R.id.prdmg_attr_list_textview);
			holder.selectIamge = (ImageView) convertView.findViewById(R.id.icon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.showTextView.setText(list.get(position).getName());
		ProductAttribute pa=(ProductAttribute) getItem(position);
		boolean isSelected=false;
		for (ProductAttribute productAttribute : checkedList) {
			if(pa.getProductAttributeID()==productAttribute.getProductAttributeID()){
				isSelected=true;
			}
		}
		if(!isSelected){
			holder.selectIamge.setVisibility(View.INVISIBLE);
		}else{
			holder.selectIamge.setVisibility(View.VISIBLE);
		}
//		holder.check.setChecked(getChecked(list.get(position).getProductAttributeID()));
//		final int pos = position;
//		holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				setItemChecked(list.get(pos), isChecked);
//
//			}
//
//		});
		return convertView;

	}

	public class ViewHolder {
		public TextView showTextView;
		public ImageView  selectIamge;
//		public CheckBox check;
		

	}

	public void setonItemSelect(int position) {
		ProductAttribute pa=(ProductAttribute) getItem(position);
		boolean isSelected=false;
		for (ProductAttribute productAttribute : checkedList) {
			if(pa.getProductAttributeID()==productAttribute.getProductAttributeID()){
				isSelected=true;
			}
		}
		if(!isSelected){
			checkedList.add(pa);
		}else{
			checkedList.remove(pa);
		}
		this.notifyDataSetChanged();
	}

}
