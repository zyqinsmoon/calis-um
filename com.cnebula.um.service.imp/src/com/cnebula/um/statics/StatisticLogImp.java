package com.cnebula.um.statics;

import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import com.cnebula.aas.rt.IPermissionDetail;
import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.es.TxType;
import com.cnebula.common.conf.IEasyServiceConfAdmin;
import com.cnebula.common.ejb.IStatisticLogService;
import com.cnebula.common.ejb.manage.EntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.EntityPermissionExcuteStatus;
import com.cnebula.common.ejb.manage.EntityPropertityItemStatus;
import com.cnebula.common.ejb.manage.IEntityCRUDListener;
import com.cnebula.common.ejb.manage.IEntityCRUDPrivateService;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.common.ejb.manage.perm.ResourceType;
import com.cnebula.common.ejb.relation.EntityInfo;
import com.cnebula.common.ejb.relation.IEntityFullInfoPool;
import com.cnebula.common.ejb.relation.IEntityInfoPool;
import com.cnebula.common.es.IRequest;
import com.cnebula.common.es.RequestContext;
import com.cnebula.common.es.SessionContext;
import com.cnebula.common.log.ILog;
import com.cnebula.common.log.MessageLevel;
import com.cnebula.common.log.imp.LogImp;
import com.cnebula.common.log.imp.LogManager;
import com.cnebula.common.log.imp.TargetInitException;
import com.cnebula.common.log.target.FileTarget;
import com.cnebula.common.remote.IAdminService;
import com.cnebula.common.remote.Request;
import com.cnebula.common.remote.core.RemoteServiceAccessPermission;
import com.cnebula.common.security.auth.ILoginService;
import com.cnebula.um.ejb.entity.perm.GeneralResource;
import com.cnebula.um.ejb.entity.statistics.LogItem;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.setup.ISetupService;

@EasyService(properties={@Property(name="unit", value="#{umunit}")})
public class StatisticLogImp implements IStatisticLogService, IEntityCRUDListener{
	
	public static final String ENTITY_RESOURCE_TYPE_NAME = "$ENTITY_RESOURCE_TYPE" ; 
	public static final String PERMISSION_RESOURCE_TYPE_NAME = "$PERMISSION_RESOURCE_TYPE" ; 
	
	Thread saveThread;


	Object rw = new Object();
	
	Object quitMutex = new Object(); 

	protected volatile boolean shutDown = false;
	
	protected List<LogItem> buffer;

	protected AsynStatisticsLogConfig asyn ; 

	@ESRef(target="(unit=#{umunit})")
	protected IEntityCRUDPrivateService entityCRUDService ; 
	
	@ESRef
	protected Context jndiCtx;
	
	DataSource dataSource;
	
	@ESRef
	protected ILog generalLog ;
	
	@ESRef(target="(unit=#{umunit})")
	protected IEntityFullInfoPool entityInfoPool ;
	
	@ESRef
	protected ISetupService setupService;
	
	protected Map<String, ResourceType> resourceTypeMap = new  HashMap<String, ResourceType>();
	
	@ESRef
	protected IEasyServiceConfAdmin configAdmin;
	
	
	LogImp integerateLog ; 
	volatile boolean quited = false;
	
