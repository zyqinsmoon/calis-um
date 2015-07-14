package com.cnebula.um.service.imp;

import java.util.Map;

import com.cnebula.um.ejb.entity.usr.UMPrincipal;

public interface IUpdateUserInfo {
	
    public String changePassword(String oldPassword, String newPassword);
	
	public Map<String, String> updateUser(UMPrincipal user);

	public Map<String, String> updateUserByFields(Map<String, Object> avaliableFields);
}

