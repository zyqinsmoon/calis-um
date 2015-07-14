package com.cnebula.um.service;

import com.cnebula.common.saas.ITenant;

public interface ITenantQueryService {
	
	public boolean isTenantExist(String tenantId) ; 
	
	public ITenant getTenant(String tenantId);

}
