package com.cnebula.um.usermanage.util;

import java.io.IOException;
import java.util.Map;

import com.cnebula.um.service.UserManageServiceConfig;

public interface IUserManageUtilService {
	
	public Map<String,String> getUserManageServcesConfig();
	
	public String generateRandomCode(int length);
	
	public byte[] getCaptchaPicture() throws IOException;
	
	public String getRegisterConfig();
	
	public String getHttpServerPort();
	
	public int getResetPSWDExpiredDuration();
	
	public boolean isEnableSAAS();
	
	public boolean sendMail(String loginId,String emailAddr,String activeCode, String mailType, String newPassword);
}
