package com.cnebula.um.ejb.entity.perm;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;

@Entity
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="应用系统角色"), @I18nLocale(lang="en_US", value="Dynamic Role")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		}
		,searchPoints={"name", "enabled", "application.name", "application.shortName", "type", "uMRoleOwner.name","maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version"}
		,summaryPoints={"name", "enabled", "type", "uMRoleOwner.name", "application.name", "maintainInfo.creator", "maintainInfo.lastModifier","maintainInfo.lastModifyTime"}
//		,detailPoints={"id", "name","enabled","type","parent","owner","userPolicy","permissionPolicy"}
		,groups={@EntityUIPropertyGroup(name="basic", properties={"id", "name","code", "enabled", "type", "application"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本属性"), @I18nLocale(lang="en_US", value="Basic")}))
				,@EntityUIPropertyGroup(name="userRules", properties={"uMUserRules"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="用户指派策略"), @I18nLocale(lang="en_US", value="User Filter Policy")}))
//				,@EntityUIPropertyGroup(name="permissionRules", properties={"uMPermissionRules"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="权限过滤策略"), @I18nLocale(lang="en_US", value="Permission Filter Policy")}))
				,@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
				}
		,plugin=@EntityUIPropertyPlugin(type="RoleEntityPlugin")
		
)
public class LegacyRole extends Role {
	
	protected Application application;
	
	String code;
	
	@EntityUIProperty(
			required=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="应用系统"), @I18nLocale(lang="en_US", value="Application System")})
	)
	@ManyToOne
	public Application getApplication() {
		return application;
	}
	
	public void setApplication(Application application) {
		this.application = application;
	}
	
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="角色代码"), @I18nLocale(lang="en_US", value="Code")})
	)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@EntityUIProperty(
			readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="父角色"), @I18nLocale(lang="en_US", value="Parent Role")})
			,plugin=@EntityUIPropertyPlugin(type="SelectParentRolePlugin")
	)
	@ManyToOne
	public Role getUMRoleParent() {
		return (Role)super.getParent();
	}
	
	public void setUMRoleParent(Role parent) {
		super.setParent(parent);
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		String applicationid=this.getApplication()==null?"null":getApplication().getId();
		sb.append("[")
		.append("ID:"+this.getId()).append(",")
		.append("Code:"+this.getCode()).append(",")
		.append("Application:"+applicationid).append(",")
		.append("Name:"+this.getName())
		.append("]");
		
		return sb.toString();
	}
	
}
