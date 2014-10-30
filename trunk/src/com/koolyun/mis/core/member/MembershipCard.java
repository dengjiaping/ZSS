package com.koolyun.mis.core.member;

public class MembershipCard {
	private String cardNo;
	private int memberNo;
	private int type_id;
	private float balance;
	private float onsale;
	private String deadLine;
	private String remark;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public float getOnsale() {
		return onsale;
	}

	public void setOnsale(float onsale) {
		this.onsale = onsale;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public MembershipCard(String cardNo, int memberNo, int type_id, float balance, float onsale, String deadLine,
			String remark) {
		this.balance = balance;
		this.cardNo = cardNo;
		this.deadLine = deadLine;
		this.memberNo = memberNo;
		this.onsale = onsale;
		this.remark = remark;
		this.type_id = type_id;
	}
}
