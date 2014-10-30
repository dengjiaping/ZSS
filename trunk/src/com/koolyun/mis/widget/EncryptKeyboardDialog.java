package com.koolyun.mis.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.koolyun.mis.R;

public class EncryptKeyboardDialog extends Dialog {

	public interface EncryptKeyboardDialogInterface {
		public void confirm();
	}

	private EncryptKeyboardDialogInterface dialogInterface;
	private Button cancelImageButton, confirmImageButton;

	public EncryptKeyboardDialog(Context context) {
		super(context);
	}

	public EncryptKeyboardDialog(Context context, EncryptKeyboardDialogInterface dialogInterface) {
		super(context);
		this.dialogInterface = dialogInterface;
	}

	public EncryptKeyboardDialog(Context context, int theme) {
		super(context, theme);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();

		params.width = 808;
		params.height = 660;
		params.alpha = 1.0f;
		params.dimAmount = 0.5f;
		params.x = 231;
		params.y = 10;
		window.setAttributes(params);
	}

	public EncryptKeyboardDialog(Context context, int theme, EncryptKeyboardDialogInterface dialogInterface) {
		super(context, theme);
		this.dialogInterface = dialogInterface;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.encrypt_keyboard);

		setCanceledOnTouchOutside(false);

		cancelImageButton = (Button) findViewById(R.id.imageButtonCancel);
		cancelImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		confirmImageButton = (Button) findViewById(R.id.imageButtonConfirm);
		confirmImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();

				if (dialogInterface != null)
					dialogInterface.confirm();
			}
		});

		EditText edit = (EditText) findViewById(R.id.implyAccont);
		edit.setInputType(InputType.TYPE_NULL);

		// Order order = CloudPosApp.getInstance().getOrder();
		// passwdAccountTextView = (TextView) findViewById(R.id.implyAccont);
		// passwdAccountTextView.setText("楼 " + order.getTotalAmount());
	}
}
