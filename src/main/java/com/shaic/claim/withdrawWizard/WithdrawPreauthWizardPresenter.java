package com.shaic.claim.withdrawWizard;

import java.util.ArrayList;
import java.util.List;

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
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(WithdrawPreauthWizard.class)
public class WithdrawPreauthWizardPresenter extends AbstractMVPPresenter<WithdrawPreauthWizard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String WITHDRAW_SUBMITTED_EVENT = "Withdraw preauth sumbit Event";
	
	public static final String VALIDATE_WITHDRAW_PREAUTH_USER_RRC_REQUEST = "withdraw_preauth_user_rrc_request";
	
	public static final String WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "withdraw_preauth_load_rrc_request_drop_down_values";
	
	public static final String WITHDRAW_PREAUTH_SAVE_RRC_REQUEST_VALUES = "withdraw_preauth_save_rrc_request_values";
	
	public static final String CLICK_WITHDRAW_ACTION = "click_withdraw_action";
	
	public static final String CLICK_WITHDRAW_AND_REJECT_ACTION = "click_withdraw_and_reject_action";
	
	public static final String STANDALONE_WITHDRAW_GENERATE_REMARKS = "Standalone Withdraw generate Remarks";
	
	public static final String PREAUTH_APPROVED_DATE_WITHDRAW_PAGE = "set preauth approved date in Withdraw page letter";

	public static final String WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "withdraw_preauth_load_rrc_request_sub_category_values";

	public static final String WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES = "withdraw_preauth_load_rrc_request_source_values";

	@EJB
	private PreMedicalService premedicalService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ClaimService claimService;
	
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
	
	
	public void submitWizard(
			@Observes @CDIEvent(WITHDRAW_SUBMITTED_EVENT) final ParameterDTO parameters)  throws Exception{

		
		WithdrawPreauthPageDTO withdrawDTO = (WithdrawPreauthPageDTO) parameters.getPrimaryParameter();
		
//		PreauthDTO preauthDto=preauthMapper.getPreauthDTO(withdrawDTO.getPreauth());
		
		PreauthDTO preauthDto = withdrawDTO.getPreauthDto();
		
//		preauthDto.setReferenceType(withdrawDTO.getPreauth().getPreauthId());
		
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
//	    for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisDetails) {
//			List<DiagnosisPED> diagnosisPed=preauthService.getPEDDiagnosisByPEDValidationKey(diagnosisDetailsTableDTO.getKey());
//			List<PedDetailsTableDTO> pedList=preauthMapper.getDiagnosisPEDListDto(diagnosisPed);
//			for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
//				pedDetailsTableDTO.setKey(null);
//			}
//			diagnosisDetailsTableDTO.setPedList(pedList);
//			diagnosisDetailsTableDTO.setKey(null);
//		}
	    
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
		preauthDto.setMedicalRemarks(withdrawDTO.getMedicalRemarks());
		preauthDto.setStatusKey(withdrawDTO.getStatusKey());
		preauthDto.setStageKey(ReferenceTable.WITHDRAW_STAGE);
		preauthDto.getPreauthDataExtractionDetails().setReferenceNo(newReferenceNo);
		preauthDto.setKey(null);
		preauthDto.setProcessType("W");
		preauthDto.getResidualAmountDTO().setRemarks(residualAmount.getRemarks());
		preauthDto.getPreauthDataExtractionDetails().setApprovedAmount(withdrawDTO.getTotalApprovedAmt().toString());
		preauthDto.getPreauthDataExtractionDetails().setSpecialityList(specialityDtoList);
		preauthDto.getPreauthDataExtractionDetails().setClaimedDetailsList(claimAmountDtoList);
		preauthDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(withdrawDTO.getMedicalRemarks());
		preauthDto.getPreauthMedicalDecisionDetails().setDoctorNote(withdrawDTO.getDoctorNote());
		preauthDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(withdrawDTO.getMedicalRemarks());
		preauthDto.getPreauthMedicalDecisionDetails().setWithdrawRemarks(withdrawDTO.getWithdrawRemarks());
		
		
		if(preauthDto.getPreviousPreauthKey() != null){
			dbCalculationService.reimbursementRollBackProc(preauthDto.getPreviousPreauthKey(),"C");
		}
		
		if(preauthDto.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)){
			
			if(preauthDto.getWithdrawReason() != null
					&& SHAUtils.getPolCondionalExclutionRejKeys().containsKey(preauthDto.getWithdrawReason().getId())) {
				preauthDto.getPreauthMedicalDecisionDetails().setPolicyConditionNoReject(withdrawDTO.getPolicyConditionNoReject());
			}
		}	
		//premedicalService.saveDuplicatePreauthValues(preauthDto,"W");
		
		
		Preauth preauth=preauthService.submitPreAuth(preauthDto, true);
		
		
		//CR R1313
		if(preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) 
				&& withdrawDTO.getReasonForWithdraw() != null 
				&& withdrawDTO.getReasonForWithdraw().getId().equals(ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW_RETAIL)){
			Hospitals hospitalObject = preauthService.getHospitalObject(preauth.getClaim().getIntimation().getHospital());
			String provisionAmtInput = SHAUtils.getProvisionAmtInput(preauth.getClaim(), hospitalObject.getName(), "0");
			PremiaService.getInstance().updateProvisionAmount(provisionAmtInput);
			claimService.updateClaimProvisionAmount(preauth.getClaim());			
		}
		
		String outCome = "SUBMIT";
		
		if(preauthDto.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
				 || preauthDto.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
				|| preauthDto.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)){
			
			preauthService.cancellNegotiation(preauthDto);
		}
		if(withdrawDTO.getReasonForWithdraw() != null && withdrawDTO.getReasonForWithdraw().getId().equals(ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW)){
//			outCome = "NOTADMITTED";
			
			outCome = SHAConstants.OUTCOME_FOR_WITHDRAW_PREAUTH_PATIENT_NOT_ADMITTED;
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				PremiaService.getInstance().UnLockPolicy(withdrawDTO.getNewIntimationDto().getIntimationId());
			}
		} else {
			outCome = SHAConstants.OUTCOME_FOR_WITHDRAW_PREAUTH_OTHERS;
		}
		
		
		
		Preauth preauthById = preauthService.getPreauthById(preauth.getKey());
		
