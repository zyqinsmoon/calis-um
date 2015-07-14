package com.cnebula.um.service;

public class UasIntegrateConfig {
	
	public final static int NONE = 0;
	public final static int AUTH = 1;
	public final static int SHARE = 2;
	public final static int FULL = 3;
	
	public final static String VERSIONTYPE_LOCAL = "standard";//独立版统一认证
	public final static String VERSIONTYPE_CALISCENTER = "calisCenter";//CALIS中心版
	public final static String VERSIONTYPE_SAASCENTER = "saasCenter";//省中心版
	
	public String aid;
	public String appid;
	public String host;
	public int port=80;
	public int type = FULL;
	public String target = "(id=UMValidLoginService)";
	public String versionType = VERSIONTYPE_LOCAL;
	public int ver = 330; //3.3.0
	
}
