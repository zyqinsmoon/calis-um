package com.cnebula.um.ejb.entity.env;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.um.ejb.saas.UMTenant;

@Entity
@Table(name = "UM_IPV6Segment")
@EntityUI(
		 shortDescription = "id"
		,summaryPoints = { "start", "end" }
		,searchPoints = {"start", "end"}
		,i18ns={@I18n(items={@I18nLocale(lang="zh_CN", value="IPV6 段"), @I18nLocale(lang="en_US", value="IPV6 Segment")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})}
		,groups={@EntityUIPropertyGroup(name="basic", properties={"id","start", "end"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")}))}
)
public class IPV6Segment implements Serializable, ISaasable{

	private static final long serialVersionUID = 8587147320533590463L;
	
	/***************************************************************************
	 * 实体属性部分
	 **************************************************************************/
	/**
	 * IPV6段标识 流水号
	 */
	private String id;// = UUID.randomUUID().toString();
	/**
	 * IPV6段起始IP
	 */
	private String start;
	/**
	 * IPV6段终止IP
	 */
	private String end;

	private IPRange iprange = null;


	public IPV6Segment(){
	}
	
	public IPV6Segment(String start, String end) {
		this.start = start;
		this.end = end;
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	@EntityUIProperty(
			readOnly=true,
			i18n = @I18n(items={@I18nLocale(lang="zh_CN", value="标识"), @I18nLocale(lang="en_US", value="ID")})
	)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@EntityUIProperty(regex="^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}",regexHint="2001:DA8:0201:FEDA:1245:BA98:3210:4562",maxlen=39, minlen=15
			,i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="开始"), @I18nLocale(lang="en_US", value="Start")})
			)
	@Column(name = "IPv6Start", nullable = false, length = 40)
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	@EntityUIProperty(regex="^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}",regexHint="2001:DA8:0201:FEDA:1245:BA98:3210:4562", maxlen=39, minlen=15
			,i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="结束"), @I18nLocale(lang="en_US", value="End")})
			)
	@Column(name = "IPv6End", nullable = false, length = 40)
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@ManyToOne
	public IPRange getIprange() {
		return iprange;
	}

	public void setIprange(IPRange iprange) {
		this.iprange = iprange;
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
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		String iprid=this.getIprange()==null?"null":getIprange().getId();
		sb.append("[")
		.append("ID:"+this.getId()).append(",")
		.append("Start:"+this.getStart()).append(",")
		.append("End:"+this.getEnd()).append(",")
		.append("IPRange:"+iprid)
		.append("]");
		
		return sb.toString();
	}
}
