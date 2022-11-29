package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.previousclaims;

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
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAHealthClaimRequestPreviousClaimsPageInterface.class)
public class PAHealthClaimRequestPreviousClaimsPagePresenter extends AbstractMVPPresenter<PAHealthClaimRequestPreviousClaimsPageInterface>  {
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

	public static final String MEDICAL_APPROVAL_PREVIOUS_CLAIMS_SETUP_REFERNCE = "pa_health_medical_approval_claim_request_previous_claims_setup_reference";
	public static final String MEDICAL_APPROVAL_PREVIOUS_CLAIM_DETAILS = "pa_health_medical_approval_claim_request_previous_claims_details_list";
	public static final String MEDICAL_APPROVAL_RELAPSE_OF_ILLNESS_CHANGED = "pa_health_medical_approval_claim_request_previous_claims_relapse_of_illness_changed";
	
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	public void setUpReference(
			@Observes @CDIEvent(MEDICAL_APPROVAL_PREVIOUS_CLAIMS_SETUP_REFERNCE) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		view.setupReferences(referenceData);
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void getPreviousClaimDetails(@Observes @CDIEvent(MEDICAL_APPROVAL_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters)
	{
		
		PreauthDTO preauthDto = (PreauthDTO)parameters.getPrimaryParameter();
	/*	String policyNumberOrInsuredId = (String) parameters
				.getPrimaryParameter();*/
//		List<ViewTmpClaim> claimList = claimService
//				.getTmpClaimsByInsuredId(preauthDto.getNewIntimationDTO()
//						.getInsuredPatient().getInsuredId());
		System.out.println("--the current claim id---"+preauthDto.getClaimDTO().getClaimId());
		
//		List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils.getPreviousClaims(claimList, preauthDto.getClaimDTO().getClaimId()
//				, pedValidationService, masterService);
		
		/*List<ViewTmpClaim> claimList = new ArrayList<ViewTmpClaim>();

		try{

			List<ViewTmpClaim> claimsByPolicyNumber = claimService
					.getViewTmpClaimsByPolicyNumber(preauthDto.getNewIntimationDTO().getPolicy().getPolicyNumber());
			
			for (ViewTmpClaim viewTmpClaim : claimsByPolicyNumber) {
				if(preauthDto.getNewIntimationDTO().getInsuredPatient().getHealthCardNumber().equalsIgnoreCase(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber())){
					claimList.add(viewTmpClaim);
				}
			}
				claimList = getPreviousClaimInsuedWiseForPreviousPolicy(preauthDto.getNewIntimationDTO().getPolicy().getRenewalPolicyNumber(), 
						claimList,preauthDto.getNewIntimationDTO().getInsuredPatient().getHealthCardNumber());
				
			}catch(Exception e){
				e.printStackTrace();
		}
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(claimList,
						preauthDto.getClaimDTO()
					.getClaimId()); */  
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
				preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.INSURED_WISE_SEARCH_TYPE);
		
		view.getPreviousClaimDetails(previousClaimDTOList);
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

}
