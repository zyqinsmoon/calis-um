package com.cnebula.um.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.cnebula.common.log.ILog;

public interface DatabaseSynTaskLogService {

	public void log(String task, String status, Date recordTimestampfm);
	
	public List<DatabaseSynTaskLogInfo> topLog(String task, int topMax) throws IOException;
	
	public long getTaskLogCount(String task) throws IOException;
	
	public List<DatabaseSynTaskLogInfo> getTaskLogList(String task, int numPerpage, int curpage) throws IOException;
	
	public ILog getLog();
}
