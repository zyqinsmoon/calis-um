package com.cnebula.um.service.imp;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.conf.IEasyServiceConfAdmin;
import com.cnebula.um.service.CardCashMapConfig;
import com.cnebula.um.service.IConfigAssistService;

@EasyService
public class ConfigAssistServiceImp implements IConfigAssistService {

	
	@ESRef
	protected IEasyServiceConfAdmin confAdmin;
	
	public CardCashMapConfig getCardCashMapConfig() {
		return confAdmin.get("cardCashMap", CardCashMapConfig.class);
	}

}
