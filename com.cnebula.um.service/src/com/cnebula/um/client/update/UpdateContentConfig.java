package com.cnebula.um.client.update;

public class UpdateContentConfig {
	
	protected String dir = "update";
	protected String fileListXml = "list.xml";
	protected String localJRE = "jre.exe";
	protected String remoteJRE = "http://www.oracle.com/technetwork/java/javase/downloads/index.html";
	protected String uiname = "智能客户端";
	
	
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getFileListXml() {
		return fileListXml;
	}
	public void setFileListXml(String fileListXml) {
		this.fileListXml = fileListXml;
	}
	public String getLocalJRE() {
		return localJRE;
	}
	public void setLocalJRE(String localJRE) {
		this.localJRE = localJRE;
	}
	public String getRemoteJRE() {
		return remoteJRE;
	}
	public void setRemoteJRE(String remoteJRE) {
		this.remoteJRE = remoteJRE;
	}
	public String getUiname() {
		return uiname;
	}
	public void setUiname(String uiname) {
		this.uiname = uiname;
	}
	
	
}