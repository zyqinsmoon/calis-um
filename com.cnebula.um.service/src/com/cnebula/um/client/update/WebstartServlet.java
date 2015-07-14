package com.cnebula.um.client.update;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnebula.aas.rt.SimpleRuntimeContext;
import com.cnebula.common.servlet.impl.NothingToDoServlet;
import com.cnebula.common.str.SimpleStringTemplate;
import com.cnebula.um.service.UMConfig;
import com.cnebula.um.service.UasIntegrateConfig;

public class WebstartServlet extends NothingToDoServlet {

	Map<String, SimpleStringTemplate> bootTemplateCache = new HashMap<String, SimpleStringTemplate>();
	
	UpdateContentConfig updateContentConfig;
	
	UMConfig umconf;
	
	public WebstartServlet(String internalName, UpdateContentConfig updateContentConfig) {
		super(internalName);
		this.updateContentConfig = updateContentConfig;
	}
	
	public WebstartServlet(String internalName, UpdateContentConfig updateContentConfig ,UMConfig umconf) {
		super(internalName);
		this.updateContentConfig = updateContentConfig;
		this.umconf = umconf;
	}
	
	/**
	 * 判断是否集成了统一认证
	 * @return
	 */
	protected boolean isIntegrateUas() {
		if(umconf == null)
			return false;
		if(umconf.uasIntegrateConfig == null)
			return false;
		if(umconf.uasIntegrateConfig.type <= UasIntegrateConfig.NONE ||
		   umconf.uasIntegrateConfig.type > UasIntegrateConfig.FULL)
			return false;
		return true;
	}
	
