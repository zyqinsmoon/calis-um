package com.cnebula.um.ejb.entity.perm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.entity.env.IPRange;
import com.cnebula.um.ejb.entity.usr.AuthCenter;
import com.cnebula.um.ejb.entity.usr.Contactor;
import com.cnebula.um.ejb.entity.usr.Personality;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

@SuppressWarnings("serial")
@Entity 
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="应用系统"), @I18nLocale(lang="en_US", value="Application System")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		},
		shortDescription = "name", 
		summaryPoints = {"name", "shortName", "sysType", "type.name", "vender", "authCenter.name"},
		searchPoints ={"name", "shortName", "sysType", "type", "vender", "authCenter.name", "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version"},
		groups={
				@EntityUIPropertyGroup(name="basic", properties={"id", "name", "shortName","sysType", "type", "authId", "url", "vender"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
				@EntityUIPropertyGroup(name="roles", properties={"roles"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="角色列表"), @I18nLocale(lang="en_US", value="Roles")})),
				@EntityUIPropertyGroup(name="contacts", properties={"contactors"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="联系人"), @I18nLocale(lang="en_US", value="Contacts")})),
				@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		}
)
@DiscriminatorValue("UM_Application")
public class Application  extends GeneralResource implements Serializable {

	/****************
	 * 实体属性部分
	 ****************/
//	/**
//	 * 应用系统ID
//	 */
//	String id = IDUtil.getId(A.class);;;
	/**
	 * 应用系统名称
	 */
//	String name;
	/**
	 * 应用系统简称
	 */
	String shortName;
	
	String authId;
	
	/**
	 * 应用系统类型
	 */
	String sysType;
	/**
	 * 是否中心系统
	 */
	boolean isCenter = false;
	/**
	 * 部署实例序号
	 * 序号从0开始
	 */
	/**
	 * 应用系统网络访问地址
	 */
	String url;

	
	/****************
	 * 实体关系部分
	 ****************/
	/**
	 * 所属资源商
	 */
	String vender;

	/**
	 * 所属认证中心
	 * 
	 */
	AuthCenter authCenter;
	
	/**
	 * 联系人
	 */
	List<Contactor> contactors = new ArrayList<Contactor>();
	
	
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="简称"), @I18nLocale(lang="en_US", value="Short Name")})
	)
	public String getShortName() {
		return shortName;
	}


	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="系统型号"), @I18nLocale(lang="en_US", value="System Type")})
	)
	public String getSysType() {
		return sysType;
	}


	public void setSysType(String sysType) {
		this.sysType = sysType;
	}


	public Application(){
	}
	
	

	
	
	@Column
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="中心系统"), @I18nLocale(lang="en_US", value="Center System")})
	)
	public boolean getIsCenter() {
		return isCenter;
	}
	public void setIsCenter(boolean isCenter) {
		this.isCenter = isCenter;
	}
	
	
	@Column(length=300)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="服务地址"), @I18nLocale(lang="en_US", value="Service URL")})
	)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="资源商"), @I18nLocale(lang="en_US", value="Vender")})
	)
	public String getVender() {
		return vender;
	}
	public void setVender(String vender) {
		this.vender = vender;
	}

	@ManyToOne
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="认证中心"), @I18nLocale(lang="en_US", value="Authentication Center")})
	)
	public AuthCenter getAuthCenter() {
		return authCenter;
	}

	public void setAuthCenter(AuthCenter authCenter) {
		this.authCenter = authCenter;
	}

	@OneToMany(cascade={ALL}, fetch = EAGER)
	@JoinTable(name="UM_App_Contactor")
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="联系人"), @I18nLocale(lang="en_US", value="Contact")})
	)
	public List<Contactor> getContactors() {
		return contactors;
	}

	public void setContactors(List<Contactor> contactors) {
		this.contactors = contactors;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="统一认证分配Id"), @I18nLocale(lang="en_US", value="Auth Id")})
	)
	public String getAuthId() {
		return authId;
	}


	public void setAuthId(String authId) {
		this.authId = authId;
	}
	
	List<LegacyRole> roles = new ArrayList<LegacyRole>();


	 @EntityUIProperty(
//			 plugin=@EntityUIPropertyPlugin(type="EmbedToManyPlugin"),
				i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="角色列表"), @I18nLocale(lang="en_US", value="RoleList")})
	)
	@OneToMany(fetch=EAGER,mappedBy="application",cascade=CascadeType.ALL)
	public List<LegacyRole> getRoles() {
		return roles;
	}

	public void setRoles(List<LegacyRole> roles) {
		this.roles = roles;
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("[")
		.append("ID:"+this.getId()).append(",")
		.append("AuthId:"+this.getAuthId()).append(",")
		.append("SysType:"+this.getSysType()).append(",")
		.append("Name:"+this.getName())
		.append("]");
		
		return sb.toString();
	}

	
}
