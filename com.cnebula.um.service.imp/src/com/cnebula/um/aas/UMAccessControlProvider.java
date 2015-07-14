package com.cnebula.um.aas;

import java.net.MalformedURLException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.security.auth.login.LoginException;

import org.osgi.service.component.ComponentContext;

import com.cnebula.aas.rt.AASRuntimeContext;
import com.cnebula.aas.rt.IPermissionDetail;
import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.conf.IEasyServiceConfAdmin;
import com.cnebula.common.ejb.EntityPermission;
import com.cnebula.common.ejb.index.IPSegmentMemoryIndex;
import com.cnebula.common.ejb.manage.EntityPermissionExcuteStatus;
import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.ICRUDSupportAccessControlProvider;
import com.cnebula.common.ejb.manage.IEntityCRUDListener;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.common.ejb.manage.perm.Operation;
import com.cnebula.common.es.ISession;
import com.cnebula.common.es.RequestContext;
import com.cnebula.common.es.SessionContext;
import com.cnebula.common.i18n.I18nService;
import com.cnebula.common.log.ILog;
import com.cnebula.common.reflect.ClassInfoPool;
import com.cnebula.common.reflect.ObjectRef;
import com.cnebula.common.remote.IBinding;
import com.cnebula.common.remote.IRemoteProtocolProvider;
import com.cnebula.common.remote.IRemoteServiceRegister;
import com.cnebula.common.remote.ITransport;
import com.cnebula.common.remote.ServiceURL;
import com.cnebula.common.security.IAccessControlProviderRegister;
import com.cnebula.common.security.IDynamicRole;
import com.cnebula.common.security.PermissionCheckUtil;
import com.cnebula.common.security.auth.IAuthorizationService;
import com.cnebula.common.security.auth.ILoginValidateService;
import com.cnebula.common.security.simple.SimpleDynamicAccessControlProvider;
import com.cnebula.common.server.http.HttpServerConfig;
import com.cnebula.um.ejb.entity.env.IPRange;
import com.cnebula.um.ejb.entity.env.IPSegment;
import com.cnebula.um.ejb.entity.env.IPV6Segment;
import com.cnebula.um.ejb.entity.perm.LegacyRole;
import com.cnebula.um.ejb.entity.perm.PermissionRule;
import com.cnebula.um.ejb.entity.perm.Role;
import com.cnebula.um.ejb.entity.perm.UserRule;
import com.cnebula.um.ejb.entity.usr.AdditionalId;
import com.cnebula.um.ejb.entity.usr.Card;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.service.UMConfig;
import com.cnebula.um.service.UasIntegrateConfig;
import com.cnebula.um.setup.ISetupService;

//@EasyService(immediate=true, servicefactory=false, properties={@Property(name="id", value="UMValidLoginService")}, interfaces={IIPAndRoleIndexer.class, ILoginValidateService.class, IAuthorizationService.class, ICRUDSupportAccessControlProvider.class})
public class UMAccessControlProvider extends SimpleDynamicAccessControlProvider implements ILoginValidateService, IEntityCRUDListener, IAuthorizationService, ICRUDSupportAccessControlProvider, IIPAndRoleIndexer {

	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDService crudService;
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService crudPrivateService;
	
	@ESRef(target="(namespace=com.cnebula.um.aas.UMAccessControlProvider)")
	protected I18nService i18nService;
	
	
	@ESRef
	protected ICacheByConnection icacheByConnection;
	
	protected CacheByConnection cacheByConnection;
	
	@ESRef
	protected ILog log;
	
	@ESRef
	protected IAccessControlProviderRegister accessControlProviderRegister;
	
	@ESRef(target="(protocol=http)")
	protected ITransport httpTransport;
	
	@ESRef(target="(protocol=hessian)")
	protected IBinding hessinBinding;
	/**
	 * 保证setup成功后才启动本服务
	 */
	@ESRef
	protected ISetupService setupService;
	
	@ESRef
	protected IEasyServiceConfAdmin confAdmin;
	
	protected UMConfig config;
	
	protected HashSet<String> permissionRelatedClasses = new HashSet<String>();
	
	protected HashSet<String> ipRelatedClasses = new HashSet<String>();
	
	protected Map<String, Role> roleIndex = new HashMap<String, Role>();
	
	protected static UMPrincipal anonymousUser;
	
	static {
		anonymousUser = new UMPrincipal();
		anonymousUser.setId("$anonymous");
		anonymousUser.setLoginId("$anonymous");
		anonymousUser.setName("anonymous");
		anonymousUser.setLocalLoginId("$anonymous");
	}
	
