package com.cnebula.um.simple.permission.imp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.es.TxType;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.log.ILog;
import com.cnebula.um.ejb.entity.perm.Application;
import com.cnebula.um.ejb.entity.perm.LegacyRole;
import com.cnebula.um.ejb.entity.perm.Role;
import com.cnebula.um.ejb.entity.perm.UserRule;
import com.cnebula.um.simple.permission.ISimpleRoleBindService;
import com.cnebula.um.simple.permission.ISimpleRoleService;
import com.cnebula.um.simple.permission.UserRoleGroup;

@EasyService(txType=TxType.CMT,properties = { @Property(name = "id", value = "um") },interfaces={ISimpleRoleService.class})
public class UmSimpleRoleService implements ISimpleRoleService{
	private static final String ALL_ROLE_TOGETHER = "allRoleTogether";

	private static final String USER_ROLES_BY_APPLICATION = "userRolesByApplication";

	private static final String ROLE = "角色";

	private static final String ALL_ROLES = "所有角色";

	private static final String USER_GROUP_NAME = "用户组";

	@ESRef
	ISimpleRoleBindService iUserRoleBindService;
	
	@ESRef
	private ILog log;

	@ESRef(target = "(unit=#{umunit})")
	private IEntityCRUDPrivateService entityCRUDPrivateService;
	
	@ESRef(target="(unit=calisum)")
	EntityManager em;
	

	protected void activate(ComponentContext ctx) {
		log.info("启动用户简单角色授权服务");
	}

	public void saveRoleByEm(Role role) {
		em.merge(role);
	}
	
	
	public List<UserRoleGroup> getUserRoleGroupList(String tenantId,
			String groupName) {
		List<UserRoleGroup> userRoleGroups = new ArrayList<UserRoleGroup>();
		getUserRoleGroups(tenantId,groupName);
		
//		if (groupName == null) {
//			userRoleGroups = iUserRoleBindService.getUserRoleGroupList(tenantId);
//		} else {
			userRoleGroups=iUserRoleBindService.getUserRoleGroupList(groupName,tenantId);
//		}
		return userRoleGroups;
	}

	private void getUserRoleGroups(String tenantId,String groupName) {
			if(groupName.equals(USER_ROLES_BY_APPLICATION)){
				getUserRolesByApplication(tenantId);
				
			}else if(groupName.equals(ALL_ROLE_TOGETHER)){
				getAllRoleTogetherGroup(tenantId);
			}
	}

	private void getUserRolesByApplication(String tenantId) {
		
		EntityQuery queryApp = EntityQuery.createExactQuery("uMTenant.tenantId", tenantId);
		List<Application> apps=entityCRUDPrivateService.query(Application.class,queryApp);
		
		List<UserRule> userRuleList = new ArrayList<UserRule>();
		EntityQuery query = EntityQuery.createExactQuery("uMTenant.tenantId", tenantId);
	
		userRuleList =entityCRUDPrivateService.query(UserRule.class,query);
		
		for(Application app:apps){
			EntityQuery queryRoleFromApp = EntityQuery.createExactQuery("application.id", app.getId());
			
			List<LegacyRole> LegacyRoleList = new ArrayList<LegacyRole>();
			
			LegacyRoleList = entityCRUDPrivateService.query(LegacyRole.class,EntityQuery.createAndQuery(queryRoleFromApp, queryApp));

			List<Role> roleList = new ArrayList<Role>();
			for(LegacyRole legacyRole:LegacyRoleList){
				roleList.add((Role)legacyRole);
			}
			if(roleList.size()>0){
				UserRoleGroup userRoleGroup=new UserRoleGroup();
				userRoleGroup.setId(USER_ROLES_BY_APPLICATION+app.getId());
				userRoleGroup.setRoleGroupName(app.getName()+ROLE);
				userRoleGroup.setUserRuleGroupName(USER_GROUP_NAME);
				userRoleGroup.setRoles(roleList);
				userRoleGroup.setUserRules(userRuleList);
				iUserRoleBindService.registerUserRoleGroup(userRoleGroup,tenantId);
			}
			
		}
		

		
	}
	private void getAllRoleTogetherGroup(String tenantId) {
		List<Role> roleList = new ArrayList<Role>();
		EntityQuery querySelf = EntityQuery.createExactQuery("uMTenant.tenantId", tenantId);

		roleList =(List<Role>)entityCRUDPrivateService.query(Role.class,querySelf);

		List<UserRule> userRuleList = new ArrayList<UserRule>();
		EntityQuery query = EntityQuery.createExactQuery("uMTenant.tenantId", tenantId);
		
		userRuleList =entityCRUDPrivateService.query(UserRule.class,query);
		
		UserRoleGroup userRoleGroup=new UserRoleGroup();
		userRoleGroup.setId(ALL_ROLE_TOGETHER);
		userRoleGroup.setRoleGroupName(ALL_ROLES);
		userRoleGroup.setUserRuleGroupName(USER_GROUP_NAME);
		userRoleGroup.setRoles(roleList);
		userRoleGroup.setUserRules(userRuleList);
		iUserRoleBindService.registerUserRoleGroup(userRoleGroup,tenantId);
	}


	public void saveRoleByCRUD(EntityPropertityItemStatus rootOpInfo) {
		try {
			entityCRUDPrivateService.op(rootOpInfo);
		} catch (EntityCRUDException e) {
			log.error(e);
		}
	}
}
