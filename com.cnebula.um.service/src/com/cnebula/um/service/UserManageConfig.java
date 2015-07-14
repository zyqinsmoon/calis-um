package com.cnebula.um.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnebula.common.annotations.xml.CollectionStyleType;
import com.cnebula.common.annotations.xml.FieldStyleType;
import com.cnebula.common.annotations.xml.XMLMapping;

@XMLMapping(fieldStyle=FieldStyleType.TEXT)
public class UserManageConfig {
	
	/**
	 * 是否启用用户管理Web端(包括用户注册，用户信息查看，用户信息修改，修改密码，密码找回)
	 */
	private boolean enableUserManage = false;//期望使用UM模块的开发组，按照自己的需求，定制自己的用户管理系统
	
	/**
	 * 该系统的域名地址（不建议使用IP地址，如果该主机不在公网上，会造成用户无法访问激活链接）
	 * 
	 */
	private String domain = "";
	
	/**
	 * 用户注册类型:
	 * 		"SimpleRegisterService"：代表不使用邮箱验证;
	 *  	"ActiveByMailRegisterService"：代表使用邮箱验证；
	 */
	private String registerType = "SimpleRegisterService";

	/**
	 * 重置密码有效期:
	 *	 restPasswordExpiredDuration按小时计
	 */
	private int restPasswordExpiredDuration = 5;

	/**
	 * 激活新用户的链接
	 *	  相对路径，如/easyservice/com.cnebula.um.manage.IUserManageService/activeNewUser?activeCode=
	 */
	private String activeNewUserURI = "/easyservice/com.cnebula.um.service.IUserManageService/activateNewUser?activeCode=";
	
	/**
	 * 激活新密码的链接
	 *	  相对路径，如/easyservice/com.cnebula.um.manage.IUserManageService/activeNewUser?activeCode=
	 */	
	private String activeResetPasswordURI = "/easyservice/com.cnebula.um.service.IUserManageService/activateNewPassword?activeCode=";
	
	
	private List<UserManageServiceConfig> services = new ArrayList<UserManageServiceConfig>();

	public boolean isEnableUserManage() {
		return enableUserManage;
	}

	public void setEnableUserManage(boolean enableUserManage) {
		this.enableUserManage = enableUserManage;
	}
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public int getRestPasswordExpiredDuration() {
		return restPasswordExpiredDuration;
	}

	public void setRestPasswordExpiredDuration(int restPasswordExpiredDuration) {
		this.restPasswordExpiredDuration = restPasswordExpiredDuration;
	}

	public String getActiveNewUserURI() {
		return activeNewUserURI;
	}

	public void setActiveNewUserURI(String activeNewUserURI) {
		this.activeNewUserURI = activeNewUserURI;
	}

	public String getActiveResetPasswordURI() {
		return activeResetPasswordURI;
	}

	public void setActiveResetPasswordURI(String activeResetPasswordURI) {
		this.activeResetPasswordURI = activeResetPasswordURI;
	}

	@XMLMapping(childTag="service",collectionStyle=CollectionStyleType.FLAT)
	public List<UserManageServiceConfig> getServices() {
		return services;
	}

	public void setServices(List<UserManageServiceConfig> services) {
		this.services = services;
	}
}
