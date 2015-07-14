--是否985院校
ALTER TABLE UM_Organization add isNineEightFive VARCHAR2(10)  default 'noset';
--是否新升本院校
ALTER TABLE UM_Organization add isNewUpToUndergraduate VARCHAR2(10)  default 'noset';
--西部院校类型
ALTER TABLE UM_Organization add westCollegeType VARCHAR2(10)  default 'noset';