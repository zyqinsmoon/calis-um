package com.cnebula.um.setup;

import java.util.Map;

import org.osgi.service.component.ComponentContext;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.ejb.manage.IdGeneratorService;
import com.cnebula.common.jdbc.SqlTaskService;

@EasyService
public class UMServerDBInit implements ISetupService {

	@ESRef(target="(id=TemplateSqlTaskImp)")
	SqlTaskService sqlTaskService;
	
	@ESRef(target="(unit=#{umunit})")
	IdGeneratorService idGeneratorService;
	
	boolean finishSetup = false;
	
	protected void activate(ComponentContext context){
		Map<String, Object> rt = sqlTaskService.exec("com.cnebula.um.setup.UMServerDBInit");
		if (Boolean.TRUE.equals(rt.get("initDB")) ) {
			idGeneratorService.refresh();
		}
		finishSetup = true;
	}

	public boolean isFinishSetup() {
		return finishSetup;
	}

	public void setFinishSetup(boolean finishSetup) {
		this.finishSetup = finishSetup;
	}
	
}
