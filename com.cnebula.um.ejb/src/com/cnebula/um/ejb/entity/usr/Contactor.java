package com.cnebula.um.ejb.entity.usr;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.UUID;

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
import com.cnebula.um.ejb.saas.UMTenant;


@SuppressWarnings("serial")
@Entity
@Table(name = "UM_Contactor")
@EntityUI(
	i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="联系人"), @I18nLocale(lang="en_US", value="Contactor")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		},
	shortDescription="name" ,
	summaryPoints={"name","sex","position","email","phone","department"},
	searchPoints={"name","sex","position","email","phone","msgType","fax","department","officeAddr","mailAddr","postalCode",  "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version"},
	groups={
			@EntityUIPropertyGroup(name="basic", properties={"id","name","sex","position","department"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
			@EntityUIPropertyGroup(name="contact", properties={"email","phone","msgType","msgCode", "fax","officeAddr","mailAddr","postalCode"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="联系信息"), @I18nLocale(lang="en_US", value="Contact Infomation")})),
			@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
	}
	
)
public class Contactor implements Maintainable, Serializable, ISaasable {

	/****************
	 * 实体属性部分
	 ****************/
	/**
	 * 联系人ID
	 */
	private String id;// = IDUtil.getId(Contactor.class);
	/**
	 * 联系人姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String sex="m";
	/**
	 * 职务、职称
	 */
	private String position;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 网络消息传输工具类型：如QQ、MSN
	 */
	private String msgType;
	/**
	 * 网络消息传输工具号码
	 */
	private String msgCode;
	/**
	 * 传真号码
	 */
	private String fax;
	/**
	 * 所在部门名称
	 */
	private String department;
	/**
	 * 办公地址
	 */
	private String officeAddr;
	/**
	 * 通信地址
	 */
	private String mailAddr;
	/**
	 * 邮政编码
	 */
	private String postalCode;
	/** 
	 * 维护信息
	 * 含扩展属性
	 */
	
	protected int version;
	
	private MaintainInfo maintainInfo = new MaintainInfo();
	
	public Contactor(){
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
	
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="姓名"), @I18nLocale(lang="en_US", value="Name")})
		)
	@Column(nullable=false, length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(nullable=false, length=50)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="性别"), @I18nLocale(lang="en_US", value="Sex")}),
			plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://sex"), 
			@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Column(length=100)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="职务"), @I18nLocale(lang="en_US", value="Position")})
	)
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(length=200)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="电子邮箱"), @I18nLocale(lang="en_US", value="EMail")}),
			regex="([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})", regexHint="tom@sohu.com"
	)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(length=100)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="电话"), @I18nLocale(lang="en_US", value="Telephone")})
	)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(length=50)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="即时消息类型"), @I18nLocale(lang="en_US", value="Instant Messager Type")}),
			plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://imType"), 
			@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Column(length=100)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="即时消息帐号"), @I18nLocale(lang="en_US", value="Messager Code")})
	)
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	
	@Column(length=50)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="传真"), @I18nLocale(lang="en_US", value="Fax")})
	)
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Column(length=150)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="部门"), @I18nLocale(lang="en_US", value="Department")})
	)
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
	@Column(length=200)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="办公地点"), @I18nLocale(lang="en_US", value="Office Address")})
	)
	public String getOfficeAddr() {
		return officeAddr;
	}
	public void setOfficeAddr(String officeAddr) {
		this.officeAddr = officeAddr;
	}
	
	@Column(length=200)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="通讯地址"), @I18nLocale(lang="en_US", value="Mail Address")})
	)
	public String getMailAddr() {
		return mailAddr;
	}
	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}
	
	@Column(length=30)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="邮政编码"), @I18nLocale(lang="en_US", value="Postal Code")})
	)
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	@Embedded
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}
	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
	}
	
//	@ManyToOne
//	public AbstractOrg getAbsOrg() {
//		return absOrg;
//	}
//
//	public void setAbsOrg(AbstractOrg absOrg) {
//		this.absOrg = absOrg;
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
