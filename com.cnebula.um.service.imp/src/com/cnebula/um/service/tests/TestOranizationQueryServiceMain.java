package com.cnebula.um.service.tests;


import java.util.List;


import com.cnebula.common.reflect.EasyConverter;
import com.cnebula.common.remote.ws.EasyServiceClient;
import com.cnebula.um.ejb.entity.env.IPRange;
import com.cnebula.um.ejb.entity.env.IPSegment;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.service.IOrganizationQueryService;

public class TestOranizationQueryServiceMain {


	public static void printOrg(Organization o) throws Exception {
		
		System.out.println(o.getName() + ", ill url : " + o.getOtherPropertity1());
		for (IPRange ipr : o.getIpRanges()){
			System.out.print(ipr.getName());
			for (IPSegment s : ipr.getIpSegments()){
				//把IP起止（类型为long）转换为xxx.xxx.xxx.xxx的字符串格式
				System.out.print("\t start :" + EasyConverter.getDefaultInstance().restoreString(s.getStart(), "xxx.xxx.xxx.xxx"));
				System.out.print("\t end :" + EasyConverter.getDefaultInstance().restoreString(s.getEnd(), "xxx.xxx.xxx.xxx"));
			}
			System.out.println();
		}
		System.out.println("============================================");
	}
	
	public static void main(String[] args) {
		IOrganizationQueryService organizationQueryService = null;
		//初始化一次
		try {
			organizationQueryService = EasyServiceClient.lookup("192.168.1.190",8880 , IOrganizationQueryService.class);
		} catch (Throwable e) {
			//省略了出错处理
			e.printStackTrace();
		}
		
		
		System.out.println("获得所有机构信息测试示例");
		System.out.println("*********************************");
		try {
			//获得所有机构信息，同时加载机构关联的IP范围信息
			List<Organization> orgs = organizationQueryService.getAllOrganizations("ipRanges");
			for (Organization o : orgs){
				printOrg(o);
			}
		} catch (Throwable e) {
			//省略了出错处理
			e.printStackTrace();
		}
		System.out.println();
		System.out.println();
		System.out.println("根据IP查询机构测试示例");
		System.out.println("*********************************");
		try{
			//查询162.105.139.1所在机构信息
			Organization o = organizationQueryService.getOrganizationByIp("162.105.139.1");
			System.out.println("ip为162.105.139.1的机构为：");
			printOrg(o);
		}catch (Exception e) {
			
		}
	}
}
