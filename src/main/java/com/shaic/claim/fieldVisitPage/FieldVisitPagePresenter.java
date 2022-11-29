package com.shaic.claim.fieldVisitPage;

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
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.OrganaizationUnitService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.State;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.TmpFvR;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(FieldVisitPageView.class)
public class FieldVisitPagePresenter extends
		AbstractMVPPresenter<FieldVisitPageView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ASSIGN_FVR = "Assign FVR Button";
	public static final String SKIP_FVR = "Skip FVR Button";

	public static final String FIELD_VISIT = "set data into fields";

	public static final String SUBMIT_EVENT = "Submit Events";

	public static final String SEARCH_CITY = "Search City";
	
	public static final String SEARCH_BRANCH_OFFICE = "Search Branch Office";
	
	public static final String SEARCH_REPRESENTATIVE = "Representative";
	
	public static final String RETRIVE_REPRESENTATION = "Retrive Representative";

	public static final String SEARCH_REPRESENTATVIE_NAME = "Search Representative Name";

	@EJB
	private FieldVisitRequestService fieldVisitSerivice;

	@Inject
	private FieldVisitPageViewImpl fieldVisitPageViewImpl;

	@EJB
	private PreauthService preAuthService;
	
	@EJB
	private OrganaizationUnitService organaizationUnitService;

	@EJB
	private IntimationService intimationService;

	@EJB
	private ClaimService claimService;

	@Inject
	private ClaimMapper claimMapper;

	@EJB
	private ViewFVRService viewFVRService;

	@EJB
	private MasterService masterService;

	@EJB
	private SearchRepresentativeNameService searchRepresentativeNameService;

	@Inject
	private FieldVisitPageRepresentativeNameSearchUI fieldVisitPageRepresentativeNameSearchUI;
	
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;

	private Intimation intimation;
	
	
	
	@EJB
	private ReimbursementService reimbursementServiceForRRC;
	
	public static final String VALIDATE_FIELD_VISIT_REP_USER_RRC_REQUEST = "process_field_visit_rep_user_rrc_request";
	
	public static final String FIELD_VISIT_REP_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "process_field_visit_rep_load_rrc_request_drop_down_values";
	
	public static final String FIELD_VISIT_REP_SAVE_RRC_REQUEST_VALUES = "process_field_visit_rep_save_rrc_request_values";

	public static final String FIELD_VISIT_REP_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "field_visit_rep_load_rrc_request_sub_category_values";

	public static final String FIELD_VISIT_REP_LOAD_RRC_REQUEST_SOURCE_VALUES = "field_visit_rep_load_rrc_request_source_values";
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_FIELD_VISIT_REP_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementServiceForRRC.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(FIELD_VISIT_REP_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		RRCDTO rrcDTO = preauthDTO.getRrcDTO();
		String rrcRequestNo = "";
		Map<String, Object> wrkFlowMap = (Map<String, Object>) preauthDTO.getDbOutArray();

		if(null != wrkFlowMap)
		{
			String currentQueue = (String)wrkFlowMap.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
			if(SHAConstants.MA_CURRENT_QUEUE.equalsIgnoreCase(currentQueue) || SHAConstants.BILLING_CURRENT_QUEUE.equalsIgnoreCase(currentQueue) ||SHAConstants.FA_CURRENT_QUEUE.equalsIgnoreCase(currentQueue))
			//if(null != humanTask.getPayloadCashless())
			{
				rrcRequestNo = reimbursementServiceForRRC.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
			}
			else //if(null != humanTask.getPayload())
			{
				rrcRequestNo = reimbursementServiceForRRC.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
			}
		}
		
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(FIELD_VISIT_REP_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */

	public void generateFieldsBasedOnAssignfvr(
			@Observes @CDIEvent(ASSIGN_FVR) final ParameterDTO parameters) {
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedAssignFVRClick(isChecked);
	}

	public void searchCity(
			@Observes @CDIEvent(SEARCH_CITY) final ParameterDTO parameters) {
		Long state_id = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<CityTownVillage> cityContainer = new BeanItemContainer<CityTownVillage>(
				CityTownVillage.class);
		BeanItemContainer<SelectValue> citySelectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		cityContainer = masterService.getCityTownVillage(state_id);
		List<CityTownVillage> cityTownVillageList = cityContainer.getItemIds();
		for (CityTownVillage cityTownVillage : cityTownVillageList) {
			SelectValue stateSelectValue = new SelectValue();
			stateSelectValue.setId(cityTownVillage.getKey());
			stateSelectValue.setValue(cityTownVillage.getValue());
			citySelectValueContainer.addBean(stateSelectValue);
		}
		fieldVisitPageRepresentativeNameSearchUI
				.setCityContainer(citySelectValueContainer);
	}
	
	public void searchBrach(
			@Observes @CDIEvent(SEARCH_BRANCH_OFFICE) final ParameterDTO parameters) {
		Long state_Id = (Long) parameters.getPrimaryParameter();
		Long city_Id = (Long) parameters.getSecondaryParameters()[0];
		
		BeanItemContainer<SelectValue> brachContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		List<OrganaizationUnit> organaizationUnitList = organaizationUnitService.getOrganaizationUnitByStateAndCity(state_Id, city_Id);
		
		for (OrganaizationUnit organaizationUnit : organaizationUnitList) {
			SelectValue stateSelectValue = new SelectValue();
			stateSelectValue.setId(organaizationUnit.getKey());
			stateSelectValue.setValue(organaizationUnit.getOrganizationUnitId()+" / "+organaizationUnit.getOrganizationUnitName());
			brachContainer.addBean(stateSelectValue);
		}
		fieldVisitPageRepresentativeNameSearchUI
				.setBranchContainer(brachContainer);
	}
	
	@SuppressWarnings("unchecked")
	public void searchRepresentative(
			@Observes @CDIEvent(SEARCH_REPRESENTATIVE) final ParameterDTO parameters) {
		Map<String,Object> searchRepresentative = (Map<String,Object>) parameters.getPrimaryParameter();
		Long stateId = (Long) searchRepresentative.get("stateId");
		Long cityId = (Long) searchRepresentative.get("cityId");
//		String branchCode = (String) searchRepresentative.get("branchCode");
		Long catgoryId = (Long) searchRepresentative.get("catgoryId");
//		String[] branchCode1 = branchCode.split(" / ");
		List<TmpFvR> tmpFVRList = searchRepresentativeNameService.getRepresentativeList(stateId, cityId, catgoryId);
		List<SearchRepresentativeTableDTO> tmpfvrDTOList = SearchRepresentativeMapper.getInstance().getViewClaimHistoryDTO(tmpFVRList);
		fieldVisitPageRepresentativeNameSearchUI.setTableData(tmpfvrDTOList);		
	}	
	
	public void searchRetriveRepresentative(
			@Observes @CDIEvent(RETRIVE_REPRESENTATION) final ParameterDTO parameters) {
		String representativeCode = (String) parameters.getPrimaryParameter();
		TmpFVRDTO tmpFVRDTOList = (TmpFVRDTO) fieldVisitSerivice.getTmpFVRDetails(representativeCode);
		view.setRepresentativeDetails(tmpFVRDTOList);
	}

	public void searchRepresentativeDetails(
			@Observes @CDIEvent(SEARCH_REPRESENTATVIE_NAME) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> stateContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);		
		BeanItemContainer<SelectValue> allocationToContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		BeanItemContainer<SelectValue> assignToContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		BeanItemContainer<SelectValue> fvrPriorityContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<State> stateList = masterService.getStateListForFVR();
		if (!stateList.isEmpty()) {
			for (State state : stateList) {
				SelectValue stateSelectValue = new SelectValue();
				stateSelectValue.setId(state.getKey());
				stateSelectValue.setValue(state.getValue());
				stateContainer.addBean(stateSelectValue);
			}
		}
		allocationToContainer = masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO);
		assignToContainer = masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO);
		fvrPriorityContainer = masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY);
		
		view.initRepresentativeSearch(stateContainer,
				allocationToContainer,assignToContainer,fvrPriorityContainer);
	}

	public void generateFieldBasedOnAssignfvr(
			@Observes @CDIEvent(SKIP_FVR) final ParameterDTO parameters) {
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedSkipFVRClick(isChecked);
	}

	public void setDataField(
			@Observes @CDIEvent(FIELD_VISIT) final ParameterDTO parameters) {
		NewIntimationDto newIntimationDto = null;
		ClaimDto claimDTO = null;
		List<ViewFVRDTO> fvrDTOList = null;
		SearchFieldVisitTableDTO searchTableDto = (SearchFieldVisitTableDTO) parameters
				.getPrimaryParameter();
		FieldVisitDTO fieldVisitDto = fieldVisitSerivice
				.getDetailsByKey(searchTableDto.getKey());
		Map<String, Object> wrkFlowMap = (Map<String, Object>) searchTableDto.getDbOutArray();
		
		if(null != wrkFlowMap){
			Long key = (Long)wrkFlowMap.get(SHAConstants.PAYLOAD_ROD_KEY);
			if(null != key){
				Reimbursement reimbursement = reimbursementService.getReimbursement(key);
				if(reimbursement != null && reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
					fieldVisitDto.setIsFinancialFvr(true);
				}
			}
		}
		/*HumanTask humanTaskDTO = searchTableDto.getHumanTaskDTO();
		if(humanTaskDTO != null && humanTaskDTO.getPayload() != null){
			if(humanTaskDTO.getPayload().getRod() != null){
				Long key = humanTaskDTO.getPayload().getRod().getKey();
				Reimbursement reimbursement = reimbursementService.getReimbursement(key);
				if(reimbursement != null && reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
					fieldVisitDto.setIsFinancialFvr(true);
				}
			}
		}*/
		if (fieldVisitDto != null) {
			List<FieldVisitRequest> fieldVisitRequestList = fieldVisitSerivice
					.getFieldVisitRequestByKey(fieldVisitDto.getKey());
			if (!fieldVisitRequestList.isEmpty()) {
				Claim claim = claimService
						.getClaimByClaimKey(fieldVisitRequestList.get(0)
								.getClaim().getKey());
				this.intimation = intimationService.getIntimationByKey(claim
						.getIntimation().getKey());
				newIntimationDto = intimationService
						.getIntimationDto(this.intimation);
				claimMapper.getAllMapValues();
				claimDTO = claimMapper.getClaimDto(claim);
				fvrDTOList = viewFVRService.searchFVR(intimation.getKey());
				if (fvrDTOList != null && !fvrDTOList.isEmpty()) {
					fieldVisitDto
							.setFvrCount(String.valueOf(fvrDTOList.size()));
				}
				if (newIntimationDto != null) {
					fieldVisitDto.setReasonForAdmission(newIntimationDto
							.getReasonForAdmission());
				}

				fieldVisitDto.setReasonForAdmission(fieldVisitDto
						.getReasonForAdmission());
			}

			view.setReferenceData(fieldVisitDto, searchTableDto,
					newIntimationDto, claimDTO, fvrDTOList, intimation);
		} else {
			fieldVisitPageViewImpl.showFVRNotFoundMessage();
		}
	}

	@SuppressWarnings("unchecked")
	public void submitEvent(
			@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters) {
		Map<String, Object> mapDTO = (Map<String, Object>) parameters
				.getPrimaryParameter();
		SearchFieldVisitTableDTO searchTableDTO = (SearchFieldVisitTableDTO) mapDTO
				.get("searchTableDTO");
		SearchFieldVisitTableDTO tableDto = (SearchFieldVisitTableDTO) parameters.getSecondaryParameter(1, SearchFieldVisitTableDTO.class);
		
		FieldVisitDTO bean = (FieldVisitDTO) mapDTO.get("fieldVisitDTO");
		if (searchTableDTO != null && bean != null) {
			String option = (String) parameters.getSecondaryParameter(0,
					String.class);
			Boolean result = false;
			if (option.equals("ASSIGN")) {
				result = fieldVisitSerivice.submitAssignDB(bean, option,
						tableDto);
			} else {
				//result = fieldVisitSerivice.submitSkipEvent(bean, option,
					//	searchTableDTO);
				
				result = fieldVisitSerivice.submitSkipEventDB(bean, option,
						searchTableDTO);
			}

			if (result) {
				view.result();
			}
		}

	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(FIELD_VISIT_REP_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(FIELD_VISIT_REP_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
