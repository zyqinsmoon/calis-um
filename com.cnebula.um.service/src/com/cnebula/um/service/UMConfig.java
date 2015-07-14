package com.cnebula.um.service;

import com.cnebula.um.service.CertificateAuthConfig;

public class UMConfig {
	public boolean onlyUseMainPassword = true;
	public boolean supportCardLogin = true;
	public boolean supportAdditionalIdLogin = true;
	public boolean supportIPLogin = false;
	public final static String CACHEUSER_FULL = "full";
	public final static String CACHEUSER_COMPACT = "compact";
	public final static String CACHEUSER_NONE = "none";
	public UasIntegrateConfig uasIntegrateConfig;
	public CertificateAuthConfig certificateAuthConfig;
	public String cacheType = "none"; // full, compact
	public int cacheLimit = Integer.MAX_VALUE;
	public String editableUserCondition = "true";
}
