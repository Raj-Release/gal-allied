package com.shaic.claim.withdrawPostProcessWizard;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.apache.commons.lang3.ArrayUtils;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cashlessprocess.downsize.search.SearchDownsizeCashLessProcessService;
import com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess.WithDrawPostProcessBillDetailsDTO;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(WithdrawPreauthPostProcessWizard.class)
public class WithdrawPreauthPostProcessWizardPresenter extends AbstractMVPPresenter<WithdrawPreauthPostProcessWizard> {

	/**
	 * 
	 */
	 private static final long serialVersionUID = 1L;

	 public static final String WITHDRAW_POST_PROCESS_SUBMITTED_EVENT = "Withdraw preauth post process sumbit Event";

	 public static final String VALIDATE_WITHDRAW_PREAUTH_USER_RRC_REQUEST = "withdraw_preauth_post_process_user_rrc_request";

	 public static final String WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "withdraw_preauth_post_process_load_rrc_request_drop_down_values";

	 public static final String WITHDRAW_PREAUTH_SAVE_RRC_REQUEST_VALUES = "withdraw_preauth_post_process_save_rrc_request_values";

	 public static final String STANDALONE_WITHDRAW_POST_GENERATE_REMARKS = "Standalone Withdraw post generate Remarks";
	 
	 public static final String BILL_CLASSIFICATION_LOAD_BILL_DETAILS_VALUES = "bill_classification_load_bill_details_values";
	 
	 public static final String BILL_CLASSIFICATION_LOAD_BILL_REFERENCE_VALUE = "bill_classification_load_bill_reference_values";
	 
	 public static final String BILL_CLASSIFICATION_CATEGORY_DROPDOWN_VALUE = "bill_classification_bill_entry_category_values";
	 
	 public static final String BILL_ENTRY_SUBMIT_STATUS = "withdraw_bill_entry_submit_status";
	 
	 public static final String WITHDRAW_LOAD_BILL_DETAILS_VALUES = "withdraw_load_bill_details_values";

	 public static final String PREAUTH_APPROVED_DATE_POST_PROCESS = "set preauth approved date in Withdraw Post process letter";

	 public static final String WITHDRAW_PREAUTH_POST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "withdraw_preauth_post_load_rrc_request_sub_category_values";

	 public static final String WITHDRAW_PREAUTH_POST_LOAD_RRC_REQUEST_SOURCE_VALUES = "withdraw_preauth_post_load_rrc_request_source_values";


	 @EJB
	 private PreMedicalService premedicalService;

	 @EJB
	 private PreauthService preauthService;

	 @EJB
	 private CreateRODService  createRodService;

	 @EJB
	 SearchDownsizeCashLessProcessService downsizePreauthService;

	 @EJB
	 private ReimbursementService reimbursementService;

	 @EJB
	 private MasterService masterService;

	 @EJB
	 private DBCalculationService dbCalculationService;
	 
	 @EJB
	private PreviousPreAuthService previousPreauthService;
	 
	 @EJB
		private  ClaimService claimService;
	 
	 @EJB
		private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	 
	 Map<String, Object> referenceData = new HashMap<String, Object>();


