package com.koolyun.mis.adapter;

import java.util.List;
import java.util.Map;

import com.koolyun.mis.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<Map<String, Object>> groups;
	private List<List<Map<String, Object>>> childs;
	LayoutInflater listInflater = null;

	// Resources res ;

	public ExpandableListAdapter(Context context, List<Map<String, Object>> groups,
			List<List<Map<String, Object>>> childs) {
		this.context = context;
		this.groups = groups;
		this.childs = childs;
		listInflater = LayoutInflater.from(this.context);
		// res = context.getResources();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) getChild(groupPosition, childPosition);
		String textshowMoney = (String) map.get("price");
		String textshowName = (String) map.get("kind");
		String textshowTime = (String) map.get("time");
		Integer imageview = (Integer) map.get("image");
		// imageview.setBounds(20, 10, 100, 90);
		// Bitmap bmp = BitmapFactory.decodeResource(res, imageview);
		// Bitmap newBmp = Bitmap.createScaledBitmap(bmp, 80, 80, filter)
		ViewHolderchild holder = null;
		if (convertView == null) {
			holder = new ViewHolderchild();
			convertView = listInflater.inflate(R.layout.child, null);
			holder.showchildimage = (ImageView) convertView.findViewById(R.id.leftbarpauseideal);
			holder.showchildMoney = (TextView) convertView.findViewById(R.id.leftbarmoney);
			// convertView.setTag(R.id.leftbarpauseideal,convertView.findViewById(R.id.leftbarpauseideal));
			// convertView.setTag(R.id.leftbarmoney,convertView.findViewById(R.id.leftbarmoney));
			// convertView.setTag(R.id.leftbarkind,convertView.findViewById(R.id.leftbarkind));
			// convertView.setTag(R.id.leftbartime,convertView.findViewById(R.id.leftbartime));
			holder.showchildName = (TextView) convertView.findViewById(R.id.leftbarkind);
			holder.showchildTime = (TextView) convertView.findViewById(R.id.leftbartime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderchild) convertView.getTag();
		}
		holder.showchildimage.setBackgroundResource(imageview);
		// ((ImageView)
		// convertView.getTag(R.id.leftbarpauseideal)).setBackground(imageview);
		// ((TextView)
		// convertView.getTag(R.id.leftbarmoney)).setText(textshowMoney);
		holder.showchildMoney.setText(textshowMoney);
		// ((TextView)
		// convertView.getTag(R.id.leftbarkind)).setText(textshowName);
		holder.showchildName.setText(textshowName);
		holder.showchildTime.setText(textshowTime);
		// ((TextView)
		// convertView.getTag(R.id.leftbarkind)).setCompoundDrawables(imageview,
		// null, null, null);
		// ((TextView)
		// convertView.getTag(R.id.leftbarkind)).setText(textshowTime);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childs.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// Log.i("grounp", "Expanda  == "+ groups.size());
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolderGrounp holder;
		if (convertView == null) {
			holder = new ViewHolderGrounp();
			convertView = listInflater.inflate(R.layout.groups, null);
			holder.showgrounptext = (TextView) convertView.findViewById(R.id.textGroup);
			holder.showgrounpimageview = (ImageView) convertView.findViewById(R.id.imageGroup);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderGrounp) convertView.getTag();
		}
		String textshow = (String) groups.get(groupPosition).get("groupText");
		int image = (Integer) groups.get(groupPosition).get("groupimage");
		holder.showgrounptext.setText(textshow);
		holder.showgrounpimageview.setBackgroundResource(image);

		return convertView;

	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	static class ViewHolderGrounp {
		public ImageView showgrounpimageview;
		public TextView showgrounptext;
	}

	static class ViewHolderchild {
		public ImageView showchildimage;
		public TextView showchildMoney;
		public TextView showchildName;
		public TextView showchildTime;
	}

}
