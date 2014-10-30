package com.koolyun.mis.core.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Onsale {

	@SerializedName("cid")
	@Expose
	private int onsaleID;
	@Expose
	private int onsaleType = 1;
	@Expose
	private String onsaleName;
	@Expose
	private String value;
	@Expose
	private int enable = 1;

	private int syncFlag = 0;

	private int isSelected;

	public Onsale() {

	}

	@Override
	public boolean equals(Object o) {
		Onsale tmp = (Onsale) o;
		return (this.onsaleID == tmp.getOnsaleID());
	}

	public Onsale(int onsaleID, int onsaleType, String onsaleName, String value, int enable) {
		this.onsaleID = onsaleID;
		this.onsaleType = onsaleType;
		this.onsaleName = onsaleName;
		this.value = value;
		this.enable = enable;
	}

	public int getOnsaleID() {
		return onsaleID;
	}

	public void setOnsaleID(int onsaleID) {
		this.onsaleID = onsaleID;
	}

	public int getOnsaleType() {
		return onsaleType;
	}

	public void setOnsaleType(int onsaleType) {
		this.onsaleType = onsaleType;
	}

	public String getOnsaleName() {
		return onsaleName;
	}

	public void setOnsaleName(String onsaleName) {
		this.onsaleName = onsaleName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public int getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}

}
