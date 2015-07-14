create table njl50.Z303 (
z303_rec_key	char(12)	,		
z303_proxy_for_id	char(12)	 ,		
z303_primary_id	varchar2(12)	 ,		
z303_name_key	char(50)	 ,		
z303_user_library	char(5)	 ,		
z303_open_date	number(8)	 ,		
z303_update_date	number(8)	 ,		
z303_con_lng	char(3)	 ,		
z303_alpha	char(1)	 ,		
z303_name	varchar2(200)	 ,		
z303_title	char(10)	 ,		
z303_delinq_1	number(2)	 ,		
z303_delinq_n_1	varchar2(200)	 ,		
z303_delinq_1_update_date	number(8)	 ,		
z303_delinq_1_cat_name	char(10)	 ,		
z303_delinq_2	number(2)	 ,		
z303_delinq_n_2	varchar2(200)	 ,		
z303_delinq_2_update_date	number(8)	 ,		
z303_delinq_2_cat_name	char(10)	 ,		
z303_delinq_3	number(2)	 ,		
z303_delinq_n_3	varchar2(200)	 ,		
z303_delinq_3_update_date	number(8)	 ,		
z303_delinq_3_cat_name	char(10)	 ,		
z303_budget	varchar2(50)	 ,		
z303_profile_id	varchar2(12)	 ,		
z303_ill_library	char(5)	 ,		
z303_home_library	char(5)	 ,		
z303_field_1	varchar2(100)	,		
z303_field_2	varchar2(100)	 ,		
z303_field_3	varchar2(100)	 ,		
z303_ill_total_limit	number(4)	 ,		
z303_ill_active_limit	number(4)	 ,		
z303_dispatch_library	char(5)	 ,		
z303_birth_date	number(8)	 ,		
z303_export_consent	char(1)	 ,		
z303_proxy_id_type	number(2)	 ,		
z303_send_all_letters	char(1)	 ,		
z303_plif_modification	varchar2(50)	
);

create table njl50.Z304 (
Z304_REC_KEY	CHAR(14)               
Z304_ADDRESS	VARCHAR2(250)  ,     
Z304_ZIP	CHAR(9),
Z304_EMAIL_ADDRESS	VARCHAR2(60),
Z304_TELEPHONE	VARCHAR2(30),
Z304_DATE_FROM	NUMBER(8),
Z304_DATE_TO	NUMBER(8),
Z304_ADDRESS_TYPE	NUMBER(2),
Z304_TELEPHONE_2	VARCHAR2(30),
Z304_TELEPHONE_3	VARCHAR2(30),
Z304_TELEPHONE_4	VARCHAR2(30),
Z304_UPDATE_DATE	NUMBER(8),
Z304_CAT_NAME	CHAR(10)
);

create table njl50.Z305 (
Z305_REC_KEY	CHAR(17),           
Z305_OPEN_DATE	NUMBER(8)                           ,      
Z305_UPDATE_DATE	NUMBER(8)                         ,
Z305_BOR_TYPE	CHAR(2)                               ,       
Z305_BOR_STATUS	CHAR(2)                             ,
Z305_EXPIRY_DATE	NUMBER(8)                         ,       
Z305_NOTE	VARCHAR2(80)                                                 ,            
Z305_LOAN_PERMISSION	CHAR(1)                                          ,            
Z305_PHOTO_PERMISSION	CHAR(1)                                          ,            
Z305_OVER_PERMISSION	CHAR(1)                                          ,            
Z305_MULTI_HOLD	CHAR(1)                                                ,            
Z305_LOAN_CHECK	CHAR(1),
Z305_DELIVERY_TYPE	CHAR(1)                                                    ,            
Z305_HOLD_PERMISSION	CHAR(1)                                                  ,            
Z305_RENEW_PERMISSION	CHAR(1)                                                  ,            
Z305_RR_PERMISSION	CHAR(1)                                                    ,            
Z305_IGNORE_LATE_RETURN	CHAR(1)                                                ,            
Z305_LAST_ACTIVITY_DATE	NUMBER(8)                      ,            
Z305_PHOTO_CHARGE	CHAR(1)                              ,            
Z305_NO_LOAN	NUMBER(4)                                ,            
Z305_NO_HOLD	NUMBER(4)                                ,            
Z305_NO_PHOTO	NUMBER(4)                                ,            
Z305_NO_CASH	NUMBER(4)                         ,            
Z305_CASH_LIMIT	CHAR(10)                        ,            
Z305_CREDIT_DEBIT	CHAR(1)                       ,            
Z305_SUM	CHAR(10)                              ,            
Z305_DELINQ_1	NUMBER(2)                         ,            
Z305_DELINQ_N_1	VARCHAR2(200)                                  ,            
Z305_DELINQ_1_UPDATE_DATE	NUMBER(8)                            ,            
Z305_DELINQ_1_CAT_NAME	CHAR(10)                               ,            
Z305_DELINQ_2	NUMBER(2)                                        ,            
Z305_DELINQ_N_2	VARCHAR2(200)                                  ,            
Z305_DELINQ_2_UPDATE_DATE	NUMBER(8)                                ,            
Z305_DELINQ_2_CAT_NAME	CHAR(10)                                   ,            
Z305_DELINQ_3	NUMBER(2)                                            ,            
Z305_DELINQ_N_3	VARCHAR2(200)                                      ,            
Z305_DELINQ_3_UPDATE_DATE	NUMBER(8)                                ,            
Z305_DELINQ_3_CAT_NAME	CHAR(10)                                      ,            
Z305_FIELD_1	VARCHAR2(200)                                           ,            
Z305_FIELD_2	VARCHAR2(200)                                           ,            
Z305_FIELD_3	VARCHAR2(200)                                           ,            
Z305_HOLD_ON_SHELF	CHAR(1)                                           ,            
Z305_END_BLOCK_DATE	NUMBER(8)
);

