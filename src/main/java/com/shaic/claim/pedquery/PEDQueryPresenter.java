package com.shaic.claim.pedquery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

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
import com.shaic.claim.pedquery.search.SearchPEDQueryTableDTO;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresmentDetailsTable;
import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PEDQueryView.class)
public class PEDQueryPresenter extends AbstractMVPPresenter<PEDQueryView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SET_FIRST_TABLE = "Set data into First Table";
	
	public static final String SET_FIRST_EDIT_TABLE="Set first Edit table";
	
	public static final String SET_SECOND_TABLE="Set second Table list";
	
	public static final String SET_FIELD_DATA="Set data into the field";
	
	public static final String SET_EDIT_DATA="Set Editable data in PED Query";
	
	public static final String SET_TABLE_CLICK_EVENT="Set data by clicking table";
	
	public static final String SET_SECOND_EDIT_TABLE="Set second edit table";
	
	public static final String SUBMIT_BUTTON_CLICK="Submit Button Clicked";
	
	public static final String GET_ICD_BLOCK="Set data for ICD Block in ped query view";
	
	public static final String GET_PED_CODE="Set ped code value for Ped query view page";
	
	public static final String GET_ICD_CODE = "Set data for icd_code in ped Query View page";
	
	@Inject
	private ViewPEDEndoresmentDetailsTable viewPEDEndoresmentDetailsTable;

	@Inject
	private ViewPEDEndoresementDetailsService viewPEDEndoresementDetailsService;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private IntimationService intimationSerivice;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_PROCESS_PED_QUERY_USER_RRC_REQUEST = "process_ped_query_user_rrc_request";
	
	public static final String PROCESS_PED_QUERY_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "process_ped_query_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_PED_QUERY_SAVE_RRC_REQUEST_VALUES = "process_ped_query_save_rrc_request_values";
	
