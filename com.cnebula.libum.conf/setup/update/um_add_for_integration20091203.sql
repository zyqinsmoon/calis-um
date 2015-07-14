--用于集成增加的属性
ALTER table UM_Principle add userGroup NUMBER(10)
ALTER table UM_Principle add maxConcurrentNumber NUMBER(10)
ALTER table UM_Principle add background NUMBER(10)
ALTER table UM_Principle add directlyUser NUMBER(1) default 0
ALTER table UM_Principle add collegeName VARCHAR2(255)
ALTER table UM_Principle add departmentName VARCHAR2(255)
ALTER table UM_Principle add professionalName VARCHAR2(255)
ALTER table UM_Principle add stopReason VARCHAR2(255)