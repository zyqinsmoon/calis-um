insert into um_userrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-ur-000004', '', 1, 'u.userType=1', 3, '学生用户', 0, 0, 'com.cnebula.um.ejb.entity.usr.UMPrincipal', '14-12月-09 04.09.36.687000 下午', '17-12月-09 11.20.29.500000 上午', 'administrator', 'administrator', '', 4);

insert into um_userrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-ur-000005', '', 1, 'u.userType=2', 5, '教师用户', 0, 0, 'com.cnebula.um.ejb.entity.usr.UMPrincipal', '14-12月-09 04.10.17.562000 下午', '18-12月-09 11.25.08.015000 上午', 'administrator', 'administrator', '', 4);

insert into um_userrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-ur-000001', '', 1, 'u.status=1', 9, '停用用户', 0, 0, 'com.cnebula.um.ejb.entity.usr.UMPrincipal', '09-12月-09 10.32.50.937000 上午', '21-12月-09 02.41.51.734000 下午', 'administrator', 'administrator', '', 4);

insert into um_userrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-ur-000002', '', 1, 'u.status=2', 3, '正常用户', 0, 0, 'com.cnebula.um.ejb.entity.usr.UMPrincipal', '09-12月-09 10.33.32.218000 上午', '17-12月-09 11.20.34.468000 上午', 'administrator', 'administrator', '', 4);

insert into um_userrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('zj-ums-ur-000003', '', 1, '(u.invalidDate<curdate)|(u.validDate>curdate)', 5, '过期用户', 0, 0, 'com.cnebula.um.ejb.entity.usr.UMPrincipal', '09-12月-09 10.43.30.375000 上午', '18-12月-09 11.25.03.500000 上午', 'administrator', 'administrator', '', 4);

insert into um_userrule (ID, OPERATIONS, ENABLED, EXPRESSION, VERSION, NAME, TYPE, AUTHABLE, ENTITYLIMITTYPE, CREATETIME, LASTMODIFYTIME, LASTMODIFIER, CREATOR, DESCRIPTION, LIFECYCLESTATUS)
values ('$limit_org_own_user_rule', '', 1, 'u.organization.id=cu.organization.id&u.userType>9000', 2, '限制为自己馆的用户规则', 0, 0, 'com.cnebula.um.ejb.entity.usr.UMPrincipal', '17-12月-09 11.35.03.609000 上午', '21-12月-09 12.30.42.578000 下午', 'administrator', 'administrator', '', 4);

