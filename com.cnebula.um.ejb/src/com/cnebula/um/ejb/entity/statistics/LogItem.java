package com.cnebula.um.ejb.entity.statistics;

import static javax.persistence.FetchType.EAGER;

import java.util.Calendar;
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
import com.cnebula.common.annotations.ejb.ui.SortField;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.es.IRequest;
import com.cnebula.common.es.RequestContext;
import com.cnebula.common.es.SessionContext;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.saas.UMTenant;
/**
 * 示例1：
 * 
 * 高级用户Tom以关键词computer检索特色库tsk1，返回300条结果
 * 
 * subjectType: advance
 * subjectId：Tom
 * ObjectType: tsk
 * objectId: tsk1
 * operation: modify
 * 
 * 
 * 示例2：
 * 
 *
 */
@Entity(name="UM_LogItem")
@EntityUI(
	shortDescription="name",
	i18ns={
			@I18n(items={@I18nLocale(lang="zh_CN", value="日志记录"), @I18nLocale(lang="en_US", value="Log Record")})
			,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
			,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
	},
	searchPoints={"createDate","subjectId", "objectType","objectId", "operation", "remoteIp", "status"},
	summaryPoints={"createDate","subjectId", "objectType","objectId", "operation", "remoteIp", "status"}
	,groups={@EntityUIPropertyGroup(name="basic", properties={"subjectType","subjectId", "objectType","objectId", "operation", "remoteIp", "status"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本属性"), @I18nLocale(lang="en_US", value="Basic")}))
			,@EntityUIPropertyGroup(name="Time Info", properties={"createDate" ,"cyear", "cmonth","cweek","cday"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="时间信息"), @I18nLocale(lang="en_US", value="Time Info")})),
			}
	,sortPoints={@SortField(field="createDate", isASC=false)}
)
public class LogItem implements ISaasable{
	
	
	String id ; 
	
	//主体信息
	String subjectType ; 
	
    String subjectId ; 
    
    //客体信息，对应ResourceType
    String objectType ; 
    
	String objectId ; 
	
	//操作信息，对应ResourceType中的一种operation
	String operation ;
	
	//original 和 target 只在实体属性级有效
	/**
	 * operation: modify，
	 * original 为原值
	 * target 为目标值
	 */
//	String original ; 
//	
//	String target ;
	
	int status ; //
	
	//环境信息
	
	String sessionId ; 
	
	Date createDate ; 
	
	String remoteIp ;
	
	int cyear ;
	
	int cmonth ; 
	
	int cweek ; 
	
	int cday ;
	
	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="年份"), @I18nLocale(lang="en_US", value="Year")})
		)
	public int getCyear() {
		return cyear;
	}

	public void setCyear(int cyear) {
		this.cyear = cyear;
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="月份"), @I18nLocale(lang="en_US", value="Month")})
		)
	public int getCmonth() {
		return cmonth;
	}

	public void setCmonth(int cmonth) {
		this.cmonth = cmonth;
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="周次"), @I18nLocale(lang="en_US", value="Week")})
		)
	public int getCweek() {
		return cweek;
	}

	public void setCweek(int cweek) {
		this.cweek = cweek;
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="日期"), @I18nLocale(lang="en_US", value="Day")})
		)
	public int getCday() {
		return cday;
	}

	public void setCday(int cday) {
		this.cday = cday;
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
	
	public void fillEnv() {
		//环境
		IRequest request = RequestContext.getRequest();
		
		if(request != null ){
			setRemoteIp(RequestContext.getRequest().getRemoteIp());
		}
		setSessionId(SessionContext.getSession().getId());
		Calendar calendar = Calendar.getInstance() ; 
		setCreateDate(new Date());
		calendar.setTime(createDate);
		setCyear(calendar.get(Calendar.YEAR)) ; 
		setCmonth(calendar.get(Calendar.MONTH)+1);
		setCweek(calendar.get(Calendar.WEEK_OF_YEAR));
		setCday(calendar.get(Calendar.DAY_OF_YEAR));
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="用户类型"), @I18nLocale(lang="en_US", value="User Type")})
		)
	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="用户标识"), @I18nLocale(lang="en_US", value="User Id")})
		)
	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="资源类型"), @I18nLocale(lang="en_US", value="Resource Type")})
		)
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="资源标识"), @I18nLocale(lang="en_US", value="Resource Id")})
		)
	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="操作"), @I18nLocale(lang="en_US", value="Operation")})
		)
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

//	public String getOriginal() {
//		return original;
//	}
//
//	public void setOriginal(String original) {
//		this.original = original;
//	}
//
//	public String getTarget() {
//		return target;
//	}
//
//	public void setTarget(String target) {
//		this.target = target;
//	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="状态"), @I18nLocale(lang="en_US", value="Status")})
		)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="会话标识"), @I18nLocale(lang="en_US", value="Session Id")})
		)
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@EntityUIProperty(
	    	readOnly = true,
	    	formt="yyyy-MM-dd HH:mm:ss",
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="创建日期"), @I18nLocale(lang="en_US", value="Create Date")})
		)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@EntityUIProperty(
	    	readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="网络地址"), @I18nLocale(lang="en_US", value="IP")})
		)
	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

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
	
	@Transient
	public ITenant getTenant() {
		return tenant;
	}

	public void setTenant(ITenant tenant) {
		this.tenant = (UMTenant)tenant ; 
	}
	
//	String operationType ; 
	
}
