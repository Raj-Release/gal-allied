package com.shaic.claim.reimbursement.financialapproval.wizard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.APIService;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.RawInvsHeaderDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.TextField;
import com.zybnet.autocomplete.server.AutocompleteField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ViewInterface(FinancialWizard.class)
public class FinancialWizardPresenter extends AbstractMVPPresenter<FinancialWizard>{
	private static final long serialVersionUID = 7488192193097057694L;
	public static final String SUBMIT_CLAIM_FINANCIAL = "submit_claim_financial";
	
	public static final String SAVE_FINANCIAL_RRC_REQUEST_VALUES = "save_financial_rrc_request_values";
	
	public static final String FINANCIAL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "financial_load_rrc_request_drop_down_values";
	public static final String FINANCIAL_LOAD_EMPLOYEE_DETAILS = "financial_load_employee_details";
	public static final String VALIDATE_FINANCIAL_USER_RRC_REQUEST = "validate_financial_user_rrc_request"; 
	
	public static final String SUBMIT_CLAIM_FINANCIAL_MAPPING = "submit_claim_financial_mapping";
	
	public static final String BILLING_APPROVED_AMOUNT_VALIDATION = "approved amount validation for financial";
	
	public static final String FINANCIAL_64VB_CHQUEQ_STATUS = "64vb cheque status alert";
	
	public static final String DEFINED_LIMIT_ALERT = "defined limit alert msg for super surplus";
	
	public static final String DEFINED_LIMIT_ALERT_FOR_FIRST = "defined limit alert msg for super surplus for first";
	public static final String CLAIMED_AMNT_ALERT = "Claimed Amount Alert - FA";

	public static final String VALIDATE_FINANCIAL_USER_LUMEN_REQUEST = "validate_financial_user_lumen_request";
	
	public static final String FINANCIAL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "financial_load_rrc_request_sub_category_values";
	
	public static final String FINANCIAL_LOAD_RRC_REQUEST_SOURCE_VALUES = "financial_load_rrc_request_source_values";
	
	public static final String BILLING_FA_SAVE_AUTO_ALLOCATION_CANCEL_REMARKS = "billing_FA_save_auto_allocation_cancel_remarks";
	
	public static final String BILLING_FA_HOLD_SUBMIT = "submit_hold_claim_financial";
	
	private final Logger log = LoggerFactory.getLogger(FinancialWizardPresenter.class);
	 
		
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private APIService apiService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private CreateRODService createRODService;
	