	protected boolean useUserCache = false;
	
	protected volatile IPSegmentMemoryIndex<Organization> ipIndex = new IPSegmentMemoryIndex<Organization>();
	
	@ESRef
	protected IRemoteServiceRegister register;
	
	@ESRef(target="(protocol=ws)")
	protected IRemoteProtocolProvider remoteProtocolProvider;
	
	/**
	 * 认证集成相关属性
	 */
	protected String centerToken;
	protected ILoginValidateService centerLoginValidateService;
	protected IAuthorizationService centerAuthorizationService;
	protected Map<String, Object> subAuthorizationServiceMap = Collections.synchronizedMap(new HashMap<String, Object>());
	
	
	public UMAccessControlProvider() {
		
		permissionRelatedClasses.add(PermissionRule.class.getName());
		permissionRelatedClasses.add(UserRule.class.getName() ); 
		permissionRelatedClasses.add(Role.class.getName() );     
		permissionRelatedClasses.add(LegacyRole.class.getName()) ;
		permissionRelatedClasses.add(Operation.class.getName() );
		
		ipRelatedClasses.add(Organization.class.getName());
		ipRelatedClasses.add(IPRange.class.getName());
		ipRelatedClasses.add(IPSegment.class.getName());
		
	}
	
	public static UMPrincipal getAnonymousUMPrincipal() {
		return anonymousUser;
	}
	
	public Object getAnonymousUser() {
		return anonymousUser;
	}

	public final static String EXT_AID = "aid";
	public final static String EXT_ROLES = "roles";
	
	/**
	 * 填补角色到扩展属性，仅仅供本地用户使用
	 * @param u
	 */
	@SuppressWarnings("unchecked")
	public void fillAuthInfo(UMPrincipal u) {
		if (u == null){
			return;
		}
		Map<String, String> ext = u.getExtAttributes();
		if (config.uasIntegrateConfig.aid != null){
			ext.put(EXT_AID, config.uasIntegrateConfig.aid);
		}
		List<Role> roles = (List<Role>) getRolesByUser(u);
		StringBuilder rolesString = new StringBuilder();
		
		for (int i = 0; i < roles.size(); i++) {
			Role r = roles.get(i);
			if (i > 0) {
				rolesString.append(",");
			}
			if (r instanceof LegacyRole) {
				LegacyRole lr = (LegacyRole) r;
				rolesString.append(lr.getApplication().getAuthId()).append(".").append(lr.getCode());
			}else{
				rolesString.append(roles.get(i).getName());
			}
			
		}
		ext.put(EXT_ROLES, rolesString.toString());
	}
	
	public static String generateArtifact(String aid, String sessionId, Random artifactSeed) {
		byte seed = (byte)artifactSeed.nextInt(127);
		byte ver = 25;
		StringBuilder sb = new StringBuilder(aid).append('-').append(sessionId);
		byte[] bs = sb.toString().getBytes();
		sb.delete(0, sb.length());
		sb.append(Integer.toString(ver, 36)).append("-").append(Integer.toString(seed, 36)).append("-");
		//encode
//		if (sb.toString().indexOf("--") > 0){
//			System.out.println("err : " + sb.toString() + " seed : " + seed);
//		}
		int bslen = bs.length;
		for (byte i = 0; i < bslen; i++){
			bs[i] = (byte)(bs[i] ^ seed);
			String ss = Integer.toString(bs[i], 36);
			if (ss.length() < 2) {
				sb.append(0);
			}
			sb.append(ss);
		}
		return sb.toString();
//		try {
//			StringBuilder last = StringEnDeCoder.Encode(StringEnDeCoder.CT_BASE64, bs);
//			int len = last.length();
//			if (last.charAt(len-2) == '=') {
//				last.delete(len-2, len);
//			}else if (last.charAt(len-1) == '='){
//				last.delete(len-1, len);
//			}
//			sb.append(last.toString());
//			return  sb.toString();
//		} catch (UnsupportedEncodingException e) { // nerver happen
//			return null;
//		}
	}
	
