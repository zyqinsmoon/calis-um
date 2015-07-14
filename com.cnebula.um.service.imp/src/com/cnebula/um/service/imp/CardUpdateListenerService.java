package com.cnebula.um.service.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.conf.IEasyServiceConfAdmin;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.manage.EntityPermissionExcuteStatus;
import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.common.ejb.manage.IEntityCRUDListener;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.ejb.relation.EntityInfo;
import com.cnebula.common.ejb.relation.EntityInfoPoolService;
import com.cnebula.common.ejb.relation.EntityProperty;
import com.cnebula.um.ejb.entity.usr.Card;
import com.cnebula.um.ejb.entity.usr.Cash;
import com.cnebula.um.ejb.entity.usr.HistoryCard;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.service.CardCashMapConfig;

@EasyService
public class CardUpdateListenerService implements IEntityCRUDListener{

	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService entityCRUDService;
	
	@ESRef
	IEasyServiceConfAdmin confAdmin;
	
	protected void activate(ComponentContext ctx) {
		entityCRUDService.registerEntityCRUDListener(Card.class.getName(), this);
	}
	
	private HistoryCard createHistoryFromCard(Card card) {
		HistoryCard rt = new HistoryCard();
		try{
			rt = new HistoryCard();
			EntityInfoPoolService poolService = (EntityInfoPoolService) entityCRUDService.getEntityFullInfoPool();
			EntityInfo<Card> cardEI = poolService.getEntityInfo(Card.class);
			EntityInfo<HistoryCard> hyCardEI = poolService.getEntityInfo(HistoryCard.class);
			Map<String, EntityProperty> cardEIPropertyMap = cardEI.getPropertyMap();
			for(String p : cardEIPropertyMap.keySet()){
				hyCardEI.getProperty(p).setValue(rt, cardEIPropertyMap.get(p).getValue(card));
			}
			rt.setMaintainInfo(new MaintainInfo());
			rt.getMaintainInfo().setCreator(card.getMaintainInfo().getCreator());
			rt.getMaintainInfo().setCreateTime(card.getMaintainInfo().getCreateTime());
		}catch (Exception e) {//should never happen
			throw new RuntimeException(e);
		}
		return rt;
	}
	
	public void afterCRUDEvent(EntityPropertityItemStatus rootOpInfo)throws EntityCRUDException{
		if (!rootOpInfo.isUpdate()){
			return;
		}
		List<EntityPropertityItemStatus> itemStatusList = rootOpInfo.getOpInfo().get("status");
		if (itemStatusList == null || itemStatusList.isEmpty()){
			return;
		}
		//is revoke card
		if (itemStatusList.get(0).isUpdate() &&  "0".equals(itemStatusList.get(0).getItem()+"") ){
			Card card = (Card)rootOpInfo.getItem();
			UMPrincipal ump = card.getPrinciple();
			CardCashMapConfig cardCashMapConfig = confAdmin.get("cardCashMap", CardCashMapConfig.class);
			//return cash
			String cashTypeStr = cardCashMapConfig.getCardCashMap().get( card.getType()+"" ).getCashType();
			if (cashTypeStr != null){
				int cashType = Integer.parseInt( cashTypeStr );
				List<Cash> orderedCashList = new ArrayList<Cash>();
				orderedCashList.addAll(ump.getCashs());
				Collections.sort(orderedCashList, new Comparator<Cash>(){
					public int compare(Cash o1, Cash o2) {
						return o2.getMaintainInfo().getCreateTime().compareTo( o1.getMaintainInfo().getCreateTime());
					}
				});
				for (Cash cash :  orderedCashList){
					if (cashType == cash.getType()){
						if (cash.getStatus() == Cash.CASH_CREDIT){
//							cash.setStatus(Cash.CASH_CREDIT);
//							EntityPropertityItemStatus  cashOpStatus = new EntityPropertityItemStatus();
//							cashOpStatus.setItem(cash);
//							cashOpStatus.setStatus(EntityPropertityItemStatus.UPDATE);
//							entityCRUDClientHelper.op(cashOpStatus);
							entityCRUDService.doOperationByIds(Cash.class, Arrays.asList(cash.getId()), entityCRUDService.queryOperation(Cash.class.getName(), "debitCash"), null);
						}
						break;
					}
				}
				
			}
			
			EntityPropertityItemStatus umpStatus = new EntityPropertityItemStatus(ump,EntityPropertityItemStatus.UPDATE);
			EntityPropertityItemStatus cardDisconnectStatus = 
				new EntityPropertityItemStatus(card, EntityPropertityItemStatus.DISCONNECT);
			card.setPrinciple(null);
			card.setStatus(0);
			String cardDesc = card.getMaintainInfo().getDescription();
			if (cardDesc == null){
				cardDesc = "";
			}
			card.getMaintainInfo().setDescription("原持卡人:" + ump.getId() + "," + ump.getName() + "," + cardDesc);
			ump.setCard(null);
			List<EntityPropertityItemStatus> cardStatusList = new ArrayList<EntityPropertityItemStatus>();
			
			cardStatusList.add(cardDisconnectStatus);
			
			umpStatus.getOpInfo().put("card", cardStatusList);
			
			List<EntityPropertityItemStatus> historyCardStatusList = new ArrayList<EntityPropertityItemStatus>();
			
			HistoryCard historyCard = createHistoryFromCard(card) ;
			historyCard.getMaintainInfo().setDescription(cardDesc);
			historyCard.setId(null);
			EntityPropertityItemStatus historyCardCreateStatus = 
				new EntityPropertityItemStatus(historyCard,EntityPropertityItemStatus.CREATE);
			
			EntityPropertityItemStatus historyCardConnectStatus = 
				new EntityPropertityItemStatus(historyCard,EntityPropertityItemStatus.NOP);
			ump.getHistoryCards().add(historyCard);
			historyCard.setPrinciple(ump);
			historyCardStatusList.add(historyCardCreateStatus);
			historyCardStatusList.add(historyCardConnectStatus);
			
			umpStatus.getOpInfo().put("historyCards", historyCardStatusList);
			entityCRUDService.op(umpStatus);
		}

	}

	public void afterCRUDSuccessEvent(EntityPropertityItemStatus rootOpInfo,
			List<EntityPermissionExcuteStatus> permissionStatusForLog) {
	}

	public void beforeCRUDEvent(EntityPropertityItemStatus rootOpInfo) {
	}

}
