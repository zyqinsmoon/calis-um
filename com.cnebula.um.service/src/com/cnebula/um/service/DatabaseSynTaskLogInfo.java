package com.cnebula.um.service;

import java.util.Date;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
@EntityUI(
		shortDescription="task",
		searchPoints={"task","date", "recordTimestamp", "status"},
		summaryPoints={"task","date", "recordTimestamp", "status"},
		groups={
				@EntityUIPropertyGroup(name="", properties={"task","date", "recordTimestamp", "status"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")})),
		}
		,i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="任务信息"), @I18nLocale(lang="en_US", value="TaskLogInfo")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
				
		}
)
public class DatabaseSynTaskLogInfo {
	
	Date date;
	Date recordTimestamp;
	String task;
	String status;
	
	
	@EntityUIProperty(
    		readOnly=true,
			i18n = @I18n(items={@I18nLocale(lang="zh_CN", value="最近记录时间戳"), @I18nLocale(lang="en_US", value="recordTimestamp")})
	)
	public Date getRecordTimestamp() {
		return recordTimestamp;
	}

	public void setRecordTimestamp(Date recordTimestamp) {
		this.recordTimestamp = recordTimestamp;
	}

	
	public DatabaseSynTaskLogInfo() {
	}
	
	public DatabaseSynTaskLogInfo(Date date, String task, String status) {
		super();
		this.date = date;
		this.task = task;
		this.status = status;
	}

	@EntityUIProperty(
    		readOnly=true,
			i18n = @I18n(items={@I18nLocale(lang="zh_CN", value="执行日期"), @I18nLocale(lang="en_US", value="date")})
	)
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
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
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
	
	
	
}
