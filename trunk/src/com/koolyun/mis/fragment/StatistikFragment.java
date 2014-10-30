package com.koolyun.mis.fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.MyViewPagerAdapter;
import com.koolyun.mis.adapter.ProductStatisticAdapter;
import com.koolyun.mis.core.order.StatisticsItem;
import com.koolyun.mis.core.order.StatisticsManager;
import com.koolyun.mis.core.product.SaleProductInfo;
import com.koolyun.mis.core.store.StoreManager;
import com.koolyun.mis.sqlite.MySharedPreferencesEdit;
import com.koolyun.mis.txt.ItemsTxt;
import com.koolyun.mis.txt.SalesTxt;
import com.koolyun.mis.ui.widget.date.time.ScreenInfo;
import com.koolyun.mis.ui.widget.date.time.WheelMain;
import com.koolyun.mis.util.CalendarUtil;
import com.koolyun.mis.util.CloudPosApp;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.util.Printer.PrinterManager;
import com.koolyun.mis.widget.AnyWhereDialog;
import com.koolyun.mis.widget.StatistikListView;

public class StatistikFragment extends LeftBarBaseFragment /*implements OnCheckedChangeListener*/ {
	View layout = null;
	Button leftbar_detail_back_to1_btn = null;
	Button printerButton = null;
	Button startButton = null;
	Button endButton = null;
	TextView mStartText = null;
	TextView mEndText = null;
	
	private ListView productStatisticListView;
	CalendarUtil tt = new CalendarUtil();
	private ViewPager viewPager;//页卡内容
	private ImageView imageView;// 动画图片
	private Button button1,button2,saveBtn;
	private List<View> views;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private MySharedPreferencesEdit mySharedPreferencesEdit;
	private int currentPage = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private View view1,view2;//各个页卡
	StatistikListView mStatistikListView = null;
	ProductStatisticAdapter productStatisticAdapter = null;
	AnyWhereDialog mDialog = null;
	private WheelMain wheelMain;
	private String path;

	String startDate = "";
	String endDate = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mySharedPreferencesEdit = MySharedPreferencesEdit.getInstancePublic(getActivity());
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.statistik_normal, container, false);
		leftbar_detail_back_to1_btn = (Button) layout.findViewById(R.id.leftbar_detail_back_to1);
		printerButton = (Button) layout.findViewById(R.id.statistik_printer);
		printerButton.setOnClickListener(this);
		startButton = (Button) layout.findViewById(R.id.start_date);
		endButton = (Button) layout.findViewById(R.id.end_date);
		startButton.setOnClickListener(this);
		endButton.setOnClickListener(this);
		saveBtn = (Button) layout.findViewById(R.id.statistik_save);
		saveBtn.setOnClickListener(this);
		//mStartText = (TextView)layout.findViewById(R.id.start_text);
		//mEndText = (TextView)layout.findViewById(R.id.end_text);
		
		leftbar_detail_back_to1_btn.setOnClickListener(this);

		startDate = getResources().getString(R.string.begin_time)+" : ";
		endDate = getResources().getString(R.string.end_time) + " : ";
		String startTime = Common.getCurrentDateStartTimeString(Common.DATETIMEFORMAT, getActivity());
		String endTime = Common.getCurrentDateEndTimeString(Common.DATETIMEFORMAT, getActivity());
		startButton.setText(startDate + startTime);
		endButton.setText(endDate + endTime);
		
		button1 = (Button) layout.findViewById(R.id.statistic_total_title);
		button2 = (Button) layout.findViewById(R.id.statistic_product_title);
		String []array = getActivity().getResources().getStringArray(R.array.statistic_title_array);
		button1.setText(array[0]);
		button2.setText(array[1]);
		InitImageView(layout);
		button1.setOnClickListener(new MyOnClickListener(0));
		button2.setOnClickListener(new MyOnClickListener(1));
		viewPager=(ViewPager) layout.findViewById(R.id.vPager);
		views=new ArrayList<View>();
		view1=inflater.inflate(R.layout.statistic_total, null);
		view2=inflater.inflate(R.layout.statistic_product, null);
		views.add(view1);
		views.add(view2);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mStatistikListView = (StatistikListView)view1.findViewById(R.id.statistik_view);
		
		productStatisticListView = (ListView) view2.findViewById(R.id.productStatisticListView);
		// 初始化商品统计
		productStatisticAdapter = new ProductStatisticAdapter(getActivity());
		productStatisticListView.setAdapter(productStatisticAdapter);
		return layout;
	}
	
	@Override
	public void onResume() {
		List<SaleProductInfo> mplistInfo = StatisticsManager.getSaleProductInfo(
				startButton.getText().toString().split(" : ")[1].trim(),
				endButton.getText().toString().split(" : ")[1].trim());
		if(productStatisticAdapter != null)
			productStatisticAdapter.updateList(mplistInfo);
		String startTime = Common.getCurrentDateStartTimeString(Common.DATETIMEFORMAT, getActivity());
		String endTime = Common.getCurrentDateEndTimeString(Common.DATETIMEFORMAT, getActivity());
		mStatistikListView.setStatistics(StatisticsManager.getStatisticsItem(startTime, endTime));
		super.onResume();
	}

	private void InitImageView(View v) {
		imageView= (ImageView) v.findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.slide_indicator).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = 850;//dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	private class MyOnClickListener implements OnClickListener{
        private int index=0;
        public MyOnClickListener(int i){
        	index=i;
        }
		public void onClick(View v) {
			viewPager.setCurrentItem(index);			
		}
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener{
    	int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0: // page 0
				button1.setTextColor(getActivity().getResources().getColor(android.R.color.black));
				button2.setTextColor(getActivity().getResources().getColor(android.R.color.darker_gray));
				button1.setBackgroundResource(R.drawable.statistics_selector2);
				button2.setBackgroundResource(R.drawable.statistics_selector);
				break;
			case 1: // page 1
				button2.setTextColor(getActivity().getResources().getColor(android.R.color.black));
				button1.setTextColor(getActivity().getResources().getColor(android.R.color.darker_gray));
				button2.setBackgroundResource(R.drawable.statistics_selector2);
				button1.setBackgroundResource(R.drawable.statistics_selector);
				break;
			}
			Animation animation = new TranslateAnimation(one*currentPage, one*arg0, 0, 0);
			currentPage = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
		}
    }

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.leftbar_detail_back_to1:
			mLeftBarFragment.backBtnClicked();
			break;
		case R.id.statistik_printer:
