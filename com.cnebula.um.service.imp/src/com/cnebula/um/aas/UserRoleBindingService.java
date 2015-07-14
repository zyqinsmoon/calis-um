package com.cnebula.um.aas;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.TxIgnore;
import com.cnebula.common.annotations.es.TxType;
import com.cnebula.common.ejb.manage.EntityCRUDService;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.common.es.SessionContext;
import com.cnebula.common.log.ILog;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.entity.perm.Role;
import com.cnebula.um.ejb.entity.perm.UserRule;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.service.IRoleBuildHelper;
import com.cnebula.um.service.IUserRoleBindingService;

@EasyService(txType = TxType.CMT, interfaces = { IUserRoleBindingService.class })
public class UserRoleBindingService implements IUserRoleBindingService {

	private static final String OPTIMISTIC_LOCK_EXCEPTION_MSG = "当前编辑对象已经被其他用户修改，请刷新画面重新进行授权";

	@ESRef(target = "(unit=calisum)")
	IEntityCRUDService entityCRUDService;

	@ESRef(target = "(unit=calisum)")
	EntityManager em;

	List<Role> allRoles = new ArrayList<Role>();

	@ESRef
	IIPAndRoleIndexer roleIndexer;

	@ESRef
	IRoleBuildHelper roleBuildHelper;

	@ESRef
	ILog log;

	@TxIgnore
	public synchronized void bind(boolean create, UserRule rule,
			Map<Role, Boolean> roleBinds) throws Exception {
		
		try {
			this.bind(create, rule, roleBinds, true);

		} catch (Exception e) {
			throw e;
		}

	}
	@TxIgnore
	public synchronized void bind(boolean create, UserRule rule,
			Map<Role, Boolean> roleBinds, boolean rebuild) throws Exception {
		try {
			save(create, rule, roleBinds);
			String tenantId="";
			if(rule.getTenant()!=null){
				tenantId=rule.getTenant().getTenantId();
			}
			if (rebuild) {
				rebuildRoleIndex(tenantId);
			}

		} catch (Exception e) {
			throw e;
		}

	}

