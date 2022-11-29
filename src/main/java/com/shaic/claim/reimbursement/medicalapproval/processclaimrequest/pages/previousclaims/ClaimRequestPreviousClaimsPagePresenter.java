package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.previousclaims;

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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(ClaimRequestPreviousClaimsPageInterface.class)
public class ClaimRequestPreviousClaimsPagePresenter extends AbstractMVPPresenter<ClaimRequestPreviousClaimsPageInterface>  {
	private static final long serialVersionUID = -5364393591968926152L;

	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PreMedicalService preMedicalService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private PEDValidationService pedValidationService;
	
	@EJB 
	private IntimationService intimationService;

	public static final String MEDICAL_APPROVAL_PREVIOUS_CLAIMS_SETUP_REFERNCE = "medical_approval_claim_request_previous_claims_setup_reference";
	
	public static final String MEDICAL_APPROVAL_RELAPSE_OF_ILLNESS_CHANGED = "medical_approval_claim_request_previous_claims_relapse_of_illness_changed";

	public static final String CLAIM_REQUEST_APPROVE_EVENT = "claim_request_apporve_event_previous_claim_page";
	
	public static final String CLAIM_CANCEL_ROD_EVENT      =  "claim_request_cancel_rod_previous_claim_page";  
	
	public static final String CLAIM_REQUEST_QUERY_BUTTON_EVENT = "claim_request_query_button_event_previous_claim_page";

	public static final String CLAIM_REQUEST_REJECTION_EVENT = "claim_request_rejection_button_event_previous_claim_page";

	public static final String CLAIM_REQUEST_ESCALATE_EVENT = "claim_request_escalte_button_event_previous_claim_page";

	public static final String CLAIM_REQUEST_ESCALATE_REPLY_EVENT = "claim_request_escalte_reply_event_previous_claim_page";

	public static final String CLAIM_REQUEST_REFERCOORDINATOR_EVENT = "claim_request_refer_to_coordinator_event_previous_claim_page";

	public static final String CLAIM_REQUEST_SPECIALIST_EVENT = "claim_request_specialist_event_previous_claim_page";

	public static final String CLAIM_REQUEST_SENT_TO_REPLY_EVENT = "claim_request_sent_to_reply_event_previous_claim_page";
	
	public static final String CLAIM_REQUEST_PREVIOUS_CLAIM_DETAILS = "claim_request_previous_claims_details";
	
	public static final String CLAIM_REQUEST_PREVIOUS_CLAIM_FOR_POLICY = "claim_request_previous_claims_details_1";
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	public void setUpReference(
			@Observes @CDIEvent(MEDICAL_APPROVAL_PREVIOUS_CLAIMS_SETUP_REFERNCE) final ParameterDTO parameters) {
		
		//PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		view.setupReferences(referenceData);
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void getPreviousClaimForPolicy(
			@Observes @CDIEvent(CLAIM_REQUEST_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		String selectionType = (String)parameters.getSecondaryParameters()[0];
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = new ArrayList<PreviousClaimsTableDTO>();
		
		if(null != selectionType && SHAConstants.POLICY_WISE.equalsIgnoreCase(selectionType)){
		
			previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
				preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);
		}
		else if(SHAConstants.INSURED_WISE.equalsIgnoreCase(selectionType)){
				 previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
					preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.INSURED_WISE_SEARCH_TYPE);
		}
		else{
				 previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
					preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.RISK_WISE_SEARCH_TYPE);
		}				
		view.setPreviousClaimDetailsForPolicy(previousClaimDTOList);
	}
	
	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			if(renewalPolNo != null) {
				List<ViewTmpIntimation> intimationKeys = intimationService.getIntimationByPolicyKey(renewalPolNo.getKey());
				List<ViewTmpClaim> claimsByPolicyNumber = claimService
						.getViewTmpClaimsByIntimationKeys(intimationKeys);
//				List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if(claimsByPolicyNumber != null && !claimsByPolicyNumber.isEmpty()) {
					for (ViewTmpClaim viewTmpClaim : claimsByPolicyNumber) {
						if(!generatedList.contains(viewTmpClaim)) {
							generatedList.add(viewTmpClaim);
						}
					}
				}
				if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
					getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
				} else {
					return generatedList;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return generatedList;
	}
		
	public List<ViewTmpClaim> getPreviousClaimInsuedWiseForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList,String healthCardNumber) {
		
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			if(renewalPolNo != null) {
				List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
					for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
						if(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(healthCardNumber)){
							if(!generatedList.contains(viewTmpClaim)) {
								generatedList.add(viewTmpClaim);
							}
						}
					}
				}
				if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
					getPreviousClaimInsuedWiseForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList,healthCardNumber);
				} else {
					return generatedList;
				}
			}
		} catch(Exception e) {
	}
	return generatedList;
}
	
	public void generateFieldsBasedOnRelapseIllness(@Observes @CDIEvent(MEDICAL_APPROVAL_RELAPSE_OF_ILLNESS_CHANGED) final ParameterDTO parameters)
	{
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		SelectValue relpaseofIllness = parameters.getSecondaryParameters() != null ? (SelectValue) parameters.getSecondaryParameters()[0] : null;
	
		if(relpaseofIllness != null && relpaseofIllness.getId() == ReferenceTable.COMMONMASTER_YES){
			BeanItemContainer<SelectValue> relapseClaimContainer = claimService.getRelpseClaimsForPolicy(preauthDto.getPolicyDto().getPolicyNumber() , preauthDto.getClaimNumber());
			referenceData.put("relapsedClaims", relapseClaimContainer);
		}
		view.genertateFieldsBasedOnRelapseOfIllness(referenceData);
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
	
	public void getPreviousClaimForPolicy1(
			@Observes @CDIEvent(CLAIM_REQUEST_PREVIOUS_CLAIM_FOR_POLICY) final ParameterDTO parameters) {
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = new ArrayList<PreviousClaimsTableDTO>();
		
		
			previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
				preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);
		view.setPreviousClaimDetailsForPolicy(previousClaimDTOList);
	}

}