//			mDialog = new AnyWhereDialog(getActivity(), 540, 280, 250,-20,
//					R.layout.print_dialog_layout, R.style.Theme_dialog1, Gravity.LEFT
//					| Gravity.TOP, true);
//			
//		    Button mPrinterBtn = (Button) mDialog.findViewById(R.id.printer_print_icon_btn);
//		    Button mSaveBtn = (Button) mDialog.findViewById(R.id.printer_save_icon_btn);
//		    mPrinterBtn.setOnClickListener(this);
//			mSaveBtn.setOnClickListener(this);
//			mDialog.show();
			MyLog.i("Statisticfragment", "execute print btn");
			if(currentPage == 0) {
				PrinterManager.getInstance().printStatistik(StatisticsManager.getStatisticsItem(
						startButton.getText().toString().split(" : ")[1], endButton.getText().toString().split(" : ")[1]),
						startButton.getText().toString().split(" : ")[1], endButton.getText().toString().split(" : ")[1]);
			} else if(currentPage == 1) {
				List<SaleProductInfo> mplistInfo = StatisticsManager.getSaleProductInfo(
						startButton.getText().toString().split(" : ")[1].trim(),
						endButton.getText().toString().split(" : ")[1].trim());
				PrinterManager.getInstance().printProductCount(mplistInfo,
						startButton.getText().toString().split(" : ")[1], endButton.getText().toString().split(" : ")[1]);
			}
			break;
		case R.id.start_date:
			startDateChanged();
			break;
		case R.id.end_date:
			endDateChanged();
			break;
		case R.id.printer_print_icon_btn:
			MyLog.i("Statisticfragment", "execute print btn");
			if(currentPage == 0) {
				PrinterManager.getInstance().printStatistik(StatisticsManager.getStatisticsItem(
						startButton.getText().toString().split(" : ")[1], endButton.getText().toString().split(" : ")[1]),
						startButton.getText().toString().split(" : ")[1], endButton.getText().toString().split(" : ")[1]);
			} else if(currentPage == 1) {
				List<SaleProductInfo> mplistInfo = StatisticsManager.getSaleProductInfo(
						startButton.getText().toString().split(" : ")[1].trim(),
						endButton.getText().toString().split(" : ")[1].trim());
				PrinterManager.getInstance().printProductCount(mplistInfo,
						startButton.getText().toString().split(" : ")[1], endButton.getText().toString().split(" : ")[1]);
			}
			break;
		case R.id.printer_save_icon_btn:
			MyLog.i("Statisticfragment", "execute save btn");
			try {
				saveStatistics();
			} catch (IOException e) {
				//add failed prompt
				e.printStackTrace();
				Toast.makeText(CloudPosApp.getInstance(), 
						CloudPosApp.getInstance().getString(R.string.savedocfail_prompt),
						Toast.LENGTH_SHORT).show();
				break;
			}
			//add success prompt
			MyLog.i("Statisticfragment", "success");
			Toast.makeText(CloudPosApp.getInstance(), 
					CloudPosApp.getInstance().getString(R.string.savedocsuccess_prompt)+ path,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.statistik_save:
			MyLog.i("Statisticfragment", "execute save btn");
			try {
				saveStatistics();
			} catch (IOException e) {
				//add failed prompt
				e.printStackTrace();
				Toast.makeText(CloudPosApp.getInstance(), 
						CloudPosApp.getInstance().getString(R.string.savedocfail_prompt),
						Toast.LENGTH_SHORT).show();
				break;
			}
			//add success prompt
			MyLog.i("Statisticfragment", "success");
			Toast.makeText(CloudPosApp.getInstance(), 
					CloudPosApp.getInstance().getString(R.string.savedocsuccess_prompt)+ path,
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	/**
	 * 选择开始时间
	 */
	private void startDateChanged()
	{
		showSelectDateTimeDialog(true);
	}
	
	private void showSelectDateTimeDialog(final boolean isStartTime) {
		LayoutInflater inflater=LayoutInflater.from(getActivity());
		final View timepickerview=inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(getActivity());
		wheelMain = new WheelMain(timepickerview, true);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(Common.DATETIMEFORMAT, Locale.getDefault());
		if(!isStartTime) { // end time
			Date date = null;
			try {
				date = sdf.parse(endButton.getText().toString().split(" : ")[1].trim());
				calendar.setTimeInMillis(date.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Date date = null;
			try {
				date = sdf.parse(startButton.getText().toString().split(" : ")[1].trim());
				calendar.setTimeInMillis(date.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		wheelMain.initDateTimePicker(year,month,day,hour, minute);
		
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(timepickerview);
		final TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
		Button sureButton = (Button) dialog.findViewById(R.id.sureButton);
		Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
		
		sureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				String selectTime = wheelMain.getTime();
				long selectLong = Common.getTimeMillis(selectTime, "yyyy-MM-dd HH:mm");
				Calendar selectCalendar = Calendar.getInstance();
				selectCalendar.setTimeInMillis(selectLong);
				if(isStartTime) { // start time
					startButton.setText(startDate+selectTime);
					mStatistikListView.setStatistics(StatisticsManager.getStatisticsItem(
							startButton.getText().toString().split(" : ")[1].trim(), endButton.getText().toString().split(" : ")[1].trim()));
//					if(selectCheckIndex == 0) {
						mySharedPreferencesEdit.setStartHour(selectCalendar.get(Calendar.HOUR_OF_DAY));
						mySharedPreferencesEdit.setStartMinute(selectCalendar.get(Calendar.MINUTE));
						mySharedPreferencesEdit.setStartToEndDays(calculateDays());
//					}
				} else { // end time
					endButton.setText(endDate+selectTime);
					mStatistikListView.setStatistics(StatisticsManager.getStatisticsItem(
							startButton.getText().toString().split(" : ")[1].trim(), endButton.getText().toString().split(" : ")[1].trim()));
//					if(selectCheckIndex == 0) {
						mySharedPreferencesEdit.setEndHour(selectCalendar.get(Calendar.HOUR_OF_DAY));
						mySharedPreferencesEdit.setEndMinute(selectCalendar.get(Calendar.MINUTE));
						mySharedPreferencesEdit.setStartToEndDays(calculateDays());
//					}
				}
				
				List<SaleProductInfo> mplistInfo = StatisticsManager.getSaleProductInfo(
						startButton.getText().toString().split(" : ")[1].trim(),
						endButton.getText().toString().split(" : ")[1].trim());
				productStatisticAdapter.updateList(mplistInfo);
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				RelativeLayout.LayoutParams tRParams =
						(RelativeLayout.LayoutParams) dialog_title.getLayoutParams();
				tRParams.width = timepickerview.getWidth();
				dialog_title.setLayoutParams(tRParams);
			}
		}, 100);
		
		dialog.show();
	}
	
	private int calculateDays() {
	    try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			long to = df.parse(endButton.getText().toString().split(" : ")[1]).getTime();
			long from = df.parse(startButton.getText().toString().split(" : ")[1]).getTime();
			return (int) ((to - from) / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 选择结束时间
	 */
	private void endDateChanged()
	{
		showSelectDateTimeDialog(false);
	}
	
	private void saveStatistics() throws IOException {

		String startdate = startButton.getText().toString().split(" : ")[1];
		String enddate = endButton.getText().toString().split(" : ")[1];

		if(currentPage == 0)
		{
			path = "/mnt/sdcard/DCIM/sales.txt";
			SalesTxt salestxt = new SalesTxt(path);
			salestxt.setStoreName(StoreManager.getStoreName());

			salestxt.setFromDate(startdate);
			salestxt.setToDate(enddate);
			StatisticsItem item = StatisticsManager.getStatisticsItem(startdate, enddate);
			salestxt.setTotalSales(item.getTotalAmount());
			salestxt.setCash(item.getCashAmount());
			salestxt.setCreditCard(item.getCardAmount());
			salestxt.setTransactions(String.valueOf(item.getCount()));
			salestxt.RecordToSalesTxt();
		}
		else if(currentPage == 1)
		{
			List<SaleProductInfo> mplistInfo = StatisticsManager.getSaleProductInfo(
					startdate.trim(),
					enddate.trim());
			path = "/mnt/sdcard/DCIM/items.txt";
			ItemsTxt itemstxt = new ItemsTxt(path);
			itemstxt.setStoreName(StoreManager.getStore().getNickName());
			itemstxt.setFromDate(startdate);
			itemstxt.setToDate(enddate);
			itemstxt.RecordToItemsTxt(mplistInfo);
		}
	}

}
