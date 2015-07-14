package com.cnebula.um.ejb.entity.usr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.cnebula.common.annotations.ejb.ui.EntityUI;
import com.cnebula.common.annotations.ejb.ui.EntityUIProperty;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyGroup;
import com.cnebula.common.annotations.ejb.ui.EntityUIPropertyPlugin;
import com.cnebula.common.annotations.ejb.ui.Validator;
import com.cnebula.common.annotations.ejb.ui.ValidatorType;
import com.cnebula.common.annotations.es.Property;
import com.cnebula.common.annotations.xml.CollectionStyleType;
import com.cnebula.common.annotations.xml.XMLIgnore;
import com.cnebula.common.annotations.xml.XMLMapping;
import com.cnebula.common.ejb.manage.ExtendAttributeHelper;
import com.cnebula.common.ejb.manage.Extendable;
import com.cnebula.common.ejb.manage.MaintainInfo;
import com.cnebula.common.ejb.manage.Maintainable;
import com.cnebula.common.ejb.manage.saas.Tenant;
import com.cnebula.common.i18n.I18n;
import com.cnebula.common.i18n.I18nLocale;
import com.cnebula.common.saas.ISaasable;
import com.cnebula.common.saas.ITenant;
import com.cnebula.common.xml.MapEntry;
import com.cnebula.um.ejb.saas.UMTenant;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.CascadeType.ALL;

@SuppressWarnings("serial")
@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(	name = "principle_type", 
//						discriminatorType = DiscriminatorType.STRING, 
//						length = 20)
//@DiscriminatorValue(value="principle")
@Table(name = "UM_Principle")
@EntityUI(
		i18ns={
				@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="用户"), @I18nLocale(lang="en_US", value="Principle")})
				,@I18n(defaultLang="zh_CN", key="/image", items={@I18nLocale(lang="zh_CN", value=".png")})
				,@I18n(defaultLang="zh_CN", key="/image48", items={@I18nLocale(lang="zh_CN", value="48.png")})
		}
		,shortDescription="name"
		,searchPoints={"name", "sex", "vip","userType", "userGroup","birthday", "organization.name", "organization.code", "organization.parent.name","position", "institute", "loginId",  "status", "card.code","additionalIds.id"}
		,summaryPoints={"name", "birthday", "vip","userType", "sex", "organization.name", "status", "position", "email"}
		,groups={
			@EntityUIPropertyGroup(name="basic", properties={"id", "name", "sex", "userType", "userGroup","photo", "birthday", "organization", "position", "institute","background","directlyUser","collegeName","departmentName","professionalName"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="基本信息"), @I18nLocale(lang="en_US", value="Basic Information")})),
			@EntityUIPropertyGroup(name="login", properties={"loginId","localLoginId", "password",  "status", "checkOrgIp", "card",  "maxConcurrentNumber","validDate", "invalidDate", "personality"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="登录信息"), @I18nLocale(lang="en_US", value="Login Information")})),
			@EntityUIPropertyGroup(name="contact", properties={"email","phone","msgType","msgCode", "fax","postalCode", "homeAddress", "officeAddr","mailAddr"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="联系信息"), @I18nLocale(lang="en_US", value="Contact Infomation")})),
			@EntityUIPropertyGroup(name="cash", properties={"totalCashValue","cashs"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="押金信息"), @I18nLocale(lang="en_US", value="Cash Infomation")})),
			@EntityUIPropertyGroup(name="aditionalId", properties={"additionalIds"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="附加ID"), @I18nLocale(lang="en_US", value="Additional ID")})),
			@EntityUIPropertyGroup(name="historyCards", properties={"historyCards"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="历史卡信息"), @I18nLocale(lang="en_US", value="History Card Infomation")})),
			@EntityUIPropertyGroup(name="extend", properties={ "otherPropertity1", "otherPropertity2", "otherPropertity3"}, i18n=@I18n(defaultLang="zh_CN", key="", items={@I18nLocale(lang="zh_CN", value="扩展信息"), @I18nLocale(lang="en_US", value="Extend")})),
			@EntityUIPropertyGroup(name="maintainInfo", properties={ "maintainInfo.creator", "maintainInfo.createTime","maintainInfo.lastModifier","maintainInfo.lastModifyTime", "maintainInfo.lifeCycleStatus", "version", "maintainInfo.description"}, i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="维护信息"), @I18nLocale(lang="en_US", value="Infomation for Maintenance")})),
		},
		plugin=@EntityUIPropertyPlugin(type="com.cnebula.um.admin.ui.UserEntityPlugin")
		,validator=@Validator(location=com.cnebula.common.annotations.ejb.ui.Location.server,type=ValidatorType.Service,target="(id=UMPrincipalValidator)")
		

)
public class UMPrincipal implements Maintainable, Extendable, Cloneable, ISaasable{

