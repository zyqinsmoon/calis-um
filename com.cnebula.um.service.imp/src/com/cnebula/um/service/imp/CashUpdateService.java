package com.cnebula.um.service.imp;

import java.math.BigDecimal;
import java.util.List;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.manage.EntityPermissionExcuteStatus;
import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.common.ejb.manage.IEntityCRUDListener;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.um.ejb.entity.usr.Cash;

@EasyService
public class CashUpdateService implements IEntityCRUDListener{

	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService entityCRUDService;
	
	protected void activate(ComponentContext ctx) {
		entityCRUDService.registerEntityCRUDListener(Cash.class.getName(), this);
	}
	
	
	public void afterCRUDEvent(EntityPropertityItemStatus rootOpInfo)throws EntityCRUDException{
			Cash cash = (Cash)rootOpInfo.getItem();
			BigDecimal totalCashValue = null;
			if (cash.getPrincipal().getTotalCashValue() == null){
				cash.getPrincipal().setTotalCashValue(new BigDecimal(0));
			}
			if(rootOpInfo.isCreate()){
				totalCashValue = cash.getPrincipal().getTotalCashValue().add(cash.getValue());
			}else if(rootOpInfo.isUpdate()&&cash.getStatus()==Cash.CASH_DEBIT){ //退还押金
				totalCashValue = cash.getPrincipal().getTotalCashValue().subtract(cash.getValue());
			}
			cash.getPrincipal().setTotalCashValue(totalCashValue );
			entityCRUDService.update(cash.getPrincipal());
	}

	public void afterCRUDSuccessEvent(EntityPropertityItemStatus rootOpInfo,
			List<EntityPermissionExcuteStatus> permissionStatusForLog) {
	}

	public void beforeCRUDEvent(EntityPropertityItemStatus rootOpInfo) {
	}

}
