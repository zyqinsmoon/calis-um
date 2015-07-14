package com.cnebula.common.mail.service;
import java.util.Iterator;
import java.util.List;

import com.cnebula.common.mail.service.templateBean;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.EntityQuery;
import com.cnebula.common.ejb.manage.IEntityCRUDService;
import com.cnebula.common.str.SimpleStringTemplate;

@EasyService
public class TemplateService implements ITemplateService {
	@ESRef(target="(unit=#{dmpersistence})")
    IEntityCRUDService entityCRUDService;
	SimpleStringTemplate aa=new SimpleStringTemplate();
	String allshowType="text/html";
	public String convert(Object ob, String modelString) {
		String str="";
		  try{
			  str=aa.eval(ob, modelString);
		  }catch(Exception e)
		  {
			  str="error";
		  }
		  return str;
	}

	public String convert1(Object ob, String mid) {
		templateBean md;
		md=entityCRUDService.find(templateBean.class, mid);
		String modelString="ModelString is empty";
		try{
		modelString=md.getModelString();
		allshowType=md.getShowType();
		}catch(Exception e){
			
		}
		String str="";
		  try{
			  str=aa.eval(ob, modelString);
		  }catch(Exception e)
		  {
			  str="error";
		  }
		  return str;
	}

	public String convert2(Object ob, String functionid) {
		EntityQuery query = null;
		try{
			query = EntityQuery.filterRule2EntityQuery("functionid=\""+functionid+"\"");
			}catch(Exception e)
			{
				System.out.println(e);
				query=null;
			}
		List<templateBean> mds=entityCRUDService.query(templateBean.class,query);
		Iterator it=mds.iterator();
		templateBean md=null;
		String modelString="ModelString is empty!";
		try{
		md=(templateBean)it.next();
		modelString=md.getModelString();
		allshowType=md.getShowType();
		}catch(Exception e)
		{
		}
		String str="";
		  try{
			  str=aa.eval(ob, modelString);
		  }catch(Exception e)
		  {
			  str="error";
		  }
		  return str;
	}
	public String GetShowType(){
		return this.allshowType;
	}
	public String GetShowType1(){
		return this.allshowType;
	}
}
