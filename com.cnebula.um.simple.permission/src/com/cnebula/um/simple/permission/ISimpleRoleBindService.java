package com.cnebula.um.simple.permission;

import java.util.List;

public interface ISimpleRoleBindService {
	public List<UserRoleGroup> getUserRoleGroupList(String tenantID);
	public void registerUserRoleGroup(UserRoleGroup userRoleGroup,String tenantID);
	public List<UserRoleGroup> getUserRoleGroupList(String groupName,String tenantID);
}
