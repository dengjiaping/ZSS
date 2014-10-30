package com.koolyun.mis.util.pay;

public class PayByMsrCard extends PayBase {

	public PayByMsrCard() {
		this.setmType(PayBase.PAYTYPE.PAY_BY_MSR);
	}

	public PayByMsrCard(String mPayMoney) {
		this.setmType(PayBase.PAYTYPE.PAY_BY_MSR);
		this.setPayMoney(mPayMoney);
	}

	@Override
	public void payCancel() {

	}

	@Override
	public void payConfirmed() {

	}

}
