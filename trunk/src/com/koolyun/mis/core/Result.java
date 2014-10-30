package com.koolyun.mis.core;

import com.google.gson.annotations.Expose;

public class Result {
	@Expose
	int ret;
	@Expose
	int retcode;
	@Expose
	String msg;

	public int getRet() {
		return ret;
	}

	public int getRetcode() {
		return retcode;
	}

	public String getMsg() {
		return msg;
	}

}
