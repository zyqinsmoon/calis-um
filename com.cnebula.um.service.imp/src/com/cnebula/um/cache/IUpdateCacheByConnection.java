package com.cnebula.um.cache;

import java.sql.SQLException;

/***
 * UpdateCacheByConnection专门负责从数据库捞数据来更新缓存
 * @author Administrator
 *
 */
public interface IUpdateCacheByConnection {
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
}