	@EJB
	private PreauthService preauthService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_CLAIM_FINANCIAL) final ParameterDTO parameters) throws Exception{
		PreauthDTO reimbursementDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		Boolean isAlreadyAcquired = Boolean.FALSE;
		StringBuffer aquiredUser = new StringBuffer();
		Map<String, Object> wrkFlowMap = (Map<String, Object>) reimbursementDTO.getDbOutArray();
		if (wrkFlowMap != null){
			DBCalculationService db = new DBCalculationService();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String aciquireByUserId = db.getLockUser(wrkFlowKey);
			if((reimbursementDTO.getStrUserName() != null && aciquireByUserId != null && ! reimbursementDTO.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				isAlreadyAcquired = Boolean.TRUE;
				aquiredUser.append(aciquireByUserId);
			}
		
		}
		
		// If hospitalization ROD is not submitted then we should not allow other ROD to submit.
		
			if(! isAlreadyAcquired){
				
				Boolean isValidUser = true;
				if(reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
					if(reimbursementDTO.getUserLimitAmount() != null){
						String limitAmountValidation = "N";
						Double claimedAmount = SHAUtils.getDoubleFromStringWithComma(reimbursementDTO.getUserLimitAmount());
						if((reimbursementDTO.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(reimbursementDTO.getScreenName())) 
								|| (reimbursementDTO.getScreenName() != null && SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(reimbursementDTO.getScreenName()))) {
								limitAmountValidation = dbCalculationService.getLimitAmountValidationForCommonBillingFA(reimbursementDTO.getStrUserName(),claimedAmount.longValue(),reimbursementDTO.getKey());
							}else{
							limitAmountValidation = dbCalculationService.getLimitAmountValidationForFA(reimbursementDTO.getStrUserName(),claimedAmount.longValue(), SHAConstants.REIMBURSEMENT_CHAR);
						}
						if(limitAmountValidation != null && limitAmountValidation.equalsIgnoreCase("N")){
							isValidUser = false;
						}
					}
					
				}
				if(isValidUser){
				
				if(ReferenceTable.getFHORevisedKeys().containsKey(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					policyService.updatePolicyBonusDetails(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
				}
			
				Reimbursement currentReimbursement = createRODService.getReimbursementObjectByKey(reimbursementDTO.getKey());
				if(!currentReimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
				Boolean isFirstRODApproved = createRODService.getStatusOfFirstROD(reimbursementDTO.getClaimDTO().getKey(), currentReimbursement);
				
				if(reimbursementDTO.getIsReconsiderationRequest() != null && !reimbursementDTO.getIsReconsiderationRequest() && currentReimbursement != null) {
					Boolean paymentAvailable = reimbursementService.isPaymentAvailable(currentReimbursement.getRodNumber());
					reimbursementDTO.setIsPaymentAvailable(paymentAvailable);
					reimbursementDTO.setIsPaymentAvailableShown(paymentAvailable);
					DocAcknowledgement acknowledgementByKey = reimbursementService.getAcknowledgementByKey(currentReimbursement.getDocAcknowLedgement().getKey());
					if(acknowledgementByKey != null && acknowledgementByKey.getPaymentCancellationFlag() != null && acknowledgementByKey.getPaymentCancellationFlag().equalsIgnoreCase("Y")) {
						reimbursementDTO.setIsPaymentAvailable(false);
						reimbursementDTO.setIsPaymentAvailableShown(false);
					}
				}
				
				Boolean isAlreadExistHospRod = false;
				if(currentReimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && currentReimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")){
				    Reimbursement hospitalizationRodForFAApproved = reimbursementService.getHospitalizationRodForFAApproved(reimbursementDTO.getClaimDTO().getKey(), currentReimbursement.getKey(),currentReimbursement.getRodNumber());
				    if(hospitalizationRodForFAApproved != null){
				    	isAlreadExistHospRod = true;
				    }
				}
				// add for hostipal cash flow change for remb
				if(reimbursementDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						reimbursementDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
						reimbursementDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						reimbursementDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
					isFirstRODApproved = true;
				}
				if(isFirstRODApproved && !reimbursementDTO.getIsPaymentAvailableShown() && ! isAlreadExistHospRod
						////Mail - Unable to refer Pre, Post claim so added below condition will allow to refer not for approved
						|| (reimbursementDTO.getStatusKey() != null && !reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS))) {
					
					 Boolean isCallRollBack = false;
					    List<DiagnosisProcedureTableDTO> diagnosisProcedureDtoList = reimbursementDTO.getDiagnosisProcedureDtoList();
					    if(diagnosisProcedureDtoList != null && ! diagnosisProcedureDtoList.isEmpty()){
					    	for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : diagnosisProcedureDtoList) {
					    		if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null && diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getKey() != null){
									PedValidation diagnosisByKey = reimbursementService.getDiagnosisByKey(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getKey());
									String sublimitOption = "N";
									if(diagnosisProcedureTableDTO.getSublimitYesOrNo() != null && diagnosisProcedureTableDTO.getSublimitYesOrNo().getId().equals(ReferenceTable.COMMONMASTER_YES)){
                                         sublimitOption = "Y";
									}
									
									if(diagnosisByKey.getSubLimitApplicable() != null && ! diagnosisByKey.getSubLimitApplicable().equalsIgnoreCase(sublimitOption)){
										diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().setIsSublimitValueChange(true);
									}
									
									
									if(diagnosisByKey.getSublimitId() != null && diagnosisProcedureTableDTO.getSublimitYesOrNo() != null && diagnosisProcedureTableDTO.getSublimitYesOrNo().getId().equals(ReferenceTable.COMMONMASTER_NO)){
										isCallRollBack = true;
									}
									
									
									
								}else if(diagnosisProcedureTableDTO.getProcedureDTO() != null && diagnosisProcedureTableDTO.getProcedureDTO().getKey() != null){
									Procedure procedure = reimbursementService.getProcedureKey(diagnosisProcedureTableDTO.getProcedureDTO().getKey());
									if(procedure.getSublimitNameId() != null && diagnosisProcedureTableDTO.getSublimitYesOrNo() != null &&  diagnosisProcedureTableDTO.getSublimitYesOrNo().getId().equals(ReferenceTable.COMMONMASTER_NO)){
										isCallRollBack = true;
									}
									
									String sublimitOption = "N";
									if(diagnosisProcedureTableDTO.getSublimitYesOrNo() != null && diagnosisProcedureTableDTO.getSublimitYesOrNo().getId().equals(ReferenceTable.COMMONMASTER_YES)){
                                         sublimitOption = "Y";
									}
									//IMSSUPPOR-29528 - Null handled for GMC MED-SEC-01J with procedure choosen
									if(procedure.getSubLimitApplicable() != null && ! procedure.getSubLimitApplicable().equalsIgnoreCase(sublimitOption)){
										diagnosisProcedureTableDTO.getProcedureDTO().setIsSublimitValueChange(true);
									}
									
									
								}
							}
					    }
					
					
					// Rollback procedure to clear the accumulator value.................
					if(((reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) || (reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY))) && !reimbursementDTO.getIsDirectToFinancial() ) {
		//				dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getPreviousPreauthKey().equals(0l) ? reimbursementDTO.getKey() : reimbursementDTO.getPreviousPreauthKey() , reimbursementDTO.getPreviousPreauthKey().equals(0l) ? "R" : "C");
						dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getKey(),"R");
						isCallRollBack = false;
					}
					
					if(reimbursementDTO.getIsDirectToFinancial() && ! (reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY))
							&& ! reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)){
						if(reimbursementDTO.getClaimDTO().getLatestPreauthKey() != null){
							dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getClaimDTO().getLatestPreauthKey(), "C");
							isCallRollBack = false;
						}
					}
					
