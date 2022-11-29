package com.shaic.reimbursement.assigninvesigation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasInvZone;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.State;
import com.shaic.domain.Status;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(AssignInvestigationWizard.class)
public class AssignInvestigationPresenter extends
		AbstractMVPPresenter<AssignInvestigationWizard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	protected EntityManager entityManager;

	public static final String ASSIGN_INVESTIGATION_SUBMIT = "Assign Investigation Submit For Reimbursement";
	
//	Part of CR R0767
	public static final String VALIDATE_ASSIGN_INV_COUNT = "Validate Assign Investigation Count by Investigator Code";

	@Inject
	private InvestigationService investigationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_ASSIGN_INVESTIGATION_USER_RRC_REQUEST = "validate_assign_investigation_user_rrc_request";
	
	public static final String PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "assign_investigation_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_ASSIGN_INVESTIGATION_SAVE_RRC_REQUEST_VALUES = "assign_investigation_save_rrc_request_values";
	
	public static final String PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_assign_investigation_load_rrc_request_sub_category_values";
	
	public static final String PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_assign_investigation_load_rrc_request_source_values";
	
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_ASSIGN_INVESTIGATION_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_ASSIGN_INVESTIGATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
			BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
			view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

	/**
	 * Part of CR R0767
	 * @param parameters
	 */
	public void validateAssignmentCount(
			@Observes @CDIEvent(VALIDATE_ASSIGN_INV_COUNT) final ParameterDTO parameters) {
		
		AssignInvestigatorDto bean = (AssignInvestigatorDto) parameters
				.getPrimaryParameter();
		
		List<AssignInvestigatorDto> multiInvestigatorListDto = bean.getMultipleInvestigatorList();
		if(multiInvestigatorListDto != null && !multiInvestigatorListDto.isEmpty()){
			StringBuffer alertMsg = new StringBuffer("");
			for (AssignInvestigatorDto element : multiInvestigatorListDto) {
				if(element.getInvestigatorNameListSelectValue().getCommonValue() != null) {
				int pendingCount = investigationService.getPendingInvCountByInvestigatorCode(element.getInvestigatorNameListSelectValue().getCommonValue());
					if(pendingCount >= element.getMaxCount()){				
						alertMsg.append("For "+element.getInvestigatorName() +", You have assigned Max. Count of Investigation.<br>No.of Investigation assigned is "+pendingCount+".<br>");
						//view.showAssignCountAlert(alertMsg.toString());
					} else{
						bean.setIsPrivateInvAllow(true);
						
					}
					
				}
				else{
					bean.setIsPrivateInvAllow(true);
					
				}
			}
			
			view.showAssignCountAlert(alertMsg.toString());
		}
	}
	
	public void submitWizard(
			@Observes @CDIEvent(ASSIGN_INVESTIGATION_SUBMIT) final ParameterDTO parameters) {
		AssignInvestigatorDto bean = (AssignInvestigatorDto) parameters
				.getPrimaryParameter();
		
		
		Map<String, Object> parentWrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		Long parent_WK_key = parentWrkFlowMap.get(SHAConstants.WK_KEY) != null ? Long.valueOf(String.valueOf(parentWrkFlowMap.get(SHAConstants.WK_KEY))) : null;
		String reimb_req_by = parentWrkFlowMap.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) != null ? String.valueOf(parentWrkFlowMap.get(SHAConstants.PAYLOAD_REIMB_REQ_BY)) : null;
		
		Investigation investigation = investigationService
				.getByInvestigationKey(bean.getKey());
		bean.setUhidIpNo(investigation.getUhidIpNo()!=null?investigation.getUhidIpNo():"");

		List<AssignInvestigatorDto> multiInvestigatorListDto = bean.getMultipleInvestigatorList();
		if(multiInvestigatorListDto != null && !multiInvestigatorListDto.isEmpty()){
			for (AssignInvestigatorDto element : multiInvestigatorListDto) {

				if (null != element.getInvestigatorNameListSelectValue()
						&& null != element.getInvestigatorNameListSelectValue()
								.getId()) {
					TmpInvestigation tmpInvestigation = investigationService
							.getRepresentativeListByInvestigationKey(element
									.getInvestigatorNameListSelectValue()
									.getId());
					MasPrivateInvestigator privateInvestigation =  investigationService
							.getPrivateRepresentativeListByInvestigationKey(element
									.getInvestigatorNameListSelectValue()
									.getId());
					
					element.setGender(tmpInvestigation!=null?tmpInvestigation.getGender():null);
					element.setEmail(tmpInvestigation!=null?tmpInvestigation.getEmailID():null);

					if (investigation != null && (tmpInvestigation != null || privateInvestigation != null)) {

						AssignedInvestigatiorDetails assignedDetails = new AssignedInvestigatiorDetails();

						assignedDetails.setInvestigation(investigation);
						if(privateInvestigation != null){
							assignedDetails.setInvestigatorCode(privateInvestigation
									.getPrivateInvestigationKey().toString());
							assignedDetails.setInvestigatorName(privateInvestigation
									.getInvestigatorName());
							if(privateInvestigation.getZoneCode()!=null){
							assignedDetails.setZoneCode(privateInvestigation.getZoneCode().toString());
							}
							if(privateInvestigation.getZoneName()!=null){
								MasInvZone masInvZone = masterService.getMasInvZoneName(privateInvestigation.getZoneName());
								if(masInvZone !=null){
									assignedDetails.setZoneCode(masInvZone.getKey().toString());
								}
							}
							assignedDetails.setStarCoordinatorName(privateInvestigation.getCordinatorName());
						} else {
							assignedDetails.setInvestigatorCode(tmpInvestigation
									.getInvestigatorCode());
							assignedDetails.setInvestigatorName(tmpInvestigation
									.getInvestigatorName());
							assignedDetails.setZoneCode(element.getZoneSelectValue().getValue());
						}
					
						
						Reimbursement reimbObj = reimbursementService
								.getReimbursementByKey(investigation
										.getTransactionKey());

						if (reimbObj != null) {
							assignedDetails.setReimbursement(reimbObj);
						}
						Date date = new Date();
						assignedDetails.setCreatedDate(date);
						assignedDetails.setCreatedBy(bean.getUserName());
						Stage stage = preauthService
								.getStageByKey(ReferenceTable.INVESTIGATION_STAGE);
						Status status = preauthService
								.getStatusByPreauth(ReferenceTable.ASSIGN_INVESTIGATION);
						assignedDetails.setStage(stage);
						assignedDetails.setStatus(status);
						if (element.getAllocationToSelectValue() != null) {
							SelectValue allocationToSelectValue = element
									.getAllocationToSelectValue();
							MastersValue allocationTo = new MastersValue();
							allocationTo
									.setKey(allocationToSelectValue.getId());
							allocationTo.setValue(allocationToSelectValue
									.getValue());
							assignedDetails.setAllocationTo(allocationTo);
						}
						if (element.getStateSelectValue() != null) {
							State stateObj = masterService.getStateByKey(element.getStateSelectValue().getId());
							assignedDetails.setState(stateObj);
						}
						if (element.getCitySelectValue() != null) {
							CityTownVillage cityObj = masterService.getCityByKey(element.getCitySelectValue().getId());
							assignedDetails.setCity(cityObj);
						}

						// investigation.setInvestigatorCode(tmpInvestigation
						// .getInvestigatorCode());
						// investigation.setInvestigatorName(tmpInvestigation
						// .getInvestigatorName());
						// investigation.setAssignedDate(date);

						investigation.setClaimBackgroundDetails(bean
								.getClaimBackgroundDetails());
						investigation.setInvestigationTriggerPoints(bean
								.getInvestigationTriggerPoints());
						investigation.setStage(stage);
						investigation.setStatus(status);
						
						investigationService.createAssignInvestigator(
								assignedDetails, element);

						// TODO UPDATE INVESTIGATION AND ASSIGNED INVESTIGATOR
						Investigation investigationResult = investigationService
								.updateIvestigation(investigation,
										SHAConstants.ASSIGN_INVESTIGATION,
										bean.getUserName());

						investigationService.updateReimbursementStatus(
								bean.getRodKey(), status, stage,
								bean.getUserName());

						/*
						 * if(investigationResult != null && !
						 * bean.getIsCashless()){
						 * investigationService.submitAssignInvestigationTaskToDB
						 * (investigationResult, bean); }else
						 * if(investigationResult != null &&
						 * bean.getIsCashless()){
						 * investigationService.submitInvestigationBPMForPreauth
						 * (investigationResult,bean); }
						 */
						
						if(null != investigation && null != investigation.getTransactionFlag() && !SHAConstants.TRANSACTION_FLAG_CASHLESS.equalsIgnoreCase(investigation.getTransactionFlag()) 
								&& null != assignedDetails.getReimbursement()){
							Hospitals hospObj = reimbursementService.getHospitalByKey(assignedDetails.getReimbursement().getClaim().getIntimation().getHospital());
							Map<String, Object> wrkFlowMap = SHAUtils.getRevisedPayloadMap(assignedDetails.getReimbursement().getClaim(), hospObj);
							wrkFlowMap.put(SHAConstants.WK_KEY,0);
							wrkFlowMap.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY, investigation.getKey());
							wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, assignedDetails.getReimbursement().getClaim().getCrcFlag());   	
							wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_KEY, element.getKey());   				// Temprary slot for Assigned Investigator key in PayLoad Suggested By Raju.
							wrkFlowMap.put(SHAConstants.PAYLOAD_ROD_KEY,assignedDetails.getReimbursement().getKey());
							wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_ELIGIBILITY,parent_WK_key);
							wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE,assignedDetails.getReimbursement().getClaim().getCrcFlag());
							wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,reimb_req_by);
							String stgSource = parentWrkFlowMap.get(SHAConstants.STAGE_SOURCE) != null ? String.valueOf(parentWrkFlowMap.get(SHAConstants.STAGE_SOURCE)): null;
							if(stgSource != null){
								wrkFlowMap.put(SHAConstants.STAGE_SOURCE,stgSource);
							}	
							
							element.setDbOutArray(wrkFlowMap);
						}
						else {
							
							Preauth preauth = reimbursementService.getPreauthById(investigation.getTransactionKey());
							if(null != preauth){
								Hospitals hospObj = reimbursementService.getHospitalByKey(preauth.getClaim().getIntimation().getHospital());
								Map<String, Object> wrkFlowMap = SHAUtils.getRevisedPayloadMap(preauth.getClaim(), hospObj);
								wrkFlowMap.put(SHAConstants.WK_KEY,0);
								wrkFlowMap.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY, investigation.getKey());
								wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, preauth.getClaim().getCrcFlag());   	
								wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_KEY, element.getKey());   				// Temprary slot for Assigned Investigator key in PayLoad Suggested By Raju.
								//wrkFlowMap.put(SHAConstants.PAYLOAD_ROD_KEY,0);
								wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_ELIGIBILITY,parent_WK_key);
								wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE,preauth.getClaim().getCrcFlag());
								wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,reimb_req_by);
								String stgSource = parentWrkFlowMap.get(SHAConstants.STAGE_SOURCE) != null ? String.valueOf(parentWrkFlowMap.get(SHAConstants.STAGE_SOURCE)): null;
								if(stgSource != null){
									wrkFlowMap.put(SHAConstants.STAGE_SOURCE,stgSource);
								}	
								
								element.setDbOutArray(wrkFlowMap);
							}
							/** The below code is added for PA investigation raised at registration level for death case**/
							else
							{
								Hospitals hospObj = reimbursementService.getHospitalByKey(investigation.getClaim().getIntimation().getHospital());
								Map<String, Object> wrkFlowMap = SHAUtils.getRevisedPayloadMap(investigation.getClaim(), hospObj);
								wrkFlowMap.put(SHAConstants.WK_KEY,0);
								wrkFlowMap.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY, investigation.getKey());
								wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, investigation.getClaim().getCrcFlag());   	
								wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_KEY, element.getKey());   				// Temprary slot for Assigned Investigator key in PayLoad Suggested By Raju.
								//wrkFlowMap.put(SHAConstants.PAYLOAD_ROD_KEY,0);
								wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_ELIGIBILITY,parent_WK_key);
								wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE,investigation.getClaim().getCrcFlag());
								wrkFlowMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY,reimb_req_by);
								String stgSource = parentWrkFlowMap.get(SHAConstants.STAGE_SOURCE) != null ? String.valueOf(parentWrkFlowMap.get(SHAConstants.STAGE_SOURCE)): null;
								if(stgSource != null){
									wrkFlowMap.put(SHAConstants.STAGE_SOURCE,stgSource);
								}	
								
								element.setDbOutArray(wrkFlowMap);
							}
						}

						if (investigation != null) {
							investigationService
									.submitAssignInvestigationTaskToDB(
											investigation, element);
						}
					}
				}
			}
			String docToken = investigationService.generateAuthorizationLetter(bean);
			if (multiInvestigatorListDto != null && !multiInvestigatorListDto.isEmpty()) {
				for (AssignInvestigatorDto element : multiInvestigatorListDto) {
					AssignedInvestigatiorDetails assignedDetails = investigationService.getAssignedInvestigByKey(element.getKey());
					assignedDetails.setFileToken(null != docToken ? docToken: null);
					investigationService.createAssignInvestigator(assignedDetails, element);
				}
			}

			
			investigationService.submitAssignInvestigationTaskToDB(investigation, bean);
			view.buildSuccessLayout();
}
		System.out.println(bean.getAllocationToValue());
	}

	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}



}
