package com.cnebula.um.aas;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.TxType;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.ejb.manage.EntityPermissionExcuteStatus;
import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.common.ejb.manage.IEntityCRUDListener;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.log.ILog;
import com.cnebula.common.remote.Consts;
import com.cnebula.common.remote.ServiceURL;
import com.cnebula.common.remote.ws.EasyServiceClient;
import com.cnebula.common.security.auth.IAuthCenter;
import com.cnebula.common.security.auth.IIdentityProviderQueryService;
import com.cnebula.common.security.auth.ILoginValidateService;
import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.AuthCenter;
import com.cnebula.um.ejb.entity.usr.Card;
import com.cnebula.um.ejb.entity.usr.HistoryCard;
import com.cnebula.um.ejb.entity.usr.NewUserBean;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.setup.ISetupService;


@EasyService(interfaces={ICacheByConnection.class,IIdentityProviderQueryService.class})
//(txType=TxType.CMT)
public class CacheByConnection implements ICacheByConnection ,IIdentityProviderQueryService {

	private static final String SELECT_T_ID_FROM_SAAS_TENANT_T_WHERE_T_TENANTID = "select t.id from saas_tenant t where t.tenantid=?";

	@ESRef(target="(name=#{umds})")
	protected DataSource ds;
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService crudPrivateService;
	
	@ESRef(target="(component.name=com.cnebula.um.setup.UMServerDBInit)")
	protected ISetupService is;
	
	public IEntityCRUDPrivateService getCrudPrivateService() {
		return crudPrivateService;
	}

	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDService crudService;
	
	@ESRef
	protected UserTransaction tx;
	
	@ESRef
	protected ILog log;
	
	String aidQuerySql ; 
	
	private Map<String, ILoginValidateService> IdpLoginValidateServiceCache = 
		new HashMap<String, ILoginValidateService>();
	
	private List<IAuthCenter> authCenters = new ArrayList<IAuthCenter>();

	protected Map<String, Organization> orgIdCaches = new HashMap<String, Organization>();
	protected Map<String, Tenant> tenantIdCaches = new HashMap<String, Tenant>();
	protected Map<String, UMPrincipal> userIdCaches = new HashMap<String, UMPrincipal>();
	protected Map<String, UMPrincipal> cardIdCaches = new HashMap<String, UMPrincipal>();
	protected Map<String, UMPrincipal> aidCaches = new HashMap<String, UMPrincipal>();
 	
	public void loadUserCache(int limit,String tenantId) throws SQLException {
		loadUserCache(limit, null, null,tenantId);
	}
	
	protected void activate(ComponentContext ctx) {

	}
	
	public synchronized UMPrincipal getUser(String id){
		return userIdCaches.get(id);
	}
	
	
	public synchronized UMPrincipal getUserByCardId(String cid){
		return cardIdCaches.get(cid);
	}


	public void afterCRUDEvent(EntityPropertityItemStatus rootOpInfo) throws EntityCRUDException {
		
	}

