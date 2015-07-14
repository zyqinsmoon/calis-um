package com.cnebula.um.service;

import java.util.List;

import com.cnebula.aas.util.PolicyRuleParseException;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.um.ejb.entity.usr.Organization;

public interface IOrganizationQueryService {
	/**
	 * 根据ip获得包含该ip的机构
	 * @param ip IP地址串
	 * @return 包含该ip的机构。
	 */
	public Organization getOrganizationByIp(String ip);
	
	/**
	 * 根据上下文请求，获取其所在的机构信息
	 * @return
	 */
	public Organization getOrganizationByRequest();
	
	/**
	 * 获得所有机构
	 * @param loadRelations 需要同时返回的附带复杂关系（一对多，多对多）属性列表,以逗号分隔，可包括ipRanges(IP范围列表)、 contactors(联系人列表)和 children(下级机构)
	 * @return 返回所有机构
	 * @throws EntityCRUDException 如果loadRelations中有非法的属性列表，则抛出异常
	 */
	public List<Organization> getAllOrganizations(String loadRelations) throws EntityCRUDException;
	

	/**<pre>
	 * 根据查询条件返回按照排序要求排好序的机构列表
	 * 示例：
	 * 1）查出所有上级机构名称是北京大学的机构,并且按照名称正序排序, 同时返回机构附带下级机构信息和联系人列表信息
	 * List<Organization> rt = orgqueryService.searchOrganization("parent.name=\"北京大学\"", "+name", "children,contactors");
	 * </pre>
	 * @param queryString 见统一服务接口规范2.1 基本表达式 （1）属性过滤表达式, 如果是null，则表示返回所有机构
	 * @param sortString  见统一服务接口规范2.1 基本表达式 （2）排序表达式，如果是null则表示不做任何特殊排序
	 * @param loadRelations 需要同时返回的附带复杂关系（一对多，多对多）属性列表,以逗号分隔，可包括ipRanges(IP范围列表)、 contactors(联系人列表)和 children(下级机构)
	 * @return 返回符合查询条件，并按照排序要求排好序的机构列表
	 * @throws EntityCRUDException 如果loadRelations中有非法的属性列表，则抛出异常EntityCRUDException
	 * @throws PolicyRuleParseException 如果queryString非法，或者sortString非法，则抛出异常PolicyRuleParseException
	 */
	public List<Organization> searchOrganization(String queryString, String sortString, String loadRelations) throws EntityCRUDException, PolicyRuleParseException;
	
	public List<Organization> getAllTopOrganization();
}
