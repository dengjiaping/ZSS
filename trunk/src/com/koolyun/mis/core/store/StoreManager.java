package com.koolyun.mis.core.store;

import android.database.Cursor;

import com.koolyun.mis.core.ManagerBase;

public class StoreManager extends ManagerBase {

	static Store currentStore = null;

	public static Address getAddressInfoByStoreID(int storeid) {
		return new Address();
	}

	public static Store getStore() {
		String sql = "SELECT * FROM `Store`";
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return new Store();
		}
		int storeID = result.getInt(0);

		String storeName = (result.getString(1) == null || result.getString(1).length() == 0 || result.getString(1)
				.equals("null")) ? "" : result.getString(1);
		int storeAddressID = result.getInt(2);
		String storePhoto = result.getString(3);
		int companyAddressID = result.getInt(4);
		String companyName = (result.getString(5) == null || result.getString(5).length() == 0 || result.getString(5)
				.equals("null")) ? "" : result.getString(5);
		String companyPhoto = result.getString(6);
		String nickName = (result.getString(7) == null || result.getString(7).length() == 0 || result.getString(7)
				.equals("null")) ? "" : result.getString(7);
		String address = (result.getString(8) == null || result.getString(8).length() == 0 || result.getString(8)
				.equals("null")) ? "" : result.getString(8);
		String caddress = (result.getString(9) == null || result.getString(9).length() == 0 || result.getString(9)
				.equals("null")) ? "" : result.getString(9);

		currentStore = new Store(storeID, storeAddressID, companyAddressID, storeName, storePhoto, companyName,
				companyPhoto, nickName, address, caddress);

		result.close();
		return currentStore;
	}

	public static Address getAddress() {
		String sql = "SELECT * FROM `Address`";
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return new Address();
		}
		int addressID = result.getInt(0);
		String addressInfo = result.getString(1);
		int longitude = result.getInt(2);
		int latitude = result.getInt(3);

		Address ac = new Address(addressID, addressInfo, longitude, latitude);
		result.close();
		return ac;
	}

	public static String getStoreAddress() {
		currentStore = getStore();
		if (currentStore.getAddress() != null && !currentStore.getAddress().equals("")
				&& !currentStore.getAddress().equals("null"))
			return currentStore.getAddress();
		else if (currentStore.getCompanyAddress() != null && !currentStore.getCompanyAddress().equals("")
				&& !currentStore.getCompanyAddress().equals("null"))
			return currentStore.getCompanyAddress();
		else
			return "";
	}

	public static String getStoreName() {
		currentStore = getStore();
		if (currentStore.getNickName() != null && !currentStore.getNickName().equals("")
				&& !currentStore.getNickName().equals("null"))
			return currentStore.getNickName();
		else if (currentStore.getCompanyName() != null && !currentStore.getCompanyName().equals("")
				&& !currentStore.getCompanyName().equals("null"))
			return currentStore.getCompanyName();
		else
			return "";
	}

	public static Store getStorePic() {

		Store mStore = null;
		String sql = "SELECT * FROM `Store`";
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return null;
		}
		int storeID = result.getInt(0);
		String storeName = result.getString(1);
		int storeAddressID = result.getInt(2);
		String storePhoto = result.getString(3);
		int companyAddressID = result.getInt(4);
		String companyName = result.getString(5);
		String companyPhoto = result.getString(6);
		String nickName = result.getString(7);
		String address = result.getString(8);
		String caddress = result.getString(9);

		mStore = new Store(storeID, storeAddressID, companyAddressID, storeName, storePhoto, companyName, companyPhoto,
				nickName, address, caddress);
		result.close();
		return mStore;
	}

	public static Address findAddressdinfo(int id) {
		Address address = null;
		String sql = "select * from Address where addressID ='" + id + "'";
		Cursor cursor = msqlitedb.rawQuery(sql, null);
		boolean retval = cursor.moveToFirst();
		if (retval == false) {
			cursor.close();
			return new Address();
		}
		int addressID = cursor.getInt(0);
		String addressInfo = cursor.getString(1);
		int longitude = cursor.getInt(2);
		int latitude = cursor.getInt(3);
		cursor.close();
		address = new Address(addressID, addressInfo, longitude, latitude);
		return address;
	}

	public static void updateStoreLogo(String picName) {
		String sql = "update Store set storePhoto='" + picName + "'";
		msqlitedb.execSQL(sql);

	}

	public static void updateStoreInfo(String nickName, String companyName, String storeAddress, String companyAddress) {
		String sql = "update Store set nickName='" + nickName + "',companyName='" + companyName + "',companyAddress='"
				+ companyAddress + "',address='" + storeAddress + "'";
		msqlitedb.execSQL(sql);
	}

	public static void updateStoreId(int storeID) {
		String sql = "update Store set storeID=" + storeID;
		msqlitedb.execSQL(sql);
	}
}
