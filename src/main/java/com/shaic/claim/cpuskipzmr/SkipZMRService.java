package com.shaic.claim.cpuskipzmr;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.skipZMR.CPUStageMapping;


@Stateless
public class SkipZMRService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SkipZMRService() {
		super();
	}
	
	public List<TmpCPUCode> getMasCpuCode(Long cpuCode){
		Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		List<TmpCPUCode> listOfTmpCodes = query.getResultList();
		
		return listOfTmpCodes;
	}

	public void submitSkipZMR(List<SkipZMRListenerTableDTO> dtoList) {
		for (SkipZMRListenerTableDTO skipZMRListenerTableDTO : dtoList) {
			CPUStageMapping cpuMapping = new CPUStageMapping();
			cpuMapping.setCpuCode( Long.valueOf(skipZMRListenerTableDTO.getCpuCode()));
			cpuMapping.setCashlessFlag(skipZMRListenerTableDTO.getSkipZMRForCashless() ? "Y" : "N");
			cpuMapping.setReimbursementFlag(skipZMRListenerTableDTO.getSkipZMRForReimbursement() ? "Y" : "N");
			cpuMapping.setActiveStatus(1l);
			
			if(skipZMRListenerTableDTO.getKey() != null) {
				cpuMapping.setKey(skipZMRListenerTableDTO.getKey());
			}
			
			if(cpuMapping.getKey() != null) {
				cpuMapping.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				cpuMapping.setModifiedBy(skipZMRListenerTableDTO.getUserName());
				entityManager.merge(cpuMapping);
			} else {
				cpuMapping.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				cpuMapping.setCreatedBy(skipZMRListenerTableDTO.getUserName());
				entityManager.persist(cpuMapping);
			}
			entityManager.flush();
			entityManager.clear();
		}
		
	}
	
	
	 public List<CPUStageMapping> getStageMappingByCpuCode(Long cpuCode) {
	    	//Boolean shouldSkipZMR = false;
			Query query = entityManager.createNamedQuery("CPUStageMapping.findByCPUCode");
			query = query.setParameter("cpuCode", cpuCode);
			List<CPUStageMapping> mappingList = query.getResultList();
			if(null != mappingList && !mappingList.isEmpty()) {
				return mappingList;
			}
			return null;

		}
	
}
