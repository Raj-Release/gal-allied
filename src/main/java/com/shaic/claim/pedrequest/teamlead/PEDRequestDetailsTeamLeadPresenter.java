package com.shaic.claim.pedrequest.teamlead;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApproveDTO;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.viewPedEndorsement.PedEndorsementMapper;
import com.shaic.domain.Claim;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Notification;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PEDRequestDetailsTeamLeadView.class)
public class PEDRequestDetailsTeamLeadPresenter extends
		AbstractMVPPresenter<PEDRequestDetailsTeamLeadView> {
	
	public static final String PED_TL_QUERY_BUTTON="PED TL Query";
	public static final String PED_TL_REFER_BUTTON="PED TL Refer to Specialist";
	public static final String PED_TL_APPROVE_BUTTON="PED TL Send to Approve";
	public static final String PED_TL_WATCH_BUTTON="PED TL Add to WatchList";
	public static final String PED_TL_REJECT_BUTTON="PED TL rejection";
	
	public static final String PED_TL_SET_FIRST_TABLE = "Set data for Ped TL request approve table";
	
	public static final String PED_TL_SET_FIELD_DATA="set data for ped TL query approval";
	
	public static final String PED_TL_QUERY_REMARKS="query remark(TL)";
	
	public static final String PED_TL_SPECIALIST_REMARKS="Specialist Remarks updated in PED TL";
	
	public static final String PED_TL_WATCHLIST_REMARKS="Watchlist Remarks updated in PED TL";
	
	public static final String PED_TL_SET_MODIFIED_DATA="Set modified data to all fields PED TL";
	
	
	public static final String PED_TL_REJECTION_REMARKS="Rejection remarks updated PED TL";
	
	public static final String PED_TL_APPROVE_REMARKS="Approve Remarks updated PED TL";
	
	public static final String PED_TL_SET_SECOND_TABLE="set data to editable table in ped TL approve";
	
	public static final String PED_TL_EDIT_BUTTON = "edit button for current PED in Ped request Details Process PED TL";
	
	public static final String PED_TL_GET_ICD_BLOCK="Set data for ICD Block in initiate ped endorsement for ped TL";
	
	public static final String PED_TL_GET_ICD_CODE = "Set data for icd_code in initiate Ped endorsement for ped TL";
	
	public static final String PED_TL_GET_PED_CODE="Set ped code value for view details Endorsement for ped TL";
	
	public static final String GET_PED_AVAILABLE_DETAILS_TEAM_LEAD = "Get PED Available Details for ped TL";
	
	public static final String TL_GET_PED_ALREADY_AVAILABLE = "Get PED Already Available for the Suggestion for TL";
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationSerivice;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private InsuredService insuredService;
	
	public static final String VALIDATE_PED_TL_USER_RRC_REQUEST = "process_ped_tl_user_rrc_request";
	
	public static final String PROCESS_PED_TL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "process_ped_tl_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_PED_TL_SAVE_RRC_REQUEST_VALUES = "process_ped_tl_query_save_rrc_request_values";
	
	public static final String INITIATE_PED_TEAM_LEAD_USER_LUMEN_REQUEST = "initiate_ped_team_lead_user_lumen_request";

	public static final String PROCESS_PED_TL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_ped_tl_load_rrc_request_sub_category_values";

	public static final String PROCESS_PED_TL_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_ped_tl_load_rrc_request_source_values";
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PED_TL_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	
	public void validatUserForLumenRequest(@Observes @CDIEvent(INITIATE_PED_TEAM_LEAD_USER_LUMEN_REQUEST) final ParameterDTO parameters){
		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(newIntimationDto.getIntimationId());
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_PED_TL_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_PED_TL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void generateFieldsBasedOnQuery(@Observes @CDIEvent(PED_TL_QUERY_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnQueryClick(isChecked);
	}
	
	public void generateFieldsBasedOnWatchlist(@Observes @CDIEvent(PED_TL_WATCH_BUTTON) final ParameterDTO parameters) {
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnWatchClick(isChecked);
	}
	public void generatefieldsForEdit(@Observes @CDIEvent(PED_TL_EDIT_BUTTON) final ParameterDTO parameters){
		
		OldPedEndorsementDTO tableDTO = (OldPedEndorsementDTO) parameters
				.getPrimaryParameter();
		
		 OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(tableDTO.getKey());
		 
		List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = pedQueryService.getIntitiatePedEndorsementDetails(tableDTO.getKey());
		
		PedEndorsementMapper endormentMapper = PedEndorsementMapper.getInstance();
		List<ViewPEDTableDTO> pedInitiateDetailsDTOList = endormentMapper.getPedInitiateDetailsDTOList(intitiatePedEndorsementDetails);
		
		PEDRequestDetailsApproveDTO bean = new PEDRequestDetailsApproveDTO();
		 
		 bean.setPedApprovalName(initiate.getPedName());
		 MastersValue pedSuggestion = initiate.getPedSuggestion();
		 if(pedSuggestion != null){
			 SelectValue selectedPed = new SelectValue();
			 selectedPed.setId(pedSuggestion.getKey());
			 selectedPed.setValue(pedSuggestion.getValue());
			 bean.setPedSuggestion(selectedPed);
		 }
		 bean.setRemarks(initiate.getRemarks());
		 bean.setRepudiationLetterDate(initiate.getRepudiationLetterDate());
		 
		 bean.setIsEditPED(true);
		 
		 bean.setPedInitiateDetails(pedInitiateDetailsDTOList);
		
		 view.showEditPanel(bean);
		
	}
	
	public void generateFieldsBasedOnRefer(@Observes @CDIEvent(PED_TL_REFER_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnReferClick(isChecked);
	}
	
	public void generateFieldsBasedApprove(@Observes @CDIEvent(PED_TL_APPROVE_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnApproveClick(isChecked);
	}
	
	public void generateFieldsBasedOnReject(@Observes @CDIEvent(PED_TL_REJECT_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnRejectClick(isChecked);
	}
	
	public void submitClick(
			@Observes @CDIEvent(PED_TL_QUERY_REMARKS) final ParameterDTO parameters) {
		PEDRequestDetailsApproveDTO bean = (PEDRequestDetailsApproveDTO) parameters
				.getPrimaryParameter();
		
		SearchPEDRequestApproveTableDTO searchDTO=(SearchPEDRequestApproveTableDTO)parameters.getSecondaryParameter(0,SearchPEDRequestApproveTableDTO.class);
		
		 Boolean result=pedQueryService.updateQueryRemarksApprover(bean,searchDTO);
         
		 if(result){
			 
				view.result(result);
			}
		 else
		 {
			 Notification.show("Intiatior type should be Approver");
		 }
		
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void getIcdBlock(@Observes @CDIEvent(PED_TL_GET_ICD_BLOCK) final ParameterDTO parameters)
	{
		Long chapterKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdBlockContainer = masterService.searchIcdBlockByChapterKey(chapterKey);
		
		view.setIcdBlock(icdBlockContainer);
	}
	
	public void getIcdCode(@Observes @CDIEvent(PED_TL_GET_ICD_CODE) final ParameterDTO parameters)
	{
		Long blockKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdCodeContainer = masterService.searchIcdCodeByBlockKey(blockKey);
		
		view.setIcdCode(icdCodeContainer);
	}
	
	public void getpedCode(@Observes @CDIEvent(PED_TL_GET_PED_CODE) final ParameterDTO parameters)
	{
		Long blockKey = (Long) parameters.getPrimaryParameter();
		
		String pedCode=masterService.getPEDCode(blockKey);
		
		view.setPEDCode(pedCode);
	}
	
	public void submitSpecialistClick(
			@Observes @CDIEvent(PED_TL_SPECIALIST_REMARKS) final ParameterDTO parameters) {
		PEDRequestDetailsApproveDTO bean = (PEDRequestDetailsApproveDTO) parameters
				.getPrimaryParameter();
		
		SearchPEDRequestApproveTableDTO searchDTO=(SearchPEDRequestApproveTableDTO)parameters.getSecondaryParameter(0,SearchPEDRequestApproveTableDTO.class);
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDTO.getDbOutArray();
		wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_TEAM_LEAD_REFER_TO_SPECIALIST_OUTCOME);		
		
		 Boolean result=pedQueryService.updateSpecialistDetails(bean, searchDTO);
         
		 if(result){
			 
				view.result(result);
			}
		 else
		 {
			 Notification.show("Intiatior type should be Approver");
		 }
		 
		
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void submitWatchListClick(
			@Observes @CDIEvent(PED_TL_WATCHLIST_REMARKS) final ParameterDTO parameters) {
		
		PEDRequestDetailsApproveDTO bean = (PEDRequestDetailsApproveDTO) parameters
				.getPrimaryParameter();
		
		SearchPEDRequestApproveTableDTO searchDTO=(SearchPEDRequestApproveTableDTO)parameters.getSecondaryParameter(0,SearchPEDRequestApproveTableDTO.class);
		
		 Boolean result=pedQueryService.updatedWatchListApproverDetails(bean, searchDTO);
         
		 if(result){
			 
				view.result(result);
			}
		 else
		 {
			 Notification.show("Intiatior type should be Approver");
		 }
		 
		
		//view.list(searchFieldVisitService.search(searchFormDTO));
	
		
	}
	
	public void submitClickEvent(
			@Observes @CDIEvent(PED_TL_REJECTION_REMARKS) final ParameterDTO parameters) {
		PEDRequestDetailsApproveDTO bean = (PEDRequestDetailsApproveDTO) parameters
				.getPrimaryParameter();
		
		SearchPEDRequestApproveTableDTO searchDTO=(SearchPEDRequestApproveTableDTO)parameters.getSecondaryParameter(0, SearchPEDRequestApproveTableDTO.class);
		
		 Boolean result=pedQueryService.updatedRejectionRemarks(bean,searchDTO);
         
		 if(result){
				view.result(result);
			}
		 else
		 {
			 Notification.show("Intiatior type should be Approver");
		 }
		
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void submitClickApprove(
			@Observes @CDIEvent(PED_TL_APPROVE_REMARKS) final ParameterDTO parameters) {
		PEDRequestDetailsApproveDTO bean = (PEDRequestDetailsApproveDTO) parameters
				.getPrimaryParameter();
		
		SearchPEDRequestApproveTableDTO searchDTO=(SearchPEDRequestApproveTableDTO)parameters.getSecondaryParameter(0, SearchPEDRequestApproveTableDTO.class);
		
		 Boolean result=pedQueryService.updatedApproveRemarks(bean,searchDTO);
         
		 if(result){
				view.result(result);
			}
		 else
		 {
			 Notification.show("Intiatior type should be Approver");
		 }
		
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void editApproveTable(
			@Observes @CDIEvent(PED_TL_SET_MODIFIED_DATA) final ParameterDTO parameters) {
		
		OldPedEndorsementDTO bean=(OldPedEndorsementDTO)parameters.getPrimaryParameter();
		
		PEDRequestDetailsApproveDTO resultDto=pedQueryService.getPedApproveByKey(bean.getKey());
		
	    OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(resultDto.getKey());
			
	    NewIntimationDto intimationDto=intimationSerivice.getIntimationDto(initiate.getIntimation());
	    
	    Claim claim=initiate.getClaim();
        
        ClaimDto claimDto=new ClaimDto();
        
        claimDto = ClaimMapper.getInstance().getClaimDto(claim);
        
        claimDto.setClaimId(claim.getClaimId());
        MastersValue currency=claim.getCurrencyId();
        SelectValue currencyId=new SelectValue();
        currencyId.setId(currency.getKey());
        currencyId.setValue(currency.getValue());
        
        claimDto.setCurrencyId(currencyId);
     
        intimationDto.setPolicy(initiate.getpolicy());
	     
	    BeanItemContainer<SelectValue> selectValueContainer=masterService.getConversionReasonByValue(ReferenceTable.PED_SUGGESTION);

	    view.setReferenceData(resultDto,selectValueContainer,intimationDto,claimDto);
	}
	
	public void setReferenceData(
			@Observes @CDIEvent(PED_TL_SET_FIELD_DATA) final ParameterDTO parameters) {
		SearchPEDRequestApproveTableDTO searchFormDTO = (SearchPEDRequestApproveTableDTO) parameters
				.getPrimaryParameter();
		//SearchPEDQueryTableDTO search=new SearchPEDQueryTableDTO();
		
		PEDRequestDetailsApproveDTO resultDto=pedQueryService.getPedApproveByKey(searchFormDTO.getKey());
		
		 OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(resultDto.getKey());
			
	     NewIntimationDto intimationDto=intimationSerivice.getIntimationDto(initiate.getIntimation());
	     
	     Claim claim=initiate.getClaim();
	        
	     ClaimDto claimDto=new ClaimDto();
	     claimDto =  ClaimMapper.getInstance().getClaimDto(claim);
	     claimDto.setClaimId(claim.getClaimId());
	     MastersValue currency=claim.getCurrencyId();
	     SelectValue currencyId=new SelectValue();
	     currencyId.setId(currency.getKey());
	     currencyId.setValue(currency.getValue());
	        
	     claimDto.setCurrencyId(currencyId);
	     
	     Boolean watchListAvailable = initiate.getWatchListFlag() != null ? initiate.getWatchListFlag().equalsIgnoreCase("Y") ? true : false : false;
	     
	     Boolean isAlreadyWatchList = false;
	        
	        if(initiate.getStatus() != null && initiate.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_APPROVER)){
	        	isAlreadyWatchList = true;
	        }
	     
	     resultDto.setIsWatchList(isAlreadyWatchList);
	     resultDto.setIsAlreadyWatchList(watchListAvailable);
	     
	     List<InsuredPedDetails> pedByInsured = policyService.getPEDByInsured(intimationDto.getInsuredPatient().getInsuredId());
	     resultDto.setInsuredPedDetails(pedByInsured);
	     
	     List<PreExistingDisease> approvedPedByInsured = policyService.getPedList(intimationDto.getInsuredPatient().getKey());
	     resultDto.setApprovedPedDetails(approvedPedByInsured);
	     
	    // intimationDto.setPolicy(initiate.getpolicy());
	     
	     BeanItemContainer<SelectValue> selectValueContainer=masterService.getConversionReasonByValue(ReferenceTable.PED_SUGGESTION);
	     
		
		view.setReferenceData(resultDto,selectValueContainer,intimationDto,claimDto);
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void searchClick(
			@Observes @CDIEvent(PED_TL_SET_FIRST_TABLE) final ParameterDTO parameters) {
//		SearchPEDQueryTableDTO searchFormDTO = (SearchPEDQueryTableDTO) parameters
//				.getPrimaryParameter();
		
		SearchPEDRequestApproveTableDTO searchFormDtO = (SearchPEDRequestApproveTableDTO) parameters
				.getPrimaryParameter();
		Page<OldPedEndorsementDTO> resultList=pedQueryService.search(searchFormDtO.getKey());
		
		view.list(resultList);
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void setEditableTable( @Observes@CDIEvent(PED_TL_SET_SECOND_TABLE) final ParameterDTO parameters){
		SearchPEDRequestApproveTableDTO searchFormDTO =(SearchPEDRequestApproveTableDTO)parameters.getPrimaryParameter();
		
		List<NewInitiatePedEndorsementDTO> newInitiatePedDtoList =pedQueryService.getInitiateDetailsPed(searchFormDTO.getKey());
//		OldPedEndorsementDTO editableList=pedQueryService.searchEditableTableList(newInitiatePedDtoList);
		BeanItemContainer<SelectValue> selectValueContainer=new BeanItemContainer<SelectValue>(SelectValue.class);
		BeanItemContainer<SelectValue> selectIcdChapterContainer=new BeanItemContainer<SelectValue>(SelectValue.class);
		BeanItemContainer<SelectValue> selectIcdBlockContainer=new BeanItemContainer<SelectValue>(SelectValue.class);
		BeanItemContainer<SelectValue> selectPedCodeContainer=new BeanItemContainer<SelectValue>(SelectValue.class);
		
		
		for (NewInitiatePedEndorsementDTO newInitiatePedEndorsementDTO : newInitiatePedDtoList) {
			SelectValue icdChapter=masterService.getIcdChapterbyId(newInitiatePedEndorsementDTO.getICDChapterId());
			newInitiatePedEndorsementDTO.setICDChapter(icdChapter);
			selectIcdChapterContainer.addBean(icdChapter);
			SelectValue icdBlock=masterService.getIcdBlock(newInitiatePedEndorsementDTO.getICDBlockId());
			newInitiatePedEndorsementDTO.setICDBlock(icdBlock);
			selectIcdBlockContainer.addBean(icdBlock);
			SelectValue icdCode=masterService.getIcdCodeByKey(newInitiatePedEndorsementDTO.getICDCodeId());
			newInitiatePedEndorsementDTO.setICDCode(icdCode);
			selectValueContainer.addBean(icdCode);
			SelectValue pedCode=masterService.getPedCodebyId(newInitiatePedEndorsementDTO.getPedCodeId());
			newInitiatePedEndorsementDTO.setPedCode(pedCode);
			selectPedCodeContainer.addBean(pedCode);
			//SelectValue source=masterService
		}
		
		OldPedEndorsementDTO editableList=new OldPedEndorsementDTO();
		editableList.setNewInitiatePedEndorsementDto(newInitiatePedDtoList);
		
		referenceData.put("description", editableList.getNewInitiatePedEndorsementDto().get(0).getDescription());
		referenceData.put("ICDCode", selectValueContainer);
		referenceData.put("ICDChapter",selectIcdChapterContainer);
		referenceData.put("ICDBlock", selectIcdBlockContainer);
		referenceData.put("pedCode",selectPedCodeContainer);
		referenceData.put("source", masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE));
		referenceData.put("othersSpecify", editableList.getNewInitiatePedEndorsementDto().get(0).getOthersSpecify());	
		referenceData.put("doctorRemarks", editableList.getNewInitiatePedEndorsementDto().get(0).getDoctorRemarks());
		
		view.setPEDEndorsementTable(editableList,referenceData);
		
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void getPEDAvailableDetails( @Observes@CDIEvent(GET_PED_AVAILABLE_DETAILS_TEAM_LEAD) final ParameterDTO parameters){
	
		WeakHashMap<Integer,Object> inputMap = (WeakHashMap<Integer,Object>)parameters.getPrimaryParameter();
		
		ViewPEDTableDTO newInitiatePedDTO = (ViewPEDTableDTO)parameters.getSecondaryParameters()[0];
	   		
	   		Boolean isPedAvailable = pedQueryService.getPEDAvailableDetailsByICDChapter(inputMap);
	   		
	   		if(isPedAvailable){
	   			view.showPEDAlreadyAvailable(SHAConstants.PED_ALREADY_RAISED_FOR_SAME_ICD_CHAPTER);
	   			view.resetPEDDetailsTable(newInitiatePedDTO);
	   		}	
	}
	
	public void getPedAlreadyAvailableBySuggestion(@Observes @CDIEvent(TL_GET_PED_ALREADY_AVAILABLE) final ParameterDTO parameters)
	{
		Long suggestionKey = (Long) parameters.getPrimaryParameter();
		
		Long intimateKey = (Long) parameters.getSecondaryParameters()[0];
		
		Long insuredKey = (Long) parameters.getSecondaryParameters()[0];
		
//		Long pedInitiateKey = parameters.getSecondaryParameters()[1] != null ? (Long) parameters.getSecondaryParameters()[1] : null;
		
		boolean pedAvailable = false;
		
		/*if(suggestionKey != null && intimateKey != null){
			pedAvailable = pedQueryService.getPEDAvailableDetails(suggestionKey, intimateKey, pedInitiateKey);
		}*/
		
		Intimation intimationObj = intimationService.getIntimationByKey(intimateKey);
		
		List<Insured> insuredListByPolicyKey = insuredService.getInsuredListByPolicyNo(String.valueOf(intimationObj.getPolicy().getKey()));
		boolean isdeletedPedAvailable = pedQueryService.getPEDAvailableDetails(ReferenceTable.PED_SUGGESTION_SUG006, insuredKey);
		
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
					&& intimationObj.getPolicy().getPolicyType() != null 
					&& ReferenceTable.FLOATER_POLICY.equals(intimationObj.getPolicy().getPolicyType())){
				
				OldInitiatePedEndorsement pedObj = pedQueryService.getPedDetailsByPolicyKey(ReferenceTable.PED_SUGGESTION_SUG004, intimationObj.getPolicy().getKey());
				pedAvailable = pedObj != null && pedObj.getStatus() != null && pedObj.getStatus().getKey() != null && ReferenceTable.ENDORSEMENT_APPROVED_BY_PREMIA.equals(pedObj.getStatus().getKey()) ? true : false;
				
			}		
			else if(intimationObj != null && intimationObj.getPolicy() != null 
					&& intimationObj.getPolicy().getPolicyType() != null 
					&& ReferenceTable.INDIVIDUAL_POLICY.equals(intimationObj.getPolicy().getPolicyType())) {
				OldInitiatePedEndorsement pedObj = pedQueryService.getPedDetailsBySuggestionKeyForInsured(ReferenceTable.PED_SUGGESTION_SUG004, insuredKey);
				
				pedAvailable = pedObj != null && pedObj.getStatus() != null && pedObj.getStatus().getKey() != null && ReferenceTable.ENDORSEMENT_APPROVED_BY_PREMIA.equals(pedObj.getStatus().getKey()) ? true : false;  
			}					
						
		}
		else{
			
			if(!isdeletedPedAvailable){
				
//				pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(suggestionKey, intimationObj.getPolicy().getKey());
				
				if(!isdeletedPedAvailable && suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG002)){
					if(!pedAvailable) {
						pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(ReferenceTable.PED_SUGGESTION_SUG010, intimationObj.getPolicy().getKey());
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
						pedAvailable = pedQueryService.getPEDAvailableDetails(ReferenceTable.PED_SUGGESTION_SUG006, insuredKey);
					}
					else{
						pedAvailable = true; // Single Insured can not be deleted so always true.
					}
				}
				else if(!isdeletedPedAvailable && suggestionKey.equals(ReferenceTable.PED_SUGGESTION_SUG010)){
					pedAvailable = pedQueryService.getPEDAvailableDetailsByPolicyKey(ReferenceTable.PED_SUGGESTION_SUG002, intimationObj.getPolicy().getKey());					
				}
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
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_PED_TL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_PED_TL_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
}
