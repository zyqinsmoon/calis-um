package com.cnebula.um.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.es.IEasyServiceManager;
import com.cnebula.common.i18n.I18nService;
import com.cnebula.common.log.ILog;
import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.NewUserBean;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.ejb.saas.UMTenant;
import com.cnebula.um.usermanage.dao.INewUserDao;
import com.cnebula.um.usermanage.dao.IUMPrincipalDao;
import com.cnebula.um.usermanage.util.IUserManageUtilService;
import com.cnebula.um.usermanage.util.IValidateService;

@EasyService(properties={@Property(name="id", value="UMUserManage")})
public class ActiveByMailRegisterService implements IActiveByMailRegisterService {

	protected IValidateService validate;
	
	protected @ESRef(target="(id=UMUserManage)") INewUserDao newUserDao;
	
	protected @ESRef(target="(id=UMUserManage)") IUMPrincipalDao umPrincipalDao;
	
	@ESRef(target="(namespace=com.cnebula.um.service.userRegister)")
	protected I18nService i18;
	
	protected @ESRef IUserManageUtilService  userManageUtilService;
	
	protected @ESRef ILog log;
	
	protected @ESRef IEasyServiceManager easyServiceManager;
	
	protected void activate(ComponentContext ctx){
		Map<String, String> servciesConfig  = userManageUtilService.getUserManageServcesConfig();
		if( servciesConfig.get(IValidateService.class.getName()) != null)
			validate = (IValidateService) easyServiceManager.getService(this.getClass(), ctx, IValidateService.class.getName(), servciesConfig.get(IValidateService.class.getName()));
		else
			validate = (IValidateService) easyServiceManager.getService(this.getClass(), ctx, IValidateService.class.getName(), UserManageService.UM_USERMANAGE_SERVICE_TAGETID);		
	}
	
	/**
	 * 可以重写实现自己附加的校验信息
	 * @param UMPrincipal
	 * @return error(错误)
	 */
	public Map<String,String> additionalValidate(UMPrincipal user){
		
		Map<String,String> errorMap =  new HashMap<String, String>();
		if(userManageUtilService.isEnableSAAS()){//需要进行SAAS化
			if(user.getUMTenant() == null){
				errorMap.put("$EnableSAAS.error", i18.getString("$EnableSAAS.error"));
			}
			else{
				String tenantId = user.getUMTenant().getTenantId();
				if(tenantId == null)
					errorMap.put("$EnableSAAS.error", i18.getString("$EnableSAAS.error"));
				else{
					UMTenant tenant = umPrincipalDao.getTenantByTenantID(tenantId);
					if(tenant == null){//error
						errorMap.put("$TenantId.notExist", i18.getFormattedString("$TenantId.notExist",new String[]{tenantId}));
					}else
						user.setUMTenant(tenant);
				}
			}
		}else
			user.setTenant(null);
		return errorMap;
	}
	
	/**
	 * 可以重写实现自己附加的业务逻辑（应该避免错误的信息出现校验错误）
	 * @param UMPrincipal
	 * @return error(错误)
	 */
	public Map<String,String> additionalBusinessLogic(UMPrincipal user){
		//多馆用户处理
		if(user.getLoginId().indexOf(':') > 0){
			String orgCode = user.getLoginId().substring(0, user.getLoginId().indexOf(':'));
			Organization o = newUserDao.getOrganizationByCode(orgCode);
			user.setOrganization(o);
		}
		return new HashMap<String, String>();
	}
	
