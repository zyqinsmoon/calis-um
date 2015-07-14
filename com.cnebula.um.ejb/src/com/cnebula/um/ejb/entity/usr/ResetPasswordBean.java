package com.cnebula.um.ejb.entity.usr;

import static javax.persistence.FetchType.EAGER;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.saas.UMTenant;

@Entity
@Table(name = "UM_Resetpasswd")
public class ResetPasswordBean implements Cloneable, ISaasable{

	private String id;
	
	private String newPassword;

	private String activeCode;
	
	private String emailaddr;
	
	private Date resetDate;

	private NewUserBean newUserBean;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Column(nullable = false, length = 40)
	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	
	@Column(nullable = false, length = 200)
	public String getEmailaddr() {
		return emailaddr;
	}

	public void setEmailaddr(String emailaddr) {
		this.emailaddr = emailaddr;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@EntityUIProperty(formt="yyyy-MM-dd HH:mm:ss",
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="重置密码时间"), @I18nLocale(lang="en_US", value="Reset Password Date")})
			)	
	public Date getResetDate() {
		return resetDate;
	}

	public void setResetDate(Date resetDate) {
		this.resetDate = resetDate;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	public NewUserBean getNewUserBean() {
		return newUserBean;
	}

	public void setNewUserBean(NewUserBean newUserBean) {
		this.newUserBean = newUserBean;
	}

	public ResetPasswordBean clone(){
		try {
			return (ResetPasswordBean) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	//针对SAAS改造增加的方法
	
	private UMTenant tenant ; 
	
	@EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="租客"), @I18nLocale(lang="en_US", value="Calis ID")})
    )
    @ManyToOne(fetch=EAGER,targetEntity=UMTenant.class)
    @JoinColumn(name="TENANT_ID", referencedColumnName="id")
    @XMLIgnore
	public UMTenant getUMTenant() {
		return (UMTenant)tenant;
	}
	
	public void setUMTenant(UMTenant tenant) {
		this.tenant = tenant;
	}
	@XMLIgnore
	@Transient
//	@XMLMapping(tagTypeMapping={"tenant=com.cnebula.um.ejb.saas.UMTenant"})
	public ITenant getTenant() {
		return tenant;
	}

	public void setTenant(ITenant tenant) {
		this.tenant = (UMTenant)tenant ; 
	}
}
