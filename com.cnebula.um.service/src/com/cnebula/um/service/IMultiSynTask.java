package com.cnebula.um.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ScheduledFuture;

public interface IMultiSynTask  extends  Runnable{
	
	public static final int TASK_ENABLED=1;
	public static final String NULL_STRING="NULL";
	public static final String COLUMN_VALIDDATE = "validdate";
	public static final String COLUMN_PASSWORDTYPE = "PASSWORDTYPE";
	public static final String COLUMN_FILENAME="fname";
	
	public static final String TASK="task";
	
	public static final String SEPARATER_BETWEEN_ORGID_TIME = "@";
	public static final String VALIDDATE = "1005-01-01";
	public static final String INVALIDDATE = "3005-01-01";
	public static final String ERROR_RECORD_TIMESTAMP_SMALL_THEN_SYS = "时间戳不能大于当前系统时间";
	public static final String ERROR_RECORD_TIMESTAMP_SMALL_THEN_LAST = "记录时间戳小于上次同步的最后更新时间戳，本条记录不予导入！";
	
	public static final String COLUMN_PASSWORD = "um.password";
	public static final String COLUMN_CARDCODE = "card.code";
	public static final String COLUMN_CARDSTATUS = "card.status";
	public static final String COLUMN_AIDCODE = "aid.code";
	public static final String COLUMN_AIDTYPE = "aid.type";
	
	public static final String COLUMN_AIDSTATUS = "aid.status";
	public static final String COLUMN_EMAIL = "aid.um.email.code";
	public static final String COLUMN_EMAILSTATUS = "aid.um.email.status";
	public static final String COLUMN_PHONE = "aid.um.phone.code";
	public static final String COLUMN_PHONESTATUS = "aid.um.phone.status";
	
	public static final String COLUMN_INVALIDDATE = "um.invalidDate";
	public static final String COLUMN_NAME = "um.name";
	public static final String COLUMN_SEX = "um.sex";
	public static final String COLUMN_COLLEGENAME = "um.collegeName";
	public static final String COLUMN_DEPARTMENTNAME = "um.departmentName";
	
	public static final String COLUMN_PROFESSIONALNAME = "um.professionalName";
	public static final String COLUMN_USERTYPE = "um.userType";
	public static final String COLUMN_STATUS = "um.status";
	public static final String COLUMN_MAILADDR = "um.mailAddr";
	public static final String COLUMN_POSTALCODE = "um.postalCode";
	
	public static final String COLUMN_LOCALID = "um.otherPropertity1";
	public static final String COLUMN_BIRTHDAY = "um.birthday";
	public static final String COLUMN_TIMESTAMP = "um.timestamp";
	public static final String COLUMN_LOGINID="um.loginId";
	public static final String COLUMN_LOCAL_LOGINID="um.local.loginId";
	
	public static final String COLUMN_TEMP_CARD_ID="temp_card_id";
	public static final String COLUMN_TEMP_ADDI_ID="temp_addi_id";
	public static final String COLUMN_TEMP_UM_ID="temp_um_id";
	public static final String COLUMN_TEMP_ADDI_EMAIL_TYPE="temp_addi_email_type";
	public static final String COLUMN_TEMP_ADDI_EMAIL_ID="temp_addi_email_id";
	public static final String COLUMN_TEMP_ADDI_PHONE_ID="temp_addi_phone_id";
	public static final String COLUMN_TEMP_ADDI_PHONE="temp_addi_phone";
	public static final String COLUMN_TEMP_ADDI_EMAIL="temp_addi_email";

	public static final String COLUMN_TEMP_ADDI_PHONE_TYPE="temp_addi_phone_type";
	public static final String COLUMN_TEMP_UM_ORG_ID="temp_um_org_id";

	
	public final static String[] COLUMN_NAMES = { COLUMN_PASSWORD, COLUMN_CARDCODE,
		COLUMN_AIDCODE, COLUMN_AIDTYPE, COLUMN_AIDSTATUS,
		COLUMN_EMAIL, COLUMN_EMAILSTATUS, COLUMN_PHONE, 
		COLUMN_INVALIDDATE, COLUMN_NAME, COLUMN_SEX, COLUMN_COLLEGENAME,
		COLUMN_DEPARTMENTNAME, COLUMN_PROFESSIONALNAME, COLUMN_USERTYPE, 
		COLUMN_MAILADDR,COLUMN_POSTALCODE, COLUMN_LOCALID,COLUMN_TIMESTAMP,
		COLUMN_BIRTHDAY,COLUMN_STATUS,COLUMN_LOCAL_LOGINID};


	// 系统内部ID(id)
	// 登录名（loginId）
	// 密码（password）
	// 姓名（name）
	// 性别（sex）
	// 最后修改时间戳（timestamp）
	// 状态（status）
	// 生日("birthday")
	// 单位（"institute"）
	// 用户类型("userType")
	// 教育程度（"education"）
	// 家庭地址（"homeAddress"）
	// 电话（"phone"）
	// 身份证("idcard")
	// email
	// 通讯地址（"mailAddr"）
//	public final static String[] COLUMN_NAMES = { COLUMN_ID, COLUMN_LOGINID,
//			COLUMN_PASSWORD, COLUMN_NAME, COLUMN_SEX, COLUMN_TIMESTAMP,
//			COLUMN_STATUS, COLUMN_BIRTHDAY, COLUMN_INSTITUTE, COLUMN_USERTYPE,
//			COLUMN_EDUCATION, COLUMN_HOMEADDRESS, COLUMN_PHONE, COLUMN_IDCARD,
//			COLUMN_EMAIL, COLUMN_MAILADDR, COLUMN_AIDTYPE, COLUMN_AIDCODE,
//			COLUMN_CARDCODE };

	
	public abstract void stop();
	public abstract boolean isStoped();
	
	public abstract ScheduledFuture getScheduledFuture();

//	public abstract String getCurrentStatus();

//	public abstract Date getLastRecordTimestamp();

	public abstract String getID();

	public abstract void setScheduledFuture(ScheduledFuture sf);
	
//	public abstract Map<String,String> getConfig();

	public abstract MultiSynTaskInfo getCurrentStatus();
}