	public static String[] parseArtifact(String artifact) {
		try {
			if (artifact == null) {
				return null;
			}
			int pos = artifact.indexOf('-');
			if (pos < 0) {
				return null;
			}
			byte ver = (byte) Integer.parseInt(artifact.substring(0, pos), 36);
			if (pos < 0) {
				return null;
			}

			int pos2 = artifact.indexOf('-', pos + 1);

			byte seed = (byte) Integer.parseInt(artifact.substring(pos + 1, pos2), 36);

			artifact = artifact.substring(pos2 + 1);

			int len = artifact.length();
//			if (len % 4 == 2) {
//				artifact += "==";
//			} else if (len % 4 == 3) {
//				artifact += "=";
//			}
//			byte[] bs = StringEnDeCoder.Decode(StringEnDeCoder.CT_BASE64, artifact);
			
			byte[] bs = new byte[len/2];
			for (int i = 0; i < len; i+=2) {
				bs[i/2] = Byte.parseByte(artifact.substring(i, i+2), 36);
			}
			
			int bslen = bs.length;
			int sp = 0;
			for (byte i = 0; i < bslen; i++) {
				bs[i] = (byte) (bs[i] ^ seed);
				if (bs[i] == '-') {
					sp = i;
				}
			}
			if (sp == 0) {
				return null;
			}
			String da = new String(bs);
			return new String[] {  da.substring(0, sp), da.substring(sp + 1), ver + ""};
		} catch (Throwable e) {
			return null;
		}
	}

//merge松散认证 begin
	public void fixExRole(UMPrincipal u){
		String rolesString = u.getExtAttributes().get(EXT_ROLES);
		if (rolesString == null){
			return;
		}
		String appidPrefx = config.uasIntegrateConfig.appid+".";
		String[] prefixRoles = rolesString.split("\\,");
		StringBuilder fixedRoles = new StringBuilder();
		for (String pr : prefixRoles){
			if (pr.startsWith(appidPrefx)){
				if (fixedRoles.length() != 0){
					fixedRoles.append(",");
				}
				fixedRoles.append(pr.substring(appidPrefx.length()));
			}
		}
		//追加本地最低用户权限
		if (fixedRoles.length() != 0){
			fixedRoles.append(",");
		}
		fixedRoles.append(anonymousUser.getExtAttributes().get(EXT_ROLES));
		u.getExtAttributes().put(EXT_ROLES, fixedRoles.toString());
	}
	//merge松散认证end
	
	public Object valid(Object obj) throws LoginException {
		UMPrincipal u = null;
		if (obj instanceof String) {
			String code = (String)obj;
			if ( centerLoginValidateService != null) {
				u = (UMPrincipal)centerLoginValidateService.valid(obj);
//				SessionContext.getSession().setAttribute("uas_artifact", obj);
//				if (config.uasIntegrateConfig.type != UasIntegrateConfig.FULL){
//					u = checkOrganiztionIp(u);
//				}
				if (u == null){
					return null;
				}
				//merge松散认证 begin
				fixExRole(u);
				//merge松散认证 end
				return u;
			}
			//artifact login
			String[] artifactSegments = parseArtifact(code);
			if (artifactSegments != null){
				ISession session = SessionContext.getSession(artifactSegments[1]);
				if (session == null || session.getAttribute("uas_artifact") == null){
					return null;
				}else {
					u = session.getCurrentUser();
				}
			}else {
				if (u == null && config.supportAdditionalIdLogin) {
					EntityQuery q  = EntityQuery.createExactQuery("code", code);
					q = EntityQuery.createAndQuery(q, EntityQuery.createExactQuery("loginType", AdditionalId.LOGINTYPE_ID));
					AdditionalId pin = crudPrivateService.querySingle(AdditionalId.class, q);
					if (pin != null){
						u = pin.getOwner();
					}
				}
			}
			u = checkOrganiztionIp(u);
		}else if (obj instanceof X509Certificate []){
			if (config.certificateAuthConfig != null && config.certificateAuthConfig.enable){
				X509Certificate [] certs = (X509Certificate [])obj;
				X509Certificate userCert = certs[0];
				try {
					if (false) userCert.checkValidity();
					String fullName = userCert.getSubjectDN().getName();
					String[] attrPairs = fullName.split("\\, ");
					u =  new UMPrincipal();
					for (String pair : attrPairs){
						String[] kv = pair.split("\\=");
						System.out.println("k: " + kv[0] + ", v:" + kv[1]);
						String field = config.certificateAuthConfig.certAttrToUserFieldMap.get(kv[0]);
						if (field != null){
							try {
								crudPrivateService.getEntityFullInfoPool().getClassInfoPool().setPropertityOrMapValue(u, field, kv[1]);
							} catch (Throwable e) {
								e.printStackTrace();
							} 
						}
					}
					u.setValidDate(userCert.getNotBefore());
					u.setInvalidDate(userCert.getNotAfter());
					if (u.getEmail() != null){
						u.setLoginId(u.getEmail());
					}
					
				} catch (CertificateExpiredException e) {
					e.printStackTrace();
				} catch (CertificateNotYetValidException e) {
					e.printStackTrace();
				}
			}else {
				throw new LoginException("certficate auth is disabled");
			}
			
			
		}
		else {
			throw new LoginException("unsupported credential type: " + ( obj != null ? obj.getClass() : null ));
		}
		u = u.clone();
		u.setPassword("");
		fillAuthInfo(u);
		return u;
	}

