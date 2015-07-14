--退卡操作
insert into perm_operation(id, name, namespace, creator, lastmodifier, version, lifecyclestatus) values ('$revokeCard', 'revokeCard',  'com.cnebula.perm.Operation.Names',  '$system', '$system', 1, 1)
insert into perm_resourcetype_operation(res_id, ope_id) values ('$Card',  '$revokeCard'  )  

insert into perm_operationfieldcontraint(id, operation_id, fieldname, fromtype, fromvalue, totype, tovalue) values('$revokeCard_status_contraint', '$revokeCard', 'status', 1, 2, 1, 0)
