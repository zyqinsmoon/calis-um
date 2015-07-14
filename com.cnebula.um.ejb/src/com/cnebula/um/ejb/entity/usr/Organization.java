package com.cnebula.um.ejb.entity.usr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.cnebula.common.annotations.ejb.ui.*;
import com.cnebula.common.i18n.*;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.common.ejb.manage.ExtendAttributeHelper;
import com.cnebula.common.ejb.manage.Extendable;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.ejb.manage.Maintainable;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.um.ejb.entity.env.IPRange;
import com.cnebula.um.ejb.saas.UMTenant;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.CascadeType.ALL;

import com.cnebula.common.annotations.es.*;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_Organization")
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="机构"), @I18nLocale(lang="en_US", value="Organization")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		},
		shortDescription = "name", 
		summaryPoints = {"name", "shortName", "parent.name", "type", "code", "otherPropertity2", "authCenter.name", "province", "city", "homepage", "dType"},
		searchPoints ={"name", "parent.name", "type", "code",  "otherPropertity2", "authCenter.name", "province", "city", "homepage", "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description", "dType"},
		groups={
				@EntityUIPropertyGroup(name="basic", properties={ "id", "name", "shortName", "parent", "type", "codeType", "code", "otherPropertity2", "authCenter", "province", "city", "homepage", "completeCode", "dType"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
				@EntityUIPropertyGroup(name="extend", properties={ "otherPropertity1", "otherPropertity2", "otherPropertity3"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="扩展信息"), @I18nLocale(lang="en_US", value="Extend")})),
				@EntityUIPropertyGroup(name="ipRange", properties={"ipRanges"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="IP范围"), @I18nLocale(lang="en_US", value="IP Range")})),
				@EntityUIPropertyGroup(name="children", properties={"children"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="下级机构"), @I18nLocale(lang="en_US", value="Lower  Organization")})),
				@EntityUIPropertyGroup(name="contacts", properties={"contactors"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="联系人"), @I18nLocale(lang="en_US", value="Contacts")})),
				@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		}
)
public class Organization implements Maintainable, Extendable, ISaasable{
	
	final static String CONSTANT_NOSET = "noset" ;

	/****************
	 * 实体关系部分
	 ****************/
	/**
	 * 当前机构的父机构
	 * 对应机构树的上级节点
	 */
	private Organization parent = null;
	/**
	 * 当前机构的下属机构
	 * 对应机构树的下级节点
	 */
	private List<Organization> children = new ArrayList<Organization>();
	/**
	 * 当前机构下的用户
	 */
//	private List<UMPrincipal> principles = new ArrayList<UMPrincipal>();
	
	
	String otherPropertity1;
	
	String otherPropertity2;
	
	String otherPropertity3;

	/****************
	 * 实体属性部分
	 ****************/
	/**
	 * 机构ID
	 * 主键，唯一
	 */
	private String id;// = IDUtil.getId(Organization.class);
	/**
	 * 机构代码
	 */
	private String code;
	/**
	 * <pre>
	 * 机构代码类型
	  <item key="ChinaMOE" defaultLang="zh_CN" >
	      <locale value="教育部馆代码(ChinaMOE)" lang="zh_CN" />
	   </item>
	   <item key="CALIS" defaultLang="zh_CN" >
	      <locale value="CALIS馆代码(CALIS)" lang="zh_CN" />
	   </item>
	   <item key="ChinaStdOrgCode" defaultLang="zh_CN" >
	      <locale value="组织机构代码(ChinaStdOrgCode)" lang="zh_CN" />
	   </item>
	   <item key="otherCode" defaultLang="zh_CN" >
	      <locale value="其他类型代码(otherCode)" lang="zh_CN" />
	   </item>
	 * </pre>
	 */
	private String codeType;
	/**
	 * 机构名称
	 */
	private String name;
	/**
	 * 机构名称缩写
	 */
	private String shortName;
	/**
	 * 机构类型, CALIS成员馆(calisMember), 教育机构（edu）,一般机构（org），公司（com），政府部门(gov), 其他(other)
	 */
	private String type="edu";
	/**
	 * 机构所在省份
	 */
	private String province;
	/**
	 * 机构所在城市
	 */
	private String city;
	/**
	 * 机构网站地址
	 */
	private String homepage;
	/**
	 * 维护信息
	 * 含扩展属性
	 */
	private MaintainInfo maintainInfo = new MaintainInfo();
	