	/****************
	 * 实体属性部分
	 ****************/
	/**
	 * 用户ID
	 * 流水号
	 */
	String id;// = IDUtil.getId(Principle.class);
	
	
	/**
	 * 用户姓名
	 */
	String name;

	/**
	 * 用户状态
<item key="0" defaultLang="zh_CN">
      <locale value="None(0)" lang="en_US"/>
      <locale value="注销(0)" lang="zh_CN"/>
  	</item>
  	<item key="1" defaultLang="zh_CN">
      <locale value="Stopped(1)" lang="en_US"/>
      <locale value="停用(1)" lang="zh_CN"/>
  	</item>
  	<item key="2" defaultLang="zh_CN">
      <locale value="Active(2)" lang="en_US"/>
      <locale value="启用(2)" lang="zh_CN"/>
  	</item>
  	<item key="3" defaultLang="zh_CN">
      <locale value="Wait for verfying(3)" lang="en_US"/>
      <locale value="待审核(3)" lang="zh_CN"/>
  	</item>
	 */
	int status = 2;
	/**
	 * 帐户ID
	 */
	String loginId;
	
	/**
	 * 用户密码
	 */
	String password;
	
	boolean checkOrgIp = false;
	
	boolean ipUser = false;
	
	/**
	 * 帐户生效日期
	 */
	Date validDate = new Date();
	/**
	 * 帐户失效日期
	 */
	Date invalidDate = new Date(System.currentTimeMillis()+1000L*3600*24*365*50);
	
	//自然人属性
	
	/**
	 * 性别
	 */
	String sex = "m";
	/**
	 * 职务、职称
	 */
	String position;
	/**
	 * 电子邮箱
	 */
	String email;
	/**
	 * 电话
	 */
	String phone;
//	/**
//	 * 网络消息传输工具类型：如qq、msn
//	 */
	String msgType="qq";
	
	String msgCode;
	/**
	 * 用户类型，可根据类型确定扩展属性模板
	 * 可以是应用系统用户
	 * 发证员、读者、管理员
	 */
	int userType = 1;
	
	/**
	 * 传真号码
	 */
	String fax;
	
	/**
	 * 教育程度
	 */
	String education;
	
	/**
	 *用于用户对自己所在单位的描述，除非该用户所在“机构实体中的机构”就是他实际的机构。
	 */
	String institute;
	
	/**
	 * 办公地址
	 */
	String officeAddr;
	/**
	 * 通信地址
	 */
	String mailAddr;
	/**
	 * 邮政编码
	 */
	String postalCode;

	/**
	 * 居住地址
	 */
	String homeAddress;
	
	/**
	 * 照片
	 */
	byte[] photo = new byte[0];
	

	/**
	 * 维护信息
	 * 含扩展属性
	 */
	MaintainInfo maintainInfo = new MaintainInfo() ;
	
	/****************
	 * 实体关系部分
	 ****************/
	/**
	 * 所属机构
	 */
	Organization organization;
	
