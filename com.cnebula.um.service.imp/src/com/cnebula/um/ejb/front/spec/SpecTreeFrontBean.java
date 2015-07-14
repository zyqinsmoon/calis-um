package com.cnebula.um.ejb.front.spec;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.cnebula.common.annotations.es.ESRef;
import com.cnebula.common.annotations.es.EasyService;
import com.cnebula.common.annotations.es.TxType;
import com.cnebula.common.ejb.manage.spec.SpecBaseInfo;
import com.cnebula.common.ejb.manage.spec.SpecDataItem;

@SuppressWarnings("unchecked")
@EasyService(txType=TxType.CMT, immediate=true)
public class SpecTreeFrontBean implements ISpecTreeFront {
	
	@ESRef(target="(unit=#{umunit})")
	EntityManager em;
	
	public SpecDataItem findItemByKey(String key) {
		String sql = "select sdi from SpecDataItem sdi where sdi.key=:key";
		Query q = em.createQuery(sql);
		q.setParameter("key", key);
		List<?> qList = (List<?>) q.getResultList();
		if (qList.isEmpty()) {
			return null;
		} else {
			return (SpecDataItem) qList.get(0);
		}
	}

	public void deleteInfo(SpecBaseInfo info){
		info = em.merge(info) ;
		em.remove(info) ;
	}
	
	public void deleteItem(SpecDataItem item) {
		SpecDataItem si = em.find(SpecDataItem.class,item.getId()) ; 
		SpecDataItem parent = si.getParent() ;
		SpecBaseInfo root = si.getRoot() ;
		si.setParent(null) ;
		si.setRoot(null) ;
		if (parent != null) {
			parent.getChildren().remove(si);
		}
		if (root != null) {
			root.getDataItems().remove(si);
		}
		em.remove(si) ;
		if (parent != null) {
			em.merge(parent);
		}
		if (root != null) {
			em.merge(root);
		}
	}
	
	public void modifyItem(SpecDataItem item ){
		em.merge(item);
	}
	
	public void modifyBaseInfo(SpecBaseInfo sbi){
		em.merge(sbi) ;
	}
	
	public String addSpecDataInfo(SpecBaseInfo spi) {
		spi = em.merge(spi) ;
		em.persist(spi);
		spi = em.merge(spi) ;
		return "addSpecDataInfo";
	}

	
	
	public String addSpecDataItem(SpecDataItem sdi) {
		if (sdi.getParent() != null) {
			em.merge(sdi.getParent());
		}
		if (sdi.getRoot() != null) {
			em.merge(sdi.getRoot());
		}
		em.persist(sdi) ;
		return "addSpecDataItem";
	}
	
	
	public SpecBaseInfo findSpecBaseInfo(String id){
		return (SpecBaseInfo)em.find(SpecBaseInfo.class, id)  ;
	}
	
	
	
	public List getAllSpecBaseInfo() {
		String sql  = "select sbi from SpecBaseInfo sbi" ;
		Query q  = em.createQuery(sql) ;
		List rl =  q.getResultList() ;
		return rl;
	}

	public List getSpecDataItems(SpecBaseInfo specbaseinfo) {

		return null;
	}

	public void deleteItemByKey(String key) {
		
	}

	public List getAllDataType() {
		String sql = "select d from DataType d";
		Query q = em.createQuery(sql);
		List l = q.getResultList();
		return l;
	}


}
