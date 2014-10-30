package com.koolyun.mis.core;

public class CheckSelfItemInfo {
	private int deviceId;
	private String deviceName;
	private boolean devStatus;
	private String devInfo;

	public CheckSelfItemInfo() {

	}

	public CheckSelfItemInfo(int deviceId, String deviceName, boolean devStatus, String devInfo) {
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.devStatus = devStatus;
		this.devInfo = devInfo;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public boolean isDevStatus() {
		return devStatus;
	}

	public void setDevStatus(boolean devStatus) {
		this.devStatus = devStatus;
	}

	public String getDevInfo() {
		return devInfo;
	}

	public void setDevInfo(String devInfo) {
		this.devInfo = devInfo;
	}

}
