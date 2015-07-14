package com.cnebula.um.usermanage.dao;

import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.um.ejb.entity.usr.ResetPasswordBean;

public interface IResetPasswordDao {

	public void add(ResetPasswordBean resetPassword) throws EntityCRUDException;
	
	public void update(ResetPasswordBean resetPassword) throws EntityCRUDException;
	
	public void deleteByEmail(String emailAddr) throws EntityCRUDException;
	
	public ResetPasswordBean getByActiveCode(String activeCode);
	
	public ResetPasswordBean getByEmail(String email);
}
