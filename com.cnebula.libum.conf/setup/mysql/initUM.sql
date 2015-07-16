--该文件提供使用UM库需要初始化的数据库脚本
--最后更新时间：20090409

--创建表
CREATE TABLE UM_Resetpasswd (id VARCHAR(255) NOT NULL, ACTIVECODE VARCHAR(40) NOT NULL, NEWPASSWORD VARCHAR(40) NOT NULL, EMAILADDR VARCHAR(200) NOT NULL, RESETDATE DATE, NEWUSERBEAN_id VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE template (mid VARCHAR(255) NOT NULL, MODELSTRING VARCHAR(4000) NOT NULL, FILESTRING VARCHAR(200) NOT NULL, FUNCTIONID VARCHAR(20) NOT NULL, SHOWTYPE VARCHAR(20) NOT NULL, PRIMARY KEY (mid));
ALTER TABLE UM_Resetpasswd ADD TENANT_ID VARCHAR(255) NULL;

--update um_principle u set u.dtype='UMPrincipal' where u.id='$administrator'

--CALIS 用户管理系统 (资源类型);
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$UserManageService', 'CALIS用户管理系统', 'com.cnebula.um.service.IUserManageService','com.cnebula.perm.ResourceType.Names','$system', '$system', 1, 1);
--创建用户管理系统权限规则
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$UserManageService_rule_p', '用户管理服务权限规则', 'com.cnebula.um.service.IUserManageService',null,0,1,'','$system','$system',1,1);
--创建操作类型
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$registerUser', 'registerUser',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$activateNewUser', 'activateNewUser',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$checkLoginId', 'checkLoginId',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$checkEmail', 'checkEmail',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$checkAdditionalId', 'checkAdditionalId',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$checkCard', 'checkCard',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$resetPassword', 'resetPassword',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$activateNewPassword', 'activateNewPassword',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$updateUser', 'updateUser',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$getTopOrg', 'getTopOrg',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$isEnableSAAS', 'isEnableSAAS',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
	--changePassword操作之前已有
	--insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$changePassword', 'changePassword',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
--为 CALIS 用户管理系统添加新创建操作类型
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$registerUser');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$activateNewUser');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$checkLoginId');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$checkEmail');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$checkAdditionalId');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$checkCard');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$resetPassword');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$activateNewPassword');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$updateUser');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$getTopOrg');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$UserManageService','$isEnableSAAS');

--为匿名用户添加用户管理系统规则
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$UserManageService_rule_p');
--允许匿名用户创建状态为3的新用户(注册新用户);
insert into um_role (id, dtype, enabled, name, type, creator, lastmodifier,lifecyclestatus, umroleowner_id, umroleparent_id) values ('$register_role', 'Role', 1, '匿名用户注册角色', 1, '$system', '$system', 4, '$administrator', '$admin_role');
insert into um_permissionrule (id, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE,LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS) values ('$register_role_p', 'create', 1, 'status=3 | status=2', 1, '匿名用户注册规则', 0, 0, 'com.cnebula.um.ejb.entity.usr.UMPrincipal', '$system', '$system', '', 4);
insert into um_role_permissionrule (ROLE_ID, RULE_ID) values ('$register_role', '$register_role_p');
insert into um_role_userrule (ROLE_ID, RULE_ID) values ('$register_role', '$anonymous_rule_u');

insert into um_role (id, dtype, enabled, name, type, creator, lastmodifier,lifecyclestatus, umroleowner_id, umroleparent_id) values ('$user_update_role', 'Role', 1, '用户更新角色', 1, '$system', '$system', 4, '$administrator', '$admin_role');
insert into um_permissionrule (id, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE,LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS) values ('$user_update_role_p', 'update', 1, 'cu.id=r.id', 1, '更新自己权限规则', 0, 0, 'com.cnebula.um.ejb.entity.usr.UMPrincipal', '$system', '$system', '', 4);
insert into um_role_permissionrule (ROLE_ID, RULE_ID) values ('$user_update_role', '$user_update_role_p');
insert into um_role_userrule (ROLE_ID, RULE_ID) values ('$user_update_role', '$anonymous_rule_u');

--简单授权服务接口权限授予
insert into um_permissionrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS, TENANT_ID) values ('$ISimpleRoleService', '', 1, 'true', 1, 'ISimpleRoleService', 0, 0, 'com.cnebula.um.simple.permission.ISimpleRoleService',NULL, NULL, '$system', '$system', '', 4, null);
insert into um_role_permissionrule (ROLE_ID, RULE_ID) values ('$anonymous_role', '$ISimpleRoleService');

--实体展现提供者服务接口权限授予
insert into um_permissionrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS) values ('$IEntityUIInfoProvidergetEntityUIInfo', '', 1, 'true', 1, 'IEntityUIInfoProvidergetEntityUIInfo', 0, 0, 'com.cnebula.common.ejb.entity.ui.IEntityUIInfoProvider', NULL, NULL, '$system', '$system', '', 4);
insert into um_role_permissionrule (ROLE_ID, RULE_ID) values ('$anonymous_role', '$IEntityUIInfoProvidergetEntityUIInfo');

--用户数据同步相关接口权限授予
insert into um_permissionrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)values ('$IMultiSynTaskManager', '', 1, 'true', 1, '多馆数据同步', 0, 0, 'com.cnebula.um.service.IMultiSynTaskManager',NULL,NULL, 'administrator', 'administrator', '', 4);
insert into um_role_permissionrule (ROLE_ID, RULE_ID) values ('$anonymous_role', '$IMultiSynTaskManager');


--添加UM租客的资源类型
insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$UMTenant', 'UM租客', 'com.cnebula.um.ejb.saas.UMTenant',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1);
