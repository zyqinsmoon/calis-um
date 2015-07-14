package com.cnebula.um.statics;

public class IntegrateConfig {
	
	public String integrateLogDir;
	public String logType ;
	public String actionId ;
	public boolean shouldIntegrate;
	
	public String getIntegrateLogDir() {
		return integrateLogDir;
	}

	public void setIntegrateLogDir(String integrateLogDir) {
		this.integrateLogDir = integrateLogDir;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public boolean isShouldIntegrate() {
		return shouldIntegrate;
	}

	public void setShouldIntegrate(boolean shouldIntegrate) {
		this.shouldIntegrate = shouldIntegrate;
	}
	
	

}