	/**
	 * 图书证
	 */
	Card card;
	

	/**
	 * 当前用户个性化信息
	 */
	Personality personality = new Personality() ;
	
	/**
	 * 出生日期
	 */
	
	Date birthday; 
	
	List<AdditionalId> additionalIds = new ArrayList<AdditionalId>();
	
	String otherPropertity1;
	
	String otherPropertity2;
	
	String otherPropertity3;
	
	
	/*
	1：高中，
	2：中专，
	3：大专，
	4：本科，
	5：硕士，
	6：博士，
	7：博士后
	 */
	int background;
	boolean directlyUser;
	String collegeName;
	String departmentName;
	String professionalName;
//	String stopReason ;
	int userGroup;
	int maxConcurrentNumber = -1;
	
	boolean localRegisted = true;

	// 以下reserved_0到reserved_9为系统保留字段，这些字段仅供UM系统开发人员内部使用
	String reserved_0;
	String reserved_1;
	String reserved_2;
	String reserved_3;
	String reserved_4;
	String reserved_5;
	String reserved_6;
	String reserved_7;
	String reserved_8;
	String reserved_9;
	
	// 以下depext_0到depext_9 扩展字段，主要面向部署应用实际情况有UMPrincipal没有明确定义的字段，其中depext_0字段用作馆员属性已经启用
	String depext_0="0"; // 已经启用
	String depext_1;
	String depext_2;
	String depext_3;
	String depext_4;
	String depext_5;
	String depext_6;
	String depext_7;
	String depext_8;
	String depext_9;
	

	@EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="教育背景"), @I18nLocale(lang="en_US", value="Education Background")}),
    		plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.UMPrincipalBackground"), 
//					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			})
    	)
	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}
	
    @EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="是否直属用户"), @I18nLocale(lang="en_US", value="Directly User")})
    	)
	public boolean isDirectlyUser() {
		return directlyUser;
	}

	public void setDirectlyUser(boolean directlyUser) {
		this.directlyUser = directlyUser;
	}
	
	
    @EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="所属学院名称"), @I18nLocale(lang="en_US", value="College Name")})
    	)
	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	
	
    @EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="所属系名称"), @I18nLocale(lang="en_US", value="Department Name")})
    	)
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	
    @EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="所属专业名称"), @I18nLocale(lang="en_US", value="Professional Name")})
    	)
	public String getProfessionalName() {
		return professionalName;
	}

	public void setProfessionalName(String professionalName) {
		this.professionalName = professionalName;
	}
	
//    @EntityUIProperty(
//    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="用户停用原因"), @I18nLocale(lang="en_US", value="User Stop Reason")}),
//    		maxlen=1024, plugin=@EntityUIPropertyPlugin(properties={
//			@Property(name="viewStyle", value=((1<<1)+ (1<<8) + (1<<9))+"", type=Property.Type.Integer),
//			@Property(name="viewHeight", value="50", type=Property.Type.Integer),
//			@Property(name="viewWidth", value="400", type=Property.Type.Integer)
//			})
//	)
//	public String getStopReason() {
//		return stopReason;
//	}
//
//	public void setStopReason(String stopReason) {
//		this.stopReason = stopReason;
//	}


	List<Cash> cashs  = new ArrayList<Cash>();
	
	protected int version;
	
	BigDecimal totalCashValue = new BigDecimal(0) ; 
	
	
	List<HistoryCard> historyCards = new ArrayList<HistoryCard>();
	
	@Version
	@EntityUIProperty(
		    	readOnly = true,
				i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="版本"), @I18nLocale(lang="en_US", value="Version")})
			)
	public int getVersion() {
		return version;
	}
	
	@Transient
	public boolean isValid() {
		long cur = System.currentTimeMillis();
		return validDate.getTime() <= cur && invalidDate.getTime()> cur;
	}
	
	//just for rest , has no effect
	public void setValid(boolean isValid){
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Temporal(TemporalType.DATE)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="生日"), @I18nLocale(lang="en_US", value="Birthday")})
			,formt="yyyy-MM-dd"
			,plugin=@EntityUIPropertyPlugin(name="IInputCalendarView")
			)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="校验机构IP范围"), @I18nLocale(lang="en_US", value="Check Organization IP Ranges")})
	)
	public boolean isCheckOrgIp() {
		return checkOrgIp;
	}

	public void setCheckOrgIp(boolean checkOrgIp) {
		this.checkOrgIp = checkOrgIp;
	}

	public UMPrincipal(){
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
	
	
	@Column(nullable=false, length=100)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="姓名"), @I18nLocale(lang="en_US", value="Name")} )
		,maxlen=32,minlen=2
	)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(nullable=false, length=100)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="密码"), @I18nLocale(lang="en_US", value="Password")} )
		,minlen=6,maxlen=20,
		plugin=@EntityUIPropertyPlugin(properties={@Property(name="$view.echoChar", value="*", type=Property.Type.Char)})
	)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	

	@Column(nullable=false, length=50)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="性别"), @I18nLocale(lang="en_US", value="Sex")} ),
			plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://sex"), 
