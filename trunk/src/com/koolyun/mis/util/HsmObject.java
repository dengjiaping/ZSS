package com.koolyun.mis.util;

public class HsmObject {

	public String mId;
	public String mLabel;
	public String mPassword;
	public int mType;

	public HsmObject() {

	}

	public HsmObject(String mId, String mLabel, String mPassword, int mType) {
		super();
		this.mId = mId;
		this.mLabel = mLabel;
		this.mPassword = mPassword;
		this.mType = mType;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmLabel() {
		return mLabel;
	}

	public void setmLabel(String mLabel) {
		this.mLabel = mLabel;
	}

	public String getmPassword() {
		return mPassword;
	}

	public void setmPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public int getmType() {
		return mType;
	}

	public void setmType(int mType) {
		this.mType = mType;
	}

}
