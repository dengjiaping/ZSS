package com.koolyun.mis.core;

import java.util.List;

import com.koolyun.mis.core.order.OrderContentData;

public interface IProductSelector {
	void addNewOrderContent(List<OrderContentData> mList);
}