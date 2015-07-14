package com.cnebula.um.ejb.entity.usr;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;

@Entity
@Table(name = "UM_Principle")
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="用户"), @I18nLocale(lang="en_US", value="Principle")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		}
		,shortDescription="name"
		,searchPoints={"name", "sex", "userType", "birthday", "organization.name", "organization.code", "organization.parent.name","position", "institute", "loginId",  "status", "checkOrgIp", "card.code", "card.type","card.status",  "card.validDate", "card.invalidDate", "validDate", "invalidDate", "additionalIds.id", "additionalIds.type", "additionalIds.code","additionalIds.status", "otherPropertity1", "otherPropertity2", "otherPropertity3", "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version"}
		,summaryPoints={"name", "birthday", "userType", "sex", "organization.name", "status", "position", "email"}
		,groups={
			@EntityUIPropertyGroup(name="basic", properties={"id", "name", "sex", "userType", "userGroup","photo", "birthday", "organization", "position", "institute","background","directlyUser","collegeName","departmentName","professionalName"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")})),
			@EntityUIPropertyGroup(name="login", properties={"loginId","localLoginId", "password",  "status", "checkOrgIp", "card",  "maxConcurrentNumber","validDate", "invalidDate", "personality"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="登录信息"), @I18nLocale(lang="en_US", value="Login Information")})),
			@EntityUIPropertyGroup(name="contact", properties={"email","phone","msgType","msgCode", "fax","postalCode", "homeAddress", "officeAddr","mailAddr"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="联系信息"), @I18nLocale(lang="en_US", value="Contact Infomation")})),
			@EntityUIPropertyGroup(name="cash", properties={"totalCashValue","cashs"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="押金信息"), @I18nLocale(lang="en_US", value="Cash Infomation")})),
			@EntityUIPropertyGroup(name="aditionalId", properties={"additionalIds"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="附加ID"), @I18nLocale(lang="en_US", value="Additional ID")})),
			@EntityUIPropertyGroup(name="historyCards", properties={"historyCards"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="历史卡信息"), @I18nLocale(lang="en_US", value="History Card Infomation")})),
			@EntityUIPropertyGroup(name="extend", properties={ "otherPropertity1", "otherPropertity2", "otherPropertity3"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="扩展信息"), @I18nLocale(lang="en_US", value="Extend")})),
			@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		},
		plugin=@EntityUIPropertyPlugin(type="UserEntityPlugin")
)
@DiscriminatorValue(value="NewUserBean")
@XMLMapping(tag="UMPrincipal")
public class NewUserBean extends UMPrincipal implements Cloneable{
	
	private static final long serialVersionUID = 8688006628488190547L;
	
	/**
	 * 激活码
	 */
	private String activeCode;
	
	/**
	 * 是否激活
	 */
	private int active;
	
	/**
	 * 注册时间
	 */
	private Date registerDate;

	@Column(nullable=false,length=40)
    @EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="激活码"), @I18nLocale(lang="en_US", value="ActiveCode")} )
		,maxlen=39,minlen=4
	)
	@XMLIgnore
    public String getActiveCode() {
		return activeCode;
	}
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	
	@Column(nullable=false)
		@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="激活标志位"), @I18nLocale(lang="en_US", value="IsActivated")})
			)
	@XMLIgnore
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	
	@Temporal(TemporalType.DATE)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="注册日期"), @I18nLocale(lang="en_US", value="Register Date")})
			,formt="yyyy-MM-dd"
			)
    @XMLIgnore			
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	public NewUserBean clone(){
		return (NewUserBean) super.clone();
	}
	
}
