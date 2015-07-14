package com.cnebula.common.mail.config;


import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.conf.IEasyServiceConfAdmin;

@EasyService
public class MailConfigService implements IMailConfigService{
	@ESRef
	IEasyServiceConfAdmin confAdmin;
	MailConfig mailConfig = null;

	 public MailConfig getMailConfig(){
		 if(mailConfig == null)mailConfig = confAdmin.get("mailConfig", MailConfig.class);
		 return mailConfig;
	 }
}
