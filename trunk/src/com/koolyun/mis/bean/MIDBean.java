package com.koolyun.mis.bean;

import java.io.Serializable;

public class MIDBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mName; // merchant name
	private String mID; // merchant id
	private String storeID;

	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmID() {
		return mID;
	}

	public void setmID(String mID) {
		this.mID = mID;
	}
}
