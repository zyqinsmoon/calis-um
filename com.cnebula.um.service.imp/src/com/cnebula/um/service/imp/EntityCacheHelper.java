package com.cnebula.um.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.IEntityCacheHelper;
import com.cnebula.common.ejb.manage.perm.Operation;
import com.cnebula.common.ejb.manage.perm.OperationFieldContraint;
import com.cnebula.common.ejb.manage.perm.ResourceType;
import com.cnebula.common.log.ILog;
import com.cnebula.um.setup.ISetupService;


@EasyService
public class EntityCacheHelper implements IEntityCacheHelper {
	
	@ESRef
	ILog log;
	
	@ESRef(target="(unit=#{umunit})")
	IEntityCRUDPrivateService crudService;
	
	@ESRef
	ISetupService setupService;
	
	
	private Map<String,ResourceType> resourceTypeMap=new HashMap<String, ResourceType>();
	
	protected void activate(ComponentContext ctx) {
		log.debug("IEntityCacheHelper start!");
		refresh();
	}	
	
	public ResourceType queryResourceType(String entityLimitType) {
		return resourceTypeMap.get(entityLimitType);
	}

	public Operation queryOperation(String entityClassName, String operationName) {
		ResourceType resourceType = queryResourceType(entityClassName);
		if (resourceType != null) {
			for (Operation op : resourceType.getOperations()) {
				if (operationName.equals(op.getName())) {
					return op;
				}
			}
		}
		return null;
	}

	public void refresh() {
		List<ResourceType> resourceTypeList=crudService.queryAllResourceType();
		for(ResourceType rt:resourceTypeList){
//			for(Operation op:rt.getOperations()){
//				List<OperationFieldContraint> ops=crudService.query(OperationFieldContraint.class, EntityQuery.createExactQuery("operation.id", op.getId()));
//				if(ops.size()>0){
//					op.setOperationFieldContraints(ops);
//				}
//			}
			resourceTypeMap.put(rt.getEntityType(), rt);
		}
	}
}