	protected void activate(ComponentContext ctx) throws TargetInitException, NamingException {
		dataSource = ((PersistenceUnitInfo)jndiCtx.lookup("java:comp/env/pui/"+ctx.getProperties().get("unit"))).getJtaDataSource();
		refreshResourceTypeMap();
		entityCRUDService.registerEntityCRUDListener(ResourceType.class.getName(), this);
		asyn = configAdmin.get("statisticsConfig", AsynStatisticsLogConfig.class);
		if(asyn == null){
			asyn = new AsynStatisticsLogConfig();
		}
		initAsynBufferAndThread();
		if(asyn.integrate!=null){
			FileTarget fileTarget = new FileTarget();
			Map<String, String> params = new HashMap<String, String>();
			params.put(FileTarget.ROOTDIR, asyn.integrate.integrateLogDir);
			fileTarget.init(params);
			IntegerateFormatter formatter = new IntegerateFormatter();
			formatter.setLogType(asyn.integrate.logType);
			formatter.setActionId(asyn.integrate.actionId);
			fileTarget.setFormatter(formatter);
			fileTarget.setId("Integrate");
			integerateLog = new LogImp(MessageLevel.all);
			integerateLog.addTarget(fileTarget);
		}
	}
	
	
	public void initAsynBufferAndThread() {
		if (asyn.bufferSize <= 0) {
			asyn.bufferSize = AsynStatisticsLogConfig.DEFAULT_BUFFER_SIZE;
		}
		if (asyn.flushTime <= 0) {
			asyn.flushTime = AsynStatisticsLogConfig.DEFAULT_FLUSH_TIME;
		}
//		buffer = new ArrayBlockingQueue<LogItem>(
//				asyn.bufferSize > 0 ? asyn.bufferSize
//						: AsynStatisticsLogConfig.DEFAULT_BUFFER_SIZE);
		buffer = new ArrayList<LogItem>(asyn.bufferSize > 0 ? asyn.bufferSize
				: AsynStatisticsLogConfig.DEFAULT_BUFFER_SIZE);
		createSaveThread(asyn.flushTime*1000L);
		Dictionary d = new Hashtable();
		d.put(EventConstants.EVENT_TOPIC, IAdminService.TOPIC_ADMIN_SHUTDOWN );
		if(LogManager.instance.getBundleContext()!=null){
			LogManager.instance.getBundleContext().registerService( EventHandler.class.getName(),
					new EventHandler() {
						public void handleEvent(Event event) {
							shutDown = true;
							synchronized (rw) {
								rw.notifyAll();
							}
							
								try {
									int i = asyn.shutDownWaitTime;
									while (true){
										if (i-- < 0 || quited) {
											return;
										}
										synchronized (rw) {
											rw.notifyAll();
										}
										generalLog.info("wait for log thread exit");
										synchronized (quitMutex) {
											quitMutex.wait(1000L);
										}
									}
									
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							
						}
					}
					, d );
		}
		saveThread.setPriority(asyn.threadPriority);
		saveThread.start();
	}

	private void createSaveThread(final long flushTime) {
		try {
			saveThread = new Thread() {
				@Override
				public void run() {
					String sql = "INSERT INTO UM_LOGITEM (ID, "
							+ "CMONTH, "
							+ "CWEEK, "
							+ "STATUS, "
							+ "CDAY, "
							+ "SESSIONID, "
							+ "SUBJECTTYPE, "
							+ "CYEAR, SUBJECTID, "
							+ "REMOTEIP, "
							+ "OBJECTTYPE, "
							+ "OBJECTID, "
							+ "OPERATION, "
							+ "CREATEDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

					while (true) {
						synchronized (rw) {
							try {
								rw.wait(flushTime);
							} catch (InterruptedException e) {// should never
								e.printStackTrace();
							}
							// start to work
							if (!buffer.isEmpty()) {
								Connection conn = null;
								try {
									conn = dataSource.getConnection();
									conn.setAutoCommit(false);
									PreparedStatement pstmt = conn
											.prepareStatement(sql);
									for (LogItem top : buffer) {
										((EntityCRUDPrivateService) entityCRUDService)
												.fixId(top);
										int pos = 0;
										pstmt.setString(++pos, top.getId());
										pstmt.setInt(++pos, top.getCmonth());
										pstmt.setInt(++pos, top.getCweek());
										pstmt.setInt(++pos, top.getStatus());
										pstmt.setInt(++pos, top.getCday());
										pstmt.setString(++pos, top
												.getSessionId());
										pstmt.setString(++pos, top
												.getSubjectType());
										pstmt.setInt(++pos, top.getCyear());
										pstmt.setString(++pos, top
												.getSubjectId());
										pstmt.setString(++pos, top
												.getRemoteIp());
										pstmt.setString(++pos, top
												.getObjectType());
										pstmt.setString(++pos, top
												.getObjectId());
										pstmt.setString(++pos, top
												.getOperation());
										pstmt.setTimestamp(++pos,
												new Timestamp(top
														.getCreateDate()
														.getTime()));
										pstmt.addBatch();
										if(integerateLog!=null){
											integerateLog.log(MessageLevel.info, top, null);
										}
									}
									pstmt.executeBatch();
									pstmt.close();
									conn.commit();
								} catch (Throwable e) {
									try {
										conn.rollback();
									} catch (SQLException e1) {
										generalLog.error("保存日志后回滚失败", e);
									}
									generalLog.error("保存日志失败", e);
								} finally {
									buffer.clear();
									if (conn != null) {
										try {
											conn.close();
										} catch (SQLException e) {
											generalLog.error("保存日志后关闭连接失败", e);
										}
									}
								}
							}
							
						}

						if (shutDown) {
							synchronized (quitMutex) {
								quitMutex.notifyAll();
							}
							quited = true;
							return;
						} else {
							
						}

					}
				}
			};


		} catch (Exception e) { //should never happen
			e.printStackTrace() ; 
		}

	}
	
	public void asynLog(LogItem logItem) {
		synchronized (rw) {
			buffer.add(logItem);
			if (buffer.size() >= asyn.bufferSize){
				rw.notifyAll();
			}
		}
	}
	
	
	protected void refreshResourceTypeMap() {
		
		synchronized (resourceTypeMap) {
			resourceTypeMap.clear();
			for (ResourceType rt : entityCRUDService.queryAllResourceType()){
				resourceTypeMap.put(rt.getEntityType(), rt);
			}
		}
	}
	
	public  String getResourceTypeName(String entiyType) {
		ResourceType rt = null;
		synchronized (resourceTypeMap) {
			 rt = resourceTypeMap.get(entiyType);
		}
		return rt == null ? entiyType : rt.getName();
		
	}
	
	public void log(Object logItem) {
		if (logItem instanceof LogItem) {
			if(logItem!=null){
				LogItem i = (LogItem)logItem;
				i.setObjectType( getResourceTypeName(i.getObjectType()) );
				asynLog(i);
			}
		}
	}
	
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//	public void realLog(LogItem logItem) {
//		try{
//			entityCRUDService.create(logItem);
//			if(integerateLog!=null){
//				integerateLog.log(MessageLevel.info, logItem, null);
//			}
//		}catch (Exception e) {
//			generalLog.error(null, e);
//		}
//	}

	public void log(final Permission permission, final int status) {
//		AccessController.doPrivileged(new PrivilegedAction<Object>(){
//			public Object run() {
				LogItem logItem = CreateLogItem(permission, status);
				if(logItem!=null){
					asynLog(logItem);
				}
//				return null;
//			}
//		});
	}

	private LogItem CreateLogItem(Permission permission, int status) {
		
		LogItem logItem = new LogItem();
		
		//主体
		if(SessionContext.getSession()!=null){
			UMPrincipal umPrincipal = SessionContext.getSession().getCurrentUser();
			
			logItem.setSessionId(SessionContext.getSession().getId());
			
			if(umPrincipal!=null){
				logItem.setSubjectId(umPrincipal.getId());
			}
		}
		
		//客体
		logItem.setOperation(permission.getActions());
		
		if(permission instanceof IPermissionDetail){
			
			if (permission instanceof RemoteServiceAccessPermission) {
				RemoteServiceAccessPermission rsap = (RemoteServiceAccessPermission) permission;
				logItem.setObjectType(getResourceTypeName(rsap.getEntityClass().getName()));
				logItem.setOperation(rsap.getActions());
//				logItem.setStatus(status);
			}else {
				IPermissionDetail ipd = (IPermissionDetail) permission; 
				Object res = ipd.getResource() ; 
				
				if(res instanceof LogItem){
					return null ; 
				}
				if(res instanceof GeneralResource){
					String type="application";
					if(((GeneralResource)res).getType()!=null){
						type=((GeneralResource)res).getType().getName();
					}
					
					logItem.setObjectType(type) ; 
					logItem.setObjectId(((GeneralResource)res).getId());
				}else{
					
					
					EntityInfo ei = entityInfoPool.getEntityInfo(res.getClass());
					if(ei!=null){
						logItem.setObjectType( getResourceTypeName(ei.getType().getName()) );
						
						try {
							logItem.setObjectId(ei.getSimpleIdProperty().getValue(res)+"");
						} catch (Throwable e) {
						} 
					}else{
						
						//should not happen now
					}
				}
			}
			
		}else{
			logItem.setObjectType(PERMISSION_RESOURCE_TYPE_NAME) ; 
			logItem.setObjectId(permission.getName());
		}
		
		
		//环境
		IRequest request = RequestContext.getRequest();
		
		if(request != null ){
			logItem.setRemoteIp(RequestContext.getRequest().getRemoteIp());
			if (logItem.getSubjectId() == null){
				//if login service
				if (request instanceof Request) {
					Request rsReq = (Request)request;
					if (ILoginService.class.isAssignableFrom(rsReq.getInterfaceClass())) {
						String method = rsReq.getMehtodName();
						if (method.equals("loginByNamePassword")){
							logItem.setSubjectId((String)rsReq.getArgs()[0]);
						}else if (method.equals("loginByIp")){
							logItem.setSubjectId(rsReq.getRemoteIp());
						}else if (method.equals("loginByCredential") ){
							logItem.setSubjectId(""+rsReq.getArgs()[0]);
						}
					}
				}
			}
		}
		
		
		
		
		logItem.setStatus(status);
		
		logItem.setCreateDate(new Date());
		
		//cyear , cweek , cmonth , cday ;
		
		Calendar calendar = Calendar.getInstance() ; 
		calendar.setTime(logItem.getCreateDate());
		logItem.setCyear(calendar.get(Calendar.YEAR)) ; 
		logItem.setCmonth(calendar.get(Calendar.MONTH)+1);
		logItem.setCweek(calendar.get(Calendar.WEEK_OF_YEAR));
		logItem.setCday(calendar.get(Calendar.DAY_OF_YEAR));
		
		return logItem ; 
		
	}

	


	public boolean shouldPrepareLogForRemoteService(Request req) {
//		if (ILoginService.class.isAssignableFrom(req.getInterfaceClass()) && !req.getMehtodName().equals("logout") && !req.getMehtodName().startsWith("get")){
//			return true;
//		}
//		return false;
		return true;
	}

	public void afterCRUDSuccessEvent(EntityPropertityItemStatus rootOpInfo, List<EntityPermissionExcuteStatus> permissionStatusForLog) {
		refreshResourceTypeMap();
	}


	public void afterCRUDEvent(EntityPropertityItemStatus rootOpInfo) {
	}


	public void beforeCRUDEvent(EntityPropertityItemStatus rootOpInfo) {
	}



}
