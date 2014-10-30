package com.koolyun.mis.core;

public class BaseInfoManager {
	private boolean infoHasGot = false;
	private String mMerchantCode = null;
	private String mTerminalCode = null;
	private String mSerialID = null;

	public void setAllBaseInfo(String mm, String mt, String ms) {
		this.mSerialID = ms;
		this.mMerchantCode = mm;
		this.mTerminalCode = mt;
		this.infoHasGot = true;
	}

	public String getmMerchantCode() {
		return mMerchantCode;
	}

	public void setmMerchantCode(String mMerchantCode) {
		this.mMerchantCode = mMerchantCode;
	}

	public String getmTerminalCode() {
		if (mTerminalCode == null)
			return "00000001";
		return mTerminalCode;
	}

	public void setmTerminalCode(String mTerminalCode) {
		this.mTerminalCode = mTerminalCode;
	}

	public String getmSerialID() {
		return mSerialID;
	}

	public void setmSerialID(String mSerialID) {
		this.mSerialID = mSerialID;
	}

	public void clearAllInfos() {
		this.mSerialID = null;
		this.mMerchantCode = null;
		this.mTerminalCode = null;
		this.infoHasGot = false;
	}

	public boolean isInfoHasGot() {
		return infoHasGot;
	}

}