	public  synchronized void afterCRUDSuccessEvent(EntityPropertityItemStatus rootOpInfo, List<EntityPermissionExcuteStatus> permissionStatusForLog) throws EntityCRUDException {
		Class type = rootOpInfo.getItem().getClass();
		switch (rootOpInfo.getStatus()) {
		case EntityPropertityItemStatus.CREATE:
			if (rootOpInfo.getItem() instanceof UMPrincipal){
				UMPrincipal u = (UMPrincipal)rootOpInfo.getItem();
				u = u.clone();
				userIdCaches.put(u.getLoginId(), u);
				if (u.getCard() != null){
					cardIdCaches.put(u.getCard().getCode(), u);
				}
				
				//for additional id 
				List<EntityPropertityItemStatus> aidEpList =  rootOpInfo.getOpInfo().get("additionalIds");
				if(aidEpList!=null){
					for(EntityPropertityItemStatus ep : aidEpList){
						if(ep.isCreate()||ep.isUpdate()){
							aidCaches.put(((AdditionalId)ep.getItem()).getCode(), u);
						}else if(ep.isDisConnect()||ep.isDelete()){
							aidCaches.remove(((AdditionalId)ep.getItem()).getCode());
						}
					}
				}
			}else if (type == Card.class){
				Card c = (Card)rootOpInfo.getItem();
				if (c.getPrinciple() != null){
					cardIdCaches.put(c.getCode(), c.getPrinciple());
				}
				
			}else if (type == Organization.class){
				Organization o = (Organization)rootOpInfo.getItem();
				orgIdCaches.put(o.getId(), o);
			}else if (type == Tenant.class){
				Tenant t = (Tenant)rootOpInfo.getItem();
				tenantIdCaches.put(t.getId(), t);
				}
			else if(type == AdditionalId.class){
				AdditionalId aid = (AdditionalId) rootOpInfo.getItem() ; 
				if(aid.getOwner()!=null){
					aidCaches.put(aid.getCode(), aid.getOwner());
				}
			}
			break;
		case EntityPropertityItemStatus.DELETE:
			if (rootOpInfo.getItem() instanceof UMPrincipal){
				UMPrincipal u = (UMPrincipal)rootOpInfo.getItem();
				u = userIdCaches.remove(u.getLoginId());
				if (u.getCard() != null){
					cardIdCaches.remove(u.getCard().getCode());
				}
				if(u.getAdditionalIds()!=null){
					for(AdditionalId aid : u.getAdditionalIds()){
						aidCaches.remove(aid.getCode());
					}
				}
			}else if (type == Card.class){
				Card c = (Card)rootOpInfo.getItem();
				if (c.getPrinciple() != null){
					UMPrincipal u = cardIdCaches.remove(c.getCode());
					u.setCard(null);
				}
				
			}else if (type == Organization.class){
				Organization o = (Organization)rootOpInfo.getItem();
				orgIdCaches.remove(o.getId());
			}else if (type == Tenant.class){
				Tenant t = (Tenant)rootOpInfo.getItem();
				tenantIdCaches.remove(t.getId());
				}
			else if(type == AdditionalId.class){
				AdditionalId aid = (AdditionalId) rootOpInfo.getItem() ; 
				if(aid.getOwner()!=null){
					aidCaches.remove(aid.getCode());
				}
			}
			break;
		case EntityPropertityItemStatus.UPDATE:
			if (rootOpInfo.getItem() instanceof UMPrincipal){
				UMPrincipal u = (UMPrincipal)rootOpInfo.getItem();
				u = u.clone();
				userIdCaches.put(u.getLoginId(), u);
				if (u.getCard() != null){
					cardIdCaches.put(u.getCard().getCode(), u);
				}
				for (HistoryCard hc : u.getHistoryCards()){
					cardIdCaches.remove(hc.getCode());
				}
				List<EntityPropertityItemStatus> aidEpList =  rootOpInfo.getOpInfo().get("additionalIds");
				
				for(EntityPropertityItemStatus ep : aidEpList){
					if(ep.isCreate()||ep.isUpdate()){
						aidCaches.put(((AdditionalId)ep.getItem()).getCode(), u);
					}else if(ep.isDisConnect()||ep.isDelete()){
						aidCaches.remove(((AdditionalId)ep.getItem()).getCode());
					}
				}
					
			}else if (type == Card.class){
				Card c = (Card)rootOpInfo.getItem();
				if (c.getPrinciple() != null){
					cardIdCaches.put(c.getCode(), c.getPrinciple());
				}
				
			}else if (type == Organization.class){
				Organization o = (Organization)rootOpInfo.getItem();
				orgIdCaches.put(o.getId(), o);
			}else if (type == Tenant.class){
				Tenant t = (Tenant)rootOpInfo.getItem();
				tenantIdCaches.put(t.getId(),t);
				}
			else if(type == AdditionalId.class){
				AdditionalId aid = (AdditionalId) rootOpInfo.getItem() ; 
				if(aid.getOwner()!=null){
					aidCaches.put(aid.getCode(), aid.getOwner());
				}
			}
			break;
		default:
			break;
		}
	}

//	private void initLoginValidateService(IAuthCenter ac) {
//		try{
//			ILoginValidateService lvs  = EasyServiceClient.lookup(
//					new ServiceURL(ac.getLoginValidServiceURL()), ILoginValidateService.class, ac.getTarget());
//			IdpLoginValidateServiceCache.put(ac.getId(), lvs);
//		}catch (Exception e) {
//			log.error("初始化认证中心"+ac.getName()+"校验服务失败",e);
//		}
//	}

	public void beforeCRUDEvent(EntityPropertityItemStatus rootOpInfo) throws EntityCRUDException {
		
	}

