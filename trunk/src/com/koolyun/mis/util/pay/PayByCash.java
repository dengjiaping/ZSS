package com.koolyun.mis.util.pay;

public class PayByCash extends PayBase {

	public PayByCash(String mPayMoney) {
		this.setmType(PayBase.PAYTYPE.PAY_BY_CASH);
		this.setPayMoney(mPayMoney);
	}

	@Override
	public void payCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void payConfirmed() {
		// TODO Auto-generated method stub

	}

}
