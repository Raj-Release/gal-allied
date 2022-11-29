package com.shaic.claim.pedrequest.process;

import java.util.List;

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
import com.shaic.claim.pedrequest.process.search.SearchPEDRequestProcessTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
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
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PEDRequestDetailsProcessView.class)
public class PEDRequestDetailsProcessPresenter extends
		AbstractMVPPresenter<PEDRequestDetailsProcessView> {

	public static final String QUERY_BUTTON="Query Button";
	public static final String EDIT_BUTTON = "edit button for current PED";
	public static final String REFER_BUTTON="Refer to Specialist";
	public static final String APPROVE_BUTTON="Send to Approve";
	public static final String WATCH_BUTTON= "WatchList";
	public static final String SET_FIRST_TABLE="Set first table values";
	public static final String SET_DATA_FIELD="set data into common carousel";
	public static final String QUERY_REMARKS="Query remarks updated";
	public static final String WATCH_REMARKS="Watch Remarks";
	public static final String REVIEW_REMARKS="Review Remarks";
	public static final String SPECIALIST_REMARKS="Specialist Remarks";
	
	public static final String GET_ICD_BLOCK="Set data for ICD Block in initiate ped endorsement for ped processor";
	
	public static final String GET_ICD_CODE = "Set data for icd_code in initiate Ped endorsement for ped processor";
	
	public static final String GET_PED_CODE="Set ped code value for view details Endorsement for ped processor";
	
	public static final String GET_PED_AVAILABLE_DETAILS_PROCESSOR = "Get PED Available Details for Processor";
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private IntimationService intimationSerivice;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
	
	
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_PED_PROCESS_USER_RRC_REQUEST = "process_ped_processor_user_rrc_request";
	
	public static final String PROCESS_PED_PROCESS_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "process_ped_processor_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_PED_PROCESS_SAVE_RRC_REQUEST_VALUES = "process_ped_processor_query_save_rrc_request_values";

	public static final String PROCESS_PED_PROCESS_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_ped_process_load_rrc_request_sub_category_values";

	public static final String PROCESS_PED_PROCESS_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_ped_process_load_rrc_request_source_values";
	

	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PED_PROCESS_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
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
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_PED_PROCESS_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_PED_PROCESS_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
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
	
	public void generateFieldsBasedOnRefer(@Observes @CDIEvent(REFER_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnReferClick(isChecked);
	}
	
	public void generateFieldsBasedOnWatch(@Observes @CDIEvent(WATCH_BUTTON) final ParameterDTO parameters) {
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnWatchClick(isChecked);
	}
	
	public void generatefieldsForEdit(@Observes @CDIEvent(EDIT_BUTTON) final ParameterDTO parameters){
		
		PEDRequestDetailsProcessDTO bean = (PEDRequestDetailsProcessDTO) parameters
				.getPrimaryParameter();
		
		 OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(bean.getKey());
		 
		List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = pedQueryService.getIntitiatePedEndorsementDetails(bean.getKey());
		
		PedEndorsementMapper endormentMapper = PedEndorsementMapper.getInstance();
		List<ViewPEDTableDTO> pedInitiateDetailsDTOList = endormentMapper.getPedInitiateDetailsDTOList(intitiatePedEndorsementDetails);
		 
		 bean.setNameofPED(initiate.getPedName());
		 MastersValue pedSuggestion = initiate.getPedSuggestion();
		 if(pedSuggestion != null){
			 SelectValue selectedPed = new SelectValue();
			 selectedPed.setId(pedSuggestion.getKey());
			 selectedPed.setValue(pedSuggestion.getValue());
			 bean.setPedSuggestion(selectedPed);
		 }
		 bean.setRemarks(bean.getRemarks());
		 bean.setRepudiationLetterDate(initiate.getRepudiationLetterDate());
		 
		 bean.setPedInitiateDetails(pedInitiateDetailsDTOList);
		
		 view.showEditPanel(bean);
		
	}
	
	public void generateFieldsBasedApprove(@Observes @CDIEvent(APPROVE_BUTTON) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedOnApproveClick(isChecked);
	}
	
	public void generateFieldsBasedSpecialist(@Observes @CDIEvent(SPECIALIST_REMARKS) final ParameterDTO parameters)
	{
		PEDRequestDetailsProcessDTO bean = (PEDRequestDetailsProcessDTO) parameters
				.getPrimaryParameter();
		SearchPEDRequestProcessTableDTO searchDto=(SearchPEDRequestProcessTableDTO)parameters.getSecondaryParameter(0,SearchPEDRequestProcessTableDTO.class);
		
		  Boolean result=pedQueryService.specialistDetails(bean,searchDto);
	      if(result){
	    	  view.result();
	      }
	}
	
	public void generateFieldsBasedWatchlist(@Observes @CDIEvent(WATCH_REMARKS) final ParameterDTO parameters)
	{
		PEDRequestDetailsProcessDTO bean = (PEDRequestDetailsProcessDTO) parameters
				.getPrimaryParameter();
		SearchPEDRequestProcessTableDTO searchDto=(SearchPEDRequestProcessTableDTO)parameters.getSecondaryParameter(0,SearchPEDRequestProcessTableDTO.class);
		
		  Boolean result=pedQueryService.updateWatchlistProcessor(bean,searchDto);
	      if(result){
	    	  view.result();
	      }
		
	}
	
	@SuppressWarnings({ "unused" })
	public void searchClick(
			@Observes @CDIEvent(SET_FIRST_TABLE) final ParameterDTO parameters) {
		SearchPEDRequestProcessTableDTO searchFormDTO = (SearchPEDRequestProcessTableDTO) parameters
				.getPrimaryParameter();
		
//		SearchPEDQueryTableDTO bean=new SearchPEDQueryTableDTO();
		
		Page<PEDRequestDetailsProcessDTO> resultList=pedQueryService.searchPedProcess(searchFormDTO);

		BeanItemContainer<SelectValue> selectValueContainer=masterService.getConversionReasonByValue(ReferenceTable.PED_SUGGESTION);
		
		view.list(resultList);
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void getDataForCarousel(@Observes @CDIEvent(SET_DATA_FIELD) final ParameterDTO parameters){
		
		SearchPEDRequestProcessTableDTO searchFormDTO = (SearchPEDRequestProcessTableDTO) parameters
				.getPrimaryParameter();
		
		  OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(searchFormDTO.getKey());
		
		  Claim claim=initiate.getClaim();
		
		    ClaimDto claimDto=new ClaimDto();
		    claimDto = ClaimMapper.getInstance().getClaimDto(claim);
	        claimDto.setClaimId(claim.getClaimId());
	        MastersValue currency=claim.getCurrencyId();
	        SelectValue currencyId=new SelectValue();
	        currencyId.setId(currency.getKey());
	        currencyId.setValue(currency.getValue());
	       
	        claimDto.setCurrencyId(currencyId);
	        
	        
	        Boolean watchListAvailable = initiate.getWatchListFlag() != null ? initiate.getWatchListFlag().equalsIgnoreCase("Y") ? true : false : false;
	        
	        Boolean isAlreadyWatchList = false;
	        
	        if(initiate.getStatus() != null && initiate.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_PROCESSOR)){
	        	isAlreadyWatchList = true;
	        }
			
	      NewIntimationDto intimationDto=intimationSerivice.getIntimationDto(initiate.getIntimation());
	      
	      List<InsuredPedDetails> pedByInsured = policyService.getPEDByInsured(intimationDto.getInsuredPatient().getInsuredId());
			
		 
			
//			intimationDto.setPolicy(initiate.getpolicy());
		
		view.setReference(intimationDto,claimDto,pedByInsured,isAlreadyWatchList,watchListAvailable);
		
	}
	
	public void reviewRemarks(@Observes @CDIEvent(REVIEW_REMARKS) final ParameterDTO parameters) {
		PEDRequestDetailsProcessDTO bean = (PEDRequestDetailsProcessDTO) parameters
				.getPrimaryParameter();
		SearchPEDRequestProcessTableDTO searchDto=(SearchPEDRequestProcessTableDTO)parameters.getSecondaryParameter(0,SearchPEDRequestProcessTableDTO.class);
		
		      Boolean result=pedQueryService.updatedReviewRemarks(bean,searchDto);
		      if(result){
		    	  view.result();
		      }
		
		
	}
	
	public void queryRemarks(@Observes @CDIEvent(QUERY_REMARKS) final ParameterDTO parameters) {
		PEDRequestDetailsProcessDTO bean = (PEDRequestDetailsProcessDTO) parameters
				.getPrimaryParameter();
		
		SearchPEDRequestProcessTableDTO searchDto=(SearchPEDRequestProcessTableDTO)parameters.getSecondaryParameter(0,SearchPEDRequestProcessTableDTO.class);
		
		      Boolean result=pedQueryService.updateQueryRemarks(bean,searchDto);
		      if(result){
		    	  view.result();
		      }
		
		
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_PED_PROCESS_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_PED_PROCESS_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
}
