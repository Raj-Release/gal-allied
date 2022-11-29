package com.shaic.claim.pedrequest.approve.bancspedrequest;

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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApproveDTO;
import com.shaic.claim.pedrequest.approve.bancspedQuery.BancsSearchPEDRequestApproveTableDTO;
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
import com.shaic.domain.PedQueryDetailsTableData;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Notification;

@ViewInterface(BancsPEDRequestDetailsApproveView.class)
public class BancsPEDRequestDetailsApprovePresenter extends
		AbstractMVPPresenter<BancsPEDRequestDetailsApproveView> {
			
	public static final String BANCS_SET_FIRST_TABLE = "Bancs Set data for Ped request approve table";
	
	public static final String BANCS_SET_QUERY_TABLE = "Bancs Set data for Ped Query detail table";

	public static final String BANCS_SET_FIELD_DATA="Bancs set data for ped query approval";
	
	
	public static final String BANCS_SET_MODIFIED_DATA="Bancs Set modified data to all fields";
	
	public static final String BANCS_APPROVE_REMARKS="Bancs Approve Remarks updated";
	
	public static final String BANCS_SET_SECOND_TABLE="Bancs set data to editable table in ped approve";
	
	public static final String BANCS_EDIT_BUTTON = "Bancs edit button for current PED in Ped request Details Process";
	
	public static final String BANCS_GET_ICD_BLOCK="Bancs Set data for ICD Block in initiate ped endorsement for ped approver";
	
	public static final String BANCS_GET_ICD_CODE = "Bancs Set data for icd_code in initiate Ped endorsement for ped approver";
	
	public static final String BANCS_GET_PED_CODE="Bancs Set ped code value for view details Endorsement for ped approver";
	
	public static final String BANCS_GET_PED_AVAILABLE_DETAILS_APPROVER = "Bancs Get PED Available Details for Approver";
	
	public static final String BANCS_GET_PED_ALREADY_AVAILABLE_APPROVER = "Bancs Get PED Already Available for the Suggestion for Approvae";
	
	public static final String BANCS_SHOW_DUP_PED_AVAILABLE_APPROVER = "Bancs Duplicate PED Available in Diagnosis Detail Table for Approver";
	
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
	
	public static final String BANCS_VALIDATE_PED_APPROVE_USER_RRC_REQUEST = "Bancs process_ped_approver_user_rrc_request";
	
	public static final String BANCS_PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "Bancs process_ped_approver_load_rrc_request_drop_down_values";
	
	public static final String BANCS_PROCESS_PED_APPROVE_SAVE_RRC_REQUEST_VALUES = "Bancs process_ped_approver_query_save_rrc_request_values";
	
	public static final String BANCS_VALIDATE_PED_APPROVE_USER_LUMEN_REQUEST = "Bancs validate_ped_approve_user_lumen_request";
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(BANCS_VALIDATE_PED_APPROVE_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	
	public void preauthLumenRequest(@Observes @CDIEvent(BANCS_VALIDATE_PED_APPROVE_USER_LUMEN_REQUEST) final ParameterDTO parameters){
		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(newIntimationDto.getIntimationId());
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(BANCS_PROCESS_PED_APPROVE_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(BANCS_PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	

	public void generatefieldsForEdit(@Observes @CDIEvent(BANCS_EDIT_BUTTON) final ParameterDTO parameters){
		
		OldPedEndorsementDTO tableDTO = (OldPedEndorsementDTO) parameters
				.getPrimaryParameter();
		
		 OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(tableDTO.getKey());
		 
		List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = pedQueryService.getIntitiatePedEndorsementDetails(tableDTO.getKey());
		
		List<ViewPEDTableDTO> pedInitiateDetailsDTOList = null;
		if(intitiatePedEndorsementDetails != null){
		PedEndorsementMapper endormentMapper = PedEndorsementMapper.getInstance();
		pedInitiateDetailsDTOList = endormentMapper.getPedInitiateDetailsDTOList(intitiatePedEndorsementDetails);
		}
		
		BancsPEDRequestDetailsApproveDTO bean = new BancsPEDRequestDetailsApproveDTO();
		 
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

	
	public void getIcdBlock(@Observes @CDIEvent(BANCS_GET_ICD_BLOCK) final ParameterDTO parameters)
	{
		Long chapterKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdBlockContainer = masterService.searchIcdBlockByChapterKey(chapterKey);
		
		view.setIcdBlock(icdBlockContainer);
	}
	
	public void getIcdCode(@Observes @CDIEvent(BANCS_GET_ICD_CODE) final ParameterDTO parameters)
	{
		Long blockKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdCodeContainer = masterService.searchIcdCodeByBlockKey(blockKey);
		
		view.setIcdCode(icdCodeContainer);
	}
	
	public void getpedCode(@Observes @CDIEvent(BANCS_GET_PED_CODE) final ParameterDTO parameters)
	{
		Long blockKey = (Long) parameters.getPrimaryParameter();
		//BeanItemContainer<SelectValue> icdCodeContainer = masterService.searchPEDCode(blockKey);
		
		String pedCode=masterService.getPEDCode(blockKey);
		
		view.setPEDCode(pedCode);
	}


	public void submitClickApprove(
			@Observes @CDIEvent(BANCS_APPROVE_REMARKS) final ParameterDTO parameters) {
		BancsPEDRequestDetailsApproveDTO bean = (BancsPEDRequestDetailsApproveDTO) parameters
				.getPrimaryParameter();
		
		BancsSearchPEDRequestApproveTableDTO searchDTO=(BancsSearchPEDRequestApproveTableDTO)parameters.getSecondaryParameter(0, BancsSearchPEDRequestApproveTableDTO.class);
		
		 Boolean result=pedQueryService.updatedPEDQueryDetails(bean,searchDTO);
         
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
			@Observes @CDIEvent(BANCS_SET_MODIFIED_DATA) final ParameterDTO parameters) {
		
		OldPedEndorsementDTO bean=(OldPedEndorsementDTO)parameters.getPrimaryParameter();
		
		BancsPEDRequestDetailsApproveDTO resultDto=pedQueryService.getPedQueryApproveByKey(bean.getKey());
		
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
			@Observes @CDIEvent(BANCS_SET_FIELD_DATA) final ParameterDTO parameters) {
		BancsSearchPEDRequestApproveTableDTO searchFormDTO = (BancsSearchPEDRequestApproveTableDTO) parameters
				.getPrimaryParameter();
		//SearchPEDQueryTableDTO search=new SearchPEDQueryTableDTO();
		
		BancsPEDRequestDetailsApproveDTO resultDto=pedQueryService.getPedQueryApproveByKey(searchFormDTO.getKey());
		
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
	}
	
	public void searchClick(
			@Observes @CDIEvent(BANCS_SET_FIRST_TABLE) final ParameterDTO parameters) {
	
		BancsSearchPEDRequestApproveTableDTO searchFormDtO = (BancsSearchPEDRequestApproveTableDTO) parameters
				.getPrimaryParameter();
		Page<OldPedEndorsementDTO> resultList=pedQueryService.search(searchFormDtO.getKey());
		
		view.list(resultList);
	}
	
	public void searchPEDQueryClick(
			@Observes @CDIEvent(BANCS_SET_SECOND_TABLE) final ParameterDTO parameters) {
	
		BancsSearchPEDRequestApproveTableDTO searchFormDtO = (BancsSearchPEDRequestApproveTableDTO) parameters
				.getPrimaryParameter();
		Page<BancsPEDQueryDetailTableDTO> resultList=pedQueryService.searchQuery(searchFormDtO.getKey());
		
		view.listQueryDetails(resultList);
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void getPEDAvailableDetails( @Observes@CDIEvent(BANCS_GET_PED_AVAILABLE_DETAILS_APPROVER) final ParameterDTO parameters){
		
		WeakHashMap<Integer,Object> inputMap = (WeakHashMap<Integer,Object>)parameters.getPrimaryParameter();
		
		ViewPEDTableDTO newInitiatePedDTO = (ViewPEDTableDTO)parameters.getSecondaryParameters()[0];
	   		
	   		Boolean isPedAvailable = pedQueryService.getPEDAvailableDetailsByICDChapter(inputMap);
	   		
	   		if(isPedAvailable){
	   			view.showPEDAlreadyAvailable(SHAConstants.PED_ALREADY_RAISED_FOR_SAME_ICD_CHAPTER);
	   			view.resetPEDDetailsTable(newInitiatePedDTO);
	   		}	
	}
	
	public void getPedAlreadyAvailableBySuggestion(@Observes @CDIEvent(BANCS_GET_PED_ALREADY_AVAILABLE_APPROVER) final ParameterDTO parameters)
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
	
	public void showDuplicatePEDAlert( @Observes@CDIEvent(BANCS_SHOW_DUP_PED_AVAILABLE_APPROVER) final ParameterDTO parameters){
		
		ViewPEDTableDTO newInitiatePedDTO = (ViewPEDTableDTO)parameters.getPrimaryParameter();
		
		view.showPEDAlreadyAvailable(SHAConstants.PED_ALREADY_RAISED_FOR_SAME_ICD_CHAPTER);
		view.resetPEDDetailsTable(newInitiatePedDTO);
		
	}
}
