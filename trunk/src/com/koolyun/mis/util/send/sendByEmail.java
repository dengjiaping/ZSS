package com.koolyun.mis.util.send;

import com.koolyun.mis.util.SendReceiptBase;

public class sendByEmail implements SendReceiptBase {

	@SuppressWarnings("unused")
	private String mailAddress;

	public sendByEmail(String email) {
		mailAddress = email;
	}

	@Override
	public void receiptSend() {
		// TODO Auto-generated method stub

	}

}
