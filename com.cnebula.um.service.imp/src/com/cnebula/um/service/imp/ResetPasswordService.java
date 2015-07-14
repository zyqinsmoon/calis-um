package com.cnebula.um.service.imp;

import java.util.Date;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.ejb.manage.EntityCRUDException;
import com.cnebula.common.i18n.I18nService;
import com.cnebula.common.log.ILog;
import com.cnebula.um.ejb.entity.usr.NewUserBean;
import com.cnebula.um.ejb.entity.usr.ResetPasswordBean;
import com.cnebula.um.usermanage.dao.INewUserDao;
import com.cnebula.um.usermanage.dao.IResetPasswordDao;
import com.cnebula.um.usermanage.util.IUserManageUtilService;

@EasyService(properties={@Property(name="id", value="UMUserManage")})
public class ResetPasswordService implements IResetPasswordService {

	protected @ESRef(target="(id=UMUserManage)") IResetPasswordDao resetDao;
	
	protected @ESRef(target="(id=UMUserManage)") INewUserDao newUserDao;
	
	protected @ESRef(target="(namespace=com.cnebula.um.service.resetPassword)")
			I18nService i18;
	
	protected @ESRef ILog log;
	
	protected @ESRef IUserManageUtilService userManageUtilService;
	
	public String resetPassword(String loginId, String email) {
		
		NewUserBean aUser = newUserDao.getByLoginId(loginId);
		String qLoginId = aUser.getLoginId();
		if(qLoginId == null){
			return i18.getFormattedString("$ResetPassword.noSuchUser",new String[]{loginId});
		}
		String qEmail = aUser.getEmail();
		if(qEmail==null || !qEmail.equals(email)){
			return i18.getFormattedString("$ResetPassword.emailAddrError",new String[]{loginId,email});
		}
		
		String activeCode = userManageUtilService.generateRandomCode(8);
		ResetPasswordBean resetPasswordBean = new ResetPasswordBean();
		resetPasswordBean.setActiveCode(activeCode);
		resetPasswordBean.setEmailaddr(email);
		resetPasswordBean.setResetDate(new Date());
		resetPasswordBean.setNewUserBean(aUser);
		String newPassword = userManageUtilService.generateRandomCode(6);
		resetPasswordBean.setNewPassword(newPassword);
		
		//发送密码重置激活邮件
		boolean isSendMailOk = userManageUtilService.sendMail(loginId, email, activeCode, "resetPassword", newPassword);
		
		if(!isSendMailOk){
			return i18.getString("$ResetPasswordMail.sendFailed");
		}else{
			try {
				resetDao.add(resetPasswordBean);
			} catch (EntityCRUDException e) {
				log.debug(e.getMessage());
			}
		}
		return "success";
	}

	public String activateNewPassword(String activeCode) {
		
		ResetPasswordBean resetPasswordBean = new ResetPasswordBean();
		resetPasswordBean = resetDao.getByActiveCode(activeCode);

		//已经激活
		if(resetPasswordBean.getEmailaddr() == null){
			return "already";
		}
		//过期了
		Date resetDate = resetPasswordBean.getResetDate();
		Date nowDate = new Date();
		
		int duration = userManageUtilService.getResetPSWDExpiredDuration();
		if((nowDate.getTime() - resetDate.getTime()) > duration*3600L*1000L){
			return "expired";
		}
		//修改密码
		NewUserBean theUser = resetPasswordBean.getNewUserBean();
		theUser.setPassword(resetPasswordBean.getNewPassword());
		try {
			newUserDao.update(theUser);
		} catch (EntityCRUDException e) {
			log.debug(e.getMessage());
			return "failed";
		}
		try {
			resetDao.deleteByEmail(resetPasswordBean.getEmailaddr());
		} catch (EntityCRUDException e) {
			log.debug(e.getMessage());
		}
		return "success";
	}
}