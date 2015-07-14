package com.cnebula.um.aas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.TxIgnore;
import com.cnebula.common.ejb.impl.EntityManagerImp;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.log.ILog;
import com.cnebula.um.ejb.entity.perm.Application;
import com.cnebula.um.ejb.entity.perm.LegacyRole;
import com.cnebula.um.ejb.entity.perm.PermissionRule;
import com.cnebula.um.ejb.entity.perm.Role;
import com.cnebula.um.ejb.entity.perm.UserRule;
import com.cnebula.um.ejb.saas.UMTenant;
import com.cnebula.um.service.IRoleBuildHelper;


@EasyService
public class RoleBuildHelper implements IRoleBuildHelper {

	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService crudPrivateService;
	
	@ESRef(target="(unit=#{umunit})")
	EntityManager entityManager;
	
	@ESRef
	ILog log;
	
	@ESRef(target="(name=#{umds})")
	protected DataSource ds;
	
	@TxIgnore
	public List<Role> fetchAllCompiledRole(String tenantId) {
		((EntityManagerImp)entityManager).clearCache(Role.class,UserRule.class,PermissionRule.class,Application.class,UMTenant.class);
		List<Role> allRoles=this.stepGetRoleList(tenantId);
		List<Role> roles=new ArrayList<Role>();
		for(Role role:allRoles){
			if(role!=null){
				if (!role.isEnabled()){
					continue;
				}
				try {
					role=(Role) role.clone();
					role.compile();
				} catch (Throwable e) {
					log.warn("发现角色" + role.getName() + "中有非法的规则，该角色将被忽略", e);
				}
				roles.add(role);
			}
		}
		
		return roles;
	}
	/**
	 * 分步实例化角色
	 * @return
	 */
	@TxIgnore
	public List<Role> stepGetRoleList(String tenantId){
		List<Role> allRoles = new ArrayList<Role>();
		
		Connection conn = null;
		
		Map<String,List<String>> roleId_userRuleIds = new HashMap<String,List<String>>();
		Map<String,List<String>> roleId_permissionRuleIds = new HashMap<String,List<String>>();
		Map<String,UMTenant> umTenantMap = new HashMap<String,UMTenant>();		
		Map<String, String> tenantIdReverseMap=new HashMap<String, String>();//key: tenantId, value:id
		EntityQuery query=null;
		if(tenantId!=null&&tenantId.length()>0){
			query=EntityQuery.createExactQuery("tenantId", tenantId);
		}
		List<UMTenant> allUMTenant = crudPrivateService.query(UMTenant.class, query);
		for(UMTenant t : allUMTenant){
			umTenantMap.put(t.getId(), t);
			tenantIdReverseMap.put(t.getTenantId(), t.getId());
		}
		Map<String,Application> applicationMap = new HashMap<String,Application>();
		Map<String,Role> roleMap = new HashMap<String,Role>();
		Map<String,PermissionRule> permissionRuleMap = new HashMap<String,PermissionRule>();
		Map<String,UserRule> userRuleMap = new HashMap<String,UserRule>();
		try {			
			conn = ds.getConnection();
			roleId_userRuleIds = initRoleUserRuleMapped(conn);
			roleId_permissionRuleIds = initRolePermissionruleMapped(conn);
			applicationMap = initApplicationMapped(conn, tenantId, tenantIdReverseMap, umTenantMap);
			roleMap = initRoleMapped(conn, tenantId, tenantIdReverseMap, applicationMap, umTenantMap);
			permissionRuleMap = initPermissionRuleMapped(conn, tenantId, tenantIdReverseMap, umTenantMap);
			userRuleMap = initUserRule(conn, tenantId, tenantIdReverseMap, umTenantMap);
			
		} catch (SQLException e) {
			log.error("JDBC的方式获取角色的级联属性异常：", e);			
		}finally{
			try {
				if(conn != null)conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//mapping Role,UserRule,PermissionRule 
		for(Role r : roleMap.values()){
			String roleId = r.getId();
			List userRules = r.getUserRules();
			if(userRules == null) userRules = new ArrayList<UserRule>();
			List<String> userRuleIds = roleId_userRuleIds.get(roleId);
			if(userRuleIds != null){
				for(String id : userRuleIds){
					UserRule rule = userRuleMap.get(id);
					userRules.add(rule);
				}
				r.setUserRules(userRules);
			}
			List permissionRules = r.getPermissionRules();
			if(permissionRules == null)permissionRules = new ArrayList<PermissionRule>();
			List<String> permissionRuleIds = roleId_permissionRuleIds.get(roleId);
			if(permissionRuleIds != null){
				for(String id : permissionRuleIds){
					PermissionRule rule = permissionRuleMap.get(id);
					permissionRules.add(rule);
				}
				r.setPermissionRules(permissionRules);	
			}
			
			
			allRoles.add(r);
		}
		
		return allRoles;
	}
	/**
	 * 初始化角色和用户规则的映射关系
	 * @param conn
	 * @return
	 */
	@TxIgnore
	public Map<String,List<String>> initRoleUserRuleMapped(Connection conn){
		Map<String,List<String>> roleId_userRuleIds = new HashMap<String,List<String>>();
		String sql = "select ROLE_ID,RULE_ID from UM_Role_UserRule";
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()){
				String role_Id = rs.getString("ROLE_ID");
				String rule_Id = rs.getString("RULE_ID");
				if(roleId_userRuleIds.containsKey(role_Id)){
					List<String> list = roleId_userRuleIds.get(role_Id);
					list.add(rule_Id);
					roleId_userRuleIds.put(role_Id, list);
				}else{
					List<String> list = new ArrayList<String>();
					list.add(rule_Id);
					roleId_userRuleIds.put(role_Id, list);
				}
			}
		}catch(Exception e){
			log.error("JDBC的方式获取角色与用户规则的映射关系异常：", e);
		}finally{
			try {
				if(rs != null)rs.close();
				if(pstm != null)pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return roleId_userRuleIds;
	}
	/**
	 * 初始化角色和权限规则的映射关系
	 * @param conn
	 * @return
	 */
	@TxIgnore
	public Map<String,List<String>> initRolePermissionruleMapped(Connection conn){
		Map<String,List<String>> roleId_permissionRuleIds = new HashMap<String,List<String>>();
		String sql = "select ROLE_ID,RULE_ID from UM_ROLE_PERMISSIONRULE";
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()){
				String role_Id = rs.getString("ROLE_ID");
				String rule_Id = rs.getString("RULE_ID");
				if(roleId_permissionRuleIds.containsKey(role_Id)){
					List<String> list = roleId_permissionRuleIds.get(role_Id);
					list.add(rule_Id);
					roleId_permissionRuleIds.put(role_Id, list);
				}else{
					List<String> list = new ArrayList<String>();
					list.add(rule_Id);
					roleId_permissionRuleIds.put(role_Id, list);
				}
			}
		}catch(Exception e){
			log.error("JDBC的方式获取角色与权限规则的映射关系异常：", e);
		}finally{
			try {
				if(rs != null)rs.close();
				if(pstm != null)pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return roleId_permissionRuleIds;
	}
	/**
	 * 初始化应用系统实体关系映射
	 * @param conn
	 * @param tenantId
	 * @param tenantIdReverseMap key:tenantId, value:id
	 * @param umTenantMap
	 * @return
	 */
	@TxIgnore
	public Map<String,Application> initApplicationMapped(Connection conn,String tenantId,Map<String,String> tenantIdReverseMap,Map<String,UMTenant> umTenantMap){
		Map<String,Application> applicationMap = new HashMap<String,Application>();
		String sql = "select ID,NAME,SHORTNAME,AUTHID,SYSTYPE,ISCENTER,URL,VENDER,VERSION,CREATETIME,LASTMODIFYTIME,LASTMODIFIER,CREATOR,DESCRIPTION,LIFECYCLESTATUS,TENANT_ID" +
						" from UM_GENERALRESOURCE";
		PreparedStatement pstm = null;
		ResultSet rs = null;
		if(tenantId != null && !tenantId.equals("")){
			sql += " where TENANT_ID = ?";
		}
		try{
			pstm = conn.prepareStatement(sql);
			if(tenantId != null && !tenantId.equals("")){
				pstm.setString(1, tenantIdReverseMap.get(tenantId));
			}
			rs = pstm.executeQuery();
			while(rs.next()){
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				String shortName = rs.getString("SHORTNAME");
				String authId = rs.getString("AUTHID");
				String sysType = rs.getString("SYSTYPE");
				boolean isCenter = rs.getBoolean("ISCENTER");
				String url = rs.getString("URL");
				String vender = rs.getString("VENDER");
				int version = rs.getInt("VERSION");
				Date createTime = rs.getDate("CREATETIME");
				Date lastModifyTime = rs.getDate("LASTMODIFYTIME");
				String lastModifier = rs.getString("LASTMODIFIER");
				String creator = rs.getString("CREATOR");
				String description = rs.getString("DESCRIPTION");
				Short lifeCycleStatus = rs.getShort("LIFECYCLESTATUS");
				String tenant_Id = rs.getString("TENANT_ID");
				
				Application app = new Application();
				app.setId(id);
				app.setName(name);
				app.setShortName(shortName);
				app.setAuthId(authId);
				app.setSysType(sysType);
				app.setIsCenter(isCenter);
				app.setUrl(url);
				app.setVender(vender);
				app.setVersion(version);
				app.setUMTenant(umTenantMap.get(tenant_Id));
				
				MaintainInfo maintainInfo = this.istMaintainInfo(createTime, lastModifyTime, creator, lastModifier, description, lifeCycleStatus);
				app.setMaintainInfo(maintainInfo);
				applicationMap.put(app.getId(), app);
			}
			
		}catch(Exception e){
			log.error("JDBC的方式获取Application异常：", e);
		}finally{
			try {
				if(rs != null)rs.close();
				if(pstm != null)pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return applicationMap;
	}
	/**
	 * 初始化角色实体关系映射
	 * @param conn
	 * @param tenantId
	 * @param tenantIdReverseMap key:tenantId, value:id
	 * @param applicationMap
	 * @param umTenantMap
	 * @return
	 */
	@TxIgnore
	public Map<String,Role> initRoleMapped(Connection conn,String tenantId,Map<String,String> tenantIdReverseMap,Map<String,Application> applicationMap,Map<String,UMTenant> umTenantMap){
		Map<String,Role> roleMap = new HashMap<String,Role>();
		String sql = "select ID,DTYPE,ENABLED,TYPE,VERSION,NAME,CREATETIME,LASTMODIFYTIME,LASTMODIFIER,CREATOR,DESCRIPTION,LIFECYCLESTATUS," +
						"APPLICATION_ID,TENANT_ID,CODE,CATEGORY from UM_ROLE";
		PreparedStatement pstm = null;
		ResultSet rs = null;
		if(tenantId != null && !tenantId.equals("")){
			sql += " where TENANT_ID = ?";
		}
		try{
			pstm = conn.prepareStatement(sql);
			if(tenantId != null && !tenantId.equals("")){
				pstm.setString(1, tenantIdReverseMap.get(tenantId));
			}
			rs = pstm.executeQuery();
			while(rs.next()){
				String id = rs.getString("ID");
				String dType = rs.getString("DTYPE");
				Boolean enabled = rs.getBoolean("ENABLED");
				int type = rs.getInt("TYPE");
				int version = rs.getInt("VERSION");
				String name = rs.getString("NAME");
				
				
				Date createTime = rs.getDate("CREATETIME");
				Date lastModifyTime = rs.getDate("LASTMODIFYTIME");
				String creator = rs.getString("CREATOR");
				String lastModifier = rs.getString("LASTMODIFIER");
				String description = rs.getString("DESCRIPTION");
				Short lifeCycleStatus = rs.getShort("LIFECYCLESTATUS");
				
				String appId = rs.getString("APPLICATION_ID");
				String tenant_Id = rs.getString("TENANT_ID");
				String code = rs.getString("CODE");
				String category = rs.getString("CATEGORY");
				
				if(dType.equalsIgnoreCase("LegacyRole")){
					LegacyRole legacyRole = new LegacyRole();
					legacyRole.setId(id);
					legacyRole.setEnabled(enabled);
					legacyRole.setType(type);
					legacyRole.setVersion(version);
					legacyRole.setName(name);
					MaintainInfo maintainInfo = this.istMaintainInfo(createTime, lastModifyTime, creator, lastModifier, description, lifeCycleStatus);
					legacyRole.setMaintainInfo(maintainInfo);
					legacyRole.setApplication(applicationMap.get(appId));
					legacyRole.setUMTenant(umTenantMap.get(tenant_Id));
					legacyRole.setCode(code);
					legacyRole.setCategory(category);
					roleMap.put(legacyRole.getId(), legacyRole);
				}else{
					Role role = new Role();
					role.setId(id);
					role.setEnabled(enabled);
					role.setType(type);
					role.setVersion(version);
					role.setName(name);
					MaintainInfo maintainInfo = this.istMaintainInfo(createTime, lastModifyTime, creator, lastModifier, description, lifeCycleStatus);
					role.setMaintainInfo(maintainInfo);
					role.setUMTenant(umTenantMap.get(tenant_Id));
					role.setCategory(category);
					roleMap.put(role.getId(), role);
				}
				
			}
		}catch(Exception e){
			log.error("JDBC的方式获取Role异常：", e);
		}finally{
			try {
				if(rs != null)rs.close();
				if(pstm != null)pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return roleMap;
	}
	/**
	 * 初始化权限规则映射关系
	 * @param conn
	 * @param tenantId
	 * @param tenantIdReverseMap key:tenantId, value:id
	 * @param umTenantMap
	 * @return
	 */
	@TxIgnore
	public Map<String,PermissionRule> initPermissionRuleMapped(Connection conn,String tenantId,Map<String,String> tenantIdReverseMap,Map<String,UMTenant> umTenantMap){
		Map<String,PermissionRule> permissionRuleMap = new HashMap<String,PermissionRule>();
		String sql = "select ID,OPERATIONS,ENABLED,EXPRESSION,VERSION,NAME,TYPE,AUTHABLE,ENTITYLIMITTYPE,CREATETIME,LASTMODIFYTIME,LASTMODIFIER,CREATOR,DESCRIPTION,LIFECYCLESTATUS,TENANT_ID" +
						" from UM_PERMISSIONRULE";
		if(tenantId != null && !tenantId.equals("")){
			sql += " where TENANT_ID = ?";
		}
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			pstm = conn.prepareStatement(sql);
			if(tenantId != null && !tenantId.equals("")){
				pstm.setString(1, tenantIdReverseMap.get(tenantId));
			}
			rs = pstm.executeQuery();
			while(rs.next()){
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				Integer type = rs.getInt("TYPE");
				Integer version = rs.getInt("VERSION");
				String entityLimitType = rs.getString("ENTITYLIMITTYPE");
				String operations = rs.getString("OPERATIONS");
				Boolean authable = rs.getBoolean("AUTHABLE");
				Boolean enabled = rs.getBoolean("ENABLED");
				String expression = rs.getString("EXPRESSION");
				String tenant_Id = rs.getString("TENANT_ID");
		
				Date createTime = rs.getDate("CREATETIME");
				Date lastModifyTime = rs.getDate("LASTMODIFYTIME");
				String creator = rs.getString("CREATOR");
				String lastModifier = rs.getString("LASTMODIFIER");
				String description = rs.getString("DESCRIPTION");
				Short lifeCycleStatus = rs.getShort("LIFECYCLESTATUS");
				
				PermissionRule permissionRule = new PermissionRule();
				permissionRule.setId(id);
				permissionRule.setName(name);
				permissionRule.setType(type);
				permissionRule.setVersion(version);
				permissionRule.setEntityLimitType(entityLimitType);
				permissionRule.setOperations(operations);
				permissionRule.setAuthable(authable);
				permissionRule.setEnabled(enabled);
				permissionRule.setExpression(expression);
				permissionRule.setUMTenant(umTenantMap.get(tenant_Id));
				MaintainInfo maintainInfo = this.istMaintainInfo(createTime, lastModifyTime, creator, lastModifier, description, lifeCycleStatus);
				permissionRule.setMaintainInfo(maintainInfo);
				
				permissionRuleMap.put(id, permissionRule);
			}
		}catch(Exception e){
			log.error("JDBC的方式获取PermissionRule异常：", e);
		}finally{
			try {
				if(rs != null)rs.close();
				if(pstm != null)pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return permissionRuleMap;
		
	}
	/**
	 * 初始化用户规则映射关系
	 * @param conn
	 * @param tenantId
	 * @param tenantIdReverseMap key:tenantId, value:id
	 * @param umTenantMap
	 * @return
	 */
	@TxIgnore
	public Map<String,UserRule> initUserRule(Connection conn,String tenantId,Map<String,String> tenantIdReverseMap,Map<String,UMTenant> umTenantMap){
		Map<String,UserRule> userRuleMap = new HashMap<String,UserRule>();
		String sql = "select ID,OPERATIONS,ENABLED,EXPRESSION,VERSION,NAME,TYPE,AUTHABLE,ENTITYLIMITTYPE,CREATETIME,LASTMODIFYTIME,LASTMODIFIER,CREATOR,DESCRIPTION,LIFECYCLESTATUS,TENANT_ID" +
						" from UM_USERRULE";
		if(tenantId != null && !tenantId.equals("")){
			sql += " where TENANT_ID = ?";
		}
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			pstm = conn.prepareStatement(sql);
			if(tenantId != null && !tenantId.equals("")){
				pstm.setString(1, tenantIdReverseMap.get(tenantId));
			}
			rs = pstm.executeQuery();
			while(rs.next()){
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				Integer type = rs.getInt("TYPE");
				Integer version = rs.getInt("VERSION");
				String entityLimitType = rs.getString("ENTITYLIMITTYPE");
				String operations = rs.getString("OPERATIONS");
				Boolean authable = rs.getBoolean("AUTHABLE");
				Boolean enabled = rs.getBoolean("ENABLED");
				String expression = rs.getString("EXPRESSION");
				String tenant_Id = rs.getString("TENANT_ID");
		
				Date createTime = rs.getDate("CREATETIME");
				Date lastModifyTime = rs.getDate("LASTMODIFYTIME");
				String creator = rs.getString("CREATOR");
				String lastModifier = rs.getString("LASTMODIFIER");
				String description = rs.getString("DESCRIPTION");
				Short lifeCycleStatus = rs.getShort("LIFECYCLESTATUS");
				
				UserRule userRule = new UserRule();
				userRule.setId(id);
				userRule.setName(name);
				userRule.setType(type);
				userRule.setVersion(version);
				userRule.setEntityLimitType(entityLimitType);
				userRule.setOperations(operations);
				userRule.setAuthable(authable);
				userRule.setEnabled(enabled);
				userRule.setExpression(expression);
				userRule.setUMTenant(umTenantMap.get(tenant_Id));
				MaintainInfo maintainInfo = this.istMaintainInfo(createTime, lastModifyTime, creator, lastModifier, description, lifeCycleStatus);
				userRule.setMaintainInfo(maintainInfo);
				
				userRuleMap.put(id, userRule);
			}
		}catch(Exception e){
			log.error("JDBC的方式获取UserRule异常：", e);
		}finally{
			try {
				if(rs != null)rs.close();
				if(pstm != null)pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return userRuleMap;
	}
	@TxIgnore
	public MaintainInfo istMaintainInfo(Date createTime,Date lastModifyTime,String creator,
			String lastModifier,String description,short lifeCycleStatus){
		
		MaintainInfo maintainInfo = new MaintainInfo();
		maintainInfo.setCreateTime(createTime);
		maintainInfo.setLastModifyTime(lastModifyTime);
		maintainInfo.setCreator(creator);
		maintainInfo.setLastModifier(lastModifier);
		maintainInfo.setDescription(description);
		maintainInfo.setLifeCycleStatus(lifeCycleStatus);
		
		return maintainInfo;
	}


}
