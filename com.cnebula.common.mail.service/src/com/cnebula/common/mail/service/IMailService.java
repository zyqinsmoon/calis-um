package com.cnebula.common.mail.service;

public interface IMailService {

	public boolean SendMail(String host, String user, String password, String to, String head, String tail, String content,String mid, String subject);
	public boolean SendMail(String host, String user, String password, String to, String head, String tail, Object content, String mid, String subject);
	public boolean SendMail(String host, String user, String password, String[] to, String head, String tail, Object content, String mid, String subject);
	public boolean SendMail(String host, String user, String password, String[] to, String head, String tail, String content, String mid, String subject);
	public boolean SendMail1(String host, String user, String password, String to, String head, String tail, String content,String functionid, String subject);
	public boolean SendMail1(String host, String user, String password, String to, String head, String tail, Object content, String functionid, String subject);
	public boolean SendMail1(String host, String user, String password, String[] to, String head, String tail, Object content, String functionid, String subject);
	public boolean SendMail1(String host, String user, String password, String[] to, String head, String tail, String content, String functionid, String subject);
	public boolean SendMail2(String host, String user, String password, String[] to, String head, String tail, String content, String modelString, String subject);
	public boolean SendMail2(String host, String user, String password, String[] to, String head, String tail, String content, String subject);
	
}
