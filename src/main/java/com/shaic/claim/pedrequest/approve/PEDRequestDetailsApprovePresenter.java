package com.shaic.claim.pedrequest.approve;

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
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.viewPedEndorsement.PedEndorsementMapper;
import com.shaic.domain.Claim;
import com.shaic.domain.InsuredPedDetails;
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

@ViewInterface(PEDRequestDetailsApproveView.class)
public class PEDRequestDetailsApprovePresenter extends
		AbstractMVPPresenter<PEDRequestDetailsApproveView> {
	
	public static final String QUERY_BUTTON="PED Approve Query";
	public static final String REFER_BUTTON="PED Approve Refer to Specialist";
	public static final String APPROVE_BUTTON="PED Approve Send to Approve";
	public static final String WATCH_BUTTON="PED Approve Add to WatchList";
	public static final String REJECT_BUTTON="PED Approve rejection";
	
	public static final String ESCALATE_BUTTON="PED Approver To TL Eascalation";
	
	public static final String SET_FIRST_TABLE = "Set data for Ped request approve table";
	
	public static final String SET_FIELD_DATA="set data for ped query approval";
	
	public static final String QUERY_REMARKS="query remark(Approver)";
	
	public static final String SPECIALIST_REMARKS="Specialist Remarks updated in PED approver";
	
	public static final String WATCHLIST_REMARKS="Watchlist Remarks updated in PED Approver";
	
	public static final String ESCALATE_REMARKS="Escalate Remarks updated in PED Approver";
	
	public static final String SET_MODIFIED_DATA="Set modified data to all fields";
	
	
	public static final String REJECTION_REMARKS="Rejection remarks updated";
	
	public static final String APPROVE_REMARKS="Approve Remarks updated";
	
	public static final String SET_SECOND_TABLE="set data to editable table in ped approve";
	
	public static final String EDIT_BUTTON = "edit button for current PED in Ped request Details Process";
	
	public static final String GET_ICD_BLOCK="Set data for ICD Block in initiate ped endorsement for ped approver";
	
	public static final String GET_ICD_CODE = "Set data for icd_code in initiate Ped endorsement for ped approver";
	
	public static final String GET_PED_CODE="Set ped code value for view details Endorsement for ped approver";
	
	public static final String GET_PED_AVAILABLE_DETAILS_APPROVER = "Get PED Available Details for Approver";
	
	public static final String GET_PED_ALREADY_AVAILABLE_APPROVER = "Get PED Already Available for the Suggestion for Approvae";
	
	public static final String SHOW_DUP_PED_AVAILABLE_APPROVER = "Duplicate PED Available in Diagnosis Detail Table for Approver";
	
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
	
	public static final String VALIDATE_PED_APPROVE_USER_RRC_REQUEST = "process_ped_approver_user_rrc_request";
	
	public static final String PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "process_ped_approver_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_PED_APPROVE_SAVE_RRC_REQUEST_VALUES = "process_ped_approver_query_save_rrc_request_values";
	
	public static final String VALIDATE_PED_APPROVE_USER_LUMEN_REQUEST = "validate_ped_approve_user_lumen_request";
	
public static final String PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_ped_approve_load_rrc_request_sub_category_values";
	
	public static final String PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_ped_approve_load_rrc_request_source_values";
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PED_APPROVE_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	
	public void preauthLumenRequest(@Observes @CDIEvent(VALIDATE_PED_APPROVE_USER_LUMEN_REQUEST) final ParameterDTO parameters){
		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(newIntimationDto.getIntimationId());
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_PED_APPROVE_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void generateFieldsBasedOnQuery(@Observes @CDIEvent(QUERY_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnQueryClick(isChecked);
	}
	
	public void generateFieldsBasedOnWatchlist(@Observes @CDIEvent(WATCH_BUTTON) final ParameterDTO parameters) {
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnWatchClick(isChecked);
	}
	public void generatefieldsForEdit(@Observes @CDIEvent(EDIT_BUTTON) final ParameterDTO parameters){
		
		OldPedEndorsementDTO tableDTO = (OldPedEndorsementDTO) parameters
				.getPrimaryParameter();
		
		 OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(tableDTO.getKey());
		 
		List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = pedQueryService.getIntitiatePedEndorsementDetails(tableDTO.getKey());
		
		List<ViewPEDTableDTO> pedInitiateDetailsDTOList = null;
		if(intitiatePedEndorsementDetails != null){
		PedEndorsementMapper endormentMapper = PedEndorsementMapper.getInstance();
		pedInitiateDetailsDTOList = endormentMapper.getPedInitiateDetailsDTOList(intitiatePedEndorsementDetails);
		}
		
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
	
	public void generateFieldsBasedOnRefer(@Observes @CDIEvent(REFER_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnReferClick(isChecked);
	}
	
	public void generateFieldsBasedApprove(@Observes @CDIEvent(APPROVE_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnApproveClick(isChecked);
	}
	
	public void generateFieldsBasedEscalation(@Observes @CDIEvent(ESCALATE_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnEscalateClick(isChecked);
	}
	
	public void generateFieldsBasedOnReject(@Observes @CDIEvent(REJECT_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnRejectClick(isChecked);
	}
	
	public void submitClick(
			@Observes @CDIEvent(QUERY_REMARKS) final ParameterDTO parameters) {
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
	
	public void submitSpecialistClick(
			@Observes @CDIEvent(SPECIALIST_REMARKS) final ParameterDTO parameters) {
		PEDRequestDetailsApproveDTO bean = (PEDRequestDetailsApproveDTO) parameters
				.getPrimaryParameter();
		
		SearchPEDRequestApproveTableDTO searchDTO=(SearchPEDRequestApproveTableDTO)parameters.getSecondaryParameter(0,SearchPEDRequestApproveTableDTO.class);
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDTO.getDbOutArray();
		wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_APPROVER_REFER_TO_SPECIALIST_OUTCOME);
			
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
			@Observes @CDIEvent(WATCHLIST_REMARKS) final ParameterDTO parameters) {
		
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
			@Observes @CDIEvent(REJECTION_REMARKS) final ParameterDTO parameters) {
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
			@Observes @CDIEvent(APPROVE_REMARKS) final ParameterDTO parameters) {
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
	
	public void escalateClickApprove(
			@Observes @CDIEvent(ESCALATE_REMARKS) final ParameterDTO parameters) {
		PEDRequestDetailsApproveDTO bean = (PEDRequestDetailsApproveDTO) parameters
				.getPrimaryParameter();
		
		SearchPEDRequestApproveTableDTO searchDTO=(SearchPEDRequestApproveTableDTO)parameters.getSecondaryParameter(0, SearchPEDRequestApproveTableDTO.class);
		
		 Boolean result=pedQueryService.updatedEscalateRemarks(bean,searchDTO);
         
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
			@Observes @CDIEvent(SET_MODIFIED_DATA) final ParameterDTO parameters) {
		
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
			@Observes @CDIEvent(SET_FIELD_DATA) final ParameterDTO parameters) {
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
			@Observes @CDIEvent(SET_FIRST_TABLE) final ParameterDTO parameters) {
//		SearchPEDQueryTableDTO searchFormDTO = (SearchPEDQueryTableDTO) parameters
//				.getPrimaryParameter();
		
		SearchPEDRequestApproveTableDTO searchFormDtO = (SearchPEDRequestApproveTableDTO) parameters
				.getPrimaryParameter();
		Page<OldPedEndorsementDTO> resultList=pedQueryService.search(searchFormDtO.getKey());
		
		view.list(resultList);
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void setEditableTable( @Observes@CDIEvent(SET_SECOND_TABLE) final ParameterDTO parameters){
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
	
	public void getPEDAvailableDetails( @Observes@CDIEvent(GET_PED_AVAILABLE_DETAILS_APPROVER) final ParameterDTO parameters){
		
		WeakHashMap<Integer,Object> inputMap = (WeakHashMap<Integer,Object>)parameters.getPrimaryParameter();
		
		ViewPEDTableDTO newInitiatePedDTO = (ViewPEDTableDTO)parameters.getSecondaryParameters()[0];
	   		
	   		Boolean isPedAvailable = pedQueryService.getPEDAvailableDetailsByICDChapter(inputMap);
	   		
	   		if(isPedAvailable){
	   			view.showPEDAlreadyAvailable(SHAConstants.PED_ALREADY_RAISED_FOR_SAME_ICD_CHAPTER);
	   			view.resetPEDDetailsTable(newInitiatePedDTO);
	   		}	
	}
	
	public void getPedAlreadyAvailableBySuggestion(@Observes @CDIEvent(GET_PED_ALREADY_AVAILABLE_APPROVER) final ParameterDTO parameters)
	{
		Long suggestionKey = (Long) parameters.getPrimaryParameter();
		
		Long intimateKey = (Long) parameters.getSecondaryParameters()[0];
		
//		Long pedInitiateKey = (Long) parameters.getSecondaryParameters()[1];
		
		boolean pedAvailable = false;
		if(suggestionKey != null && intimateKey != null){
			pedAvailable = pedQueryService.getPEDAvailableDetails(suggestionKey, intimateKey/*,pedInitiateKey*/);
		}
		if(pedAvailable){
			view.showPEDAlreadyAvailable("Selected PED Suggestion was already initiated");
		}	
	}
	
	public void showDuplicatePEDAlert( @Observes@CDIEvent(SHOW_DUP_PED_AVAILABLE_APPROVER) final ParameterDTO parameters){
		
		ViewPEDTableDTO newInitiatePedDTO = (ViewPEDTableDTO)parameters.getPrimaryParameter();
		
		view.showPEDAlreadyAvailable(SHAConstants.PED_ALREADY_RAISED_FOR_SAME_ICD_CHAPTER);
		view.resetPEDDetailsTable(newInitiatePedDTO);
		
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
}