public static final String PROCESS_PED_QUERY_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_ped_query_load_rrc_request_sub_category_values";
	
	public static final String PROCESS_PED_QUERY_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_ped_query_load_rrc_request_source_values";
	
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PROCESS_PED_QUERY_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_PED_QUERY_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_PED_QUERY_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	
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
	
	public void searchClick(
			@Observes @CDIEvent(SET_FIRST_TABLE) final ParameterDTO parameters) {
		SearchPEDQueryTableDTO searchFormDTO = (SearchPEDQueryTableDTO) parameters
				.getPrimaryParameter();
		Page<OldPedEndorsementDTO> resultList=pedQueryService.search(searchFormDTO.getKey());
		
		view.list(resultList,null);
		
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void setFirstEditTable(@Observes @CDIEvent(SET_FIRST_EDIT_TABLE) final ParameterDTO parameters){
		
		OldPedEndorsementDTO editDTO=(OldPedEndorsementDTO)parameters.getPrimaryParameter();
		
		Page<OldPedEndorsementDTO> resultList=pedQueryService.search(editDTO.getKey());
		
		view.list(resultList, null);
		
		
	}
	
	public void getpedCode(@Observes @CDIEvent(GET_PED_CODE) final ParameterDTO parameters)
	{
		Long blockKey = (Long) parameters.getPrimaryParameter();
		//BeanItemContainer<SelectValue> icdCodeContainer = masterService.searchPEDCode(blockKey);
		
		String pedCode=masterService.getPEDCode(blockKey);
		
		view.setPEDCode(pedCode);
	}
	public void setEditableTable( @Observes@CDIEvent(SET_SECOND_TABLE) final ParameterDTO parameters){
		SearchPEDQueryTableDTO searchFormDTO =(SearchPEDQueryTableDTO)parameters.getPrimaryParameter();
		
		//OldPedEndorsementDTO editableList=pedQueryService.searchEditableTableList(searchFormDTO.getKey());
		
		List<NewInitiatePedEndorsementDTO> newInitiatePedDtoList =pedQueryService.getInitiateDetailsPed(searchFormDTO.getKey());
//		OldPedEndorsementDTO editableList=pedQueryService.searchEditableTableList(newInitiatePedDtoList);
		
		for (NewInitiatePedEndorsementDTO newInitiatePedEndorsementDTO : newInitiatePedDtoList) {
			SelectValue icdChapter=masterService.getIcdChapterbyId(newInitiatePedEndorsementDTO.getICDChapterId());
			newInitiatePedEndorsementDTO.setICDChapter(icdChapter);
			SelectValue icdBlock=masterService.getIcdBlock(newInitiatePedEndorsementDTO.getICDBlockId());
			newInitiatePedEndorsementDTO.setICDBlock(icdBlock);
			SelectValue icdCode=masterService.getIcdCodeByKey(newInitiatePedEndorsementDTO.getICDCodeId());
			newInitiatePedEndorsementDTO.setICDCode(icdCode);
			SelectValue pedCode=masterService.getPedCodebyId(newInitiatePedEndorsementDTO.getPedCodeId());
			newInitiatePedEndorsementDTO.setPedCode(pedCode);
			//SelectValue source=masterService
		}
		
		OldPedEndorsementDTO editableList=new OldPedEndorsementDTO();
		editableList.setNewInitiatePedEndorsementDto(newInitiatePedDtoList);
		
		BeanItemContainer<SelectValue> selectValueContainer=masterService.getIcdCodeValue(editableList.getNewInitiatePedEndorsementDto().get(0).getICDBlockId());
		
		BeanItemContainer<SelectValue> selectIcdChapterContainer=masterService.getIcdChapterbyKey(editableList.getNewInitiatePedEndorsementDto().get(0).getICDChapterId());
		
		BeanItemContainer<SelectValue> selectIcdBlockContainer=masterService.getIcdBlockbyKey(editableList.getNewInitiatePedEndorsementDto().get(0).getICDChapterId());
		
		BeanItemContainer<SelectValue> pedCodeContainer=masterService.getPedDescription();
		
		BeanItemContainer<SelectValue> icdChapterContainer=masterService.getICDchapter();
		
		referenceData.put("description", editableList.getNewInitiatePedEndorsementDto().get(0).getDescription());
		referenceData.put("ICDCode", selectValueContainer);
		referenceData.put("ICDChapter",selectIcdChapterContainer);
		referenceData.put("ICDBlock", selectIcdBlockContainer);
		referenceData.put("source", masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE));
		referenceData.put("othersSpecify", editableList.getNewInitiatePedEndorsementDto().get(0).getOthersSpecify());	
		referenceData.put("doctorRemarks", editableList.getNewInitiatePedEndorsementDto().get(0).getDoctorRemarks());
		
		
		view.setPEDEndorsementTable(editableList,referenceData,pedCodeContainer,icdChapterContainer);
		
	}
	
	public void setSecondEditableTable(@Observes@CDIEvent(SET_SECOND_EDIT_TABLE) final ParameterDTO parameters){
		
		OldPedEndorsementDTO editDto=(OldPedEndorsementDTO)parameters.getPrimaryParameter();
		
		List<NewInitiatePedEndorsementDTO> newInitiatePedDtoList =pedQueryService.getInitiateDetailsPed(editDto.getKey());
				
		for (NewInitiatePedEndorsementDTO newInitiatePedEndorsementDTO : newInitiatePedDtoList) {
			SelectValue icdChapter=masterService.getIcdChapterbyId(newInitiatePedEndorsementDTO.getICDChapterId());
			newInitiatePedEndorsementDTO.setICDChapter(icdChapter);
			SelectValue icdBlock=masterService.getIcdBlock(newInitiatePedEndorsementDTO.getICDBlockId());
			newInitiatePedEndorsementDTO.setICDBlock(icdBlock);
			SelectValue icdCode=masterService.getIcdCodeByKey(newInitiatePedEndorsementDTO.getICDCodeId());
			newInitiatePedEndorsementDTO.setICDCode(icdCode);
			SelectValue pedCode=masterService.getPedCodebyId(newInitiatePedEndorsementDTO.getPedCodeId());
			newInitiatePedEndorsementDTO.setPedCode(pedCode);
					//SelectValue source=masterService
		}
		OldPedEndorsementDTO editableList=new OldPedEndorsementDTO();
		editableList.setNewInitiatePedEndorsementDto(newInitiatePedDtoList);
		
		BeanItemContainer<SelectValue> selectValueContainer=masterService.getIcdCodeValue(editableList.getNewInitiatePedEndorsementDto().get(0).getICDBlockId());
			
		BeanItemContainer<SelectValue> selectIcdChapterContainer=masterService.getIcdChapterbyKey(editableList.getNewInitiatePedEndorsementDto().get(0).getICDChapterId());
		
		BeanItemContainer<SelectValue> selectIcdBlockContainer=masterService.getIcdBlockbyKey(editableList.getNewInitiatePedEndorsementDto().get(0).getICDChapterId());
		
		BeanItemContainer<SelectValue> pedCodeContainer=masterService.getPedDescription();
		
		BeanItemContainer<SelectValue> icdChapterContainer=masterService.getICDchapter();
		
		referenceData.put("description", editableList.getNewInitiatePedEndorsementDto().get(0).getDescription());
		referenceData.put("ICDCode", selectValueContainer);
		referenceData.put("ICDChapter",selectIcdChapterContainer);
		referenceData.put("ICDBlock", selectIcdBlockContainer);
		referenceData.put("source", masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE));
		referenceData.put("othersSpecify", editableList.getNewInitiatePedEndorsementDto().get(0).getOthersSpecify());	
		referenceData.put("doctorRemarks", editableList.getNewInitiatePedEndorsementDto().get(0).getDoctorRemarks());
		
		view.setPEDEndorsementTable(editableList,referenceData,pedCodeContainer,icdChapterContainer);
	}
	public void setReferenceData(
			@Observes @CDIEvent(SET_FIELD_DATA) final ParameterDTO parameters) {
		SearchPEDQueryTableDTO searchFormDTO = (SearchPEDQueryTableDTO) parameters
				.getPrimaryParameter();
		
		OldPedEndorsementDTO resultDto=pedQueryService.getPedDetailsByKey(searchFormDTO.getKey());
		
		OldPedEndorsementDTO secondResult=pedQueryService.getPedQueryRemarks(resultDto.getKey());
		
        OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(resultDto.getKey());
        
        Claim claim=initiate.getClaim();
        
        ClaimDto claimDto=new ClaimDto();
        claimDto = ClaimMapper.getInstance().getClaimDto(claim);
        claimDto.setClaimId(claim.getClaimId());
        MastersValue currency=claim.getCurrencyId();
        SelectValue currencyId=new SelectValue();
        currencyId.setId(currency.getKey());
        currencyId.setValue(currency.getValue());
        
        claimDto.setCurrencyId(currencyId);
        
        
        
		NewIntimationDto intimationDto=intimationSerivice.getIntimationDto(initiate.getIntimation());
		
//		intimationDto.setPolicy(initiate.getpolicy());
//		intimationDto.setCpuCode(initiate.getIntimation().getCpuCode().getKey());
//		
//		Hospitals hospitalDetails=hospitalService.getHospitalById(initiate.getIntimation().getHospital());
		
		//intimationDto.setHospitalName(hospitalDetails.getName());
		
		if(secondResult!=null){
			resultDto.setReasonforReferring(secondResult.getReasonforReferring());
		}
		
		BeanItemContainer<SelectValue> selectValueContainer=masterService.getConversionReasonByValue(ReferenceTable.PED_SUGGESTION);
		BeanItemContainer<SelectValue> pedValueContainer=masterService.getPedDescription();

		view.setReferenceData(resultDto,selectValueContainer,intimationDto,claimDto,pedValueContainer);
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void setTableClickEvent(@Observes @CDIEvent(SET_TABLE_CLICK_EVENT) final ParameterDTO parameters){
		
		OldPedEndorsementDTO editDTO=(OldPedEndorsementDTO)parameters.getPrimaryParameter();
		view.setSearchDtoToTableDto(editDTO);
		
	}
	
	public void setEditReferenceData(@Observes @CDIEvent(SET_EDIT_DATA) final ParameterDTO parameters){
		
		OldPedEndorsementDTO editDTO=(OldPedEndorsementDTO)parameters.getPrimaryParameter();
		
		OldPedEndorsementDTO resultDto=pedQueryService.getPedDetailsByKey(editDTO.getKey());
		
		
        OldPedEndorsementDTO secondResult=pedQueryService.getPedQueryRemarks(editDTO.getKey());
		
        OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(editDTO.getKey());
		
       Claim claim=initiate.getClaim();
        
        ClaimDto claimDto=new ClaimDto();
        claimDto.setClaimId(claim.getClaimId());
        MastersValue currency=claim.getCurrencyId();
        SelectValue currencyId=new SelectValue();
        currencyId.setId(currency.getKey());
        currencyId.setValue(currency.getValue());
        
        claimDto.setCurrencyId(currencyId);
        
		
        NewIntimationDto intimationDto=intimationSerivice.getIntimationDto(initiate.getIntimation());
		
		//intimationDto.setPolicy(initiate.getpolicy());
		
		if(secondResult!=null){
			resultDto.setReasonforReferring(secondResult.getReasonforReferring());
		}
		
		BeanItemContainer<SelectValue> selectValueContainer=masterService.getConversionReasonByValue(ReferenceTable.PED_SUGGESTION);
		
		BeanItemContainer<SelectValue> pedValueContainer=masterService.getPedDescription();

//		view.setReferenceData(resultDto,selectValueContainer,intimationDto,claimDto,pedValueContainer);
		
		view.setReferenceEditData(resultDto, selectValueContainer, intimationDto, claimDto, pedValueContainer);
		
		
	}
	
	public void submitEvent(
			@Observes @CDIEvent(SUBMIT_BUTTON_CLICK) final ParameterDTO parameters) {
		OldPedEndorsementDTO pedDTO = (OldPedEndorsementDTO) parameters.getPrimaryParameter();
		SearchPEDQueryTableDTO searchDTO=(SearchPEDQueryTableDTO)parameters.getSecondaryParameter(0, SearchPEDQueryTableDTO.class);
		Boolean result=pedQueryService.sumbitPedQuery(pedDTO,searchDTO);
		if(result){
			view.result(result);
		}
		
	}
	/*
	public List<HumanTask> getHumanTask(SearchPEDQueryTableDTO bean){
		
		return null;
	}*/

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_PED_QUERY_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_PED_QUERY_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
}