	/****************
	 * 实体关系部分
	 ****************/
	/**
	 * 所属认证中心（或认证系统）
	 */
	private AuthCenter authCenter ; //= new AuthCenter();
	/**
	 * 旗下的联系人
	 */
	private List<Contactor> contactors = new ArrayList<Contactor>();
	/**
	 * 允许的IP范围
	 */
	private List<IPRange> ipRanges = new ArrayList<IPRange>();
	/**
	 * 扩展属性
	 */
	
	private int version;
	
	/**
	 * 机构完整代码，包括上级机构的代码，
	 * 例如父机构的代码为aaa，自己的机构代码为bbb
	 * 则完整代码为ccc
	 */
	private String completeCode ; 
	
	//是否985
	private String isNineEightFive = CONSTANT_NOSET;
		
	//是否新升本院校
	private String  isNewUpToUndergraduate = CONSTANT_NOSET;
	
	//西部院校类型：取值范围：A类西部院校、B类西部院校。
	private String westCollegeType = CONSTANT_NOSET;
	
    /**共享域类型,虚拟共享域='VirtualSAASCenterInfo',实体共享域='SAASCenterInfo',普通机构='NodeInfo'*/
	private String dType;
	
	@EntityUIProperty(
			readOnly=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="共享域类型"), @I18nLocale(lang="en_US", value="dType")})
	)
	@Column(insertable = false, updatable = false)
	public String getdType() {
		return dType;
	}

	public void setdType(String dType) {
		this.dType = dType;
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
	
	@Column(length=100)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="代码"), @I18nLocale(lang="en_US", value="Code")})
	)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(length=100)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="代码类型"), @I18nLocale(lang="en_US", value="Code Type")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://orgCodeType"), 
//					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="名称"), @I18nLocale(lang="en_US", value="Name")})
	)
	@Column(nullable=false, length=200)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="类型"), @I18nLocale(lang="en_US", value="Type")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://orgType"), 
//					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
				}) )
	@Column(nullable=false, length=100)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(length=50)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="省份"), @I18nLocale(lang="en_US", value="Province")})
	)
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	@Column(length=50)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="城市"), @I18nLocale(lang="en_US", value="City")})
	)
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	@Embedded
	@XMLIgnore
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}
	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
	}
	
    @ManyToOne
    @JoinColumn(name="authCenter_id")
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="认证中心"), @I18nLocale(lang="en_US", value="Authentication Center")})
	)
	@XMLIgnore
	public AuthCenter getAuthCenter() {
		return authCenter;
	}
	public void setAuthCenter(AuthCenter authCenter) {
		this.authCenter = authCenter;
	}
	
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="UM_Org_Contactor")
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="联系人"), @I18nLocale(lang="en_US", value="Contact")})
	)
	@XMLIgnore
	public List<Contactor> getContactors() {
		return contactors;
	}
	public void setContactors(List<Contactor> contactors) {
		this.contactors = contactors;
	}
	public void addContactor(Contactor contactor) {
		if(!this.contactors.contains(contactor)) {
			this.contactors.add(contactor);
		}
	}
	public void removeContactor(Contactor contactor) {
		this.contactors.remove(contactor);
	}
	
	@OneToMany(mappedBy="organization", cascade=ALL, fetch=FetchType.EAGER) 
	@XMLIgnore
    public List<IPRange> getIpRanges() {
		return ipRanges;
	}
	public void setIpRanges(List<IPRange> ipRanges) {
		this.ipRanges = ipRanges;
	}
	public void addIpRange(IPRange ipRange) {
		if(!this.ipRanges.contains(ipRange)) {
			this.ipRanges.add(ipRange);
		}
	}
	public void removeIpRange(IPRange ipRange) {
		this.ipRanges.remove(ipRange);
	}

	Map<String, String> extAttributes = new HashMap<String, String>();
	
	@Lob
	@XMLIgnore
	public byte[] getExtAttributeData() throws Exception {
		return ExtendAttributeHelper.convertToBytes(this);
	}
	
	public void setExtAttributeData(byte[] extAttributeData) throws Exception{
		extAttributes = ExtendAttributeHelper.convertToMap(extAttributeData);
	}

	@Transient
	@XMLIgnore
	public Map<String, String> getExtAttributes() {
		return extAttributes;
	}

	public void setExtAttributes(Map<String, String> value) {
		this.extAttributes = value;
	}
	
	
	public Organization(){
	}
	
    @ManyToOne
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="上级机构"), @I18nLocale(lang="en_US", value="Upper Organization")})
	)
	@XMLIgnore
	public Organization getParent() {
		return parent;
	}
	public void setParent(Organization parent) {
		this.parent = parent;
	}
	
    @OneToMany(mappedBy="parent", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
     @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="下级机构"), @I18nLocale(lang="en_US", value="Lower Organization")})
	)
	@XMLIgnore
	public List<Organization> getChildren() {
		return children;
	}
	public void setChildren(List<Organization> children) {
		this.children = children;
	}
    public void addSubOrg(Organization child) {
        if (!this.children.contains(child)) {
             this.children.add(child);
             child.setParent(this);
        }
	}
	public void removeChild(Organization child) {
		child.setParent(null); //(null);
		this.children.remove(child);
	}
	
