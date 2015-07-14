package com.cnebula.um.client.update;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.conf.IEasyServiceConfAdmin;
import com.cnebula.common.log.ILog;
import com.cnebula.um.service.UMConfig;

@EasyService(noservice=true)
public class UpdateContentSetuper {

	@ESRef
	protected IEasyServiceConfAdmin confAdmin;
	
	@ESRef
	protected HttpService httpService;
	
	@ESRef
	protected ILog log;
	
	protected void activate(ComponentContext ctx) {
		UpdateContentConfig config = confAdmin.get("updateContent", UpdateContentConfig.class);
		UMConfig umConfig = confAdmin.get("calisUM", UMConfig.class);
		
		if (config != null){
			String rootPath = null;
			if (config.dir.charAt(0) == '/' || config.dir.charAt(1) == ':') {
				rootPath = config.dir;
			}else {
				rootPath = new File(confAdmin.getConfRoot(), config.dir).getAbsolutePath();
			}
			if (config.localJRE != null){
				//if (config.localJRE.charAt(0) != '/') {
				if (!(config.localJRE.startsWith("http://")
					||config.localJRE.startsWith("ftp://")
					||config.localJRE.startsWith("file://"))) {	
					config.localJRE  = "/update/" + config.localJRE;
				}
			}
			final File root = new File(rootPath);
//			final String fileListXml = config.fileListXml;
			try {
				httpService.registerServlet("/update", new WebstartServlet("", config, umConfig), 
						null, new HttpContext(){
							public String getMimeType(String name) {
								if (name.equals("boot")){
									return "application/x-java-jnlp-file";
								}else if (name.equals("st")){
									return "text/html";
								}
								return null;
							}

							public URL getResource(String name) {
								try {
									return new File(root, name).toURI().toURL();
								} catch (MalformedURLException e) {
									return null;
								}
							}

							public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException {
								return true;
							}
							
				});
				log.info("更新服务路径绑定到路径" + root.getAbsolutePath());
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (NamespaceException e) {
				e.printStackTrace();
			}
			
		}else {
			log.warn("无法找到配置点updateContent，update服务停用");
		}
	}
}
