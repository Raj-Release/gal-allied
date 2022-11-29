package com.shaic.claim.cvc.auditreport;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.domain.MasReportConfig;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;

@Stateless
public class ClaimsAuditReportService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public String getReportDuration(String reportName){
		Query  query = entityManager.createNamedQuery("MasReportConfig.findbyName");
		query = query.setParameter("reportName", reportName);
		List<MasReportConfig> listOfObj = query.getResultList();
		
		if(listOfObj != null && !listOfObj.isEmpty()){
			return listOfObj.get(0).getReportDuration();
		}
		return null;
	}

}
