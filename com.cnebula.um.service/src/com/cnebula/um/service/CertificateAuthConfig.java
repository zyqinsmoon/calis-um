package com.cnebula.um.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.xml.MapEntry;

public class CertificateAuthConfig {
	
	public boolean enable = true;
	public boolean synToDatabase = false;
	
	@XMLMapping(tag="certAttrToUserFieldMap", childTag="mapping", keyTag="certAttr", valueTag="userField", itemTypes={MapEntry.class, String.class, String.class})
	public Map<String, String> certAttrToUserFieldMap = new LinkedHashMap<String, String>();
}
