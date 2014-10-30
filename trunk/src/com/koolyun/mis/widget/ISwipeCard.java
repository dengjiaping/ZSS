package com.koolyun.mis.widget;

public interface ISwipeCard {

	void swipDone();

	void swipeError(int code);

	void pinPadError(int code);

	void posError(int code);
}
