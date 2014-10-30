package com.koolyun.mis.core.store;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.koolyun.mis.util.Common;

public class Store {

	@Expose
	private int storeID;
	@Expose
	private int storeAddressID;
	@Expose
	private int companyAddressID;
	@Expose
	private String storeName;
	@Expose
	private String storePhoto;
	@Expose
	private String companyName;
	@Expose
	private String companyPhoto;
	@Expose
	private String nickName;
	@Expose
	private String address;
	@Expose
	private String companyAddress;

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	Drawable photoTOShow = null;

	public Store() {
		this.storeID = -1;
		this.storeAddressID = -1;
		this.companyAddressID = -1;
		this.storeName = "";
		this.storePhoto = null;
		this.companyName = null;
		this.companyPhoto = null;
		this.nickName = null;
		this.address = null;
		this.companyAddress = null;
	}

	public Store(int storeID, int storeAddressID, int companyAddressID, String storeName, String storePhoto,
			String companyName, String companyPhoto, String nickName, String address, String companyAddress) {
		this.storeID = storeID;
		this.storeAddressID = storeAddressID;
		this.companyAddressID = companyAddressID;
		this.storeName = storeName;
		this.storePhoto = storePhoto;
		this.companyName = companyName;
		this.companyPhoto = companyPhoto;
		this.nickName = nickName;
		this.address = address;
		this.companyAddress = companyAddress;
	}

	public void setInfo(int storeID, int storeAddressID, int companyAddressID, String storeName, String storePhoto,
			String companyName, String companyPhoto, String nickName, String address, String companyAddress) {
		this.storeID = storeID;
		this.storeAddressID = storeAddressID;
		this.companyAddressID = companyAddressID;
		this.storeName = storeName;
		this.storePhoto = storePhoto;
		this.companyName = companyName;
		this.companyPhoto = companyPhoto;
		this.nickName = nickName;
		this.address = address;
		this.companyAddress = companyAddress;
	}

	public int getStoreID() {
		return storeID;
	}

	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getStoreAddressID() {
		return storeAddressID;
	}

	public void setStoreAddressID(int storeAddressID) {
		this.storeAddressID = storeAddressID;
	}

	public String getStorePhoto() {
		return storePhoto;
	}

	public void setStorePhoto(String storePhoto) {
		this.storePhoto = storePhoto;
	}

	public int getCompanyAddressID() {
		return companyAddressID;
	}

	public void setCompanyAddressID(int companyAddressID) {
		this.companyAddressID = companyAddressID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyPhoto() {
		return companyPhoto;
	}

	public void setCompanyPhoto(String companyPhoto) {
		this.companyPhoto = companyPhoto;
	}

	public Drawable getPhotoToShow() {
		String path = null;
		if (photoTOShow == null) {
			if (storePhoto != null && !storePhoto.isEmpty())
				path = storePhoto;
			else if (companyPhoto != null && !companyPhoto.isEmpty()) {
				path = companyPhoto;
			} else {
				return null;
			}
			photoTOShow = Drawable.createFromPath(Common.getRealImagePath(path));
			return photoTOShow;
		} else {
			return photoTOShow;
		}

	}

}
