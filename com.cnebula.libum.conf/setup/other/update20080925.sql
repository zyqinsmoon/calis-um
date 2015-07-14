
CREATE TABLE Gen_IntegrateInfo (id VARCHAR2(255) NOT NULL, STATUS NUMBER(10) NULL, VERSION NUMBER(10) NULL, ENTITYCLSNAME VARCHAR2(255) NULL, ENTITYPROPERTITYITEMSTATUSDATA BLOB NULL, ENTITYID VARCHAR2(255) NULL, CREATETIME TIMESTAMP NULL, LASTMODIFYTIME TIMESTAMP NULL, LASTMODIFIER VARCHAR2(255) NULL, CREATOR VARCHAR2(255) NULL, DESCRIPTION VARCHAR2(255) NULL, LIFECYCLESTATUS NUMBER(5) NULL, PRIMARY KEY (id))
CREATE TABLE GEN_SINGLEINTEGRATESTATUS (ID VARCHAR2(255) NOT NULL, CREATETIMESTAMP TIMESTAMP NULL, DETAIL CLOB NULL, SYSTEM VARCHAR2(255) NULL, PARENTVERSION NUMBER(10) NULL, STATUS NUMBER(10) NULL, PARENT_id VARCHAR2(255) NULL, PRIMARY KEY (ID))
ALTER TABLE GEN_SINGLEINTEGRATESTATUS ADD CONSTRAINT GNSINGLEINTEGRATESTATUSPRENTid FOREIGN KEY (PARENT_id) REFERENCES Gen_IntegrateInfo (id)



--升级Operation，增加属性service和target
--ALTER TABLE PERM_Operation ADD  SERVICE VARCHAR2(255)
ALTER TABLE PERM_Operation ADD  TARGET VARCHAR2(255)



insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.IntegrateInfo', 0, 'igi', 0, 'nj-ums', 0)

--系统敏感权限扣除角色
insert into um_role(id, dtype, enabled, name, type, creator, lastmodifier, version, lifecyclestatus) values('$system_ex_role', 'Role', 1, '系统敏感权限扣除角色', 0, '$system', '$system', 1, 1)
insert into um_role_userrule(role_id, rule_id) values('$system_ex_role', '$anonymous_rule_u')
--押金管理不可更改和删除
insert into UM_PERMISSIONRULE (ID, ENABLED, OPERATIONS, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, CREATOR, LASTMODIFIER, DESCRIPTION, LIFECYCLESTATUS) values ('$admin_role_cash_ex_p', 1, 'update,delete,create', null, 2, '日志更改删除', 0, 0, 'com.cnebula.um.ejb.entity.statistics.LogItem', to_timestamp('07-10-2008 15:32:31.949000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('07-10-2008 15:33:21.070000', 'dd-mm-yyyy hh24:mi:ss.ff'), '$system', '$system', null, 4)
insert into UM_ROLE_PERMISSIONRULE (ROLE_ID, RULE_ID) values ('$system_ex_role', '$admin_role_cash_ex_p')
--日志仅仅可以查看,不能人为创建
insert into UM_PERMISSIONRULE (ID, ENABLED, OPERATIONS, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, CREATOR, LASTMODIFIER, DESCRIPTION, LIFECYCLESTATUS) values ('$admin_role_log_ex_p', 1, '', 'ops<={"update"} & ops != {"update.status"}', 1, '押金更改删除', 0, 0, 'com.cnebula.um.ejb.entity.usr.Cash', to_timestamp('07-10-2008 15:10:44.529000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('07-10-2008 15:10:44.529000', 'dd-mm-yyyy hh24:mi:ss.ff'), '$system', '$system', null, 4)
insert into UM_PERMISSIONRULE (ID, ENABLED, OPERATIONS, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, CREATOR, LASTMODIFIER, DESCRIPTION, LIFECYCLESTATUS) values ('$admin_role_log_ex_p2', 1, 'delete', null, 1, '押金更改删除', 0, 0, 'com.cnebula.um.ejb.entity.usr.Cash', to_timestamp('07-10-2008 15:10:44.529000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('07-10-2008 15:10:44.529000', 'dd-mm-yyyy hh24:mi:ss.ff'), '$system', '$system', null, 4)

insert into UM_ROLE_PERMISSIONRULE (ROLE_ID, RULE_ID) values ('$system_ex_role', '$admin_role_log_ex_p');
insert into UM_ROLE_PERMISSIONRULE (ROLE_ID, RULE_ID) values ('$system_ex_role', '$admin_role_log_ex_p2');

--增加数据同步操作记录
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$IntegrateInfo', '集成信息', 'com.cnebula.common.ejb.manage.IntegrateInfo',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)


insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus, service, target) values ('$synIntegrate', 'synIntegrate',  'com.cnebula.perm.Operation.Names',  'administrator', 'administrator', 1, 1, 'com.cnebula.common.ejb.manage.IAdvancedOperationService', '(unit=calisum)');

insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$synIntegrate_contraint', '$synIntegrate', 'status', 4, 1024, 1, 1024)

insert into perm_resourcetype_operation(res_id, ope_id) values ('$IntegrateInfo',	     '$synIntegrate'   )

--押金表
CREATE TABLE UM_Cash (id VARCHAR2(255) NOT NULL, VERSION NUMBER(10) NULL, VALUE NUMBER(19) NULL, STATUS NUMBER(10) NULL, TYPE NUMBER(10) NULL, LIFECYCLESTATUS NUMBER(5) NULL, CREATETIME TIMESTAMP NULL, CREATOR VARCHAR2(255) NULL, LASTMODIFYTIME TIMESTAMP NULL, DESCRIPTION VARCHAR2(255) NULL, LASTMODIFIER VARCHAR2(255) NULL, PRINCIPAL_id VARCHAR2(255) NULL, PRIMARY KEY (id))

alter table UM_CASH add receiptNumber varchar2(255)

ALTER TABLE UM_Cash ADD CONSTRAINT FK_UM_Cash_PRINCIPAL_id FOREIGN KEY (PRINCIPAL_id) REFERENCES UM_Principle (id)

ALTER TABLE UM_Principle  ADD totalCashValue NUMBER(10,2)


insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$Cash', '押金', 'com.cnebula.um.ejb.entity.usr.Cash',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$debitCash', 'debitCash',  'com.cnebula.perm.Operation.Names',  'administrator', 'administrator', 1, 1)

--退还押金约束
insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$debitCash_status_contraint', '$debitCash', 'status', 1, 1, 1, 2)

insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$create'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$debitCash'  )  
