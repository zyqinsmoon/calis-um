package com.cnebula.um.ejb.entity.perm;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.cnebula.aas.rt.AList;
import com.cnebula.aas.rt.SimpleEnvironment;
import com.cnebula.aas.rt.IPermissionDetail;
import com.cnebula.aas.util.PolicyRuleParseEx;
import com.cnebula.aas.util.PolicyRuleParseException;
import com.cnebula.common.ObjectPermission;
import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.ejb.manage.Maintainable;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.common.security.simple.SimpleRule;
import com.cnebula.um.ejb.entity.usr.UMPrincipal;
import com.cnebula.um.ejb.saas.UMTenant;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_UserRule")
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="用户过滤规则"), @I18nLocale(lang="en_US", value="Rule")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		},
		shortDescription = "name", 
		summaryPoints = {"name"},
		searchPoints ={"name"},
		groups={
				@EntityUIPropertyGroup(name="basic", properties={"id", "name", "entityLimitType",  "expression"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
				@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		}
)
public class UserRule   extends SimpleRule  implements Maintainable, Serializable, ISaasable {
	/**
	 * id
	 */
	protected String id;
	/**
	 * =“0”：由系统自动生成的规则。	
	 * =“1”，工作人员手工输入的规则。
	 */
	protected Integer type = new Integer(0);
	/**
	 * 1）若type＝0，则RuleName由系统给出，便于调试、查询。	
	 * 2）若type＝1，则RuleName需由管理员录入。此时，RuleName在同一个策略内唯一。
	 */

	
	
	protected MaintainInfo maintainInfo = new MaintainInfo() ;
	
	
	protected int version;
	
	/**
	 * 限制在某个实体类的过滤规则
	 */
	protected String entityLimitType = UMPrincipal.class.getName();
	
	protected String operations =""; 
	
	protected boolean authable = false;
	
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
	
	public UserRule(){
	}
	
	@Column(nullable=false)
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	@Embedded
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}
	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
	}
	
	@Column(nullable=false, length=150)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="名称"), @I18nLocale(lang="en_US", value="Name")})
	)
	public String getName() {
		return name;
	}
	
//	@Column(length=50)
//	public String getRuleEffect() {
//		return ruleEffect;
//	}
//	public void setRuleEffect(String ruleEffect) {
//		this.ruleEffect = ruleEffect;
//	}
//	
	@Column(nullable=false)
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
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
	
	/**
	 * 当type=0时	为script表达式，系统生成的表达式, 2）若type＝1, 手动创建的表达式
	 */
	@Override
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="表达式"), @I18nLocale(lang="en_US", value="Expression")}),
			maxlen=1024, 
			plugin=@EntityUIPropertyPlugin(type = "com.cnebula.um.admin.ui.UserRulePlugin" ,
			properties={
			@Property(name="viewStyle", value=((1<<1)+ (1<<8) + (1<<9))+"", type=Property.Type.Integer),
			@Property(name="viewHeight", value="100", type=Property.Type.Integer),
			@Property(name="viewWidth", value="1500", type=Property.Type.Integer),
			@Property(name="isSimple", value="true", type=Property.Type.Boolean)
			})
	)
	@Column(length=1024)
	public String getExpression() {
		return super.getExpression();
	}
	
	@Override
	public void setExpression(String expression) {
		super.setExpression(expression);
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
	}
	
	
	@Override
	public String toString() {
		String result = "";
		result += "name: ";
		result += name;
		result += "\n";
		result += "enabled: ";
		result += enabled;
		result += "\n";
		result += "Expression: ";
		result += getExpression();
		return result;
	}

	public void compile() throws PolicyRuleParseException {
		StringBuilder rt = new StringBuilder();
		if (tenant != null && tenant.getTenantId()!=null&& tenant.getTenantId().trim().length()>0){
			rt.append("(").append("u.tenant.tenantId=\""+tenant.getTenantId()).append("\")").append("&");
		}
		if (expression != null && expression.trim().length() > 0){
			rt.append("(").append(expression).append(")");
		}
		
		if (entityLimitType != null) {
			if (rt.length() > 0){
				rt.append("&");
			}
			rt.append( "typeOf(u)=").append("\"").append( entityLimitType ) .append("\"") ;
		}
//		if(operations != null &&  operations.trim().length() > 0 ){
//			rt.append( "ops<={" );
//			String[] ops = operations.split(",");
//			int index = 0 ;
//			for(String o: ops){
//				rt.append( "\"").append(o).append("\"");
//				if(index++ != ops.length-1){
//					rt.append(",") ; 
//				}
//			}
//			
//			rt.append("}") ; 
//		}
		
		if (rt.length() == 0){
			rt.append("true");
		}
		
		try{
			compiledAST = PolicyRuleParseEx.parse(rt.toString());
		}catch (PolicyRuleParseException e) {
			throw new PolicyRuleParseException(e.getMessage() + " in rule:" + name);
		}
		
	}
	@EntityUIProperty(
			readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="实体限制类型"), @I18nLocale(lang="en_US", value="Entity Limit Type")})
	)
	public String getEntityLimitType() {
		return entityLimitType;
	}

	public void setEntityLimitType(String entityLimitType) {
		this.entityLimitType = entityLimitType;
	}

	public boolean contain(String clsName, String[] operations, boolean authable) {
		if(entityLimitType!=null && clsName.equals(entityLimitType)&& (this.authable || (this.authable == authable))){
			String[] ops = this.operations.split(",");
			AList la = new AList(ops);
			if(la.implyAll(new AList(operations))>=0){
				return true; 
			}
		}
		return false;
	}

	public EntityQuery toEntityQuery(boolean authable) {
		
		if(entityLimitType == null || !(this.authable || (this.authable == authable)))
			return null ;
		try{
			return EntityQuery.filterRule2EntityQuery(expression);
		}catch (Exception e) {
			return null ;
		}
	}
	
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="可授权"), @I18nLocale(lang="en_US", value="Grantable")})
	)
	public boolean isAuthable() {
		return authable;
	}

	public void setAuthable(boolean authable) {
		this.authable = authable;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="操作序列"), @I18nLocale(lang="en_US", value="Operations")})
	)
	public String getOperations() {
		return operations;
	}

	public void setOperations(String operations) {
		this.operations = operations;
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
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserRule) {
			UserRule rule = (UserRule) obj;
			return String.valueOf(id).equals(rule.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (id+expression).hashCode();
	}
}
