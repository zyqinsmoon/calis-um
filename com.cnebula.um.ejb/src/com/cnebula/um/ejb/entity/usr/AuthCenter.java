package com.cnebula.um.ejb.entity.usr;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.ejb.manage.Maintainable;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.common.security.auth.IAuthCenter;
import com.cnebula.um.ejb.saas.UMTenant;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_AuthCenter")
@EntityUI(
	shortDescription="name",
	i18ns={
			@I18n(items={@I18nLocale(lang="zh_CN", value="认证中心"), @I18nLocale(lang="en_US", value="Anthentication Center")})
			,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
			,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
	},
	searchPoints={"name","versionType", "isCurrent","rootURL", "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version"},
	summaryPoints={"name","versionType", "rootURL", "versionType",  "maintainInfo.creator", "maintainInfo.lastModifyTime"},
	groups={
			@EntityUIPropertyGroup(name="basic", properties={"id","name","versionType","isCurrent","rootURL"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")})),
			@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
	}
)
@Deprecated
public class AuthCenter implements Maintainable, Serializable , ISaasable{

	/****************
	 * 实体属性部分
	 ****************/
	/**
	 * 认证系统ID
	 * 主键，系统内唯一，不是oracle的流水号
	 */
	protected String id;
	/**
	 * 认证系统名称
	 */
	protected String name;
	/**
	 * 是否是当前认证系统
	 */
	protected boolean isCurrent = true;
	/**
	 * 认证系统类型
	 */
	
	
	/**
	 * 认证中心部署的root URL： 比如http://192.168.1.102:8080/
	 */
	protected String rootURL;
	
	protected String versionType;
	/**
	 * 维护信息
	 * 含扩展属性
	 */
	
	protected int version;
	
	protected MaintainInfo maintainInfo = new MaintainInfo();
	
	/****************
	 * 实体关系部分
	 ****************/
//	/**
//	 * 旗下的机构
//	 */
//	private List<Ora> abstOrgs = new ArrayList<AbstractOrg>();
	
	public AuthCenter(){
//		id = IDUtil.getId(this.getClass());
	}
	

	
	
	@EntityUIProperty(
			readOnly=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="版本"), @I18nLocale(lang="en_US", value="Version")})
		)
	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Id
	@GeneratedValue
	@Column(name="id")
	@EntityUIProperty(
    	readOnly = true,
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="标识"), @I18nLocale(lang="en_US", value="ID")})
	)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(nullable=false, length=100, unique=true)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="名称"), @I18nLocale(lang="en_US", value="Name")})
	)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(nullable=false)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="当前认证中心"), @I18nLocale(lang="en_US", value="Is Current")})
	)
	public boolean getIsCurrent() {
		return isCurrent;
	}
	public void setIsCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="版本类型"), @I18nLocale(lang="en_US", value="Type")})
	)
	@Column(nullable=false, length=100)
	public String getVersionType() {
		return versionType;
	}
	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}
	
	@Embedded
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}
	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
	}

	@EntityUIProperty(
			plugin=@EntityUIPropertyPlugin(properties={
					@Property(name="viewWidth", value="300", type=Property.Type.Integer)
					}),
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="服务根路径"), @I18nLocale(lang="en_US", value="Root URL")})
	)
	public String getRootURL() {
		return rootURL;
	}

	public void setRootURL(String rootURL) {
		this.rootURL = rootURL;
	}




	@Transient
	public String getAuthCenterURL() {
		String rt = rootURL;
		if(!rootURL.endsWith("/")){
			rt += "/" ; 
		}
		rt += "amconsole/AuthServices" ;
		return rt;
	}




	@Transient
	public String getTarget() {
		return "(id=UMValidLoginService)";
	}




	@Transient
	public String getLoginValidServiceURL() {
		String rt = "es:ws-rest-"+rootURL;
		if(!rootURL.endsWith("/")){
			rt += "/" ; 
		}
		rt += "easyservice/com.cnebula.common.security.auth.ILoginValidateService" ;
		return rt;
	}

	
//    @OneToMany(mappedBy="authCenter", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
//	public List<AbstractOrg> getAbstOrgs() {
//		return abstOrgs;
//	}
//	public void setAbstOrgs(List<AbstractOrg> abstOrgs) {
//		this.abstOrgs = abstOrgs;
//	}
//	public void addAbstractOrg(AbstractOrg org) {
//		if(!this.abstOrgs.contains(org)) {
//			this.abstOrgs.add(org);
//			org.setAuthCenter(this);
//		}
//	}
//	public void removeAbstractOrg(AbstractOrg org) {
//		org.setAuthCenter(null);
//		this.abstOrgs.remove(org);
//	}

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
