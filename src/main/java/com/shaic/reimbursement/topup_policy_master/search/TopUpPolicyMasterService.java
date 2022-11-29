package com.shaic.reimbursement.topup_policy_master.search;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.TopUpPolicy;
import com.shaic.domain.Policy;

@Stateless
public class TopUpPolicyMasterService {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public TopUpPolicyMasterService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public List<TopUpPolicy> getTempPolicyNo(String policyNo){
		Query  query = entityManager.createNamedQuery("TopUpPolicy.findByPolicyNo");
		query = query.setParameter("policyNo", policyNo);
		List<TopUpPolicy> listOfTmpCodes = query.getResultList();
		
		return listOfTmpCodes;
	}
	
	public TopUpPolicy getTopUpPolicyObj(String policyNo){
		Query  query = entityManager.createNamedQuery("TopUpPolicy.findByPolicyNo");
		query = query.setParameter("policyNo", policyNo);
		List<TopUpPolicy> listOfTmpCodes = query.getResultList();
		
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
	
	public void submitTopUpPolicy(List<TopUpPolicyMasterTableDTO> dto) {
		for (TopUpPolicyMasterTableDTO idaTableDTO : dto) {
			TopUpPolicy tempPolicyNo = getTopUpPolicyObj(idaTableDTO.getPolicyNo());
					if(tempPolicyNo == null) {
						tempPolicyNo = new TopUpPolicy();
						tempPolicyNo.setPolicyNo(idaTableDTO.getPolicyNo());
						if(idaTableDTO.getEnable() != null && idaTableDTO.getEnable()) {
							tempPolicyNo.setActiveStatus(String.valueOf(1));
						} else if(idaTableDTO.getDisable() != null && idaTableDTO.getDisable()) {
							tempPolicyNo.setActiveStatus(String.valueOf(0));
						}
						tempPolicyNo.setRemarks(idaTableDTO.getRemarks());
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
						tempPolicyNo.setRemarks(idaTableDTO.getRemarks());
						entityManager.merge(tempPolicyNo);
					}
					
					entityManager.flush();
					entityManager.clear();
		}
		
	}
}
