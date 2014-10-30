package com.koolyun.mis.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.RefundActivity;
import com.koolyun.mis.SaleActivity;
import com.koolyun.mis.adapter.OrderDetailAdapter;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.order.AnalizeOrderProcess;
import com.koolyun.mis.core.order.OrderData;
import com.koolyun.mis.core.order.OrderManager;
import com.koolyun.mis.core.order.OrderProcess;
import com.koolyun.mis.core.order.OrderData.OrderStatus;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.JavaUtil;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.util.Printer.OrderPrint;
import com.koolyun.mis.util.Printer.PrinterManager;
import com.koolyun.mis.util.pay.PayBase;
import com.koolyun.mis.widget.AnyWhereDialog;
import com.koolyun.mis.widget.MoneyView;

/**
 * 销售记录-全部记录-某一条订单的详情fragment
 */
public class LeftBarProductItemInfoFragment extends LeftBarBaseFragment implements OnClickListener {
	private View leftBarProductItemLayout = null;
	private Button back_button, LeftButton, RightButton;
	private OrderData mOrderData = null;
	private ListView listView = null;
	private OrderDetailAdapter mOrderDetailAdapter = null;
	private AnyWhereDialog mDeleteHangupDialog = null;
	private AnyWhereDialog mContCoverDialog = null;
	private MoneyView mMoneyView = null;
	private TextView mDateView = null;
	private TextView mPayMethod = null;
	private TextView mBillNo = null;
	private TextView accountName = null;
	private TextView leftbarTitleText = null;
	private Button orderPrint = null;
	private Button leftbar_checkout_deal_btn;
	private int orderid;
	private int level;// level {1:挂单和近期记录 2:全部记录 }
	private static final int PRESTORETYPE0 = 0;
	private static final int PRESTORETYPE1 = 1;
	private static final int PRESTORETYPE2 = 2;
	private int restoreType = PRESTORETYPE0;

	public static LeftBarProductItemInfoFragment newInstance(int orderid, int type, int level) {
		LeftBarProductItemInfoFragment frag = new LeftBarProductItemInfoFragment();
		Bundle args = new Bundle();
		args.putInt("orderid", orderid);
		args.putInt("type", type);
		args.putInt("level", level);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orderid = getArguments().getInt("orderid");
		getArguments().getInt("type");
		level = getArguments().getInt("level");
		// mOrderData = OrderManager.getOrderDataById(orderid);
		updateData();
	}

