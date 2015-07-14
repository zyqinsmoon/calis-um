package com.cnebula.um.aas;

public interface IIPAndRoleIndexer {
	
	public void rebuildIpIndex();
	
	public void rebuildRoleIndex(String tenantId);

}
