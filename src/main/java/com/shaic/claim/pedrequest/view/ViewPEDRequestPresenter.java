package com.shaic.claim.pedrequest.view;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.apache.tools.ant.taskdefs.Length;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.viewPedEndorsement.PedEndorsementMapper;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.vaadin.v7.data.util.BeanItemContainer;


@ViewInterface(ViewPEDRequestView.class)
public class ViewPEDRequestPresenter extends AbstractMVPPresenter<ViewPEDRequestView> {
	
	@EJB
	private MasterService masterService;
	
	public static final String GET_ICD_BLOCK="Set data for ICD Block in initiate ped endorsement";
	
	public static final String GET_ICD_CODE = "Set data for icd_code in initiate Ped endorsement";
	
	public static final String GET_PED_CODE="Set ped code value for view details Endorsement";
	
	public static final String PED_EDIT_DETAILS = "view ped edit details";
	
	public static final String GET_PED_AVAILABLE_DETAILS = "Get PED Available Details for view";
	
	public static final String GET_PED_ALREADY_AVAILABLE = "Get PED Already available by Suggestion Key";
	
	public static final String SHOW_DUP_PED_AVAILABLE = "Show Dup PED Available in Diag detail Table";
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private InsuredService insuredService;
	
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
		Long diagKey = (Long) parameters.getPrimaryParameter();
		//BeanItemContainer<SelectValue> icdCodeContainer = masterService.searchPEDCode(blockKey);
		
//		Long intimateKey = (Long) parameters.getSecondaryParameters()[0];
		
		String pedCode=masterService.getPEDCode(diagKey);
		
		boolean pedAvailable = false;
		
//		if(pedCode != null && !pedCode.isEmpty() && (pedCode.contains("002") || pedCode.contains("004"))){
//			pedAvailable = pedQueryService.getPEDAvailableDetails(pedsuggestionKey, intimateKey);
//		}	
		
		view.setPEDCode(pedCode);
	}
	
	public void setEditDetails(@Observes @CDIEvent(PED_EDIT_DETAILS) final ParameterDTO parameters)
	{
		Long pedKey = (Long)parameters.getPrimaryParameter();
		
		OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(pedKey);
		
		List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = pedQueryService.getIntitiatePedEndorsementDetails(pedKey);
		
		List<ViewPEDTableDTO> pedInitiateDetailsDTOList = null;
		if(intitiatePedEndorsementDetails != null && !intitiatePedEndorsementDetails.isEmpty()){
			PedEndorsementMapper mapper = PedEndorsementMapper.getInstance();
			pedInitiateDetailsDTOList = mapper.getPedInitiateDetailsDTOList(intitiatePedEndorsementDetails);
		}
		
		
//		Boolean isWatchList = pedQueryService.isTaskAvailableInWatchList(initiate.getIntimation(), initiate, BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
		Boolean isWatchList = preauthService.getDBTaskForPreauth(initiate.getClaim().getIntimation().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
		
		
		view.setEditDetailsForPED(initiate,pedInitiateDetailsDTOList,isWatchList);
		
		
	}	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void getPEDByICDChapter(@Observes @CDIEvent(GET_PED_AVAILABLE_DETAILS) final ParameterDTO parameters)
	{
   		WeakHashMap<Integer,Object> inputMap = (WeakHashMap<Integer,Object>)parameters.getPrimaryParameter();
   		
   		ViewPEDTableDTO newInitiatePedDTO = (ViewPEDTableDTO)parameters.getSecondaryParameters()[0];
   		
   		Boolean isPedAvailable = pedQueryService.getPEDAvailableDetailsByICDChapter(inputMap);

   		if(isPedAvailable){
   			view.showPEDAlreadyAvailable(SHAConstants.PED_ALREADY_RAISED_FOR_SAME_ICD_CHAPTER);
   			view.resetPEDDetailsTable(newInitiatePedDTO);
   		}	   		
	}
	
	public void getPedAlreadyAvailableForSuggestion(@Observes @CDIEvent(GET_PED_ALREADY_AVAILABLE) final ParameterDTO parameters)
	{
		Long suggestionKey = (Long) parameters.getPrimaryParameter();
		
		Long intimateKey = (Long) parameters.getSecondaryParameters()[0];
		
		Long insuredKey = parameters.getSecondaryParameters()[1] != null ? (Long) parameters.getSecondaryParameters()[1] : null;
		
		boolean pedAvailable = false;
		
			Intimation intimationObj = intimationService.getIntimationByKey(intimateKey);
			
			List<Insured> insuredListByPolicyKey = insuredService.getInsuredListByPolicyNo(String.valueOf(intimationObj.getPolicy().getKey()));
			boolean isdeletedPedAvailable = pedQueryService.getPEDAvailableDetails(ReferenceTable.PED_SUGGESTION_SUG006, insuredKey);
			
			if(suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG004)) {				
				pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(suggestionKey, intimationObj.getPolicy().getKey());
				if(!pedAvailable){
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
					
//					pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(suggestionKey, intimationObj.getPolicy().getKey());
					
					if(!isdeletedPedAvailable && (suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG002) || suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG010))){
						List<Long> pedSuggestionKeyList = new ArrayList<Long>();
						pedSuggestionKeyList.add(ReferenceTable.PED_SUGGESTION_SUG002);
						pedSuggestionKeyList.add(ReferenceTable.PED_SUGGESTION_SUG010);
						pedAvailable = pedQueryService.getPEDAvailableDetailsBySuggestionForPolicy(pedSuggestionKeyList, intimationObj.getPolicy().getKey());
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
							pedAvailable = pedQueryService.getPEDAvailableDetails(ReferenceTable.PED_SUGGESTION_SUG006, insuredKey);
						}
						else{
							pedAvailable = true; // Single Insured can not be deleted so always true.
						}
					}
//					else if(!isdeletedPedAvailable && suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG010)){
//						pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(ReferenceTable.PED_SUGGESTION_SUG002, intimationObj.getPolicy().getKey());					
//					}
				}
				else{
					
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

	public void showDupPEDAlertForTable(@Observes @CDIEvent(SHOW_DUP_PED_AVAILABLE) final ParameterDTO parameters)
	{
		ViewPEDTableDTO newInitiatePedDTO = (ViewPEDTableDTO)parameters.getPrimaryParameter();
   		
		view.showPEDAlreadyAvailable(SHAConstants.PED_ALREADY_RAISED_FOR_SAME_ICD_CHAPTER);
		view.resetPEDDetailsTable(newInitiatePedDTO);
	}
}
