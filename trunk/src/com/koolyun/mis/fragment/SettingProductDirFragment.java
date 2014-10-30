package com.koolyun.mis.fragment;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.SettingProductDirAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewFlipper;

public class SettingProductDirFragment extends AbstractFragment implements OnClickListener{

	private ListView settingDonwLoadListView;
	private SettingProductDirAdapter settingProductAdapter;
	private ViewFlipper settingViewFlipper;
	private Button settingDownLoadBtn,settingDownLoadSureBtn,settingDownLoadCancelBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.setting_product_dir_layout, container, false);
		settingDonwLoadListView = (ListView) result.findViewById(R.id.settingDonwLoadListView);
		settingDownLoadBtn = (Button) result.findViewById(R.id.settingDownLoadBtn);
		settingDownLoadSureBtn = (Button) result.findViewById(R.id.settingDownLoadSureBtn);
		settingDownLoadCancelBtn = (Button) result.findViewById(R.id.settingDownLoadCancelBtn);
		settingViewFlipper = (ViewFlipper) result.findViewById(R.id.settingViewFlipper);
		settingDownLoadBtn.setOnClickListener(this);
		settingDownLoadSureBtn.setOnClickListener(this);
		settingDownLoadCancelBtn.setOnClickListener(this);
		settingProductAdapter = new SettingProductDirAdapter(getActivity());
		settingDonwLoadListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		settingDonwLoadListView.setAdapter(settingProductAdapter);
		settingDonwLoadListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		settingDonwLoadListView.setItemChecked(0, true);
		return result;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settingDownLoadBtn:
			if(settingViewFlipper.getDisplayedChild() != 1){
				settingViewFlipper.setDisplayedChild(1);
			}
			break;
		case R.id.settingDownLoadSureBtn:
			if(settingViewFlipper.getDisplayedChild() != 0){
				settingViewFlipper.setDisplayedChild(0);
			}
			break;
		case R.id.settingDownLoadCancelBtn:
			if(settingViewFlipper.getDisplayedChild() != 0){
				settingViewFlipper.setDisplayedChild(0);
			}
			break;
		}
	}

	
}
