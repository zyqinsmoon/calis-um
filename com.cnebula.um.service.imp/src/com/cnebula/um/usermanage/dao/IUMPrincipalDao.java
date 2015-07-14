package com.cnebula.um.usermanage.dao;

import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.ejb.saas.UMTenant;

/**
 * UMPrincipal实体类操作接口
 * @author sandor
 * @since 2009-07-30
 * 
 */
public interface IUMPrincipalDao {

	public void add(UMPrincipal ump) throws EntityCRUDException;
	
	public void update(UMPrincipal ump) throws EntityCRUDException;
	
	public void deleteByLoginId(String loginId) throws EntityCRUDException;
	
	public UMPrincipal getByLoginId(String loginId);
	
	public UMPrincipal getByEmail(String email);
	
	public Organization getOrganizationByCode(String code);
	
	public UMTenant getTenantByTenantID(String tenantId);
}
