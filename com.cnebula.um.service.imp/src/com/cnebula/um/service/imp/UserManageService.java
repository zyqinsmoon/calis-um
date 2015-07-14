package com.cnebula.um.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.es.IEasyServiceManager;
import com.cnebula.common.i18n.I18nService;
import com.cnebula.common.security.auth.IAuthorizationService;
import com.cnebula.common.security.auth.ILoginService;
import com.cnebula.um.ejb.entity.perm.Role;
import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.service.IOrganizationQueryService;
import com.cnebula.um.service.IUserManageService;
import com.cnebula.um.usermanage.util.IUserManageUtilService;
import com.cnebula.um.usermanage.util.IValidateService;

@EasyService(properties={@Property(name="id", value="UMUserManage")})
public class UserManageService implements IUserManageService {
	
	public static String UM_USERMANAGE_SERVICE_TAGETID = "(id=UMUserManage)";
	
	@ESRef
	private IUserManageUtilService userManageUtilService; 
	
	private IValidateService validateService;
	
	private	IActiveByMailRegisterService activeByMailRegisterService;
	
	private ISimpleRegisterService simpleRegisterService;
	
	private IResetPasswordService resetPasswordService;
	
	private IUpdateUserInfo updateInfo;
	
	public @ESRef IOrganizationQueryService organizationQueryService;
	
	private ILoginService loginService;
	
	@ESRef
	public  IEasyServiceManager easyServiceManager;
	
	@ESRef(target="(namespace=com.cnebula.um.service.userRegister)")
	private I18nService i18;
	
	private com.cnebula.common.security.auth.IAuthorizationService authorizationService;
	
	private ComponentContext ctx;
	
	protected void activate(ComponentContext ctx){
		this.ctx = ctx;
		initServices(ctx);
	}
	
	public String activateNewPassword(String activateCode) {
		if(resetPasswordService == null)
			initServices(ctx);
		return resetPasswordService.activateNewPassword(activateCode);
	}

	public String activateNewUser(String activeCode) {
		if(activeByMailRegisterService == null)
			initServices(ctx);
		return activeByMailRegisterService.activeNewUser(activeCode);
	}
	
	public String changePassword(String oldPassword, String newPassword) {
		if(updateInfo == null)
			initServices(ctx);
		return updateInfo.changePassword(oldPassword, newPassword);
	}

	public boolean checkEmail(String email) {
		if(validateService == null)
			initServices(ctx);
		return validateService.checkEmail(email);
	}
	
	public boolean checkAdditionalId(String code) {
		if(validateService == null)
			initServices(ctx);
		return validateService.checkAdditionalId(code);
	}

	public boolean checkCard(String code) {
		if(validateService == null)
			initServices(ctx);
		return validateService.checkCard(code);
	}
	public boolean checkLoginId(String loginId) {
		if(validateService == null)
			initServices(ctx);
		return validateService.checkLoginId(loginId);
	}

	public Map<String, String> registerUser(UMPrincipal user, String captchaCode) {
		if(activeByMailRegisterService == null || simpleRegisterService == null){
			initServices(ctx);
		}
			
		String registerType = userManageUtilService.getRegisterConfig();
		if(registerType.equals("ActiveByMailRegisterService"))
			return activeByMailRegisterService.register(user, captchaCode);
		else if(registerType.equals("SimpleRegisterService"))
			return simpleRegisterService.register(user, captchaCode);
		else{
			Map<String,String> err = new HashMap<String, String>();
			err.put("$Register.ErrorConfig", i18.getString("$Register.ErrorConfig"));
			return err;
		}
	}

	public String resetPassword(String loginId, String email) {
		if(resetPasswordService == null)
			initServices(ctx);
		return resetPasswordService.resetPassword(loginId, email);
	}

	public Map<String, String> updateUser(UMPrincipal user) {
		if(updateInfo == null)
			initServices(ctx);
		return updateInfo.updateUser(user);
	}

	public Map<String, String> updateUserByFields(Map<String, Object> avaliableFields) {
		if(updateInfo == null)
			initServices(ctx);
		return updateInfo.updateUserByFields(avaliableFields);
	}