	private void updateData() {
		mOrderData = OrderManager.getOrderDataById(orderid);
		if (mOrderDetailAdapter != null) {
			mOrderDetailAdapter.setListData(mOrderData.getOrderContentList());
			mOrderDetailAdapter.notifyDataSetChanged();
			setListViewHeightBasedOnChildren(listView);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		leftBarProductItemLayout = inflater.inflate(R.layout.leftbarproductiteminfo1, container, false);
		leftbar_checkout_deal_btn = (Button) leftBarProductItemLayout.findViewById(R.id.leftbar_checkout_deal);
		orderPrint = (Button) leftBarProductItemLayout.findViewById(R.id.order_printer);
		mMoneyView = (MoneyView) leftBarProductItemLayout.findViewById(R.id.leftbarproductprice);
		mDateView = (TextView) leftBarProductItemLayout.findViewById(R.id.leftbar_deal_deal_time);
		mPayMethod = (TextView) leftBarProductItemLayout.findViewById(R.id.leftbar_pay_method);
		mBillNo = (TextView) leftBarProductItemLayout.findViewById(R.id.leftbar_deal_deal_number);
		accountName = (TextView) leftBarProductItemLayout.findViewById(R.id.leftbar_deal_user_name);
		leftbarTitleText = (TextView) leftBarProductItemLayout.findViewById(R.id.leftbarTitle);
		back_button = (Button) leftBarProductItemLayout.findViewById(R.id.leftbar_detail_back);
		LeftButton = (Button) leftBarProductItemLayout.findViewById(R.id.left_button);
		RightButton = (Button) leftBarProductItemLayout.findViewById(R.id.right_button);
		RightButton.setOnClickListener(this);

		listView = (ListView) leftBarProductItemLayout.findViewById(R.id.leftbarlistviewdeal);

		mOrderDetailAdapter = new OrderDetailAdapter(getActivity(), mOrderData.getOrderContentList());
		listView.setAdapter(mOrderDetailAdapter);
		mOrderDetailAdapter.setListData(mOrderData.getOrderContentList());
		mOrderDetailAdapter.notifyDataSetChanged();
		setListViewHeightBasedOnChildren(listView);

		back_button.setOnClickListener(this);
		LeftButton.setOnClickListener(this);
		orderPrint.setOnClickListener(this);

		leftbar_checkout_deal_btn.setOnClickListener(this);
		InitView();

		return leftBarProductItemLayout;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setListViewHeightBasedOnChildren(listView);
	}

	private void InitView() {
		String billNoStr = mOrderData.getCurrentOrder().getBillNo();

		if (level == 1 && mOrderData.getmOrderStatus() == OrderStatus.ORDER_HANGUP) {
			// 恢复挂单
			leftbarTitleText.setText("挂单详情");
			RightButton.setText(R.string.recover_order);
		} else {
			// 撤销订单
			RightButton.setText(R.string.revocation_order);
		}

		if (!Common.checkValidString(billNoStr)) {
			mBillNo.setVisibility(View.GONE);
		} else {
			mBillNo.setText(billNoStr);
		}

		if (mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_CASH.toInt()) {
			leftbarTitleText.setText("订单详情");
			mPayMethod.setText(R.string.info_input_cash);
			leftbar_checkout_deal_btn.setVisibility(View.GONE);
			LeftButton.setVisibility(View.GONE);
		} else if ((mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_MSR.toInt())
				|| (mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_EMV.toInt())) {
			mPayMethod.setText(R.string.info_input_card);
			leftbar_checkout_deal_btn.setVisibility(View.GONE);
			leftbarTitleText.setText("订单详情");
			// TODO:remove this
			LeftButton.setVisibility(View.GONE);
		} else {
			mPayMethod.setText(R.string.notpay);
			leftbar_checkout_deal_btn.setVisibility(View.GONE);
			LeftButton.setText(R.string.cancle_hangup);
		}

		if ((mOrderData.getmOrderStatus() == OrderStatus.ORDER_PRESTORE)
				|| (mOrderData.getmOrderStatus() == OrderStatus.ORDER_PERREFUND)) {
			// TODO:未确定挂单，需要更多详细信息
			// 有3种情况

		} else if (mOrderData.getmOrderStatus() == OrderStatus.ORDER_NORMAL) {
			// 状态为normal，并且有刷卡支付信息，如果由于程序问题没有存储支付信息，添加支付信息
			int orderid = mOrderData.getCurrentOrder().getOrderID();
			OrderProcess process = OrderManager.getOrderProcessById(orderid);
			if (process != null) {
				// orderprocess返回并存储，修复订单信息
				if (AnalizeOrderProcess.getOrderAleradyPayed(orderid, process.getOrderProcessModeId(),
						mOrderData.getAmount())) {
					AnalizeOrderProcess.solveOrderAleradyPayed(orderid, process.getOrderProcessModeId(),
							mOrderData.getAmount());
				}
				mPayMethod.setText(R.string.info_input_card);
				leftbar_checkout_deal_btn.setVisibility(View.GONE);
				LeftButton.setVisibility(View.GONE);
				restoreType = PRESTORETYPE1;
			}
		}

		if (mOrderData.getmOrderStatus() == OrderStatus.ORDER_UNDO) {
			RightButton.setClickable(false);
			RightButton.setVisibility(View.GONE);
		}

		mMoneyView.setMoney(mOrderData.getAmount());
		mDateView.setText(Common.getDateTimeString(mOrderData.getCurrentOrder().getCreateTime()));
		String acN = "";
		if (mOrderData.getmOrderToAccount() != null)
			acN = mOrderData.getmOrderToAccount().getAccountName();
		accountName.setText(acN);
	}

	private void deleteHangup() {
		this.getFragmentManager().popBackStack();
		OrderManager.deleteOrderInfoById(orderid);
		SaleActivity mSaleActivity = (SaleActivity) this.getActivity();
		mSaleActivity.hideDialog();
	}

	@Override
	public void onClick(View v) {
		if (JavaUtil.isFastDoubleClick())
			return;
		switch (v.getId()) {
		case R.id.order_printer:
			int mode = 0;
			if (mOrderData.getmOrderStatus() == OrderStatus.ORDER_HANGUP) {
				mode = OrderPrint.HANGUP_MODE;
			} else if (mOrderData.getmOrderStatus() == OrderStatus.ORDER_NORMAL) {
				mode = OrderPrint.ADDTIONAL_MODE;
			} else if (mOrderData.getmOrderStatus() == OrderStatus.ORDER_UNDO) {
				mode = OrderPrint.ALREADYUND_MODE;
			} else if (mOrderData.getmOrderStatus() == OrderStatus.ORDER_PRESTORE) {
				mode = OrderPrint.SALENEEDREVERSE;
			} else if (mOrderData.getmOrderStatus() == OrderStatus.ORDER_PERREFUND) {
				mode = OrderPrint.SALEVOIDNEEDREVERSE;
			} else
				return;
			try {
				PrinterManager.getInstance().printOrder(mOrderData.clone(), mode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.leftbar_detail_back:
			if (mLeftBarFragment != null)
				mLeftBarFragment.backBtnClicked();
			break;
		case R.id.left_button:
			// 挂单的
			if (mOrderData.getPayType() != PayBase.PAYTYPE.PAY_BY_CASH.toInt()
					&& mOrderData.getPayType() != PayBase.PAYTYPE.PAY_BY_MSR.toInt()
					&& mOrderData.getmOrderStatus() != OrderStatus.ORDER_PRESTORE) {
				mDeleteHangupDialog = new AnyWhereDialog(getActivity(), 540, 280, 0, 0, R.layout.confirm_delete,
						R.style.Theme_dialog1, Gravity.LEFT | Gravity.TOP, true);
				Button confirmBtn = (Button) mDeleteHangupDialog.findViewById(R.id.confirm_delete);
				Button cancelBtn = (Button) mDeleteHangupDialog.findViewById(R.id.cancel_delete);
				confirmBtn.setOnClickListener(this);
				cancelBtn.setOnClickListener(this);
				mDeleteHangupDialog.show();
			} else if (mOrderData.getmOrderStatus() == OrderStatus.ORDER_PRESTORE
					|| (mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_MSR.toInt() && restoreType == PRESTORETYPE2)) { // 冲正//TODO:需要其他的判断信息
				// ReverseActivity

			} else
				mLeftBarFragment.addLeftBarProductItemFragment(orderid);
			break;
		case R.id.confirm_delete:
			deleteHangup();
			if (mDeleteHangupDialog != null)
				mDeleteHangupDialog.dismiss();
			break;
		case R.id.cancel_delete:
			if (mDeleteHangupDialog != null)
				mDeleteHangupDialog.dismiss();
			break;
		case R.id.leftbar_checkout_deal:
			if (mOrderData.getCardPayInfos() != null && mOrderData.getCardPayInfos().getOrderProcess() != null)
				mLeftBarFragment.addLeftBarProductBilling(mOrderData.getCardPayInfos().getOrderProcess()
						.getOrderProcessId());
			break;
		case R.id.right_button:
			if (level == 1 && mOrderData.getmOrderStatus() == OrderStatus.ORDER_HANGUP) {
				if (mOrderData != null) {
					if (DealModel.getInstance().getOrderData().getInitOlderHangUpList() != null) {
						mContCoverDialog = new AnyWhereDialog(getActivity(), 540, 280, 0, 0, R.layout.alert_xml,
								R.style.Theme_dialog1, Gravity.LEFT | Gravity.TOP, true);
						Button iKnow = (Button) mContCoverDialog.findViewById(R.id.i_know);
						iKnow.setOnClickListener(this);
						mContCoverDialog.show();
					} else {
						this.getFragmentManager().popBackStack();
						DealModel.getInstance().RecoverOrderData(mOrderData);
						SaleActivity mSaleActivity = (SaleActivity) this.getActivity();
						mSaleActivity.hideDialog();
					}
				}
			} else if (mOrderData.getmOrderStatus() == OrderStatus.ORDER_NORMAL
					|| mOrderData.getmOrderStatus() == OrderStatus.ORDER_PERREFUND) { // TODO:need
																						// check
				int count = getFragmentManager().getBackStackEntryCount();
				for (int i = 0; i < count; i++) {
					this.getFragmentManager().popBackStack();
				}
				MyLog.i("is ok here");
				SaleActivity mSaleActivity = (SaleActivity) this.getActivity();
				mSaleActivity.hideDialog();

				// RefundActivity
				Intent refundintent = new Intent(LeftBarProductItemInfoFragment.this.getActivity(),
						RefundActivity.class);
				Bundle translateBundle = ActivityOptions.makeCustomAnimation(
						LeftBarProductItemInfoFragment.this.getActivity(), android.R.anim.fade_in,
						android.R.anim.fade_out).toBundle();
				refundintent.putExtra("orderid", orderid);
				LeftBarProductItemInfoFragment.this.getActivity().startActivity(refundintent, translateBundle);
			} else
				return;
			break;
		case R.id.i_know:
			if (mContCoverDialog != null)
				mContCoverDialog.dismiss();
			break;
		}
	}

	private void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		if (listAdapter.getCount() > 0)
			totalHeight = listAdapter.getView(0, null, listView).getMinimumHeight() * listAdapter.getCount();
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			getActivity().finish();
			return true;
		}
		return false;
	}
}
