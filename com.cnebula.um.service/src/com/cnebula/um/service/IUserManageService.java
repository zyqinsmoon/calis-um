package com.cnebula.um.service;

import java.util.List;
import java.util.Map;

import com.cnebula.common.annotations.es.ParamList;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.ejb.entity.perm.Role;

public interface IUserManageService {
	/**
	 * @return 是否SAAS
	 */
	public boolean isEnableSAAS();
	
	@ParamList({"user","captchaCode"})
	public Map<String,String> registerUser(UMPrincipal user, String captchaCode);
	
	@ParamList({"activeCode"})
	public String activateNewUser(String activeCode);
	
	@ParamList({"loginId"})
	public boolean checkLoginId(String loginId);
	
	@ParamList({"email"})
	public boolean checkEmail(String email);
	
	@ParamList({"code"})
	public boolean checkAdditionalId(String code);
	
	@ParamList({"code"})
	public boolean checkCard(String code);
	
	@ParamList({"loginId","email"})
	public String resetPassword(String loginId, String email);
	
	@ParamList({"activeCode"})
	public String activateNewPassword(String activateCode);
	
	@ParamList({"user"})
	public Map<String,String> updateUser(UMPrincipal user);
	
	@ParamList({"avaliableFields"})
	public Map<String, String> updateUserByFields(Map<String, Object> avaliableFields);
	
	@ParamList({"oldPassword","newPassword"})
    public String changePassword(String oldPassword, String newPassword);
	
	/**
	 * SAAS化的UM
	 * 
	 * @return List<Organization>没有父机构的机构
	 */
	public List<Organization> getTopOrg();
	
	/**
	 * Vector js bugs
	 * 
	 * convert vector to ArrayList 
	 * @return
	 */
	public UMPrincipal getUser();
	
	/**
	 * Vector js bugs
	 * 
	 * convert vector to ArrayList 
	 * @return
	 */
	public List<Role> getRoles();
	
}
