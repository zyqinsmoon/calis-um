package com.cnebula.um.cache;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.log.ILog;
import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.Card;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
@EasyService
public class UpdateCacheByConnectionImpl implements IUpdateCacheByConnection {
	
	@ESRef
	protected ILog log;
	
	@ESRef
	protected IUMLoginCache cache;
	
	@ESRef(target="(name=#{umds})")
	protected DataSource ds;
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService crudPrivateService;
	
	private static String LOG_START="更新用户数据缓存启动成功(UpdateByConnection)！";
	private static final String SELECT_T_ID_FROM_SAAS_TENANT_T_WHERE_T_TENANTID = "select t.id from saas_tenant t where t.tenantid=?";
	
	protected Map<String, Organization> orgIdCaches = new HashMap<String, Organization>();
	protected Map<String, Tenant> tenantIdCaches = new HashMap<String, Tenant>();

	protected void activate(ComponentContext ctx) {
			log.info(LOG_START);
			
	}
	
	private String getAdditionalIdQuerySql(){
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
			return aidQuerySqlBuilder.toString() ; 
	}
	
	private void loadAdditionalId(Connection con, UMPrincipal u) throws SQLException {
		Connection conq = ds.getConnection();
		PreparedStatement statement = conq.prepareStatement(getAdditionalIdQuerySql());
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
			cache.putUserByAddCode(aid.getCode(), u);
		}
		rs.close() ; 
		statement.close() ; 
		conq.close();
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
	private void buildIndexTenant_id(Connection con) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			rs=st.executeQuery("select * from  user_indexes i where table_name ='UM_PRINCIPLE' and i.index_name='UM_PRINCIPLE_TENANT_ID'");
			if(!rs.next()){
				st.addBatch("create index UM_PRINCIPLE_TENANT_ID on UM_PRINCIPLE(TENANT_ID)");
			}
			st.executeBatch();
			log.info("创建UM_PRINCIPLE到TENANT的索引！");
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
	private void buildAdditionIDToUMPrincipal(Connection con) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			rs=st.executeQuery("select * from  user_indexes i where table_name ='UM_ADDITIONALID' and i.index_name='UM_ADDITIONALID_UMPRINCIPAL_ID'");
			if(!rs.next()){
				st.addBatch("create index UM_ADDITIONALID_UMPRINCIPAL_ID on UM_ADDITIONALID(OWNER_ID)");
			}
			
			st.executeBatch();
			log.info("创建UM_ADDITIONALID到UMPRINCIPAL的索引！");
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
	public  synchronized void loadUserCache(int limit, String fromYYYYMMDD, String toYYYYMMDD,String tenantId) throws SQLException {
		//加载机构信息
		List<Tenant> tenants = crudPrivateService.query(Tenant.class, null);
		log.info("加载Tenant信息......");
		for (Tenant t : tenants){
			tenantIdCaches.put(t.getId(), t);
		}
		log.info("加载"+tenants.size() + "Tenant信息完毕");
	}

	public void loadUserCache(int limit, String tenantId) throws SQLException {
		loadUserCache(limit, null, null,tenantId);
	}

	public void loadUserCache(int limit) throws SQLException {
		loadUserCache(Integer.MAX_VALUE, null);
	}

	public void loadUserCache(String fromYYYYMMDD, String toYYYYMMDD,
			String tenantId) throws SQLException {
		loadUserCache(Integer.MAX_VALUE, fromYYYYMMDD, toYYYYMMDD,tenantId);
	}

	public void loadUserCache(String fromYYYYMMDD, String toYYYYMMDD)
			throws SQLException {
		loadUserCache(fromYYYYMMDD,toYYYYMMDD,null);
	}
	
	

}
