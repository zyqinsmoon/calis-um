package com.cnebula.um.service.imp;

import java.util.Map;

import com.cnebula.um.ejb.entity.usr.UMPrincipal;

public interface IActiveByMailRegisterService {
	
	public Map<String, String> register(UMPrincipal user);
	
	public Map<String, String> register(UMPrincipal user, String captchaCode);
	
	public String activeNewUser(String activeCode);
}
