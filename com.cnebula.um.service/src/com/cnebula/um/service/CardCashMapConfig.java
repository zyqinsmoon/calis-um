package com.cnebula.um.service;

import java.util.HashMap;
import java.util.Map;

import com.cnebula.common.annotations.xml.XMLMapping;

public class CardCashMapConfig {

	Map<String, CardCashMapEntry> cardCashMap = new HashMap<String, CardCashMapEntry>();

	@XMLMapping(tag= "cardCashMapConfig", childTag="cardCashMapEntry", keyTag="cardType")
	public Map<String, CardCashMapEntry> getCardCashMap() {
		return cardCashMap;
	}

	public void setCardCashMap(Map<String, CardCashMapEntry> cardCashMap) {
		this.cardCashMap = cardCashMap;
	}
	
	
	
}
