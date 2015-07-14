package com.cnebula.um.statics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.cnebula.common.log.MessageLevel;
import com.cnebula.common.log.imp.FormatterInitException;
import com.cnebula.common.log.imp.IFormatter;
import com.cnebula.um.ejb.entity.statistics.LogItem;

public class IntegerateFormatter implements IFormatter {

	static  String LOG_TYPE_AND_OPERATION = null;
	
	String logType ; 
	
	String actionId ; 
	
	public void setLogType(String logType) {
		this.logType = logType;
	}
	
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	
	/**
	 * UserId , ResType , ResId, Operation, Opstatus, SessionId
	 */
	public String format(Object o) {
		final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LogItem logItem = (LogItem) o ; 
		StringBuilder rt = new StringBuilder() ; 
		rt.append(SDF.format(logItem.getCreateDate()));
		rt.append(getLogTypeAndOperation());
		rt.append(logItem.getRemoteIp()).append('%');
		rt.append("UserID=").append(logItem.getSubjectId()).append('%');
		if(logItem.getObjectType()!=null){
			rt.append("ResType=").append(logItem.getObjectType()).append('%');
		}
		if(logItem.getObjectId()!=null){
			rt.append("ResId=").append(logItem.getObjectId()).append('%');
		}
		if(logItem.getOperation()!=null){
			rt.append("Operation=").append(logItem.getOperation()).append('%');
		}
		rt.append("OpStatus=").append(logItem.getStatus()).append('%');
		if(logItem.getSessionId()!=null){
			rt.append("SessionId=").append(logItem.getSessionId()).append('%');
		}
		return rt.toString() ; 
	}
	
	private String getLogTypeAndOperation(){
		if(LOG_TYPE_AND_OPERATION == null){
			StringBuilder rt = new StringBuilder(" %LogType=");
			rt.append(logType);
			rt.append("%ActionID=");
			rt.append(actionId);
			rt.append("%IP=");
			LOG_TYPE_AND_OPERATION = rt.toString() ; 
		}
		return LOG_TYPE_AND_OPERATION ; 
	}

	public String format(Throwable t) {
		return null;
	}

	public String header(MessageLevel l) {
		return "";
	}

	public String header(MessageLevel l, Date date) {
		return "";
	}

	public void init(Map<String, String> params) throws FormatterInitException {
	}

}