//			@Property(name="viewStyle", value= (1<<3)+"", type=Property.Type.Integer)
			}) )
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Column(length=100)
	@EntityUIProperty(
		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="职务"), @I18nLocale(lang="en_US", value="Position")} )
		,maxlen=32,minlen=2
	)
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(length=200)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="电子邮箱"), @I18nLocale(lang="en_US", value="EMail")}),
			regex="([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})", regexHint="tom@sohu.com",
			minlen=6, maxlen=64
	)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	@Column(length=100)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="电话"), @I18nLocale(lang="en_US", value="Phone")}),
			minlen=6,maxlen=32
	)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	@Column(length=50)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="传真"), @I18nLocale(lang="en_US", value="Fax")}),
			minlen=6,maxlen=32
	)
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(length=200)
	@EntityUIProperty(
			plugin=@EntityUIPropertyPlugin(properties={
						@Property(name="viewWidth", value="300", type=Property.Type.Integer)
						}),
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="办公地点"), @I18nLocale(lang="en_US", value="Office Address")}),
			minlen=0, maxlen=120
	)
	public String getOfficeAddr() {
		return officeAddr;
	}
	public void setOfficeAddr(String officeAddr) {
		this.officeAddr = officeAddr;
	}
	
	@Column(length=200)
	@EntityUIProperty(
			plugin=@EntityUIPropertyPlugin(properties={
					@Property(name="viewWidth", value="300", type=Property.Type.Integer)
					}),
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="通讯地址"), @I18nLocale(lang="en_US", value="Mail Address")}),
			minlen=0, maxlen=120
	)
	public String getMailAddr() {
		return mailAddr;
	}
	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}
	
	@Column(length=30)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="邮政编码"), @I18nLocale(lang="en_US", value="Postal Code")}),
			minlen=4, maxlen=20
	)	
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Embedded
	@XMLIgnore
	public MaintainInfo getMaintainInfo() {
		return maintainInfo;
	}
	public void setMaintainInfo(MaintainInfo maintainInfo) {
		this.maintainInfo = maintainInfo;
	}
	
	@ManyToOne
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="所属机构"), @I18nLocale(lang="en_US", value="Organization")})
	)
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization org) {
		this.organization = org;
	}
	

	Map<String, String> extAttributes = new HashMap<String, String>();
	
	
