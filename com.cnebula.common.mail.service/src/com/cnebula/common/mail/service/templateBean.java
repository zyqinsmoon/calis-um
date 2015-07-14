package com.cnebula.common.mail.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.i18n.*;
import com.cnebula.common.annotations.ejb.ui.*;

@Entity
@Table(name="template")
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="模板管理"), @I18nLocale(lang="en_US", value="modelManage")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		},
		shortDescription = "template", 
		summaryPoints = { "mid", "functionid","modelString","showType", },
		searchPoints ={ "mid", "functionid","modelString","showType", },
		groups={
				@EntityUIPropertyGroup(name="basic", properties={"mid", "functionid","fileString","showType"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
				@EntityUIPropertyGroup(name="other", properties={ "modelString"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		}
)
public class templateBean {
	protected String mid;
	protected String functionid;
    protected String modelString;
    protected String fileString;
    protected String showType;
    @Id
    @GeneratedValue
    @Column(name="mid")
    @EntityUIProperty(
			readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="标识"), @I18nLocale(lang="en_US", value="ID")})
	)
    public String getMid(){
    	return mid;
    }
    public void setMid(String mid){
    	this.mid=mid;
    }
    @Column(nullable=false,length=20)
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="功能模块标识"), @I18nLocale(lang="en_US", value="functionid")})
	)
    public String getFunctionid(){
    	return functionid;
    }
    public void setFunctionid(String functionid){
    	this.functionid=functionid;
    }
    @Column(nullable=false,length=5000)
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="模板字符串"), @I18nLocale(lang="en_US", value="modelString")})
	)
    public String getModelString(){
    	return modelString;
    }
    public void setModelString(String modelString){
    	this.modelString=modelString;
    }
    
    
    @Column(nullable=false,length=200)
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="上传的文件路径"), @I18nLocale(lang="en_US", value="fileString")})
	)
    public String getFileString(){
    	return fileString;
    }
    public void setFileString(String fileString){
    	this.fileString=fileString;
    }
    
    
    @Column(nullable=false,length=20)
    @EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="显示类型"), @I18nLocale(lang="en_US", value="showType")})
	)
    public String getShowType(){
    	return showType;
    }
    public void setShowType(String showType){
    	this.showType=showType;
    }
}
