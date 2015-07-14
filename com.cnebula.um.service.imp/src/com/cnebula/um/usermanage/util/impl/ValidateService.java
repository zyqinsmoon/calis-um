package com.cnebula.um.usermanage.util.impl;


import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.EntityUIPropertyInfo;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.ValidateException;
import com.cnebula.common.ejb.relation.IEntityFullInfoPool;
import com.cnebula.common.es.IEasyServiceManager;
import com.cnebula.common.log.ILog;
import com.cnebula.common.reflect.ClassInfo;
import com.cnebula.common.reflect.IMember;
import com.cnebula.common.security.auth.IEasyCaptchaService;
import com.cnebula.common.security.auth.ILoginService;
import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.Card;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.usermanage.dao.IUMPrincipalDao;
import com.cnebula.um.usermanage.dao.impl.UMPrincipalDao;
import com.cnebula.um.usermanage.util.IValidateService;

@EasyService(properties={@Property(name="id", value="UMUserManage")})
public class ValidateService implements IValidateService {
	
	protected @ESRef IUMPrincipalDao umpDao = new UMPrincipalDao();
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityFullInfoPool entityInfoPool;
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService entityCRUDprivateService;
	
	protected @ESRef IEasyServiceManager easyServiceManager ;

	protected @ESRef ILog log;
	
	public boolean checkEmail(String email) {
		String emailInDb = umpDao.getByEmail(email).getEmail();
		if(emailInDb == null)
			return true;
		else{
			ILoginService loginService = (ILoginService) easyServiceManager.getService(this.getClass(), null, ILoginService.class.getName(), null);
			UMPrincipal user = (UMPrincipal)loginService.getUser();
			if(user.getEmail() != null && user.getEmail().equals(emailInDb))
				return true;
			else
				return false;
		}
	}
	
	public boolean checkLoginId(String loginId) {
		if(umpDao.getByLoginId(loginId).getLoginId() == null)
			return true;
		else
			return false;
	}
	
	public boolean checkCaptchaCode(String captchaCode) {
		IEasyCaptchaService easyCaptchaService = 
			(IEasyCaptchaService) easyServiceManager.getService(this.getClass(), null, IEasyCaptchaService.class.getName(), null);
		return easyCaptchaService.validate(captchaCode);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> checkUMPrincipalProperty(UMPrincipal ump) {
		
		Map<String,String> exceptionMap = new HashMap<String,String>();
		
		//UMPrincipal实体类中所有属性的propertyInfo
		Map<String,EntityUIPropertyInfo> propertyInfomap = new HashMap<String, EntityUIPropertyInfo>();
		propertyInfomap = entityInfoPool.getEntityUIInfo(UMPrincipal.class).getPropertyInfoMap();
		
		//UMPrincipal实体类中所有的成员变量
		ClassInfo<UMPrincipal> umpClassInfo = entityInfoPool.getEntityInfo(UMPrincipal.class).getClassInfo();
		Collection<IMember> memberCollect = umpClassInfo.getProperties();
		
		//校验实体类中所有成员变量
		for(IMember<?> im:memberCollect){
				EntityUIPropertyInfo euiPropertyInfo = propertyInfomap.get(im.getName());
				try {
					entityCRUDprivateService.check(euiPropertyInfo, im.getValue(ump), null, true);
				} catch (ValidateException e) {
					exceptionMap.put(euiPropertyInfo.getName(),e.getMessage());
				} catch (IllegalArgumentException e) {
					log.error(e.getMessage());
				} catch (IllegalAccessException e) {
					log.error(e.getMessage());
				} catch (InvocationTargetException e) {
					log.error(e.getMessage());
				}
		}
		return exceptionMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> checkAdditionalIdProperty(AdditionalId ad) {
		Map<String,String> exceptionMap = new HashMap<String,String>();
		
		//UMPrincipal实体类中所有属性的propertyInfo
		Map<String,EntityUIPropertyInfo> propertyInfomap = new HashMap<String, EntityUIPropertyInfo>();
		propertyInfomap = entityInfoPool.getEntityUIInfo(AdditionalId.class).getPropertyInfoMap();
		
		//UMPrincipal实体类中所有的成员变量
		ClassInfo<AdditionalId> umpClassInfo = entityInfoPool.getEntityInfo(AdditionalId.class).getClassInfo();
		Collection<IMember> memberCollect = umpClassInfo.getProperties();
		
		//校验实体类中所有成员变量
		for(IMember<?> im:memberCollect){
				EntityUIPropertyInfo euiPropertyInfo = propertyInfomap.get(im.getName());
				try {
					entityCRUDprivateService.check(euiPropertyInfo, im.getValue(ad), null, true);
				} catch (ValidateException e) {
					exceptionMap.put(euiPropertyInfo.getName(),e.getMessage());
				} catch (IllegalArgumentException e) {
					log.error(e.getMessage());
				} catch (IllegalAccessException e) {
					log.error(e.getMessage());
				} catch (InvocationTargetException e) {
					log.error(e.getMessage());
				}
		}
		return exceptionMap;
	}

	public boolean checkAdditionalId(String code) {
		EntityQuery q = EntityQuery.createLikeQuery("code", code);
		List<AdditionalId> aidsIndb = (List<AdditionalId>) entityCRUDprivateService.query(AdditionalId.class, q);
		if(aidsIndb == null || (aidsIndb != null && aidsIndb.size() <= 0))
			return true;
		else{
			boolean unique = true;
			for(AdditionalId a: aidsIndb){
				if(a.getCode() != null){
					String codeInDb = a.getCode().trim();
					if(codeInDb.indexOf(':') >= 0)
						codeInDb = codeInDb.substring(codeInDb.indexOf(':')+1);
					if(codeInDb.equals(code))
						unique = false;
				}
			}
			return unique;
		}
	}

	public boolean checkCard(String code) {
		EntityQuery q = EntityQuery.createLikeQuery("code", code);
		List<Card> cards = (List<Card>) entityCRUDprivateService.query(Card.class, q);
		if(cards == null || (cards != null && cards.size()<= 0))
			return true;
		else{
			boolean unique = true;
			for(Card c : cards){
				if(c.getCode() != null){
					String codeInDb = c.getCode();
					if(codeInDb.indexOf(':') >= 0)
						codeInDb = codeInDb.substring(codeInDb.indexOf(':')+1);
					if(codeInDb.equals(code))
						unique = false;
				}
			}
			return unique;
		}
	}
}
