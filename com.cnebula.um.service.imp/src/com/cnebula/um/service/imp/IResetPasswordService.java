package com.cnebula.um.service.imp;

public interface IResetPasswordService {
	
	public String resetPassword(String loginId, String email);
	
	public String activateNewPassword(String activateCode);
}
