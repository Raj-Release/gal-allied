package com.shaic.claim.preauth.wizard.pages;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthQuery;

@Stateless
public class PreAuthViewQueryDetailsService extends AbstractDAO<PreauthQuery> {
	
	private Long intimationKey;
	private Long preAuthKey;
	private Long policykey;
	private Long hospitalKey;
	
	@Inject
	private PreAuthViewQueryDetailsPageDTO preAuthViewQueryDetailsPageDTO;
	
	public PreAuthViewQueryDetailsService(){
		super();
	}
	
	public PreAuthViewQueryDetailsPageDTO search(PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO){
		Long key = preAuthPreviousQueryDetailsTableDTO.getKey();
		
		Query findPreAuthQueryElement = entityManager
				.createNamedQuery("PreauthQuery.findKey").setParameter("preAuthKey", key);

		PreauthQuery preAuthQueryElement = (PreauthQuery) findPreAuthQueryElement.getSingleResult();
		
		preAuthKey = preAuthQueryElement.getPreauth().getKey();		
		
		Query findPreAuthElement = entityManager
				.createNamedQuery("Preauth.findByKey").setParameter("preauthKey", preAuthKey);
		
		Preauth preAuthElement = (Preauth) findPreAuthElement.getSingleResult();
		
		preAuthViewQueryDetailsPageDTO.setDateOfAdmission(preAuthElement.getDataOfAdmission().toString());
		
		intimationKey = preAuthElement.getIntimation().getKey();
		
		Query findIntimationElement = entityManager
				.createNamedQuery("Intimation.findByKey").setParameter("intiationKey", intimationKey);
		
		Intimation intimation  = (Intimation) findIntimationElement.getSingleResult();
		
		preAuthViewQueryDetailsPageDTO.setIntimationNo(intimation.getIntimationId());
		preAuthViewQueryDetailsPageDTO.setInsuredPatientName(intimation.getInsuredPatientName());
		
		policykey = preAuthElement.getPolicy().getKey();
		
		Query findPolicyElement = entityManager
				.createNamedQuery("Policy.findByKey").setParameter("policyKey", policykey);
		
		Policy policy  = (Policy) findPolicyElement.getSingleResult();
		
		preAuthViewQueryDetailsPageDTO.setPolicyNo(policy.getPolicyNumber());
		preAuthViewQueryDetailsPageDTO.setProductName(policy.getProduct().getValue());		
		
		hospitalKey = preAuthElement.getIntimation().getHospital();
		
		Query findHospitalElement = entityManager
				.createNamedQuery("Hospitals.findByKey").setParameter("key", hospitalKey);
		
		Hospitals hospital  = (Hospitals) findHospitalElement.getSingleResult();
		
		preAuthViewQueryDetailsPageDTO.setHospitalName(hospital.getName());
		preAuthViewQueryDetailsPageDTO.setHospitalCity(hospital.getCity());
		preAuthViewQueryDetailsPageDTO.setHospitalType(hospital.getHospitalType().getValue());
		
		preAuthViewQueryDetailsPageDTO.setQueryRaisedDate(preAuthQueryElement.getCreatedDate().toString());
		preAuthViewQueryDetailsPageDTO.setQueryRemarks(preAuthQueryElement.getQueryRemarks());
		preAuthViewQueryDetailsPageDTO.setQueryStatus(preAuthQueryElement.getStatus().getProcessValue());		
		return preAuthViewQueryDetailsPageDTO;
	}

	@Override
	public Class<PreauthQuery> getDTOClass() {
		return PreauthQuery.class;
	}

}
