package com.cnebula.um.service;

import java.io.IOException;
import java.util.List;


public interface DatabaseSynTaskManager {

	public void addTask(DatabaseSynTask task);
	public List<DatabaseSynTaskInfo> getTaskList();
	public void stopTask(String name);
	public void startTask(String name);
	public DatabaseSynTaskInfo getTaskInfo(String name);
	public void reschedula(String name);
	
	public long getTaskLogCount(String task) throws IOException;
	
	public List<DatabaseSynTaskLogInfo> getTaskLogList(String task, int numPerpage, int curpage) throws IOException;
	

}
