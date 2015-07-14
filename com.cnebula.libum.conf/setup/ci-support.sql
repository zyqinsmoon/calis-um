--提供持续集成需要调用相关服务的权限脚本
--最后更新时间：20090408

insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$PlatformAdminService', '平台管理服务', 'com.cnebula.platform.admin.IPlatformStatusService',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$pacheckstatus', 'checkFrameworkStatus',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_resourcetype_operation(res_id, ope_id) values ('$PlatformAdminService',  '$pacheckstatus');
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$platformAdmin_rule_p', '平台管理服务权限规则', 'com.cnebula.platform.admin.IPlatformStatusService', null,  0, 1, '', '$system', '$system', 1, 1);
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$platformAdmin_rule_p');


insert into perm_resoucetype(id,name, entitytype,namespace, creator,lastmodifier,version, lifecyclestatus) values ('$esUnitTestClient', 'ES单元测试服务', 'com.cnebula.junit.IESUnitTestClient',  'com.cnebula.perm.ResourceType.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$testBundles', 'testBundles',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$testAll', 'testAll',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1);
insert into perm_resourcetype_operation(res_id, ope_id) values ('$esUnitTestClient',  '$testBundles');
insert into perm_resourcetype_operation(res_id, ope_id) values ('$esUnitTestClient',  '$testAll');
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$esUnitTest_rule_p', 'ES单元测试服务权限规则', 'com.cnebula.junit.IESUnitTestClient', null,  0, 1, '', '$system', '$system', 1, 1);
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$esUnitTest_rule_p');