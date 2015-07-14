--用于共享域中心增加的属性
ALTER table UM_Principle add localRegisted NUMBER(1) default 1
ALTER table UM_Principle add firstLoginTime DATE
ALTER table UM_Principle add hasILLaccount NUMBER(1) default 0
ALTER table UM_Principle add userLocalID VARCHAR2(255)
ALTER table UM_Principle add calisID VARCHAR2(255)
