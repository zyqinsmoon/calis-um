package com.cnebula.um.ejb.util;


import javax.persistence.PrePersist;

public class DefaultListener {
	
	public DefaultListener() {
	}

	@PrePersist
	public void beforePersist(Object ejb){
		try {
			if ( ejb.getClass().getMethod("getId").invoke(ejb) == null){
//				ejb.getClass().getMethod("setId", String.class).invoke(ejb, IDUtil.getId(ejb.getClass()));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} 
	}
	
}
