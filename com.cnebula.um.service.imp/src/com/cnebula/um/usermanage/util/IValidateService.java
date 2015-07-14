package com.cnebula.um.usermanage.util;

import java.util.Map;

import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;

public interface IValidateService {

	public boolean checkLoginId(String loginId);
	
	public boolean checkEmail(String email);
	
	public boolean checkCard(String code);

	public boolean checkAdditionalId(String code);
	
	public boolean checkCaptchaCode(String captchaCode);
	
	public Map<String, String> checkUMPrincipalProperty(UMPrincipal ump);
	
	public Map<String,String> checkAdditionalIdProperty(AdditionalId ad);

}
