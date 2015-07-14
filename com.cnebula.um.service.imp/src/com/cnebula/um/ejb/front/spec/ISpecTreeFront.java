package com.cnebula.um.ejb.front.spec;

import java.util.List;

import com.cnebula.common.ejb.manage.spec.SpecBaseInfo;
import com.cnebula.common.ejb.manage.spec.SpecDataItem;

@SuppressWarnings("unchecked")
public interface ISpecTreeFront {
	
	public List getAllSpecBaseInfo() ;
	
	public List getSpecDataItems(SpecBaseInfo specbaseinfo) ;
	
	public String addSpecDataInfo(SpecBaseInfo sbi) ;
		
	public String addSpecDataItem(SpecDataItem sdi) ;

	public SpecBaseInfo findSpecBaseInfo(String id);
	
	public SpecDataItem findItemByKey(String key) ;

	public void deleteItemByKey(String key);

	public void deleteItem(SpecDataItem item);

	public void modifyItem(SpecDataItem item);

	public void modifyBaseInfo(SpecBaseInfo sbi);

	public void deleteInfo(SpecBaseInfo info);
	
	public List getAllDataType() ;
}
