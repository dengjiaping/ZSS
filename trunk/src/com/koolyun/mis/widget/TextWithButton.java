package com.koolyun.mis.widget;

import com.koolyun.mis.R;
import com.koolyun.mis.widget.SendButton.BeingToSentInterface;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextWithButton extends RelativeLayout implements BeingToSentInterface {
	TextView textview;
	Drawable drawable;
	SendButton sendBtn;

	public TextWithButton(Context context) {
		super(context);

	}

	@SuppressLint("NewApi")
	public TextWithButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.edittext_with_btn, this, true);

		textview = (TextView) this.findViewById(R.id.text_input);

		textview.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().isEmpty()) {
					sendBtn.setEnabled(false);
				} else {
					sendBtn.setEnabled(true);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
		});

		sendBtn = (SendButton) this.findViewById(R.id.send_button);
		sendBtn.setEnabled(true);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithBtn);
		CharSequence text = a.getText(R.styleable.TextViewWithBtn_android_hint);
		if (text != null)
			textview.setHint(text);
		drawable = a.getDrawable(R.styleable.TextViewWithBtn_android_drawableLeft);

		a.recycle();

	}

	public TextWithButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public void onSizeChanged(int w, int h, int ow, int oh) {
		if (drawable != null) {
			drawable.setBounds(0, 0, h, h);
			textview.setCompoundDrawables(drawable, null, null, null);
		}
	}

	private class SentInfosTask extends AsyncTask<String, Void, Long> {

		protected void onPostExecute(Long result) {
			if (result == 0) // 失败
			{
				sendBtn.changeState(SendButton.BtnStateFail);
			} else if (result == 1) // 成功
			{
				sendBtn.changeState(SendButton.BtnStateSuccess);
			}
		}

		@Override
		protected Long doInBackground(String... params) {
			return null;
		}
	}

	@Override
	public void BeginToSend() {
		SentInfosTask dTask = new SentInfosTask();
		dTask.execute("15951568547");

	}

}
