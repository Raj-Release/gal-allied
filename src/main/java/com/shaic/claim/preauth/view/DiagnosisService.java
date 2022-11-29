package com.shaic.claim.preauth.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.domain.MasAilmentLimit;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.ViewTmpDiagnosis;

@Stateless
public class DiagnosisService extends AbstractDAO<PedValidation> {

	@EJB
	PreAuthService preAuthService;
	
	public DiagnosisService() {
		super();
	}
	
	public List<DiagnosisDTO> setMapper(List<PedValidation> resultList){		
		List<DiagnosisDTO> searchPedQueryTableDTO = DiagnosisMapper.getInstance()
				.getDiagnosisDTO(resultList);
		return searchPedQueryTableDTO;		
	}

	public List<PedValidation> search(Long preAuthKey) {	
		

		List<PedValidation> resultList = new ArrayList<PedValidation>();
		
		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", preAuthKey);
		
		resultList = (List<PedValidation>)query.getResultList();
	    
		return resultList;

	}
	
	public ClaimLimit getClaimLimit(Long sublimitId){
		Query claimLimitQuery = entityManager.createNamedQuery("ClaimLimit.findByKey");
    	claimLimitQuery.setParameter("limitKey",sublimitId);
		
		ClaimLimit claimLimit = (ClaimLimit)claimLimitQuery.getSingleResult();
		
		return claimLimit;
	}
	
	
	public List<DiagnosisDetailsTableDTO> getDiagnosisList(Long intimationKey){
		
		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", intimationKey);
		
		List<PedValidation> results = (List<PedValidation>) query.getResultList();
		List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
		for (PedValidation pedValidation : results) {
			DiagnosisDetailsTableDTO dto = new DiagnosisDetailsTableDTO();
			Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey", pedValidation.getDiagnosisId());
			Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
			
		    Long sublimitId = pedValidation.getSublimitId();
		    
		    pedValidation.getSumInsuredRestrictionId();
		    
		    if(sublimitId != null){
		    	
		    	Query claimLimitQuery = entityManager.createNamedQuery("ClaimLimit.findByKey");
		    	claimLimitQuery.setParameter("limitKey",sublimitId);
				
				ClaimLimit claimLimit = (ClaimLimit)claimLimitQuery.getSingleResult();
				
				dto.setSublimitNameValue(claimLimit.getLimitName());
				
				if(claimLimit.getMaxPerPolicyPeriod() != null){
				dto.setSublimitAmt(claimLimit.getMaxPerPolicyPeriod().toString());
				}
				
			    if(pedValidation.getSumInsuredRestrictionId() != null){
			    	MastersValue master = getMaster(pedValidation.getSumInsuredRestrictionId());
			    	if(master != null && master.getValue() != null){
			    	dto.setSumInsuredRestrictionValue(Integer.valueOf(master.getValue()));
			    	}
			    }
				
		    }
			
			dto.setDiagnosis(masters.getValue());
			if(pedValidation.getSubLimitApplicable() != null){
				if(pedValidation.getSubLimitApplicable().equals("Y")){
				dto.setSublimitApplicableValue("Yes");
				}
				else{
					dto.setSublimitApplicableValue("No");
				}
			}
		   if(pedValidation.getConsiderForPayment() != null){
			   if(pedValidation.getConsiderForPayment().equals("Y")){
				   dto.setConsiderForPaymentValue("Yes");
			   }
			   else
			   {
				   dto.setConsiderForPaymentValue("No");
			   }
		   }
		   diagnosisList.add(dto);
			
		}
		return diagnosisList;
		
		
	}
	
