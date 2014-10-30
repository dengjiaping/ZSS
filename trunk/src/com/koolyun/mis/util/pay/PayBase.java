package com.koolyun.mis.util.pay;

public abstract class PayBase {

	public enum PAYTYPE {
		PAY_BY_CASH(0),
		// now PAY_BY_EMV mode not used, use PAY_BY_MSR to indicate both
		PAY_BY_MSR(1), PAY_BY_EMV(2);
		private int code; // 状态码值

		PAYTYPE(int code) { // 非public构造方法
			this.code = code;
		}

		public int toInt() {
			return code;
		}

		@Override
		public String toString() {
			return String.valueOf(code);
		}
	};

	private PAYTYPE mType;
	private String mPayMoney;

	public PAYTYPE getmType() {
		return mType;
	}

	protected void setmType(PAYTYPE mType) {
		this.mType = mType;
	}

	public String getPayMoney() {
		return mPayMoney;
	}

	public void setPayMoney(String mPayMoney) {
		this.mPayMoney = mPayMoney;
	}

	public abstract void payCancel();

	public abstract void payConfirmed();

}