	protected boolean writeResource(HttpServletRequest req, HttpServletResponse resp, String resourcePath) throws IOException {
		ServletContext servletContext = config.getServletContext();
		URL url = servletContext.getResource(resourcePath);
		if (url == null)
			return false;
		File dynamicFile = null;
		try {
			File file = new File(url.toURI());
			if(isIntegrateUas()){
				if (file.isDirectory()){
					if (!resourcePath.endsWith("/")){
						resourcePath += "/default.st";
					}else{
						resourcePath += "default.st";
					}
					url = servletContext.getResource(resourcePath);
				}				
			}else{//没有集成 统一认证保留之前的逻辑
				if (file.isDirectory()){
					if (!resourcePath.endsWith("/")){
						resourcePath += "/index.st";
					}else{
						resourcePath += "index.st";
					}
					url = servletContext.getResource(resourcePath);
				}
			}
			if (resourcePath.endsWith(".boot") || resourcePath.endsWith(".st")){
				dynamicFile = new File(url.toURI());;
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		URLConnection connection = url.openConnection();
//		long lastModified = connection.getLastModified();
//		int contentLength = connection.getContentLength();
		
		CachedFile zf = fetch(resourcePath, servletContext.getMimeType(resourcePath));
		
		if (zf == null){
			return false;
		}
		
		zf.refreshWhenModify();
		
		if (zf.content == null ){
			return false;
		}
		long lastModified = zf.lastModified;
		int contentLength = zf.content.length;


		if (dynamicFile != null){
			lastModified = dynamicFile.lastModified();
			contentLength = (int) dynamicFile.length();
		}
		
		String etag = null;
		if (lastModified != -1 && contentLength != -1)
			etag = "W/\"" + contentLength + "-" + lastModified + "\""; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

		// Check for cache revalidation.
		// We should prefer ETag validation as the guarantees are stronger and all HTTP 1.1 clients should be using it
		String ifNoneMatch = req.getHeader(HEADER_IFNONEMATCH);
		if (ifNoneMatch != null && etag != null && ifNoneMatch.indexOf(etag) != -1) {
			resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return true;
		} else {
			long ifModifiedSince = req.getDateHeader(HEADER_IFMODSINCE);
			// for purposes of comparison we add 999 to ifModifiedSince since the fidelity
			// of the IMS header generally doesn't include milli-seconds
			if (ifModifiedSince > -1 && lastModified > 0 && lastModified <= (ifModifiedSince + 999)) {
				resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return true;
			}
		}

		// return the full contents regularly
		if (contentLength != -1)
			resp.setContentLength(contentLength);
		String contentType = servletContext.getMimeType(resourcePath);
		if (resourcePath.endsWith(".st")) {
			contentType = "text/html";
		}
		if (resourcePath.endsWith(".boot")){
			contentType = "application/x-java-jnlp-file";
		}
		
		if (contentType != null)
			resp.setContentType(contentType);

		if (lastModified > 0)
			resp.setDateHeader(HEADER_LASTMOD, lastModified);

		if (etag != null)
			resp.setHeader(ETAG, etag);

		if (dynamicFile != null){
			SimpleStringTemplate t = null;
			synchronized (bootTemplateCache) {
				t = bootTemplateCache.get(resourcePath);
				if (t == null){
					RandomAccessFile rf = new RandomAccessFile(dynamicFile, "r");
					MappedByteBuffer mb = rf.getChannel().map(MapMode.READ_ONLY, 0, dynamicFile.length());
					try {
						CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
						decoder.onMalformedInput (CodingErrorAction.REPLACE); 
				        decoder.onUnmappableCharacter (CodingErrorAction.REPLACE); 
				        StringBuilder stbuf = new StringBuilder();
				        CharBuffer cb = CharBuffer.allocate ((int)rf.length()); 
				        decoder.decode(mb, cb, true);
				        cb.flip();
				        stbuf.append(cb.array());
				        if(resourcePath.indexOf("default.st") >= 0 && ( umconf.uasIntegrateConfig.type == 1 || umconf.uasIntegrateConfig.type == 2)){
				        	//如果统一认证的集成方式是1（表明是松散简单认证集成(auth)模式）或
				        	//2（表明是共享应用层规则（share）模式），那么统一认证的地址从配置文件中取。
				        	String needReplace = "baseURL + \"amconsole/AuthServices?verb=login&goto=\"";
				        	String uasBaseURL = "\"http://" + umconf.uasIntegrateConfig.host + ":" + umconf.uasIntegrateConfig.port + "/amconsole/AuthServices?verb=login&goto=\"";
				        	stbuf.replace(stbuf.indexOf(needReplace),stbuf.indexOf(needReplace) + needReplace.length(),uasBaseURL);
				        }
				        
						bootTemplateCache.put(resourcePath, t = new SimpleStringTemplate(stbuf.toString()));
					} catch (Throwable e) {
						resp.sendError(500, e.getMessage());
						return false;
					}finally{
						rf.close();
					}
				}
			}
			SimpleRuntimeContext ctx = new SimpleRuntimeContext();
			Map<String, Object> env = new HashMap<String, Object>();
			String artifact = req.getParameter("artifact");
			if (artifact == null){
				artifact = "";
			}
			
			String hostAndPort = req.getHeader("Host");
			if (hostAndPort == null){
				hostAndPort = req.getHeader("host");
			}
			
			if (hostAndPort != null){
				String[] hps = hostAndPort.split("\\:");
				env.put("host", hps[0]);
				env.put("port", hps.length > 1 ? hps[1] : 80);
			}else{
				env.put("host", req.getLocalName());
				env.put("port", req.getLocalPort());
			}

			env.put("artifact", artifact);
			env.put("request", req);
			env.put("site", updateContentConfig);
//			env.put("host", req.getLocalName());
//			env.put("port", req.getLocalPort());
//			env.put("curpath", "http://" + req.getLocalName() +":" + req.getLocalPort() +   "/update" + resourcePath.substring(0, resourcePath.lastIndexOf('/')));
			env.put("curpath", "http://" + env.get("host") +":" + env.get("port") +   "/update" + resourcePath.substring(0, resourcePath.lastIndexOf('/')));
			env.put("uashost",umconf.uasIntegrateConfig.host);
			env.put("uasport",umconf.uasIntegrateConfig.port);
			env.put("uasInteType",umconf.uasIntegrateConfig.type);
			if(UasIntegrateConfig.VERSIONTYPE_SAASCENTER.equals(umconf.uasIntegrateConfig.versionType))
				env.put("normalAdminLoginButton", "block");
			else
				env.put("normalAdminLoginButton", "none");
			ctx.setDefaultObject(env);
			try {
				byte[] bytes =  t.eval(ctx).getBytes("UTF-8");
				if (contentLength == -1 || contentLength != bytes.length){
					resp.setContentLength(bytes.length);
					resp.getOutputStream().write(bytes);
					return true;
				}
			} catch (Throwable e) {
				resp.sendError(500, e.getMessage());
				return false;
			}
			
		}
		/*InputStream is = null;
		try {
			is = connection.getInputStream();
			OutputStream os = resp.getOutputStream();
			byte[] buffer = new byte[8192];
			int bytesRead = is.read(buffer);
			int writtenContentLength = 0;
			while (bytesRead != -1) {
				os.write(buffer, 0, bytesRead);
				writtenContentLength += bytesRead;
				bytesRead = is.read(buffer);
			}
			if (contentLength == -1 || contentLength != writtenContentLength)
				resp.setContentLength(writtenContentLength);
		} finally {
			if (is != null)
				is.close();
		}*/
		OutputStream os = resp.getOutputStream();
		os.write(zf.content);
		return true;
	
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ConcurrentHashMap<String, Future<CachedFile>> cachedFiles = new ConcurrentHashMap<String, Future<CachedFile>>();
	protected CachedFile fetch(final String resourcePath, final String contentType) {
		do {
			Future<CachedFile> f = cachedFiles.get(resourcePath);
			if (f == null){
				FutureTask<CachedFile> ft = new FutureTask<CachedFile>(new Callable<CachedFile>() {
					public CachedFile call() throws Exception {
						ServletContext servletContext = config.getServletContext();
						URL url = servletContext.getResource(resourcePath);
						if (url == null){
							return null;
						}
						CachedFile cgf = new CachedFile(url, contentType);
						return cgf;
					}
				});
				f = cachedFiles.putIfAbsent(resourcePath, ft);
				if (f == null){
					f = ft;
					ft.run();
				}
			}
			try {
				return f.get();
			} catch (InterruptedException e) {
				throw new RuntimeException("fetch "+resourcePath+" interrupted" , e);
			} catch (ExecutionException e) {
//				throw handleThrowable(e.getCause(), "can't fetch "+resourcePath);
				throw new IllegalStateException("can't fetch "+resourcePath,e.getCause());
			}
		}while(true);
	}

}
