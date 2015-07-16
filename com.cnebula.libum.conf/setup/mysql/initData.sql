-- 标识生成相关
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.perm.ResourceType', 0, 'rt',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.perm.Operation',    0, 'op',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.perm.DelegateAuth', 0, 'da',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.spec.SpecDataItem', 0, 'sdi', 0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.spec.I18nValue',    0, 'iv',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.spec.DataType',     0, 'dt',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.spec.SpecBaseInfo', 0, 'sbi', 0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.statistics.LogItem',    0, 'li',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.env.IPSegment',         0, 'ips', 0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.env.IPRange',           0, 'ipr', 0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.perm.Application',      0, 'app', 0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.perm.GeneralResource',  0, 'gr',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.perm.Role',             0, 'rl',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.perm.LegacyRole',       0, 'lrl', 0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.perm.UserRule',         0, 'ur',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.perm.PermissionRule',   0, 'pr',  0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.Organization',      0, 'org', 0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.UMPrincipal',       0, 'ump', 0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.NewUserBean',       0, 'nub', 0, #{ums}, 0)
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.AdditionalId',      0, 'uai', 0, #{ums}, 0)    
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.Contactor',         0, 'ctr', 0, #{ums}, 0)    
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.AuthCenter',        0, 'acr', 0, #{ums}, 0)    
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.Card',              0, 'cd',  0, #{ums}, 0)    
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.UserApplyForm',     0, 'uaf', 0, #{ums}, 0)    
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.um.ejb.entity.usr.Personality',       0, 'pny', 0, #{ums}, 0)    

-- 根角色和admin用户