	public  synchronized void loadUserCache(int limit, String fromYYYYMMDD, String toYYYYMMDD,String tenantId) throws SQLException {

		
		
		//加载机构信息
		List<Tenant> tenants = crudPrivateService.query(Tenant.class, null);
		log.info("加载Tenant信息......");
		for (Tenant t : tenants){
			tenantIdCaches.put(t.getId(), t);
		}
		log.info("加载"+tenants.size() + "Tenant信息完毕");
		
		
		
		//加载机构信息
//		List<Organization> orgs = crudPrivateService.query(Organization.class, null);
//		log.info("加载机构信息......");
//		for (Organization o : orgs){
//			orgIdCaches.put(o.getId(), o);
//		}
//		log.info("加载"+orgs.size() + "机构信息完毕");
		
//		log.info("加载认证中心信息......");
//		authCenters = (List)crudPrivateService.query(AuthCenter.class, null); 
//		for(IAuthCenter ac : authCenters){
//			initLoginValidateService(ac);
//		}
//		log.info("加载"+authCenters.size() + "认证中心信息完毕");
		
			Connection con = ds.getConnection();
			String sql = null;
			boolean isOracle = false;
			if ( con.getMetaData().getURL().indexOf("thin") > 0){
				isOracle = true;
				sql = (
						"select u.id, " +
						"u.SEX, " +
						"u.NAME, " +
						"u.PHONE, " +
						"u.VERSION, " +
						"u.MSGTYPE, " +
						"u.STATUS, " +
						"u.MSGCODE, " +
						"u.BIRTHDAY, " +
						"u.FAX, " +
						"u.PHOTO, " +
						"u.OFFICEADDR, " +
						"u.CHECKORGIP, " +
						"u.MAILADDR, " +
						"u.OTHERPROPERTITY1, " +
						"u.POSTALCODE, " +
						"u.OTHERPROPERTITY3, " +
						"u.USERTYPE, " +
						"u.INVALIDDATE, " +
						"u.EDUCATION, " +
						"u.POSITION, " +
						"u.INSTITUTE, " +
						"u.LOCALLOGINID, " +
						"u.HOMEADDRESS, " +
						"u.PASSWORD, " +
						"u.VALIDDATE, " +
						"u.EMAIL, " +
						"u.TOTALCASHVALUE, " +
						"u.OTHERPROPERTITY2, " +
						"u.CREATOR, " +
						"u.CREATETIME, " +
						"u.LIFECYCLESTATUS, " +
						"u.LASTMODIFYTIME, " +
						"u.DESCRIPTION, " +
						"u.LASTMODIFIER, " +
						"u.userGroup, " +
						"u.ORGANIZATION_id, " +
						"u.TENANT_ID, " +
//						"u.PERSONALITY_id, " +
						"c.id, " +
						"c.CODE, " +
//						"c.CASH, " +
						"c.TYPE, " +
						"c.STATUS, " +
						"c.VALIDDATE, " +
						"c.VERSION, " +
						"c.INVALIDDATE, " +
						"c.CREATOR, " +
						"c.CREATETIME, " +
						"c.LIFECYCLESTATUS, " +
						"c.LASTMODIFYTIME, " +
						"c.DESCRIPTION, " +
						"c.LASTMODIFIER " +
//						"c.PRINCIPLE_id, "+
//						"ad.ID, " +
//						"ad.CODE, " +
//						"ad.LOGINTYPE, " +
//						"ad.TYPE, " +
//						"ad.VALIDDATE, " +
//						"ad.PASSWORD, " +
//						"ad.INVALIDDATE, " +
//						"ad.STATUS, " +
//						"ad.UPDATETYPE " +
//						"ad.OWNER_id " + 
						" from UM_Principle u, UM_Card c"
						+" where c.principle_id(+) = u.id "); 
				if(tenantId!=null&&tenantId.length()>0){
					sql+="and u.tenant_id=?";
				}
				
				
						//and ad.owner_id(+)=u.id ");
				if (fromYYYYMMDD != null && toYYYYMMDD != null) {
					sql += " and u.LASTMODIFYTIME between to_timestamp('" + fromYYYYMMDD + "','yyyymmdd')";
					sql += "and to_timestamp('" + toYYYYMMDD + "','yyyymmdd')";
				}
			}else {
				sql = (
						"select u.id, " +
						"u.SEX, " +
						"u.NAME, " +
						"u.PHONE, " +
						"u.VERSION, " +
						"u.MSGTYPE, " +
						"u.STATUS, " +
						"u.MSGCODE, " +
						"u.BIRTHDAY, " +
						"u.FAX, " +
						"u.PHOTO, " +
						"u.OFFICEADDR, " +
						"u.CHECKORGIP, " +
						"u.MAILADDR, " +
						"u.OTHERPROPERTITY1, " +
						"u.POSTALCODE, " +
						"u.OTHERPROPERTITY3, " +
						"u.USERTYPE, " +
						"u.INVALIDDATE, " +
						"u.EDUCATION, " +
						"u.POSITION, " +
						"u.INSTITUTE, " +
						"u.LOCALLOGINID, " +
						"u.HOMEADDRESS, " +
						"u.PASSWORD, " +
						"u.VALIDDATE, " +
						"u.EMAIL, " +
						"u.TOTALCASHVALUE, " +
						"u.OTHERPROPERTITY2, " +
						"u.CREATOR, " +
						"u.CREATETIME, " +
						"u.LIFECYCLESTATUS, " +
						"u.LASTMODIFYTIME, " +
						"u.DESCRIPTION, " +
						"u.LASTMODIFIER, " +
						"u.userGroup, " +
						"u.ORGANIZATION_id, " +
						"u.TENANT_ID, " +
//						"u.PERSONALITY_id, " +
						"c.id, " +
						"c.CODE, " +
//						"c.CASH, " +
						"c.TYPE, " +
						"c.STATUS, " +
						"c.VALIDDATE, " +
						"c.VERSION, " +
						"c.INVALIDDATE, " +
						"c.CREATOR, " +
						"c.CREATETIME, " +
						"c.LIFECYCLESTATUS, " +
						"c.LASTMODIFYTIME, " +
						"c.DESCRIPTION, " +
						"c.LASTMODIFIER " +
//						"c.PRINCIPLE_id, "+
//						"ad.ID, " +
//						"ad.CODE, " +
//						"ad.LOGINTYPE, " +
//						"ad.TYPE, " +
//						"ad.VALIDDATE, " +
//						"ad.PASSWORD, " +
//						"ad.INVALIDDATE, " +
//						"ad.STATUS, " +
//						"ad.UPDATETYPE " +
//						"ad.OWNER_id " + 
						" from UM_Principle u left outer join UM_Card c on (c.principle_id = u.id) ");
						//left outer join UM_AdditionalId ad"
						//+"  on ( ad.owner_id = u.id) ");
				if(tenantId!=null&&tenantId.length()>0){
					sql+="and u.tenant_id=?";
				}
				if (fromYYYYMMDD != null && toYYYYMMDD != null) {
//					sql += " and u.LASTMODIFYTIME>=? and u.LASTMODIFYTIME <=?";
					throw new RuntimeException("can not support fromYYYYMMDD, toYYYYMMDD");
				}
			}
			
			
			log.info("加载缓存用户数据 sql : " + sql);
			
			//add by liuy
			//创建um_card(principle_id)的index用以加快查询速度
			if(con!=null){
				buildIndex(con);
			}
			//end add
			
			PreparedStatement stmt = con.prepareStatement(sql);
			if(tenantId!=null&&tenantId.length()>0){
				stmt.setString(1, getTenantIdOfForeignKey(con, tenantId));
			}
			ResultSet rs = stmt.executeQuery();
			
			int loaded = 0;
			while (rs.next()){
				UMPrincipal u = new UMPrincipal();
				int pos = 0;
				u.setId(rs.getString(++pos));
				u.setSex(rs.getString(++pos));
				u.setName(rs.getString(++pos));
				u.setPhone(rs.getString(++pos));
				u.setVersion(rs.getInt(++pos));
				u.setMsgType(rs.getString(++pos));
				u.setStatus(rs.getInt(++pos));
				u.setMsgCode(rs.getString(++pos));
				u.setBirthday(rs.getDate(++pos));
				u.setFax(rs.getString(++pos));
				if (isOracle){
					Blob photo = rs.getBlob(++pos);
					if (photo != null){
						u.setPhoto(photo.getBytes(1, (int)photo.length()));
					}
				}else{
					u.setPhoto(rs.getBytes(++pos));
				}
				
				u.setOfficeAddr(rs.getString(++pos));
				u.setCheckOrgIp(rs.getBoolean(++pos));
				u.setMailAddr(rs.getString(++pos));
				u.setOtherPropertity1(rs.getString(++pos));
				u.setPostalCode(rs.getString(++pos));
				u.setOtherPropertity2(rs.getString(++pos));
				u.setUserType(rs.getInt(++pos));
				u.setInvalidDate(rs.getDate(++pos));
				u.setEducation(rs.getString(++pos));
				u.setPosition(rs.getString(++pos));
				u.setInstitute(rs.getString(++pos));
				u.setLocalLoginId(rs.getString(++pos));
				u.setHomeAddress(rs.getString(++pos));
				u.setPassword(rs.getString(++pos));
				u.setValidDate(rs.getDate(++pos));
				u.setEmail(rs.getString(++pos));
				u.setTotalCashValue(rs.getBigDecimal(++pos));
				u.setOtherPropertity2(rs.getString(++pos));
				u.getMaintainInfo().setCreator(rs.getString(++pos));
				u.getMaintainInfo().setCreateTime(rs.getDate(++pos));
				u.getMaintainInfo().setLifeCycleStatus(rs.getShort(++pos));
				u.getMaintainInfo().setLastModifyTime(rs.getDate(++pos));
				u.getMaintainInfo().setDescription(rs.getString(++pos));
				u.getMaintainInfo().setLastModifier(rs.getString(++pos));
				u.setUserGroup(rs.getInt(++pos));
				u.setOrganization( orgIdCaches.get(rs.getString(++pos)) );
				u.setTenant( tenantIdCaches.get(rs.getString(++pos)) );
				String cid = rs.getString(++pos);
				if (cid != null){
					Card c = new Card();
					u.setCard(c);
					c.setId(cid);
					c.setCode(rs.getString(++pos));
					c.setType(rs.getInt(++pos));
					c.setStatus(rs.getInt(++pos));
					c.setValidDate(rs.getDate(++pos));
					c.setVersion(rs.getInt(++pos));
					c.setInvalidDate(rs.getDate(++pos));
					c.getMaintainInfo().setCreator(rs.getString(++pos));
					c.getMaintainInfo().setCreateTime(rs.getDate(++pos));
					c.getMaintainInfo().setLifeCycleStatus(rs.getShort(++pos));
					c.getMaintainInfo().setLastModifyTime(rs.getDate(++pos));
					c.getMaintainInfo().setDescription(rs.getString(++pos));
					c.getMaintainInfo().setLastModifier(rs.getString(++pos));
					c.setPrinciple(u);
				}else {
					pos += 12;
				}

//				String adid =  rs.getString(++pos);
//				if (adid != null){
//					AdditionalId ad = new AdditionalId();
////					ad.setId(rs.getString(++pos));
//					ad.setId(adid);
//					ad.setCode(rs.getString(++pos));
//					ad.setLoginType(rs.getInt(++pos));
//					ad.setType(rs.getString(++pos));
//					ad.setValidDate(rs.getDate(++pos));
//					ad.setPassword(rs.getString(++pos));
//					ad.setInvalidDate(rs.getDate(++pos));
//					ad.setStatus(rs.getInt(++pos));
//					ad.setUpdateType(rs.getInt(++pos));
//					ad.setOwner(u);
//					u.getAdditionalIds().add(ad);
//				}
				userIdCaches.put(u.getLoginId(), u);
				if (u.getCard() != null && u.getCard().getCode() != null){
					cardIdCaches.put(u.getCard().getCode(), u);
				}
				
				loadAdditionalId(con, u);

				loaded++;
				if (loaded % 10000 == 0){
					log.info("已经加载: " + loaded);
				}
				if (loaded >= limit){
					break;
				}
			}
			
			//add by liuy
			//删除um_card(principle_id)的index用以加快导入速度
			//不删除
			if(con!=null){
//				dropIndex(con);
			}
			//end add
			
			rs.close();
			stmt.close();
			con.close();
			log.info("加载完毕，本次共加载缓存用户数： " + loaded);
		
			//注册变化，以便更新缓存
			crudService.registerEntityCRUDListener(UMPrincipal.class.getName(), this);
			crudService.registerEntityCRUDListener(NewUserBean.class.getName(), this);
			crudService.registerEntityCRUDListener(Organization.class.getName(), this);
			crudService.registerEntityCRUDListener(Card.class.getName(), this);
			crudService.registerEntityCRUDListener(AdditionalId.class.getName(), this);
			crudService.registerEntityCRUDListener(Tenant.class.getName(), this);
	
	}
	
