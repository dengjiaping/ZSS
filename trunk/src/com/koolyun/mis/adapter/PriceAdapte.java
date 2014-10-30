package com.koolyun.mis.adapter;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.fragment.ProductManagerAttrFragment;

public class PriceAdapte extends BaseAdapter {

	private List<ProductSubAttribute> list = null;
	private ProductManagerAttrFragment mProductManagerAtrrrFragment = null;

	public PriceAdapte(ProductManagerAttrFragment mProductManagerAtrrrFragment, List<ProductSubAttribute> list) {
		this.mProductManagerAtrrrFragment = mProductManagerAtrrrFragment;
		this.list = list;
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
			return null;
		else
			return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (list == null || position >= list.size())
			return -1;
		else
			return list.get(position).getProductSubAttributeId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CheckView holder = new CheckView(mProductManagerAtrrrFragment.getActivity());

		holder.showTextView.setText(list.get(position).getName());
		holder.showTextPrice.setText(list.get(position).getPriceAffect());
		final int pos = position;
		holder.deletBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				list.remove(pos);
				notifyDataSetChanged();
				if (holder.isChecked && pos == PriceAdapte.this.getCount() && pos != 0)
					mProductManagerAtrrrFragment.setListItemChecked(0, true);
				mProductManagerAtrrrFragment.resizeList();

				refresh(list);
			}

		});
		return holder;
	}

	public List<ProductSubAttribute> getList() {
		return list;
	}

	public void setList(List<ProductSubAttribute> list) {
		this.list = list;
	}

	public void refresh(List<ProductSubAttribute> list) {
		this.list = list;
		notifyDataSetChanged();
		if (this.getCount() == 0)
			mProductManagerAtrrrFragment.showListBelowLine(false);
		else
			mProductManagerAtrrrFragment.showListBelowLine(true);
	}

	public void refresh(List<ProductSubAttribute> list, int defaultChoice) {
		this.list = list;
		notifyDataSetChanged();
	}

	public class CheckView extends RelativeLayout implements Checkable {
		boolean isChecked = false;
		public TextView showTextView;
		public TextView showTextPrice;
		public ImageView mImageVIew;

		public Button deletBtn;

		public CheckView(Context context) {
			super(context);
			LayoutInflater.from(context).inflate(R.layout.productmanager_attr_price_item_layout, this, true);
			showTextView = (TextView) this.findViewById(R.id.prdmg_attr_price_list_textview);
			showTextPrice = (TextView) this.findViewById(R.id.prdmg_attr_price_list_price);
			deletBtn = (Button) this.findViewById(R.id.prdmg_att_price_listdeletbtn);
			mImageVIew = (ImageView) this.findViewById(R.id.image_bg_sub);
			AbsListView.LayoutParams clp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 80);
			this.setLayoutParams(clp);
			this.setMinimumHeight(80);
			this.setGravity(Gravity.CENTER);
			if (isChecked) {
				mImageVIew.setVisibility(View.VISIBLE);
			} else {
				mImageVIew.setVisibility(View.INVISIBLE);
			}

		}

		public CheckView(Context context, AttributeSet attrs) {
			super(context, attrs);
			LayoutInflater.from(context).inflate(R.layout.productmanager_attr_price_item_layout, this, true);
			showTextView = (TextView) this.findViewById(R.id.prdmg_attr_price_list_textview);
			showTextPrice = (TextView) this.findViewById(R.id.prdmg_attr_price_list_price);
			deletBtn = (Button) this.findViewById(R.id.prdmg_att_price_listdeletbtn);
			mImageVIew = (ImageView) this.findViewById(R.id.image_bg_sub);
			this.setMinimumHeight(80);
			this.setGravity(Gravity.CENTER);
			if (isChecked) {
				mImageVIew.setVisibility(View.VISIBLE);
			} else {
				mImageVIew.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public boolean isChecked() {
			return isChecked;
		}

		@Override
		public void setChecked(boolean checked) {
			isChecked = checked;
			if (isChecked) {
				mImageVIew.setVisibility(View.VISIBLE);
			} else {
				mImageVIew.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void toggle() {
			isChecked = !isChecked;
			if (isChecked) {
				mImageVIew.setVisibility(View.VISIBLE);
			} else {
				mImageVIew.setVisibility(View.INVISIBLE);
			}
		}

	}
}
