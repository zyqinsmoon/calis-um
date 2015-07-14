package com.cnebula.um.ejb.entity.usr;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.i18n.*;
import com.cnebula.common.annotations.ejb.ui.*;
import com.cnebula.common.annotations.es.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_Personality")
@EntityUI(
	i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="个性化"), @I18nLocale(lang="en_US", value="Contactor")})
		},
	shortDescription="id" ,
	groups={
			@EntityUIPropertyGroup(name="basic", properties={"locale", "numPerPage"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic")})),
	}
	)
public class Personality implements Serializable {

	/****************
	 * 实体属性部分
	 ****************/
	/**
	 * 扩展属性标识
	 * 流水号
	 */ 
	String id;// = IDUtil.getId(Personality.class);;

	Integer numPerPage ;
	
	Locale locale = Locale.SIMPLIFIED_CHINESE;
	
	UMPrincipal principle;

	
	public Personality() {
		numPerPage = 10 ;
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

	@Column
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="每页记录数"), @I18nLocale(lang="en_US", value="Num. of Records Per Page")})
	)
	public Integer getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	@OneToOne(mappedBy="personality")
	public UMPrincipal getPrinciple() {
		return principle;
	}

	public void setPrinciple(UMPrincipal principle) {
		this.principle = principle;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="语言"), @I18nLocale(lang="en_US", value="Language")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://locale"), 
					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	} 

	
}
