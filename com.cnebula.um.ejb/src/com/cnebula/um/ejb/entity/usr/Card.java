package com.cnebula.um.ejb.entity.usr;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "UM_Card")
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="图书卡"), @I18nLocale(lang="en_US", value="Card")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		},
		shortDescription = "code", 
		summaryPoints = {"type", "code", "principle.name", "status", "validDate", "invalidDate", "maintainInfo.creator", "maintainInfo.lastModifyTime"},
		searchPoints ={"code", "type", "principle.name", "status", "validDate", "invalidDate",  "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version"},
		groups={
				@EntityUIPropertyGroup(name="basic", properties={"id", "type", "code", "status", "validDate", "invalidDate", "principle"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
				@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		}
)
public class Card implements Maintainable, Serializable, ISaasable {

	/****************
	 * 实体属性部分
	 ****************/
	/**
	 * 证卡标识
	 * 流水号
	 */
	protected String id;// = IDUtil.getId(Card.class);
	/**
	 * 证卡类型
	 * 
	 */
	protected int type ;
	/**
	 * 证卡编码
	 */
	protected String code;
	/**
	 * 证卡状态
	 * 无证卡（注销的证卡）、可用的证卡、停用的证卡
	 */
	protected int status = 2;
	
	protected BigDecimal cash=new BigDecimal(0);
	
	/**
	 * 证卡生效日期
	 */
	protected Date validDate = new Date();
	/**
	 * 证卡失效日期
	 */
	protected Date invalidDate = new Date(System.currentTimeMillis()+1000L*3600*24*365*10);
	/** 
	 * 维护信息
	 * 含扩展属性
	 */
	protected MaintainInfo maintainInfo = new MaintainInfo() ;
	
	/****************
	 * 实体关系部分
	 ****************/
	/**
	 * 所属用户
	 */
	protected UMPrincipal principle;
	

	protected int version;
	
	
	public Card(){
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
    	readOnly=true,
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="标识"), @I18nLocale(lang="en_US", value="ID")})
	)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * !11 A类读者(参考类借阅权限押金400元)；12 B类读者(普通类借阅权限押金100)；
!21 普通阅览证；31特许VIP证(同11但是不收押金);41公务证(同12但不收押金);
!51 馆内职工证；61馆内集体证;71活动奖励证
!81 少儿借阅证；13电子图书借阅权限 32人文之光
	 * @return
	 */
	
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="类型"), @I18nLocale(lang="en_US", value="Type")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.cardType"), 
//			@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	@Column
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="证号"), @I18nLocale(lang="en_US", value="Code")})
			)
	@Column(nullable=false, length=150, unique=true)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@EntityUIProperty(
			readOnly=true,
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="状态"), @I18nLocale(lang="en_US", value="Status")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.cardStatus"), 
			@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	@Embedded
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}
	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
	}
	
	@EntityUIProperty(
			readOnly=true,
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="持卡人"), @I18nLocale(lang="en_US", value="Owner")}),
			plugin=@EntityUIPropertyPlugin(type="ToOneDialogSelectPlugin")
			)
	@OneToOne
	@XMLIgnore
	public UMPrincipal getPrinciple() {
		return principle;
	}
	public void setPrinciple(UMPrincipal principle) {
		this.principle = principle;
	}

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
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="失效日期"), @I18nLocale(lang="en_US", value="Valid Until")})
			,formt="yyyy-MM-dd"
			,plugin=@EntityUIPropertyPlugin(name="IInputCalendarView")
			)
	public Date getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	@Column(precision=10, scale=2)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="押金"), @I18nLocale(lang="en_US", value="Cash")}),
			regex="\\d+(\\.\\d\\d)?", regexHint="0, 1.20"
	)
	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
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
		StringBuilder sb=new StringBuilder();
		String umid=this.getPrinciple()==null?"null":getPrinciple().getId();
		sb.append("[")
		.append("ID:"+this.getId()).append(",")
		.append("Status:"+this.getStatus()).append(",")
		.append("Code:"+this.getCode()).append(",")
		.append("UMPrincipal:"+umid).append(",")
		.append("Type:"+this.getType())
		.append("]");
		
		return sb.toString();
	}


	
}