create table njl50.Z308 (
Z308_REC_KEY	CHAR(27),
Z308_VERIFICATION	VARCHAR2(20),
Z308_VERIFICATION_TYPE	CHAR(2),
Z308_ID	CHAR(12),
Z308_STATUS	CHAR(2),
Z308_ENCRYPTION	CHAR(1)
);

create table njl50.Z31 (
z31_rec_key                               CHAR(27)     ,
z31_date_x                                CHAR(8)      ,            
z31_status                                CHAR(1)      ,            
z31_sub_library                           CHAR(5)      ,            
z31_alpha                                 CHAR(1)      ,           
z31_type                                  NUMBER(4)     ,             
z31_credit_debit                          CHAR(1)       ,             
z31_sum                                   CHAR(14)      ,       
z31_vat_sum                               CHAR(14)      ,       
z31_net_sum                               CHAR(14)      ,       
z31_payment_date_key                      CHAR(12)       ,      
z31_payment_cataloger                     CHAR(10)       ,      
z31_payment_target                        VARCHAR2(20)   ,      
z31_payment_ip                            VARCHAR2(20)   ,      
z31_payment_receipt_number                VARCHAR2(20)   ,      
z31_payment_mode                          CHAR(2)          ,    
z31_payment_identifier                    VARCHAR2(30)     ,    
z31_description                           VARCHAR2(100)    ,    
z31_key                                   VARCHAR2(100)    ,    
z31_key_type                              CHAR(10)         ,    
z31_transfer_department                   VARCHAR2(20)       ,  
z31_transfer_date                         NUMBER(8)          , 
z31_transfer_number                       CHAR(20)           ,       
z31_recall_transfer_status                CHAR(1)            ,       
z31_recall_transfer_date                  NUMBER(8)          ,       
z31_recall_transfer_number                VARCHAR2(20)         ,     
z31_related_z31_key                       CHAR(27)             ,     
z31_related_z31_key_type                  CHAR(2)            
);


