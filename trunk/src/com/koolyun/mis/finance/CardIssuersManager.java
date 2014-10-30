package com.koolyun.mis.finance;

import android.database.Cursor;

import com.koolyun.mis.core.ManagerBase;

public class CardIssuersManager extends ManagerBase {
	public static final String CARDISSUERSTABLE = "CardIssuers";

	/**
	 * @param bankID
	 * @return the first item find in db,where CardIssuersId equals only
	 *         CardIssuersName is useful
	 */

	public static CardIssuersAttribute getCardIssuersAttributeByID(String bankID) {
		CardIssuersAttribute tmpCardIssuersAttribute = new CardIssuersAttribute();
		String[] columns = null;
		String[] selectionArgs = null;
		Cursor result = msqlitedb.query(CARDISSUERSTABLE, columns, "CardIssuersId='" + bankID + "'", selectionArgs,
				null, null, null);
		try {
			if (result.moveToFirst()) {
				tmpCardIssuersAttribute.setCardIssuersId(result.getString(result
						.getColumnIndex(CardIssuersAttribute.CIId)));
				tmpCardIssuersAttribute.setCardIssuersName(result.getString(result
						.getColumnIndex(CardIssuersAttribute.CIName)));
				tmpCardIssuersAttribute
						.setCardName(result.getString(result.getColumnIndex(CardIssuersAttribute.CName)));
				tmpCardIssuersAttribute.setCardBIN(result.getString(result.getColumnIndex(CardIssuersAttribute.CBin)));
				tmpCardIssuersAttribute.setCardLen(result.getString(result.getColumnIndex(CardIssuersAttribute.CLen)));
				tmpCardIssuersAttribute.setCardBrand(result.getString(result
						.getColumnIndex(CardIssuersAttribute.CBrand)));
				tmpCardIssuersAttribute
						.setCardType(result.getString(result.getColumnIndex(CardIssuersAttribute.CType)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			result.close();
		}
		return tmpCardIssuersAttribute;
	}

	public static CardIssuersAttribute getCardIssuersAttributeByBidAndCno(String bankID, String cardNo) {
		CardIssuersAttribute tmpCardIssuersAttribute = new CardIssuersAttribute();
		if (cardNo.length() < 16 || cardNo.length() > 19)
			return tmpCardIssuersAttribute;
		String[] columns = null;
		String[] selectionArgs = new String[2];
		selectionArgs[0] = bankID;
		selectionArgs[1] = cardNo.substring(0, 5);

		Cursor result = msqlitedb.query(CARDISSUERSTABLE, columns, "CardIssuersId=? and CardBIN=?", selectionArgs,
				null, null, null);
		try {
			if (result.moveToFirst()) {
				tmpCardIssuersAttribute.setCardIssuersId(result.getString(result
						.getColumnIndex(CardIssuersAttribute.CIId)));
				tmpCardIssuersAttribute.setCardIssuersName(result.getString(result
						.getColumnIndex(CardIssuersAttribute.CIName)));
				tmpCardIssuersAttribute
						.setCardName(result.getString(result.getColumnIndex(CardIssuersAttribute.CName)));
				tmpCardIssuersAttribute.setCardBIN(result.getString(result.getColumnIndex(CardIssuersAttribute.CBin)));
				tmpCardIssuersAttribute.setCardLen(result.getString(result.getColumnIndex(CardIssuersAttribute.CLen)));
				tmpCardIssuersAttribute.setCardBrand(result.getString(result
						.getColumnIndex(CardIssuersAttribute.CBrand)));
				tmpCardIssuersAttribute
						.setCardType(result.getString(result.getColumnIndex(CardIssuersAttribute.CType)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			result.close();
		}
		return tmpCardIssuersAttribute;
	}
}