	 public void submitWizard(
			 @Observes @CDIEvent(WITHDRAW_POST_PROCESS_SUBMITTED_EVENT) final ParameterDTO parameters)  throws Exception{


		 WithdrawPreauthPostProcessPageDTO withdrawDTO = (WithdrawPreauthPostProcessPageDTO) parameters.getPrimaryParameter();

		 //			PreauthDTO preauthDto=preauthMapper.getPreauthDTO(withdrawDTO.getPreauth());

		 PreauthDTO preauthDto = withdrawDTO.getPreauthDto();

		 //			preauthDto.setReferenceType(withdrawDTO.getPreauth().getPreauthId());

		 ResidualAmount residualAmount=preauthService.getResidualAmtByPreauthKey(withdrawDTO.getPreauth().getKey());

		 List<Speciality> specialityList=preauthService.findSpecialityByClaimKey(withdrawDTO.getPreauth().getClaim().getKey());

		 List<SpecialityDTO> specialityDtoList=PreauthMapper.getInstance().getSpecialityDTOList(specialityList);
		 for (SpecialityDTO specialityDTO : specialityDtoList) {
			 specialityDTO.setKey(null);
		 }

		 List<ClaimAmountDetails> claimAmountDetailsList=preauthService.findClaimAmountDetailsByPreauthKey(withdrawDTO.getPreauth().getKey());

		 List<NoOfDaysCell> claimAmountDtoList=PreauthMapper.getInstance().getClaimedAmountDetailsDTOList(claimAmountDetailsList);

		 for (NoOfDaysCell noOfDaysCell : claimAmountDtoList) {
			 noOfDaysCell.setKey(null);
		 }

		 List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO = new ArrayList<DiagnosisProcedureTableDTO>();

		 List<Procedure> procedureList=preauthService.findProcedureByPreauthKey(withdrawDTO.getPreauth().getKey());


		 List<ProcedureDTO> procedureDto=PreauthMapper.getInstance().getProcedureMainDTOList(procedureList);
		 for (ProcedureDTO procedureDTO2 : procedureDto) {
			 procedureDTO2.setKey(null);
			 procedureDTO2.setOldApprovedAmount(procedureDTO2.getApprovedAmount());
			 procedureDTO2.setDiffAmount(procedureDTO2.getApprovedAmount());
		 }
		 preauthDto.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(procedureDto);

		 List<PedValidation> pedValidationList=preauthService.findPedValidationByPreauthKey(withdrawDTO.getPreauth().getKey());


		 List<DiagnosisDetailsTableDTO> diagnosisDetails=PreauthMapper.getInstance().getNewPedValidationTableListDto(pedValidationList);

		 for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisDetails) {
			 diagnosisDetailsTableDTO.setKey(null);
			 diagnosisDetailsTableDTO.setOldApprovedAmount(diagnosisDetailsTableDTO.getApprovedAmount());
			 diagnosisDetailsTableDTO.setDiffAmount(diagnosisDetailsTableDTO.getApprovedAmount());

		 }

		 //			
		 //		    for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisDetails) {
		 //				List<DiagnosisPED> diagnosisPed=preauthService.getPEDDiagnosisByPEDValidationKey(diagnosisDetailsTableDTO.getKey());
		 //				List<PedDetailsTableDTO> pedList=preauthMapper.getDiagnosisPEDListDto(diagnosisPed);
		 //				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
		 //					pedDetailsTableDTO.setKey(null);
		 //				}
		 //				diagnosisDetailsTableDTO.setPedList(pedList);
		 //				diagnosisDetailsTableDTO.setKey(null);
		 //			}

		 preauthDto.getPreauthDataExtractionDetails().setDiagnosisTableList(diagnosisDetails);

		 String referenceNo=preauthDto.getReferenceType();

		 String[] splitString=referenceNo.split("/");

		 Integer integerValue=new Integer(splitString[splitString.length-1]);
		 integerValue=integerValue+1;

		 splitString=ArrayUtils.remove(splitString, splitString.length-1);
		 String newReferenceNo="";

		 for (String string : splitString) {
			 newReferenceNo=newReferenceNo+string+"/";
		 }

		 newReferenceNo=newReferenceNo+"00"+integerValue;


		 preauthDto.setWithdrawReason(withdrawDTO.getReasonForWithdraw());
		 preauthDto.setDoctorNote(withdrawDTO.getDoctorNote());	
		 preauthDto.setWithdrawInternalRemarks(withdrawDTO.getWithdrawInternalRemarks());
		 preauthDto.setStatusKey(withdrawDTO.getStatusKey());
		 preauthDto.setStageKey(ReferenceTable.WITHDRAW_STAGE);
		 preauthDto.getPreauthDataExtractionDetails().setReferenceNo(newReferenceNo);
		 preauthDto.setKey(null);
		 preauthDto.setProcessType("W");
		 preauthDto.getResidualAmountDTO().setRemarks(residualAmount.getRemarks());
		 preauthDto.getPreauthDataExtractionDetails().setApprovedAmount(withdrawDTO.getTotalApprovedAmt().toString());
		 preauthDto.getPreauthDataExtractionDetails().setSpecialityList(specialityDtoList);
		 preauthDto.getPreauthDataExtractionDetails().setClaimedDetailsList(claimAmountDtoList);
		 preauthDto.getPreauthMedicalDecisionDetails().setWithdrawInternalRemarks(withdrawDTO.getWithdrawInternalRemarks());
		 preauthDto.getPreauthMedicalDecisionDetails().setDoctorNote(withdrawDTO.getDoctorNote());
//		 preauthDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(withdrawDTO.getMedicalRemarks());
		 preauthDto.getPreauthMedicalDecisionDetails().setWithdrawRemarks(withdrawDTO.getWithdrawRemarks());

