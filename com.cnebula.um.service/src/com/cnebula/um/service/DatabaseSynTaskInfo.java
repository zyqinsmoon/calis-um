package com.cnebula.um.service;

public class DatabaseSynTaskInfo {
	
	public String name;
	public String currentStatus;
	public volatile boolean running = false;
	public long period = 60*30;
	public 	String runningRangesExpression;
	
	public long getPeriod() {
		return period;
	}



	public void setPeriod(long period) {
		this.period = period;
	}



	public DatabaseSynTaskInfo() {
	}
	
	
	
	public DatabaseSynTaskInfo(String name, String currentStatus, boolean isRunning) {
		super();
		this.name = name;
		this.currentStatus = currentStatus;
		this.running = isRunning;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean isRunning) {
		this.running = isRunning;
	}
	
	
	
}
