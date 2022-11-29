package com.shaic.domain;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class CPUCodeService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<TmpCPUCode>  getTmpCpuCodes() {
		
		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>)findAll.getResultList();
		BeanItemContainer<TmpCPUCode> cpuCodeContainer = new BeanItemContainer<TmpCPUCode>(
				TmpCPUCode.class);
		cpuCodeContainer.addAll(resultCPUCodeList);
				
		return cpuCodeContainer;
		
	}

}