//	@Lob
	@Transient
	@XMLIgnore
	public byte[] getExtAttributeData() throws Exception {
		return ExtendAttributeHelper.convertToBytes(this);
	}
	
	public void setExtAttributeData(byte[] extAttributeData) throws Exception{
		extAttributes = ExtendAttributeHelper.convertToMap(extAttributeData);
	}

	@Transient
	//@XMLIgnore
	@XMLMapping(collectionStyle=CollectionStyleType.EMBED,tag="extAttributes",childTag="attr", keyTag="name", valueTag="value", itemTypes={MapEntry.class, String.class, String.class})
	public Map<String, String> getExtAttributes() {
		return extAttributes;
	}

	public void setExtAttributes(Map<String, String> value) {
		this.extAttributes = value;
	}
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="个性化"), @I18nLocale(lang="en_US", value="Personality")})
	)
	@XMLIgnore
	public Personality getPersonality() {
		return personality;
	}

	public void setPersonality(Personality personality) {
		this.personality = personality;
	}

	@Column(unique=true)
	@EntityUIProperty(
			maxRepeat = 1,
			readOnly = true,
			required=true,
			minlen=2, maxlen=32,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="系统唯一登录名"), @I18nLocale(lang="en_US", value="Login ID")})
	)
	public String getLoginId() {
		if(tenant!=null&&tenant.getTenantId()!=null&&!tenant.getTenantId().isEmpty()){
			return tenant.getTenantId()+":"+localLoginId;
		}else{
			return localLoginId;
		}
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	String localLoginId ; 
	
	@Column
	@EntityUIProperty(
			required=true,converter="com.cnebula.common.convert.basic.StringTrimConverter",
			minlen=2, maxlen=32,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="登录名"), @I18nLocale(lang="en_US", value="Login ID")})
	)
	public String getLocalLoginId() {
		return localLoginId;
	}

	public void setLocalLoginId(String localLoginId) {
		this.localLoginId = localLoginId;
	}
	

	@Temporal(TemporalType.DATE)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="生效日期"), @I18nLocale(lang="en_US", value="Valid From")})
			,formt="yyyy-MM-dd"
			,plugin=@EntityUIPropertyPlugin(name="IInputCalendarView")
			)
	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	@Temporal(TemporalType.DATE)
	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="失效日期"), @I18nLocale(lang="en_US", value="Valid Until")})
			,formt="yyyy-MM-dd"
			,plugin=@EntityUIPropertyPlugin(name="IInputCalendarView")
			)
	public Date getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	@Column(length=50)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="即时消息类型"), @I18nLocale(lang="en_US", value="Instant Messager Type")}),
			plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://imType"), 
			@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Column(length=100)
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="即时消息帐号"), @I18nLocale(lang="en_US", value="Messager Code")}),
			minlen=4,maxlen=32
	)
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="用户类型"), @I18nLocale(lang="en_US", value="User Type")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.UMPrincipalUserTypes"), 
//					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			}) )
	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	@EntityUIProperty(
			minlen=2, maxlen=10,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="教育程度"), @I18nLocale(lang="en_US", value="Education")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.UMPrincipalEducation")}
	))
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@EntityUIProperty(
			plugin=@EntityUIPropertyPlugin(properties={
					@Property(name="viewWidth", value="300", type=Property.Type.Integer)
					}),
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="单位"), @I18nLocale(lang="en_US", value="Institute")}),
			minlen=8, maxlen=120
	)
	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="家庭住址"), @I18nLocale(lang="en_US", value="Home Address")}),
			minlen=0, maxlen=120
	)
	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
		
	@Lob
	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="照片"), @I18nLocale(lang="en_US", value="Photo")}),
			maxlen=1024*100,plugin=@EntityUIPropertyPlugin(name="IImageView", 
			properties={
						@Property(name="viewHeight", value="80", type=Property.Type.Integer),
						@Property(name="$view.height", value="80", type=Property.Type.Integer),
						@Property(name="$view.width", value="80", type=Property.Type.Integer),
						@Property(name="$view.editable", value="true", type=Property.Type.Boolean),
						@Property(name="$view.useDefaultWhenNoData", value="true", type=Property.Type.Boolean)
						
						}))
	@XMLIgnore
	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="预留属性1"), @I18nLocale(lang="en_US", value="otherPropertity1")})
	)
	public String getOtherPropertity1() {
		return otherPropertity1;
	}

	public void setOtherPropertity1(String otherPropertity1) {
		this.otherPropertity1 = otherPropertity1;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="预留属性2"), @I18nLocale(lang="en_US", value="otherPropertity2")})
	)
	public String getOtherPropertity2() {
		return otherPropertity2;
	}

	public void setOtherPropertity2(String otherPropertity2) {
		this.otherPropertity2 = otherPropertity2;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="预留属性3"), @I18nLocale(lang="en_US", value="otherPropertity3")})
	)
	public String getOtherPropertity3() {
		return otherPropertity3;
	}

	public void setOtherPropertity3(String otherPropertity3) {
		this.otherPropertity3 = otherPropertity3;
	}

	@EntityUIProperty(
		readOnly=true,
		i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="状态"), @I18nLocale(lang="en_US", value="status")})
		,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.UMPrincipalStatus"), 
				@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
		}) )
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="附加ID"), @I18nLocale(lang="en_US", value="Additional IDs")})
	)
	@OneToMany(cascade={CascadeType.ALL}, fetch = EAGER, mappedBy="owner")
	@XMLMapping(tag="additionalIds", childTag="additionalId")
	public List<AdditionalId> getAdditionalIds() {
		return additionalIds;
	}

	public void setAdditionalIds(List<AdditionalId> additionalIds) {
		this.additionalIds = additionalIds;
	}

	@EntityUIProperty(
			i18n=@I18n(items={@I18nLocale(lang="zh_CN", value="图书卡"), @I18nLocale(lang="en_US", value="Libaray Card")}),
			plugin=@EntityUIPropertyPlugin(type="ToOneDialogSelectPlugin")
			)
	@OneToOne(cascade={CascadeType.MERGE},mappedBy="principle")
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	@Transient
	public boolean isIpUser() {
		return ipUser;
	}

	public void setIpUser(boolean ipUser) {
		this.ipUser = ipUser;
	}
	
	@Override
	public UMPrincipal clone()  {
		try {
			HashMap<String, String> extA = new HashMap<String, String>();
			extA.putAll(getExtAttributes());
			UMPrincipal u  = (UMPrincipal)super.clone();
			u.setExtAttributes(extA);
			return u;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	@EntityUIProperty(
			readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="押金"), @I18nLocale(lang="en_US", value="CASH")})
	)
	@OneToMany(cascade={CascadeType.ALL}, fetch = EAGER, mappedBy="principal")
