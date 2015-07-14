package com.cnebula.um.ejb.entity.perm;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;

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

import com.cnebula.aas.rt.AList;
import com.cnebula.aas.rt.Interpreter;
import com.cnebula.aas.util.PolicyRuleParseEx;
import com.cnebula.aas.util.PolicyRuleParseException;
import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.common.ejb.manage.IEntityCacheHelper;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.ejb.manage.Maintainable;
import com.cnebula.common.ejb.manage.perm.Operation;
import com.cnebula.common.ejb.manage.perm.ResourceType;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.common.security.simple.SimpleRule;
import com.cnebula.osgi.es.util.EasyServiceManagerImp;
import com.cnebula.um.ejb.saas.UMTenant;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_PermissionRule")
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="权限过滤规则"), @I18nLocale(lang="en_US", value="Rule")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		},
		shortDescription = "name", 
		summaryPoints = {"name", "entityLimitType", "operations"},
		searchPoints ={"name", "entityLimitType", "operations"},
		groups={
				@EntityUIPropertyGroup(name="basic", properties={"id", "name", "authable", "entityLimitType", "operations", "expression"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
				@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		}
)
public class PermissionRule   extends SimpleRule  implements Maintainable, Serializable , ISaasable{
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
	protected String entityLimitType;
	
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
	
	public PermissionRule(){
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
			plugin=@EntityUIPropertyPlugin(type = "com.cnebula.um.admin.ui.PermRulePlugin" ,
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
		
		IEntityCRUDService entityCRUDService =(IEntityCRUDService) EasyServiceManagerImp.getSingle().
		getService(IEntityCRUDService.class, null, IEntityCRUDService.class.getName(), "(unit=#{umunit})");
		
		IEntityCacheHelper ruleHelper= (IEntityCacheHelper) EasyServiceManagerImp.getSingle().
		getService(IEntityCacheHelper.class, null, IEntityCacheHelper.class.getName(),null);
		
		if (entityCRUDService == null){
			throw new RuntimeException("can not find IEntityCRUDService");
		}
		
		StringBuilder rt = new StringBuilder();
		if (tenant != null && tenant.getTenantId()!=null&& tenant.getTenantId().trim().length()>0){
			rt.append("(").append("u.tenant.tenantId=\""+tenant.getTenantId()).append("\")");
		}
		if (expression != null && expression.trim().length() > 0){
			rt.append("(").append(expression).append(")");
		}
		
		if (entityLimitType != null) {
			if (rt.length() > 0){
				rt.append("&");
			}
			if(entityCRUDService.getEntityFullInfoPool().getEntityInfo(entityLimitType)!= null){
				// is a entity
				rt.append( "typeOf(r)=").append("\"").append( entityLimitType ) .append("\"") ;
			}else{
				rt.append( "(typeOf(r)=").append("\"").append( entityLimitType ) .append("\"").append("|") ;
				rt.append("r.type.name = ").append("\"").append( entityLimitType ) .append("\")");
			}
			if(operations != null &&  operations.trim().length() > 0 ){
				
				String opsStr = "ops <= {";
				String conStr = "";
				boolean appendEntityConstraint = false; // copts.cpxOp= null
				
				String[] opArray = operations.split(",");
				for(String op: opArray){
					Operation opObj =ruleHelper.queryOperation(entityLimitType, op);
					if(opObj == null){
						ResourceType resourceType =ruleHelper.queryResourceType(entityLimitType);
						opObj = getOperation0(op, resourceType);
					}
					if(opObj!=null){
						String[] ConruleAndOPs = opObj.getCpxRule(entityLimitType, entityCRUDService);
						for(int i=0; i<ConruleAndOPs.length-1; i++){
							opsStr += "\"" + ConruleAndOPs[i] + "\"";
							opsStr += "," ; 
						}
						//for constraint rule
						
						if(ConruleAndOPs[ConruleAndOPs.length-1]!=null){
							
							conStr += ConruleAndOPs[ConruleAndOPs.length-1];
							conStr += "|" ;
							
						}else if(ConruleAndOPs[ConruleAndOPs.length-1]==null && !appendEntityConstraint){
							
							conStr += "(copts.cpxOp = null)|" ;
							appendEntityConstraint = true;
						}
					}else{ // should be operation of attr ,but no in a cpx op. such as update.status
						opsStr += ("\""+op + "\",") ;
						if(!appendEntityConstraint){
							conStr += "(copts.cpxOp = null)|" ;
							appendEntityConstraint = true;
						}
					}
					
					
				}
				if(opsStr.endsWith(",")){
					opsStr = opsStr.substring(0, opsStr.length()-1);
				}
				opsStr += "}" ;
				if(conStr.endsWith("|")){
					conStr = conStr.substring(0, conStr.length()-1);
				}
				rt.append("&").append(opsStr).append("&(").append(conStr).append(")");
			}
		}
		
		if (rt.length() == 0){
			rt.append("true");
		}
		
		try{
			compiledAST = PolicyRuleParseEx.parse(rt.toString());
		}catch (PolicyRuleParseException e) {
			System.out.println(rt.toString());
			e.printStackTrace();
			throw new PolicyRuleParseException(e.getMessage() + " in rule:" + name);
		}
		
	}

	private Operation getOperation0(String op,
			ResourceType resourceType) {
		if (resourceType != null) {
			for (Operation lop : resourceType.getOperations()) {
				if (op.equals(lop.getName())) {
					return lop;
				}
			}
		}
		return null;
	}
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="实体限制类型"), @I18nLocale(lang="en_US", value="Entity Limit Type")})
	)
	public String getEntityLimitType() {
		return entityLimitType;
	}

	public void setEntityLimitType(String entityLimitType) {
		this.entityLimitType = entityLimitType;
	}
	
	public boolean contain(Class cls, String[] operations, boolean authable){
		Class c = cls;
		while (c != null){
			if ( contain(c.getName(), operations, authable) ){
				return true;
			}
			c = c.getSuperclass();
		}
		return false;
	}

	public boolean contain(String clsName, String[] operations, boolean authable) {
		if (entityLimitType == null && (this.operations == null || this.operations.length() == 0)) {
			try {
				return (Boolean)Interpreter.evalObject(null, expression);
			} catch (Throwable e) {
				return false;
			}
		}
		if(entityLimitType!=null && clsName.equals(entityLimitType)&& (this.authable || (this.authable == authable))){
			if (this.operations == null || this.operations.length() == 0){
				return true;
			}
			if (operations == null || operations.length == 0){
				return true;
			}
			String[] ops = this.operations.split(",");
			AList la = new AList(ops);
			if(la.implyAll(new AList(operations))>=0){
				return true; 
			}
		}
		return false;
	}
/**
 * 
 * @param authable
 * @return null表示该规则不含任何实体过滤
 */
	public EntityQuery toEntityQuery(boolean authable) {
		
		if(entityLimitType == null || !(this.authable || (this.authable == authable)))
			return null ;
		if (expression == null || expression.length() == 0){
			return EntityQuery.NO_FILTER_QUERY;
		}
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
	public ITenant getTenant() {
		return tenant;
	}

	public void setTenant(ITenant tenant) {
		this.tenant = (UMTenant)tenant ; 
	}
	
	
}