	public Object validIp(String remoteIp, String localIp) throws LoginException {
		if (config.supportIPLogin) {
			if (centerLoginValidateService != null){
				UMPrincipal u = (UMPrincipal)centerLoginValidateService.validIp(remoteIp, localIp);
				if (u != null){
					fixExRole(u);
				}
				return u;
			}
			Organization o = ipIndex.searchLuck(remoteIp);
			if (o == null){
				return null;
			}
			UMPrincipal p = new UMPrincipal();
			p.setLoginId(remoteIp);
			p.setName(o.getCode());
			p.setInvalidDate(new Date(System.currentTimeMillis() + 1000L * 3600 * 24));
			p.setLocalLoginId(remoteIp);
			p.setLoginId(remoteIp);
			p.setIpUser(true);
			p.setOrganization(o);
			fillAuthInfo(p);
			return p;
		}
		return null;
	}

	/**
	 * 目前该方法在用户是缓存的时候，必须使用用户的密码，该代码需重构
	 */
	public Object validNameAndPassword(final String name, final String password) throws LoginException {
		if (centerLoginValidateService != null){
			UMPrincipal u = (UMPrincipal)centerLoginValidateService.validNameAndPassword(name, password);
			if (u != null){
				fixExRole(u);
			}
			return u;
		}
		final ObjectRef<Boolean> isPinLogin = new ObjectRef<Boolean>();
		isPinLogin.setValue(false);
		if (useUserCache) {
			UMPrincipal u = cacheByConnection.userIdCaches.get(name);
			if (u != null){
				if (!password.equals(u.getPassword())){
					u = null;
				}
			}else if(cacheByConnection.cardIdCaches.get(name)!=null){
				u = cacheByConnection.cardIdCaches.get(name);
				if (u != null){
					if (!password.equals(u.getPassword())){
						u = null;
					}
				}
			}else if(cacheByConnection.aidCaches.get(name)!=null){ //add by chenfeng for additional Id login
				u = cacheByConnection.aidCaches.get(name);
				if(u!=null){
					if (!password.equals(u.getPassword())){
						u = null;
					}
				}
			}
			fillAuthInfo(u);
			return u;
		}
		UMPrincipal p = 
		AccessController.doPrivileged(
					new PrivilegedAction<UMPrincipal>() {
						public UMPrincipal run()  {
							EntityQuery q = EntityQuery.createExactQuery("loginId", name);
							UMPrincipal u = crudPrivateService.querySingle(UMPrincipal.class, q);
//							if (u != null) {
//								return u;
//							}
							if (u == null && config.supportCardLogin) {
								//login by card
								q = EntityQuery.createExactQuery("code", name);
								Card c = crudPrivateService.querySingle(Card.class, q);
								if (c != null){
									u = c.getPrinciple();
								}
							}
							if (u == null && config.supportAdditionalIdLogin) {
								//login by pin
								q = EntityQuery.createExactQuery("code", name);
								AdditionalId pin = crudPrivateService.querySingle(AdditionalId.class, q);
								if (pin != null) {
									if (pin.getLoginType() == AdditionalId.LOGINTYPE_DESABLE){
										return null;
									}
									u = pin.getOwner();
									if (pin.getLoginType() == AdditionalId.LOGINTYPE_ID) {
										isPinLogin.setValue(true);
									}else if (!config.onlyUseMainPassword) {
										if (password == null || !password.equals(pin.getPassword()) ){
											u = null;
										}
									}
								}
							}
							u = checkOrganiztionIp(u);
							return u;
						}
					}
			);
		
		if (p == null) {
			return null;
		}
		if (config.onlyUseMainPassword && !isPinLogin.getValue()) {
			if (password == null || !password.equals(p.getPassword()) ){
				p = null;
			}
		}
		fillAuthInfo(p);
		return p;
	}
	
//	@Override
//	public boolean checkPermission(AASRuntimeContext ctx) {
//		return true;
//	}
	
