package com.koolyun.mis.bean;

import java.io.Serializable;

public class TIDBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sID; // merchant name
	private String tID; // merchant id
	private String storeID; // merchant id

	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	public String getsID() {
		return sID;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String gettID() {
		return tID;
	}

	public void settID(String tID) {
		this.tID = tID;
	}
}