	private void buildIndex(Connection con) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			rs=st.executeQuery("select * from  user_indexes i where table_name ='UM_CARD' and i.index_name='UM_CARD_PRINCIPLE_ID_INDEX'");
			if(!rs.next()){
				st.addBatch("create index UM_CARD_PRINCIPLE_ID_INDEX on UM_CARD(principle_id)");
			}
			
			st.executeBatch();
			log.info("创建UM_Card到UM_PRINCIPLE的索引！");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null) rs.close();
				if(st!=null) st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private String getTenantIdOfForeignKey(Connection con,String tenantId) {
		String tenantIdOfForeignKey=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(SELECT_T_ID_FROM_SAAS_TENANT_T_WHERE_T_TENANTID);
			ps.setString(1, tenantId);
			rs=ps.executeQuery();
			while(rs.next()){
				tenantIdOfForeignKey=rs.getString(1);
			}
		
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return tenantIdOfForeignKey;
		
	}
	private void dropIndex(Connection con) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			rs=st.executeQuery("select * from  user_indexes i where table_name ='UM_CARD' and i.index_name='UM_CARD_PRINCIPLE_ID_INDEX'");
			if(rs.next()){
				st.addBatch("drop index UM_CARD_PRINCIPLE_ID_INDEX");
			}
			st.executeBatch();
			log.info("删除UM_Card到UM_PRINCIPLE的索引！");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null) rs.close();
				if(st!=null) st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private String getAdditionalIdQuerySql(){
		if(aidQuerySql==null){
			StringBuilder aidQuerySqlBuilder = new StringBuilder("select ");
			aidQuerySqlBuilder.append("ad.ID,");
			aidQuerySqlBuilder.append("ad.CODE,");
			aidQuerySqlBuilder.append("ad.LOGINTYPE,");
			aidQuerySqlBuilder.append("ad.TYPE,");
			aidQuerySqlBuilder.append("ad.VALIDDATE,");
			aidQuerySqlBuilder.append("ad.PASSWORD,");
			aidQuerySqlBuilder.append("ad.INVALIDDATE,");
			aidQuerySqlBuilder.append("ad.STATUS,");
			aidQuerySqlBuilder.append("ad.UPDATETYPE");
			aidQuerySqlBuilder.append(" from UM_AdditionalId ad where ad.owner_id = ?"); 
			aidQuerySql = aidQuerySqlBuilder.toString() ; 
		}
		return aidQuerySql ; 
	}
	
	/**
	 * 目前暂时不做AID的状态和有效日期校验
	 * @param con
	 * @param u
	 * @throws SQLException 
	 */
	private void loadAdditionalId(Connection con, UMPrincipal u) throws SQLException {
		PreparedStatement statement = con.prepareStatement(getAdditionalIdQuerySql());
		statement.setString(1, u.getId());
		ResultSet rs = statement.executeQuery();
		int pos  ; 
		while(rs.next()){
			pos = 1 ;
			AdditionalId aid = new AdditionalId();
			aid.setId(rs.getString(pos++));
			aid.setCode(rs.getString(pos++));
			aid.setLoginType(rs.getInt(pos++));
			aid.setType(rs.getString(pos++));
			aid.setValidDate(rs.getDate(pos++));
			aid.setPassword(rs.getString(pos++));
			aid.setInvalidDate(rs.getDate(pos++));
			aid.setStatus(rs.getInt(pos++));
			aid.setUpdateType(rs.getInt(pos++));
			u.getAdditionalIds().add(aid);
			aidCaches.put(aid.getCode(), u);
		}
		rs.close() ; 
		statement.close() ; 
	}

	public void loadUserCache(String fromYYYYMMDD, String toYYYYMMDD,String tenantId) throws SQLException {
		loadUserCache(Integer.MAX_VALUE, fromYYYYMMDD, toYYYYMMDD,tenantId);
	}

	public  synchronized List<IAuthCenter> getAllAuthCenters() {
		return authCenters;
	}

	public synchronized ILoginValidateService getLoginValidateService(String authCenterId) {
		return IdpLoginValidateServiceCache.get(authCenterId);
	}

	public void loadUserCache(int limit) throws SQLException {
		loadUserCache(Integer.MAX_VALUE, null);
		
	}

	public void loadUserCache(String fromYYYYMMDD, String toYYYYMMDD)
			throws SQLException {
		loadUserCache(fromYYYYMMDD,toYYYYMMDD,null);
		
	}
}