//					if(reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest() != null &&
//							("Y").equalsIgnoreCase(reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()) &&
//							      reimbursementService.isClaimPaymentAvailable(reimbursementDTO.getRodNumber())){
//						if((ReferenceTable.FINANCIAL_REJECT_STATUS).equals(reimbursementDTO.getStatusKey())){
//							reimbursementDTO.setStatusKey(ReferenceTable.PAYMENT_SETTLED);
//						}
//					}
					Boolean shouldInvokeAcc = true;
				    
				    if(isCallRollBack){
				    	if(reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
				    		dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getKey(),"R");
				    	}else{
				    		if(reimbursementDTO.getClaimDTO().getLatestPreauthKey() != null){
								dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getClaimDTO().getLatestPreauthKey(), "C");
							}
				    	}
				    }
					
					Reimbursement reimbursement = reimbursementService.submitClaimFinancial(reimbursementDTO);
					
					RawInvsHeaderDetails headerObj =  claimService.getRawHeaderByIntimation(reimbursementDTO.getNewIntimationDTO().getIntimationId());
					if(null != headerObj){
						List<RawInvsDetails> existingList = claimService.getRawDetailsByRecordType(headerObj.getKey());
						for (RawInvsDetails rawInvsDetails : existingList) {
							preauthService.updateEsclateIfCompleted(rawInvsDetails);
						}
					}
					
					Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(reimbursementDTO.getClaimKey(), reimbursementDTO.getKey());
					
					if((reimbursementDTO.getIsHospitalizationRepeat() != null && !reimbursementDTO.getIsHospitalizationRepeat()) && (reimbursementDTO.getHospitalizaionFlag() != null && !reimbursementDTO.getHospitalizaionFlag()) && (reimbursementDTO.getPartialHospitalizaionFlag() != null && !reimbursementDTO.getPartialHospitalizaionFlag())) {
						shouldInvokeAcc = false;
					}
					
					if(reimbursement != null) {
						if(((reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) || (reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY))) && !reimbursementDTO.getIsDirectToFinancial()) {
		
							if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
								if(hospitalizationRod == null){
									
									if(reimbursementDTO.getClaimDTO().getLatestPreauthKey() != null && shouldInvokeAcc){
										dbCalculationService.invokeAccumulatorProcedure(reimbursementDTO.getClaimDTO().getLatestPreauthKey());
									} else {
									
										Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursementDTO.getClaimDTO().getKey());
										if(shouldInvokeAcc) {
											dbCalculationService.invokeAccumulatorProcedure(latestPreauth.getKey());
										}
										
									}
								}else{
									Reimbursement partialHospitalizationROD = reimbursementService.getPartialHospitalizationROD(reimbursementDTO.getClaimKey());
									if(shouldInvokeAcc) {
										if(partialHospitalizationROD != null) {
											dbCalculationService.invokeReimbursementAccumulatorProcedure(partialHospitalizationROD.getKey());
										} else {
											Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursementDTO.getClaimDTO().getKey());
											if(latestPreauth != null) {
												dbCalculationService.invokeAccumulatorProcedure(latestPreauth.getKey());
											}
										}
										
									}
								
								}
							}else{
								if(hospitalizationRod != null && shouldInvokeAcc){
									dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
								}
							}
							
						} else {
							if (shouldInvokeAcc && !reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD) && ! reimbursementDTO.getIsFirstStepRejection() && !reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY)
									&& !(reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) && ! reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_QUERY_STATUS)){
									dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursementDTO.getKey());
							}
							
						}
						if(!(ReferenceTable.getReferToKeys().containsKey(reimbursement.getStatus().getKey())) && !(ReferenceTable.FINANCIAL_QUERY_STATUS.equals(reimbursement.getStatus().getKey()))) {
							dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
						}
						
					}
					if(!(ReferenceTable.getReferToKeys().containsKey(reimbursement.getStatus().getKey())) && !(ReferenceTable.FINANCIAL_QUERY_STATUS.equals(reimbursement.getStatus().getKey()))) {
						dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
					}
				String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
				if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
	
						Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(reimbursement.getClaim().getIntimation().getHospital());
						Claim claim = claimService.getClaimByClaimKey(reimbursement.getClaim().getKey());
						String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
						Date startDate = new Date();
						log.info("^^^^^^^^^^^^^^^^^^^^^^^SUBMIT CLAIM FINANCIAL BEFORE PROVISION^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" +" "+reimbursementDTO.getNewIntimationDTO().getIntimationId()+ " " +startDate);
						apiService.updateProvisionAmountToPremia(provisionAmtInput);
						log.info("^^^^^^^^^^^^^^^^^^^^^^ SUBMIT CLAIM FINANCIAL AFTER PROVISION ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^------> "+" "+reimbursementDTO.getNewIntimationDTO().getIntimationId()+" "+ SHAUtils.getDurationFromTwoDate(startDate, new Date()));
						
						if((ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) && (reimbursement.getStatus() != null && (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)))) {
								if(reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
	
		//							TmpCPUCode tmpCPUCode = hospitalService.getTmpCPUCode(hospitalDetailsByKey.getCpuId());
		//							JSONObject jsonObj = new JSONObject();
		//							jsonObj.put(PremiaConstants.POLICY_NUMBER, reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
		//							jsonObj.put(PremiaConstants.INTIMATION_NUMBER, reimbursementDTO.getNewIntimationDTO().getIntimationId());
		//							jsonObj.put(PremiaConstants.CPU_CODE, tmpCPUCode != null ? String.valueOf(tmpCPUCode.getCpuCode()) : "");
		//							jsonObj.put(PremiaConstants.ADJUSTMENT_AMOUNT, String.valueOf(reimbursementDTO.getTotalConsolidatedAmt() - reimbursementDTO.getUniqueDeductedAmount()));
		//							if(reimbursementDTO.getShouldDetectPremium()) {
		//								PremiaService.getInstance().updateAdjustmentAmount(jsonObj.toString());
		//							}
								}
								
								if(((reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyTerm() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyTerm().equals(2l)) || reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)) && (reimbursement.getStatus() != null && (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)))) {
									if(reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
		//								TmpCPUCode tmpCPUCode = hospitalService.getTmpCPUCode(hospitalDetailsByKey.getCpuId());
		//								JSONObject jsonObj = new JSONObject();
		//								jsonObj.put(PremiaConstants.POLICY_NUMBER, reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
		//								jsonObj.put(PremiaConstants.INTIMATION_NUMBER, reimbursementDTO.getNewIntimationDTO().getIntimationId());
		//								jsonObj.put(PremiaConstants.CPU_CODE, tmpCPUCode != null ? String.valueOf(tmpCPUCode.getCpuCode()) : "");
		//								jsonObj.put(PremiaConstants.ADJUSTMENT_AMOUNT, String.valueOf(reimbursementDTO.getTotalConsolidatedAmt() - reimbursementDTO.getUniqueDeductedAmount()));
		//								PremiaService.getInstance().updateAdjustmentAmount(jsonObj.toString());
		//							}
									}	
								} else if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
		//							TmpCPUCode tmpCPUCode = hospitalService.getTmpCPUCode(hospitalDetailsByKey.getCpuId());
		//							JSONObject jsonObj = new JSONObject();
		//							jsonObj.put(PremiaConstants.POLICY_NUMBER, reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
		//							jsonObj.put(PremiaConstants.INTIMATION_NUMBER, reimbursementDTO.getNewIntimationDTO().getIntimationId());
		//							jsonObj.put(PremiaConstants.CPU_CODE, tmpCPUCode != null ? String.valueOf(tmpCPUCode.getCpuCode()) : "");
		//							Double amt = reimbursementDTO.getTotalConsolidatedAmt() - reimbursementDTO.getUniquePremiumAmount();
		//							jsonObj.put(PremiaConstants.ADJUSTMENT_AMOUNT, String.valueOf(reimbursementDTO.getTotalConsolidatedAmt() - amt));
		////							jsonObj.put(PremiaConstants.ADJUSTMENT_AMOUNT, String.valueOf(reimbursementDTO.getTotalConsolidatedAmt() - reimbursementDTO.getUniqueDeductedAmount()));
		//							if(reimbursementDTO.getShouldDetectPremium()) {
		//								PremiaService.getInstance().updateAdjustmentAmount(jsonObj.toString());
		//							}
								}
						}
								if(reimbursement.getStatus() != null && (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) ))  {
									if(reimbursementService.isAnyRodActive(reimbursement) ){
										PremiaService.getInstance().UnLockPolicy(reimbursement.getClaim().getIntimation().getIntimationId());
									}
								}
		//						String bedCount = reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails().getInpatientBeds();
		//						if(bedCount != null) {
		//							PremiaService.getInstance().updateBedsCount(bedCount, hospitalDetailsByKey.getHospitalCode());
		//						}
							 
		
						
						
					}
					view.buildSuccessLayout();
				} else {
					if(! isFirstRODApproved){
						view.buildFailureLayout();
					} else if(isAlreadExistHospRod){
						view.buildAlreadyExist();
					}
					else{
						view.buildPaymentFailureLayout();
					}
				}
				}else{
					view.buildPaymentFailureLayout();
				}
			}else{
				view.validationForLimit();
				}
		}else {
			view.alertForAlreadyAcquired(aquiredUser.toString());
		}
	
	}
	
	public void saveRRCRequestValues(@Observes @CDIEvent(SAVE_FINANCIAL_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(FINANCIAL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	
	public void loadEmployeeDetails(@Observes @CDIEvent(FINANCIAL_LOAD_EMPLOYEE_DETAILS) final ParameterDTO parameters){
		AutocompleteField<EmployeeMasterDTO> field = (AutocompleteField<EmployeeMasterDTO>) parameters.getPrimaryParameter();
		String query = parameters.getSecondaryParameter(0, String.class);
		List<EmployeeMasterDTO> employeeList = reimbursementService.getListOfEmployeeDetails(query);
		view.loadEmployeeMasterData(field, employeeList);
		
	}

	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_FINANCIAL_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		
		if(isValid){
			reimbursementService.loadRRCRequestValues(preauthDTO,0d,SHAConstants.FINANCIAL);
		}
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	
	public void preauthLumenRequest(@Observes @CDIEvent(VALIDATE_FINANCIAL_USER_LUMEN_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(preauthDTO.getNewIntimationDTO().getIntimationId());
	}
	
	@SuppressWarnings("static-access")
	public void submitRoomRentMapping(
			@Observes @CDIEvent(SUBMIT_CLAIM_FINANCIAL_MAPPING) final ParameterDTO parameters) {
		PreauthDTO reimbursementDTO = (PreauthDTO) parameters.getPrimaryParameter();
		reimbursementService.saveRoomRentNursingMapping(reimbursementDTO, reimbursementDTO.getRoomRentMappingDTOList());
		//added by noufel fro GMC prop CR
		if(!(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
				|| reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(reimbursementDTO.getKey());
		} 
		else if((reimbursementDTO.getProrataDeductionFlag() != null && reimbursementDTO.getProrataDeductionFlag().equalsIgnoreCase("N")) &&
				(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
						|| reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(reimbursementDTO.getKey());
		}
		else if(reimbursementDTO.getIsCashlessPropDedSelected() && (reimbursementDTO.getProrataDeductionFlag() != null && reimbursementDTO.getProrataDeductionFlag().equalsIgnoreCase("Y")) &&
				(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
						|| reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(reimbursementDTO.getKey());
		}
	}
	
	public void showAlertMessageForAmount(@Observes @CDIEvent(BILLING_APPROVED_AMOUNT_VALIDATION) final ParameterDTO parameters)
	{
		String message = (String) parameters.getPrimaryParameter();
		view.confirmMessageForApprovedAmount(message);
		
	}
	
	public void showAlertMessageFor64VB(@Observes @CDIEvent(FINANCIAL_64VB_CHQUEQ_STATUS) final ParameterDTO parameters)
	{
		String message = (String) parameters.getPrimaryParameter();
		view.confirmMessageFor64VBstatus(message);
		
	}
	
	public void showAlertMessageForDefinedLimit(@Observes @CDIEvent(DEFINED_LIMIT_ALERT) final ParameterDTO parameters)
	{
		String message = (String) parameters.getPrimaryParameter();
		Object rejectionCategoryDropdownValues = masterService.getSelectValueContainer(ReferenceTable.REIMB_REJECTION_CATEGORY);
		view.confirmMessageForDefinedLimt(rejectionCategoryDropdownValues);
		
	}
	
	public void showAlertMessageForDefinedLimitForFirst(@Observes @CDIEvent(DEFINED_LIMIT_ALERT_FOR_FIRST) final ParameterDTO parameters)
	{
		String message = (String) parameters.getPrimaryParameter();
		Object rejectionCategoryDropdownValues = masterService.getSelectValueContainer(ReferenceTable.REIMB_REJECTION_CATEGORY);
		view.confirmMessageForDefinedLimtForFirst(rejectionCategoryDropdownValues);
		
	}
	
	
	public void validateClaimedAmount(
			@Observes @CDIEvent(CLAIMED_AMNT_ALERT) final ParameterDTO parameters) 
	{
		TextField txtClaimedAmnt = (TextField) parameters.getPrimaryParameter();
		view.validateClaimedAmount(txtClaimedAmnt);
		
	}
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(FINANCIAL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(FINANCIAL_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
	public void saveCancelRemarks(@Observes @CDIEvent(BILLING_FA_SAVE_AUTO_ALLOCATION_CANCEL_REMARKS) final ParameterDTO parameters) {
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		Reimbursement reimbursment =null;
		if(null != preauthdto && null != preauthdto.getKey()){
			reimbursment = reimbursementService.getReimbursementByKey(preauthdto.getKey());
		}
		reimbursementService.updateCancelRemarks(reimbursment,preauthdto.getAutoAllocCancelRemarks(),preauthdto);

	}
	
	public void holdSubmitWizard(
			@Observes @CDIEvent(BILLING_FA_HOLD_SUBMIT) final ParameterDTO parameters) throws Exception {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();

		Boolean isAlreadyAcquired = Boolean.FALSE;
		StringBuffer aquiredUser = new StringBuffer();
		Map<String, Object> wrkFlowMap = (Map<String, Object>) preauthDTO.getDbOutArray();
		if (wrkFlowMap != null){
			DBCalculationService db = new DBCalculationService();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String aciquireByUserId = db.getLockUser(wrkFlowKey);
			if((preauthDTO.getStrUserName() != null && aciquireByUserId != null && ! preauthDTO.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				isAlreadyAcquired = Boolean.TRUE;
				aquiredUser.append(aciquireByUserId);
			}

		}

		if(! isAlreadyAcquired){

			DBCalculationService dbCalService = new DBCalculationService();
			if(preauthDTO.getDbOutArray() != null){

				Map<String, Object> holdWrkFlowMap = (Map<String, Object>) preauthDTO.getDbOutArray();
				holdWrkFlowMap.put(SHAConstants.USER_ID,preauthDTO.getStrUserName());
				holdWrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.FA_STAGE_SOURCE);
				if(preauthDTO.getStrUserName() != null){
					TmpEmployee employeeByName = preauthService.getEmployeeByName(preauthDTO.getStrUserName());
					if(employeeByName != null){
						holdWrkFlowMap.put(SHAConstants.REFERENCE_USER_ID,employeeByName.getEmpId());
					}
				}
				holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_REFERRED_BY,preauthDTO.getStrUserName());
				holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE,SHAConstants.HOLD_FLAG);
//				holdWrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PREAUTH_HOLD_OUTCOME);
				holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE,preauthDTO.getPreauthMedicalDecisionDetails().getHoldRemarks());
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(holdWrkFlowMap);

				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			}
			Reimbursement currentReimbursement = createRODService.getReimbursementObjectByKey(preauthDTO.getKey());
			if(currentReimbursement != null){
				preauthService.updateStageInformationForBilling(currentReimbursement,preauthDTO);
			}

			if(preauthDTO.getDbOutArray() != null){
				Long wkFlowKey = (Long)wrkFlowMap.get(SHAConstants.WK_KEY);	
				preauthService.updateHoldRemarksForAutoAllocation(wkFlowKey, preauthDTO.getPreauthMedicalDecisionDetails().getHoldRemarks());
			}
			view.buildSuccessLayout();
		}else{
			view.alertForAlreadyAcquired(aquiredUser.toString());
		}
	}
}
