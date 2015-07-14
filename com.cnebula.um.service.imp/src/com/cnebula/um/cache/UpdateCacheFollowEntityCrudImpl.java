package com.cnebula.um.cache;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.manage.EntityPermissionExcuteStatus;
import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.common.es.IEasyServiceManager;
import com.cnebula.common.es.SessionContext;
import com.cnebula.common.log.ILog;
import com.cnebula.common.security.auth.ILoginService;
import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.Card;
import com.cnebula.um.ejb.entity.usr.HistoryCard;
import com.cnebula.um.ejb.entity.usr.NewUserBean;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;

@EasyService
public class UpdateCacheFollowEntityCrudImpl implements IUpdateCacheFollowEntityCrud {
	
	@ESRef
	protected ILog log;
	
	@ESRef
	protected IUMLoginCache cache;
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDService crudService;
	
	@ESRef
	private IEasyServiceManager easyServiceManager;
	
	protected ILoginService loginService;
	
	private static String LOG_START="更新用户数据缓存启动成功(EntityCRUD)！";
	
	protected void activate(ComponentContext ctx) {
		crudService.registerEntityCRUDListener(UMPrincipal.class.getName(), this);
		crudService.registerEntityCRUDListener(NewUserBean.class.getName(), this);
		crudService.registerEntityCRUDListener(Card.class.getName(), this);
		crudService.registerEntityCRUDListener(AdditionalId.class.getName(), this);
		log.info(LOG_START);
	}
	
	public void afterCRUDEvent(EntityPropertityItemStatus rootOpInfo) throws EntityCRUDException {
	}

	public  synchronized void afterCRUDSuccessEvent(EntityPropertityItemStatus rootOpInfo, List<EntityPermissionExcuteStatus> permissionStatusForLog) throws EntityCRUDException {
		loginService = (ILoginService) easyServiceManager.getService(this.getClass(), null, ILoginService.class.getName(), null);
		Class<?> type = rootOpInfo.getItem().getClass();
		switch (rootOpInfo.getStatus()) {
		case EntityPropertityItemStatus.CREATE:
			if (rootOpInfo.getItem() instanceof UMPrincipal){
				UMPrincipal u = (UMPrincipal)rootOpInfo.getItem();
				u = u.clone();
				cache.putUserByLoginId(u.getLoginId(), u);
				if (u.getCard() != null){
					cache.putUserByCardCode(u.getCard().getCode(), u);
				}
				
				//for additional id 
				List<EntityPropertityItemStatus> aidEpList =  rootOpInfo.getOpInfo().get("additionalIds");
				if(aidEpList!=null){
					for(EntityPropertityItemStatus ep : aidEpList){
						if(ep.isCreate()||ep.isUpdate()){
							cache.putUserByAddCode(((AdditionalId)ep.getItem()).getCode(), u);
						}else if(ep.isDisConnect()||ep.isDelete()){
							cache.putUserByAddCode(((AdditionalId)ep.getItem()).getCode(), null);
						}
					}
				}
			}else if (type == Card.class){
				Card c = (Card)rootOpInfo.getItem();
				if (c.getPrinciple() != null){
					cache.putUserByCardCode(c.getCode(), c.getPrinciple());
				}
				
			}else if(type == AdditionalId.class){
				AdditionalId aid = (AdditionalId) rootOpInfo.getItem() ; 
				if(aid.getOwner()!=null){
					cache.putUserByAddCode(aid.getCode(), aid.getOwner());
				}
			}
			break;
		case EntityPropertityItemStatus.DELETE:
			if (rootOpInfo.getItem() instanceof UMPrincipal){
				UMPrincipal u = (UMPrincipal)rootOpInfo.getItem();
				cache.putUserByLoginId(u.getLoginId(), null);
				if (u.getCard() != null){
					cache.putUserByCardCode(u.getCard().getCode(), null);
				}
				if(u.getAdditionalIds()!=null){
					for(AdditionalId aid : u.getAdditionalIds()){
						cache.putUserByAddCode(aid.getCode(), null);
					}
				}
			}else if (type == Card.class){
				Card c = (Card)rootOpInfo.getItem();
				if (c.getPrinciple() != null){
					UMPrincipal u=cache.getUserByCardCode(c.getCode());
					cache.putUserByCardCode(c.getCode(), null);
					u.setCard(null);
				}
				
			}else if(type == AdditionalId.class){
				AdditionalId aid = (AdditionalId) rootOpInfo.getItem() ; 
				if(aid.getOwner()!=null){
					cache.putUserByAddCode(aid.getCode(), null);
				}
			}
			break;
		case EntityPropertityItemStatus.UPDATE:
			UMPrincipal u = null;
			if (rootOpInfo.getItem() instanceof UMPrincipal){
				u = (UMPrincipal)rootOpInfo.getItem();
				u = u.clone();
				cache.putUserByLoginId(u.getLoginId(), u);
				if (u.getCard() != null){
					cache.putUserByCardCode(u.getCard().getCode(), u);
				}
				for (HistoryCard hc : u.getHistoryCards()){
					cache.putUserByCardCode(hc.getCode(), null);
				}
				List<EntityPropertityItemStatus> aidEpList =  rootOpInfo.getOpInfo().get("additionalIds");
				
				if(aidEpList != null)
					for(EntityPropertityItemStatus ep : aidEpList){
						if(ep.isCreate()||ep.isUpdate()){
							cache.putUserByAddCode(((AdditionalId)ep.getItem()).getCode(), u);
						}else if(ep.isDisConnect()||ep.isDelete()){
							cache.putUserByAddCode(((AdditionalId)ep.getItem()).getCode(), null);
						}
					}
			}
			if (type == Card.class){
				Card c = (Card)rootOpInfo.getItem();
				if (c.getPrinciple() != null){
					cache.putUserByCardCode(c.getCode(), c.getPrinciple());
				}
				
			}
			if(type == AdditionalId.class){
				AdditionalId aid = (AdditionalId) rootOpInfo.getItem() ; 
				if(aid.getOwner()!=null){
					cache.putUserByAddCode(aid.getCode(), aid.getOwner());
				}
			}
			/**
			 *  add by liyi
			 *	更新用户的Session
			*/
			final UMPrincipal user = u;
			if(u != null){
				UMPrincipal ump = SessionContext.getCurrentUser();
				if (u != null && u.getId()!=null &&ump!=null &&u.getId().equals(ump.getId())) {
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						public Object run() {
							loginService.updateUser(user);
							return null;
						}
					});
				}
			}
			break;
		default:
			break;
		}
	}

	public void beforeCRUDEvent(EntityPropertityItemStatus rootOpInfo)
			throws EntityCRUDException {
	}
}
