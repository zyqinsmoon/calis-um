package com.cnebula.um.service.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.relation.EntityProperty;
import com.cnebula.common.ejb.relation.IEntityFullInfoPool;
import com.cnebula.common.es.IEasyServiceManager;
import com.cnebula.common.i18n.I18nService;
import com.cnebula.common.log.ILog;
import com.cnebula.common.security.auth.ILoginService;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.usermanage.dao.IUMPrincipalDao;
import com.cnebula.um.usermanage.util.IUserManageUtilService;
import com.cnebula.um.usermanage.util.IValidateService;

@EasyService(properties={@Property(name="id", value="UMUserManage")})
public class UpdateUserInfo implements IUpdateUserInfo {

	protected @ESRef(target="(id=UMUserManage)") IUMPrincipalDao umpDao;
	
	protected @ESRef(target="(namespace=com.cnebula.um.service.updateUserInfo)")
    I18nService i18; 
	
	protected @ESRef(target="(namespace=com.cnebula.um.service.userRegister)")
	I18nService i18R;
	
	protected @ESRef IEasyServiceManager easyServiceManager;
	
	protected @ESRef ILog log ; 
	
	@ESRef(target="(unit=#{umunit})")
	private IEntityFullInfoPool entityInfoPool ;
	
	protected @ESRef IUserManageUtilService  userManageUtilService;
	
	protected IValidateService validate;
	
	protected void activate(ComponentContext ctx){
		Map<String, String> servciesConfig  = userManageUtilService.getUserManageServcesConfig();
		if( servciesConfig.get(IValidateService.class.getName()) != null)
			validate = (IValidateService) easyServiceManager.getService(this.getClass(), ctx, IValidateService.class.getName(), servciesConfig.get(IValidateService.class.getName()));
		else
			validate = (IValidateService) easyServiceManager.getService(this.getClass(), ctx, IValidateService.class.getName(), UserManageService.UM_USERMANAGE_SERVICE_TAGETID);		
	}
	
	public String changePassword(String oldPassword, String newPassword) {
		
		ILoginService loginService = (ILoginService) easyServiceManager.getService(this.getClass(), null, ILoginService.class.getName(), null);
		UMPrincipal  aUser = (UMPrincipal) loginService.getUser();
		
		
		//用户没有登录
		if(aUser.getPassword() == null && aUser.getLoginId().equals("$anonymous")){
			return i18.getString("$ChangePassword.notLogin");
		}
		
		UMPrincipal theUserInDb = umpDao.getByLoginId(aUser.getLoginId());
		
		//旧密码有错
		if(!theUserInDb.getPassword().equals(oldPassword)){
			return i18.getString("$ChangePassword.wrongOldPassword");
		}
		//更新密码
		theUserInDb.setPassword(newPassword);
		
		try {
			umpDao.update(theUserInDb);
		} catch (EntityCRUDException e) {
			log.error("change password error", e);
			return "failed";
		}
		return "success";
	}
	
	public Map<String, String> updateUser(UMPrincipal user) {
		
		Map<String, String> errMap = new HashMap<String, String>();
		ILoginService loginService = (ILoginService) easyServiceManager.getService(this.getClass(), null, ILoginService.class.getName(), null);
		UMPrincipal aUser = (UMPrincipal) loginService.getUser();
		if (aUser.getLoginId().equals("$anonymous")) {
			errMap.put("$UpdateUserInfo.notLogin", i18.getString("$UpdateUserInfo.notLogin"));
			return errMap;
		} 
		
		//校验用户的Email
		if(!validate.checkEmail(((UMPrincipal) loginService.getUser()).getEmail())){
			errMap.put("$Email.uniqueCheck", i18.getString("$Email.uniqueCheck"));
		}
		
		if(errMap.size() > 0 )
			return errMap;
		
		try {
			UMPrincipal wouldBeCommited = getUserShouldBeCommit(user);
			umpDao.update(wouldBeCommited);
		} catch (Throwable e) {
			log.error(e);
			errMap.put("updateException", e.getMessage());
		}
		return errMap;
	}
	
	public Map<String, String> updateUserByFields(Map<String, Object> avaliableFields) {
		Map<String, String> errMap = new HashMap<String, String>();
		if (avaliableFields == null) {
			return errMap;
		}
		ILoginService loginService = (ILoginService) easyServiceManager.getService(this.getClass(), null, ILoginService.class.getName(), null);
		String loginId = ((UMPrincipal) loginService.getUser()).getLoginId();
		if (loginId.equals("$anonymous")) {
			errMap.put("$UpdateUserInfo.notLogin", i18.getString("$UpdateUserInfo.notLogin"));
		}
		
		//校验用户的Email
		if(!validate.checkEmail(((UMPrincipal) loginService.getUser()).getEmail())){
			errMap.put("$Email.uniqueCheck", i18.getString("$Email.uniqueCheck"));
		}
		
		if(errMap.size() > 0 )
			return errMap;
		
		try {
			UMPrincipal wouldBeCommited = getUserShouldBeCommit(loginId, avaliableFields);
			umpDao.update(wouldBeCommited);
		} catch (Throwable e) {
			log.error(e);
			errMap.put("updateException", e.getMessage());
		}
		return errMap;
	}
	
	@SuppressWarnings("unchecked")
	private UMPrincipal getUserShouldBeCommit(UMPrincipal user) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		UMPrincipal userInDB = umpDao.getByLoginId(user.getLoginId());
		Map<String, EntityProperty> pMap= entityInfoPool.getEntityInfo(UMPrincipal.class).getPropertyMap();
		for(String p : pMap.keySet()){
			Object valueInDB = pMap.get(p).getValue(userInDB);
			Object newInMemory = pMap.get(p).getValue(user);
			if(!p.equals("version") && newInMemory!=null && !newInMemory.equals(valueInDB)){
				pMap.get(p).setValue(userInDB, newInMemory);
			}
		}
		return userInDB ; 
	}
	
	@SuppressWarnings("unchecked")
	private UMPrincipal getUserShouldBeCommit(String loginId, Map<String, Object> avaliableFields) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		UMPrincipal userInDB = umpDao.getByLoginId(loginId);
		Map<String, EntityProperty> pMap= entityInfoPool.getEntityInfo(UMPrincipal.class).getPropertyMap();
		for(String p : avaliableFields.keySet()){
			Object fieldValue = avaliableFields.get(p);
			pMap.get(p).setValue(userInDB, fieldValue);
		}
		return userInDB ; 
	}

}
