package com.cnebula.um.cache;



import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
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

@EasyService(properties={@Property(name="id",value="PartyLoadUMLoginCacheImpl")})
public class PartyLoadUMLoginCacheImpl implements IUMLoginCache {
	
	@ESRef
	protected ILog log;
	
	@ESRef
	protected ICacheProvider cacheProvider;
	
	@ESRef
	protected IEasyServiceConfAdmin confAdmin;
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService crudPrivateService;
	
	protected UMConfig config;
	
	ICache userInfoCache=null;
	
	private static String CACHE_USERINFO="userInfo";
	private static String PREFIX_CARD="card_";
	private static String PREFIX_ADDI="addi_";
	private static String PREFIX_UMPRINCIPAL="um_";
	private static String LOG_START="用户数据缓存(动态置换)启动成功！";
	private static String LOG_START_ERROR="用户数据缓存(动态置换)启动失败！！！！找不到配置文件或服务器内部错误！忽略缓存！";
	
	protected void activate(ComponentContext ctx) {
		if(startSuccess()){
			log.info(LOG_START);
		}else{
			log.error(LOG_START_ERROR);
		}
			
	}
	
	private boolean startSuccess(){
		if(cacheProvider!=null){
			userInfoCache=cacheProvider.getCache(CACHE_USERINFO);
		}
		if(userInfoCache==null){
			return false;
		}
		return true;
		
	}
	
	private boolean validate(){
		if(userInfoCache==null) {
			log.debug(LOG_START_ERROR);
			return false;
		}
		return true;
	}
	
	private void putUserInuserInfoCache(String key,UMPrincipal u){
		if(!validate()) {
			return;
		}
		userInfoCache.putElement(key, u);
	}

	private UMPrincipal getUserFromDb(String prefix,String field,String value){
		UMPrincipal u=null;
		EntityQuery eq=EntityQuery.createExactQuery(field, value);
		if(prefix.equals(PREFIX_ADDI)){
			AdditionalId aid=crudPrivateService.querySingle(AdditionalId.class, eq);
			if(aid!=null)u=aid.getOwner();
		}else if(prefix.equals(PREFIX_CARD)){
			Card card=crudPrivateService.querySingle(Card.class, eq);
			if(card!=null) u=card.getPrinciple();
		}else if(prefix.equals(PREFIX_UMPRINCIPAL)){
			u=crudPrivateService.querySingle(UMPrincipal.class, eq);
		}
		return u;
	}
	
	private UMPrincipal getUser(String prefix,String field,String value){
		UMPrincipal u=null;
		if(validate()){
			u=(UMPrincipal) userInfoCache.getElement(prefix+value);
			if(u == null){
				u=getUserFromDb(prefix,field, value);
				putUserInuserInfoCache(prefix+value, u);
			}
		}else{
			u=getUserFromDb(prefix,field, value);
		}
		if(u == null)
			return u;
		else
			return u.clone();
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
		putUserInuserInfoCache(PREFIX_ADDI+addCode, u);
	}

	public void putUserByCardCode(String cardCode, UMPrincipal u) {
		putUserInuserInfoCache(PREFIX_CARD+cardCode, u);
	}

	public void putUserByLoginId(String loginID, UMPrincipal u) {
		putUserInuserInfoCache(PREFIX_UMPRINCIPAL+loginID, u);
	}
}
