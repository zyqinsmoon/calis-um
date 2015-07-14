package com.cnebula.um.usermanage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.es.TxType;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.Card;
import com.cnebula.um.ejb.entity.usr.NewUserBean;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.usermanage.dao.INewUserDao;

@EasyService(txType=TxType.CMT,properties={@Property(name="id", value="UMUserManage")})
public class NewUserDao implements INewUserDao {
	
	@ESRef(target="(unit=#{umunit})")
    private IEntityCRUDService entityCRUDService;
	
	@ESRef(target="(unit=#{umunit})")
    private IEntityCRUDPrivateService entityPrivateService;	
	
	public void add(NewUserBean newUserBean) throws EntityCRUDException {
		if(newUserBean.getCard() != null && newUserBean.getCard().getCode() != null){
			entityPrivateService.create(newUserBean.getCard());
			EntityQuery q = EntityQuery.createExactQuery("code", newUserBean.getCard().getCode());
			Card c = entityPrivateService.querySingle(Card.class, q);
			newUserBean.setCard(c);
			c.setPrinciple(newUserBean);
			entityCRUDService.create(newUserBean);
		}else
			entityCRUDService.create(newUserBean);
	}
	
	public void update(NewUserBean newUserBean) throws EntityCRUDException{
		EntityPropertityItemStatus rootOpInfo = new EntityPropertityItemStatus();
		rootOpInfo.setItem(newUserBean);
		rootOpInfo.setStatus(EntityPropertityItemStatus.UPDATE);
		entityPrivateService.op(rootOpInfo);
	}
	
	public void deleteByActiveCode(String activeCode) throws EntityCRUDException{
		EntityQuery q = EntityQuery.createExactQuery("activeCode", activeCode);
		NewUserBean  newUserBean  = entityPrivateService.querySingle(NewUserBean.class, q);
		List<AdditionalId> aIds = null;
		List<String> aIdCodes = new ArrayList<String>();
		String cardCode = null;
		if(newUserBean != null){
			if(newUserBean.getCard() != null && newUserBean.getCard().getCode() != null && !newUserBean.getCard().getCode().trim().equals("")){
				cardCode = newUserBean.getCard().getCode();
			}
			//解除关系
			if(newUserBean.getAdditionalIds() != null){
				aIds = newUserBean.getAdditionalIds();
				for(AdditionalId a: aIds){
					a.setOwner(null);
					aIdCodes.add(a.getCode());
				}
			}
			newUserBean.getAdditionalIds().clear();
			newUserBean.getCard().setPrinciple(null);
			newUserBean.setCard(null);
			entityPrivateService.update(newUserBean);
			//删除card
			if(cardCode != null){
				Card c = (Card) entityPrivateService.querySingle(Card.class, EntityQuery.createExactQuery("code", cardCode));
				c.setPrinciple(null);
				entityPrivateService.delete(c);
			}
			//删除AdditionalId
			if(aIdCodes.size() > 0){
				for(String code: aIdCodes){
						AdditionalId dba = (AdditionalId) entityPrivateService.querySingle(AdditionalId.class, EntityQuery.createExactQuery("code", code));
						entityPrivateService.delete(dba);
				}
			}
			//删除UMPrincipal
			newUserBean = entityPrivateService.querySingle(NewUserBean.class, q);
			entityPrivateService.delete(newUserBean);
		}
	}

	public NewUserBean getByActiveCode(String activeCode) {
		NewUserBean newUser = new NewUserBean();
		if (activeCode == null || activeCode.trim().equals("")) {
			return newUser;
		} else {
			EntityQuery q = EntityQuery.createExactQuery("activeCode",activeCode);
			List<NewUserBean> newUserList = entityPrivateService.query(NewUserBean.class, q);
			if (newUserList.size() <= 0)
				return newUser;
			else {
				newUser = newUserList.get(0).clone();
			}
			return newUser;
		}
	}

	public void deleteByLoginId(String loginId) throws EntityCRUDException {
		EntityQuery q = EntityQuery.createExactQuery("loginId", loginId);
		NewUserBean  newUserBean  = entityPrivateService.querySingle(NewUserBean.class, q);
		List<AdditionalId> aIds = null;
		List<String> aIdCodes = new ArrayList<String>();
		String cardCode = null;
		if(newUserBean != null){
			if(newUserBean.getCard() != null && newUserBean.getCard().getCode() != null && !newUserBean.getCard().getCode().trim().equals("")){
				cardCode = newUserBean.getCard().getCode();
			}
			//解除关系
			if(newUserBean.getAdditionalIds() != null){
				aIds = newUserBean.getAdditionalIds();
				for(AdditionalId a: aIds){
					a.setOwner(null);
					aIdCodes.add(a.getCode());
				}
			}
			newUserBean.getAdditionalIds().clear();
			newUserBean.getCard().setPrinciple(null);
			newUserBean.setCard(null);
			entityPrivateService.update(newUserBean);
			//删除card
			if(cardCode != null){
				Card c = (Card) entityPrivateService.querySingle(Card.class, EntityQuery.createExactQuery("code", cardCode));
				c.setPrinciple(null);
				entityPrivateService.delete(c);
			}
			//删除AdditionalId
			if(aIdCodes.size() > 0){
				for(String code: aIdCodes){
						AdditionalId dba = (AdditionalId) entityPrivateService.querySingle(AdditionalId.class, EntityQuery.createExactQuery("code", code));
						entityPrivateService.delete(dba);
				}
			}
			//删除UMPrincipal
			newUserBean = entityPrivateService.querySingle(NewUserBean.class, q);
			entityPrivateService.delete(newUserBean);
		}
	}

	public NewUserBean getByLoginId(String loginId) {
		NewUserBean newUser = new NewUserBean();
		if (loginId == null || loginId.trim().equals("")) {
			return newUser;
		} else {
			EntityQuery q = EntityQuery.createExactQuery("loginId", loginId);
			List<NewUserBean> newUserList = entityPrivateService.query(NewUserBean.class, q);
			if (newUserList.size() <= 0)
				return newUser;
			else {
				newUser = newUserList.get(0).clone();
			}
			return newUser;
		}
	}
	
	public Organization getOrganizationByCode(String code){
		EntityQuery q = EntityQuery.createExactQuery("code", code);
		Organization o = entityPrivateService.querySingle(Organization.class, q);
		if(o != null){
			return o;
		}else
			return null;
	}	
}