		 SelectValue section = new SelectValue();
		 section.setCommonValue(withdrawDTO.getPreauth().getClaim().getClaimSectionCode());
		 
		 SelectValue cover = new SelectValue();
		 cover.setCommonValue(withdrawDTO.getPreauth().getClaim().getClaimCoverCode());
		 
		 SelectValue subCover = new SelectValue();
		 subCover.setCommonValue(withdrawDTO.getPreauth().getClaim().getClaimSubCoverCode());		 
		 
		/* preauthDto.getPreauthDataExtractionDetails().getSectionDetailsDTO().setSection(section);
		 preauthDto.getPreauthDataExtractionDetails().getSectionDetailsDTO().setCover(cover);
		 preauthDto.getPreauthDataExtractionDetails().getSectionDetailsDTO().setSubCover(subCover);*/

		 if(preauthDto.getPreviousPreauthKey() != null){
			 dbCalculationService.reimbursementRollBackProc(preauthDto.getPreviousPreauthKey(),"C");
		 }

		 //premedicalService.saveDuplicatePreauthValues(preauthDto,"W");

		 Preauth preauth=preauthService.submitPreAuth(preauthDto, true);
		 claimService.submitConvertToReimbursementPostProcess(withdrawDTO.getConvertClaimDTO(),"Conversion");
		 if(withdrawDTO.getUpdateBillClassificationValues() != null && !withdrawDTO.getUpdateBillClassificationValues().isEmpty()) {
			 Long docAckKey=0l;
			 List<UploadDocumentDTO> billClasification = withdrawDTO.getUpdateBillClassificationValues();
			 for (UploadDocumentDTO uploadDocumentDTO : billClasification) {
				if(uploadDocumentDTO.getAckDocKey() != null) {
					DocAcknowledgement docAcknowledgement = createRodService.saveWithdrawCashlessBillClassificationDtls(uploadDocumentDTO);
					if(docAcknowledgement.getRodKey() != null){
						dbCalculationService.updateProvisionAmount(docAcknowledgement.getRodKey(), docAcknowledgement.getClaim().getKey());	
					}
					if(docAcknowledgement.getRodKey() != null){
						List<PedValidation> pedDetailsList = createRodService.getDiagnosis(docAcknowledgement.getRodKey());
						if(pedDetailsList != null && ! pedDetailsList.isEmpty()){
							for(PedValidation peddetails : pedDetailsList){
								if((peddetails.getStage().getKey().equals(ReferenceTable.CREATE_ROD_STAGE_KEY) && peddetails.getStatus().getKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)) || peddetails.getStatus().getKey().equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)){
									peddetails.setDeleteFlag(0L);
									peddetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
									createRodService.saveDiagnosisDetails(peddetails);
								}
							}	
						}
					}
				}
			}
		 }
			
		 String outCome = "SUBMIT";

		 if(preauthDto.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
				 || preauthDto.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
				 || preauthDto.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
				 || preauthDto.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)){

			 preauthService.cancellNegotiation(preauthDto);
		 }
		 if(withdrawDTO.getReasonForWithdraw() != null && withdrawDTO.getReasonForWithdraw().getId().equals(ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW)){
			 //				outCome = "NOTADMITTED";

			 outCome = SHAConstants.OUTCOME_FOR_WITHDRAW_PREAUTH_PATIENT_NOT_ADMITTED;

			 String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			 if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				 PremiaService.getInstance().UnLockPolicy(withdrawDTO.getNewIntimationDto().getIntimationId());
			 }
		 } else {
			 outCome = SHAConstants.OUTCOME_FOR_WITHDRAW_PREAUTH_OTHERS;
		 }



		 Preauth preauthById = preauthService.getPreauthById(preauth.getKey());

		 //			dbCalculationService.invokeAccumulatorProcedure(preauthById.getKey());

		 if(preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)) {
			 //				createRodService.stopCashlessReminderLetter(preauth.getClaim().getKey(),preauthDto.getStrUserName(), preauthDto.getStrPassword());
			 //				createRodService.stopCashlessReminderProcess(preauth.getClaim().getKey(), preauthDto.getStrUserName(), preauthDto.getStrPassword());
		 }

		 if(preauth!=null){


			 if(preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)){
				 outCome = SHAConstants.OUTCOME_FOR_STANDALONE_WITHDRAW_REJECT;
			 }

			 //				preauthService.setBPMOutComeForWithdraw(preauthDto, preauthById,outCome);

