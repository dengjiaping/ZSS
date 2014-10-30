package com.koolyun.mis.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.koolyun.mis.core.order.OrdersToPaymentType;

public class JsonBean {

	private JSONObject jsonOrder = new JSONObject();
	private JSONArray jsonOrderContent = new JSONArray();
	private JSONArray jsonOrderContentAttribute = new JSONArray();
	private JSONArray jsonOrderContentOnsale = new JSONArray();

	public void SetCreateTime(long time) throws JSONException {
		jsonOrder.put("createTime", time);
	}

	public void SetOrderStatus(int status) throws JSONException {
		jsonOrder.put("orderStatus", status);
	}

	public void SetPaymentType(List<OrdersToPaymentType> list) throws JSONException {

		JSONArray jsonPaymentType = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			OrdersToPaymentType ps = list.get(i);
			int paymenttype = ps.getPaymentTypeID();
			String cash = ps.getAmount();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("paymentTypeId", paymenttype);
			jsonObj.put("price", cash);
			jsonPaymentType.put(jsonObj);
		}
		jsonOrder.put("OrdersToPaymentType", jsonPaymentType);
	}

	public void SetOrderProcess(int mode, long createtime, String traceno, String referenceno, String authno,
			long cleardate, String ackno, String price, int longitude, int latitude) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("orderProcessModeId", mode);
		jsonObj.put("createTime", createtime);
		jsonObj.put("traceNo", traceno);
		jsonObj.put("referenceNo", referenceno);
		jsonObj.put("authorizationNo", authno);
		jsonObj.put("clearingDate", cleardate);
		jsonObj.put("ackNo", ackno);
		jsonObj.put("price", price);
		jsonObj.put("longitude", longitude);
		jsonObj.put("latitude", latitude);
		jsonOrder.put("OrderProcess", jsonObj);
	}

	public void SetBankCard(String cardno, String validThrough, String owner, String phone, String email,
			String signature) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("bankCardNo", cardno);
		jsonObj.put("validThrough", validThrough);
		jsonObj.put("cardOwnerName", owner);
		jsonObj.put("telephoneNo", phone);
		jsonObj.put("emailInfo", email);
		jsonObj.put("signature", signature);
		jsonOrder.put("BankCard", jsonObj);
	}

	public void AddOrderContentAttributeToArray(int subattr, String priceaffect) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("productSubAttributeId", subattr);
		jsonObj.put("priceAffect", priceaffect);
		jsonOrderContentAttribute.put(jsonObj);
	}

	public void AddOrderContentOnsaleToArray(int onsaleid, String value) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("onsaleId", onsaleid);
		jsonObj.put("value", value);
		jsonOrderContentOnsale.put(jsonObj);
	}

	public void AddOrderContentToArray(int productid, int count, String amount) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("productId", productid);
		jsonObj.put("count", count);
		jsonObj.put("price", amount);
		jsonObj.put("OrderContentToAttribute", jsonOrderContentAttribute);
		jsonObj.put("OrderContentToOnsale", jsonOrderContentOnsale);
		jsonOrderContent.put(jsonObj);
	}

	public void SetOrderContent() throws JSONException {
		jsonOrder.put("OrderContent", jsonOrderContent);
	}
}