	protected UMPrincipal checkOrganiztionIp(UMPrincipal u) {
		if (u != null){
			//check checkOrgIp
			Organization uo = u.getOrganization();
			if (u.isCheckOrgIp() && uo != null){
				Organization o = ipIndex.searchLuck(RequestContext.getRequest().getRemoteIp());
				if (o == null || !o.getId().equals( uo.getId() )){
					u = null;
				}
			}
		}
		return u;
	}

	protected void cacheTest() {
		log.info("begin loading");
	}
	
	protected void activate(ComponentContext ctx) {
		
		
		
		config = confAdmin.get("calisUM", UMConfig.class);
		cacheByConnection = (CacheByConnection)icacheByConnection;
		
		if (!UMConfig.CACHEUSER_NONE.equals(config.cacheType)){
			try {
				cacheByConnection.loadUserCache(config.cacheLimit,null);
				useUserCache = true;
			} catch (Throwable e) {
				log.error("加载数据失败", e);
				System.exit(0);
			}
		}
		
		log.info(i18nService.getFormattedString("start_service", new Object[] {ClassInfoPool.getDafault().getProfile(config)}));
		accessControlProviderRegister.register(this);
		if (config.uasIntegrateConfig != null && config.uasIntegrateConfig.type == UasIntegrateConfig.SHARE){
			HttpServerConfig httpcfg = confAdmin.get("httpServer", HttpServerConfig.class);
			try {
				ServiceURL curl = new ServiceURL("es:ws-hessian-http://" + config.uasIntegrateConfig.host + ":" + config.uasIntegrateConfig.port + "/easyservice/" + IAuthorizationService.class.getName());
				centerAuthorizationService = register.lookup(curl, IAuthorizationService.class, config.uasIntegrateConfig.target);
				rebuildRoleIndex(centerAuthorizationService.getAllRoles(),null);
//				if (config.supportIPLogin){
					ipIndex = (IPSegmentMemoryIndex<Organization>)centerAuthorizationService.getIpIndex();
//				}
				centerToken = centerAuthorizationService.registerFullIntegrateSystem(httpcfg.getPort());
			} catch (Throwable e) {
				log.warn("无法获得远程认证中心授权服务" , e);
			} 
		}else {
			for (String pc : permissionRelatedClasses){
				crudService.registerEntityCRUDListener(pc, this);
				crudPrivateService.registerEntityCRUDListener(pc, this);
			}
			for (String ic : ipRelatedClasses){
				crudService.registerEntityCRUDListener(ic, this);
				crudPrivateService.registerEntityCRUDListener(ic, this);
			}
			rebuildRoleIndex(null,null);
			rebuildIpIndex();
		}
		if (config.uasIntegrateConfig != null && config.uasIntegrateConfig.type != UasIntegrateConfig.FULL
				&&config.uasIntegrateConfig.type!=UasIntegrateConfig.NONE){
			try {
				ServiceURL lurl = new ServiceURL("es:ws-hessian-http://" + config.uasIntegrateConfig.host + ":" + config.uasIntegrateConfig.port + "/easyservice/" + ILoginValidateService.class.getName());
				centerLoginValidateService =  register.lookup(lurl, ILoginValidateService.class, config.uasIntegrateConfig.target);
				fillAuthInfo(anonymousUser);
			} catch (Throwable e) {
				log.warn("无法获得远程认证中心认证服务" , e);
			} 
			
		}
		crudService.setAuthorizationService(this);
		crudPrivateService.setAuthorizationService(this);
		//merge松散认证 begin
		if (config.uasIntegrateConfig != null && config.uasIntegrateConfig.type == UasIntegrateConfig.AUTH){
			fillAuthInfo(anonymousUser);
		}
		//merge松散认证 end
	}
	
	protected void deactivate(ComponentContext ctx) {
		accessControlProviderRegister.unRegister(this);
	}

	@Override
	public boolean checkUITypeAndOpPermission(AASRuntimeContext ctx) {
		IPermissionDetail p = ctx.getPermissionDetail();
		if (p instanceof EntityPermission) {
			EntityPermission ep = (EntityPermission) p;
			List<Role> roles = (List<Role>)getRolesByUser( ctx.getEnvironment().getCurrentUser());
			for (Role r : roles) {
				if (r.isBlackRole() && r.implyEntityTypeAndOp(ctx) && r.toEntityQuery(ctx) == EntityQuery.NO_FILTER_QUERY) {
//					if (getCurrentUserQueryFilter(ep.getEntityClass()) == EntityQuery.FILTER_ALL_QUERY){
//						return false;
//					}
					return false;
				}
			}
			for ( Role r :  roles) {
				if (!r.isBlackRole() && r.implyEntityTypeAndOp(ctx)) {
					return true;
				}
			}
			
			return false;
		}
		return super.checkUIPermission(ctx);
	}
	

