package com.cnebula.um.service;

import com.cnebula.common.annotations.xml.FieldStyleType;
import com.cnebula.common.annotations.xml.XMLMapping;

@XMLMapping(fieldStyle=FieldStyleType.ATTR)
public class UserManageServiceConfig {
	
	String name = "";
	
	String targetId = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
}
