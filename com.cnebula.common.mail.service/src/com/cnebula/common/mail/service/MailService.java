package com.cnebula.common.mail.service;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.cnebula.common.mail.service.templateBean;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import com.cnebula.common.str.SimpleStringTemplate;
import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
@EasyService
public class MailService implements IMailService {
	@ESRef
	ITemplateService its;
//	 Session mailSession;
public boolean SendMail(String host, String user, String password, String to,
			String head, String tail, String content, String mid, String subject) {
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
	//		e1.printStackTrace();
			return false;
		}
		mailData md=new mailData();
		md.setContent(content);
		md.setHead(head);
		md.setTail(tail);
		String showType="text/html;charset=gb2312";
		String str=its.convert1(md, mid);
		showType=its.GetShowType();
	//	System.out.println(str);
		try{
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message);   
		System.out.println("OK");
		}catch(Exception e)
		{
	//		System.out.println(e);
			return false;
		}
		return true;
	}
//
	public boolean SendMail(String host, String user, String password, String to,
			String head, String tail, Object content, String mid, String subject) {
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
		//	e1.printStackTrace();
			return false;
		}
		mailData md=new mailData();
		md.setContent(content);
		md.setHead(head);
		md.setTail(tail);
		String showType="text/html;charset=gb2312";
		String str=its.convert1(md, mid);
		showType=its.GetShowType();
	//	System.out.println(str);
		try{
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message);   
		}catch(Exception e)
		{
		//	System.out.println(e);
			return false;
		}
		return true;
	}
//
	public boolean SendMail(String host, String user, String password, String[] to, String head, String tail, Object content,
			String mid, String subject) {
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
//			e1.printStackTrace();
			return false;
		}
		mailData md=new mailData();
		md.setContent(content);
		md.setHead(head);
		md.setTail(tail);
		String showType="text/html;charset=gb2312";
		String str=its.convert1(md, mid);
		showType=its.GetShowType();
	//	System.out.println(str);
		try{
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message);   
		}catch(Exception e)
		{
			//System.out.println(e);
			return false;
		}
		return true;

	}
//
	public boolean SendMail(String host, String user, String password, String[] to, String head, String tail, String content,
			String mid, String subject) {
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
//			e1.printStackTrace();
			return false;
		}
		mailData md=new mailData();
		md.setContent(content);
		md.setHead(head);
		md.setTail(tail);
		String showType="text/html;charset=gb2312";
		String str=its.convert1(md, mid);
		showType=its.GetShowType();
	//	System.out.println(str);
		try{
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message);   
		}catch(Exception e)
		{
//			System.out.println(e);
			return false;
		}
		return true;
	}
//	
//	
//	
	public MimeMessage setParam(String user,String password,String host,String to, String subject) throws Exception
	{
		final String user1=user;
		final String password1=password;
		MimeMessage message=null;

				Properties mailProps = new Properties(); 
				mailProps.put("mail.smtp.host", host); 
				mailProps.put("mail.smtp.auth", "true");
				Session mailSession = Session.getDefaultInstance(mailProps,   
		                new Authenticator() {   
		                    public PasswordAuthentication getPasswordAuthentication() {   
		                        return new PasswordAuthentication(user1,password1);   
		                    }   
		                });   

			    message = new MimeMessage(mailSession);
				message.setFrom(new InternetAddress(user));
				message.setRecipient(Message.RecipientType.TO,new InternetAddress(to)); 
				message.setSubject(subject);  
			//	System.out.println("OK");

			return message;
	}
//	
//	
//	
	public boolean SendMail1(String host, String user, String password, String to,
			String head, String tail, String content, String functionid, String subject) {
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
//			e1.printStackTrace();
			return false;
		}
		mailData md=new mailData();
		md.setContent(content);
		md.setHead(head);
		md.setTail(tail);
		String showType="text/html;charset=gb2312";
		
		String str=its.convert2(md, functionid);
		showType=its.GetShowType();
		try{
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message);   
		}catch(Exception e)
		{
//			System.out.println(e);
			return false;
		}
		return true;

	}
