package com.koolyun.mis.util.update;

import com.google.gson.annotations.Expose;

public class UpdateSendInfo {
	@Expose
	private String storeId = null;
	@Expose
	private String apkName = null;
	@Expose
	private String version = null;

	public UpdateSendInfo(String storeId, String apkName, String version) {
		this.storeId = storeId;
		this.apkName = apkName;
		this.version = version;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