//	@OrderBy("maintainInfo.createTime DESC")
	@XMLIgnore
	public List<Cash> getCashs() {
		return cashs;
	}

	public void setCashs(List<Cash> cashs) {
		this.cashs = cashs;
	}

	@EntityUIProperty(
			readOnly=true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="押金总额"), @I18nLocale(lang="en_US", value="totalCashValue")}),
			regex="\\d+(\\.\\d\\d)?", regexHint="100.00, 1.20"
	)
	@Column(precision=10, scale=2)
	@XMLIgnore
	public BigDecimal getTotalCashValue() {
		return totalCashValue;
	}

	public void setTotalCashValue(BigDecimal totalCashValue) {
		this.totalCashValue = totalCashValue;
	}
	

	@EntityUIProperty(
			readOnly = true,
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="历史卡信息"), @I18nLocale(lang="en_US", value="History  Cards")})
	)
	@OneToMany(cascade={CascadeType.ALL},fetch = EAGER,mappedBy="principle")
	@XMLIgnore
	public List<HistoryCard> getHistoryCards() {
		return historyCards;
	}

	public void setHistoryCards(List<HistoryCard> historyCards) {
		this.historyCards = historyCards;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="所属用户组"), @I18nLocale(lang="en_US", value="User Group")})
			,plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.UMPrincipalUserGroup")}
	))
	public int getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(int userGroup) {
		this.userGroup = userGroup;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="帐户最大用户在线数"), @I18nLocale(lang="en_US", value="max Concurrent Number")})
	)
	@XMLIgnore
	public int getMaxConcurrentNumber() {
		return maxConcurrentNumber;
	}

	public void setMaxConcurrentNumber(int maxConcurrentNumber) {
		this.maxConcurrentNumber = maxConcurrentNumber;
	}
	
