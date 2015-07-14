package com.cnebula.um.service;

import java.util.Map;

public interface IMultiSynTaskManager {
	 public MultiSynTaskInfo getTaskInfo(String taskID);
	 public void stopTask(String taskID);
	 public void startTask(Map<String,String> taskConfig);
	 public void deleteTask(String taskID);
	 public void updateTaskConfig(Map<String,String> taskConfig);
	 public void forceStop(String taskID);
}