//	@OneToMany(mappedBy="organization", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
//	public List<UMPrincipal> getPrinciples() {
//		return principles;
//	}
//	public void setPrinciples(List<UMPrincipal> principles) {
//		this.principles = principles;
//	}
//    public void addPrinciple(UMPrincipal principle) {
//        if (!this.principles.contains(principle)) {
//             this.principles.add(principle);
//             principle.setOrganization(this);
//        }
//	}
//	public void removePrinciple(UMPrincipal principle) {
//		principle.setOrganization(null);
//		this.principles.remove(principle);
//	}
	
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
			plugin=@EntityUIPropertyPlugin(properties={
					@Property(name="viewWidth", value="300", type=Property.Type.Integer)
					}),
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="主页"), @I18nLocale(lang="en_US", value="Homepage")})
	)
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="预留属性1"), @I18nLocale(lang="en_US", value="otherPropertity1")})
	)
	public String getOtherPropertity1() {
		return otherPropertity1;
	}

	public void setOtherPropertity1(String otherPropertity1) {
		this.otherPropertity1 = otherPropertity1;
	}

	@EntityUIProperty(
			readOnly=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="预留属性2"), @I18nLocale(lang="en_US", value="fullCode")})
	)
	public String getOtherPropertity2() {
		return otherPropertity2;
	}

	public void setOtherPropertity2(String otherPropertity2) {
		this.otherPropertity2 = otherPropertity2;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="预留属性3"), @I18nLocale(lang="en_US", value="otherPropertity3")})
	)
	public String getOtherPropertity3() {
		return otherPropertity3;
	}

	public void setOtherPropertity3(String otherPropertity3) {
		this.otherPropertity3 = otherPropertity3;
	}

	@EntityUIProperty(readOnly= true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="完整代码"), @I18nLocale(lang="en_US", value="otherPropertity3")})
	)
	public String getCompleteCode() {
		return completeCode;
	}

	public void setCompleteCode(String completeCode) {
		this.completeCode = completeCode;
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

	public String getIsNineEightFive() {
		return isNineEightFive;
	}

	public void setIsNineEightFive(String isNineEightFive) {
		this.isNineEightFive = isNineEightFive;
	}

	public String getIsNewUpToUndergraduate() {
		return isNewUpToUndergraduate;
	}

	public void setIsNewUpToUndergraduate(String isNewUpToUndergraduate) {
		this.isNewUpToUndergraduate = isNewUpToUndergraduate;
	}

	public String getWestCollegeType() {
		return westCollegeType;
	}

	public void setWestCollegeType(String westCollegeType) {
		this.westCollegeType = westCollegeType;
	}
	
}
