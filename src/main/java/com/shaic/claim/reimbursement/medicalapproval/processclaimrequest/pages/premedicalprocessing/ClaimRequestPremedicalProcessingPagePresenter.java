package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing;

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

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(ClaimRequestPremedicalProcessingPageInterface.class)
public class ClaimRequestPremedicalProcessingPagePresenter extends AbstractMVPPresenter<ClaimRequestPremedicalProcessingPageInterface> {
	private static final long serialVersionUID = 3892478510257767753L;

	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	
	public static final String GET_EXCLUSION_DETAILS = "medical_approval_claim_request_get_exclusion_details";
	public static final String MEDICAL_APPROVAL_PRE_MEDICAL_PROCESSING_SETUP_REFERNCE = "medical_approval_claim_request_prrmedical_processing_setup_reference";
	public static final String ZONAL_REVIEW_SUGGEST_QUERY_EVENT = "medical_approval_claim_request_suggest_query_event";
	public static final String ZONAL_REVIEW_SUGGEST_REJECTION_EVENT = "medical_approval_claim_request_suggest_reject_event";
	public static final String ZONAL_REVIEW_SUGGEST_APPROVAL_EVENT = "medical_approval_claim_request_suggest_approval_event";
	public static final String REFERENCE_DATA_CLEAR = "clear reference data premedical processing page";
	
	public static final String CLAIM_REQUEST_APPROVE_EVENT = "claim_request_apporve_event_premedical_processing_page";
	
	public static final String CLAIM_CANCEL_ROD_EVENT      =  "claim_request_cancel_rod_premedical_processing_page";  
	
	public static final String CLAIM_REQUEST_QUERY_BUTTON_EVENT = "claim_request_query_button_event_premedical_processing_page";

	public static final String CLAIM_REQUEST_REJECTION_EVENT = "claim_request_rejection_button_event_premedical_processing_page";

	public static final String CLAIM_REQUEST_ESCALATE_EVENT = "claim_request_escalte_button_event_premedical_processing_page";

	public static final String CLAIM_REQUEST_ESCALATE_REPLY_EVENT = "claim_request_escalte_reply_event_premedical_processing_page";

	public static final String CLAIM_REQUEST_REFERCOORDINATOR_EVENT = "claim_request_refer_to_coordinator_event_premedical_processing_page";

	public static final String CLAIM_REQUEST_SPECIALIST_EVENT = "claim_request_specialist_event_premedical_processing_page";

	public static final String CLAIM_REQUEST_SENT_TO_REPLY_EVENT = "claim_request_sent_to_reply_event_premedical_processing_page";

	
	public static final String ZONAL_REVIEW_SUM_INSURED_CALCULATION = "medical_approval_claim_request_row_by_row_calculation";
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
		referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON));
		view.setupReferences(referenceData);
	}
	
	private BeanItemContainer<SelectValue> getCopayValues(PreauthDTO dto) {
		 BeanItemContainer<SelectValue> coPayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		 	List<String> coPayPercentage = new ArrayList<String>();
		    for (Double string : dto.getProductCopay()) {
		    	coPayPercentage.add(String.valueOf(string));
			}
		    
		    Long i = 0l;
		    SelectValue value = null;
		    for (String string : coPayPercentage) {
		    	value = new SelectValue();
		    	String[] copayWithPercentage = string.split("\\.");
				String copay = copayWithPercentage[0].trim();
		    	value.setId(Long.valueOf(copay));
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
		 	PreauthDTO preauthDto = (PreauthDTO) parameters.getSecondaryParameter(1, PreauthDTO.class);
			String diagnosis = null;
			if(values.containsKey("diagnosisId")) {
				diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
			}
			
			if (dto.getDiagnosisDetailsDTO() != null) {
				dto.getDiagnosisDetailsDTO()
						.setDiagnosis(diagnosis);
			}
			
			Map<String, Object> medicalDecisionTableValues = null;
			
			
			values.put("productKey", preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey());
			
			if(ReferenceTable.getGMCProductList().containsKey(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValueForGMC(values,preauthDto.getNewIntimationDTO());
			}else{
				medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValue(values,preauthDto.getNewIntimationDTO());
			}
			
			view.getValuesForMedicalDecisionTable(dto, medicalDecisionTableValues);
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setReferenceDataClear(
			@Observes @CDIEvent(REFERENCE_DATA_CLEAR) final ParameterDTO parameters) {
		SHAUtils.setClearReferenceData(referenceData);
		
	}

	public void generateApproveLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_APPROVE_EVENT) final ParameterDTO parameters) {		
		
		view.generateApproveLayout();
	}
	
	
	public void generateCancelRODLayout(
			@Observes @CDIEvent(CLAIM_CANCEL_ROD_EVENT) final ParameterDTO parameters) {
		view.generateCancelRodLayout();
	}
	
	public void generateQueryLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_QUERY_BUTTON_EVENT) final ParameterDTO parameters) {
		view.generateQueryLayout();
	}

	public void generateRejectLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_REJECTION_EVENT) final ParameterDTO parameters) {

		view.generateRejectionLayout(masterService
				.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
	}

	public void generateEscalateLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_ESCALATE_EVENT) final ParameterDTO parameters) {
		view.generateEscalateLayout(masterService
				.getSelectValueContainer(ReferenceTable.ESCALATE_TO_ROD));
	}

	public void generateEscalateReplyLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_ESCALATE_REPLY_EVENT) final ParameterDTO parameters) {
		view.generateEscalateReplyLayout();
	}

	public void generateReferCoOrdinatorLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_REFERCOORDINATOR_EVENT) final ParameterDTO parameters) {
		view.generateReferCoOrdinatorLayout(masterService
				.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
	}
	
	public void generateSpecialistLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_SPECIALIST_EVENT) final ParameterDTO parameters) {
		view.genertateSpecialistLayout(masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE)));
	}

	public void generateSentToReplyLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_SENT_TO_REPLY_EVENT) final ParameterDTO parameters) {
		view.genertateSentToReplyLayout();
	}
}
