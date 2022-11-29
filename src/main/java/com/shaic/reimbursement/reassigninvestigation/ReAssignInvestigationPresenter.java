package com.shaic.reimbursement.reassigninvestigation;

import java.util.Date;
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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IntimationService;
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
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(ReAssignInvestigationWizard.class)
public class ReAssignInvestigationPresenter extends
		AbstractMVPPresenter<ReAssignInvestigationWizard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String REASSIGN_INVESTIGATION_SUBMIT = "ReAssign Investigation Submit";
	
	public static final String VALIDATE_RE_ASSIGN_INV_COUNT = "Validate Reassign Inv. Count";

	@Inject
	private InvestigationService investigationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_REASSIGN_INVESTIGATION_USER_RRC_REQUEST = "validate_re_assign_investigation_user_rrc_request";
	
	public static final String PROCESS_REASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "reassign_investigation_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_REASSIGN_INVESTIGATION_SAVE_RRC_REQUEST_VALUES = "reassign_investigation_save_rrc_request_values";
	
	public static final String PROCESS_REASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_reassign_investigation_load_rrc_request_sub_category_values";
	
	public static final String PROCESS_REASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_reassign_investigation_load_rrc_request_source_values";
	
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_REASSIGN_INVESTIGATION_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_REASSIGN_INVESTIGATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_REASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
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
			@Observes @CDIEvent(VALIDATE_RE_ASSIGN_INV_COUNT) final ParameterDTO parameters) {
		
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
						view.showAssignCountAlert(alertMsg.toString());
					} else {
						bean.setIsPrivateInvAllow(true);
					}
				}
				else{
					bean.setIsPrivateInvAllow(true);

				}
			}
			
			
		}
	}
	
	public void submitWizard(
			@Observes @CDIEvent(REASSIGN_INVESTIGATION_SUBMIT) final ParameterDTO parameters) {
		AssignInvestigatorDto bean = (AssignInvestigatorDto) parameters
				.getPrimaryParameter();
		
//		Map<String, Object> parentWrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
//		Long parent_WK_key = parentWrkFlowMap.get(SHAConstants.WK_KEY) != null ? Long.valueOf(String.valueOf(parentWrkFlowMap.get(SHAConstants.WK_KEY))) : null;
//		Investigation investigation = investigationService
//				.getByInvestigationKey(bean.getKey());
		
//		List<AssignInvestigatorDto> multiInvestigatorListDto = bean.getMultipleInvestigatorList();
//		if(multiInvestigatorListDto != null && !multiInvestigatorListDto.isEmpty()){
//			for (AssignInvestigatorDto element : multiInvestigatorListDto) {
	


				if (bean.getMultipleInvestigatorList().get(0) != null && bean.getMultipleInvestigatorList().get(0).getInvestigatorNameListSelectValue() != null
						&& bean.getMultipleInvestigatorList().get(0).getInvestigatorNameListSelectValue().getId() != null) {
					TmpInvestigation tmpInvestigation = investigationService
							.getRepresentativeListByInvestigationKey(bean.getMultipleInvestigatorList().get(0)
									.getInvestigatorNameListSelectValue()
									.getId());
					
					bean.getMultipleInvestigatorList().get(0).setGender(tmpInvestigation!=null?tmpInvestigation.getGender():null);
					bean.getMultipleInvestigatorList().get(0).setEmail(tmpInvestigation!=null?tmpInvestigation.getEmailID():null);
					MasPrivateInvestigator privateInvestigation =  investigationService
							.getPrivateRepresentativeListByInvestigationKey(bean.getMultipleInvestigatorList().get(0)
									.getInvestigatorNameListSelectValue()
									.getId());

					if (tmpInvestigation != null || privateInvestigation != null) {

//						AssignedInvestigatiorDetails assignedDetails = new AssignedInvestigatiorDetails();
						
						AssignedInvestigatiorDetails assignedDetails = investigationService.getAssignedInvestigByKey(bean.getKey());
						
						Investigation investigation = assignedDetails.getInvestigation();
						
						bean.setUhidIpNo(investigation.getUhidIpNo()!=null?investigation.getUhidIpNo():"");

						
						/*assignedDetails.setInvestigatorCode(tmpInvestigation
								.getInvestigatorCode());
						assignedDetails.setInvestigatorName(tmpInvestigation
								.getInvestigatorName());
						assignedDetails.setAssignedFrom(bean.getMultipleInvestigatorList().get(0).getReassignedFrom());
						assignedDetails.setReassignComments(bean.getMultipleInvestigatorList().get(0).getReassignComment());
						assignedDetails.setReassignedDate(bean.getMultipleInvestigatorList().get(0).getReassignedDate());*/
						if(privateInvestigation != null) {
							assignedDetails.setInvestigatorCode(privateInvestigation
									.getPrivateInvestigationKey().toString());
							assignedDetails.setInvestigatorName(privateInvestigation
									.getInvestigatorName());
							assignedDetails.setAssignedFrom(bean.getMultipleInvestigatorList().get(0).getReassignedFrom());
							assignedDetails.setReassignComments(bean.getMultipleInvestigatorList().get(0).getReassignComment());
							assignedDetails.setReassignedDate(bean.getMultipleInvestigatorList().get(0).getReassignedDate());
							if(privateInvestigation.getZoneCode()!=null){
							assignedDetails.setZoneCode(privateInvestigation.getZoneCode().toString());
							}
							if(bean.getMultipleInvestigatorList().get(0).getZoneSelectValue().getValue() !=null){
								System.out.println("******************************"+bean.getMultipleInvestigatorList().get(0).getZoneSelectValue().getValue());
								
								MasInvZone masInvZone = masterService.getMasInvZoneName(bean.getMultipleInvestigatorList().get(0).getZoneSelectValue().getValue());
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
							assignedDetails.setAssignedFrom(bean.getMultipleInvestigatorList().get(0).getReassignedFrom());
							assignedDetails.setReassignComments(bean.getMultipleInvestigatorList().get(0).getReassignComment());
							assignedDetails.setReassignedDate(bean.getMultipleInvestigatorList().get(0).getReassignedDate());
							assignedDetails.setZoneCode(bean.getMultipleInvestigatorList().get(0).getZoneSelectValue().getId().toString());
						}
						assignedDetails.setModifiedBy(bean.getUserName());
						assignedDetails.setModifiedDate(bean.getMultipleInvestigatorList().get(0).getReassignedDate());
						Status status = preauthService
								.getStatusByPreauth(ReferenceTable.RE_ASSIGN_INVESTIGATION_STATUS);
						assignedDetails.setStatus(status);
						if (bean.getMultipleInvestigatorList().get(0).getAllocationToSelectValue() != null) {
							SelectValue allocationToSelectValue = bean.getMultipleInvestigatorList().get(0)
									.getAllocationToSelectValue();
							MastersValue allocationTo = new MastersValue();
							allocationTo
									.setKey(allocationToSelectValue.getId());
							allocationTo.setValue(allocationToSelectValue
									.getValue());
							assignedDetails.setAllocationTo(allocationTo);
						}
						if (bean.getMultipleInvestigatorList().get(0).getStateSelectValue() != null) {
							State stateObj = masterService.getStateByKey(bean.getMultipleInvestigatorList().get(0).getStateSelectValue().getId());
							assignedDetails.setState(stateObj);
						}
						if (bean.getMultipleInvestigatorList().get(0).getCitySelectValue() != null) {
							CityTownVillage cityObj = masterService.getCityByKey(bean.getMultipleInvestigatorList().get(0).getCitySelectValue().getId());
							assignedDetails.setCity(cityObj);
						}
						String docToken = investigationService.generateReAssignAuthorizationLetter(bean);
						assignedDetails.setFileToken(null != docToken ? docToken: null);

						investigationService.createAssignInvestigator(
								assignedDetails, bean);
					}
					view.buildSuccessLayout();
				}
//			}
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_REASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_REASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
}