	public void save(boolean create, UserRule rule, Map<Role, Boolean> roleBinds)
			throws Exception {
		try {
			boolean isCreate = isCreate(rule);
			if (isCreate ^ create) {
				throw new Exception(OPTIMISTIC_LOCK_EXCEPTION_MSG);
			}
			if (isCreate) {
				UMPrincipal userPrincipal = SessionContext.getCurrentUser();
				rule.setTenant(userPrincipal.getTenant());
				((EntityCRUDService) entityCRUDService).fixId(rule);
				((EntityCRUDService) entityCRUDService).fixMaintainInfo(rule);
				em.persist(rule);

			} else {
				((EntityCRUDService) entityCRUDService).fixMaintainInfo(rule);
				rule = em.merge(rule);

			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(OPTIMISTIC_LOCK_EXCEPTION_MSG);
		}

		List<Role> tobeSavedList = new ArrayList<Role>();
		for (Role role : roleBinds.keySet()) {
			tobeSavedList.add(role);
		}
		for (int i = 0; i < tobeSavedList.size(); i++) {
			Role role = tobeSavedList.get(i);

			try {
				saveRole(rule, roleBinds, role);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(OPTIMISTIC_LOCK_EXCEPTION_MSG);
			}
		}
	}

	private void saveRole(UserRule rule, Map<Role, Boolean> roleBinds, Role role)
			throws Exception {
		try {
			Role newRole = getNewRole(rule, roleBinds, role);
			((EntityCRUDService) entityCRUDService).fixMaintainInfo(newRole);
			em.merge(newRole);
		} catch (Exception e) {
			throw e;
		}

	}

	protected void activate(ComponentContext ctx) {
		refreshRoleIndex();
	}

	private void refreshRoleIndex() {
		if(roleIndexer instanceof EnhencedUMAccessControlProvider){
			Map<String, Map<String, Role>> map = ((EnhencedUMAccessControlProvider) roleIndexer)
			.getRoleIndex();
			allRoles = new ArrayList<Role>();
			for (String tenantId : map.keySet()) {
				if (map.get(tenantId) != null && map.get(tenantId).size() > 0) {
					for (Role role : map.get(tenantId).values()) {
						try {
							role = (Role) role.clone();
						} catch (Throwable e) {
							log.warn("发现角色" + role.getName()
											+ "中有非法的规则，该角色将被忽略", e);
						}
						allRoles.add(role);
					}
				}
			}
			try {
				Collections.sort(allRoles, new Comparator<Role>(){
					public int compare(Role o1, Role o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			log.debug("==allRoles.size():" + allRoles.size());
		}
	
	}

	private Role getNewRole(UserRule rule, Map<Role, Boolean> roleBinds,
			Role role) throws Exception {
		try {
			if (roleBinds.get(role)) {
				if (!role.getUMUserRules().contains(rule)) {
					role.getUMUserRules().add(rule);
				}
			} else {
				if (role.getUMUserRules().contains(rule)) {
					role.getUMUserRules().remove(rule);
				}
			}
			return role;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	public List<Role> getNotCategeoryRoles(String notCategeory) {
		refreshRoleIndex();
		List<Role> roles = new ArrayList<Role>();
		String tenantId = getTenantId();
		List<Role> myRoles = getMyRoles(allRoles, tenantId);
		for (Role r : myRoles) {
			if (!r.getCategory().equals(notCategeory)) {
				roles.add(r);
			}
		}
		return roles;
	}
	private String getTenantId() {
		ITenant tenant = ((UMPrincipal) SessionContext.getCurrentUser())
				.getTenant();
		String tenantId = "";
		if (tenant != null) {
			tenantId = tenant.getTenantId();
		}
		return tenantId;
	}

	public List<Role> getCategeoryRoles(String categeory) {
		refreshRoleIndex();
		List<Role> roles = new ArrayList<Role>();
		String tenantId = getTenantId();
		List<Role> myRoles = getMyRoles(allRoles, tenantId);
		for (Role r : myRoles) {
			if (r.getCategory().equals(categeory)) {
				roles.add(r);
			}
		}

		return roles;
	}

	public List<Role> getAllRolesInDb() {
		refreshRoleIndex();
		ITenant tenant = ((UMPrincipal) SessionContext.getCurrentUser())
				.getTenant();
		String tenantId = null;
		if (tenant != null) {
			tenantId = tenant.getTenantId();
		}
		List<Role> myRoles = getMyRoles(allRoles, tenantId);
		return myRoles;
	}

	private boolean isMyRole(Role role, String TenantId) {
		boolean result = false;
		if (role.getTenant() != null) {
			if (role.getTenant().getTenantId() != null && TenantId != null
					&& role.getTenant().getTenantId().equals(TenantId)) {
				result = true;
			}
		}
		return result;
	}

	/*
	 * 返回适用于当前用户的Role
	 */
	private List<Role> getMyRoles(List<Role> roles, String TenantId) {
		List<Role> resultRoles = new ArrayList<Role>();
		for (Role role : roles) {
			if (isMyRole(role, TenantId) && !isXmlConfigRole(role)) {
				resultRoles.add((Role) role);
			}
		}
		return resultRoles;
	}

	private boolean isXmlConfigRole(Role role) {
		if (role.getMaintainInfo() != null) {
			if (role.getMaintainInfo().getDescription() != null
					&& role.getMaintainInfo().getDescription().equals("XML配置")) {
				return true;
			}
		}
		return false;
	}

	private boolean isCreate(UserRule rule) {
		boolean result = false;
		long i = entityCRUDService.queryCount(UserRule.class,
				EntityQuery.createExactQuery("id", rule.getId()));
		if (i== 0) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
	public synchronized void deleteUserRule(List<Object> ids) throws Exception {
		String tenantId = getTenantId();
		for (Object id : ids) {
			Map<Role, Boolean> roleBinds = new HashMap<Role, Boolean>();
			UserRule userRule = entityCRUDService.querySingle(UserRule.class,
					EntityQuery.createExactQuery("id", id));
			List<Role> roles = getRelatedRoles(userRule);
			for (Role r : roles) {
				r.getUMUserRules().remove(userRule);
				roleBinds.put(r, false);
			}
			try {
				bind(false, userRule, roleBinds,false);
			} catch (Exception e) {
				throw e;
			}
			userRule = entityCRUDService.querySingle(UserRule.class,
					EntityQuery.createExactQuery("id", id));
			em.remove(userRule);
			
			rebuildRoleIndex(tenantId);
		}

	}
	private void rebuildRoleIndex(final String tenantId) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				roleIndexer.rebuildRoleIndex(tenantId);
				refreshRoleIndex();
				return null;
			}
		});
	}

	private List<Role> getRelatedRoles(UserRule userRule) {
		List<Role> roles = new ArrayList<Role>();
		String tenantId = getTenantId();
		List<Role> myRolesList = getMyRoles(allRoles, tenantId);
		for (Role r : myRolesList) {
			if (r.getUMUserRules().contains(userRule)) {
				roles.add(r);
			}
		}
		return roles;
	}

}
