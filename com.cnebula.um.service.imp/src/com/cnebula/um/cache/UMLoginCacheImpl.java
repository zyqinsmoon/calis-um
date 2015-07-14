package com.cnebula.um.cache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.cache.CacheException;
import com.cnebula.common.cache.ICache;
import com.cnebula.common.cache.ICacheProvider;
import com.cnebula.common.conf.IEasyServiceConfAdmin;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.log.ILog;
import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.Card;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.service.UMConfig;

//@EasyService
public class UMLoginCacheImpl implements IUMLoginCache {
	
	@ESRef
	protected ILog log;
	
	@ESRef
	protected ICacheProvider cacheProvider;
	
	@ESRef
	protected IEasyServiceConfAdmin confAdmin;
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService crudPrivateService;
	
	protected UMConfig config;
	
	ICache loginNameAndPasswordCache=null;
	
	ICache userInfoCache=null;
	
	private static String CONFIG_PATH="config_path";
	
	private static String CACHE_LOGINNAMEANDPASSWORD="loginNameAndPassword";
	private static String CACHE_USERINFO="userInfo";
	private static String CACHE_USER_CONFIGFILE="CacheConfig.xml";
	
	private static String PREFIX_CARD="card_";
	private static String PREFIX_ADDI="addi_";
	private static String PREFIX_UMPRINCIPAL="um_";
	
	
	private static String LOG_START="用户数据缓存启动成功！";
	private static String LOG_START_ERROR="用户数据缓存启动失败！！！！";
	private static String ERROR_FILE_NOT_EXIST="用户数据缓存配置文件不存在！";
	
	protected void activate(ComponentContext ctx) {
		if(startSuccess()){
			log.info(LOG_START);
		}else{
			log.error(LOG_START_ERROR);
		}
			
	}
	
	private boolean startSuccess(){
		try {
			Map<String,String> loginAndNameCachePro=new HashMap<String,String>();
			loginAndNameCachePro.put(CONFIG_PATH, null);
			loginNameAndPasswordCache=cacheProvider.addCache(CACHE_LOGINNAMEANDPASSWORD, loginAndNameCachePro);

			Map<String,String> userInfoCachePro=new HashMap<String,String>();
			userInfoCachePro.put(CONFIG_PATH, null);
			userInfoCache=cacheProvider.addCache(CACHE_USERINFO, userInfoCachePro);
		} catch (CacheException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	private boolean validate(){
		if(loginNameAndPasswordCache==null||userInfoCache==null) {
			return false;
		}
		config = confAdmin.get("calisUM", UMConfig.class);
		return true;
	}
	
	private void putUserInuserInfoCache(String key,UMPrincipal u){
		if(!validate()) {
			return;
		}
		userInfoCache.putElement(key, u);
	}
	
	private void putUserInloginNameAndPasswordCache(String key,UMPrincipal u){
		if(!validate()) {
			return;
		}
		String password=u==null?null:u.getPassword();
		loginNameAndPasswordCache.putElement(key, password);
	}
	
	private UMPrincipal getUserFromDb(String prefix,String field,String value){
		UMPrincipal u=null;
		EntityQuery eq=EntityQuery.createExactQuery(field, value);
		if(prefix.equals(PREFIX_ADDI)){
			AdditionalId aid=crudPrivateService.querySingle(AdditionalId.class, eq);
			u=aid.getOwner();
		}else if(prefix.equals(PREFIX_CARD)){
			Card card=crudPrivateService.querySingle(Card.class, eq);
			u=card.getPrinciple();
		}else if(prefix.equals(PREFIX_UMPRINCIPAL)){
			u=crudPrivateService.querySingle(UMPrincipal.class, eq);
		}
		return u;
	}
	
	
	private UMPrincipal getUser(String prefix,String field,String value){
		UMPrincipal u=null;
		if(loginNameAndPasswordCache.getElement(prefix+value)==null){
			return u;
		}
		u=(UMPrincipal) userInfoCache.getElement(prefix+value);
		if(u!=null){
			return u;
		}
		u=getUserFromDb(prefix,field, value);
		putUserInloginNameAndPasswordCache(prefix+value, u);
		putUserInuserInfoCache(prefix+value, u);
		return u;
			
	}
	
	public UMPrincipal getUserByAddCode(String addCode) {
		return getUser(PREFIX_ADDI, "code", addCode);
	}

	public UMPrincipal getUserByCardCode(String cardCode) {
		return getUser(PREFIX_CARD, "code", cardCode);
	}

	public UMPrincipal getUserByLoginId(String loginID) {
		return getUser(PREFIX_UMPRINCIPAL, "loginId", loginID);
	}


	public void putUserByAddCode(String addCode, UMPrincipal u) {
		putUserInloginNameAndPasswordCache(PREFIX_ADDI+addCode, u);
		putUserInuserInfoCache(PREFIX_ADDI+addCode, u);
	}

	public void putUserByCardCode(String cardCode, UMPrincipal u) {
		putUserInloginNameAndPasswordCache(PREFIX_CARD+cardCode, u);
		putUserInuserInfoCache(PREFIX_CARD+cardCode, u);
	}

	public void putUserByLoginId(String loginID, UMPrincipal u) {
		putUserInloginNameAndPasswordCache(PREFIX_UMPRINCIPAL+loginID, u);
		putUserInuserInfoCache(PREFIX_UMPRINCIPAL+loginID, u);
	}

}