//			 preauthService.submitDBProcedureForStadWithraw(preauthById.getClaim(), preauthById ,outCome);
			 dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.OTHERS);

			 //     R1135    -     13-04-2018
			 dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.PAN_CARD);
			 //preauthService.setBPMOutComeForWithdraw(preauthDto, preauthById,outCome, false);


			 //				downsizePreauthService.setBPMOutCome(preauth, preauthDto);
		 }

		 view.buildSuccessLayout();
		 //			preauthService.submitPreAuth(preauthDTO, false);

	 }

	 /**
	  * Added for RRC Starts
	  * */
	 public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_WITHDRAW_PREAUTH_USER_RRC_REQUEST) final ParameterDTO parameters){
		 PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		 Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		 view.buildValidationUserRRCRequestLayout(isValid);

	 }


	 public void saveRRCRequestValues(@Observes @CDIEvent(WITHDRAW_PREAUTH_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		 PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		 String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		 view.buildRRCRequestSuccessLayout(rrcRequestNo);

	 }

	 public void loadRRCRequestDropDownValues(
			 @Observes @CDIEvent(WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
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



	 public void getRemarks(
			 @Observes @CDIEvent(STANDALONE_WITHDRAW_POST_GENERATE_REMARKS) final ParameterDTO parameters) {
		 Long id = (Long) parameters.getPrimaryParameter();
		 String decision = (String) parameters.getSecondaryParameters()[0];

		 MasterRemarks remarks = masterService.getRemarks(id);
		 view.setRemarks(remarks,decision);
	 }
	 
	 public void loadBillDetailsValues(
				@Observes @CDIEvent(BILL_CLASSIFICATION_LOAD_BILL_DETAILS_VALUES) final ParameterDTO parameters) {
		 UploadDocumentDTO uploadBillDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		 UploadDocumentDTO uploadDTO = new UploadDocumentDTO();
//		 List<UploadDocumentDTO> listDoc = uploadBillDTO.getBillClassificationTableDTO();
		 /*for (UploadDocumentDTO uploadDocumentDTO : listDoc) {
			if(uploadDocumentDTO.getDocSummaryKey().equals(uploadBillDTO.getDocumentSummaryKey())){
				uploadDTO = uploadDocumentDTO;
			}
		}*/
			/*List<BillEntryDetailsDTO> dtoList = createRodService.getWithDrawClaimBillEntryDetailsList(uploadBillDTO.getDocSummaryKey());
			uploadBillDTO.setBillEntryDetailList(dtoList);
			view.setUploadDTOBillEntryDtls(uploadBillDTO);*/
		 List<BillEntryDetailsDTO> dtoList = createRodService.getBillEntryDetailsList(uploadBillDTO);
		 uploadBillDTO.setBillEntryDetailList(dtoList);
			view.setUploadDTOBillEntryDtls(uploadBillDTO);
	 }
	 
	 public void setUpReference(
				@Observes @CDIEvent(BILL_CLASSIFICATION_LOAD_BILL_REFERENCE_VALUE) final ParameterDTO parameters){
			
			PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
			Boolean isBasedOnPremium = false;
			if(ReferenceTable.getPremiumDeductionProductKeys().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalisationFlag() != null && dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))) {
				isBasedOnPremium = true;
			}
			referenceData.put("sectionDetails", masterService
					.getSectionList(dto.getNewIntimationDTO().getPolicy().getProduct().getKey(),dto.getNewIntimationDTO().getPolicy()));
			referenceData.put("patientStatus", masterService.getSelectValueContainer(ReferenceTable.REIMBURSEMENT_PATIENT_STATUS));
			referenceData.put("previousPreauth", previousPreauthService.search(dto.getClaimKey(), isBasedOnPremium));
			referenceData.put("status", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
			referenceData.put("fileType", masterService
					.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
			
			
			referenceData.put("billClassification", setClassificationValues(dto));
			referenceData.put(SHAConstants.ALL_BILL_CLASSIFICATIONS, masterService.getMasBillClassificationValues());
			
			//referenceData.put("billClassification", masterService.getMasBillClassificationValues());
			
			Double insuredAge = dto.getNewIntimationDTO().getInsuredPatient().getInsuredAge();
			Double sumInsured = dbCalculationService.getInsuredSumInsured(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), dto.getPolicyDto().getKey(),dto.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			if (sumInsured == 0) {
				sumInsured = dto.getPolicyDto().getTotalSumInsured();
			}
			
			String policyPlan = dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";
			
			/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
			if(dto.getNewIntimationDTO().getPolicy().getProduct() != null 
					&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode())
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()) || 
							SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							&& dto.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
				policyPlan = dto.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? dto.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
			}
			
			if(dto.getPreauthDataExtractionDetails().getSection() != null && dto.getPreauthDataExtractionDetails().getSection().getId() != null){
				if(ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(dto.getNewIntimationDTO().getPolicy().getKey(),
							dto.getNewIntimationDTO().getInsuredPatient().getKey(),dto.getNewIntimationDTO().getPolicy().getSectionCode());
					referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSectionForGMC(dto.getPolicyDto().getKey(), sumInsured, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
							dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode())));
				}else{
					referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
							policyPlan, (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode()),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				}
				
			}else{
				if(ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(dto.getNewIntimationDTO().getPolicy().getKey(),
							dto.getNewIntimationDTO().getInsuredPatient().getKey(),dto.getNewIntimationDTO().getPolicy().getSectionCode());
					referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSectionForGMC(dto.getPolicyDto().getKey(), sumInsured, insuredAge,0l,"0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode())));
				}else{
					referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,0l,policyPlan, (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode()),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				}
				
			}
			
			
			referenceData.put("insuredPedList", masterService.getInusredPEDList(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString()));
			
			NewIntimationDto intimationDTO = dto.getNewIntimationDTO();
//			Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey());
			Double balanceSI = 0d;
			List<Double> copayValue = new ArrayList<Double>();
			if(ReferenceTable.getGMCProductList().containsKey(intimationDTO.getPolicy().getProduct().getKey())){
				balanceSI = dbCalculationService.getBalanceSIForGMC(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey());
				copayValue = dbCalculationService.getProductCoPayForGMC(intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getKey());
			}else{
				balanceSI = dbCalculationService.getBalanceSIForReimbursement(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(),dto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
				copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),
						intimationDTO);
			}
			
			
			dto.setBalanceSI(SHAUtils.getHospOrPartialAppAmt(dto, reimbursementService, balanceSI));
			
			Reimbursement reimbursementByKey = ackDocReceivedService.getReimbursementByKey(dto.getKey());
			
			if(reimbursementByKey != null && reimbursementByKey.getDocAcknowLedgement() != null){
				Boolean isSettled = reimbursementService.isSettledPaymentAvailable(reimbursementByKey.getRodNumber());
				DocAcknowledgement docAcknowLedgement = reimbursementByKey.getDocAcknowLedgement();
				Double preHospitalLAlreadyPaid = 0d;
				Double postHospitalAlreadyPaid = 0d;
				Double prepostAlreadyPaid = 0d;
				if(isSettled){
					if(reimbursementByKey.getReconsiderationRequest() != null && reimbursementByKey.getReconsiderationRequest().equalsIgnoreCase("Y") ){
						Double alreadyPaidAmt = 0d;
						List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = ackDocReceivedService.getReimbursementCalculationDetails(dto.getKey());
						for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
							
							if(docAcknowLedgement.getDocumentReceivedFromId() != null && docAcknowLedgement.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
								if(reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() !=null && reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() > 0){
									alreadyPaidAmt += reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() != null ? reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() :0;
								} else {
									alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableToHospAftTDS() != null ? reimbursementCalCulationDetails2.getPayableToHospAftTDS() : 0;
							}
							}else{
								if(reimbursementCalCulationDetails2.getPayableInsuredAfterPremium() != null){
									if(reimbursementCalCulationDetails2.getBillClassificationId() != null && ! reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)
											&& ! reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
										alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableInsuredAfterPremium();
									}
									/*if(reimbursementCalCulationDetails2.getPayableToHospital() != null){
										alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableToHospital();
									}*/
								}
							}
						}
						
						dto.setBalanceSI(dto.getBalanceSI()+alreadyPaidAmt);
						
					}
				}
				
				/*if(reimbursementByKey.getDocAcknowLedgement().getPreHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")) {
				preHospitalLAlreadyPaid = getPreHospitalizationRODValues(dto.getClaimKey(), dto.getKey());
				}
				if(reimbursementByKey.getDocAcknowLedgement().getPostHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")) {
				postHospitalAlreadyPaid = getPostHospitalizationRODValues(dto.getClaimKey(), dto.getKey());
				}
				
				prepostAlreadyPaid = (preHospitalLAlreadyPaid != null ? preHospitalLAlreadyPaid :0) + (postHospitalAlreadyPaid != null ? postHospitalAlreadyPaid :0);
				dto.setBalanceSI(dto.getBalanceSI()+prepostAlreadyPaid);*/
				
				
			}
			
			dto.setProductCopay(copayValue);
			referenceData.put("terminateCover", masterService.getSelectValueContainer(ReferenceTable.TERMINATE_COVER));
			
			referenceData.put("irdaLevel1", masterService.getIRDALevel1Values());
			referenceData.put("commonValues", masterService
					.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
			referenceData.put("reasonForReconsiderationRequest", masterService
					.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION));
			
			referenceData.put("illness", masterService.getSelectValueContainer(ReferenceTable.ILLNESS));
			referenceData.put("criticalIllness", preauthService.getCriticalIllenssMasterValues(dto));
			referenceData.put("section", masterService
					.getSelectValueContainer(ReferenceTable.SECTION));
			
			if(dto.getPreauthDataExtractionDetails().getSection() != null && dto.getPreauthDataExtractionDetails().getSection().getId() != null){
				
				MastersValue master = masterService.getMaster(dto.getPreauthDataExtractionDetails().getSection().getId());
				if(master != null){
					SelectValue section = dto.getPreauthDataExtractionDetails().getSection();
					section.setValue(master.getValue());
				}
				
			}
			
			referenceData.put("roomCategory", masterService.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
			referenceData.put(SHAConstants.BILL_CLASSIFICATION_DETAILS, validateBillClassification(dto.getClaimDTO().getKey()));
			view.setupReferences(referenceData);
		}
	 
	 public List<DocumentDetailsDTO> validateBillClassification(Long claimKey) {
			List<Reimbursement> reimbursementList = ackDocReceivedService.getReimbursementDetailsForBillClassificationValidation(claimKey);
			List<DocumentDetailsDTO> docDTOList = new ArrayList<DocumentDetailsDTO>();
			if(null != reimbursementList && !reimbursementList.isEmpty())
			{
				DocumentDetailsDTO docDTO = null;
				for (Reimbursement reimbursement : reimbursementList) {
					DocAcknowledgement docAck= reimbursement.getDocAcknowLedgement();
					docDTO = new DocumentDetailsDTO();
					docDTO.setHospitalizationFlag(docAck.getHospitalisationFlag());
					docDTO.setPartialHospitalizationFlag(docAck.getPartialHospitalisationFlag());
					docDTO.setPreHospitalizationFlag(docAck.getPreHospitalisationFlag());
					docDTO.setPostHospitalizationFlag(docAck.getPostHospitalisationFlag());
					docDTO.setLumpSumAmountFlag(docAck.getLumpsumAmountFlag());
					docDTO.setAddOnBenefitsPatientCareFlag(docAck.getPatientCareFlag());
					docDTO.setAddOnBenefitsHospitalCashFlag(docAck.getHospitalCashFlag());
					if(null != reimbursement.getStatus()) {
						docDTO.setStatusId(reimbursement.getStatus().getKey());
					}
					
					docDTOList.add(docDTO);
				}
			}
			
			return docDTOList;
		}
	 
	 public BeanItemContainer<SelectValue> setClassificationValues(PreauthDTO preauthDTO)
		{
			BeanItemContainer<SelectValue> beanContainer = masterService.getMasBillClassificationValues();
			
			List<SelectValue> selectValueList = beanContainer.getItemIds();
			
			List<SelectValue> finalClassificationList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> classificationValueContainer = null;
			/*if(null != selectValueList && !selectValueList.isEmpty())
			{
				 classificationValueContainer = new BeanItemContainer<SelectValue>(
							SelectValue.class);
				for (SelectValue selectValue : selectValueList) {		
					//if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag()))
					if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag())
							|| (null != preauthDTO.getIsHospitalizationRepeat() && preauthDTO.getIsHospitalizationRepeat())
							)
					
					//	if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag()))
						if((ReferenceTable.HOSPITALIZATION).equals(selectValue.getId()) && ((null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag())
								|| (null != preauthDTO.getIsHospitalizationRepeat() && preauthDTO.getIsHospitalizationRepeat()) || (null != preauthDTO.getPartialHospitalizaionFlag() &&  preauthDTO.getPartialHospitalizaionFlag())
								))
						{
							finalClassificationList.add(selectValue);
						}
						else if((ReferenceTable.PRE_HOSPITALIZATION).equals(selectValue.getId()) && (null != preauthDTO.getPreHospitalizaionFlag() && preauthDTO.getPreHospitalizaionFlag()))
						{
							finalClassificationList.add(selectValue);
						}
						else if((ReferenceTable.POST_HOSPITALIZATION).equals(selectValue.getId()) && (null != preauthDTO.getPostHospitalizaionFlag() && preauthDTO.getPostHospitalizaionFlag()))
						{
							finalClassificationList.add(selectValue);
						}
						else if((ReferenceTable.LUMPSUM).equals(selectValue.getId()) && (null != preauthDTO.getLumpSumAmountFlag() && preauthDTO.getLumpSumAmountFlag()))
						{
							finalClassificationList.add(selectValue);
						}
						else if((ReferenceTable.OTHER_BENEFIT).equals(selectValue.getId()) && (null != preauthDTO.getOtherBenefitsFlag() && preauthDTO.getOtherBenefitsFlag()))
						{
							finalClassificationList.add(selectValue);
						}
					
				}
				classificationValueContainer.addAll(finalClassificationList);
			}*/
//			return classificationValueContainer;
			return beanContainer;
		}
	 
		
		public void setUpCategoryValues(
				@Observes @CDIEvent(BILL_CLASSIFICATION_CATEGORY_DROPDOWN_VALUE) final ParameterDTO parameters) {
			Long billClassificationKey = (Long) parameters.getPrimaryParameter();
			SelectValue claimType = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
			//BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey,claimType);
			Long productKey = (Long) parameters.getSecondaryParameter(2, Long.class);
			String subCoverValue = (String) parameters.getSecondaryParameter(3, String.class);
			Boolean isDomicillary = (Boolean) parameters.getSecondaryParameter(4, Boolean.class);
			String intimationNo = (String) parameters.getSecondaryParameter(5, String.class);
			
			Intimation intimationObj = preauthService.getIntimationByNo(intimationNo);
			
			BeanItemContainer<SelectValue> selectValueContainer = masterService
					.getMasBillCategoryValuesForZonalAndMedical(billClassificationKey,claimType,isDomicillary);
			List<SelectValue> selectValueList = selectValueContainer.getItemIds();
			
			List<SelectValue> finalCategoryList = new ArrayList<SelectValue>();
			List<SelectValue> protaCategoryList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> categoryValueContainer = null;
			if(null != selectValueList && !selectValueList.isEmpty())
			{
				categoryValueContainer = new BeanItemContainer<SelectValue>(
							SelectValue.class);
				for (SelectValue selectValue : selectValueList) {	
					if(!ReferenceTable.getPrePostNatalMap().containsKey(selectValue.getId()))
					{
						if(ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(productKey)){
							if(billClassificationKey.equals(12L)){
								if(selectValue.getId().equals(ReferenceTable.COMPASSIONATE_TRAVEL) 
										|| selectValue.getId().equals(ReferenceTable.PREFERRED_NETWORK_HOSPITAL)){
									finalCategoryList.add(selectValue);
								}
							}
							else{
								finalCategoryList.add(selectValue);
							}
						}
							
						else if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(productKey)){
							if(billClassificationKey.equals(12L)){
								if(selectValue.getId().equals(ReferenceTable.SHARED_ACCOMODATION)){
									finalCategoryList.add(selectValue);
								}
							}else{
								finalCategoryList.add(selectValue);
							}
						}
						else if(ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(productKey)){
							if(billClassificationKey.equals(12L)){
								if(!selectValue.getId().equals(ReferenceTable.COMPASSIONATE_TRAVEL)){
									finalCategoryList.add(selectValue);
								}
							}else{
								finalCategoryList.add(selectValue);
							}
						}
						else if(intimationObj != null && (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))
								|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
								|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))
								&& ("G").equalsIgnoreCase(intimationObj.getInsured().getPolicyPlan()))
								|| (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))){
							if(billClassificationKey.equals(12L)){
								if(selectValue.getId().equals(ReferenceTable.SHARED_ACCOMODATION)){
									finalCategoryList.add(selectValue);
								}
							}else{
								finalCategoryList.add(selectValue);
							}
							
						}// new added for GLX2020193
						else if(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(productKey)){
							if(billClassificationKey.equals(12L)){
								if(!selectValue.getId().equals(ReferenceTable.PREFERRED_NETWORK_HOSPITAL)){
									finalCategoryList.add(selectValue);
								}
							}else{
								finalCategoryList.add(selectValue);
							}
						}
						else
						 {
							finalCategoryList.add(selectValue);
						 }
					}
					//added for GMC prorata Calculation
					if(intimationObj != null && !(ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
							|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))){
						protaCategoryList.add(selectValue);
						if(billClassificationKey.equals(8L)){
							if(selectValue.getId().equals(ReferenceTable.OTHERS_WITH_PRORORTIONATE_DEDUCTION) 
									|| selectValue.getId().equals(ReferenceTable.OTHERS_WITHOUT_PRORORTIONATE_DEDUCTION)){
								finalCategoryList.remove(selectValue);

							}
							if(selectValue.getId().equals(ReferenceTable.OTHERS_WITH_PRORORTIONATE_DEDUCTION) 
									|| selectValue.getId().equals(ReferenceTable.OTHERS_WITHOUT_PRORORTIONATE_DEDUCTION)){
								protaCategoryList.remove(selectValue);

							}
						}
					}
					
					// new added for GLX2020193
					if(intimationObj != null && !(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(intimationObj.getPolicy().getProduct().getKey()))){
						if(billClassificationKey.equals(12L)){
							if(selectValue.getId().equals(ReferenceTable.VALUABLE_SERVICE_PROVIDER)){
								finalCategoryList.remove(selectValue);
								protaCategoryList.remove(selectValue);
							}
						}
					}
				}
				categoryValueContainer.addAll(finalCategoryList);
				
				if(intimationObj != null && !(ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
						|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))){
					selectValueContainer.removeAllItems();
					selectValueContainer.addAll(protaCategoryList);
				}
			}
			/**
			 * For star wedding gift insurance , policy pre and post
			 * natal shouldn't be addded in drop down list. Hence below condition is
			 * added , based on which drop down data with or without 
			 * pre and post natal would be added. 
			 * 
			 * This change could've been done in master service. The reason
			 * for not doing there is, domicillary based check was available and
			 * that was respective to claim type. Instead of tampering that code, 
			 * the condition was handled in presenter level.
			 * 
			 * */
			if((ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey) || (ReferenceTable.getComprehensiveProducts().containsKey(productKey))) && (!ReferenceTable.getMaternityMap().containsKey(subCoverValue)) ||
					ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(productKey)  || ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(productKey)) 
				view.setUpCategoryValues(categoryValueContainer);
			else	
				view.setUpCategoryValues(selectValueContainer);
			}
		
		public void setBillEntryStatus(
				@Observes @CDIEvent(BILL_ENTRY_SUBMIT_STATUS) final ParameterDTO parameters) {
				//Boolean status = (Boolean) parameters.getPrimaryParameter();
				UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
				createRodService.saveBillEntryValues(uploadDTO);
				view.setBillEntryFinalStatus(uploadDTO);
			
		}
		
		public void withDrawloadBillDetailsValues(
				@Observes @CDIEvent(WITHDRAW_LOAD_BILL_DETAILS_VALUES) final ParameterDTO parameters) {
			WithDrawPostProcessBillDetailsDTO withDrawDto = (WithDrawPostProcessBillDetailsDTO) parameters.getPrimaryParameter();
			List<UploadDocumentDTO> uploadDocs = withDrawDto.getUploadDocList();
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocs) {
				List<BillEntryDetailsDTO> dtoList = createRodService.getBillEntryDetailsList(uploadDocumentDTO);
				uploadDocumentDTO.setBillEntryDetailList(dtoList);
			}

			view.setBillClassificationBillEntries(uploadDocs);
		}

		public void previousPreauthApporvedDate(@Observes @CDIEvent(PREAUTH_APPROVED_DATE_POST_PROCESS) final ParameterDTO parameters) {
			WithdrawPreauthPostProcessPageDTO preauthdto = (WithdrawPreauthPostProcessPageDTO) parameters.getPrimaryParameter();
			Preauth previousPreauthList = preauthService.getPreviousPreauthList(preauthdto.getClaimDto().getKey());
		    if(previousPreauthList != null && previousPreauthList.getModifiedDate() != null){
		    	preauthdto.getPreauthDto().getClaimDTO().setPreauthApprovedDate(previousPreauthList.getModifiedDate());
		    }
		}
		
		public void setUpSubCategoryValues(
				@Observes @CDIEvent(WITHDRAW_PREAUTH_POST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
			Long categoryKey = (Long) parameters.getPrimaryParameter();
			GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
			SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
			BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
			view.setsubCategoryValues(selectValueContainer,subCategory,sel);
		}
		
		public void setUpsourceValues(
				@Observes @CDIEvent(WITHDRAW_PREAUTH_POST_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
			Long subCategoryKey = (Long) parameters.getPrimaryParameter();
			GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
			SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
			BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
			view.setsourceValues(selectValueContainer,source,sel);
		}
}

