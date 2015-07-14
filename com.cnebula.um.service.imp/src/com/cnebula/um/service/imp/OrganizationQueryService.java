package com.cnebula.um.service.imp;

import java.util.List;

import com.cnebula.aas.util.PolicyRuleParseException;
import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.index.IPSegmentMemoryIndex;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.es.IRequest;
import com.cnebula.common.es.RequestContext;
import com.cnebula.common.security.auth.IAuthorizationService;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.service.IOrganizationQueryService;

@EasyService
public class OrganizationQueryService implements IOrganizationQueryService {

	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService crudPrivateService; 
	
	@ESRef
	protected IAuthorizationService authorizationService;
	
	public OrganizationQueryService() {
	}

	public List<Organization> getAllOrganizations(String loadRelations) throws EntityCRUDException {
		return crudPrivateService.query(Organization.class, null, EntityQuery.paresLazyFields(loadRelations));
	}

	@SuppressWarnings("unchecked")
	public Organization getOrganizationByIp(String ip) {
		return ((IPSegmentMemoryIndex<Organization>)authorizationService.getIpIndex()).searchLuck(ip);
	}
	
	@SuppressWarnings("unchecked")
	public Organization getOrganizationByRequest(){
		IRequest req = RequestContext.getRequest(); 
		if(req==null||req.getRemoteIp()==null){
			return null; 
		}else{
			return ((IPSegmentMemoryIndex<Organization>)authorizationService.getIpIndex()).searchLuck(req.getRemoteIp());
		}
	}

	public List<Organization> searchOrganization(String queryString, String sortString, String loadRelations) throws EntityCRUDException, PolicyRuleParseException {
		EntityQuery q = queryString == null ? null : EntityQuery.filterRule2EntityQuery(queryString);
		return crudPrivateService.query(Organization.class, q, EntityQuery.parseSortFieldInfoList(sortString), EntityQuery.paresLazyFields(loadRelations));
	}

	public List<Organization> getAllTopOrganization() {
		EntityQuery q = EntityQuery.createExactQuery("parent.id", null);
		return crudPrivateService.query(Organization.class	, q);
	}
}
