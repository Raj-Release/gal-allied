package com.shaic.claim.ped.outsideprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pedquery.PEDQueryDTO;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(InitiatePedView.class)
public class InitiatePedPresenter extends
AbstractMVPPresenter<InitiatePedView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String GET_ICD_BLOCK="Set data for ICD Block in initiate ped endorsement for outside process";
	
	public static final String GET_ICD_CODE = "Set data for icd_code in initiate Ped endorsement for outside process";
	
	public static final String GET_PED_CODE="Set ped code value for view details Endorsement for outside process";
	
	public static final String SUBMIT_DATA = "submit ped process for outside";
	
	public static final String INITIATE_PED_USER_LUMEN_REQUEST =  "initiate_ped_user_lumen_request";
	
	public static final String GET_PED_REVIEWER = "Set ped reviewer";
	
	public static final String GET_PED_AVAILABLE_DETAILS_INITIATOR = "Get PED Available Details for view for Initiator";
	
	public static final String GET_PED_ALREADY_AVAILABLE_INITIATOR = "Get PED Already available by Suggestion Key For Initiator";
	
	public static final String SHOW_DUP_PED_AVAILABLE_OUTSIDE_PROCESS = "Duplicate PED Available in Diagnosis Detail table outside process";
	
	public static final String CHECK_PED_APPLICABLE_FOR_POLICY = "Validate Donot renew Risk for Current Policy";
	
	public static final String CHECK_INSURED_STATUS = "Check Insured Status deceased";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private DBCalculationService dbService;
	
	public void getIcdBlock(@Observes @CDIEvent(GET_ICD_BLOCK) final ParameterDTO parameters)
	{
		Long chapterKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdBlockContainer = masterService.searchIcdBlockByChapterKey(chapterKey);
		
		view.setIcdBlock(icdBlockContainer);
	}
	
	public void getIcdCode(@Observes @CDIEvent(GET_ICD_CODE) final ParameterDTO parameters)
	{
		Long blockKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdCodeContainer = masterService.searchIcdCodeByBlockKey(blockKey);
		
		view.setIcdCode(icdCodeContainer);
	}
	
	public void getpedCode(@Observes @CDIEvent(GET_PED_CODE) final ParameterDTO parameters)
	{
		Long blockKey = (Long) parameters.getPrimaryParameter();
		//BeanItemContainer<SelectValue> icdCodeContainer = masterService.searchPEDCode(blockKey);
		
		String pedCode=masterService.getPEDCode(blockKey);
		
		view.setPEDCode(pedCode);
	}
	
	public void submitPedProcess(@Observes @CDIEvent(SUBMIT_DATA) final ParameterDTO parameters)
	{
		PEDQueryDTO bean = (PEDQueryDTO)parameters.getPrimaryParameter();
		
		Claim claim = claimService.getClaimByClaimKey(bean.getClaimDto().getKey());
		
		pedQueryService.submitPedEndorsement(
				bean, null, claim.getIntimation(), claim.getIntimation().getPolicy(),
				claim, bean.getUsername(), bean.getPassword(),SHAConstants.PED_INITIATE_OUTSIDE_PROCESS);
		
		if(!bean.getIsEditPED()){
			pedQueryService.submitPEDTaskToDB(claim, claim.getIntimation(), bean);	
		}
		
		view.buildSuccessLayout();
	}
	
	public void preauthLumenRequest(@Observes @CDIEvent(INITIATE_PED_USER_LUMEN_REQUEST) final ParameterDTO parameters){
		PEDQueryDTO bean = (PEDQueryDTO)parameters.getPrimaryParameter();
		Claim claim = claimService.getClaimByClaimKey(bean.getClaimDto().getKey());		
		view.buildInitiateLumenRequest(bean.getClaimDto().getNewIntimationDto().getIntimationId());
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setPedReviewer(@Observes @CDIEvent(GET_PED_REVIEWER) final ParameterDTO parameters)
	{
		PEDQueryDTO bean = (PEDQueryDTO)parameters.getPrimaryParameter();
		bean.setIsWatchListReviewer(pedQueryService.isUserPedReviewer(bean.getUsername()));
	}

	public void getPEDByICDChapter(@Observes @CDIEvent(GET_PED_AVAILABLE_DETAILS_INITIATOR) final ParameterDTO parameters)
	{
   		WeakHashMap<Integer,Object> inputMap = (WeakHashMap<Integer,Object>)parameters.getPrimaryParameter();
   		
   		ViewPEDTableDTO newInitiatePedDTO = (ViewPEDTableDTO)parameters.getSecondaryParameters()[0];
   		
   		Boolean isPedAvailable = pedQueryService.getPEDAvailableDetailsByICDChapter(inputMap);

   		if(isPedAvailable){
   			view.showPEDAlreadyAvailable(SHAConstants.PED_ALREADY_RAISED_FOR_SAME_ICD_CHAPTER);
   			view.resetPEDDetailsTable(newInitiatePedDTO);
   		}	   		
	}
	
	public void getInsuredStatusAlertApplicable(@Observes @CDIEvent(CHECK_INSURED_STATUS) final ParameterDTO parameters)
	{
		Long insuredKey = (Long) parameters.getPrimaryParameter();
		
		Insured insuredObj = insuredService.getInsuredByInsuredKey(insuredKey);
		
		Policy policyObj = insuredObj.getPolicy();    // intimationService.getPolicyServiceInstance().getPolicyByKey(
		
		String deceasedFlag = dbService.getInsuredPatientStatus(policyObj.getKey(), insuredKey);
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(deceasedFlag)) {
			view.showPEDAlreadyAvailable(SHAConstants.INSURED_DECEASED_ALERT);
		}	
	}
	

	public void getPedAlreadyAvailableForSuggestion(@Observes @CDIEvent(GET_PED_ALREADY_AVAILABLE_INITIATOR) final ParameterDTO parameters)
	{
		Long suggestionKey = (Long) parameters.getPrimaryParameter();
		
		Long intimateKey = (Long) parameters.getSecondaryParameters()[0];
		
		Long insuredKey = (Long) parameters.getSecondaryParameters()[1];
		
		boolean pedAvailable = false;
		
		/*if(suggestionKey != null && intimateKey != null){
			pedAvailable = pedQueryService.getPEDAvailableDetails(suggestionKey, intimateKey);
		}*/	
		
		
		Intimation intimationObj = intimationService.getIntimationByKey(intimateKey);
		
		List<Insured> insuredListByPolicyKey = insuredService.getInsuredListByPolicyNo(String.valueOf(intimationObj.getPolicy().getKey()));
//		boolean isdeletedPedAvailable = pedQueryService.getPEDAvailableDetails(ReferenceTable.PED_SUGGESTION_SUG006, intimateKey);
		boolean isdeletedPedAvailable = pedQueryService.getStatusOfInsuredForNonDisclosePed(insuredKey);
		
		if(suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG004)) {				
			pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(suggestionKey, intimationObj.getPolicy().getKey());
			if(!pedAvailable) {
				if(!isdeletedPedAvailable){
					if(intimationObj != null && intimationObj.getPolicy() != null 
							&& intimationObj.getPolicy().getProductType() != null 
							&& ReferenceTable.FLOATER_POLICY.equals(intimationObj.getPolicy().getProductType().getKey())){
						pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(suggestionKey, intimationObj.getPolicy().getKey());
					}	
					else if(intimationObj != null && intimationObj.getPolicy() != null 
							&& intimationObj.getPolicy().getProductType() != null 
							&& ReferenceTable.INDIVIDUAL_POLICY.equals(intimationObj.getPolicy().getProductType().getKey())) {
						pedAvailable = pedQueryService.getPEDAvailableDetails(suggestionKey, insuredKey);
					}
				}
				else{
					if(intimationObj != null && intimationObj.getPolicy() != null 
							&& intimationObj.getPolicy().getProductType() != null 
							&& ReferenceTable.FLOATER_POLICY.equals(intimationObj.getPolicy().getProductType().getKey())){
						pedAvailable =  false;
					}
					else{
						pedAvailable =  true;
					}
				}
			}
			else{
					pedAvailable =  true;
			}	
		}
		else if(suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG005)){				
			
			if(intimationObj != null && intimationObj.getPolicy() != null 
					&& intimationObj.getPolicy().getProductType() != null 
					&& ReferenceTable.FLOATER_POLICY.equals(intimationObj.getPolicy().getProductType().getKey())){
				
				OldInitiatePedEndorsement pedObj = pedQueryService.getPedDetailsByPolicyKey(ReferenceTable.PED_SUGGESTION_SUG004, intimationObj.getPolicy().getKey());
				pedAvailable = pedObj != null && pedObj.getStatus() != null && pedObj.getStatus().getKey() != null && ReferenceTable.ENDORSEMENT_APPROVED_BY_PREMIA.equals(pedObj.getStatus().getKey()) ? true : false;
				
			}		
			else if(intimationObj != null && intimationObj.getPolicy() != null 
					&& intimationObj.getPolicy().getProductType() != null 
					&& ReferenceTable.INDIVIDUAL_POLICY.equals(intimationObj.getPolicy().getProductType().getKey())) {
				OldInitiatePedEndorsement pedObj = pedQueryService.getPedDetailsBySuggestionKeyForInsured(ReferenceTable.PED_SUGGESTION_SUG004, insuredKey);
				
				pedAvailable = pedObj != null && pedObj.getStatus() != null && pedObj.getStatus().getKey() != null && ReferenceTable.ENDORSEMENT_APPROVED_BY_PREMIA.equals(pedObj.getStatus().getKey()) ? true : false;  
			}					
						
		}
		else{
			
			if(!isdeletedPedAvailable){
				
//				pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(suggestionKey, intimationObj.getPolicy().getKey());
				
				if(!isdeletedPedAvailable && (suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG002) || suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG010))){
					pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(suggestionKey, intimationObj.getPolicy().getKey());
					if(!pedAvailable) {
						List<Long> sugKeyList = new ArrayList<Long>();
						sugKeyList.add(ReferenceTable.PED_SUGGESTION_SUG002);
						sugKeyList.add(ReferenceTable.PED_SUGGESTION_SUG010);
						pedAvailable = pedQueryService.getPEDAvailableDetailsBySuggestionForPolicy(sugKeyList, intimationObj.getPolicy().getKey());
					}
				}				
				else if(suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG006)){
					
					/*if(intimationObj != null && intimationObj.getPolicy() != null 
						&& intimationObj.getPolicy().getPolicyType() != null 
						&& ReferenceTable.INDIVIDUAL_POLICY.equals(intimationObj.getPolicy().getPolicyType())) {
					
				}
				else if(intimationObj != null && intimationObj.getPolicy() != null 
						&& intimationObj.getPolicy().getPolicyType() != null 
						&& ReferenceTable.FLOATER_POLICY.equals(intimationObj.getPolicy().getPolicyType())) {
					
				}*/
					if(insuredListByPolicyKey != null && !insuredListByPolicyKey.isEmpty() && insuredListByPolicyKey.size() > 1){
//						pedAvailable = pedQueryService.getPEDAvailableDetails(ReferenceTable.PED_SUGGESTION_SUG006, intimateKey);
						pedAvailable = pedQueryService.getStatusOfInsuredForNonDisclosePed(insuredKey);
					}
					else{
						pedAvailable = true; // Single Insured can not be deleted so always true.
					}
				}
				/*else if(!isdeletedPedAvailable && suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG010)){
					pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(ReferenceTable.PED_SUGGESTION_SUG002, intimationObj.getPolicy().getKey());					
				}*/
			}
			else{
//				pedAvailable = true;  // Insured already deleted so PED not allowed.
				
				if(!suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG002) && !suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG003) && !suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG010)){
					
					pedAvailable = true;  // Insured already deleted so PED not allowed except sugg. type 2,3,10.
				}
				else{
					if(suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG002) || suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG010)){
						List<Long> pedSuggestionKeyList = new ArrayList<Long>();
						pedSuggestionKeyList.add(ReferenceTable.PED_SUGGESTION_SUG002);
						pedSuggestionKeyList.add(ReferenceTable.PED_SUGGESTION_SUG010);
						pedAvailable = pedQueryService.getPEDAvailableDetailsBySuggestionForPolicy(pedSuggestionKeyList, intimationObj.getPolicy().getKey());
					}
					else{
						pedAvailable = false;
					}
				}
			}
		}
		
		
		if(pedAvailable){
//			view.showPEDAlreadyAvailable("Selected PED Suggestion was already initiated");
			
			if(suggestionKey != null && (suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG003) || suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG005) || suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG010))){
				view.showPEDAlreadyAvailable("Selected PED Suggestion is not allowed.");
			}
			else if(suggestionKey != null && suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG006)){
				
				if(insuredListByPolicyKey != null && !insuredListByPolicyKey.isEmpty() && insuredListByPolicyKey.size() == 1){
					view.showPEDAlreadyAvailable("Selected PED Suggestion is not allowed.<br>Pleae Raise PED for - SUG 002 - Cancel Policy.");   // Single Insured can not be deleted
				}
				else{
					view.showPEDAlreadyAvailable("Selected PED Suggestion was already initiated.");
				}
			}
			else{
				view.showPEDAlreadyAvailable("Selected PED Suggestion was already initiated.");
			}	
		
		}	
	}
	
	public void showDuplicatePEDAlert(@Observes @CDIEvent(SHOW_DUP_PED_AVAILABLE_OUTSIDE_PROCESS) final ParameterDTO parameters)
	{
		ViewPEDTableDTO newInitiatePedDTO = (ViewPEDTableDTO)parameters.getPrimaryParameter();
   		
		view.showPEDAlreadyAvailable(SHAConstants.PED_ALREADY_RAISED_FOR_SAME_ICD_CHAPTER);
   		view.resetPEDDetailsTable(newInitiatePedDTO);
	}
}