insert into um_principle(id, usertype, name, loginId, localLoginId , password , sex, msgtype, status, validdate, invaliddate, creator, lastmodifier, version, lifecyclestatus, dtype, photo) values ('$administrator', 3, '超级管理员', 'administrator', 'administrator', 'administrator', 'm', 4, 2,  str_to_date( '10050701', '%Y%m%d' ),  str_to_date( '30050701', '%Y%m%d' ), '$system', '$system', 1, 1, 'UMPrincipal',#{admin_photo})

insert into um_permissionrule(id, name, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$admin_rule_p', '系统超级管理员权限规则', 'true',  0, 1, '' , '$system', '$system', 1, 1)
insert into um_userrule(id, name, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$admin_rule_u', '系统超级管理员用户规则', 'u.id="$administrator"',  0, 1, '', '$system', '$system', 1, 1)
insert into um_role(id, dtype, enabled, name, type, creator, lastmodifier, version, lifecyclestatus) values('$admin_role', 'Role', 1, '系统超级管理员角色', 1, '$system', '$system', 1, 1)
insert into um_role_userrule(role_id, rule_id) values('$admin_role', '$admin_rule_u')
insert into um_role_permissionrule(role_id, rule_id) values('$admin_role','$admin_rule_p')

--匿名用户角色
--insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$anonymous_rule_p', '系统匿名用户权限规则', 'com.cnebula.common.remote.core.RemoteServiceAccessPermission', 'name="com.cnebula.common.security.auth.ILoginService" | name="com.cnebula.common.ejb.manage.IEntityCRUDService" | name="com.cnebula.common.i18n.I18nRemoteAccessService | name="com.cnebula.common.security.auth.IAuthorizationService" | name="com.cnebula.common.remote.IRemoteServiceDiscoverer"',  0, 1, '', '$system', '$system', 1, 1)
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$loginservice_rule_p', '登录服务权限规则', 'com.cnebula.common.security.auth.ILoginService', null,  0, 1, '', '$system', '$system', 1, 1)
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$crudservice_rule_p', '实体访问服务权限规则', 'com.cnebula.common.ejb.manage.IEntityCRUDService', null,  0, 1, '', '$system', '$system', 1, 1)
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$1i8nremote_rule_p', '远程国际化服务权限规则', 'com.cnebula.common.i18n.I18nRemoteAccessService', null,  0, 1, '', '$system', '$system', 1, 1)
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$authservice_rule_p', '授权服务权限规则', 'com.cnebula.common.security.auth.IAuthorizationService', null,  0, 1, '', '$system', '$system', 1, 1)
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$remotediscover_rule_p', '远程服务发现权限规则', 'com.cnebula.common.remote.IRemoteServiceDiscoverer', null,  0, 1, '', '$system', '$system', 1, 1)
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$webconfigservice_rule_p', 'web配置查询服务', 'com.cnebula.um.client.service.WebSiteConfigService', null,  0, 1, '', '$system', '$system', 1, 1)
--认证的校验接口
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$loginvalidatorservice_rule_p', '认证校验服务', 'com.cnebula.common.security.auth.ILoginValidateService', null,  0, 1, '', '$system', '$system', 1, 1)
insert into um_userrule(id, name, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$anonymous_rule_u', '系统匿名用户规则', 'true',  0, 1, '', '$system', '$system', 1, 1)
insert into um_role(id, umroleowner_id, umroleparent_id, dtype, enabled, name, type, creator, lastmodifier, version, lifecyclestatus) values('$anonymous_role', '$administrator', '$admin_role', 'Role', 1, '系统最低权限用户角色', 1,  '$system', '$system', 1, 1)
insert into um_role_userrule(role_id, rule_id) values('$anonymous_role', '$anonymous_rule_u')
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$loginservice_rule_p')
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$crudservice_rule_p')
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$1i8nremote_rule_p')
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$authservice_rule_p')
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$remotediscover_rule_p')
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$webconfigservice_rule_p')
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$loginvalidatorservice_rule_p')
--任意用户共有权限角色
--修改自身密码权限


--匿名用户同步权限
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$MultiSynManager_rule_p', '多馆同步任务服务', 'com.cnebula.um.service.IMultiSynTaskManager', null,  0, 1, '', '$system', '$system', 1, 1)
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$MultiSynManager_rule_p')

--扣权防止用户创建非法的角色（uMRoleOwner != u）
--insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$roleCreate_rule_p', '角色创建限制权限规则', 'com.cnebula.um.ejb.entity.perm.LegacyRole', 'uMRoleOwner != u',  0, 1, 'create,update', '$system', '$system', 1, 1)
--insert into um_userrule(id, name, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$roleCreate_rule_u', '角色创建限制用户规则', 'true',  0, 1, '', '$system', '$system', 1, 1)
--insert into um_role(id, umroleowner_id, umroleparent_id, dtype, enabled, name, type, creator, lastmodifier, version, lifecyclestatus) values('$roleCreate_role', '$administrator', '$admin_role', 'Role', 1, '角色创建限制角色', 0,  '$system', '$system', 1, 1)
--insert into um_role_userrule(role_id, rule_id) values('$roleCreate_role', '$roleCreate_rule_u')
--insert into um_role_permissionrule(role_id, rule_id) values('$roleCreate_role','$roleCreate_rule_p')


-- 资源类型和操作
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$ResourceType', '资源类型', 'com.cnebula.common.ejb.manage.perm.ResourceType',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$Operation', '操作', 'com.cnebula.common.ejb.manage.perm.Operation',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$OperationFieldContraint', '操作字段约束', 'com.cnebula.common.ejb.manage.perm.OperationFieldContraint',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$IPSegment', 'IP段', 'com.cnebula.um.ejb.entity.env.IPSegment',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$IPRange', 'IP范围', 'com.cnebula.um.ejb.entity.env.IPRange',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$LogItem', '日志', 'com.cnebula.um.ejb.entity.statistics.LogItem',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$Application', '应用系统', 'com.cnebula.um.ejb.entity.perm.Application',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$GeneralResource', '资源', 'com.cnebula.um.ejb.entity.perm.GeneralResource',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$Role', '角色', 'com.cnebula.um.ejb.entity.perm.Role',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$LegacyRole', '应用系统角色', 'com.cnebula.um.ejb.entity.perm.LegacyRole',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$PermissionRule', '权限过滤规则', 'com.cnebula.um.ejb.entity.perm.PermissionRule',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$UserRule', '用户过滤规则', 'com.cnebula.um.ejb.entity.perm.UserRule',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)

insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$Organization', '机构', 'com.cnebula.um.ejb.entity.usr.Organization',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$UMPrincipal', '用户', 'com.cnebula.um.ejb.entity.usr.UMPrincipal',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$AdditionalId', '附加标识', 'com.cnebula.um.ejb.entity.usr.AdditionalId',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$Contactor', '联系人', 'com.cnebula.um.ejb.entity.usr.Contactor',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$AuthCenter', '认证中心', 'com.cnebula.um.ejb.entity.usr.AuthCenter',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$Card', '图书卡', 'com.cnebula.um.ejb.entity.usr.Card',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$Personality', '个性化', 'com.cnebula.um.ejb.entity.usr.Personality',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)

insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$LoginService', '登录服务', 'com.cnebula.common.security.auth.ILoginService',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$DelegateAuth', '代理认证', 'com.cnebula.common.ejb.manage.perm.DelegateAuth',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)


--CALIS 应用系统类型
insert into perm_resoucetype(id,name,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$CALIS_RLS', 'CALIS资源调度',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$CALIS_Portal', 'CALIS门户',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$CALIS_usp', 'CALIS统一检索',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$CALIS_ILL', 'CALIS馆际互借',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$CALIS_URS', 'CALIS联合资源仓储',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$CALIS_CVRS', 'CALIS虚拟参考系统',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$CALIS_Local_ETD', 'CALIS学位论文本地系统',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)
insert into perm_resoucetype(id,name,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$General_System', '普通应用系统',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1)

insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$view', 'view',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$update', 'update',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$delete', 'delete',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$create', 'create',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$reportLoss', 'reportLoss',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)

insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$stop', 'stop',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$activate', 'activate',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$verify', 'verify',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)

insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$cancelReportLoss', 'cancelReportLoss',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$loginByNamePassword', 'loginByNamePassword',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$loginByIp', 'loginByIp',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$logout', 'logout',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$logAnalyse', 'logAnalyse',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$changePassword', 'changePassword',  'com.cnebula.perm.Operation.Names',  'administrator', 'administrator', 1, 1)

--挂失解挂操作约束
insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$reportLoss_status_contraint', '$reportLoss', 'status', 1, 2, 1, 3)
insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$cancelReportLoss_status_contraint', '$cancelReportLoss', 'status', 1, 3, 1, 2)

--修改密码的操作约束
insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$changePassword_input_contraint', '$changePassword', 'password', 6, 'itemAt(copts, "input.oldPassword")', 6, 'itemAt(copts, "input.newPassword")')
insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$changePassword_user_contraint', '$changePassword', 'id', 6, 'u.id', 7, '')

--用户停用启用审核约束
insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$activate_status_contraint', '$activate', 'status', 1, 1, 1, 2)
insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$stop_status_contraint', '$stop', 'status', 1, 2, 1, 1)
insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$verify_status_contraint', '$verify', 'status', 1, 3, 1, 2)

insert into perm_resourcetype_operation(res_id, ope_id) values ('$LoginService',	     '$loginByNamePassword'   )
insert into perm_resourcetype_operation(res_id, ope_id) values ('$LoginService',	     '$loginByIp'   )
insert into perm_resourcetype_operation(res_id, ope_id) values ('$LoginService',	     '$logout'   )

insert into perm_resourcetype_operation(res_id, ope_id) values ('$ResourceType',	     '$view'   )
insert into perm_resourcetype_operation(res_id, ope_id) values ('$ResourceType',       '$update' )
insert into perm_resourcetype_operation(res_id, ope_id) values ('$ResourceType',       '$delete' )
insert into perm_resourcetype_operation(res_id, ope_id) values ('$ResourceType',       '$create' )


insert into perm_resourcetype_operation(res_id, ope_id) values ('$Operation', 			 '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Operation', 	     '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Operation', 	     '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Operation', 	     '$create'  )  

   				
insert into perm_resourcetype_operation(res_id, ope_id) values ('$OperationFieldContraint',		  '$view'     )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$OperationFieldContraint',	    '$update'   )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$OperationFieldContraint',	    '$delete'   )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$OperationFieldContraint',	    '$create'   )  

 
insert into perm_resourcetype_operation(res_id, ope_id) values ('$IPSegment',            '$view'      )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$IPSegment',            '$update'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$IPSegment',            '$delete'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$IPSegment',            '$create'    )  

       
insert into perm_resourcetype_operation(res_id, ope_id) values ('$IPRange',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$IPRange',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$IPRange',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$IPRange',  '$create'  )  

insert into perm_resourcetype_operation(res_id, ope_id) values ('$LogItem',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$LogItem',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$LogItem',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$LogItem',  '$create'  )  

insert into perm_resourcetype_operation(res_id, ope_id) values ('$Application', '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Application', '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Application', '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Application', '$create'  )  


insert into perm_resourcetype_operation(res_id, ope_id) values ('$GeneralResource',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$GeneralResource',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$GeneralResource',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$GeneralResource',  '$create'  )  

insert into perm_resourcetype_operation(res_id, ope_id) values ('$Role',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Role',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Role',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Role',  '$create'  )  


insert into perm_resourcetype_operation(res_id, ope_id) values ('$LegacyRole',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$LegacyRole',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$LegacyRole',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$LegacyRole',  '$create'  )  


insert into perm_resourcetype_operation(res_id, ope_id) values ('$PermissionRule',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$PermissionRule',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$PermissionRule',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$PermissionRule',  '$create'  )  

insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserRule',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserRule',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserRule',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserRule',  '$create'  )  


insert into perm_resourcetype_operation(res_id, ope_id) values ('$Organization', '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Organization', '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Organization', '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Organization', '$create'  )  


insert into perm_resourcetype_operation(res_id, ope_id) values ('$UMPrincipal',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UMPrincipal',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UMPrincipal',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UMPrincipal',  '$create'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UMPrincipal',  '$changePassword'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UMPrincipal',  '$activate'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UMPrincipal',  '$stop'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UMPrincipal',  '$verify'  )  


insert into perm_resourcetype_operation(res_id, ope_id) values ('$AdditionalId', '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$AdditionalId', '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$AdditionalId', '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$AdditionalId', '$create'  )  


insert into perm_resourcetype_operation(res_id, ope_id) values ('$Contactor',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Contactor',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Contactor',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Contactor',  '$create'  )  


insert into perm_resourcetype_operation(res_id, ope_id) values ('$AuthCenter',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$AuthCenter',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$AuthCenter',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$AuthCenter',  '$create'  )  

insert into perm_resourcetype_operation(res_id, ope_id) values ('$Card',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Card',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Card',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Card',  '$create'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Card',  '$reportLoss'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Card',  '$cancelReportLoss'  )  

insert into perm_resourcetype_operation(res_id, ope_id) values ('$Personality',  '$view'    )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Personality',  '$update'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Personality',  '$delete'  )  
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Personality',  '$create'  )  


--20090317添加
insert into seq_entityidgenerator(ENTITYNAME, VERSION, ENTITYPREFIX, DIRTY, SYSTEMPREFIX, SEQUENCE) values('com.cnebula.common.ejb.manage.IntegrateInfo', 0, 'igi', 0, 'nj-ums', 0)

--系统敏感权限扣除角色
insert into um_role(id, dtype, enabled, name, type, creator, lastmodifier, version, lifecyclestatus) values('$system_ex_role', 'Role', 1, '系统敏感权限扣除角色', 0, '$system', '$system', 1, 1)
insert into um_role_userrule(role_id, rule_id) values('$system_ex_role', '$anonymous_rule_u')
--押金管理不可更改和删除
insert into UM_PERMISSIONRULE (ID, ENABLED, OPERATIONS, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, CREATOR, LASTMODIFIER, DESCRIPTION, LIFECYCLESTATUS) values ('$admin_role_cash_ex_p', 1, 'update,delete,create', null, 2, '日志更改删除', 0, 0, 'com.cnebula.um.ejb.entity.statistics.LogItem', timestamp('2008-10-07 15:32:31.529000'), timestamp('2008-10-07 15:33:21.529000'), '$system', '$system', null, 4)
insert into UM_ROLE_PERMISSIONRULE (ROLE_ID, RULE_ID) values ('$system_ex_role', '$admin_role_cash_ex_p')

--历史卡只可创建查看，不可修改删除
insert into UM_PERMISSIONRULE (ID, ENABLED, OPERATIONS, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, CREATOR, LASTMODIFIER, DESCRIPTION, LIFECYCLESTATUS) values ('$admin_role_hcard_ex_p', 1, 'update,delete', null, 2, '历史卡更改删除', 0, 0, 'com.cnebula.um.ejb.entity.usr.HistoryCard', timestamp('2008-10-07 15:32:31.529000'), timestamp('2008-10-07 15:33:21.529000'), '$system', '$system', null, 4)
insert into UM_ROLE_PERMISSIONRULE (ROLE_ID, RULE_ID) values ('$system_ex_role', '$admin_role_hcard_ex_p')


--日志仅仅可以查看,不能人为创建
insert into UM_PERMISSIONRULE (ID, ENABLED, OPERATIONS, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, CREATOR, LASTMODIFIER, DESCRIPTION, LIFECYCLESTATUS) values ('$admin_role_log_ex_p', 1, '', 'ops<={"update"} & ops != {"update.status"}', 1, '押金更改删除', 0, 0, 'com.cnebula.um.ejb.entity.usr.Cash', timestamp('2008-10-07 15:10:44.529000'), timestamp('2008-10-07 15:10:44.529000'), '$system', '$system', null, 4)
insert into UM_PERMISSIONRULE (ID, ENABLED, OPERATIONS, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, CREATOR, LASTMODIFIER, DESCRIPTION, LIFECYCLESTATUS) values ('$admin_role_log_ex_p2', 1, 'delete', null, 1, '押金更改删除', 0, 0, 'com.cnebula.um.ejb.entity.usr.Cash', timestamp('2008-10-07 15:10:44.529000'), timestamp('2008-10-07 15:10:44.529000'), '$system', '$system', null, 4)

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

--

