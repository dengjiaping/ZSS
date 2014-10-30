package com.koolyun.mis.core.order;

import java.util.ArrayList;
import java.util.List;

import com.koolyun.mis.util.BasicArithmetic;

import android.util.Log;

public class OrderContentListCompare {

	// static public int compare(OrderContentData old,OrderContentData current)

	public static List<OrderChangeInfo> compare(List<OrderContentData> old, List<OrderContentData> current) {
		List<OrderChangeInfo> rlist = new ArrayList<OrderChangeInfo>();

		for (int i = 0; i < old.size(); i++) {

			IndexAndRet tmp = compareOneInList(old.get(i), current, rlist);
			int ret = tmp.getRet();
			int j = tmp.getIndex();

			if (ret == OrderContentDataCompare.ALL_THE_SAME) {
				rlist.add(new OrderChangeInfo(old.get(i).getProductId(), old.get(i).getOnePrice(), i, j, true, false,
						0, OrderChangeInfo.NORMAL, "0.00" + "."));
			} else if (ret == OrderContentDataCompare.ONLY_COUNT_CHANGE) {
				int count = current.get(j).getCount() - old.get(i).getCount();
				rlist.add(new OrderChangeInfo(old.get(i).getProductId(), old.get(i).getOnePrice(), i, j, false, true,
						count, OrderChangeInfo.NORMAL, BasicArithmetic.sub(current.get(j).getItemAmount(), old.get(i)
								.getItemAmount())));
			} else if (ret > OrderContentDataCompare.NOT_SAME && ret < OrderContentDataCompare.ONLY_COUNT_CHANGE) {
				int count = current.get(j).getCount() - old.get(i).getCount();
				rlist.add(new OrderChangeInfo(old.get(i).getProductId(), old.get(i).getOnePrice(), i, j, false, false,
						count, OrderChangeInfo.CHANGE, BasicArithmetic.sub(current.get(j).getItemAmount(), old.get(i)
								.getItemAmount())));
			} else if (ret == OrderContentDataCompare.NOT_SAME) {
				int count = old.get(i).getCount();
				rlist.add(new OrderChangeInfo(old.get(i).getProductId(), old.get(i).getOnePrice(), i, -1, false, false,
						-count, OrderChangeInfo.DELETE, BasicArithmetic.sub("0.00", old.get(i).getItemAmount())));
			}

		}

		for (int j = 0; j < current.size(); j++) {

			if (getIsNewInList(rlist, j))
				continue;
			int count = current.get(j).getCount();
			rlist.add(new OrderChangeInfo(current.get(j).getProductId(), current.get(j).getOnePrice(), -1, j, false,
					false, count, OrderChangeInfo.ADD, BasicArithmetic.sub(current.get(j).getItemAmount(), "0.00")));

		}

		for (int i = 0; i < rlist.size(); i++) {
			Log.d("  ", rlist.get(i).toString());
		}
		// int count = rlist.size();
		// for(int i = 0 ; i < count ; i++)
		// {
		// getOrderContentData(rlist.get(i),old,current);
		// }
		//
		return rlist;

	}

	static private IndexAndRet compareOneInList(OrderContentData oldone, List<OrderContentData> current,
			List<OrderChangeInfo> rlist) {
		int ret = 0;
		int index = -1;
		for (int j = 0; j < current.size(); j++) {
			if (getIsNewInList(rlist, j))
				continue;
			int tmp = OrderContentDataCompare.compare(oldone, current.get(j));
			if (tmp > ret) {
				ret = tmp;
				index = j;
			}
		}
		return new IndexAndRet(index, ret);
	}

	private static boolean getIsNewInList(List<OrderChangeInfo> rlist, int newIndex) {
		if (rlist == null || rlist.isEmpty())
			return false;
		for (int i = 0; i < rlist.size(); i++) {
			if (rlist.get(i).getNewIndex() == newIndex)
				return true;
		}
		return false;
	}

}
