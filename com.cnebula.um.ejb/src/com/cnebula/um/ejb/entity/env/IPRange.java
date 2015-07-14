package com.cnebula.um.ejb.entity.env;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.ejb.manage.Maintainable;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.entity.usr.Organization;
import com.cnebula.um.ejb.saas.UMTenant;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.CascadeType.ALL;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_IPRange")
@EntityUI(
		shortDescription="name",
		searchPoints={"name"},
		summaryPoints={"name"}	
		,i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="IP范围"), @I18nLocale(lang="en_US", value="IP Range")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
				
		}
//		,detailPoints={"id","name", "ipSegments"}
		,groups={
				@EntityUIPropertyGroup(name="basic", properties={"id","name","organization"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")})),
				@EntityUIPropertyGroup(name="ipv4Segment", properties={"ipSegments"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="IPv4 段"), @I18nLocale(lang="en_US", value="IPv4 Segment")})),
				@EntityUIPropertyGroup(name="ipv6Segment", properties={"ipV6Segments"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="IPv6 段"), @I18nLocale(lang="en_US", value="IPv6 Segment")})),
				@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		}
		
)
public class IPRange implements Maintainable, Serializable, ISaasable {

	/****************
	 * 实体属性部分
	 ****************/
	/**
	 * IP范围标识
	 * 流水号
	 */ 
	private String id;
	/**
	 * IP范围名称
	 */
	private String name;
	/** 
	 * 维护信息
	 * 含扩展属性
	 */
	private MaintainInfo maintainInfo = new MaintainInfo();
	
	/****************
	 * 实体关系部分
	 ****************/
	
	/**
	 * 其下IP段
	 */
	private List<IPSegment> ipSegments = new ArrayList<IPSegment>();
	
	/**
	 * 其下IPV6段
	 */
	private List<IPV6Segment> ipV6Segments = new ArrayList<IPV6Segment>();
	
	private int version;
	
	private Organization organization;
	
	@Version
	@EntityUIProperty(
			readOnly=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="版本"), @I18nLocale(lang="en_US", value="Version")})
		)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	public IPRange(){
	}
	
    @Id
    @GeneratedValue
    @Column(name="id")
    @EntityUIProperty(
    		readOnly=true,
			i18n = @I18n(items={@I18nLocale(lang="zh_CN", value="标识"), @I18nLocale(lang="en_US", value="ID")})
	)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(nullable=false, length=200)
	@EntityUIProperty(
			maxlen=32
			, i18n = @I18n(items={@I18nLocale(lang="zh_CN", value="名称"), @I18nLocale(lang="en_US", value="Name")})
	)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Embedded
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}
	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
	}
	
    @OneToMany( fetch=EAGER, mappedBy="iprange", cascade = { ALL })
    @EntityUIProperty(plugin=@EntityUIPropertyPlugin(name="rmembed"),
    		i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="所含IP段"), @I18nLocale(lang="en_US", value="IP Segements List")})
    )
	public List<IPSegment> getIpSegments() {
		return ipSegments;
	}
    
	public void setIpSegments(List<IPSegment> ipSegments) {
		this.ipSegments = ipSegments;
	}
	
	@OneToMany( fetch=EAGER, mappedBy="iprange", cascade = { ALL })
    @EntityUIProperty(plugin=@EntityUIPropertyPlugin(name="rmembed"),
    		i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="所含IPV6段"), @I18nLocale(lang="en_US", value="IPV6 Segements List")})
    )
	public List<IPV6Segment> getIpV6Segments() {
		return ipV6Segments;
	}

	public void setIpV6Segments(List<IPV6Segment> ipV6Segments) {
		this.ipV6Segments = ipV6Segments;
	}
	
	public void addIPSegment(IPSegment ipSeg) {
		if(!this.ipSegments.contains(ipSeg)) {
			this.ipSegments.add(ipSeg);
		}
	}
	public void removeIPSegment(IPSegment ipSeg) {
		this.ipSegments.remove(ipSeg);
	}

    @ManyToOne
    @JoinColumn(name="ORGANIZATION_ID")
	@EntityUIProperty(
			required=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="所属机构"), @I18nLocale(lang="en_US", value="Organization")})
	)
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	//针对SAAS改造增加的方法
	
	private UMTenant tenant ; 
	
	@EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="租客"), @I18nLocale(lang="en_US", value="Calis ID")})
    )
    @ManyToOne(fetch=EAGER,targetEntity=UMTenant.class)
    @JoinColumn(name="TENANT_ID", referencedColumnName="id")
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
		String orgid=this.getOrganization()==null?"null":getOrganization().getId();
		StringBuilder sb=new StringBuilder();
		sb.append("[")
		.append("ID:"+this.getId()).append(",")
		.append("Name:"+this.getName()).append(",")
		.append("Org:"+orgid).append(",")
		.append("]");
		
		return sb.toString();
	}
}
