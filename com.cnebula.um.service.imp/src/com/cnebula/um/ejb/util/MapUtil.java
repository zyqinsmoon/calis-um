package com.cnebula.um.ejb.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class MapUtil {
	
	public static byte[] convert(Map<String, String> map){
		if (map == null){
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oo;
		try {
			oo = new ObjectOutputStream(out);
			oo.writeObject(map);
			oo.flush();
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> convert(byte[] buffer){
		if (buffer == null){
			return null;
		}
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		
		try {
			ObjectInputStream oi = new ObjectInputStream(in);
			return (Map<String, String>) oi.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
