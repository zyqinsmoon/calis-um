package com.cnebula.um.service;

import java.util.ArrayList;
import java.util.List;

import com.cnebula.common.annotations.xml.CollectionStyleType;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.um.ejb.entity.perm.Role;

public class StaticRolesConfig {
	List<Role> roles = new ArrayList<Role>();

	@XMLMapping(collectionStyle=CollectionStyleType.FLAT,childTag="Role")
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
}
