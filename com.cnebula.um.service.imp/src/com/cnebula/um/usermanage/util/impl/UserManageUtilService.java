package com.cnebula.um.usermanage.util.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.osgi.service.component.ComponentContext;
import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.conf.IEasyServiceConfAdmin;
import com.cnebula.common.ejb.manage.saas.SAASConfig;
import com.cnebula.common.es.IEasyServiceManager;
import com.cnebula.common.i18n.I18nService;
import com.cnebula.common.log.ILog;
import com.cnebula.common.mail.config.IMailConfigService;
import com.cnebula.common.mail.config.MailConfig;
import com.cnebula.common.mail.service.IMailService;
import com.cnebula.common.security.auth.IEasyCaptchaService;
import com.cnebula.common.server.http.HttpServerConfig;
import com.cnebula.um.service.UserManageConfig;
import com.cnebula.um.service.UserManageServiceConfig;
import com.cnebula.um.usermanage.util.IUserManageUtilService;


@EasyService
public class UserManageUtilService implements IUserManageUtilService {
	
	private @ESRef IEasyServiceConfAdmin confAdmin;
	
	private @ESRef IEasyServiceManager easyServiceManager ; 
	
	private @ESRef IMailConfigService mailConfigService;
	
	private @ESRef IMailService mailService;
	
	private @ESRef ILog log;
	
	private @ESRef(target="(namespace=com.cnebula.um.service.userActiveMail)")
			I18nService i18RegisterMail;
	
	private @ESRef(target="(namespace=com.cnebula.um.service.newPasswordActiveMail)")
			I18nService i18ResetPassWdMail;
	
	private Random random = new Random();
	
	private String password = "";
	
	private SAASConfig saasConfig = null;
	
	private UserManageConfig userManageConfig = null;
	
	private Map<String,String> userManageServiceConfig = new HashMap<String,String>();
	
	private char ch[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'0', '1' };

	protected void activate(ComponentContext ctx){
		saasConfig = confAdmin.get("SAASConfig", SAASConfig.class);
		userManageConfig = confAdmin.get("UserManageConfig", UserManageConfig.class);
		
		if(saasConfig == null)
			saasConfig = new SAASConfig();
		if(userManageConfig == null){
			userManageConfig = new UserManageConfig();
		}
		List<UserManageServiceConfig> srvs = userManageConfig.getServices();
		for(UserManageServiceConfig umsc: srvs){
			userManageServiceConfig.put(umsc.getName(), umsc.getTargetId());
		}
	}
	
