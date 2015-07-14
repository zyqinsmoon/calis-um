package com.cnebula.um.service;

import java.util.List;

import com.cnebula.um.ejb.entity.perm.Role;

public interface IRoleBuildHelper {
	
	public List<Role> fetchAllCompiledRole(String tenantId);
	
}
