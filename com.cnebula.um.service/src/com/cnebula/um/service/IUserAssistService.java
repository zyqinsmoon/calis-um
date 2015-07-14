package com.cnebula.um.service;

import javax.security.auth.login.LoginException;

@Deprecated
public interface IUserAssistService {
	
	public void changePassword(String userId, String oldPassword, String newPassword)  throws LoginException;
	
	
}
