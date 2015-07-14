package com.cnebula.um.cache;

import com.cnebula.um.ejb.entity.usr.UMPrincipal;
/***
 * IUmLoginCache用于缓存与用户登陆认证有关的数据
 * 
 * @author Administrator
 *
 */
public interface IUMLoginCache {
	public UMPrincipal getUserByLoginId(String loginID);
	public UMPrincipal getUserByCardCode(String cardCode);
	public UMPrincipal getUserByAddCode(String addCode);
	public void putUserByLoginId(String loginID,UMPrincipal u);
	public void putUserByCardCode(String cardCode,UMPrincipal u);
	public void putUserByAddCode(String addCode,UMPrincipal u);
	
}
