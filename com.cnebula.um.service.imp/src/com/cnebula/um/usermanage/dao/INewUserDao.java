package com.cnebula.um.usermanage.dao;

import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.um.ejb.entity.usr.NewUserBean;
import com.cnebula.um.ejb.entity.usr.Organization;

public interface INewUserDao {
	
	public void add(NewUserBean newUserBean) throws EntityCRUDException;
	
	public void update(NewUserBean newUserBean) throws EntityCRUDException;
	
	public void deleteByActiveCode(String activeCode) throws EntityCRUDException;
	
	public void deleteByLoginId(String loginId) throws EntityCRUDException;
	
	public NewUserBean getByActiveCode(String activeCode);
	
	public NewUserBean getByLoginId(String loginId);
	
	public Organization getOrganizationByCode(String code);
}
