package com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.json.JSONObject;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.PremiaConstants;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.APIService;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.reimbursement.service.PAReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.zybnet.autocomplete.server.AutocompleteField;

@ViewInterface(PANonHospFinancialWizard.class)
public class PANonHospFinancialWizardPresenter extends AbstractMVPPresenter<PANonHospFinancialWizard>{
	private static final long serialVersionUID = 7488192193097057694L;
	public static final String SUBMIT_CLAIM_FINANCIAL = "pa_submit_claim_financial";
	
	public static final String SAVE_FINANCIAL_RRC_REQUEST_VALUES = "pa_save_financial_rrc_request_values";
	
	public static final String FINANCIAL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "pa_financial_load_rrc_request_drop_down_values";
	public static final String FINANCIAL_LOAD_EMPLOYEE_DETAILS = "pa_financial_load_employee_details";
	public static final String VALIDATE_FINANCIAL_USER_RRC_REQUEST = "pa_validate_financial_user_rrc_request"; 
	
	public static final String SUBMIT_CLAIM_FINANCIAL_MAPPING = "pa_submit_claim_financial_mapping";

	public static final String FINANCIAL_RRC_REQUEST_SUB_CATEGORY_VALUES = "financial_rrc_request_sub_category_values";

	public static final String FINANCIAL_RRC_REQUEST_SOURCE_VALUES = "financial_rrc_request_source_values";

	
	@EJB
	private PAReimbursementService reimbursementService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
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
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_CLAIM_FINANCIAL) final ParameterDTO parameters) {
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
		
		if(! isAlreadyAcquired){
		
		// If hospitalization ROD is not submitted then we should not allow other ROD to submit.
		Reimbursement currentReimbursement = createRODService.getReimbursementObjectByKey(reimbursementDTO.getKey());
		if(!currentReimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
		Boolean isFirstRODApproved = createRODService.getStatusOfFirstROD(reimbursementDTO.getClaimDTO().getKey(), currentReimbursement);
		isFirstRODApproved = Boolean.TRUE;
		if(isFirstRODApproved) {
			// Rollback procedure to clear the accumulator value.................
			if(((reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD)) || (reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY))) && !reimbursementDTO.getIsDirectToFinancial() ) {
//				dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getPreviousPreauthKey().equals(0l) ? reimbursementDTO.getKey() : reimbursementDTO.getPreviousPreauthKey() , reimbursementDTO.getPreviousPreauthKey().equals(0l) ? "R" : "C");
				dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getKey(),"R");
			}
			
			if(reimbursementDTO.getIsDirectToFinancial() && ! (reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY))){
				if(reimbursementDTO.getClaimDTO().getLatestPreauthKey() != null){
					dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getClaimDTO().getLatestPreauthKey(), "C");
				}
			}
			
			Reimbursement reimbursement = reimbursementService.submitClaimFinancial(reimbursementDTO);
			
			Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(reimbursementDTO.getClaimKey(), reimbursementDTO.getKey());
			Boolean shouldInvokeAcc = true;
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
					if (shouldInvokeAcc && !reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD) && !reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY)){
							dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursementDTO.getKey());
					}
					
				}
				
				/**
				 * update provision amount procedure needs to be called for cancel rod scenario.
				 * This was implemented as per prakash suggestion.
				 * */
				
				if(!ReferenceTable.getReferToKeys().containsKey(reimbursement.getStatus().getKey()) || (reimbursementDTO.getStatusKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))) {
				//	dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
					dbCalculationService.updateProvisionAmountForPANonHealth(reimbursement.getKey(), reimbursement.getClaim().getKey());
				}
				
				String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
				if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
					try {
						Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(reimbursement.getClaim().getIntimation().getHospital());
						Claim claim = claimService.getClaimByClaimKey(reimbursement.getClaim().getKey());
						String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
						apiService.updateProvisionAmountToPremia(provisionAmtInput);
						
						if(((reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyTerm() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyTerm().equals(2l)) || reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)) && (reimbursement.getStatus() != null && (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)))) {
							if(reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
								TmpCPUCode tmpCPUCode = hospitalService.getTmpCPUCode(hospitalDetailsByKey.getCpuId());
								JSONObject jsonObj = new JSONObject();
								jsonObj.put(PremiaConstants.POLICY_NUMBER, reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
								jsonObj.put(PremiaConstants.INTIMATION_NUMBER, reimbursementDTO.getNewIntimationDTO().getIntimationId());
								jsonObj.put(PremiaConstants.CPU_CODE, tmpCPUCode != null ? String.valueOf(tmpCPUCode.getCpuCode()) : "");
								jsonObj.put(PremiaConstants.ADJUSTMENT_AMOUNT, String.valueOf(reimbursementDTO.getTotalConsolidatedAmt() - reimbursementDTO.getUniqueDeductedAmount()));
								PremiaService.getInstance().updateAdjustmentAmount(jsonObj.toString());
							}
							
						} 
						
						if(reimbursement.getStatus() != null && (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) ))  {
							if(reimbursementService.isAnyRodActive(reimbursement) ){
								PremiaService.getInstance().UnLockPolicy(reimbursement.getClaim().getIntimation().getIntimationId());
							}
						}
						String bedCount = reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails().getInpatientBeds();
						if(bedCount != null) {
							PremiaService.getInstance().updateBedsCount(bedCount, hospitalDetailsByKey.getHospitalCode());
						}
					} catch(Exception e) {
						
					}
				}
				
			}
			
			
			view.buildSuccessLayout();
		} else {
			view.buildFailureLayout();
		}	
		}else{
			view.buildPaymentFailureLayout();
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
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	
	@SuppressWarnings("static-access")
	public void submitRoomRentMapping(
			@Observes @CDIEvent(SUBMIT_CLAIM_FINANCIAL_MAPPING) final ParameterDTO parameters) {
		PreauthDTO reimbursementDTO = (PreauthDTO) parameters.getPrimaryParameter();
		reimbursementService.saveRoomRentNursingMapping(reimbursementDTO, reimbursementDTO.getRoomRentMappingDTOList());
		dbCalculationService.getBillDetailsSummary(reimbursementDTO.getKey());
	}

	public void setUpSubCategoryValues(
			@Observes @CDIEvent(FINANCIAL_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(FINANCIAL_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	

}
