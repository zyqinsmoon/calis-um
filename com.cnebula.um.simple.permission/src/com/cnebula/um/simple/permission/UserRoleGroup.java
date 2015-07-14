package com.cnebula.um.simple.permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnebula.um.ejb.entity.perm.Role;
import com.cnebula.um.ejb.entity.perm.UserRule;

public class  UserRoleGroup{	

	Map<String,String> properties=new HashMap<String,String>();
	
	String roleGroupName="";
	
	String userRuleGroupName="";
	
	String id="";
	
	List<Role> roles;
	
	List<UserRule> userRules;

	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getRoleGroupName() {
		return roleGroupName;
	}

	public void setRoleGroupName(String roleGroupName) {
		this.roleGroupName = roleGroupName;
	}

	public String getUserRuleGroupName() {
		return userRuleGroupName;
	}


	public void setUserRuleGroupName(String userRuleGroupName) {
		this.userRuleGroupName = userRuleGroupName;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}





	public List<UserRule> getUserRules() {
		return userRules;
	}





	public void setUserRules(List<UserRule> userRules) {
		this.userRules = userRules;
	}





	@Override
	public String toString() {
		return "id=" + id;
	}
}
