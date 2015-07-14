package com.cnebula.um.usermanage.dao.impl;

import java.util.List;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.es.TxType;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.um.ejb.entity.usr.ResetPasswordBean;
import com.cnebula.um.usermanage.dao.IResetPasswordDao;

@EasyService(txType=TxType.CMT,properties={@Property(name="id", value="UMUserManage")})
public class ResetPasswordDao implements IResetPasswordDao{
	@ESRef(target="(unit=#{umunit})")
    private IEntityCRUDPrivateService entityCRUDService;
	
	public void add(ResetPasswordBean resetPassword) throws EntityCRUDException{
		entityCRUDService.create(resetPassword);
	}
	
	public void update(ResetPasswordBean resetPassword) throws EntityCRUDException{
		entityCRUDService.update(resetPassword);
	}
	
	public void deleteByEmail(String emailAddr) throws EntityCRUDException{
		EntityQuery q = EntityQuery.createExactQuery("emailaddr", emailAddr);
		List<ResetPasswordBean> rpblist = entityCRUDService.query(ResetPasswordBean.class, q);
		for(ResetPasswordBean rpb:rpblist)
			entityCRUDService.delete(rpb);
	}
	
	public ResetPasswordBean getByActiveCode(String activeCode){
		ResetPasswordBean resetPasswordBean = new ResetPasswordBean();
		EntityQuery q = EntityQuery.createExactQuery("activeCode", activeCode);
		List<ResetPasswordBean> rpblist = entityCRUDService.query(ResetPasswordBean.class, q);
		if(rpblist.size()<=0)
			return resetPasswordBean;
		else{
			resetPasswordBean = rpblist.get(0).clone();
		}
		return resetPasswordBean;
	}

	public ResetPasswordBean getByEmail(String email){
		ResetPasswordBean resetPasswordBean = new ResetPasswordBean();
		EntityQuery q = EntityQuery.createExactQuery("emailaddr", email);
		List<ResetPasswordBean> rpblist = entityCRUDService.query(ResetPasswordBean.class, q);
		if(rpblist.size()<=0)
			return resetPasswordBean;
		else{
			resetPasswordBean = rpblist.get(0).clone();
		}
		return resetPasswordBean;
	}
}
	
