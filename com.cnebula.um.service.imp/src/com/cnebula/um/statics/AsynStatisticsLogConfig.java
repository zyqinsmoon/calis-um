package com.cnebula.um.statics;

import com.cnebula.common.annotations.xml.XMLMapping;

public class AsynStatisticsLogConfig {

	// the size of log queue
	public int bufferSize = 3000 ;
	
	// the grade is milliSecond
	public int flushTime = 5 ;
	
	public int shutDownWaitTime = 60 ; 
	
	public int threadPriority = 5 ;
	
	public static final int DEFAULT_FLUSH_TIME = 5 ;
	
	public static final int DEFAULT_BUFFER_SIZE = 3000 ;
	
	public IntegrateConfig integrate;
	
//	public boolean  shouldIntegrate = false ; 

	

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getFlushTime() {
		return flushTime;
	}

	public void setFlushTime(int flushTime) {
		this.flushTime = flushTime;
	}

	public int getShutDownWaitTime() {
		return shutDownWaitTime;
	}

	public void setShutDownWaitTime(int shutDownWaitTime) {
		this.shutDownWaitTime = shutDownWaitTime;
	}

	public int getThreadPriority() {
		return threadPriority;
	}

	public void setThreadPriority(int threadPriority) {
		this.threadPriority = threadPriority;
	}

	public static int getDEFAULT_FLUSH_TIME() {
		return DEFAULT_FLUSH_TIME;
	}

	public static int getDEFAULT_BUFFER_SIZE() {
		return DEFAULT_BUFFER_SIZE;
	}

//	public boolean getShouldIntegrate() {
//		return shouldIntegrate;
//	}
//
//	public void setShouldIntegrate(boolean shouldIntegrate) {
//		this.shouldIntegrate = shouldIntegrate;
//	}

	@XMLMapping(tag = "integrate")
	public IntegrateConfig getIntegrate() {
		return integrate;
	}

	public void setIntegrate(IntegrateConfig integrate) {
		this.integrate = integrate;
	}
	
	
	
}