//	@Temporal(TemporalType.DATE)
//    @EntityUIProperty(
//    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="首次登录时间"), @I18nLocale(lang="en_US", value="First Login Time")})
//    	)
//	public Date getFirstLoginTime() {
//		return firstLoginTime;
//	}
//
//	public void setFirstLoginTime(Date firstLoginTime) {
//		this.firstLoginTime = firstLoginTime;
//	}

	
	
//    @EntityUIProperty(
//    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="是否具有ILL帐户"), @I18nLocale(lang="en_US", value="Have an ILL Account")})
//    	)
//	public boolean isHasILLaccount() {
//		return hasILLaccount;
//	}
//
//	public void setHasILLaccount(boolean hasILLaccount) {
//		this.hasILLaccount = hasILLaccount;
//	}
	
//    @EntityUIProperty(
//    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="本地用户ID"), @I18nLocale(lang="en_US", value="User Local ID")})
//    	)
//	public String getUserLocalID() {
//		return userLocalID;
//	}
//
//	public void setUserLocalID(String userLocalID) {
//		this.userLocalID = userLocalID;
//	}
//
//    @EntityUIProperty(
//    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="CALIS通行证"), @I18nLocale(lang="en_US", value="Calis ID")})
//    	)
//	public String getCalisID() {
//		return calisID;
//	}
//
//	public void setCalisID(String calisID) {
//		this.calisID = calisID;
//	}

    @EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="是否是本地注册用户"), @I18nLocale(lang="en_US", value="Local Registed")})
    	)
	public boolean isLocalRegisted() {
		return localRegisted;
	}

	public void setLocalRegisted(boolean localRegisted) {
		this.localRegisted = localRegisted;
	}
	
	//针对SAAS改造增加的方法
	
	private UMTenant tenant ; 
	
	@EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="租客"), @I18nLocale(lang="en_US", value="Calis ID")})
    )
    @ManyToOne(fetch=EAGER,targetEntity=UMTenant.class)
    @JoinColumn(name="TENANT_ID", referencedColumnName="id")
    @XMLIgnore
	public UMTenant getUMTenant() {
		return (UMTenant)tenant;
	}
	
	public void setUMTenant(UMTenant tenant) {
		this.tenant = tenant;
	}
	
	@Transient
	@XMLMapping(tagTypeMapping={"tenant=com.cnebula.um.ejb.saas.UMTenant"})
	public ITenant getTenant() {
		return tenant;
	}

	public void setTenant(ITenant tenant) {
		this.tenant = (UMTenant)tenant ; 
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		String cardid=this.getCard()==null?"null":this.getCard().getId();
		sb.append("[")
		.append("ID:"+this.getId()).append(",")
		.append("Status:"+this.getStatus()).append(",")
		.append("LoginId:"+this.getLoginId()).append(",")
		.append("CardID:"+cardid).append(",")
		.append("Name:"+this.getName())
		.append("]");
		
		return sb.toString();
	}
	
	boolean vip=false;

	@EntityUIProperty(
    		i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="是否VIP"), @I18nLocale(lang="en_US", value="VIP")})
    )
	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	@EntityUIProperty(
			i18n=@I18n(defaultLang="zh_CN",items={@I18nLocale(lang="zh_CN", value="馆员标识"), @I18nLocale(lang="en_US", value="UserType")}),
			plugin=@EntityUIPropertyPlugin(name="IComboView", properties={@Property(name="dataProvider", value="i18n://com.cnebula.um.ejb.entity.usr.UMPrincipal.depext_0"), 
					@Property(name="viewStyle", value= ( (1<<3) )+"", type=Property.Type.Integer)
			})
	)
	@Column(length=1024)
	public String getDepext_0() {
		return depext_0;
	}

	public void setDepext_0(String depext_0) {
		this.depext_0 = depext_0;
	}

	@Column(length=1024)
	public String getReserved_0() {
		return reserved_0;
	}

	public void setReserved_0(String reserved_0) {
		this.reserved_0 = reserved_0;
	}

	@Column(length=1024)
	public String getReserved_1() {
		return reserved_1;
	}

	public void setReserved_1(String reserved_1) {
		this.reserved_1 = reserved_1;
	}

	@Column(length=1024)
	public String getReserved_2() {
		return reserved_2;
	}

	public void setReserved_2(String reserved_2) {
		this.reserved_2 = reserved_2;
	}

	@Column(length=1024)
	public String getReserved_3() {
		return reserved_3;
	}

	public void setReserved_3(String reserved_3) {
		this.reserved_3 = reserved_3;
	}

	@Column(length=1024)
	public String getReserved_4() {
		return reserved_4;
	}

	public void setReserved_4(String reserved_4) {
		this.reserved_4 = reserved_4;
	}

	@Column(length=1024)
	public String getReserved_5() {
		return reserved_5;
	}

	public void setReserved_5(String reserved_5) {
		this.reserved_5 = reserved_5;
	}

	@Column(length=1024)
	public String getReserved_6() {
		return reserved_6;
	}

	public void setReserved_6(String reserved_6) {
		this.reserved_6 = reserved_6;
	}

	@Column(length=1024)
	public String getReserved_7() {
		return reserved_7;
	}

	public void setReserved_7(String reserved_7) {
		this.reserved_7 = reserved_7;
	}

	@Column(length=1024)
	public String getReserved_8() {
		return reserved_8;
	}

	public void setReserved_8(String reserved_8) {
		this.reserved_8 = reserved_8;
	}

	@Column(length=1024)
	public String getReserved_9() {
		return reserved_9;
	}

	public void setReserved_9(String reserved_9) {
		this.reserved_9 = reserved_9;
	}

	@Column(length=1024)
	public String getDepext_1() {
		return depext_1;
	}

	public void setDepext_1(String depext_1) {
		this.depext_1 = depext_1;
	}

	@Column(length=1024)
	public String getDepext_2() {
		return depext_2;
	}

	public void setDepext_2(String depext_2) {
		this.depext_2 = depext_2;
	}

	@Column(length=1024)
	public String getDepext_3() {
		return depext_3;
	}

	public void setDepext_3(String depext_3) {
		this.depext_3 = depext_3;
	}

	@Column(length=1024)
	public String getDepext_4() {
		return depext_4;
	}

	public void setDepext_4(String depext_4) {
		this.depext_4 = depext_4;
	}

	@Column(length=1024)
	public String getDepext_5() {
		return depext_5;
	}

	public void setDepext_5(String depext_5) {
		this.depext_5 = depext_5;
	}

	@Column(length=1024)
	public String getDepext_6() {
		return depext_6;
	}

	public void setDepext_6(String depext_6) {
		this.depext_6 = depext_6;
	}

	@Column(length=1024)
	public String getDepext_7() {
		return depext_7;
	}

	public void setDepext_7(String depext_7) {
		this.depext_7 = depext_7;
	}

	@Column(length=1024)
	public String getDepext_8() {
		return depext_8;
	}

	public void setDepext_8(String depext_8) {
		this.depext_8 = depext_8;
	}

	@Column(length=1024)
	public String getDepext_9() {
		return depext_9;
	}

	public void setDepext_9(String depext_9) {
		this.depext_9 = depext_9;
	}
}
