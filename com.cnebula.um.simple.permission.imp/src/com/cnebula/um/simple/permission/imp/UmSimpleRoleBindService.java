package com.cnebula.um.simple.permission.imp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.log.ILog;
import com.cnebula.um.simple.permission.ISimpleRoleBindService;
import com.cnebula.um.simple.permission.UserRoleGroup;
//@EasyService(properties = { @Property(name = "id", value = "ms") }, interfaces = { ISimpleRoleBindService.class })
@EasyService()
public class UmSimpleRoleBindService implements ISimpleRoleBindService {

	private List<UserRoleGroup> userRoleGroupList = new ArrayList<UserRoleGroup>();
	
	private Map<String,Map<String,UserRoleGroup>> allRoleMap=new LinkedHashMap<String,Map<String,UserRoleGroup>>();

	@ESRef
	private ILog log;

	protected void activate(ComponentContext ctx) {

//		log.info("um IUserRoleBindService启动。");

	}

	public List<UserRoleGroup> getUserRoleGroupList(String tenantID) {
		userRoleGroupList.clear();
		for(UserRoleGroup UserRoleGroup:allRoleMap.get(tenantID).values()){
			userRoleGroupList.add(UserRoleGroup);
		}
		return userRoleGroupList;
	}

	public void registerUserRoleGroup(UserRoleGroup userRoleGroup,String tenantID) {
		Map<String,UserRoleGroup> userRoleGroupMap=null;
		if(allRoleMap.get(tenantID)!=null){
			userRoleGroupMap=allRoleMap.get(tenantID);
		}else{
			userRoleGroupMap=new LinkedHashMap<String,UserRoleGroup>();
		}
		
		
		userRoleGroupMap.put(userRoleGroup.getId(), userRoleGroup);
		
		allRoleMap.put(tenantID,userRoleGroupMap);
	}

	public List<UserRoleGroup> getUserRoleGroupList(String groupName,String tenantID) {
		List<UserRoleGroup> result=new ArrayList<UserRoleGroup>();
		if(allRoleMap.get(tenantID)==null ||allRoleMap.get(tenantID).size()==0){
			return result;
			
		}
		for(UserRoleGroup u:allRoleMap.get(tenantID).values()){
			if(u.getId().indexOf(groupName)>=0){
				
				result.add(u);
			}
		}
		return result;
	}

}