	public String generateRandomCode(int length) {
		if (length > 0) {
			char[] result = new char[length];
			int index = 0;
			int rand = random.nextInt();
			for (int i = 0; i < length % 5; i++) {
				result[index++] = ch[(byte) rand & 63];
				rand >>= 6;
			}
			for (int i = length / 5; i > 0; i--) {
				rand = random.nextInt();
				for (int j = 0; j < 5; j++) {
					result[index++] = ch[(byte) rand & 63];
					rand >>= 6;
				}
			}
			password = new String(result, 0, length);
			return password;
		} else if (length == 0) {
			return "";
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 获取注册类型
	 */
	public String getRegisterConfig() {
		return userManageConfig.getRegisterType();
	}
	
	public String getHttpServerPort(){
		return String.valueOf(confAdmin.get("httpServer", HttpServerConfig.class).getPort());
	}
	
	public byte[] getCaptchaPicture() throws IOException {
		IEasyCaptchaService easyCaptchaService = 
			(IEasyCaptchaService) easyServiceManager.getService(this.getClass(), null, IEasyCaptchaService.class.getName(), null);
		return easyCaptchaService.getImage();
	}

	/**
	 * 获取重置密码的失效时间
	 */
	public int getResetPSWDExpiredDuration() {
		return userManageConfig.getRestPasswordExpiredDuration();
	}	
	
	public boolean isEnableSAAS(){
		return saasConfig.isEnableSAAS();
	}
	
	public boolean sendMail(String loginId,String emailAddr,String activeCode, String mailType ,String newPassword) {
		
		//发送邮件所需参数
		MailConfig mailConfig = mailConfigService.getMailConfig();
		final String mailHost = mailConfig.getMailHost();
		final String mailSender = mailConfig.getMailSender();
		final String mailPassword = mailConfig.getMailPassword();
		
		//邮件发送时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sendMailDate = sdf.format(new Date()).toString();
		
		//服务器IP地址
		InetAddress localAddress;
		try {
			localAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			log.warn(e.getMessage());
			return false;
		}
		
		String mailHeader = null;
		String mail = null;
		String mailTitle = null;
		String domain = userManageConfig.getDomain();
	//需激活的注册服务
		if (mailType.equals("register")) {
			//激活链接的相对路径
			String activeURI = userManageConfig.getActiveNewUserURI();
			
			//激活地址
			StringBuilder activeURL = new StringBuilder();
			if(domain != null && !"".equals(domain) && !"null".equals(domain)){
				activeURL.append("http://").append(domain).append(activeURI).append(activeCode);
			}else{
				//fail back to local ip address
				activeURL.append("http://").append(localAddress.getHostAddress())
					 	 .append(":").append(this.getHttpServerPort())
					 	 .append(activeURI).append(activeCode);
			}
			//邮件国际化后的各个部分
			mailHeader = i18RegisterMail.getFormattedString("$Mail.head", new String[] { loginId });
			String mailSubject = i18RegisterMail.getString("$Mail.subject");
			String mailAlert = i18RegisterMail.getString("$Mail.alert");
			String mailLinkName = i18RegisterMail.getFormattedString("$Mail.linkName",new String[]{activeURL.toString()});
			String mailTip = i18RegisterMail.getFormattedString("$Mail.tip",new String[] { activeURL.toString() });
			String mailFooter = i18RegisterMail.getFormattedString("$Mail.footer", new String[] { sendMailDate });
			//拼装邮件主体
			StringBuilder mailBuilder = new StringBuilder(); 
			mailBuilder.append(mailHeader).append(mailSubject).append(mailAlert).append(mailLinkName)
							.append(mailTip).append(mailFooter);
			//邮件内容	
			mail = mailBuilder.toString();
			//邮件标题
			mailTitle = i18RegisterMail.getString("$Mail.title");
		}
	//密码重置必须激活
		else if(mailType.equals("resetPassword")){
			//激活链接的相对路径
			String activeURI = userManageConfig.getActiveResetPasswordURI();
			//激活地址
			StringBuilder activeURL = new StringBuilder();
			if(domain != null && !"".equals(domain) && !"null".equals(domain)){
				activeURL.append("http://").append(domain).append(activeURI).append(activeCode);
			}else{
				//fail back to local ip address
				activeURL.append("http://").append(localAddress.getHostAddress())
					 	 .append(":").append(this.getHttpServerPort())
					 	 .append(activeURI).append(activeCode);
			}
			//邮件国际化后的各个部分
			mailHeader = i18ResetPassWdMail.getFormattedString("$Mail.head", new String[] { loginId });
			String mailSubject = i18ResetPassWdMail.getFormattedString("$Mail.subject",new String[] { newPassword });
			String mailAlert = i18ResetPassWdMail.getString("$Mail.alert");
			String mailLinkName = i18ResetPassWdMail.getFormattedString("$Mail.linkName", new String[]{ activeURL.toString()});
			String mailTip = i18ResetPassWdMail.getFormattedString("$Mail.tip",new String[] { activeURL.toString() });
			String mailFooter = i18ResetPassWdMail.getFormattedString("$Mail.footer", new String[] { sendMailDate });
			//拼装邮件主体
			StringBuilder mailBuilder = new StringBuilder(); 
			mailBuilder.append(mailHeader).append(mailSubject).append(mailAlert).append(mailLinkName)
					   .append(mailTip).append(mailFooter);
			//邮件内容
			mail = mailBuilder.toString();
			//邮件标题
			mailTitle = i18ResetPassWdMail.getString("$Mail.title");
		}else{
			return false;
		}
		
		final String[] to;
		to = new String[1];
		to[0] = emailAddr;
		try {
			mailService.SendMail2(mailHost, mailSender, mailPassword, to, "", "", mail, mailTitle);
		} catch (Exception e) {
			log.debug("邮件发送失败");
			return false;
		}
		return true;
	}

	public Map<String, String> getUserManageServcesConfig() {
		return userManageServiceConfig;
	}
}
