--对已经更新表结构的进行记录的填补

alter table UM_CASH add receiptNumber varchar2(255)

insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.IntegrateInfo', 0, 'igi', 0, 'nj-ums', 0)

--系统敏感权限扣除角色
insert into um_role(id, dtype, enabled, name, type, creator, lastmodifier, version, lifecyclestatus) values('$system_ex_role', 'Role', 1, '系统敏感权限扣除角色', 0, '$system', '$system', 1, 1)
insert into um_role_userrule(role_id, rule_id) values('$system_ex_role', '$anonymous_rule_u')
--押金管理不可更改和删除
insert into UM_PERMISSIONRULE (ID, ENABLED, OPERATIONS, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, CREATOR, LASTMODIFIER, DESCRIPTION, LIFECYCLESTATUS) values ('$admin_role_cash_ex_p', 1, 'update,delete,create', null, 2, '日志更改删除', 0, 0, 'com.cnebula.um.ejb.entity.statistics.LogItem', to_timestamp('07-10-2008 15:32:31.949000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('07-10-2008 15:33:21.070000', 'dd-mm-yyyy hh24:mi:ss.ff'), '$system', '$system', null, 4)
insert into UM_ROLE_PERMISSIONRULE (ROLE_ID, RULE_ID) values ('$system_ex_role', '$admin_role_cash_ex_p')

--历史卡只可创建查看，不可修改删除
insert into UM_PERMISSIONRULE (ID, ENABLED, OPERATIONS, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, CREATOR, LASTMODIFIER, DESCRIPTION, LIFECYCLESTATUS) values ('$admin_role_hcard_ex_p', 1, 'update,delete', null, 2, '历史卡更改删除', 0, 0, 'com.cnebula.um.ejb.entity.usr.HistoryCard', to_timestamp('07-10-2008 15:32:31.949000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('07-10-2008 15:33:21.070000', 'dd-mm-yyyy hh24:mi:ss.ff'), '$system', '$system', null, 4)
insert into UM_ROLE_PERMISSIONRULE (ROLE_ID, RULE_ID) values ('$system_ex_role', '$admin_role_hcard_ex_p')


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



insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$Cash', '押金', 'com.cnebula.um.ejb.entity.usr.Cash',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$debitCash', 'debitCash',  'com.cnebula.perm.Operation.Names',  'administrator', 'administrator', 1, 1)

--退还押金约束
insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$debitCash_status_contraint', '$debitCash', 'status', 1, 1, 1, 2)

insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$create'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Cash',  '$debitCash'  )  


--开放卡类型押金映射关系配置服务
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$configAssist_rule_p', '辅助配置服务', 'com.cnebula.um.service.IConfigAssistService', null,  0, 1, '', '$system', '$system', 1, 1)
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$configAssist_rule_p')


--退卡操作
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$revokeCard', 'revokeCard',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Card',  '$revokeCard'  )  

insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$revokeCard_status_contraint', '$revokeCard', 'status', 1, 2, 1, 0)

--历史卡标识 权限
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.HistoryCard', 0, 'hcd', 0, 'nj-ums', 0)    
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$HistoryCard', '历史卡', 'com.cnebula.um.ejb.entity.usr.HistoryCard',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resourcetype_operation(res_id, ope_id) values ('$HistoryCard',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$HistoryCard',  '$create'  )  
