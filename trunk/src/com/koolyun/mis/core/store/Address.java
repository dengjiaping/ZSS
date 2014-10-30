package com.koolyun.mis.core.store;

import com.google.gson.annotations.Expose;

public class Address {

	@Expose
	private int addressID;
	@Expose
	private String addressInfo;
	@Expose
	private int longitude;
	@Expose
	private int latitude;

	public Address() {
		this.addressID = -1;
		this.addressInfo = "";
		this.longitude = -1;
		this.latitude = -1;
	}

	public Address(int addressID, String addressInfo, int longitude, int latitude) {
		this.addressID = addressID;
		this.addressInfo = addressInfo;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public String getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}

	public int getLongtitude() {
		return longitude;
	}

	public void setLongtitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

}