//		dbCalculationService.invokeAccumulatorProcedure(preauthById.getKey());
		
		if(preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)) {
//			createRodService.stopCashlessReminderLetter(preauth.getClaim().getKey(),preauthDto.getStrUserName(), preauthDto.getStrPassword());
//			createRodService.stopCashlessReminderProcess(preauth.getClaim().getKey(), preauthDto.getStrUserName(), preauthDto.getStrPassword());
		}
		
		if(preauth!=null){
			

			if(preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)){
				outCome = SHAConstants.OUTCOME_FOR_STANDALONE_WITHDRAW_REJECT;
			}
			
//			preauthService.setBPMOutComeForWithdraw(preauthDto, preauthById,outCome);
			
			preauthService.submitDBProcedureForStadWithraw(preauthById.getClaim(), preauthById ,outCome);
			dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.OTHERS);
			
			//     R1135    -     13-04-2018
			dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.PAN_CARD);
			//preauthService.setBPMOutComeForWithdraw(preauthDto, preauthById,outCome, false);

			
//			downsizePreauthService.setBPMOutCome(preauth, preauthDto);
		}
		
		view.buildSuccessLayout();
//		preauthService.submitPreAuth(preauthDTO, false);
		
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
	
	
	public void generateWithdrawField(@Observes @CDIEvent(CLICK_WITHDRAW_ACTION) final ParameterDTO parameters){
		
		// CR R1313  ----- START
		Long productKey = (Long) parameters.getPrimaryParameter();
				
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON);
		
		if(!ReferenceTable.getGMCProductList().containsKey(productKey)){
			selectValueContainer = masterService
					.getSortedMasterBsedOnMasterTypeCode(ReferenceTable.WITHDRAWAL_REASON_RETAIL);
		}  // CR R1313  ----- END
		
		view.buildWithdrawFields(selectValueContainer);
//		view.buildWithdrawFields(masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON));  // CR R1313
		
	}
	
	
	public void generateWithdrawAndRejectFields(@Observes @CDIEvent(CLICK_WITHDRAW_AND_REJECT_ACTION) final ParameterDTO parameters){
		
		//IMSSUPPOR-27154
		//PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		//Long productKey = preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey();
		WithdrawPreauthPageDTO preauthDTO = (WithdrawPreauthPageDTO) parameters.getPrimaryParameter();
		Long productKey = preauthDTO.getPreauthDto().getNewIntimationDTO().getPolicy().getProduct().getKey();
		
		if(ReferenceTable.getGMCProductCodeList().containsKey(productKey)
				&& !(productKey.equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY) || productKey.equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM)
						|| productKey.equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI) || productKey.equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM))){
		
		view.buildWithdrawAndRejctFields(masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON), masterService.getRevisedRejectionCategoryByValue(productKey)); 
//				masterService.getRejectionCategoryByValue();
		}else if(productKey.equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY) || productKey.equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM)
				|| productKey.equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI) || productKey.equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM)){
			if(preauthDTO.getPreauthDto().getPolicyDto().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)){
				
				view.buildWithdrawAndRejctFields(masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON), masterService.getRevisedRejectionCategoryByValueForCoron(preauthDTO.getPreauthDto().getPolicyDto().getProduct().getKey() ,preauthDTO.getPreauthDto().getPolicyDto().getPolicyPlan())); 

			}else if(preauthDTO.getPreauthDto().getPolicyDto().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)){
				
				view.buildWithdrawAndRejctFields(masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON), masterService.getRevisedRejectionCategoryByValueForCoron(preauthDTO.getPreauthDto().getPolicyDto().getProduct().getKey(),preauthDTO.getPreauthDto().getPolicyDto().getPolicyPlan())); 

			}
		}
		
		else	
		{				// Commented Below code due to CR2019105
//			view.buildWithdrawAndRejctFields(masterService.getSortedMasterBsedOnMasterTypeCode(ReferenceTable.REJECT_WITHDRAWAL_REASON_RETAIL));
			view.buildWithdrawAndRejctFields(masterService.getRevisedRejectWithdrawCategoryByValue(productKey));
			
		}
		
	}
	
	public void getRemarks(
			@Observes @CDIEvent(STANDALONE_WITHDRAW_GENERATE_REMARKS) final ParameterDTO parameters) {
		Long id = (Long) parameters.getPrimaryParameter();
		String decision = (String) parameters.getSecondaryParameters()[0];
	
		MasterRemarks remarks = masterService.getRemarks(id);
		view.setRemarks(remarks,decision);
	}
	
	public void previousPreauthApporvedDate(@Observes @CDIEvent(PREAUTH_APPROVED_DATE_WITHDRAW_PAGE) final ParameterDTO parameters) {
		WithdrawPreauthPageDTO preauthdto = (WithdrawPreauthPageDTO) parameters.getPrimaryParameter();
		Preauth previousPreauthList = preauthService.getPreviousPreauthList(preauthdto.getClaimDto().getKey());
		 if(previousPreauthList != null && previousPreauthList.getModifiedDate() != null){
	    	preauthdto.getPreauthDto().getClaimDTO().setPreauthApprovedDate(previousPreauthList.getModifiedDate());
	    }
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
