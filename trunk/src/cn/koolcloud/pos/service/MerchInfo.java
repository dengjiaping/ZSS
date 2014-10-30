package cn.koolcloud.pos.service;

import android.os.Parcel;
import android.os.Parcelable;

public class MerchInfo implements Parcelable {
	private String merchId;
	private String terminalId;

	public String getMerchName() {
		return merchName;
	}

	public void setMerchName(String merchName) {
		this.merchName = merchName;
	}

	private String merchName;

	public MerchInfo(String merchName, String merchId, String terminalId) {
		this.merchId = merchId;
		this.terminalId = terminalId;
		this.merchName = merchName;
	}

	public MerchInfo(Parcel source) {
		super();
		this.readFromParcel(source);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(merchId);
		parcel.writeString(terminalId);
		parcel.writeString(merchName);
	}

	public void readFromParcel(Parcel in) {
		merchId = in.readString();
		terminalId = in.readString();
		merchName = in.readString();
	}

	public String getMerchId() {
		return merchId;
	}

	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public static final Parcelable.Creator<MerchInfo> CREATOR = new Parcelable.Creator<MerchInfo>() {
		public MerchInfo createFromParcel(Parcel source) {
			return new MerchInfo(source);
		}

		public MerchInfo[] newArray(int size) {
			return new MerchInfo[size];
		}
	};
}
