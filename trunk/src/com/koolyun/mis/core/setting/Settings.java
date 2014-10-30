package com.koolyun.mis.core.setting;

import com.koolyun.mis.util.Common;

public class Settings {

	private static final int SERIALNUMLENGTH = 8;
	private String cynovoIP;
	private int cynovoPort;
	private String cardlinkIP;
	private int cardlinkPort;
	private String serialNumber;
	private String traceNo;

	// private String merchantNo;
	// private String terminalNo;

	public Settings(String cynovoIP, int cynovoPort, String cardlinkIP, int cardlinkPort, String serialNumber,
			String traceNo) {
		this.cynovoIP = cynovoIP;
		this.cynovoPort = cynovoPort;
		this.cardlinkIP = cardlinkIP;
		this.cardlinkPort = cardlinkPort;
		this.serialNumber = serialNumber;
		this.traceNo = traceNo;
	}

	public Settings() {

	}

	public String getCynovoIP() {
		return cynovoIP;
	}

	public void setCynovoIP(String cynovoIP) {
		this.cynovoIP = cynovoIP;
	}

	public int getCynovoPort() {
		return cynovoPort;
	}

	public void setCynovoPort(int cynovoPort) {
		this.cynovoPort = cynovoPort;
	}

	public String getCardlinkIP() {
		return cardlinkIP;
	}

	public void setCardlinkIP(String cardlinkIP) {
		this.cardlinkIP = cardlinkIP;
	}

	public int getCardlinkPort() {
		return cardlinkPort;
	}

	public void setCardlinkPort(int cardlinkPort) {
		this.cardlinkPort = cardlinkPort;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	// public void setMerchantNo(String no) {
	// this.merchantNo = no;
	// }
	//
	// public String getMerchantNo() {
	// return merchantNo;
	// }
	//
	// public void setTermialNo(String no) {
	// this.terminalNo = no;
	// }
	//
	// public String getTermialNo() {
	// return terminalNo;
	// }

	public String getSerialNumber() {
		if (serialNumber.length() != SERIALNUMLENGTH
				|| !serialNumber.substring(0, 2).equals(Common.getCurrentDateTimeString("dd"))) {
			serialNumber = Common.getCurrentDateTimeString("dd") + "000000";
			SettingsManager.updateSerialNumber(serialNumber);
		}
		return serialNumber.substring(2, SERIALNUMLENGTH);
	}

	public void setSerialNumber(String serialNumber) {
		if (serialNumber == null || serialNumber.length() != 6)
			serialNumber = "000001";
		this.serialNumber = Common.getCurrentDateTimeString("dd") + serialNumber;
	}

	public String getAndIncTraceNo() {
		String tempTraceNoString = traceNo;

		if (tempTraceNoString.equals("999999"))
			traceNo = "000001";
		else
			traceNo = String.format("%06d", (Integer.valueOf(tempTraceNoString) + 1));

		SettingsManager.updateTraceNo(traceNo);

		return tempTraceNoString;
	}

	public String incAndGetSerialNumber() {
		String tempSerialNoString = getSerialNumber();
		if (tempSerialNoString.equals("999999"))
			setSerialNumber("000001");
		else
			setSerialNumber(String.format("%06d", (Integer.valueOf(tempSerialNoString) + 1)));
		SettingsManager.updateSerialNumber(serialNumber);
		return getSerialNumber();
	}
}
