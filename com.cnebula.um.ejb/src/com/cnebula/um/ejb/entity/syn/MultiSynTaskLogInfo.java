package com.cnebula.um.ejb.entity.syn;

import static javax.persistence.FetchType.EAGER;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
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

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.saas.UMTenant;
@EntityUI(
		shortDescription="task",
		searchPoints={"task.name"},
		summaryPoints={"task.name","date", "recordTimestamp", "status"},
		groups={
				@EntityUIPropertyGroup(name="", properties={"task","date", "recordTimestamp", "status"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")})),
		}
		,i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="任务日志"), @I18nLocale(lang="en_US", value="TaskLogInfo")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
				
		}
)
@Entity
@Table(name = "UM_SYN_TASK_LOG")
public class MultiSynTaskLogInfo implements ISaasable {
	
	Date date;
	Date recordTimestamp;
	MultiSynTaskConfigInfo task;
	String status;
	String id;
	
	
    @Id
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
			formt="yyyy年MM月dd日  HH:mm:ss",
    		readOnly=true,
			i18n = @I18n(items={@I18nLocale(lang="zh_CN", value="最近记录时间戳"), @I18nLocale(lang="en_US", value="recordTimestamp")})
	)
	@Column(name="LASTTIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRecordTimestamp() {
		return recordTimestamp;
//		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
//		return sf.format(recordTimestamp);
	}

	public void setRecordTimestamp(Date recordTimestamp) {
		this.recordTimestamp = recordTimestamp;
	}

//	public getFormatedRecordTimestamp(){
//		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
//		return sf.format(recordTimestamp);
//	}
//	
	public MultiSynTaskLogInfo() {
	}
	
	public MultiSynTaskLogInfo(Date date, MultiSynTaskConfigInfo task, String status) {
		super();
		this.date = date;
		this.task = task;
		this.status = status;
	}

	@EntityUIProperty(
			formt="yyyy-MM-dd HH:mm:ss",
    		readOnly=true,
			i18n = @I18n(items={@I18nLocale(lang="zh_CN", value="执行日期"), @I18nLocale(lang="en_US", value="date")})
	)
	@Column(name="RUNDATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@EntityUIProperty(
    		readOnly=true,
			i18n = @I18n(items={@I18nLocale(lang="zh_CN", value="任务"), @I18nLocale(lang="en_US", value="task")})
	)
	@ManyToOne
	public MultiSynTaskConfigInfo getTask() {
		return task;
	}
	public void setTask(MultiSynTaskConfigInfo task) {
		this.task = task;
	}
	

		@EntityUIProperty(
	    	readOnly=true,
			i18n = @I18n(items={@I18nLocale(lang="zh_CN", value="任务状态"), @I18nLocale(lang="en_US", value="taskStatus")}),
			maxlen=1024, plugin=@EntityUIPropertyPlugin(properties={
			@Property(name="viewStyle", value=((1<<1)+ (1<<8) + (1<<9))+"", type=Property.Type.Integer),
			@Property(name="viewHeight", value="50", type=Property.Type.Integer),
			@Property(name="viewWidth", value="400", type=Property.Type.Integer)
			})
			)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	
	@Transient
	public ITenant getTenant() {
		return tenant;
	}

	public void setTenant(ITenant tenant) {
		this.tenant = (UMTenant)tenant ; 
	}
	
	
}
