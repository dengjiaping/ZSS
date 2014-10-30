package com.koolyun.mis.line;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.core.user.AccountManager;
import com.koolyun.mis.util.MyLog;
import com.koolyun.mis.util.communicate.OperateTimer;

public class Online extends Line {
	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	String sid = "sid=n85isr8b3u3toqtlim0lb1s9l0";
	String cpuid = null;

	@Override
	public void pollTimerJob(List<OperateTimer> ot) {
		// 更改各个回收定时器
		if (ot == null || ot.isEmpty())
			return;

		try {
			ot.get(0).getPeriod();
			MyLog.e("polltimer");
			for (int i = 0; i < ot.size(); i++) {
				int time = ot.get(i).getPeriodTime();
				ot.get(i).reScheduleDefault(time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void orderTimerJob() {
		// 搜索orders表，回收

		// cpuid = getCpuId();
		// if(null == cpuid)
		// return;
		//
		// List<Orders> orderList0 =OrderManager.getOrdersForSync(0);
		// for (Orders orders : orderList0) {
		// int orderId = orders.getOrderID();
		// List<OrderProcess> orderProcesses =
		// OrderManager.getOrderProcessById1(orderId);
		// List<OrderCustomerInfo> orderCustomerInfos = new
		// ArrayList<OrderCustomerInfo>();
		// List<BankCard> bankCard = new ArrayList<BankCard>();
		// for (OrderProcess orderProcess : orderProcesses) {
		// int orderProcessesId =orderProcess.getOrderProcessId();
		// OrderCustomerInfo oci =
		// OrderManager.getOrderCustomerInfoByProcessId(orderProcessesId);
		// orderCustomerInfos.add(oci);
		// int bankCardid = oci.getBankCardId();
		// BankCard bc = OrderManager.getBankCardById(bankCardid);
		// bankCard.add(bc);
		// }
		// //List<Address> addresses = new ArrayList<Address>();
		// List<OrdersToPaymentType> ordersToPaymentTypes =
		// OrderManager.getOrdersPaymentTypeById(orderId);
		// List<OrderContent> orderContents =
		// OrderManager.getOrderContentByOrderId1(orderId);
		// List<OrderContentToAttribute> orderContentToAttribute = new
		// ArrayList<OrderContentToAttribute>();
		// List<OrderContentToOnsale> orderContentToOnsale = new
		// ArrayList<OrderContentToOnsale>();
		// for (OrderContent orderContent : orderContents) {
		// int orderContentId = orderContent.getOrderContentId();
		// List<OrderContentToAttribute> octa = OrderManager.
		// getOrderContentToAttributesByOrderContentId(orderContentId);
		// orderContentToAttribute.addAll(octa);
		// List<OrderContentToOnsale> octo =OrderManager.
		// getOrderContentToOnsalesByOrderContentId(orderContentId);
		// orderContentToOnsale.addAll(octo);
		// }
		//
		// String orderString = gson.toJson(orders);
		// Type ordersToPaymentType = new
		// TypeToken<List<OrdersToPaymentType>>(){}.getType();
		// String otptString = gson.toJson(ordersToPaymentTypes,
		// ordersToPaymentType);
		// //Type orderProcessType = new
		// TypeToken<List<OrderProcess>>(){}.getType();
		// String opString = gson.toJson(orderProcesses);
		// Type orderCustomerInfoType = new
		// TypeToken<List<OrderCustomerInfo>>(){}.getType();
		// String ociString = gson.toJson(orderCustomerInfos,
		// orderCustomerInfoType);
		// Type orderContentType = new
		// TypeToken<List<OrderContent>>(){}.getType();
		// String ocString = gson.toJson(orderContents, orderContentType);
		// Type octaType = new
		// TypeToken<List<OrderContentToAttribute>>(){}.getType();
		// String octaString = gson.toJson(orderContentToAttribute, octaType);
		// Type onsaleType = new TypeToken<List<Onsale>>(){}.getType();
		// String octoString = gson.toJson(orderContentToOnsale, onsaleType);
		// Type bankcardType = new TypeToken<List<BankCard>>(){}.getType();
		// String bcString = gson.toJson(bankCard, bankcardType);
		//
		// String orderstring = cpuid + "&order="+ orderString +"&otpt="
		// +otptString +"&op=" +opString
		// +"&oci=" +ociString+"&oc=" + ocString +"&octa=" +octaString
		// +"&octo=" +octoString +"&bc=" +bcString;
		// String ret = NetworkCommunication.postRequest("order/create.php",
		// orderstring);
		//
		// if(ret != null) {
		// Result result = gson.fromJson(ret, Result.class);
		// if(result.getRet() == 0) {
		// OrderManager.orderSynced(orders.getOrderID());
		// }
		// }
		// }
		//
		// List<Orders> ordersList = OrderManager.getOrdersForSync(1);
		// for (Orders order : ordersList) {
		// List<OrderProcess> orderProcesses =
		// OrderManager.getOrderProcessById1(order.getOrderID());
		//
		// String orderString = gson.toJson(order);
		// Type type = new TypeToken<List<OrderProcess>>(){}.getType();
		// String opString = gson.toJson(orderProcesses, type);
		//
		// String ret = NetworkCommunication.postRequest("order/modify.php",
		// cpuid + "&order="+ orderString +"&op=" +opString);
		//
		// if(ret != null) {
		// Result result = gson.fromJson(ret, Result.class);
		// if(result.getRet() == 0) {
		// OrderManager.orderSynced(order.getOrderID());
		// }
		// }
		// }

	}

	@Override
	public void productTimerJob() {
		// 搜索product表，回收
		//
		//
		// cpuid = getCpuId();
		// if(null == cpuid)
		// return;
		//
		// Gson gson = new
		// GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		// List<ProductCategory> pc =
		// ProductManager.getProductCategoryForSync();
		// for (ProductCategory productCategory : pc) {
		// String ret = null;
		// if(productCategory.getSyncFlag() == 0) {
		// ret = NetworkCommunication.postRequest(
		// "product/category/create.php",
		// cpuid + "&pc="+ gson.toJson(productCategory));
		// } else {
		// if(productCategory.getEnable() == 0) {
		// ret = NetworkCommunication.postRequest(
		// "product/category/delete.php",
		// cpuid + "&pcid="+ productCategory.getProductCategoryId());
		// }
		// }
		//
		// if(ret != null) {
		// Log.i("ret", ret);
		// Result result = gson.fromJson(ret, Result.class);
		// if(result.getRet() == 0) {
		// ProductManager.productCategorySynced(productCategory.getProductCategoryId());
		// }
		// }
		// }
		//
		//
		// List<Product> pdt = ProductManager.getProductForSync();
		// for (Product product : pdt) {
		// String ret = null;
		// if(product.getSyncFlag() == 0) {
		// ret = NetworkCommunication.postRequest(
		// "product/create.php", cpuid +"&pdt="+gson.toJson(product));
		// } else {
		// ret = NetworkCommunication.postRequest(
		// "product/modify.php", cpuid +"&pdt="+gson.toJson(product));
		// }
		//
		// if(ret != null) {
		// Log.i("ret", ret);
		// Result result = gson.fromJson(ret, Result.class);
		// if(result.getRet() == 0) {
		// ProductManager.productSynced(product.getProductID());
		// }
		// }
		// }
		// /**api:product/attribute/create**/
		//
		// List<ProductAttribute> pa =
		// ProductManager.getProductAttributeForSync();
		// for (ProductAttribute productAttribute : pa) {
		// String ret = null;
		// if(productAttribute.getSyncFlag() == 0) {
		// ret = NetworkCommunication.postRequest(
		// "product/attribute/create.php",
		// cpuid+"&pa="+gson.toJson(productAttribute));
		// } else {
		// if(productAttribute.getEnable() == 0) {
		// ret = NetworkCommunication.postRequest(
		// "product/attribute/delete.php",
		// cpuid+"&paid="+productAttribute.getProductAttributeID());
		// }
		// }
		//
		// if(ret != null) {
		// Log.i("ret", ret);
		// Result result = gson.fromJson(ret, Result.class);
		// if(result.getRet() == 0) {
		// ProductManager.productAttributeSynced(productAttribute.getProductAttributeID());
		// }
		// }
		// }
		//
		//
		// List<ProductSubAttribute> psa
		// =ProductManager.getProductsSubAttributesForSync();
		// for (ProductSubAttribute productSubAttribute : psa) {
		// String ret = null;
		// if(productSubAttribute.getSyncFlag() == 0) {
		// ret = NetworkCommunication.postRequest(
		// "product/sub_attribute/create.php",
		// cpuid+"&psa="+gson.toJson(productSubAttribute));
		// } else {
		// ret = NetworkCommunication.postRequest(
		// "product/sub_attribute/modify.php",
		// cpuid+"&psa="+gson.toJson(productSubAttribute));
		// }
		//
		// if(ret != null) {
		// Log.i("ret", ret);
		// Result result = gson.fromJson(ret, Result.class);
		// if(result.getRet() == 0) {
		// ProductManager.productSubAttributeSynced(
		// productSubAttribute.getProductSubAttributeId());
		// }
		// }
		// }
		//
		//
		// List<Onsale> onsale = ProductManager.getOnsaleForSync();
		// for (Onsale osl : onsale) {
		// String ret = null;
		// if(osl.getSyncFlag() == 0) {
		// ret = NetworkCommunication.postRequest(
		// "product/onsale/create.php",
		// cpuid+"&osl="+gson.toJson(osl));
		// } else {
		// if(osl.getEnable() == 0) {
		// ret = NetworkCommunication.postRequest(
		// "product/onsale/delete.php",
		// cpuid+"&oid="+osl.getOnsaleID());
		// }
		// }
		//
		// if(ret != null) {
		// Log.i("ret", ret);
		// Result result = gson.fromJson(ret, Result.class);
		// if(result.getRet() == 0) {
		// ProductManager.onsaleSynced(osl.getOnsaleID());
		// }
		// }
		// }
	}

	public int OfflineloginProceess(Account mAccount) {

		Account tmpAccount = AccountManager.getInstance().getAccountByName(mAccount.getAccount());

		if (tmpAccount != null && mAccount.getPass().equals(tmpAccount.getPass())) {
			return 2;
		}
		return 3;
	}

	@Override
	public int loginProceess(Account mAccount) {
		/*
		 * int loginResult = 0; { cpuid = getCpuId(); if(null == cpuid) { return
		 * 4; } else { String ret =
		 * NetworkCommunication.postRequest("control/login.php", cpuid + "&acc="
		 * + mAccount.getAccount() + "&pass=" + mAccount.getPass());
		 * 
		 * if (ret != null) { Gson gson = new Gson(); Result result =
		 * gson.fromJson(ret, Result.class); HttpSession session =
		 * gson.fromJson(ret, HttpSession.class);
		 * 
		 * if (result.getRet() == 0) { if (session.getSid() != null) {
		 * Log.i("session_id", session.getSid()); sid = session.getSid();
		 * mAccount.setAccountPrivilege(session.getPrivilege()); //
		 * AccountManager
		 * .getInstance().getmBaseInfo().setAllBaseInfo(session.getMerchantNo(),
		 * session.getTerminalNo(), session.getSid()); } loginResult = 2; } }
		 * else { loginResult = 1; } } } return loginResult;
		 */
		return OfflineloginProceess(mAccount);
	}
}