	public UMPrincipal getUser() {
		loginService = (ILoginService) easyServiceManager.getService(this.getClass(), null, ILoginService.class.getName(), null);
		UMPrincipal aUser = (UMPrincipal) loginService.getUser();
		List<AdditionalId> additionalIds = aUser.getAdditionalIds();
		List<AdditionalId> aids = new ArrayList<AdditionalId>();
		for(AdditionalId aid: additionalIds){
			aids.add(aid);
		}
		aUser.setAdditionalIds(aids);
		aUser.setPersonality(null);
		return aUser;
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRoles() {
		loginService = (ILoginService) easyServiceManager.getService(this.getClass(), null, ILoginService.class.getName(), null);
		UMPrincipal aUser = (UMPrincipal) loginService.getUser();
		authorizationService = (IAuthorizationService) easyServiceManager.getService(this.getClass(), null, IAuthorizationService.class.getName(), null);
		List<Role> roles = authorizationService.getRolesByUser(aUser);
		List<Role> rs = new ArrayList<Role>();
		for(Role r: roles){
			rs.add(r);
		}
		return rs;
	}

	public List<Organization> getTopOrg() {
		List<Organization> rst = organizationQueryService.getAllTopOrganization();
		List<Organization> list = new ArrayList<Organization>();
		for(Organization o : rst){
			list.add(o);
		}
		return list;
	}

	public boolean isEnableSAAS() {
		return userManageUtilService.isEnableSAAS();
	}

	private void initServices(ComponentContext ctx) {
		Map<String, String> servciesConfig  = userManageUtilService.getUserManageServcesConfig();
		if( servciesConfig.get(IValidateService.class.getName()) != null)
			validateService = (IValidateService) easyServiceManager.getService(this.getClass(), ctx, IValidateService.class.getName(), servciesConfig.get(IValidateService.class.getName()));
		else
			validateService = (IValidateService) easyServiceManager.getService(this.getClass(), ctx, IValidateService.class.getName(), UM_USERMANAGE_SERVICE_TAGETID);
		
		if ( servciesConfig.get(IActiveByMailRegisterService.class.getName()) != null )
			activeByMailRegisterService = (IActiveByMailRegisterService) easyServiceManager.getService(this.getClass(), ctx, IActiveByMailRegisterService.class.getName(), servciesConfig.get(IActiveByMailRegisterService.class.getName()));
		else
			activeByMailRegisterService = (IActiveByMailRegisterService) easyServiceManager.getService(this.getClass(), ctx, IActiveByMailRegisterService.class.getName(), UM_USERMANAGE_SERVICE_TAGETID);
		
		if ( servciesConfig.get(ISimpleRegisterService.class.getName()) != null )
			simpleRegisterService = (ISimpleRegisterService) easyServiceManager.getService(this.getClass(), ctx, ISimpleRegisterService.class.getName(), servciesConfig.get(ISimpleRegisterService.class.getName()));
		else
			simpleRegisterService = (ISimpleRegisterService) easyServiceManager.getService(this.getClass(), ctx, ISimpleRegisterService.class.getName(), UM_USERMANAGE_SERVICE_TAGETID);
		
		if ( servciesConfig.get(IResetPasswordService.class.getName()) != null  )
			resetPasswordService = (IResetPasswordService) easyServiceManager.getService(this.getClass(), ctx, IResetPasswordService.class.getName(), servciesConfig.get(IResetPasswordService.class.getName()));
		else
			resetPasswordService = (IResetPasswordService) easyServiceManager.getService(this.getClass(), ctx, IResetPasswordService.class.getName(), UM_USERMANAGE_SERVICE_TAGETID);
		
		if ( servciesConfig.get(IUpdateUserInfo.class.getName()) != null )
			updateInfo = (IUpdateUserInfo) easyServiceManager.getService(this.getClass(), ctx, IUpdateUserInfo.class.getName(), servciesConfig.get(IUpdateUserInfo.class.getName()));
		else
			updateInfo = (IUpdateUserInfo) easyServiceManager.getService(this.getClass(), ctx, IUpdateUserInfo.class.getName(), UM_USERMANAGE_SERVICE_TAGETID);
	}	
}
