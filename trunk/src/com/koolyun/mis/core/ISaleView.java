package com.koolyun.mis.core;

import java.util.List;

import com.koolyun.mis.core.order.OrderContent;

public interface ISaleView {

	public void setup();

	public void setItems(List<OrderContent> mList);

	public void addItems(List<OrderContent> mList);

	public void updateItems(List<OrderContent> mList);

	public void removeItems(List<OrderContent> mList);

	public void removeAllItems();
}