	/**
	 * @param user(用户实体对象UMPrincipal)
	 * @return Map<String,String> 错误消息
	 */
	public Map<String,String> register(UMPrincipal user){
		
		Map<String, String> errorMap = new HashMap<String, String>();
		
		/*******************additionalValidate*************************/
		errorMap.putAll(additionalValidate(user));
		
		//业务逻辑需要的非空校验和唯一性校验
		if(user.getLoginId() == null || (user.getLoginId() != null && user.getLoginId().trim().equals(""))){
			errorMap.put("$LoginId.notNullCheck", i18.getString("$LoginId.notNullCheck"));
		}else {	
			if(!validate.checkLoginId(user.getLoginId())){
				errorMap.put("$LoginId.uniqueCheck", i18.getString("$LoginId.uniqueCheck"));
			}		
		}
		if(user.getEmail() == null || (user.getEmail() != null && user.getEmail().trim().equals(""))){
			errorMap.put("$Email.notNullCheck", i18.getString("$Email.notNullCheck"));
		}else{	
			if(!validate.checkEmail(user.getEmail())){
				errorMap.put("$Email.uniqueCheck", i18.getString("$Email.uniqueCheck"));
			}
		}
		if(user.getPassword() == null || user.getPassword().trim().equals("")){
			errorMap.put("$Password.notNullCheck", i18.getString("$Password.notNullCheck"));
		}
		if(user.getCard() == null || (user.getCard() != null && user.getCard().getCode() == null)
		    || user.getCard() != null && user.getCard().getCode() !=null && user.getCard().getCode().equals("")){
			
			errorMap.put("$Card.notNullCheck", i18.getString("$Card.notNullCheck"));
		}else{
			if(!validate.checkCard(user.getCard().getCode())){
				errorMap.put("$Card.uniqueCheck", i18.getString("$Card.uniqueCheck"));
			}
		}
		if(user.getAdditionalIds() == null || (user.getAdditionalIds() != null && user.getAdditionalIds().size() <= 0) 
			|| (user.getAdditionalIds() != null && user.getAdditionalIds().size() > 0 && user.getAdditionalIds().get(0) == null)
			|| (user.getAdditionalIds() != null && user.getAdditionalIds().size() > 0 && user.getAdditionalIds().get(0) != null && user.getAdditionalIds().get(0).getCode() == null)
			|| (user.getAdditionalIds() != null && user.getAdditionalIds().size() > 0 && user.getAdditionalIds().get(0) != null && user.getAdditionalIds().get(0).getCode() != null && user.getAdditionalIds().get(0).getCode().trim().equals(""))){
			
			errorMap.put("$AdditionalId.notNullCheck", i18.getString("$AdditionalId.notNullCheck"));
		}else{
			if(!validate.checkAdditionalId(user.getAdditionalIds().get(0).getCode())){
				errorMap.put("$AdditionalId.uniqueCheck", i18.getString("$AdditionalId.uniqueCheck"));
			}	
		}
		if (errorMap.size() != 0)
 			return errorMap;
		
		//数据库校验
		Map<String,String> validateExceptionMap = validate.checkUMPrincipalProperty(user);
		if(validateExceptionMap.size() > 0)
			return validateExceptionMap;
		
		//正常
		user.setStatus(2);

		//新修改的页面按照原来读者管理系统改，要修改additionalId中的字段属性
		List<AdditionalId> addIds = user.getAdditionalIds();
		for(AdditionalId aId: addIds){
    		aId.setOwner(user);
    		aId.setUpdateType(1);//只读
    		aId.setLoginType(2);//凭条码和密码可登录
    		aId.setStatus(2);//未定义
    		aId.setValidDate(new Date());
    		aId.setInvalidDate(new Date(System.currentTimeMillis() + 3650L*24*3600*1000L));
    		if(aId.getType() == null || (aId.getType() != null && aId.getType().trim().equals(""))){
    			aId.setType(null);
    		}    		
    		validateExceptionMap =  validate.checkAdditionalIdProperty(aId);
    		if(user.getTenant() != null)
    			aId.setTenant(user.getTenant());
		}
		if(user.getTenant() != null)
			user.getCard().setTenant(user.getTenant());
		if(validateExceptionMap.size()>0){
			return validateExceptionMap;
		}
		
		//需要使用NewUserBean实体类，来实现使用邮件激活用户的注册方式
		NewUserBean newUserBean = null;
		try{
			newUserBean = (NewUserBean)user;
		}catch(ClassCastException e){
			log.debug(e.getMessage());
			errorMap.put("$WrongUserEntity", i18.getString("$WrongUserEntity"));
			return errorMap;
		}
		
		//需要激活
		newUserBean.setActive(0);
		//激活码
		newUserBean.setActiveCode(userManageUtilService.generateRandomCode(8));
		//注册日期
		newUserBean.setRegisterDate(new Date());		
		
		/******************additionalBusinessLogic*************************/
		errorMap.putAll(additionalBusinessLogic(newUserBean));
		
		//发送激活邮件
		boolean  isSendMailOk = userManageUtilService.sendMail(newUserBean.getLocalLoginId(),
									  newUserBean.getEmail(),
									  newUserBean.getActiveCode(),
									  "register","");
		if(!isSendMailOk){
			errorMap.put("$ActiveMail.sendFailed", i18.getString("$ActiveMail"));
		}
		
		//邮件发送失败将返回
		if (errorMap.size() != 0)
 			return errorMap;
		
		//持久化
		try {
			newUserDao.add(newUserBean);
		} catch (EntityCRUDException e) {
			log.info(e.getMessage());
		}
		return errorMap;		
	}
	
	/**
	 * @param user(用户实体对象UMPrincipal),captchaCode(人机区分校验信息)
	 * @return Map<String,String> 错误消息
	 */
	public Map<String, String> register(UMPrincipal user, String captchaCode) {
		
		Map<String, String> errorMap = new HashMap<String, String>();
		
		if(captchaCode == null || captchaCode.trim().equals("") || !validate.checkCaptchaCode(captchaCode)){
			errorMap.put("$CaptchaCode.correctCheck", i18.getString("$CaptchaCode.correctCheck"));
			return errorMap;
		}else{
			return register(user);
		}
	}
	
	/**
	 * @param activeCode 激活码
	 * @return String 反馈信息
	 */
	public String activeNewUser(String activeCode) {
		NewUserBean aUser = new NewUserBean();
		aUser = newUserDao.getByActiveCode(activeCode);

		//已经激活
		if(aUser.getActive() == 1)
			return "already";
		else if(aUser.getActive() == 0 && aUser.getLoginId() != null){
			//激活
			aUser.setActive(1);
			//修改用户状态为“启用”状态
			aUser.setStatus(2);
			//持久化
			try {
				newUserDao.update(aUser);
			} catch (EntityCRUDException e) {
				log.debug(e.getMessage());
				return "failed";
			}
			return "success";
		}else
			return "failed";
	} 
}
