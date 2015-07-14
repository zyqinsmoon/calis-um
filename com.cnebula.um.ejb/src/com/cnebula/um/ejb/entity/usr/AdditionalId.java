package com.cnebula.um.ejb.entity.usr;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.saas.UMTenant;

@Entity(
		name="UM_AdditionalId"
			)
@EntityUI(
	shortDescription="code",
	i18ns={@I18n(items={@I18nLocale(lang="zh_CN", value="附加标识"), @I18nLocale(lang="en_US", value="Additional ID")})},
	searchPoints={"code","type", "status","owner.name"},
	summaryPoints={"code","type", "status","owner.name"},
	groups={
			@EntityUIPropertyGroup(name="basic", properties={"code", "password", "type", "owner", "status", "updateType",  "loginType", "validDate", "invalidDate"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")})),
//			@EntityUIPropertyGroup(name="delegateAuth", properties={"name", "action", "user", "password", "encode", "baseURL", "requestString", "prepareCode"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="代理认证"), @I18nLocale(lang="en_US", value="Basic Information")})),
	}
)

public class AdditionalId implements Serializable, ISaasable{
	
	
	
	protected UMPrincipal owner;
	protected String id;
	protected String code;
	protected String type = "";
	
	/**
	 * <singleNamespace
		namespace="com.cnebula.um.ejb.entity.usr.idCardUpdateType">
		<item key="0" defaultLang="zh_CN">
			<locale value="Hidden(0)" lang="en_US" />
			<locale value="不可见(0)" lang="zh_CN" />
		</item>
		<item key="1" defaultLang="zh_CN">
			<locale value="Read Only(1)" lang="en_US" />
			<locale value="只读(1)" lang="zh_CN" />
		</item>
		<item key="2" defaultLang="zh_CN">
			<locale value="Only Password Updatable(2)" lang="en_US" />
			<locale value="仅密码可更改(2)" lang="zh_CN" />
		</item>
		<item key="3" defaultLang="zh_CN">
			<locale value="Only Id Updatable(3)" lang="en_US" />
			<locale value="仅条码可更改(3)" lang="zh_CN" />
		</item>
		<item key="4" defaultLang="zh_CN">
			<locale value="Updatable(4)" lang="en_US" />
			<locale value="可更改(4)" lang="zh_CN" />
		</item>
	 */
	protected int updateType=0;
	
	/**
	 * <item key="0" defaultLang="zh_CN">
			<locale value="Login Desabled(0)" lang="en_US" />
			<locale value="不可登录(0)" lang="zh_CN" />
		</item>
		<item key="1" defaultLang="zh_CN">
			<locale value="Login by ID(1)" lang="en_US" />
			<locale value="可仅凭条码登录(1)" lang="zh_CN" />
		</item>
		<item key="2" defaultLang="zh_CN">
			<locale value="Login by ID and Password(2)" lang="en_US" />
			<locale value="凭条码和密码登录(2)" lang="zh_CN" />
		</item>
	 */
	protected int loginType;
	protected int status;
	protected String password;
	
	public final static int LOGINTYPE_DESABLE = 0;
	public final static int LOGINTYPE_ID = 1;
	public final static int LOGINTYPE_ID_PASSWORD = 2;
	
	
	/**
	 * ID生效日期
	 */
	protected Date validDate = new Date();;
	/**
	 * ID失效日期
	 */
	protected Date invalidDate = new Date(System.currentTimeMillis()+1000L*3600*24*365*10);
	
	
	@Temporal(TemporalType.DATE)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="生效日期"), @I18nLocale(lang="en_US", value="Valid From")})
			,formt="yyyy-MM-dd"
			,plugin=@EntityUIPropertyPlugin(name="IInputCalendarView")
			)
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	
	@Temporal(TemporalType.DATE)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="失效日期"), @I18nLocale(lang="en_US", value="Valid From")})
			,formt="yyyy-MM-dd"
			,plugin=@EntityUIPropertyPlugin(name="IInputCalendarView")
			)
	public Date getInvalidDate() {
		return invalidDate;
	}
	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}
	@ManyToOne
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="用户"), @I18nLocale(lang="en_US", value="User")})
	)
	@XMLIgnore
	public UMPrincipal getOwner() {
		return owner;
	}
	public void setOwner(UMPrincipal owner) {
		this.owner = owner;
	}
	
	@Id
	@GeneratedValue
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
	
	@Column(unique=true)
	@EntityUIProperty(
			required=true,
			minlen=2,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="条码"), @I18nLocale(lang="en_US", value="Code")})
		)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable=false)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="类型"), @I18nLocale(lang="en_US", value="Type")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.idCardType"), 
//			@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			})
			)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="状态"), @I18nLocale(lang="en_US", value="Status")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.idCardStatus")}
	))			
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Transient
	public boolean isValid() {
		long cur = System.currentTimeMillis();
		if(validDate == null || invalidDate == null)
			return false;
		return validDate.getTime() <= cur && invalidDate.getTime()> cur;
	}
	
	public void setValid(boolean valid){
	}
	
	@Column(length=100)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="密码"), @I18nLocale(lang="en_US", value="Password")} )
		,minlen=6,maxlen=32,
		plugin=@EntityUIPropertyPlugin(properties={@Property(name="$view.echoChar", value="*", type=Property.Type.Char)})
	)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(nullable=false)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="更改类别"), @I18nLocale(lang="en_US", value="Update Type")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.idCardUpdateType"), 
			@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			})
			)
	@XMLIgnore
	public int getUpdateType() {
		return updateType;
	}
	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}
	
	@Column(nullable=false)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="登录类别"), @I18nLocale(lang="en_US", value="Update Type")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.idCardLoginType"), 
			@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			})
			)
	public int getLoginType() {
		return loginType;
	}
	public void setLoginType(int loginType) {
		this.loginType = loginType;
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
	public String toString() {
		String ownerid=this.getOwner()==null?"null":getOwner().getId();
		StringBuilder sb=new StringBuilder();
		sb.append("[")
		.append("ID:"+this.getId()).append(",")
		.append("Status:"+this.getStatus()).append(",")
		.append("Code:"+this.getCode()).append(",")
		.append("Owner:"+ownerid).append(",")
		.append("Type:"+this.getType())
		.append("]");
		
		return sb.toString();
	}
	
}
