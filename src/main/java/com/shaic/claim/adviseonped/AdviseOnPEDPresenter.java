package com.shaic.claim.adviseonped;

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
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.adviseonped.search.SearchAdviseOnPEDTableDTO;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedquery.search.SearchPEDQueryTableDTO;
import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PedSpecialist;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(AdviseOnPEDView.class)
public class AdviseOnPEDPresenter extends
		AbstractMVPPresenter<AdviseOnPEDView> {
	
	public static final String SET_FIRST_TABLE = "Set data into table for advice on PED query";
	
	public static final String SET_EDIT_FIRST_TABLE="Set edit data into table";
	
	public static final String SET_FIELD_DATA="set data for Advice On ped query";
	
	public static final String SET_EDIT_DATA="Set edit data for Advise on Ped";
	
	public static final String TABLE_CLICK_EVENT="values are changed when table row is clicked for Advise on ped page";
	
	public static final String SUBMIT_BUTTON="Save data in advice on ped query";
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_ADVISE_ON_PED_USER_RRC_REQUEST = "advise_on_ped_user_rrc_request";
	
	public static final String ADVISE_ON_PED_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "advise_on_ped_load_rrc_request_drop_down_values";
	
	public static final String ADVISE_ON_PED_SAVE_RRC_REQUEST_VALUES = "advise_on_ped_save_rrc_request_values";

	public static final String ADVISE_ON_PED_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "advise_on_ped_load_rrc_request_sub_category_values";

	public static final String ADVISE_ON_PED_LOAD_RRC_REQUEST_SOURCE_VALUES = "advise_on_ped_load_rrc_request_source_values";
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationSerivice;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_ADVISE_ON_PED_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(ADVISE_ON_PED_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(ADVISE_ON_PED_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void searchClick(
			@Observes @CDIEvent(SET_FIRST_TABLE) final ParameterDTO parameters) {
		SearchAdviseOnPEDTableDTO searchFormDTO = (SearchAdviseOnPEDTableDTO) parameters
				.getPrimaryParameter();
		SearchPEDQueryTableDTO formDto=new SearchPEDQueryTableDTO();                        //need to implement
		Page<OldPedEndorsementDTO> resultList=pedQueryService.search(searchFormDTO.getKey());
		
		view.list(resultList);
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void searchEditClick(
			@Observes @CDIEvent(SET_EDIT_FIRST_TABLE) final ParameterDTO parameters) {
		OldPedEndorsementDTO editDTO=(OldPedEndorsementDTO)parameters.getPrimaryParameter();
		Page<OldPedEndorsementDTO> resultList=pedQueryService.search(editDTO.getKey());
		
		view.list(resultList);
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
public void setTableClickEvent(@Observes @CDIEvent(TABLE_CLICK_EVENT) final ParameterDTO parameters){
		
		OldPedEndorsementDTO editDTO=(OldPedEndorsementDTO)parameters.getPrimaryParameter();
		view.setSearchDtoToTableDto(editDTO);
		
	}
	
	public void setReferenceData(
			@Observes @CDIEvent(SET_FIELD_DATA) final ParameterDTO parameters) {
		SearchAdviseOnPEDTableDTO searchFormDTO = (SearchAdviseOnPEDTableDTO) parameters
				.getPrimaryParameter();
		
		
		OldPedEndorsementDTO resultDto=pedQueryService.getPedDetailsByKey(searchFormDTO.getKey());
		
		PedSpecialist pedSpecialist=pedQueryService.getSpecialistDetailsByKey(searchFormDTO.getKey());
		if (pedSpecialist != null) {
			resultDto.setReasonforReferring(pedSpecialist.getReferringReason());
			resultDto.setFileName(pedSpecialist.getFileName());
			resultDto.setTokenName(pedSpecialist.getDocumentToken());
		}
		
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
		
		OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(searchFormDTO.getKey());
		
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
			
			
		if(editableList.getNewInitiatePedEndorsementDto() != null && ! editableList.getNewInitiatePedEndorsementDto().isEmpty()){
			referenceData.put("description", editableList.getNewInitiatePedEndorsementDto().get(0).getDescription());
			referenceData.put("othersSpecify", editableList.getNewInitiatePedEndorsementDto().get(0).getOthersSpecify());	
			referenceData.put("doctorRemarks", editableList.getNewInitiatePedEndorsementDto().get(0).getDoctorRemarks());
		}
		
		referenceData.put("ICDCode", selectValueContainer);
		referenceData.put("ICDChapter",selectIcdChapterContainer);
		referenceData.put("ICDBlock", selectIcdBlockContainer);
		referenceData.put("pedCode",selectPedCodeContainer);
		referenceData.put("source", masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE));
		
		OldPedEndorsementDTO secondResult=pedQueryService.getSpecialistRemarks(resultDto.getKey());
		if(secondResult!=null){
			resultDto.setReasonforReferring(secondResult.getReasonforReferring());
		}
		
		BeanItemContainer<SelectValue> selectValueContainer1=masterService.getConversionReasonByValue(ReferenceTable.PED_SUGGESTION);

		view.setReferenceData(resultDto,selectValueContainer1,intimationDto,referenceData,editableList,claimDto);
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	
	public void setEditReferenceData(
			@Observes @CDIEvent(SET_EDIT_DATA) final ParameterDTO parameters) {
		OldPedEndorsementDTO editDTO=(OldPedEndorsementDTO)parameters.getPrimaryParameter();
		
		
		OldPedEndorsementDTO resultDto=pedQueryService.getPedDetailsByKey(editDTO.getKey());
		
		List<NewInitiatePedEndorsementDTO> newInitiatePedDtoList =pedQueryService.getInitiateDetailsPed(editDTO.getKey());
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
		
		OldInitiatePedEndorsement initiate=pedQueryService.getOldInitiatePedEndorsementDetails(editDTO.getKey());
		
		NewIntimationDto intimationDto=intimationSerivice.getIntimationDto(initiate.getIntimation());
		
		     Claim claim=initiate.getClaim();
	        
	        ClaimDto claimDto=new ClaimDto();
	        claimDto.setClaimId(claim.getClaimId());
	        MastersValue currency=claim.getCurrencyId();
	        SelectValue currencyId=new SelectValue();
	        currencyId.setId(currency.getKey());
	        currencyId.setValue(currency.getValue());
	        
	        claimDto.setCurrencyId(currencyId);
			
			intimationDto.setPolicy(initiate.getpolicy());
		
		referenceData.put("description", editableList.getNewInitiatePedEndorsementDto().get(0).getDescription());
		referenceData.put("ICDCode", selectValueContainer);
		referenceData.put("ICDChapter",selectIcdChapterContainer);
		referenceData.put("ICDBlock", selectIcdBlockContainer);
		referenceData.put("pedCode",selectPedCodeContainer);
		referenceData.put("source", masterService.getSelectValueContainer(ReferenceTable.PED_SOURCE));
		referenceData.put("othersSpecify", editableList.getNewInitiatePedEndorsementDto().get(0).getOthersSpecify());	
		referenceData.put("doctorRemarks", editableList.getNewInitiatePedEndorsementDto().get(0).getDoctorRemarks());
		
		OldPedEndorsementDTO secondResult=pedQueryService.getSpecialistRemarks(resultDto.getKey());
		if(secondResult!=null){
			resultDto.setReasonforReferring(secondResult.getReasonforReferring());
		}
		
		BeanItemContainer<SelectValue> selectValueContainer1=masterService.getConversionReasonByValue(ReferenceTable.PED_SUGGESTION);

		view.setReferenceData(resultDto,selectValueContainer1,intimationDto,referenceData,editableList,claimDto);
		//view.list(searchFieldVisitService.search(searchFormDTO));
	}
	

	public void submitButtonClicked(
			@Observes @CDIEvent(SUBMIT_BUTTON) final ParameterDTO parameters) {
		SearchAdviseOnPEDTableDTO searchDTO=(SearchAdviseOnPEDTableDTO)parameters.getPrimaryParameter();
		
		OldPedEndorsementDTO bean=(OldPedEndorsementDTO)parameters.getSecondaryParameter(0, OldPedEndorsementDTO.class);
		
	    Boolean result=pedQueryService.submitSpecialist(searchDTO, bean);
		
		if(result){
			view.result();
		}
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(ADVISE_ON_PED_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(ADVISE_ON_PED_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
}
