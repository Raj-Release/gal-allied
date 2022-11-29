package com.shaic.claim.reassignfieldVisitPage;

import java.sql.Timestamp;
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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.fieldVisitPage.FieldVisitDTO;
import com.shaic.claim.fieldVisitPage.FieldVisitPageRepresentativeNameSearchUI;
import com.shaic.claim.fieldVisitPage.SearchRepresentativeMapper;
import com.shaic.claim.fieldVisitPage.SearchRepresentativeNameService;
import com.shaic.claim.fieldVisitPage.SearchRepresentativeTableDTO;
import com.shaic.claim.fieldVisitPage.TmpFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.reassignfieldvisit.search.SearchReAssignFieldVisitTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.OrganaizationUnitService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.State;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.TmpFvR;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;


@ViewInterface(ReAssignFieldVisitView.class)
public class ReAssignFieldVisitPresenter  extends 
AbstractMVPPresenter<ReAssignFieldVisitView> {

	
	
	/*@Inject
	private ReAssignFieldVisitViewImpl fieldVisitPageViewImpl;*/


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String RE_ASSIGN_FVR = "Re Assign FVR Button";
	
	public static final String FIELD_VISIT = "set data into Re Assign fields";
	
	public static final String SEARCH_CITY = "Search City for Re assign";
	
	public static final String SEARCH_BRANCH_OFFICE = "Search Branch Office for Re assign";
	
	public static final String SEARCH_REPRESENTATIVE = "Representative for Re assign";
	
	public static final String RETRIVE_REPRESENTATION = "Retrive Representative for Re assign";
	
	public static final String SEARCH_REPRESENTATVIE_NAME = "Search Representative Name for reassign fvr";
	
	public static final String SUBMIT_BUTTON = "submit button for Reassign FVR";
	
	@EJB
	private FieldVisitRequestService fieldVisitSerivice;
	
	@EJB
	private MasterService masterService;

	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private SearchRepresentativeNameService searchRepresentativeNameService;

	@Inject
	private FieldVisitPageRepresentativeNameSearchUI fieldVisitPageRepresentativeNameSearchUI;
	
	@EJB
	private OrganaizationUnitService organaizationUnitService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private ClaimMapper claimMapper;
	
	@EJB
	private ViewFVRService viewFVRService;
	
	private Intimation intimation;
	
	


	public void generateFieldsBasedOnAssignfvr(
			@Observes @CDIEvent(RE_ASSIGN_FVR) final ParameterDTO parameters) {
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		System.out.println(isChecked);
		view.generateFieldBasedReAssignFVRClick(isChecked);
	}
	
	public void setDataField(
			@Observes @CDIEvent(FIELD_VISIT) final ParameterDTO parameters) {
		NewIntimationDto newIntimationDto = null;
		ClaimDto claimDTO = null;
		List<ViewFVRDTO> fvrDTOList = null;
		SearchReAssignFieldVisitTableDTO searchTableDto = (SearchReAssignFieldVisitTableDTO) parameters
				.getPrimaryParameter();
		
		
		FieldVisitDTO fieldVisitDto = fieldVisitSerivice
				.getDetailsByKey(searchTableDto.getKey());
		
		fieldVisitDto.setUsername(searchTableDto.getUsername());
		
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
				
				
			TmpFvR masFVR = claimService.getMasFVR(fieldVisitRequestList.get(0).getRepresentativeCode());	
				fieldVisitDto.setReasonForAdmission(fieldVisitDto
						.getReasonForAdmission());
				fieldVisitDto.setRepresentativeName(fieldVisitRequestList.get(0).getRepresentativeName());
				fieldVisitDto.setFvrTriggerPoints(fieldVisitRequestList.get(0).getFvrTriggerPoints());
				fieldVisitDto.setExcecutiveComments(fieldVisitRequestList.get(0).getExecutiveComments());
				
				if(masFVR.getMobileNumber() != null){
					fieldVisitDto.setMobileNo(Long.parseLong(masFVR.getMobileNumber()));
				}
				
				if(masFVR.getPhoneNumber() != null){
					fieldVisitDto.setTelNo(Long.parseLong(masFVR.getPhoneNumber()));
				}
				if(fieldVisitRequestList.get(0).getPriority() != null){
					MastersValue priority = fieldVisitRequestList.get(0).getPriority();
					SelectValue selected = new SelectValue();
					selected.setId(priority.getKey());
					selected.setValue(priority.getValue());
					fieldVisitDto.setPrioritySelect(selected);
				}
				
				if(fieldVisitRequestList.get(0).getAllocationTo() != null){
					MastersValue allcTo = fieldVisitRequestList.get(0).getAllocationTo();
					SelectValue selected = new SelectValue();
					selected.setId(allcTo.getKey());
					selected.setValue(allcTo.getValue());
					fieldVisitDto.setAllocateTo(selected);
				}
			}
			
			fieldVisitDto.setKey(searchTableDto.getKey());

			view.setReferenceData(fieldVisitDto, searchTableDto,
					newIntimationDto, claimDTO, fvrDTOList, intimation);
		} /*else {
			fieldVisitPageViewImpl.showFVRNotFoundMessage();
		}*/
	}
	
	public void setSubmitFVR(
			@Observes @CDIEvent(SUBMIT_BUTTON) final ParameterDTO parameters) {
		
		FieldVisitDTO bean = (FieldVisitDTO)parameters.getPrimaryParameter();
		
		FieldVisitRequest updateExistingAssignFVR = fieldVisitSerivice.updateExistingAssignFVR(bean);
		
		if(updateExistingAssignFVR != null){

		    updateExistingAssignFVR.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		    updateExistingAssignFVR.setAssignedDate(new Timestamp(System.currentTimeMillis()));
		    updateExistingAssignFVR.setRepresentativeCode(bean.getRepresentativeCode());
		    updateExistingAssignFVR.setRepresentativeName(bean.getRepresentativeName());
		    updateExistingAssignFVR.setExecutiveComments(bean.getExcecutiveComments());
		    updateExistingAssignFVR.setFvrTriggerPoints(bean.getFvrTriggerPoints());
		    
		    if(bean.getAllocateTo() != null){
		    	MastersValue allocate = new MastersValue();
		    	allocate.setKey(bean.getAllocateTo().getId());
		    	allocate.setValue(bean.getAllocateTo().getValue());
		    	updateExistingAssignFVR.setAllocationTo(allocate);
		    }
		    if(bean.getPrioritySelect() != null){
		    	MastersValue priority = new MastersValue();
		    	priority.setKey(bean.getPrioritySelect().getId());
		    	priority.setValue(bean.getPrioritySelect().getValue());
		    	updateExistingAssignFVR.setPriority(priority);
		    }
		    
		    updateExistingAssignFVR.setKey(null);
		    
		    Boolean result = fieldVisitSerivice.createReAssignFVR(updateExistingAssignFVR);
		    
		    if(result){
		    	view.result();
		    }
		    
		}
		
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

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}


}
