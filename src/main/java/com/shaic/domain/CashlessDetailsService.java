package com.shaic.domain;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.dto.CashlessDetailsDto;

@Stateless
public class CashlessDetailsService {

	@PersistenceContext
	protected EntityManager entityManager;

	@Inject
	private CashlessDetailsDto cashlessDetailsDto;

	public CashlessDetailsService() {
		super();
	}

	@SuppressWarnings("unchecked")
	public CashlessDetailsDto getCashlessDetails(Long intimationKey) {
		Query query = entityManager
				.createNamedQuery("Preauth.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		cashlessDetailsDto.setTotalAuthAmt("");
		cashlessDetailsDto.setStatusOfCashless("");
		
		int size = singleResult.size() - 1;
		if (size >= 0) {
			entityManager.refresh(singleResult.get(singleResult.size() - 1));
			if (singleResult.get(singleResult.size() - 1).getStatus() != null) {
				cashlessDetailsDto
						.setStatusOfCashless(singleResult
								.get(singleResult.size() - 1).getStatus()
								.getProcessValue() != null ? singleResult
								.get(singleResult.size() - 1).getStatus()
								.getProcessValue() : "");
			}
			for (int index = size; index >= 0; index--) {
				entityManager.refresh(singleResult.get(index));
				if (singleResult.get(index).getTotalApprovalAmount() != null
						&& !singleResult.get(index).getTotalApprovalAmount()
								.equals("")) {
					cashlessDetailsDto.setTotalAuthAmt(singleResult.get(index)
							.getTotalApprovalAmount().toString());
					if (singleResult.get(singleResult.size() - 1).getStatus() != null) {
						cashlessDetailsDto
								.setStatusOfCashless(singleResult
										.get(singleResult.size() - 1).getStatus()
										.getProcessValue() != null ? singleResult
										.get(singleResult.size() - 1).getStatus()
										.getProcessValue() : "");
					}
					break;
				}
			}
		}

		Query diagnosisQquery = entityManager
				.createNamedQuery("PedValidation.findByIntimationKey");
		diagnosisQquery.setParameter("intimationKey", intimationKey);
		List<PedValidation> diagnosisResult = (List<PedValidation>) diagnosisQquery
				.getResultList();

		String diagnosisName = "";

		for (PedValidation pedValidation : diagnosisResult) {
			Query diagnosisNameQquery = entityManager
					.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosisNameQquery.setParameter("diagnosisKey",
					pedValidation.getDiagnosisId());
			List<Diagnosis> diagnosisNameResult = (List<Diagnosis>) diagnosisNameQquery
					.getResultList();
			for (Diagnosis diagnosis : diagnosisNameResult) {
				diagnosisName = diagnosisName.toString().replaceFirst(
						diagnosis.getValue() + ", ", "");
				diagnosisName = diagnosisName + diagnosis.getValue() + ", ";				
			}
		}
		
		if(diagnosisName.length()>0){
			diagnosisName = diagnosisName.substring(0, diagnosisName.length()-2);
		}

		cashlessDetailsDto
				.setAilment(!diagnosisName.toString().equals("") ? diagnosisName
						.toString() : "");

		return cashlessDetailsDto;
	}

}
