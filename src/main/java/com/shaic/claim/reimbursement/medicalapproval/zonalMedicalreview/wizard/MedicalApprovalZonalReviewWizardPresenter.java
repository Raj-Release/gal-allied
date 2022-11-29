package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard;

import java.util.ArrayList;
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
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.zybnet.autocomplete.server.AutocompleteField;

@ViewInterface(MedicalApprovalZonalReviewWizard.class)
public class MedicalApprovalZonalReviewWizardPresenter  extends
AbstractMVPPresenter<MedicalApprovalZonalReviewWizard> {
	private static final long serialVersionUID = -580332168159230980L;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private InvestigationService investigationService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocRecvdService;
	
	@EJB
	private CreateRODService creatRODService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private HospitalService hospitalService;

	public static final String SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW = "submit_medical_approval_zonal_review";
	
	public static final String LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "load_rrc_request_drop_down_values";
	
	public static final String LOAD_EMPLOYEE_DETAILS = "load_employee_details";
	
	public static final String SAVE_RRC_REQUEST_VALUES = "save_rrc_request_values";
	
	public static final String VALIDATE_ZONAL_USER_RRC_REQUEST = "validate_zonal_user_rrc_request";
	
	protected static final String UPDATE_PREVIOUS_CLAIM_DETAILS = "update_previous_claim_details for zonal review";

	public static final String ZONAL_REVIEW_LUMEN_REQUEST = "zonal_review_lumen_request";

	public static final String ZONAL_MEDICAL_FVR_EVENT = "zonal_medical_fvr_event";
	
	public static final String ZONAL_REVIEW_INITIATE_INV_EVENT = "medical_approval_zonal_review_initiate_invstigation_event";

	public static final String LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "load_rrc_request_sub_category_values";

	public static final String LOAD_RRC_REQUEST_SOURCE_VALUES = "load_rrc_request_source_values";
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW) final ParameterDTO parameters) throws Exception {
		PreauthDTO reimbursementDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		
		// If hospitalization ROD is not submitted then we should not allow other ROD to submit.
		Reimbursement currentReimbursement = creatRODService.getReimbursementObjectByKey(reimbursementDTO.getKey());
		Boolean isFirstRODApproved = creatRODService.getStatusOfFirstROD(reimbursementDTO.getClaimDTO().getKey(), currentReimbursement);
		
		InitiateInvestigationDTO initInvestigationDTO = reimbursementDTO.getInitInvDto();
		String userName = String.valueOf(VaadinSession.getCurrent().getAttribute("UserName"));
		
/*		if (ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS.equals(reimbursementDTO.getStatusKey())) {
			
			Status status = preauthService.getStatusByKey(ReferenceTable.INITIATE_INVESTIGATION);
			Stage stage = preauthService.getStageByKey(ReferenceTable.ZONAL_REVIEW_STAGE);
			
			initInvestigationDTO.setStage(stage);
			initInvestigationDTO.setStatus(status);
			initInvestigationDTO.setTransactionKey(currentReimbursement.getKey());
			Investigation invBean = investigationService.createInvestigation(initInvestigationDTO, userName, "");
			
			if(initInvestigationDTO.getTriggerPointsList() != null && !initInvestigationDTO.getTriggerPointsList().isEmpty()){
				investigationService.submitInitiateLevelInvestigationTriggerPointsDetails(initInvestigationDTO.getTriggerPointsList(), invBean, initInvestigationDTO);
			}
			
			DocumentGenerator docGen = new DocumentGenerator();
			ReportDto reportDto = new ReportDto();
			reportDto.setClaimId(reimbursementDTO.getClaimDTO().getClaimId());
			
			List<DraftInvestigatorDto> dtoList = new ArrayList<DraftInvestigatorDto>();
			DraftInvestigatorDto draftInvestigation = new DraftInvestigatorDto();
			

			draftInvestigation.setClaimDto(reimbursementDTO.getClaimDTO());
			draftInvestigation.getClaimDto().setNewIntimationDto(reimbursementDTO.getNewIntimationDTO());
			draftInvestigation.setDiagnosisName(reimbursementDTO.getPreauthDataExtractionDetails().getDiagnosis());
			
			if(draftInvestigation.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredSumInsured() != null ){
				String amtWords = SHAUtils.getParsedAmount(draftInvestigation.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredSumInsured());
				draftInvestigation.getClaimDto().getNewIntimationDto().setComments(amtWords);
			}
			else{
				draftInvestigation.getClaimDto().getNewIntimationDto().setComments(null);
			}
			
			dtoList.add(draftInvestigation);
			reportDto.setBeanList(dtoList);
			
			String templateName = "InvestigationLetter(R)";
			
			final String filePath = docGen.generatePdfDocument(templateName, reportDto);
			
			draftInvestigation.setDocFilePath(filePath);
			draftInvestigation.setDocType(SHAConstants.DOC_TYPE_DRAFT_INVESTIGATION_LETTER);			
			draftInvestigation.setDocSource(SHAConstants.DOC_SOURCE_DRAFT_INVESTIGATION_LETTER);
			investigationService.uploadInvLetterToDms(draftInvestigation);
			
			if(invBean != null){
				Map<String,Object> workFlowObj = (Map<String,Object>)reimbursementDTO.getDbOutArray();
				workFlowObj.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY,invBean.getKey());
				reimbursementDTO.setInvestigationKey(invBean.getKey());
			}		
		}*/
		
		if(isFirstRODApproved) {
			Reimbursement reimbursement = reimbursementService.submitZonalReview(reimbursementDTO, true);
//			if(reimbursement != null && (reimbursement.getStatus().getKey().equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS))) {
//				dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
//			}
			
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				//try {
					/*Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(reimbursement.getClaim().getIntimation().getHospital());
					String bedCount = reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails().getInpatientBeds();
					if(bedCount != null) {
						PremiaService.getInstance().updateBedsCount(bedCount, hospitalDetailsByKey.getHospitalCode());
					}*/
					
				/*} catch(Exception e) {
					
				}*/
			}
			view.buildSuccessLayout();
		} else {
			view.buildFailureLayout();
		}
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	
	public void loadEmployeeDetails(@Observes @CDIEvent(LOAD_EMPLOYEE_DETAILS) final ParameterDTO parameters){
		AutocompleteField<EmployeeMasterDTO> field = (AutocompleteField<EmployeeMasterDTO>) parameters.getPrimaryParameter();
		String query = parameters.getSecondaryParameter(0, String.class);
		List<EmployeeMasterDTO> employeeList = reimbursementService.getListOfEmployeeDetails(query);
		view.loadEmployeeMasterData(field, employeeList);
		
	}
	
	public void saveRRCRequestValues(@Observes @CDIEvent(SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_ZONAL_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	
	public void showUpdateClaimDetailTable(
			@Observes @CDIEvent(UPDATE_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails = new ArrayList<UpdateOtherClaimDetailDTO>();
		
		if(preauthDTO.getUpdateOtherClaimDetailDTO().isEmpty()){

			//updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey());
			updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetailsDTOForReimbursement(preauthDTO.getKey());
		}else{
			updateOtherClaimDetails = preauthDTO.getUpdateOtherClaimDetailDTO();
		}
	    
	    view.setUpdateOtherClaimsDetails(updateOtherClaimDetails);
	}

	public void preauthLumenRequest(@Observes @CDIEvent(ZONAL_REVIEW_LUMEN_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(preauthDTO.getNewIntimationDTO().getIntimationId());
	}
	
	public void generateFieldVisitLayout(@Observes @CDIEvent(ZONAL_MEDICAL_FVR_EVENT) final ParameterDTO parameters)
	{
		/*view.genertateFieldsBasedOnFieldVisit(masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO),
				null,masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY));*/
	}


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void generateFieldsForInvestigation(
			@Observes @CDIEvent(ZONAL_REVIEW_INITIATE_INV_EVENT) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean investigationAvailable = false;
		Reimbursement reimbObj = ackDocRecvdService.getReimbursementByRodNo(preauthDTO.getRodNumber());
		/*if(null != preauthDTO.getClaimDTO().getClaimType() &&
				preauthDTO.getClaimDTO().getClaimType().getValue().equalsIgnoreCase(SHAConstants.CASHLESS_CLAIM_TYPE)){
			investigationAvailable = investigationService.getInvestigationPendingForClaim(preauthDTO.getClaimKey(),SHAConstants.TRANSACTION_FLAG_CASHLESS,preauthDTO);
			
			if(reimbObj != null && !investigationAvailable){
				investigationAvailable = investigationService.getInvestigationAvailableForTransactionKey(reimbObj.getKey());
			}
		}		
		else if(reimbObj != null){
			investigationAvailable = investigationService.getInvestigationAvailableForTransactionKey(reimbObj.getKey());
		}*/
		investigationAvailable = investigationService.getInvestigationAvailableForClaim(reimbObj.getClaim().getKey());
		preauthDTO.setIsInvestigation(investigationAvailable);
		
		if(! preauthDTO.getIsInvestigation())
		{
			boolean sendToAssignInv = false;
			
			String invBypassAllowed = dbCalculationService.bypassInvestigationAllowed(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
			sendToAssignInv = (SHAConstants.YES_FLAG).equalsIgnoreCase(invBypassAllowed) ? true : false;
			
//				 sendToAssignInv = true; //For Testing Purpose Need to Be commented  TODO 1227
			
			preauthDTO.setDirectToAssignInv(sendToAssignInv);

			if(!preauthDTO.getIsInvsRaised()){
				 
				 
				 view.generateFieldsOnInvtClick(sendToAssignInv);
			 }
			 else
			 {
				 view.confirmationForInvestigation(sendToAssignInv);
			 }
		}
		else
		{
			 
			view.alertMessageForInvestigation();
		}
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
