package com.shaic.reimbursement.investigationin_direct_assigment.search;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.claim.cpuskipzmr.SkipZMRListenerTableDTO;
import com.shaic.domain.Policy;
import com.shaic.domain.TempPolicyNo;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.skipZMR.CPUStageMapping;

@Stateless
public class InvestigationDirectAssignmentService {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public InvestigationDirectAssignmentService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public List<TempPolicyNo> getTempPolicyNo(String policyNo){
		Query  query = entityManager.createNamedQuery("TempPolicyNo.findByCode");
		query = query.setParameter("policyNo", policyNo);
		List<TempPolicyNo> listOfTmpCodes = query.getResultList();
		
		return listOfTmpCodes;
	}
	
	public TempPolicyNo getInvestigationByPass(String policyNo){
		Query  query = entityManager.createNamedQuery("TempPolicyNo.findByCode");
		query = query.setParameter("policyNo", policyNo);
		List<TempPolicyNo> listOfTmpCodes = query.getResultList();
		
		if(listOfTmpCodes != null && !listOfTmpCodes.isEmpty()){
			return listOfTmpCodes.get(0);
		}
		return null;
	}
	
	
	public List<Policy> getPolicyNo(String policyNo){
		Query  query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
		query = query.setParameter("policyNumber", policyNo);
		List<Policy> listOfPolicyNo = query.getResultList();
		
		return listOfPolicyNo;
	}
	
	

	public void submitIDA(List<InvestigationDirectAssignmentTableDTO> dto) {
		for (InvestigationDirectAssignmentTableDTO idaTableDTO : dto) {
			TempPolicyNo tempPolicyNo = getInvestigationByPass(idaTableDTO.getPolicyNo());
					if(tempPolicyNo == null) {
						tempPolicyNo = new TempPolicyNo();
						tempPolicyNo.setPolicyNo(idaTableDTO.getPolicyNo());
						if(idaTableDTO.getEnable() != null && idaTableDTO.getEnable()) {
							tempPolicyNo.setActiveStatus(String.valueOf(1));
						} else if(idaTableDTO.getDisable() != null && idaTableDTO.getDisable()) {
							tempPolicyNo.setActiveStatus(String.valueOf(0));
						}
						tempPolicyNo.setCreatedBy(idaTableDTO.getUserName());
						tempPolicyNo.setCreatedDate(new Date());
						
						entityManager.persist(tempPolicyNo);
								
					} else {
						tempPolicyNo.setModifyBy(idaTableDTO.getUserName());
						tempPolicyNo.setModifiedDate(new Date());
						
						if(idaTableDTO.getEnable() != null && idaTableDTO.getEnable()){
							tempPolicyNo.setActiveStatus(String.valueOf(1));
						}else if(idaTableDTO.getDisable() != null && idaTableDTO.getDisable()){
							tempPolicyNo.setActiveStatus(String.valueOf(0));
						}
						
						entityManager.merge(tempPolicyNo);
					}
					
					entityManager.flush();
					entityManager.clear();
		}
		
	}
}
