--开放卡类型押金映射关系配置服务
insert into um_permissionrule(id, name, entitylimittype, expression, type, enabled, operations, creator, lastmodifier, version, lifecyclestatus) values('$configAssist_rule_p', '辅助配置服务', 'com.cnebula.um.service.IConfigAssistService', null,  0, 1, '', '$system', '$system', 1, 1)
insert into um_role_permissionrule(role_id, rule_id) values('$anonymous_role','$configAssist_rule_p')