CREATE TABLE "NJL50"."Z303" 
   (	"Z303_REC_KEY" CHAR(12), 
	"Z303_PROXY_FOR_ID" CHAR(12), 
	"Z303_PRIMARY_ID" VARCHAR2(12), 
	"Z303_NAME_KEY" CHAR(50), 
	"Z303_USER_LIBRARY" CHAR(5), 
	"Z303_OPEN_DATE" NUMBER(8,0), 
	"Z303_UPDATE_DATE" NUMBER(8,0), 
	"Z303_CON_LNG" CHAR(3), 
	"Z303_ALPHA" CHAR(1), 
	"Z303_NAME" VARCHAR2(200), 
	"Z303_TITLE" CHAR(10), 
	"Z303_DELINQ_1" NUMBER(2,0), 
	"Z303_DELINQ_N_1" VARCHAR2(200), 
	"Z303_DELINQ_1_UPDATE_DATE" NUMBER(8,0), 
	"Z303_DELINQ_1_CAT_NAME" CHAR(10), 
	"Z303_DELINQ_2" NUMBER(2,0), 
	"Z303_DELINQ_N_2" VARCHAR2(200), 
	"Z303_DELINQ_2_UPDATE_DATE" NUMBER(8,0), 
	"Z303_DELINQ_2_CAT_NAME" CHAR(10), 
	"Z303_DELINQ_3" NUMBER(2,0), 
	"Z303_DELINQ_N_3" VARCHAR2(200), 
	"Z303_DELINQ_3_UPDATE_DATE" NUMBER(8,0), 
	"Z303_DELINQ_3_CAT_NAME" CHAR(10), 
	"Z303_BUDGET" VARCHAR2(50), 
	"Z303_PROFILE_ID" VARCHAR2(12), 
	"Z303_ILL_LIBRARY" CHAR(5), 
	"Z303_HOME_LIBRARY" CHAR(5), 
	"Z303_FIELD_1" VARCHAR2(100), 
	"Z303_FIELD_2" VARCHAR2(100), 
	"Z303_FIELD_3" VARCHAR2(100), 
	"Z303_ILL_TOTAL_LIMIT" NUMBER(4,0), 
	"Z303_ILL_ACTIVE_LIMIT" NUMBER(4,0), 
	"Z303_DISPATCH_LIBRARY" CHAR(5), 
	"Z303_BIRTH_DATE" NUMBER(8,0), 
	"Z303_EXPORT_CONSENT" CHAR(1), 
	"Z303_PROXY_ID_TYPE" NUMBER(2,0), 
	"Z303_SEND_ALL_LETTERS" CHAR(1), 
	"Z303_PLIF_MODIFICATION" VARCHAR2(50)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;
 
  CREATE BITMAP INDEX "NJL50"."Z303_DELINQ_1_IDX" ON "NJL50"."Z303" ("Z303_DELINQ_1") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;
 
  CREATE UNIQUE INDEX "NJL50"."Z303_REC_KEY_IDX" ON "NJL50"."Z303" ("Z303_REC_KEY") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;

CREATE TABLE "NJL50"."Z304" 
   (	"Z304_REC_KEY" CHAR(14), 
	"Z304_ADDRESS" VARCHAR2(250), 
	"Z304_ZIP" CHAR(9), 
	"Z304_EMAIL_ADDRESS" VARCHAR2(60), 
	"Z304_TELEPHONE" VARCHAR2(30), 
	"Z304_DATE_FROM" NUMBER(8,0), 
	"Z304_DATE_TO" NUMBER(8,0), 
	"Z304_ADDRESS_TYPE" NUMBER(2,0), 
	"Z304_TELEPHONE_2" VARCHAR2(30), 
	"Z304_TELEPHONE_3" VARCHAR2(30), 
	"Z304_TELEPHONE_4" VARCHAR2(30), 
	"Z304_UPDATE_DATE" NUMBER(8,0), 
	"Z304_CAT_NAME" CHAR(10)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;
 
  CREATE UNIQUE INDEX "NJL50"."Z304_REC_KEY_IDX" ON "NJL50"."Z304" ("Z304_REC_KEY") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;



CREATE TABLE "NJL50"."Z305" 
   (	"Z305_REC_KEY" CHAR(17), 
	"Z305_OPEN_DATE" NUMBER(8,0), 
	"Z305_UPDATE_DATE" NUMBER(8,0), 
	"Z305_BOR_TYPE" CHAR(2), 
	"Z305_BOR_STATUS" CHAR(2), 
	"Z305_EXPIRY_DATE" NUMBER(8,0), 
	"Z305_NOTE" VARCHAR2(80), 
	"Z305_LOAN_PERMISSION" CHAR(1), 
	"Z305_PHOTO_PERMISSION" CHAR(1), 
	"Z305_OVER_PERMISSION" CHAR(1), 
	"Z305_MULTI_HOLD" CHAR(1), 
	"Z305_LOAN_CHECK" CHAR(1), 
	"Z305_DELIVERY_TYPE" CHAR(1), 
	"Z305_HOLD_PERMISSION" CHAR(1), 
	"Z305_RENEW_PERMISSION" CHAR(1), 
	"Z305_RR_PERMISSION" CHAR(1), 
	"Z305_IGNORE_LATE_RETURN" CHAR(1), 
	"Z305_LAST_ACTIVITY_DATE" NUMBER(8,0), 
	"Z305_PHOTO_CHARGE" CHAR(1), 
	"Z305_NO_LOAN" NUMBER(4,0), 
	"Z305_NO_HOLD" NUMBER(4,0), 
	"Z305_NO_PHOTO" NUMBER(4,0), 
	"Z305_NO_CASH" NUMBER(4,0), 
	"Z305_CASH_LIMIT" CHAR(10), 
	"Z305_CREDIT_DEBIT" CHAR(1), 
	"Z305_SUM" CHAR(10), 
	"Z305_DELINQ_1" NUMBER(2,0), 
	"Z305_DELINQ_N_1" VARCHAR2(200), 
	"Z305_DELINQ_1_UPDATE_DATE" NUMBER(8,0), 
	"Z305_DELINQ_1_CAT_NAME" CHAR(10), 
	"Z305_DELINQ_2" NUMBER(2,0), 
	"Z305_DELINQ_N_2" VARCHAR2(200), 
	"Z305_DELINQ_2_UPDATE_DATE" NUMBER(8,0), 
	"Z305_DELINQ_2_CAT_NAME" CHAR(10), 
	"Z305_DELINQ_3" NUMBER(2,0), 
	"Z305_DELINQ_N_3" VARCHAR2(200), 
	"Z305_DELINQ_3_UPDATE_DATE" NUMBER(8,0), 
	"Z305_DELINQ_3_CAT_NAME" CHAR(10), 
	"Z305_FIELD_1" VARCHAR2(200), 
	"Z305_FIELD_2" VARCHAR2(200), 
	"Z305_FIELD_3" VARCHAR2(200), 
	"Z305_HOLD_ON_SHELF" CHAR(1), 
	"Z305_END_BLOCK_DATE" NUMBER(8,0)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;
 
  CREATE BITMAP INDEX "NJL50"."Z305_BOR_STATUS_IDX" ON "NJL50"."Z305" ("Z305_BOR_STATUS") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;
 
  CREATE BITMAP INDEX "NJL50"."Z305_BOR_TYPE_IDX" ON "NJL50"."Z305" ("Z305_BOR_TYPE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;
 
  CREATE UNIQUE INDEX "NJL50"."Z305_REC_KEY_IDX" ON "NJL50"."Z305" ("Z305_REC_KEY") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;

CREATE TABLE "NJL50"."Z308" 
   (	"Z308_REC_KEY" CHAR(27), 
	"Z308_VERIFICATION" VARCHAR2(20), 
	"Z308_VERIFICATION_TYPE" CHAR(2), 
	"Z308_ID" CHAR(12), 
	"Z308_STATUS" CHAR(2), 
	"Z308_ENCRYPTION" CHAR(1)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;
 
  CREATE INDEX "NJL50"."Z308_ID_IDX" ON "NJL50"."Z308" ("Z308_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;
 
  CREATE UNIQUE INDEX "NJL50"."Z308_REC_KEY_IDX" ON "NJL50"."Z308" ("Z308_REC_KEY") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;


CREATE TABLE "NJL50"."Z31" 
   (	"Z31_REC_KEY" CHAR(27), 
	"Z31_DATE_X" CHAR(8), 
	"Z31_STATUS" CHAR(1), 
	"Z31_SUB_LIBRARY" CHAR(5), 
	"Z31_ALPHA" CHAR(1), 
	"Z31_TYPE" NUMBER(4,0), 
	"Z31_CREDIT_DEBIT" CHAR(1), 
	"Z31_SUM" CHAR(14), 
	"Z31_VAT_SUM" CHAR(14), 
	"Z31_NET_SUM" CHAR(14), 
	"Z31_PAYMENT_DATE_KEY" CHAR(12), 
	"Z31_PAYMENT_CATALOGER" CHAR(10), 
	"Z31_PAYMENT_TARGET" VARCHAR2(20), 
	"Z31_PAYMENT_IP" VARCHAR2(20), 
	"Z31_PAYMENT_RECEIPT_NUMBER" VARCHAR2(20), 
	"Z31_PAYMENT_MODE" CHAR(2), 
	"Z31_PAYMENT_IDENTIFIER" VARCHAR2(30), 
	"Z31_DESCRIPTION" VARCHAR2(100), 
	"Z31_KEY" VARCHAR2(100), 
	"Z31_KEY_TYPE" CHAR(10), 
	"Z31_TRANSFER_DEPARTMENT" VARCHAR2(20), 
	"Z31_TRANSFER_DATE" NUMBER(8,0), 
	"Z31_TRANSFER_NUMBER" CHAR(20), 
	"Z31_RECALL_TRANSFER_STATUS" CHAR(1), 
	"Z31_RECALL_TRANSFER_DATE" NUMBER(8,0), 
	"Z31_RECALL_TRANSFER_NUMBER" VARCHAR2(20), 
	"Z31_RELATED_Z31_KEY" CHAR(27), 
	"Z31_RELATED_Z31_KEY_TYPE" CHAR(2)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "UAS081009" ;

