package com.cnebula.um.ejb.entity.perm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.ejb.manage.ExtendAttributeHelper;
import com.cnebula.common.ejb.manage.Extendable;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.ejb.manage.Maintainable;
import com.cnebula.common.ejb.manage.perm.ResourceType;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.saas.UMTenant;

import static javax.persistence.InheritanceType.SINGLE_TABLE;
import javax.persistence.Inheritance;
import static javax.persistence.FetchType.EAGER;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_GeneralResource")
@Inheritance(strategy=SINGLE_TABLE)
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="资源"), @I18nLocale(lang="en_US", value="Resource")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		},
		shortDescription = "name", 
		summaryPoints = {"name", "type.name", "parent.name"},
		searchPoints ={"name", "type", "parent.name","otherPropertity1", "otherPropertity2", "otherPropertity3", "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version"},
		groups={
				@EntityUIPropertyGroup(name="basic", properties={"id", "name", "parent", "type"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
				@EntityUIPropertyGroup(name="extend", properties={ "otherPropertity1", "otherPropertity2", "otherPropertity3"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="扩展信息"), @I18nLocale(lang="en_US", value="Extend")})),
				@EntityUIPropertyGroup(name="children", properties={"children"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="下级资源"), @I18nLocale(lang="en_US", value="Lower  Resource")})),
				@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		}
)
public class GeneralResource  implements  Maintainable, Extendable, Serializable , ISaasable{

	/****************
	 * 实体属性部分
	 ****************/
	
	/****************
	 * 实体关系部分
	 ****************/

	String id;
	//名称,比如Application
	String name;
	
//	List<Operation> operations = new ArrayList<Operation>();
	
	/**
	 * 上级资源
	 */
	GeneralResource parent;
	/**
	 * 下级资源
	 */
	List<GeneralResource> children = new ArrayList<GeneralResource>();
	
	/**
	 * 所属类别
	 */
	ResourceType type;
	

	MaintainInfo maintainInfo = new MaintainInfo();
	
	
	private int version;
	
	String otherPropertity1;
	
	String otherPropertity2;
	
	String otherPropertity3;
	
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
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="预留属性2"), @I18nLocale(lang="en_US", value="otherPropertity2")})
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
	
	public GeneralResource(){
	}
	
	
    @ManyToOne
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="父资源"), @I18nLocale(lang="en_US", value="Upper Organization")})
	)
	public GeneralResource getParent() {
		return parent;
	}
    
	public void setParent(GeneralResource parent) {
		this.parent = parent;
	}
	
	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL, fetch=EAGER)
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="子资源"), @I18nLocale(lang="en_US", value="Lower Organization")})
	)
	public List<GeneralResource> getChildren() {
		return children;
	}
	public void setChildren(List<GeneralResource> children) {
		this.children = children;
	}
	

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="类型"), @I18nLocale(lang="en_US", value="Type")})
	)
	@ManyToOne
	public ResourceType getType() {
		return type;
	}


	public void setType(ResourceType type) {
		this.type = type;
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
			minlen=2, maxlen = 128, 
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="名称"), @I18nLocale(lang="en_US", value="Name")})
	)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

//
//	@ManyToMany(cascade={ MERGE, REFRESH }) 
//    @JoinTable(name = "UM_GResource_Operation",
//        joinColumns = {@JoinColumn(name = "GRes_ID", referencedColumnName = "id")},
//        inverseJoinColumns = {@JoinColumn(name = "Ope_ID", referencedColumnName = "id")})
//	public List<Operation> getOperations() {
//		return operations;
//	}
//
//	public void setOperations(List<Operation> operations) {
//		this.operations = operations;
//	}
//
//    public void addOperation(Operation operation) {
//        if (!this.operations.contains(operation)) {
//             this.operations.add(operation);
////             operations.setResource(this);
//        }
//	}
//	public void removePermission(Operation operation) {
//		this.operations.remove(operation);
//	}


	@Embedded
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}

	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
	}

	Map<String, String> extAttributes = new HashMap<String, String>();
	
	@Lob
	public byte[] getExtAttributeData() throws Exception {
		return ExtendAttributeHelper.convertToBytes(this);
	}
	
	public void setExtAttributeData(byte[] extAttributeData) throws Exception{
		extAttributes = ExtendAttributeHelper.convertToMap(extAttributeData);
	}

	@Transient
	public Map<String, String> getExtAttributes() {
		return extAttributes;
	}

	public void setExtAttributes(Map<String, String> value) {
		this.extAttributes = value;
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
	public ITenant getTenant() {
		return tenant;
	}

	public void setTenant(ITenant tenant) {
		this.tenant = (UMTenant)tenant ; 
	}

}
