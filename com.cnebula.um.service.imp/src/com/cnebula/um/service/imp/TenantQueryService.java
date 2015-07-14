package com.cnebula.um.service.imp;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.saas.UMTenant;
import com.cnebula.um.service.ITenantQueryService;

@EasyService
public class TenantQueryService implements ITenantQueryService {
	
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService crudPrivateService;

	public ITenant getTenant(String tenantId) {
		return crudPrivateService.querySingle(UMTenant.class,
				EntityQuery.createExactQuery("tenantId", tenantId));
	}

	public boolean isTenantExist(String tenantId) {
		long count = 
			crudPrivateService.queryCount(UMTenant.class, 
					EntityQuery.createExactQuery("tenantId", tenantId));
		if(count>0){
			return true ; 
		}
		return false ; 
	}

}
