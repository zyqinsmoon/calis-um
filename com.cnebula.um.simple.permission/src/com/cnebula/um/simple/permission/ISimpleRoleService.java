package com.cnebula.um.simple.permission;

import java.util.List;

import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.um.ejb.entity.perm.Role;


public interface ISimpleRoleService {
	
	//public ISimplePermission getSimplePermissionGroups(String tenantId,String userType);
	
//	@ParamList({"tenantId"})
//	public List<? extends IDynamicRole> getRoleList(String tenantId);
//	
//	@ParamList({"tenantId"})
//	public List<UserRule> getUserRuleList(String tenantId);
//	
//	@ParamList({"tenantId"})
//	public List<UserRule> getUserBlackRuleList(String tenantId);
//	
//	@ParamList({"tenantId"})
//	public List<? extends IDynamicRole> getBlackRoleList(String tenantId);
	
	public void saveRoleByEm(Role role);
	
	public void saveRoleByCRUD(EntityPropertityItemStatus rootOpInfo);
	
	public List<UserRoleGroup> getUserRoleGroupList(String tenantId,String groupName);
	
}
