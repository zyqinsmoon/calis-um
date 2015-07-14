package com.cnebula.common.mail.service;

public interface ITemplateService {
	public String convert(Object ob, String modelString);
	public String convert1(Object ob, String mid);
	public String convert2(Object ob, String functionid);
	public String GetShowType();
	public String GetShowType1();
}
