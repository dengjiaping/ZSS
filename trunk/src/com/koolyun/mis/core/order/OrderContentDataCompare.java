package com.koolyun.mis.core.order;

import java.util.List;

import com.koolyun.mis.core.product.ProductSubAttribute;

public class OrderContentDataCompare {

	public static final int ALL_THE_SAME = 100;
	public static final int ONLY_COUNT_CHANGE = 90;

	public static final int PRODUCT_SAME = 50;
	public static final int COUNT_SAME = 10;

	public static final int SUBATTR_SAME = 2;// MAX 10
	public static final int SUBATTR_MAX = 10;

	public static final int ONSALE_SAME = 1;// MAX 5
	public static final int ONSALE_MAX = 5;

	public static final int REMARK_SAME = 5;

	public static final int NOT_SAME = 0;

	// 比较2个商品，完全一样返回 ALL_THE_SAME
	// 商品ID不一样返回 NOT_SAME
	// 其他的计算相似度 返回相似度之和，其中2个列表的相似度为 min(N*权,MAX)

	static public int compare(OrderContentData old, OrderContentData current) {
		int power = NOT_SAME;
		if (old.getProductId() != current.getProductId())
			return power;
		else if (old.getProductId() == 0) {
			if (old.getOnePrice().equals(current.getOnePrice())) {
				power += PRODUCT_SAME;
			} else {
				return power;
			}
		} else
			power += PRODUCT_SAME;

		if (old.getCount() == current.getCount()) {
			power += COUNT_SAME;
		}

		// if((old.getmOrderContentRemark() == null &&
		// current.getmOrderContentRemark() == null) ||
		// (old.getmOrderContentRemark() == null &&
		// current.getmOrderContentRemark().getRemark().isEmpty()) ||
		// (old.getmOrderContentRemark().getRemark().isEmpty() &&
		// current.getmOrderContentRemark() == null) ||
		// ((old.getmOrderContentRemark() != null &&
		// current.getmOrderContentRemark() != null) &&
		// old.getmOrderContentRemark().getRemark().equals(current.getmOrderContentRemark().getRemark())))
		// {
		// power += REMARK_SAME;
		// }
		power += REMARK_SAME;
		power += compareSubAttr(old.getProductSubAttrList(), current.getProductSubAttrList());
		power += compareOnSale(old.getOnsale(), current.getOnsale());

		if (power == (PRODUCT_SAME + COUNT_SAME + SUBATTR_MAX + ONSALE_MAX + REMARK_SAME))
			return ALL_THE_SAME;
		else if (power == (PRODUCT_SAME + SUBATTR_MAX + ONSALE_MAX + REMARK_SAME))
			return ONLY_COUNT_CHANGE;
		else
			return power;

	}

	static private int compareSubAttr(List<ProductSubAttribute> old, List<ProductSubAttribute> current) {
		if ((old == null || old.isEmpty()) && (current == null || current.isEmpty())) {
			return SUBATTR_MAX;
		}
		int power = 0;
		if (old == null || current == null)
			return power;

		for (int i = 0; i < old.size(); i++) {
			for (int j = 0; j < current.size(); j++) {
				if (old.get(i).getProductSubAttributeId() == current.get(j).getProductSubAttributeId()) {
					power += SUBATTR_SAME;
				}
			}
		}
		if (power == old.size() * SUBATTR_SAME && old.size() == current.size())
			return SUBATTR_MAX;
		else
			return 0;
	}

	static private int compareOnSale(List<Onsale> old, List<Onsale> current) {
		return ONSALE_MAX;
		/*
		 * if((old == null || old.isEmpty()) && (current == null ||
		 * current.isEmpty())) { return ONSALE_MAX; } int power = 0; if(old ==
		 * null || current == null) return power;
		 * 
		 * for(int i = 0 ; i < old.size() ; i++) { for(int j = 0 ; j <
		 * current.size() ; j++) { if(old.get(i).getOnsaleID() ==
		 * current.get(j).getOnsaleID()) { power += ONSALE_SAME; } } } return
		 * power>ONSALE_MAX?ONSALE_MAX:power;
		 */

	}
}
