package com.cnebula.um.service;

public class CardCashMapEntry {
	
	/**
	<entry cardType="11"  validRange= "2"  cashType = "62"  cashValue="400.00" />
	*/
	
	String cardType ; 
	
	String validRange = "2" ; 
	
	String cashType ; 
	
	String cashValue ;

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getValidRange() {
		return validRange;
	}

	public void setValidRange(String validRange) {
		this.validRange = validRange;
	}

	public String getCashType() {
		return cashType;
	}

	public void setCashType(String cashType) {
		this.cashType = cashType;
	}

	public String getCashValue() {
		return cashValue;
	}

	public void setCashValue(String cashValue) {
		this.cashValue = cashValue;
	} 
	
	
	
	

}
