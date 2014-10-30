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
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.fragment.DataManagerFragment;

public class ProductOnsaleManagerAdapter extends BaseAdapter {
	// 运行上下文
	private List<Onsale> monsale = null; // 商品信息集合
	private LayoutInflater listInflater;

	private DataManagerFragment mf = null;
	private Context mContext;

	public ProductOnsaleManagerAdapter(DataManagerFragment mf, List<Onsale> monsale) {
		mContext = mf.getActivity();
		this.mf = mf;
		this.monsale = monsale;
		listInflater = LayoutInflater.from(mContext);
	}

	public void updateOnsale(List<Onsale> monsale) {
		this.monsale = monsale;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		if (monsale == null)
			return 0;

		return monsale.size();
	}

	@Override
	public Object getItem(int position) {
		return monsale.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.allproduct_item_manager_info, null);
			holder.showAttrName = (TextView) convertView.findViewById(R.id.cate_name);
//			holder.showAttrPic = (ImageView) convertView.findViewById(R.id.cate_pic);
			holder.showEditButton = (Button) convertView.findViewById(R.id.cate_edit);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		holder.showAttrPic.setBackgroundResource(R.drawable.item_discount);
		holder.showAttrName.setText(monsale.get(position).getOnsaleName());

		final int p = position;
		holder.showEditButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mf.setEditProductOndale(monsale.get(p).getOnsaleID());
			}
		});

		convertView.requestFocus();
		return convertView;
	}

	public class ViewHolder {
//		public ImageView showAttrPic;
		public TextView showAttrName;
		public Button showEditButton;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}