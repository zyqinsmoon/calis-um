package com.cnebula.um.service;

import java.util.List;
import java.util.Map;

import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.um.ejb.entity.perm.Role;
import com.cnebula.um.ejb.entity.perm.UserRule;

public interface IUserRoleBindingService {
	
	/**
	 * 
	 * @param create 是否创建用户规则
	 * @param rule 用户规则
	 * @param roleBinds Key表示Role的Id，Value表示Role是否与rule绑定
	 * @throws EntityCRUDException 
	 */
	public void bind(boolean create, UserRule rule, Map<Role, Boolean> roleBinds) throws Exception;
	
	/**
	 * 获取非notCategeory角色类别的角色列表
	 * @param notCategeory 角色类别（0为读者，1为馆员，此处为反向使用）
	 * @return 角色列表
	 */
	public List<Role> getNotCategeoryRoles(String  notCategeory);
	
	/**
	 * 获取categeory角色类别的角色列表
	 * @param categeory 要获取的categeory类型
	 * @return	角色列表
	 */
	public List<Role> getCategeoryRoles(String categeory);
	
	
	/**
	 * 获取所有角色列表(过滤xml中配置的角色)
	 * @return	角色列表
	 */
	public List<Role> getAllRolesInDb();
	
	
	/**
	 * 删除用户规则
	 * @param ids UserRule的id列表
	 */
	public void deleteUserRule(List<Object> ids) throws Exception ;
	

}