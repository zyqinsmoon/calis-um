insert into um_permissionrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-pr-000000', '', 1, '!((r.name="com.cnebula.common.security.auth.ILoginService")|(r.name="com.cnebula.common.ejb.manage.IEntityCRUDService")|(r.name="com.cnebula.common.i18n.I18nRemoteAccessService")|(r.name="com.cnebula.um.service.IConfigAssistService"))', 18, '��Ȩֻ�ܵ�½', 0, 0, '', '08-12��-09 05.21.26.750000 ����', '18-12��-09 11.10.30.625000 ����', 'administrator', 'administrator', '', 4);

insert into um_permissionrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-pr-000001', '', 1, '', 1, '���ݵ������', 0, 1, 'com.cnebula.um.service.DatabaseSynTaskManager', '08-12��-09 05.46.01.531000 ����', '08-12��-09 05.46.01.531000 ����', 'administrator', 'administrator', '', 4);

insert into um_permissionrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-pr-000002', 'getRoleList,getUserRuleList,getBlackRoleList,getBalckUserRuleList', 1, 'u.organization.id=cu.organization.id', 3, '��Ȩ������', 0, 0, 'com.cnebula.um.simple.permission.ISimpleRole', '09-12��-09 05.51.38.500000 ����', '17-12��-09 11.23.42.750000 ����', 'administrator', 'administrator', '', 4);

insert into um_permissionrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-pr-000004', '', 1, 'true', 6, 'ͣ�����й���', 0, 0, '', '17-12��-09 02.27.48.000000 ����', '17-12��-09 02.41.57.546000 ����', 'administrator', 'administrator', '', 4);

insert into um_permissionrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-pr-000005', '', 1, 'true', 1, '���Կ�Ȩ����', 0, 0, '', '18-12��-09 11.06.17.281000 ����', '18-12��-09 11.06.17.281000 ����', 'administrator', 'administrator', '', 4);

insert into um_permissionrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-pr-000006', '', 1, '!((r.name="com.cnebula.common.security.auth.ILoginService")|(r.name="com.cnebula.common.ejb.manage.IEntityCRUDService")|(r.name="com.cnebula.common.i18n.I18nRemoteAccessService")|(r.name="com.cnebula.um.service.IConfigAssistService"))', 2, '(��Ȩ)��¼ʹ�� ������', 0, 0, '', '21-12��-09 10.21.42.234000 ����', '21-12��-09 10.46.22.593000 ����', 'administrator', 'administrator', '', 4);

