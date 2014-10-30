package com.koolyun.mis.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.koolyun.mis.R;
import com.koolyun.mis.core.product.ProductSubAttribute;
import com.koolyun.mis.fragment.ProductManagerAttrFragment;

public class AnyWhereDialog extends Dialog implements OnEditorActionListener {
	private static int default_width = 400; //
	private static int default_height = 200; //

	EditText mAttr = null;
	EditText mPrice = null;
	Button mconfirm = null;

	ProductManagerAttrFragment mProductManagerAtrrrFragment = null;

	public AnyWhereDialog(Context context, int layout, int style, int directory, boolean flag) {
		this(context, default_width, default_height, layout, 0, 0, style, directory, flag);
	}

	public void setProductManagerAtrrrFragment(ProductManagerAttrFragment mProductManagerAtrrrFragment) {
		this.mProductManagerAtrrrFragment = mProductManagerAtrrrFragment;
	}

	//
	public AnyWhereDialog(Context context, int width, int height, int x, int y, int layout, int style, int directory,
			boolean flag) {
		super(context, style);

		// set content
		setContentView(layout);

		// set window params
		Window window = getWindow();
		window.setWindowAnimations(style);
		WindowManager.LayoutParams params = window.getAttributes();
		if (layout == R.layout.attribute_factor_layout) {
			mAttr = (EditText) this.findViewById(R.id.attr_dialog_edittext);
			mAttr.setOnEditorActionListener(this);
			mPrice = (EditText) this.findViewById(R.id.price_dialog_edittext);
			mPrice.setOnEditorActionListener(this);
			mconfirm = (Button) this.findViewById(R.id.attr_dialog_save_btn);

			if (mconfirm != null) {
				mconfirm.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						saveAtt();
					}
				});
			}
		}

		// set width,height by density and gravity
		float density = getDensity(context);
		params.width = (int) (width * density);
		params.height = (int) (height * density);
		// params.gravity = directory;
		params.alpha = 1.0f;
		params.dimAmount = 0.5f;
		params.x = x;
		params.y = y;
		setCanceledOnTouchOutside(flag);
		window.setAttributes(params);
	}

	private void saveAtt() {
		if (!mAttr.getText().toString().isEmpty() && !mPrice.getText().toString().isEmpty()) {
			ProductSubAttribute mProductSubAttribute = new ProductSubAttribute(-1, -1, mAttr.getText().toString(),
					mPrice.getText().toString(), 1);
			AnyWhereDialog.this.mProductManagerAtrrrFragment.addNewSubAttr(mProductSubAttribute);
			AnyWhereDialog.this.dismiss();
		}
		mProductManagerAtrrrFragment.hideKeyBoard();
	}

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.attr_dialog_edittext: // 修改昵称
				mPrice.requestFocus();
				break;
			case R.id.price_dialog_edittext: // 修改门店地址
				saveAtt();
				break;
			}
		}
		return false;
	}
}
