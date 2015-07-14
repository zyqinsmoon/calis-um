package com.cnebula.um.client.ui2;


import java.util.Dictionary;
import java.util.List;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.http.HttpService;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.common.ejb.relation.IEntityFullInfoPool;
import com.cnebula.common.log.ILog;
import com.cnebula.common.security.auth.ILoginValidateService;
import com.cnebula.common.servlet.impl.BundleEntryHttpContext;
import com.cnebula.common.servlet.impl.NothingToDoServlet;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;

@EasyService
public class WebAppStarter {
	
	@ESRef
	HttpService httpService;
	
	@ESRef
	ILog log;
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityFullInfoPool entiyInfoPool;
	
	@ESRef(target="(id=UMValidLoginService)")
	ILoginValidateService loginValidateService;
	
	@ESRef
	IEntityCRUDService curd;
	
	@ESRef(target="(component.factory=com.cnebula.common.conf.IServiceGroupFacet_ES_Factory)")
	protected ComponentFactory serviceGroupFacetFactory;
	
	protected void activate(ComponentContext ctx) throws Exception {
		log.info("注册userManage服务到 /userManage/*");
		httpService.registerServlet("/userManage", new NothingToDoServlet(""), null, new BundleEntryHttpContext(ctx.getBundleContext().getBundle(), "/WebApp"));
		Dictionary<String, String> dc = new java.util.Hashtable<String,String>();
		dc.put("name", "um");
		serviceGroupFacetFactory.newInstance( dc );
	}
	
}
