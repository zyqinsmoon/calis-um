package com.cnebula.um.service.tests;


import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.reflect.EasyConverter;
import com.cnebula.common.remote.ws.EasyServiceClient;
import com.cnebula.um.ejb.entity.env.IPRange;
import com.cnebula.um.ejb.entity.env.IPSegment;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.service.IOrganizationQueryService;

public class TestOranizationQueryService {

	IOrganizationQueryService organizationQueryService;
	
	@Before
	public void setUp() throws Exception {
		organizationQueryService = EasyServiceClient.lookup("localhost", 80, IOrganizationQueryService.class);
	}
	
	@Test
	public void testQueryAllOrgs() {
		try {
			
			List<Organization> orgs = organizationQueryService.getAllOrganizations("ipRanges");
			for (Organization o : orgs){
				System.out.println(o.getName() + ", ill url : " + o.getOtherPropertity1());
				for (IPRange ipr : o.getIpRanges()){
					System.out.print(ipr.getName());
					for (IPSegment s : ipr.getIpSegments()){
						System.out.print("\t start :" + EasyConverter.getDefaultInstance().restoreString(s.getStart(), "xxx.xxx.xxx.xxx"));
						System.out.print("\t end :" + EasyConverter.getDefaultInstance().restoreString(s.getEnd(), "xxx.xxx.xxx.xxx"));
					}
					System.out.println();
				}
				System.out.println("============================================");
			}
		} catch (Throwable e) {
			//省略了出错处理
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
	}

}
