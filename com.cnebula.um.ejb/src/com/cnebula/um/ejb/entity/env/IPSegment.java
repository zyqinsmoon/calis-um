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

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_IPSegment")
@EntityUI(
		shortDescription = "id"
		,summaryPoints = { "start", "end" }
//		,detailPoints = { "start", "end" }
		,searchPoints = {"start", "end"}
		,i18ns={@I18n(items={@I18nLocale(lang="zh_CN", value="IP段"), @I18nLocale(lang="en_US", value="IP Segment")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})}
		,groups={@EntityUIPropertyGroup(name="basic", properties={"id","start", "end"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")}))}
)
public class IPSegment implements Serializable, ISaasable {

	/***************************************************************************
	 * 实体属性部分
	 **************************************************************************/
	/**
	 * IP段标识 流水号
	 */
	private String id;// = UUID.randomUUID().toString();
	/**
	 * IP段起始IP
	 */
	private long start;
	/**
	 * IP段终止IP
	 */
	private long end;

	private IPRange iprange = null;


	public IPSegment() {
		
	}

	public IPSegment(long start, long end) {
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

	@EntityUIProperty(formt="xxx.xxx.xxx.xxx", maxlen=14, minlen=7
			,i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="开始"), @I18nLocale(lang="en_US", value="Start")})
			)
	
	@Column(name = "IPStart", nullable = false, length = 20)
	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	@EntityUIProperty(formt="xxx.xxx.xxx.xxx", maxlen=14, minlen=7
			,i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="结束"), @I18nLocale(lang="en_US", value="End")})
			)
	@Column(name = "IPEnd", nullable = false, length = 20)
	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
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
	

//	@Transient
//	public String getStartString() {
//		return IP2String.IntToIP(start);
//	}
//
//	@Transient
//	public String getEndString() {
//		return IP2String.IntToIP(end);
//	}
//
//	public void setEndString(String endString) {
//		end = IP2String.ipToInt(endString);
//	}
//
//	public void setStartString(String startString) {
//		start = IP2String.ipToInt(startString);
//	}

//	@Embedded
//	public MaintainInfo getMaintainInfo() {
//		return maintainInfo;
//	}
//
//	public void setMaintainInfo(MaintainInfo maintainInfo) {
//		this.maintainInfo = maintainInfo;
//	}

	// @Transient
	// public String getIPEnd1() {
	// return IP2String.IntToIP(end).split("\\.")[0];
	// }
	//
	// @Transient
	// public String getIPEnd2() {
	// return IP2String.IntToIP(end).split("\\.")[1];
	// }
	//
	// @Transient
	// public String getIPEnd3() {
	// return IP2String.IntToIP(end).split("\\.")[2];
	// }
	//
	// @Transient
	// public String getIPEnd4() {
	// return IP2String.IntToIP(end).split("\\.")[3];
	// }
	//
	// @Transient
	// public String getIPStart1() {
	// return IP2String.IntToIP(start).split("\\.")[0];
	// }
	//
	// @Transient
	// public String getIPStart2() {
	// return IP2String.IntToIP(start).split("\\.")[1];
	// }
	//
	// @Transient
	// public String getIPStart3() {
	// return IP2String.IntToIP(start).split("\\.")[2];
	// }
	//
	// @Transient
	// public String getIPStart4() {
	// return IP2String.IntToIP(start).split("\\.")[3];
	// }
	//
	// public void setIPEnd1(String end1) {
	// long i = Integer.parseInt(end1);
	// i = i << 24;
	// end = (end & 0xffffffff00ffffffL) | i;
	// }
	//
	// public void setIPEnd2(String end2) {
	// long i = Integer.parseInt(end2);
	// i = i << 16;
	// end = (end & 0xffffffffff00ffffL) | i;
	// }
	//
	// public void setIPEnd3(String end3) {
	// long i = Integer.parseInt(end3);
	// i = i << 8;
	// end = (end & 0xffffffffffff00ffL) | i;
	// }
	//
	// public void setIPEnd4(String end4) {
	// long i = Integer.parseInt(end4);
	// end = (end & 0xffffffffffffff00L) | i;
	// }
	//
	// public void setIPStart1(String s1) {
	// long i = Integer.parseInt(s1);
	// i = i << 24;
	// start = (start & 0xffffffff00ffffffL) | i;
	// }
	//
	// public void setIPStart2(String s2) {
	// long i = Integer.parseInt(s2);
	// i = i << 16;
	// start = (start & 0xffffffffff00ffffL) | i;
	// }
	//
	// public void setIPStart3(String s3) {
	// long i = Integer.parseInt(s3);
	// i = i << 8;
	// start = (start & 0xffffffffffff00ffL) | i;
	// }
	//
	// public void setIPStart4(String s4) {
	// long i = Integer.parseInt(s4);
	//		start = (start & 0xffffffffffffff00L) | i;
	//	}
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
