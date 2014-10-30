package com.koolyun.mis.devices;

public class DeviceID {
	public static final int DEVICE_ID_INVALID = -1;
	public static final int DEVICE_ID_NODEV = 0;
	public static final int DEVICE_ID_MSR = 0x01;
	public static final int DEVICE_ID_EMV = 0x02;
	public static final int DEVICE_ID_NFC = 0x04;
	public static final int DEVICE_ID_PRINTER = 0x08;
	public static final int DEVICE_ID_CASHBOX = 0x10;
	public static final int DEVICE_ID_SOFTPINPAD = 0x20;
	public static final int DEVICE_ID_HARDPINPAD = 0x40;// 64
	public static final int DEVICE_ID_NETLINK = 0x80;// 128

	public static final int[] ALLDEVICE = { DEVICE_ID_MSR, DEVICE_ID_EMV, DEVICE_ID_NFC, DEVICE_ID_PRINTER,
			DEVICE_ID_CASHBOX, DEVICE_ID_SOFTPINPAD, DEVICE_ID_HARDPINPAD, DEVICE_ID_NETLINK };
}
