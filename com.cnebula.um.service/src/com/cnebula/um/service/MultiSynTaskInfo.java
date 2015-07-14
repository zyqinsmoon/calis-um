package com.cnebula.um.service;

public class MultiSynTaskInfo {
	
	
//	aConfig.put("ID", rs.getString(1));
//	aConfig.put("ENABLE", rs.getString(2));
//	aConfig.put("PERIOD", rs.getString(3));
//	aConfig.put("SUBFOLDER", rs.getString(4));
//	aConfig.put("RUNNINGTIMERANGES", rs.getString(5));
//	aConfig.put("SYNTOCACHE", rs.getString(6));
//	aConfig.put("NAME", rs.getString(7));
//	aConfig.put("ORGCODE", rs.getString(8));
//	aConfig.put("TYPE", rs.getString(9));
//	aConfig.put("ORGID", rs.getString(10));
//	aConfig.put("PASSWORDTYPE", rs.getString(11));
	public final static String STATUS_READY = "就绪";
	public static final String STATUS_NOT_ENABLED = "尚未启用";
	public static final String STATUS_RUNNING = "开始运行";
	
	private String name;
	private String currentStatus;
	private volatile boolean running = false;
	
	/***
	 * 一次提交处理的记录条数
	 */
	private int count = 0;
	/***
	 * 一次提交新增的记录条数
	 */
	private int curinsert = 0;	
	/***
	 * 一次提交更新的记录条数
	 */
	private int curupdate = 0;	
	/***
	 * 当前共处理多少条记录
	 */
	private int finished = 0;
	/***
	 * 当前共新建多少条记录
	 */
	private int insterted = 0;
	/***
	 * 当前共更新多少条记录
	 */
	private int updated = 0;
	/***
	 * 当前共出错多少条记录
	 */
	private int error = 0;
	
//	public long getPeriod() {
//		return period;
//	}
//
//
//
//	public void setPeriod(long period) {
//		this.period = period;
//	}
//


	public MultiSynTaskInfo() {
	}
	
	
	



public MultiSynTaskInfo(String name, String currentStatus, boolean running,
		int count, int curinsert, int curupdate, int finished, int insterted,
		int updated, int error) {
	super();
	this.name = name;
	this.currentStatus = currentStatus;
	this.running = running;
	this.count = count;
	this.curinsert = curinsert;
	this.curupdate = curupdate;
	this.finished = finished;
	this.insterted = insterted;
	this.updated = updated;
	this.error = error;
}






//	public MultiSynTaskInfo(String name, String currentStatus, boolean isRunning) {
//		super();
//		this.name = name;
//		this.currentStatus = currentStatus;
//		this.running = isRunning;
//	}

	

	public String getName() {
		return name;
	}
//	public String getDir() {
//		return dir;
//	}
//
//
//
//	public void setDir(String dir) {
//		this.dir = dir;
//	}
//
//
//
//	public String getRunningRangesExpression() {
//		return runningRangesExpression;
//	}
//
//
//
//	public void setRunningRangesExpression(String runningRangesExpression) {
//		this.runningRangesExpression = runningRangesExpression;
//	}
//


	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}



	public int getCurupdate() {
		return curupdate;
	}



	public void setCurupdate(int curupdate) {
		this.curupdate = curupdate;
	}



	public int getFinished() {
		return finished;
	}



	public void setFinished(int finished) {
		this.finished = finished;
	}



	public int getUpdated() {
		return updated;
	}



	public void setUpdated(int updated) {
		this.updated = updated;
	}



	public int getError() {
		return error;
	}



	public void setError(int error) {
		this.error = error;
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
	
	public int getCurinsert() {
		return curinsert;
	}



	public void setCurinsert(int curinsert) {
		this.curinsert = curinsert;
	}



	public int getInsterted() {
		return insterted;
	}



	public void setInsterted(int insterted) {
		this.insterted = insterted;
	}



	public void resetCounter(){
		setCount(0);
		setCurupdate(0);
		setError(0);
		setFinished(0);
		setUpdated(0);
		setCurinsert(0);
		setInsterted(0);
//		set
	}


//	public MultiSynTaskInfo copy(){
//		MultiSynTaskInfo info=new MultiSynTaskInfo(
//				name,currentStatus,running,count,curupdate,finished,updated,error
//				); 
//		return info;
//	}
	
	
	
}
