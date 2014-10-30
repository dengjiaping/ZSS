package com.koolyun.mis.core.user;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.koolyun.mis.line.LineFactory;
import com.koolyun.mis.util.Common;

public class Account implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int accountID;
	private int sync;

	public int getSync() {
		return sync;
	}

	public void setSync(int sync) {
		this.sync = sync;
	}

	private int phoneID;

	public int getPhoneID() {
		return phoneID;
	}

	public void setPhoneID(int phoneID) {
		this.phoneID = phoneID;
	}

	private int accountPrivilege;
	private String firstName;
	private String lastName;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Expose
	private String account;
	@Expose
	private String password;
	private String operatorID = "1";
	private String accountPhoto;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String accountName) {
		this.firstName = accountName;
	}

	public Account() {
		accountID = -1;
		accountPrivilege = 3;
	}

	/**
	 * Account 构造方法
	 * 
	 * @param accountID
	 *            账户id
	 * @param accountPrivilege
	 *            账号权限： 0：管理员 1：店长 2：店员
	 * @param firstName
	 *            姓
	 * @param account
	 *            手机号
	 * @param password
	 *            密码
	 * @param operatorID
	 *            操作员代码
	 * @param accountPhoto
	 *            照片
	 * @param lastName
	 *            名字
	 */
	public Account(int accountID, int accountPrivilege, String firstName, String account, String password,
			String operatorID, String accountPhoto, String lastName, int sync, int phoneID) {

		this.accountID = accountID;
		this.accountPrivilege = accountPrivilege;
		this.firstName = firstName;
		this.account = account;
		this.password = password;
		this.operatorID = operatorID;
		this.accountPhoto = accountPhoto;
		this.lastName = lastName;
		this.sync = sync;
		this.phoneID = phoneID;
	}

	public void InitWith(Account mAccount) {
		this.accountID = mAccount.accountID;
		this.accountPrivilege = mAccount.accountPrivilege;
		this.firstName = mAccount.firstName;
		this.account = mAccount.account;
		this.password = mAccount.password;
		this.operatorID = mAccount.operatorID;
		this.accountPhoto = mAccount.accountPhoto;
		this.lastName = mAccount.lastName;
		this.sync = mAccount.sync;
		this.phoneID = mAccount.phoneID;
	}

	public String getNameToshow() {
		if ((getFirstName() == null || getFirstName().isEmpty()) && (getLastName() == null || getLastName().isEmpty())) {
			return "";
		}

		return (getLastName() == null ? "" : getLastName()) + (getFirstName() == null ? "" : getFirstName());
	}

	/**
	 * @param account
	 * @param password
	 */
	public Account(String account, String password) {
		this.account = account;
		this.password = password;
		Account tmp = AccountManager.getInstance().getAccountByName(account);
		if (tmp != null) {
			this.accountID = tmp.getAccountID();
			this.firstName = tmp.getFirstName();
			this.lastName = tmp.getLastName();
			this.accountPhoto = tmp.getAccountPhoto();
			this.operatorID = tmp.operatorID;
			this.accountPrivilege = tmp.accountPrivilege;
			this.sync = tmp.sync;
			this.phoneID = tmp.phoneID;
		}
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setPass(String password) {
		this.password = password;
	}

	public String getAccount() {
		return account;
	}

	public String getPass() {
		return password;
	}

	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

	public int getAccountID() {
		return accountID;
	}

	public void setAccountPrivilege(int accountPrivilege) {
		this.accountPrivilege = accountPrivilege;
	}

	public int getAccountPrivilege() {
		return accountPrivilege;
	}

	public void setOperatorID(String operatorID) {
		this.operatorID = operatorID;
	}

	@SuppressLint("DefaultLocale")
	public String getOperatorID() {
		return String.format("%03d", Integer.valueOf(operatorID));
	}

	public void setAccountPhoto(String accountPhoto) {
		this.accountPhoto = accountPhoto;
	}

	public String getAccountPhoto() {
		return accountPhoto;
	}

	public Drawable getUserPhotoDrawable() {
		if (accountPhoto == null || accountPhoto.isEmpty())
			return null;
		return Drawable.createFromPath(Common.getRealImagePath(accountPhoto));
	}

	public int login() {
		return LineFactory.getOffLine().loginProceess(this);
	}
}
