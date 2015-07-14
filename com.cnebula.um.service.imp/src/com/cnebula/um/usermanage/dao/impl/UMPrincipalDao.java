package com.cnebula.um.usermanage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.ComponentContext;

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
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.ejb.saas.UMTenant;
import com.cnebula.um.usermanage.dao.IUMPrincipalDao;

/**
 * 机构码的拼接移到UAS中com.cnebula.uas.service.UASMultipleOrgListener
 * 
 * @author Administrator
 *
 */

@EasyService(txType = TxType.CMT,properties={@Property(name="id", value="UMUserManage")})
public class UMPrincipalDao implements IUMPrincipalDao {
	
	@ESRef(target = "(unit=#{umunit})")
	private IEntityCRUDService entityCRUDService;

	@ESRef(target = "(unit=#{umunit})")
	private IEntityCRUDPrivateService entityPrivateService;

	protected void activate(ComponentContext context) {
		
	}
	
	public void add(UMPrincipal ump) throws EntityCRUDException {
		if(ump.getCard() != null && ump.getCard().getCode() != null){
			entityPrivateService.create(ump.getCard());
			EntityQuery q = EntityQuery.createExactQuery("code", ump.getCard().getCode());
			Card c = entityPrivateService.querySingle(Card.class, q);
			ump.setCard(c);
			c.setPrinciple(ump);
			entityCRUDService.create(ump);
		}else
			entityCRUDService.create(ump);
	}

	public void update(UMPrincipal ump) throws EntityCRUDException {
		EntityPropertityItemStatus rootOpInfo = new EntityPropertityItemStatus();
		rootOpInfo.setItem(ump);
		rootOpInfo.setStatus(EntityPropertityItemStatus.UPDATE);
		entityCRUDService.op(rootOpInfo);	
	}

	public void deleteByLoginId(String loginId) throws EntityCRUDException {
		EntityQuery qu = EntityQuery.createExactQuery("loginId", loginId);
		UMPrincipal  ump  = entityPrivateService.querySingle(UMPrincipal.class, qu);
		List<AdditionalId> aIds = null;
		List<String> aIdCodes = new ArrayList<String>();
		String cardCode = null;
		if(ump != null){
			if(ump.getCard() != null && ump.getCard().getCode() != null && !ump.getCard().getCode().trim().equals("")){
				cardCode = ump.getCard().getCode();
			}
			//解除关系
			if(ump.getAdditionalIds() != null){
				aIds = ump.getAdditionalIds();
				for(AdditionalId a: aIds){
					a.setOwner(null);
					aIdCodes.add(a.getCode());
				}
			}
			ump.getAdditionalIds().clear();
			ump.getCard().setPrinciple(null);
			ump.setCard(null);
			entityPrivateService.update(ump);
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
			ump = entityPrivateService.querySingle(UMPrincipal.class, qu);
			entityPrivateService.delete(ump);
		}
	}	
	
	public UMPrincipal getByLoginId(String loginId) {
		UMPrincipal aUser = new UMPrincipal();
		EntityQuery q = EntityQuery.createExactQuery("loginId", loginId);
		List<UMPrincipal> umplist = entityPrivateService.query(
				UMPrincipal.class, q);
		if (umplist.size() == 0 || umplist == null) {
			return aUser;
		} else {
			aUser = umplist.get(0).clone();
		}
		return aUser;
	}

	public UMPrincipal getByEmail(String email) {
		UMPrincipal aUser = new UMPrincipal();
		EntityQuery q = EntityQuery.createExactQuery("email", email);
		List<UMPrincipal> umpList = entityPrivateService.query(UMPrincipal.class, q);
		if (umpList.size() == 0 || umpList == null) {
			return aUser;
		} else {
			aUser = umpList.get(0).clone();
			return aUser;
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

	public UMTenant getTenantByTenantID(String tenantId) {
		EntityQuery q = EntityQuery.createExactQuery("tenantId", tenantId);
		UMTenant tenant = entityPrivateService.querySingle(UMTenant.class, q);
		return tenant;
	}
}
