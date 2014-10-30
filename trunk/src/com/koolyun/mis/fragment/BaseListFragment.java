package com.koolyun.mis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koolyun.mis.R;

/*
 * BaseListFragment是基本listFragment
 * 
 */
public class BaseListFragment extends AbstractListFragment {

	/*
	 * protected OnItemClickedListener mCallback;
	 * 
	 * public enum ListFragmentType { CATEGORY, GOODS, ONSALE }
	 * 
	 * // Activity必须实现这个接口 public interface OnItemClickedListener { public void
	 * onItemClicked(ListFragmentType type, int position); }
	 * 
	 * @Override public void onAttach(Activity activity) {
	 * super.onAttach(activity);
	 * 
	 * // 确认容器activity已经实现接口，如果没有，抛出异常 try { mCallback = (OnItemClickedListener)
	 * activity; } catch (ClassCastException e) { throw new
	 * ClassCastException(activity.toString() +
	 * " must implement OnItemClickedListener"); } }
	 */

	// 设置ListFragment的整体布局Screen Layout

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listfragment_layout, container, false);
	}
}