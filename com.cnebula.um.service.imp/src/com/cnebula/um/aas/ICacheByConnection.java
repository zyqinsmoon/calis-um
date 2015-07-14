package com.cnebula.um.aas;

import java.sql.SQLException;
import java.util.Date;

import com.cnebula.common.ejb.manage.IEntityCRUDListener;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;

public interface ICacheByConnection extends IEntityCRUDListener {

	
	/**
	 * 加载用户到缓存
	 * @param limit 用户数目限制，通常用于debug，平时可以使用Integer.MAX_VALUE
	 * @throws SQLException 加载发生数据库错误的时候抛出此异常
	 */
	public void loadUserCache(int limit,String tenantId) throws SQLException ;
	
	
	public void loadUserCache(int limit) throws SQLException ;
	
	/**
	 * 加载最近修改时间戳在给定时间范围内的用户
	 * @param fromYYYYMMDD 起
	 * @param toYYYYMMDD   止
	 * @throws SQLException 加载发生数据库错误的时候抛出此异常
	 */
	public void loadUserCache(String fromYYYYMMDD, String toYYYYMMDD,String tenantId) throws SQLException ;
	
	
	public void loadUserCache(String fromYYYYMMDD, String toYYYYMMDD) throws SQLException ;
	
	/**
	 * 根据用户ID获得缓存中的用户
	 * @param id 用户ID
	 * @return 返回给定ID的用户，不存在则返回null
	 */
	public  UMPrincipal getUser(String id);
	
	/**
	 * 根据卡ID返回缓存中的用户
	 * @param cid
	 * @return 返回给定卡ID的用户，不存在则返回null
	 */
	public  UMPrincipal getUserByCardId(String cid);
	
	/**
	 * 获得EnitityCURDService
	 */
	public IEntityCRUDPrivateService getCrudPrivateService();
	
	
}