	public void afterCRUDSuccessEvent(final EntityPropertityItemStatus rootOpInfo, final List<EntityPermissionExcuteStatus> permissionStatusForLog) {
		AccessController.doPrivileged(new PrivilegedAction<Object>(){
			public Object run() {
				final String  clz = rootOpInfo.getItem().getClass().getName();
				if (permissionRelatedClasses.contains(clz)) {
					rebuildRoleIndex(null,null);
					log.info("finish rebuild role index.");
				}
				if (ipRelatedClasses.contains(clz)) {
					rebuildIpIndex();
					log.info("finish rebuild IP index.");
				}
				if (!subAuthorizationServiceMap.isEmpty()){
					Thread t = new Thread() {
						@Override
						public void run() {
							for (Entry<String, Object> ue : subAuthorizationServiceMap.entrySet()){
								try{
									IAuthorizationService ss = register.lookup((ServiceURL) ue.getValue(), IAuthorizationService.class);
									if (ss != null){
										ss.notifyUpdate(ue.getKey(), permissionRelatedClasses.contains(clz) ? Role.class.getName() : Organization.class.getName());
									}
								}catch (Exception e) {
									log.warn("通知下属集成系统失败: " + ue.getKey());
								}								
							}
						}
					};
					t.start();
				}
				return null;
			}
		});
		
	}
	
	public void rebuildIpIndex() {
//		if (config.supportIPLogin) {
			ipIndex.reset();
			List<String> lazyFields = new ArrayList<String>();
			lazyFields.add("ipRanges");
			List<Organization> orgs = crudPrivateService.query(Organization.class, null);
			for (Organization o : orgs) {
				for (IPRange r : o.getIpRanges()) {
					for (IPSegment s : r.getIpSegments()){
						ipIndex.addIPRange(s.getStart(), s.getEnd(), o);
					}
					//add by chenfeng for IPV6 support 
					for (IPV6Segment s : r.getIpV6Segments()){
						ipIndex.addIPV6Range(s.getStart(), s.getEnd(), o);
					}
				}
			}
//		}
	}

	private void rebuildRoleIndex(List<Role> roles,String tenantId) {
	    if (roles == null){
	    	roles = crudPrivateService.query(Role.class, null);
	    }
		List<Role> blackRoles = new ArrayList<Role>();
		List<Role> illegalRoles = new ArrayList<Role>();
		Map<String, Role> roleIndexSwitch = new HashMap<String, Role>();
		//****对于统一认证用户采用validate后修正的方法，所以目前舍弃此方式
//		String appid = config.uasIntegrateConfig.appid;
		for (Role r : roles) {
			if (!r.isEnabled()){
				continue;
			}
			try {
				r.compile();
				if (r.isBlackRole()){
					blackRoles.add(r);
				}
//				
//				if (r instanceof LegacyRole) {
//					LegacyRole lr = (LegacyRole) r;
//					roleIndexSwitch.put(lr.getApplication().getAuthId() + "." + lr.getName(), lr);
//				}else{
//					roleIndexSwitch.put(r.getName(), r);
//				}
				
				//****对于统一认证用户采用validate后修正的方法，所以目前舍弃此方式
//				if (appid != null){
//					roleIndexSwitch.put(appid + "." + r.getName(), r);
//				}else{
					roleIndexSwitch.put(r.getName(), r);
//				}
				
				
			} catch (Throwable e) {
				illegalRoles.add(r);
				log.warn("发现角色" + r.getName() + "中有非法的规则，该角色将被忽略", e);
			}
		}
		roles.removeAll(illegalRoles);
		roles.removeAll(blackRoles);
		if (config.uasIntegrateConfig != null && config.uasIntegrateConfig.type != UasIntegrateConfig.FULL){
				fillAuthInfo(anonymousUser);
		}
		synchronized (this.blackRoles) {
			super.roles = (List)roles;
			super.blackRoles = (List)blackRoles;
			this.roleIndex = roleIndexSwitch;
		}
	}

