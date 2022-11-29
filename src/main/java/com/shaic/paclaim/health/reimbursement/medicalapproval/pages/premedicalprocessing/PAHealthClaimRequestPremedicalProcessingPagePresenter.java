package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.premedicalprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAHealthClaimRequestPremedicalProcessingPageInterface.class)
public class PAHealthClaimRequestPremedicalProcessingPagePresenter extends AbstractMVPPresenter<PAHealthClaimRequestPremedicalProcessingPageInterface> {
	private static final long serialVersionUID = 3892478510257767753L;

	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	
	public static final String GET_EXCLUSION_DETAILS = "pa_health_medical_approval_claim_request_get_exclusion_details";
	public static final String MEDICAL_APPROVAL_PRE_MEDICAL_PROCESSING_SETUP_REFERNCE = "pa_health_medical_approval_claim_request_prrmedical_processing_setup_reference";
	public static final String ZONAL_REVIEW_SUGGEST_QUERY_EVENT = "pa_health_medical_approval_claim_request_suggest_query_event";
	public static final String ZONAL_REVIEW_SUGGEST_REJECTION_EVENT = "pa_health_medical_approval_claim_request_suggest_reject_event";
	public static final String ZONAL_REVIEW_SUGGEST_APPROVAL_EVENT = "pa_health_medical_approval_claim_request_suggest_approval_event";
	
	public static final String ZONAL_REVIEW_SUM_INSURED_CALCULATION = "pa_health_medical_approval_claim_request_row_by_row_calculation";
	public void getExclusionDetails(@Observes @CDIEvent(GET_EXCLUSION_DETAILS) final ParameterDTO parameters)
	{
		Long impactDiagnosisKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		
		view.setExclusionDetails(icdCodeContainer);
	}
	
	
	public void setUpReference(
			@Observes @CDIEvent(MEDICAL_APPROVAL_PRE_MEDICAL_PROCESSING_SETUP_REFERNCE) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("exclusionDetails",masterService.getSelectValueContainer(ReferenceTable.EXCLUSION_DETAILS));
		referenceData.put("procedureStatus",masterService.getSelectValueContainer(ReferenceTable.PROCEDURE_STATUS));
		referenceData.put("pedExclusionImpactOnDiagnosis",masterService.getSelectValueContainer(ReferenceTable.IMPACT_ON_DIAGNOSI));
		referenceData.put("medicalCategory", masterService.getSelectValueContainer(ReferenceTable.MEDICAL_CATEGORY));
		referenceData.put("copay", getCopayValues(preauthDTO));
		view.setupReferences(referenceData);
	}
	
	private BeanItemContainer<SelectValue> getCopayValues(PreauthDTO dto) {
		 BeanItemContainer<SelectValue> coPayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		 	List<String> coPayPercentage = new ArrayList<String>();
		    for (Double string : dto.getProductCopay()) {
		    	coPayPercentage.add(String.valueOf(string.intValue()));
			}
		    
		    Long i = 0l;
		    for (String string : coPayPercentage) {
		    	SelectValue value = new SelectValue();
		    	value.setId(Long.valueOf(string));
		    	value.setValue(string);
		    	coPayContainer.addBean(value);
		    	i++;
			}
		    
		    return coPayContainer;
	}
	
	public void generateFieldsForQuery(
			@Observes @CDIEvent(ZONAL_REVIEW_SUGGEST_QUERY_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnQueryClick();
	}

	public void generateFieldsForSuggestRejection(
			@Observes @CDIEvent(ZONAL_REVIEW_SUGGEST_REJECTION_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnSuggestRejectionClick();
	}

	public void generateFieldsForSuggestApproval(
			@Observes @CDIEvent(ZONAL_REVIEW_SUGGEST_APPROVAL_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnSuggestApprovalClick();
	}
	
	public void getValuesForMedicalDecisionTable(
			@Observes @CDIEvent(ZONAL_REVIEW_SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
		 	Map<String, Object> values = (Map<String, Object>) parameters.getPrimaryParameter();
		 	DiagnosisProcedureTableDTO dto = (DiagnosisProcedureTableDTO) parameters.getSecondaryParameters()[0];
			String diagnosis = null;
			PreauthDTO preauthDTO = (PreauthDTO) parameters.getSecondaryParameter(1, PreauthDTO.class);
			if(values.containsKey("diagnosisId")) {
				diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
			}
			
			if (dto.getDiagnosisDetailsDTO() != null) {
				dto.getDiagnosisDetailsDTO()
						.setDiagnosis(diagnosis);
			}
			
			Map<String, Object> medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValue(values,preauthDTO.getNewIntimationDTO());
			
			view.getValuesForMedicalDecisionTable(dto, medicalDecisionTableValues);
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
