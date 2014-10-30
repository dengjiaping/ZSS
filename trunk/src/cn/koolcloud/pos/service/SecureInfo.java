package cn.koolcloud.pos.service;

import android.os.Parcel;
import android.os.Parcelable;

public class SecureInfo implements Parcelable {
	private String sn;
	private String isOriginSn;
	private byte[] workKey;
	private String session;

	public SecureInfo(String sn, String isOriginSn, byte[] workKey, String session) {
		this.sn = sn;
		this.isOriginSn = isOriginSn;
		this.workKey = workKey;
		this.session = session;
	}

	public SecureInfo(Parcel source) {
		this.readFromParcel(source);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(sn);
		parcel.writeString(isOriginSn);
		if (workKey == null) {
			parcel.writeInt(0);
		} else {
			parcel.writeInt(workKey.length);
			parcel.writeByteArray(workKey);
		}
		parcel.writeString(session);
	}

	public void readFromParcel(Parcel in) {
		sn = in.readString();
		isOriginSn = in.readString();
		int workKeyLength = in.readInt();
		if (workKeyLength != 0) {
			workKey = new byte[workKeyLength];
			in.readByteArray(workKey);
		}
		session = in.readString();
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getIsOriginSn() {
		return isOriginSn;
	}

	public void setIsOriginSn(String isOriginSn) {
		this.isOriginSn = isOriginSn;
	}

	public byte[] getWorkKey() {
		return workKey;
	}

	public void setWorkKey(byte[] workKey) {
		this.workKey = workKey;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public static final Parcelable.Creator<SecureInfo> CREATOR = new Parcelable.Creator<SecureInfo>() {
		public SecureInfo createFromParcel(Parcel source) {
			return new SecureInfo(source);
		}

		public SecureInfo[] newArray(int size) {
			return new SecureInfo[size];
		}
	};
}
