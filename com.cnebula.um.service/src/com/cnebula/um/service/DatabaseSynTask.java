package com.cnebula.um.service;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import javax.sql.DataSource;


public interface DatabaseSynTask extends  Runnable {

	public abstract ScheduledFuture getScheduledFuture();

	public abstract String getCurrentStatus();

	public abstract DataSource getFromDataSource();
	
	public abstract DataSource getToDataSource();

	public abstract Date getLastRecordTimestamp();



	public abstract String getName();

	public abstract void setScheduledFuture(ScheduledFuture sf);

	public abstract DatabaseSynTaskInfo getTaskInfo();

	
}