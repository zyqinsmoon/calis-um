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
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.xml.MapEntry;
import com.cnebula.um.ejb.entity.usr.Organization;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.CascadeType.ALL;


@Entity
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="文件数据源同步任务"), @I18nLocale(lang="en_US", value="synTask")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		}
		,shortDescription="name"
		,searchPoints={"name", "period", "runningtimeranges","passwordType", "subfolder"}
		,summaryPoints={"name", "period", "runningtimeranges","passwordType", "subfolder"}
		,groups={
			@EntityUIPropertyGroup(name="basic", properties={"name", "period", "runningtimeranges","defaultValidate","passwordType","syntocache", "enable", "subfolder"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")})),
		}
		

)
@DiscriminatorValue(value="txt")
public class MultiSynTaskTxtConfigInfo extends MultiSynTaskConfigInfo{

	/****************
	 * 实体属性部分
	 *   ENABLE            NUMBER(1) not null,
  PERIOD            NUMBER(10) not null,
  SUBFOLDER         VARCHAR2(255) not null,
  RUNNINGTIMERANGES VARCHAR2(255) not null,
  SYNTOCACHE        NUMBER(1) not null,
  NAME              VARCHAR2(255) not null,
  TYPE              NUMBER(1) not null,
  ID                VARCHAR2(255) not null
	 ****************/
	String subfolder=null;


	public MultiSynTaskTxtConfigInfo(){
	}

    @Id
    @Column(name="id")
    @EntityUIProperty(
    	readOnly = true,
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="标识"), @I18nLocale(lang="en_US", value="ID")})
	)
	public String getId() {
		return super.getId();
	}


	public void setId(String id) {
		super.setId(id);
	}

	@Column(nullable=false, length=255)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="任务名称"), @I18nLocale(lang="en_US", value="Name")} )
		,maxlen=120,minlen=2
	)
	public String getName() {
		return super.getName();
	}


	public void setName(String name) {
		super.setName(name);
	}

	@Column(nullable=false, length=255)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="运行时间间隔（秒）"), @I18nLocale(lang="en_US", value="Period")} )
		,maxlen=6,minlen=3,maxvalue="100000"
	)
	public int getPeriod() {
		return super.getPeriod();
	}


	public void setPeriod(int period) {
		super.setPeriod(period);
	}

	@Column(nullable=false, length=255)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="用户默认有效期（月）"), @I18nLocale(lang="en_US", value="DefaultValidate")} )
		,maxlen=3,minlen=1,maxvalue="999"
	)
	public int getDefaultValidate() {
		return super.getDefaultValidate();
	}

	public void setDefaultValidate(int defaultValidate) {
		super.setDefaultValidate(defaultValidate);
	}

	@Column(nullable=false, length=255)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="运行时间段（:）"), @I18nLocale(lang="en_US", value="Runningtimeranges")} )
		,maxlen=120,minlen=1,regex="[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9]"
	)
	public String getRunningtimeranges() {
		return super.getRunningtimeranges();
	}


	public void setRunningtimeranges(String runningtimeranges) {
		super.setRunningtimeranges(runningtimeranges);
	}

	@Column(nullable=false)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="同步到缓存"), @I18nLocale(lang="en_US", value="Syntocache")} )
	)
	public boolean isSyntocache() {
		return super.isSyntocache();
	}


	public void setSyntocache(boolean syntocache) {
		super.setSyntocache(syntocache);
	}

	@Column(nullable=false)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="是否启用"), @I18nLocale(lang="en_US", value="Enable")} )
	)
	public boolean isEnable() {
		return super.isEnable();
	}


	public void setEnable(boolean enable) {
		super.setEnable(enable);
	}

	@Column(nullable=false,length=1)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="同步类型"), @I18nLocale(lang="en_US", value="Type")} )
	)
	public int getType() {
		return super.getType();
	}


	public void setType(int type) {
		super.setType(type);
	}
	
//	@ManyToOne
//	@EntityUIProperty(
//			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="所属机构"), @I18nLocale(lang="en_US", value="Organization")}
//			)
//	,required=true)
//	public Organization getOrg() {
//		return super.getOrg();
//	}
//
//	public void setOrg(Organization org) {
//		super.setOrg(org);
//	}
//	
	
	@EntityUIProperty(
			readOnly=false,
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="密码方案"), @I18nLocale(lang="en_US", value="passwordType")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.syn.MultiSynTaskConfigInfo.passwordType"), 
//					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	public int getPasswordType() {
		return super.getPasswordType();
	}

	public void setPasswordType(int passwordType) {
		super.setPasswordType(passwordType);
	}	
	

	@Column(nullable=false,length=255)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="同步子文件夹"), @I18nLocale(lang="en_US", value="Subfolder")} )
	)
	public String getSubfolder() {
		return subfolder;
	}


	public void setSubfolder(String subfolder) {
		this.subfolder = subfolder;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((subfolder == null) ? 0 : subfolder.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultiSynTaskTxtConfigInfo other = (MultiSynTaskTxtConfigInfo) obj;
		if (subfolder == null) {
			if (other.subfolder != null)
				return false;
		} else if (!subfolder.equals(other.subfolder))
			return false;
		return true;
	}



}
