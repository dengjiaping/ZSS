package com.koolyun.mis.finance;

public class CardIssuersAttribute {

	public static final String CIId = "CardIssuersId";
	public static final String CIName = "CardIssuersName";
	public static final String CName = "CardName";
	public static final String CBin = "CardBIN";
	public static final String CLen = "CardLen";
	public static final String CBrand = "CardBrand";
	public static final String CType = "CardType";

	String CardIssuersId = "";
	String CardIssuersName = "";
	String CardName = "";
	String CardBIN = "";
	String CardLen = "";
	String CardBrand = "";
	String CardType = "";

	public CardIssuersAttribute() {

	}

	public CardIssuersAttribute(String cardIssuersId, String cardIssuersName, String cardName, String cardBIN,
			String cardLen, String cardBrand, String cardType) {
		CardIssuersId = cardIssuersId;
		CardIssuersName = cardIssuersName;
		CardName = cardName;
		CardBIN = cardBIN;
		CardLen = cardLen;
		CardBrand = cardBrand;
		CardType = cardType;
	}

	public String getCardIssuersId() {
		return CardIssuersId;
	}

	public void setCardIssuersId(String cardIssuersId) {
		CardIssuersId = cardIssuersId;
	}

	public String getCardIssuersName() {
		return CardIssuersName;
	}

	public void setCardIssuersName(String cardIssuersName) {
		CardIssuersName = cardIssuersName;
	}

	public String getCardName() {
		return CardName;
	}

	public void setCardName(String cardName) {
		CardName = cardName;
	}

	public String getCardBIN() {
		return CardBIN;
	}

	public void setCardBIN(String cardBIN) {
		CardBIN = cardBIN;
	}

	public String getCardLen() {
		return CardLen;
	}

	public void setCardLen(String cardLen) {
		CardLen = cardLen;
	}

	public String getCardBrand() {
		return CardBrand;
	}

	public void setCardBrand(String cardBrand) {
		CardBrand = cardBrand;
	}

	public String getCardType() {
		return CardType;
	}

	public void setCardType(String cardType) {
		CardType = cardType;
	}

}