	/**
	 * @return EntityQuery.NO_FILTER_QUERY 没有过滤， FILTER_ALL_QUERY 表示过滤全部
	 */
	public EntityQuery getCurrentUserQueryFilter(Class entityClass) {
		Object user = SessionContext.getCurrentUser();
		AASRuntimeContext ctx = new AASRuntimeContext();
		ctx.fillEnv(null, null);
		String[] vs = {"view"};
		ctx.fillInitUser(user);
		if (user == null){
			return EntityQuery.NO_FILTER_QUERY;
		}
		EntityQuery rtquery = null;
		EntityQuery normalQuery = null;
		List<Role> roles = (List<Role>)getRolesByUser(user);
		EntityQuery blackQuery = null;
		for (Role r :  roles) {
			if (!r.isBlackRole() && normalQuery == EntityQuery.NO_FILTER_QUERY){
				continue;
			}
			EntityQuery cur = r.toEntityQuery(ctx, entityClass.getName(), vs);
			if (cur == null){
				continue;
			}
			if (cur == EntityQuery.NO_FILTER_QUERY){
//				return EntityQuery.NO_FILTER_QUERY;
				if (r.isBlackRole()){
					return EntityQuery.FILTER_ALL_QUERY;
				}else {
					normalQuery = EntityQuery.NO_FILTER_QUERY;
					continue;
				}
			}
			if (r.isBlackRole()){
				if (blackQuery == null){
					blackQuery = cur;
				}else {
					blackQuery = EntityQuery.createOrQuery(blackQuery, cur);
				}
			}else {
				if (normalQuery == null){
					normalQuery = cur;
				}else {
					normalQuery = EntityQuery.createOrQuery(normalQuery, cur);
				}
			}
		}
//		if (blackQuery == EntityQuery.NO_FILTER_QUERY){
//			return null;
//		}
		if (blackQuery != null && normalQuery != null){
			if (normalQuery == EntityQuery.NO_FILTER_QUERY){
				rtquery = EntityQuery.createNotQuery(blackQuery);
			}else {
				rtquery = EntityQuery.createAndQuery(normalQuery, EntityQuery.createNotQuery(blackQuery));
			}
		}else {
			rtquery = normalQuery;
		}
		if (rtquery != EntityQuery.NO_FILTER_QUERY) {
			//check current permission maybe call by doPrivileged
			if (PermissionCheckUtil.checkPermission(new EntityPermission(entityClass, "view") )){
				return EntityQuery.NO_FILTER_QUERY;
			}
		}
		if (rtquery == null){
			rtquery = EntityQuery.FILTER_ALL_QUERY;
		}
		return rtquery;
	}

	/**
	 * 由EntityCRUDService校验权限和过滤
	 */
	public List getAllRoles() {
		List<Role> allRoles = new ArrayList<Role>();
		allRoles.addAll((List)blackRoles);
		allRoles.addAll((List)roles);
		return allRoles;
	}

//	private void updateAllRoles(List roles) {
//		PermissionCheckUtil.checkPermissionWithException( new EntityPermission(Role.class, "update") );
//		rebuildRoleIndex(roles);
//	}
//	
//	private void updateAllOrg(List orgs) {
//		PermissionCheckUtil.checkPermissionWithException( new EntityPermission(Organization.class, "update") );
//		reb(roles);
//	}

