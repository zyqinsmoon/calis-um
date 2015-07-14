package com.cnebula.um.ejb.util;

import java.util.UUID;

public class IDUtil {

	public static String getId(Class<?> cls) {
		return UUID.randomUUID().toString() ;
	}

}