	public List<DiagnosisDetailsTableDTO> getDiagnosisListFromTmpDiagnosis(Long intimationKey){
		
		Query query = entityManager.createNamedQuery("ViewTmpDiagnosis.findByTransactionKey");
		query.setParameter("transactionKey", intimationKey);
		
		List<ViewTmpDiagnosis> results = (List<ViewTmpDiagnosis>) query.getResultList();
		List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
		for (ViewTmpDiagnosis pedValidation : results) {
			DiagnosisDetailsTableDTO dto = new DiagnosisDetailsTableDTO();
			Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey", pedValidation.getDiagnosisId());
			Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
			
		    Long sublimitId = pedValidation.getSublimitId();
		    
		    pedValidation.getSumInsuredRestrictionId();
		    
		    if(sublimitId != null){
		    	
		    	
		    	if(pedValidation.getPolicy() != null && pedValidation.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(pedValidation.getPolicy().getProduct().getKey())){
		    		Query claimLimitQuery = entityManager.createNamedQuery("MasAilmentLimit.findByKey");
			    	claimLimitQuery.setParameter("limitKey",sublimitId);
			    	
			    	List<MasAilmentLimit> claimLimit = (List<MasAilmentLimit>)claimLimitQuery.getResultList();
					
			    	if(claimLimit != null && !claimLimit.isEmpty()){
			    		dto.setSublimitNameValue(claimLimit.get(0).getAilment());
						
						if(claimLimit.get(0).getLimitPerEye() != null){
						dto.setSublimitAmt(claimLimit.get(0).getLimitPerEye().toString());
						}
			    	}
		    		
		    	}
		    	else{
		    		Query claimLimitQuery = entityManager.createNamedQuery("ClaimLimit.findByKey");
			    	claimLimitQuery.setParameter("limitKey",sublimitId);
					
					ClaimLimit claimLimit = (ClaimLimit)claimLimitQuery.getSingleResult();
					
					dto.setSublimitNameValue(claimLimit.getLimitName());
					
					if(claimLimit.getMaxPerPolicyPeriod() != null){
					dto.setSublimitAmt(claimLimit.getMaxPerPolicyPeriod().toString());
					}
					
				   
		    	}
		    	
		    	 if(pedValidation.getSumInsuredRestrictionId() != null){
				    	MastersValue master = getMaster(pedValidation.getSumInsuredRestrictionId());
				    	if(master != null && master.getValue() != null){
				    	dto.setSumInsuredRestrictionValue(Integer.valueOf(master.getValue()));
				    	}
				    }
		    	
		    }
			
			dto.setDiagnosis(masters.getValue());
			dto.setIcdChapterKey(pedValidation.getIcdChpterId());
			dto.setIcdBlockKey(pedValidation.getIcdBlockId());
			dto.setIcdCodeKey(pedValidation.getIcdCodeId());
			if(pedValidation.getSubLimitApplicable() != null){
				if(pedValidation.getSubLimitApplicable().equals("Y")){
				dto.setSublimitApplicableValue("Yes");
				}
				else{
					dto.setSublimitApplicableValue("No");
				}
			}
		   if(pedValidation.getConsiderForPayment() != null){
			   if(pedValidation.getConsiderForPayment().equals("Y")){
				   dto.setConsiderForPaymentValue("Yes");
			   }
			   else
			   {
				   dto.setConsiderForPaymentValue("No");
			   }
		   }
		   if(pedValidation.getPedImpactId() != null){
			      dto.setPedImpactOnDiagnosis(new SelectValue(pedValidation.getPedImpactId().getKey(), pedValidation.getPedImpactId().getValue()));			   
		   }   
		   
		   if(pedValidation.getNotPayingReason() != null){
			      dto.setReasonForNotPaying(new SelectValue(pedValidation.getNotPayingReason().getKey(), pedValidation.getNotPayingReason().getValue()));			   
		   }
		 //added for prmiary diagnosis CR changes
			if(pedValidation.getPrimaryDiagnosis()!=null && pedValidation.getPrimaryDiagnosis().equalsIgnoreCase("Y"))
			{
				dto.setPrimaryDiagnosis(true);
			}
			else
			{
				dto.setPrimaryDiagnosis(null);
			}
		   
		   diagnosisList.add(dto);
			
		}
		return diagnosisList;
		
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMaster(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		MastersValue a_mastersValue = new MastersValue();
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue;
	}
	
	@SuppressWarnings("unchecked")
	public List<PedDetailsTableDTO> getPedValidationList(Long rodKey){
		
		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", rodKey);
		
		List<PedValidation> pedValidation = (List<PedValidation>)query.getResultList();
		
		List<PedDetailsTableDTO> pedDetailsTableDTO = new ArrayList<PedDetailsTableDTO>();
		
		for (PedValidation pedValidation2 : pedValidation) {
			PedDetailsTableDTO dto = new PedDetailsTableDTO();
			if(null != pedValidation2.getDiagnosisId()){
				Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
				diagnosis.setParameter("diagnosisKey", pedValidation2.getDiagnosisId());
				Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
				if(masters != null){
					dto.setDiagnosisName(masters.getValue());
				}
			}
			dto.setPolicyAgeing(pedValidation2.getPolicyAging());
			Query ped = entityManager.createNamedQuery("DiagnosisPED.findByPEDValidationKey");
			ped.setParameter("pedValidationKey", pedValidation2.getKey());
			List<DiagnosisPED> diagnosisPED = (List<DiagnosisPED>)ped.getResultList();
			StringBuffer pedName = new StringBuffer();
			StringBuffer impactName = new StringBuffer();
			StringBuffer exclusion = new StringBuffer();
			for (DiagnosisPED diagnosisPED2 : diagnosisPED) {
				if(null != diagnosisPED2.getPedName()){
					pedName.append(":").append(diagnosisPED2.getPedName());
				}
				if(null != diagnosisPED2.getDiagonsisImpact()){
					impactName.append("").append(diagnosisPED2.getDiagonsisImpact().getValue());
				}
				if(null != diagnosisPED2.getExclusionDetails()){
					exclusion.append(":").append(diagnosisPED2.getExclusionDetails().getExclusion());
				}
				dto.setRemarks(diagnosisPED2.getDiagnosisRemarks());
			}
			dto.setPedName(pedName.toString());
			dto.setImpactOnDiagnosisValue(impactName.toString());
			dto.setExclusionDetailsValue(exclusion.toString());	
			if(null != pedValidation2.getCopayPercentage()){
				dto.setCoPayValue(pedValidation2.getCopayPercentage().toString());
			}
			
			pedDetailsTableDTO.add(dto);
	
		}
		
		return pedDetailsTableDTO;
		
	}

	@Override
	public Class<PedValidation> getDTOClass() {
		return PedValidation.class;
	}

}
