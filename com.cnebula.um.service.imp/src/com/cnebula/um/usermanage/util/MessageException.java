package com.cnebula.um.usermanage.util;

import com.cnebula.common.SuperException;

public class MessageException extends SuperException{

	private static final long serialVersionUID = -5610884791901886461L;
	
	private String message;
	
	public MessageException(){
		super();
	}
	
	public MessageException(String str){
		message = str;
	}
	
	public String toString(){
		return  message;
	}
	
	public String getMessage(){
		return message;
	}
}
