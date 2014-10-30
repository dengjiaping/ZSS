package com.koolyun.mis.core.user;

public class AccountPrivilege {

	private int accountPrivilege;
	private String name;
	private int value;

	public void setAccountPrivilege(int accountPrivilege) {
		this.accountPrivilege = accountPrivilege;
	}

	public int getAccountPrivilege() {
		return accountPrivilege;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPermission() {
		return value;
	}

	public void setPermission(int value) {
		this.value = value;
	}
}
