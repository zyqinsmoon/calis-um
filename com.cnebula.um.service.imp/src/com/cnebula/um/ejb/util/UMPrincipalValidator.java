package com.cnebula.um.ejb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.common.ejb.manage.IEntityValidator;
import com.cnebula.common.ejb.manage.ValidateException;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;

@EasyService(interfaces = IEntityValidator.class, properties = { @Property(name = "id", value = "UMPrincipalValidator") })
public class UMPrincipalValidator implements IEntityValidator {
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDService crudService;
	public static final String CURRENT_USER = "currentUser";
	
	public void validate(Object entity, Map<String, Object> context)
			throws ValidateException {

		UMPrincipal uMPrincipal = (UMPrincipal) entity;
		
		ITenant tenant=((UMPrincipal)context.get(CURRENT_USER)).getTenant();
		String tenantId="";
		if(tenant!=null){
			tenantId=tenant.getTenantId();
		}
		List<UMPrincipal> users=new ArrayList<UMPrincipal>();
		if(tenantId!=null&&tenantId.length()>0){
			users=crudService.query(UMPrincipal.class, EntityQuery.createExactQuery("loginId", tenantId+":"+uMPrincipal.getLocalLoginId()));
		}else{
			users=crudService.query(UMPrincipal.class, EntityQuery.createExactQuery("localLoginId", uMPrincipal.getLocalLoginId()));
		}
		
		if(users.size()>0&&!users.get(0).getId().equals(uMPrincipal.getId())){
			throw new ValidateException("用户登录ID："+uMPrincipal.getLoginId()+"已存在，请重新输入！");	
		}
	}

}