	public String registerFullIntegrateSystem(int port) {
		String host = RequestContext.getRequest().getRemoteIp();
		try {
			ServiceURL url = new ServiceURL("http://" + host + ":" + port + "/easyservice/" + IAuthorizationService.class.getName());
			String key = host+":"+port;
			subAuthorizationServiceMap.put(key, url);
			return key;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void notifyUpdate(String token, String type) {
		if (token.equals(centerToken)) {
			Thread task = null;
			if (Organization.class.getName().equals(type) 
//					&& config.supportIPLogin
					){
				task = new Thread(){
					@Override
					public void run() {
						synchronized (ipIndex) {
							ipIndex =  (IPSegmentMemoryIndex<Organization>)centerAuthorizationService.getIpIndex();
						}
					}
				};
			}else if (Role.class.getName().equals(type)){
				task = new Thread(){
					@Override
					public void run() {
						rebuildRoleIndex(centerAuthorizationService.getAllRoles(),null);
					}
				};
			}
			if (task != null){
				task.start();
			}
		}
	}

	public Object getIpIndex() {
		return ipIndex;
	}

	//为支持统一授权所作调整
	@Override
	public boolean checkPermission(AASRuntimeContext ctx) {
		UMPrincipal u = (UMPrincipal)ctx.getEnvironment().getCurrentUser();
		String tenantID = null;
		if(u.getTenant()!=null){
			tenantID=u.getTenant().getTenantId();
		}
		
		if (u == null){
			return false;
		}
//		String aid = u.getExtAttributes().get(EXT_AID);
//		String appid = config.uasIntegrateConfig.appid;
		if (config.uasIntegrateConfig.type == 1 
//				&&  aid != null && !aid.equals(config.uasIntegrateConfig.aid)
				){
			String rolesString = u.getExtAttributes().get(EXT_ROLES);
			if (rolesString == null){
				return false;
			}
			String[] userRoles = rolesString.split("\\,");
			
			for (IDynamicRole r : getMyRoles(blackRoles,tenantID)){
				if (r.fit(ctx)){
					return false;
				}
			}
			for (String rs : userRoles){
				Role r =  roleIndex.get(rs);
				//因为从user中取出的rolesString是在调用getRolesByUser后存入的，已经是自己的角色了，就不用再次判断了
//				if(!isMyRole(r,tenantID)){
//					continue;
//				}
				if (r != null && r.fitPermission(ctx)){
					if (r.isBlackRole()){
						return false;
					}
					return true;
				}
			}
			return false;
		}else{
			return super.checkPermission(ctx);
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends IDynamicRole> getRolesByUser(Object user) {
		//merge松散认证 begin
		UMPrincipal u = (UMPrincipal) user;
		if (config.uasIntegrateConfig != null
				&& config.uasIntegrateConfig.type == UasIntegrateConfig.AUTH && !anonymousUser.getId().equals(u.getId())) {
			String rolesString = u.getExtAttributes().get(EXT_ROLES);
			if (rolesString == null) {
				return Collections.EMPTY_LIST;
			}
			String[] prefixRoles = rolesString.split("\\,");
			List rt = new ArrayList<IDynamicRole>();
			for (String pr : prefixRoles) {
				IDynamicRole role = (IDynamicRole) roleIndex.get(pr);
				if (role != null) {
					rt.add(role);
				}
			}
			return rt;
			//merge松散认证 end
		} else {
			UMPrincipal umuser = (UMPrincipal) user;
			List<IDynamicRole> rt = new ArrayList<IDynamicRole>();
			String tenantID = null;
			if (umuser.getTenant() != null) {
				tenantID = umuser.getTenant().getTenantId();
			}

			AASRuntimeContext ctx = new AASRuntimeContext();
			ctx.fillEnv(null, null);
			ctx.fillInitUser(user);
			for (IDynamicRole r : getMyRoles(roles, tenantID)) {
				if (r.fitUser(ctx)) {
					rt.add(r);
				}
			}
			for (IDynamicRole r : getMyRoles(blackRoles, tenantID)) {
				if (r.fitUser(ctx)) {
					rt.add(r);
				}
			}
			return rt;
		}
	}
	
	
	/*
	 * 判断当前的Role是否适用于这个用户
	 */
	private boolean isMyRole(IDynamicRole role, String TenantId) {
		boolean result = false;

		if (role instanceof Role) {
			if (((Role) role).getTenant() != null) {
				// tenantID为空的规则为公共规则，如$anonymous_role
				if (((Role) role).getTenant().getTenantId() == null) {
					result = true;

					// 自己机构的Role
				} else if (((Role) role).getTenant().getTenantId() != null
						&& TenantId != null
						&& ((Role) role).getTenant().getTenantId().equals(
								TenantId)) {
					result = true;
				}
				// 自己机构的tenantID为null的是管理员，别的机构的Role，不适合他
				else if (((Role) role).getTenant().getTenantId() != null
						&& TenantId == null) {
					result = false;
				}
			} else {//没有tenant，返回true
				result = true;
			}
		} else {
			// 如果不是Role，而是SimpleDynamicRole，直接返回true
			result = true;
		}
		return result;
	}
	
	
	/*
	 * 返回适用于当前用户的Role
	 */
	private List<? extends IDynamicRole> getMyRoles(List<? extends IDynamicRole> roles, String TenantId) {
		List<Role> resultRoles = new ArrayList<Role>();
		for (IDynamicRole role : roles) {
			if (isMyRole(role,TenantId)) {
				resultRoles.add((Role) role);
			}
		}
		return roles;
	}
	
	
	public void afterCRUDEvent(EntityPropertityItemStatus rootOpInfo) {
		
	}

	public void beforeCRUDEvent(EntityPropertityItemStatus rootOpInfo) {
		
	}


	public void rebuildRoleIndex(String tenantId) {
		rebuildRoleIndex(null,tenantId);
	}

}
