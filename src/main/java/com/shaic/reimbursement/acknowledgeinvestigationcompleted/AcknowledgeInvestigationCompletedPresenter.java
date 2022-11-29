package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import java.util.Date;
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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(AcknowledgeInvestigationCompletedView.class)
public class AcknowledgeInvestigationCompletedPresenter extends
		AbstractMVPPresenter<AcknowledgeInvestigationCompletedView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SET_REFERENCE = "Set Reference for Acknowledgement Investigation Completed";

	public static final String SET_REFERENCE_FOR_INVESTIGATION_DETAILS_TABLE = "SET_REFERENCE_FOR_INVESTIGATION_DETAILS_TABLE";
	
	

	public static final String INVESTIGATION_OBJECT = "Investigation Object";

	public static final String CLAIM_OBJECT = "Claim Object";

	public static final String INVESTIGATION_LIST_OBJECT = "Investigation List Object";

	public static final String CONFIRMED_BY = "confirmed by Reimbursement";

	public static final String SUBMIT_CLICK = "submit click on acknowledgement completed";

	@EJB
	private InvestigationService investigationService;

	@EJB
	private ClaimService claimService;

	@Inject
	private InvestigationDetailsMapper investigationDetailsMapper;

	@EJB
	private HospitalService hospitalService;

	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;

	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_ACKNOWLEDGE_INVESTIGATION_USER_RRC_REQUEST = "validate_acknowledge_investigation_user_rrc_request";
	
	public static final String PROCESS_ACKNOWLEDGE_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "acknowledge_investigation_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_ACKNOWLEDGE_INVESTIGATION_SAVE_RRC_REQUEST_VALUES = "acknowledge_investigation_save_rrc_request_values";
	
	public static final String PROCESS_ACKNOWLEDGE_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_acknowledge_investigation_load_rrc_request_sub_category_values";
	
	public static final String PROCESS_ACKNOWLEDGE_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_acknowledge_investigation_load_rrc_request_source_values";
	 
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_ACKNOWLEDGE_INVESTIGATION_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_ACKNOWLEDGE_INVESTIGATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_ACKNOWLEDGE_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */

	@SuppressWarnings("unchecked")
	public void setReferenceForInvestigationTable(
			@Observes @CDIEvent(SET_REFERENCE_FOR_INVESTIGATION_DETAILS_TABLE) final ParameterDTO parameters) {
		List<Investigation> investigationList = (List<Investigation>) parameters
				.getPrimaryParameter();
		List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList = investigationDetailsMapper
				.getInvestigationDto(investigationList);
		for (InvestigationDetailsReimbursementTableDTO investigationDetailsReimbursementTableDTO : investigationDetailsReimbursementTableDTOList) {
			Hospitals hospitals = hospitalService.getHospitalById(Long
					.parseLong(investigationDetailsReimbursementTableDTO
							.getHospitalName()));
			investigationDetailsReimbursementTableDTO.setHospitalName(hospitals
					.getName());
			if (investigationDetailsReimbursementTableDTO.getInvestigatorCode() != null) {
				TmpInvestigation tmpInvestigation = investigationService
						.getTmpInvestigationByInactiveInvestigatorCode(investigationDetailsReimbursementTableDTO
								.getInvestigatorCode());
				if(tmpInvestigation.getPhoneNumber() != null){
				investigationDetailsReimbursementTableDTO
						.setInvestigatorContactNo(tmpInvestigation
								.getPhoneNumber().toString());
				}else{
					investigationDetailsReimbursementTableDTO.setInvestigatorContactNo("");
				}
			}
			if (investigationDetailsReimbursementTableDTO
					.getInvestigationAssignedDate() != null) {
				Date tempDate = SHAUtils
						.formatTimestamp(investigationDetailsReimbursementTableDTO
								.getInvestigationAssignedDate().toString());
				investigationDetailsReimbursementTableDTO
						.setInvestigationAssignedDate(SHAUtils
								.formatDate(tempDate));
			}
		}
		view.setReferenceForDto(investigationDetailsReimbursementTableDTOList);
		view.setCompleteLayout();
	}

	public void setReference(
			@Observes @CDIEvent(SET_REFERENCE) final ParameterDTO parameters) {
		Map<String, Object> referenceObj = new HashMap<String, Object>();
		List<Investigation> investigationList = null;
		Long investigationKey = (Long) parameters.getPrimaryParameter();
		Investigation investigaiton = investigationService
				.getByInvestigationKey(investigationKey);
		ClaimDto claimDto = claimService.claimToClaimDTO(investigaiton
				.getClaim());
		if (investigaiton != null && investigaiton.getIntimation() != null
				&& investigaiton.getIntimation().getKey() != null) {
			investigationList = investigationService
					.getByInvestigation(investigaiton.getIntimation().getKey());
		}
		BeanItemContainer<SelectValue> confirmedBy = masterService
				.getSelectValueContainer(ReferenceTable.CONFIRMED_BY);
		referenceObj.put(INVESTIGATION_OBJECT, investigaiton);
		referenceObj.put(CLAIM_OBJECT, claimDto);
		referenceObj.put(INVESTIGATION_LIST_OBJECT, investigationList);
		referenceObj.put(CONFIRMED_BY, confirmedBy);
		view.setReference(referenceObj);
		view.setLayout();
	}

	public void submitClick(
			@Observes @CDIEvent(SUBMIT_CLICK) final ParameterDTO parameters) {
		Long investigationKey = (Long) parameters.getPrimaryParameter();
		InvestigationCompletionDetailsDTO investigationCompletionDetailsDTO = (InvestigationCompletionDetailsDTO) parameters
				.getSecondaryParameters()[0];
		Investigation investigation = investigationService
				.getByInvestigationKey(investigationKey);
		investigation.setConfirmedById(investigationCompletionDetailsDTO
				.getComfirmedBy().getId());
//		investigation.setCompletionDate(investigationCompletionDetailsDTO
//				.getDateOfCompletion());
		investigation.setCompletionRemarks(investigationCompletionDetailsDTO
				.getInvestigationCompletionRemarks());
		Status status = preauthService
				.getStatusByPreauth(ReferenceTable.INVESTIGATION_REPLY_RECEIVED);
		Stage stage = preauthService
				.getStageByKey(ReferenceTable.INVESTIGATION_STAGE);
		investigation.setStatus(status);
		investigation.setStage(stage);
		Investigation investigationList = investigationService.updateIvestigation(investigation,SHAConstants.ACKNOWLEDGEMENT_INVESTIGATION,investigationCompletionDetailsDTO.getUserName());
		
		investigationService.updateReimbursementStatus(investigationCompletionDetailsDTO.getRodKey(), status, stage, investigationCompletionDetailsDTO.getUserName());
		
		if(investigationList != null){
			investigationService.setBPMOutcomeForAckInvestigation(investigationList, investigationCompletionDetailsDTO);
		}
		
		view.result();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_ACKNOWLEDGE_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_ACKNOWLEDGE_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
