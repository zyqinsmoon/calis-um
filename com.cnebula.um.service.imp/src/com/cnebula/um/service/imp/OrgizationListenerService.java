package com.cnebula.um.service.imp;

import java.util.List;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.manage.EntityPermissionExcuteStatus;
import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.common.ejb.manage.IEntityCRUDListener;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.um.ejb.entity.usr.Organization;

/**
 * @author zhangyx
 * @author chenfeng
 * 完整机构代码已经从扩张属性2变成一个独立的属性
 * version : 1.2
 */
@EasyService
@Deprecated
public class OrgizationListenerService implements IEntityCRUDListener {

	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService crudPrivateService;
	
	public void afterCRUDEvent(EntityPropertityItemStatus status)  throws EntityCRUDException {
		
	}

	public void afterCRUDSuccessEvent(EntityPropertityItemStatus status,
			List<EntityPermissionExcuteStatus> list) throws EntityCRUDException {}

	public void beforeCRUDEvent(EntityPropertityItemStatus status)	throws EntityCRUDException {

		Organization o = (Organization)status.getItem();
		int type = status.getStatus();
		if (type == EntityPropertityItemStatus.CREATE){
			if (o.getParent() != null){
				o.setCompleteCode(o.getParent().getCode() + "." + o.getCode());
			}else{
				o.setCompleteCode(o.getCode());
			}
		}else if (type == EntityPropertityItemStatus.UPDATE){
			if (o.getParent() != null){
				o.setCompleteCode(o.getParent().getCode() + "." + o.getCode());
			}else{
				o.setCompleteCode(o.getCode());
			}
		}
	
	}

	protected void activate(ComponentContext ctx) {
		crudPrivateService.registerEntityCRUDListener(Organization.class.getName(), this);
	}
	
}
