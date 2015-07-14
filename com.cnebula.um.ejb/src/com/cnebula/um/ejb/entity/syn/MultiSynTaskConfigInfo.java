package com.cnebula.um.ejb.entity.syn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.ejb.manage.ExtendAttributeHelper;
import com.cnebula.common.ejb.manage.Extendable;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.ejb.manage.Maintainable;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.common.xml.MapEntry;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.saas.UMTenant;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.CascadeType.ALL;

@SuppressWarnings("serial")
@Entity
@Table(name = "um_syn_task")
public class MultiSynTaskConfigInfo implements ISaasable,Maintainable{

	/****************
	 * 实体属性部分
	 *   ENABLE            NUMBER(1) not null,
  PERIOD            NUMBER(10) not null,
  SUBFOLDER         VARCHAR2(255) not null,
  RUNNINGTIMERANGES VARCHAR2(255) not null,
  SYNTOCACHE        NUMBER(1) not null,
  NAME              VARCHAR2(255) not null,
  ORGCODE           VARCHAR2(255) not null,
  TYPE              NUMBER(1) not null,
  ID                VARCHAR2(255) not null
	 ****************/
	/**
	 * 用户ID
	 * 流水号
	 */
	String id;// = IDUtil.getId(Principle.class);
	
	
	/**
	 * 人物名
	 */
	String name;

	/**
	 * 运行周期
	 */
	int period=3600;
	
	/**
	 * 运行时间段
	 */
	String runningtimeranges="01:00-23:00";
	
	/**
	 * 是否同步到缓存
	 */
	boolean syntocache = true;
	/**
	 * 是否启用
	 */
	boolean enable = true;
	
	/**
	 * 任务类型，1，文本文件，2数据库
	 */
	int type=1;
	
	/**
	 * 任务所属机构
	 */
//	Organization org=null;
	
	public static final int PASS_OLD=1;
	public static final int PASS_IDCARD=2;
	public static final int PASS_EMAIL=3;
	public static final int PASS_NAMEANDBIRTHDAY=4;
	
	/**
	 * 密码方案：
	 * 1，原始密码
	 * 2，身份证后六位
	 * 3，电子邮件找回
	 * 4，姓名+生日
	 */
	int passwordType=1;
	
	/**
	 * 账户默认有效期，以月为单位。
	 */
	int defaultValidate=36;
	
	

	public MultiSynTaskConfigInfo(){
	}

    @Id
    @Column(name="id")
    @EntityUIProperty(
    	readOnly = true,
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="标识"), @I18nLocale(lang="en_US", value="ID")})
	)
	public String getId() {
		return getTenant().getTenantId()+":"+name;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Column(nullable=false, length=255)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="任务名称"), @I18nLocale(lang="en_US", value="Name")} )
		,maxlen=120,minlen=2
	)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable=false, length=255)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="运行时间间隔（秒）"), @I18nLocale(lang="en_US", value="Period")} )
		,maxlen=6,minlen=3,maxvalue="100000"
	)
	public int getPeriod() {
		return period;
	}


	public void setPeriod(int period) {
		this.period = period;
	}

	@Column(nullable=false, length=255)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="用户默认有效期（月）"), @I18nLocale(lang="en_US", value="DefaultValidate")} )
		,maxlen=3,minlen=1,maxvalue="999"
	)
	public int getDefaultValidate() {
		return defaultValidate;
	}

	public void setDefaultValidate(int defaultValidate) {
		this.defaultValidate = defaultValidate;
	}

	@Column(nullable=false, length=255)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="运行时间段（:）"), @I18nLocale(lang="en_US", value="Runningtimeranges")} )
		,maxlen=120,minlen=1,regex="[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9]"
	)
	public String getRunningtimeranges() {
		return runningtimeranges;
	}


	public void setRunningtimeranges(String runningtimeranges) {
		this.runningtimeranges = runningtimeranges;
	}

	@Column(nullable=false)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="同步到缓存"), @I18nLocale(lang="en_US", value="Syntocache")} )
	)
	public boolean isSyntocache() {
		return syntocache;
	}


	public void setSyntocache(boolean syntocache) {
		this.syntocache = syntocache;
	}

	@Column(nullable=false)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="是否启用"), @I18nLocale(lang="en_US", value="Enable")} )
	)
	public boolean isEnable() {
		return enable;
	}


	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Column(nullable=false,length=1)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="同步类型"), @I18nLocale(lang="en_US", value="Type")} )
	)
	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
	
//	@ManyToOne
//	@EntityUIProperty(
//			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="所属机构"), @I18nLocale(lang="en_US", value="Organization")}
//			)
//	,required=true)
//	public Organization getOrg() {
//		return org;
//	}
//
//	public void setOrg(Organization org) {
//		this.org = org;
//	}
	
	
	@EntityUIProperty(
			readOnly=false,
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="密码方案"), @I18nLocale(lang="en_US", value="passwordType")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.syn.MultiSynTaskConfigInfo.passwordType"), 
//					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	public int getPasswordType() {
		return passwordType;
	}

	public void setPasswordType(int passwordType) {
		this.passwordType = passwordType;
	}
	
	//针对SAAS改造增加的方法
	
	private UMTenant tenant ; 
	
	@EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="租客"), @I18nLocale(lang="en_US", value="Calis ID")})
    )
    @OneToOne(fetch=EAGER,targetEntity=UMTenant.class)
    @JoinColumn(name="TENANT_ID", referencedColumnName="id")
	public UMTenant getUMTenant() {
		return (UMTenant)tenant;
	}
	
	public void setUMTenant(UMTenant tenant) {
		this.tenant = tenant;
	}
	
	@Transient
	public ITenant getTenant() {
		return tenant;
	}

	public void setTenant(ITenant tenant) {
		this.tenant = (UMTenant)tenant ; 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enable ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
		result = prime * result + period;
		result = prime
				* result
				+ ((runningtimeranges == null) ? 0 : runningtimeranges
						.hashCode());
		result = prime * result + (syntocache ? 1231 : 1237);
		result = prime * result + type;
		return result;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultiSynTaskConfigInfo other = (MultiSynTaskConfigInfo) obj;
		if (enable != other.enable)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tenant == null) {
			if (other.tenant != null)
				return false;
		} else if (!tenant.getTenantId().equals(other.tenant.getTenantId()))
			return false;
		if (period != other.period)
			return false;
		if (runningtimeranges == null) {
			if (other.runningtimeranges != null)
				return false;
		} else if (!runningtimeranges.equals(other.runningtimeranges))
			return false;
		if (syntocache != other.syntocache)
			return false;
		if (type != other.type)
			return false;
		return true;
	}


	/**
	 * 版本
	 */
	protected int version;
	
	/**
	 * 维护信息
	 */
	protected MaintainInfo maintainInfo = new MaintainInfo();
	
	@Embedded
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}
	
	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
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

	

	
}
