package com.cnebula.um.ejb.entity.usr;

import static javax.persistence.FetchType.EAGER;

import java.math.BigDecimal;

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
@Table(name = "UM_Cash")
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="押金"), @I18nLocale(lang="en_US", value="Cash")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		},
		shortDescription = "id", 
		summaryPoints = {"type", "value", "principal.name", "receiptNumber", "status", "maintainInfo.creator", "maintainInfo.lastModifyTime"},
		searchPoints ={"type", "principal.name", "receiptNumber", "status", "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version"},
		groups={
				@EntityUIPropertyGroup(name="basic", properties={"id", "type", "value", "principal", "receiptNumber", "status"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
				@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		}
)
public class Cash implements Maintainable, ISaasable{
	
	public final static int CASH_CREDIT = 1;
	
	public final static int CASH_DEBIT = 2;
	
	String id;
	
	int type = 64 ;
	
	BigDecimal value = new BigDecimal(0);
	
	String receiptNumber;
	
	UMPrincipal principal;
	
//	String description;
	
	int status = CASH_CREDIT;
	

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

	@EntityUIProperty(
	    	required=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="类型"), @I18nLocale(lang="en_US", value="Type")}),
			plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.cashType"), 
					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer
							)})
		)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	@ManyToOne
	@EntityUIProperty(
	    	required=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="用户"), @I18nLocale(lang="en_US", value="Principal")})
	)
	public UMPrincipal getPrincipal() {
		return principal;
	}

	public void setPrincipal(UMPrincipal principal) {
		this.principal = principal;
	}

//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}

	@EntityUIProperty(
			readOnly = true,
	    	required=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="状态"), @I18nLocale(lang="en_US", value="Status")}),
			plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.cashStatus"), 
					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer
							)})
		)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	private MaintainInfo maintainInfo = new MaintainInfo();
	
	private int version;
	
	@Embedded
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}
	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
	}
	
	

	@Version
	@EntityUIProperty(
			readOnly=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="版本"), @I18nLocale(lang="en_US", value="version")})
	)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@EntityUIProperty(
			required=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="金额"), @I18nLocale(lang="en_US", value="Cash")}),
			regex="\\d+(\\.\\d\\d)?", regexHint="0, 1.20"
	)
	@Column(precision=10, scale=2)
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@EntityUIProperty(
			maxRepeat=1,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="收据号"), @I18nLocale(lang="en_US", value="Receipt Number")})
	)
	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
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