//	
//	
	public boolean SendMail1(String host, String user, String password, String to,
			String head, String tail, Object content, String functionid, String subject) {
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
//			e1.printStackTrace();
			return false;
		}
		mailData md=new mailData();
		md.setContent(content);
		md.setHead(head);
		md.setTail(tail);
		String showType="text/html;charset=gb2312";
		String str=its.convert2(md, functionid);
		showType=its.GetShowType();
		try{
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message);   
		}catch(Exception e)
		{
//			System.out.println(e);
			return false;
		}
		return true;
	}
//	
//	
	public boolean SendMail1(String host, String user, String password, String[] to,
			String head, String tail, String content, String functionid, String subject) {
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
//			e1.printStackTrace();
			return false;
		}
		mailData md=new mailData();
		md.setContent(content);
		md.setHead(head);
		md.setTail(tail);
		String showType="text/html;charset=gb2312";
		String str=its.convert2(md, functionid);
		showType=its.GetShowType();
		try{
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message);   
		}catch(Exception e)
		{
//			System.out.println(e);
			return false;
		}
		return true;
	}
//	
	public boolean SendMail1(String host, String user, String password, String[] to,
			String head, String tail, Object content, String functionid, String subject) {
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
//			e1.printStackTrace();
			return false;
		}
		mailData md=new mailData();
		md.setContent(content);
		md.setHead(head);
		md.setTail(tail);
		String showType="text/html;charset=gb2312";
		String str=its.convert2(md, functionid);
		showType=its.GetShowType();
		try{
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message);   
		}catch(Exception e)
		{
//			System.out.println(e);
			return false;
		}
		return true;
	}
	public boolean SendMail2(String host, String user, String password, String[] to, String head, String tail, String content, String modelString, String subject){
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
//			e1.printStackTrace();
			return false;
		}
		mailData md=new mailData();
		md.setContent(content);
		md.setHead(head);
		md.setTail(tail);
		String showType="text/html;charset=gb2312";
		
		
		EntityQuery query = null;
		SimpleStringTemplate aa=new SimpleStringTemplate();
		try{
			String str=aa.eval(md, modelString);
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message);   
		}catch(Exception e)
		{
//			System.out.println(e);
			return false;
		}
		return true;
	}
	public boolean SendMail2(String host, String user, String password, String[] to, String head, String tail, String content, String subject){
		MimeMessage message;
		try {
			message = setParam(user,password,host,to,subject);
		} catch (Exception e1) {
//			e1.printStackTrace();
			return false;
		}
		String showType="text/html;charset=gb2312";
		String str=head+content+tail;
		
		try{
		message.setContent(str, showType);
		message.saveChanges();
		Transport.send(message); 

		}catch(Exception e)
		{
//			System.out.println(e);
			return false;
		}
		return true;
	}
	
	
	
	public MimeMessage setParam(String user,String password,String host,String[] to, String subject) throws Exception{
		final String user1=user;
		final String password1=password;
		final int length=to.length;
		Address ia[]=new InternetAddress[length];
		MimeMessage message=null;

				Properties mailProps = new Properties(); 
				mailProps.put("mail.smtp.host", host); 
				mailProps.put("mail.smtp.auth", "true");
				Session mailSession = Session.getDefaultInstance(mailProps,   
		                new Authenticator() {   
		                    public PasswordAuthentication getPasswordAuthentication() {   
		                        return new PasswordAuthentication(user1,password1);   
		                    }   
		                });   

				message = new MimeMessage(mailSession);
				message.setFrom(new InternetAddress(user));
				
				for(int i=0;i<length;i++)
					ia[i]=new InternetAddress(to[i]);
				message.setRecipients(Message.RecipientType.TO, ia); 
				message.setSubject(subject);  

			 return message;
			}
	       
       
}
