package com.koolyun.mis.fragment;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ViewFlipper;

import com.koolyun.mis.R;
import com.koolyun.mis.adapter.SwitchAdapter;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.IProductModifyer;
import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.core.order.OrderContentRemark;
import com.koolyun.mis.core.product.ProductAttribute;
import com.koolyun.mis.core.product.ProductManager;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.myinterface.OnUpdateOnsale;
import com.koolyun.mis.widget.AnyWhereDialog;
import com.koolyun.mis.widget.ISimpleCallBack;
import com.koolyun.mis.widget.MoneyView;
import com.koolyun.mis.widget.ProductAttributeSelector;

@SuppressLint("HandlerLeak")
public class AttrDialogFragment extends AbstractDialogFragment implements TextWatcher, ISimpleCallBack,
		OnClickListener, IProductModifyer, DialogInterface.OnKeyListener, OnUpdateOnsale {
	private int position = 0;
	private AnyWhereDialog mDialog;
	private ImageView mArrowNo;
	private ListView mListView;
	private Button mOnsaleButton;
	private LinearLayout layout;
	private Button mDialogPlus, mDialogMinus;
	private ViewFlipper mDialogViewFlipper;
	private OrderContentData mOrderContent;
	private TextView mName = null;
	private MoneyView mPrice = null;
	private EditText mDialogShowNumber = null;
	private EditText mRemark = null;
	private boolean flag = true;
	private SwitchAdapter mSwitchAdapter = null;
	private List<ProductAttributeSelector> mPABS = new LinkedList<ProductAttributeSelector>();

	public static AttrDialogFragment newInstance(int productid, int productCata) {
		AttrDialogFragment frag = new AttrDialogFragment();
		Bundle args = new Bundle();
		args.putInt("productID", productid);
		args.putInt("productCata", productCata);
		frag.setArguments(args);
		return frag;
	}

	class InitHandler extends Handler {
		public InitHandler() {

		}

		public InitHandler(Looper L) {
			super(L);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			initAttrSelectorState();
		}
	}

	public AttrDialogFragment() {

	}

	public void setOrderContent(OrderContentData orderContent) {
		this.mOrderContent = orderContent;
		if (mOrderContent != null && mOrderContent.getProduct().getProductID() == 0) {
			if (mOnsaleButton != null)
				mOnsaleButton.setVisibility(View.INVISIBLE);
		}
		if (mSwitchAdapter != null) {
			mSwitchAdapter.updateOnePrice(mOrderContent.getOnePrice());
		}
		initAttrSelectorState();
		if (mRemark != null) {
			mRemark.setText(mOrderContent.getmOrderContentRemark().getRemark());
		}
	}

	private void refreshView() {
		if (mOrderContent != null) {
			mName.setText("" + mOrderContent.getOrderContentName());
			mPrice.setMoney(mOrderContent.getItemAmount());
			String text = "" + mOrderContent.getCount();
			mDialogShowNumber.setText(text);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mDialog = new AnyWhereDialog(getActivity(), 580, 770, -110, 100, R.layout.myalertdialog, R.style.Theme_dialog1,
				Gravity.LEFT | Gravity.TOP, true);
		mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mDialog.setOnKeyListener(this);

		init_arrow();
		layout = (LinearLayout) mDialog.findViewById(R.id.mylineary);
		layout.setOrientation(LinearLayout.VERTICAL);

		// accountlayout = (LinearLayout) mDialog.findViewById(R.id.account);
		setShowPositon(position);
		int productID = getArguments().getInt("productID");

		if (productID != ShoppingCart.MANUID) {
			List<ProductAttribute> mpAttrList = ProductManager.getProductAttributeById(productID, true);
			for (int i = 0; i < mpAttrList.size(); i++) {
				List<ProductSubAttribute> mpsA = ProductManager.getProductSubAttributeByAttrId(mpAttrList.get(i)
						.getProductAttributeID());
				ProductAttributeSelector nAttrSelector = new ProductAttributeSelector(this.getActivity(),
						mpAttrList.get(i), mpsA, null);
				nAttrSelector.setISimpleCallBack(this);
				mPABS.add(nAttrSelector);
				layout.addView(nAttrSelector);
			}
		} else {
			// 手动输入的产品
			mRemark = new EditText(this.getActivity());
			LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			mRemark.setLayoutParams(clp);
			mRemark.setEnabled(true);
			mRemark.setHint(R.string.remark_message);
			// if you create EditText by code ,please init color with following
			// code
			// mRemark.setHintTextColor(Color.parseColor("#C7C7C7"));
			mRemark.setHintTextColor(getResources().getColor(R.color.input_hint));
			mRemark.setTextSize(25);

			mRemark.setTextColor(R.color.text_normal_color);
			if (this.mOrderContent != null && mOrderContent.getmOrderContentRemark() != null)
				mRemark.setText(mOrderContent.getmOrderContentRemark().getRemark());
			mRemark.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable arg0) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (AttrDialogFragment.this.mOrderContent != null) {
						AttrDialogFragment.this.mOrderContent.setmOrderContentRemark(new OrderContentRemark(-1, s
								.toString()));
						DealModel.getInstance().getShoppingCart().update();
						refreshView();
					}
				}
			});
			layout.addView(mRemark);
		}

		InitHandler initHandler = new InitHandler();
		initHandler.sendMessage(new Message());
		return mDialog;
	}

	private void initAttrSelectorState() {
		for (int i = 0; i < mPABS.size(); i++) {
			if (mOrderContent != null)
				mPABS.get(i).InitButtonState(mOrderContent.getProductSubAttrList());
		}

	}

	private void init_arrow() {

		mArrowNo = (ImageView) mDialog.findViewById(R.id.dialog_arrow1);
		mDialogPlus = (Button) mDialog.findViewById(R.id.dialog_plus);
		mDialogMinus = (Button) mDialog.findViewById(R.id.dialog_minus);

		mDialogViewFlipper = (ViewFlipper) mDialog.findViewById(R.id.dialog_viewflipper);
		mDialogViewFlipper.setDisplayedChild(0);
		mDialogViewFlipper.setAnimation(null);
		mOnsaleButton = (Button) mDialog.findViewById(R.id.dialog_onsale_button);
		if (mOrderContent != null) {
			if (mOrderContent.getProductId() == 0)
				mOnsaleButton.setVisibility(View.INVISIBLE);
		}
		mListView = (ListView) mDialog.findViewById(R.id.dialog_swicth_listview);
		mSwitchAdapter = new SwitchAdapter(getActivity(), this, mOrderContent.getOnsale());
		mListView.setAdapter(mSwitchAdapter);
		if (mOrderContent != null)
			mSwitchAdapter.updateOnePrice(mOrderContent.getOnePrice());

		mName = (TextView) mDialog.findViewById(R.id.dialog_show_name);
		mPrice = (MoneyView) mDialog.findViewById(R.id.dialog_show_price);
		mDialogShowNumber = (EditText) mDialog.findViewById(R.id.dialog_show_number);

		mDialogShowNumber.addTextChangedListener(this);

		mOnsaleButton.setOnClickListener(this);
		mDialogPlus.setOnClickListener(this);
		mDialogMinus.setOnClickListener(this);

		mDialogShowNumber.setOnClickListener(this);

		refreshView();
		mDialogShowNumber.setCursorVisible(false);

		mDialogShowNumber.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					flag = true;
					mDialogShowNumber.setCursorVisible(false);
				}
				return false;
			}
		});
	}

	private void setShowPositon(int position) {
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mArrowNo.getLayoutParams();
		params.setMargins(0, 180 + position * 83, 23, 0);
		mArrowNo.setLayoutParams(params);
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_onsale_button:
			viewflipper_switcher();
			break;
		case R.id.dialog_plus:
			mOrderContent.addOneItem();
			DealModel.getInstance().getShoppingCart().update();
			refreshView();
			plus_andr_minus();
			break;
		case R.id.dialog_minus:
			mOrderContent.subOneItem();
			DealModel.getInstance().getShoppingCart().update();
			refreshView();
			plus_andr_minus();
			break;
		case R.id.dialog_show_number:
			click_edittext();
			break;
		default:
			break;
		}

	}

	@Override
	public void SimpleCallback() {
		List<ProductSubAttribute> mTmp = new LinkedList<ProductSubAttribute>();
		for (int i = 0; i < mPABS.size(); i++) {
			mTmp.addAll(mPABS.get(i).getSelectedSubArrri());
		}

		mOrderContent.setProductSubAttrList(mTmp);
		DealModel.getInstance().getShoppingCart().update();
		refreshView();
	}

	private void viewflipper_switcher() {
		Animation animationin = AnimationUtils.loadAnimation(getActivity(), R.anim.hold);
		Animation animationout = AnimationUtils.loadAnimation(getActivity(), R.anim.push_fade);

		if (mDialogViewFlipper.getDisplayedChild() == 0) {
			mDialogViewFlipper.setInAnimation(animationin);
			mDialogViewFlipper.setOutAnimation(animationout);
			mDialogViewFlipper.setDisplayedChild(1);
		} else {
			mDialogViewFlipper.setInAnimation(animationin);
			mDialogViewFlipper.setOutAnimation(animationout);
			mDialogViewFlipper.setDisplayedChild(0);
		}

	}

	@Override
	public void onDestroyView() {
		DealModel.getInstance().getShoppingCart().unsetCurrentEditIndex();
		// Common.HideKeyboardIfExist(this);
		super.onDestroyView();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	private void click_edittext() {
		mDialogShowNumber.setCursorVisible(true);
		if (flag) {
			CharSequence text = mDialogShowNumber.getText();
			if (text instanceof Spannable) {
				Spannable span = (Spannable) text;
				Selection.setSelection(span, text.length());
			}
			flag = !flag;
		}
	}

	private void plus_andr_minus() {
		CharSequence text = mDialogShowNumber.getText();
		if (text instanceof Spannable) {
			Spannable span = (Spannable) text;
			Selection.setSelection(span, text.length());
		}
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if (arg0 != null && !arg0.toString().isEmpty()) {
			mOrderContent.setCount(Integer.parseInt(arg0.toString()));
			DealModel.getInstance().getShoppingCart().update();
		}
	}

	@Override
	public void onOnSaleChanged(List<Onsale> mOnsaleList) {
		mOrderContent.setOnsale(mOnsaleList);

		if (mSwitchAdapter != null) {
			mSwitchAdapter.updateOnePrice(mOrderContent.getOnePrice());
		}

		DealModel.getInstance().getShoppingCart().update();
		refreshView();
	}

	@Override
	public void updateOrderContent(OrderContentData mContent) {

	}

}
