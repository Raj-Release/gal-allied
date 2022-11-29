package com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.BankMaster;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(FinancialHospitalizationPageWizard.class)
public class FinancialHospitalizationPagePresenter extends AbstractMVPPresenter<FinancialHospitalizationPageWizard>{
	private static final long serialVersionUID = 7488192193097057694L;
	public static final String FINANCIAL_REFERCOORDINATOR_EVENT = "financial_refer_to_coordinator_event";
	public static final String FINANCIAL_REFER_TO_MEDICAL_APPROVER_EVENT = "financial_refer_to_medical_approver_event";
	public static final String FIANANCIAL_APPROVE_EVENT = "financial_approve_event";
	public static final String FIANANCIAL_QUERY_EVENT = "financial_query_event";
	public static final String FINANCIAL_CANCEL_ROD="financial_cancel_rod";
	public static final String FIANANCIAL_REJECT_EVENT = "financial_reject_event";
	public static final String FIANANCIAL_FVR_EVENT = "financial_fvr_event";
	public static final String FIANANCIAL_INVESTIGATION_EVENT = "financial_investigation_event";
	public static final String FIANANCIAL_REFER_TO_BILLING_EVENT = "financial_refer_to_billing_event";
	public static final String FIANANCIAL_REFER_TO_SPECIALIST_EVENT = "financial_refer_to_specialist_event";
	public static final String FA_SETUP_IFSC_DETAILS = "ifsc code Search in Financial Approver";
	public static final String FINANCIAL_HOSPITALIZATION_REFER_TO_BILL_ENTRY = "financial_hospitalization_refer_to_bill_entry";
	
	public static final String FINANCIAL_POPULATE_PREVIOUS_ACCT_DETAILS = "financial_populate_previous_acct_details";
	
	public static final String SETUP_FA_PAYABLE_DETAILS  = "setup_payable_details_fa";
	
	public static final String RECHARGE_SI_FOR_PRODUCT = "recharging SI for product in Process claim financial hospital page";
	
	public static final String CONSOLIDATE_AMT_AFTER_HOSP_NETWORK_DISCNT = "financial_consolidate_amt_after_network_discnt";
	
//	public static final String VERIFICATION_ACCOUNT_DETAILS ="Account Verificatin Details";
	
	public static final String VERIFIED_ACCOUNT_DETAIL_SAVE ="Save Account Verificatin Details";

	public static final String REJECT_SUB_CATEG_LAYOUT_FA_MEDICAL_DECISION = "Rejection Subcategory Layout for FA Medical Decision Page";

	public static final String VERIFY_LEGAL_HEIR_DOC_UPLOADED = "Verify Legal Heir Document uploaded for Death of Proposer";
	
	public static final String ADD_BANK_IFSC_DETAILS_FA = "setup Add IFSC Bank Details FA";
	
	public static final String FINANCIAL_APPROVE_BSI_ALERT = "financial_approve_bsi_alert";
	
	public static final String FINANCIAL_TDS_PAN_VALIDATION = "billing_tds_pan_validation";
	public static final String BILLING_HOLD_EVENT = "financial hold event";
	
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocRecvdService;
	
	@EJB
	private InvestigationService investigationService;

	@EJB
	private FieldVisitRequestService fvrService;
	
	@EJB
	private DBCalculationService dbService;
	
	@EJB
	private ReimbursementService reimbService;
	
	@EJB
	private VerficationAccountDetailsService verficationAccountDetailsService;
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setupIFSCDetails(
			@Observes @CDIEvent(FA_SETUP_IFSC_DETAILS) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		
		BankMaster masBank = masterService.getBankDetails(viewSearchCriteriaDTO.getIfscCode());
		
		if(masBank != null) {
			viewSearchCriteriaDTO.setBankId(masBank.getKey());
			viewSearchCriteriaDTO.setBankName(masBank.getBankName());
			viewSearchCriteriaDTO.setBranchName(masBank.getBranchName());
			viewSearchCriteriaDTO.setCity(masBank.getCity());
		}
		
		view.setUpIFSCDetails(viewSearchCriteriaDTO);
	}
	
	public void generateReferCoOrdinatorLayout(@Observes @CDIEvent(FINANCIAL_REFERCOORDINATOR_EVENT) final ParameterDTO parameters)
	{
		view.generateReferCoOrdinatorLayout(masterService.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
	}
	
	public void generateReferToMedicalApproverLayout(@Observes @CDIEvent(FINANCIAL_REFER_TO_MEDICAL_APPROVER_EVENT) final ParameterDTO parameters)
	{
		view.generateReferToMedicalApproverLayout();
	}
	
	public void generateApproverLayout(@Observes @CDIEvent(FIANANCIAL_APPROVE_EVENT) final ParameterDTO parameters)
	{
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isInvsAvailable = investigationService.getInvestigationAvailableForClaim(preauthDTO.getClaimDTO().getKey());
		if(null != isInvsAvailable && isInvsAvailable){
			view.alertMsgForInvsPending(SHAConstants.APPROVAL);
		}
		else{
			view.generateApproveLayout();
		}
	}
	
	public void generateQueryLayout(@Observes @CDIEvent(FIANANCIAL_QUERY_EVENT) final ParameterDTO parameters)
	{
		view.generateQueryLayout();
	}
	public void generateCancelRODLayout(@Observes @CDIEvent(FINANCIAL_CANCEL_ROD) final ParameterDTO parameters)
	{
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		reimbService.isFvrOrInvInitiatedinZMR(preauthDTO);
		reimbService.isInvPendingInFA(preauthDTO);
		if(null != preauthDTO.getProceedWithoutReport() && (SHAConstants.YES_FLAG.equalsIgnoreCase(preauthDTO.getProceedWithoutReport()))){
			
			view.alertMsgForCancelRod(SHAConstants.MEDICAL);
		}
		if(null != preauthDTO.getIsInvsPendingInFA() && preauthDTO.getIsInvsPendingInFA()){
			view.alertMsgForCancelRod(SHAConstants.FINANCIAL);
		}
		else
		{
			view.generateCancelRODLayout(masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		}
		
	}
	
	public void generateRejectLayout(@Observes @CDIEvent(FIANANCIAL_REJECT_EVENT) final ParameterDTO parameters)
	{
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isInvsAvailable = investigationService.getInvestigationAvailableForClaim(preauthDTO.getClaimDTO().getKey());
		if(null != isInvsAvailable && isInvsAvailable){
			view.alertMsgForInvsPending(SHAConstants.REJECT);
		}
		else{
			if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)
							|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))){
				if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)){
					view.generateRejectLayout(masterService.getReimbRejCategoryForCoronaFmlProd(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() ,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan())); 
				}else if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)){
					view.generateRejectLayout(masterService.getReimbRejCategoryForCoronaFmlProd(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan())); 
				}
			}else{
			view.generateRejectLayout(masterService.getReimbRejCategoryByValue());   //masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
			}
		}
	}
	
	public void generateFieldVisitLayout(@Observes @CDIEvent(FIANANCIAL_FVR_EVENT) final ParameterDTO parameters)
	{
		
		PreauthDTO bean = (PreauthDTO)parameters.getPrimaryParameter();
		
		FieldVisitRequest fvrobjByRodKey = fvrService.getPendingFieldVisitByClaimKey(bean.getClaimDTO().getKey());
		
		if((fvrobjByRodKey  == null || (fvrobjByRodKey != null && fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR.equals(fvrobjByRodKey.getStatus().getKey())))){
		
			if(fvrobjByRodKey  != null ){
				dbService.invokeProcedureAutoSkipFVR(fvrobjByRodKey.getFvrId());
				fvrService.autoSkipFirstFVR(fvrobjByRodKey);
			}

			view.genertateFieldsBasedOnFieldVisit(masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO),
				/*masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO)*/null,masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY));
		}
	}
	
	public void generateInvestigationLayout(@Observes @CDIEvent(FIANANCIAL_INVESTIGATION_EVENT) final ParameterDTO parameters)
	{
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
			if(!preauthDTO.getIsInvsRaised()){
				view.genertateInvestigationLayout(masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION));
			}
			else{
				view.confirmationForInvestigation();
			}
		}
		else
		{
			view.alertMessageForInvestigation();
		}
		
	}
	
	public void generateSpecialistLayout(@Observes @CDIEvent(FIANANCIAL_REFER_TO_SPECIALIST_EVENT) final ParameterDTO parameters)
	{
		view.genertateSpecialistLayout(masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE)));
	}
	
	public void generateReferToBillingLayout(@Observes @CDIEvent(FIANANCIAL_REFER_TO_BILLING_EVENT) final ParameterDTO parameters)
	{
		view.genertateReferToBillingLayout();
	}
	
	public void generateReferToBillEntryLayout(@Observes @CDIEvent(FINANCIAL_HOSPITALIZATION_REFER_TO_BILL_ENTRY) final ParameterDTO parameters){
		view.generateReferToBillEntryLayout();
 }
	
	public void populatePreviousPaymentDetails(@Observes @CDIEvent(FINANCIAL_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
	{
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		view.populatePreviousPaymentDetails(tableDTO);
		
	}
	
	public void setupPayableDetails(
			@Observes @CDIEvent(SETUP_FA_PAYABLE_DETAILS) final ParameterDTO parameters) {
		String payableName = (String) parameters.getPrimaryParameter();
		view.setUpPayableDetails(payableName);
	}
	
	public void rechargingSIEvent(
			@Observes @CDIEvent(RECHARGE_SI_FOR_PRODUCT) final ParameterDTO parameters) {
				PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
				
				Long policyKey = preauthDTO.getNewIntimationDTO().getPolicy().getKey();
				Long insuredKey = preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey();
				DBCalculationService dbCalculationService = new DBCalculationService();
				dbCalculationService.rechargingSIbasedOnProduct(policyKey, insuredKey);
				
				Double balanceSI = 0d;
				
				if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)){
					balanceSI = dbCalculationService.getBalanceSIForGMC(
							policyKey,insuredKey, preauthDTO.getClaimDTO().getKey());
				}else{
					//code added for cr modern sublimit SI by noufel
					if(preauthDTO.getIsBalSIForSublimitCardicSelected()){
						balanceSI = dbCalculationService.getBalanceSIForCardicCarePlatRemb(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(),preauthDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
					}
					else if(preauthDTO.getIsModernSublimitSelected()){
						balanceSI = dbCalculationService.getBalanceSIForModernSublimit(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(),preauthDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
					}
					else{
						balanceSI = dbCalculationService.getBalanceSIForReimbursement(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(),preauthDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
					}
				}
				
//				Double balanceSI = 500000d;
				
				balanceSI = SHAUtils.getHospOrPartialAppAmt(preauthDTO, reimbService, balanceSI);
				
				Reimbursement reimbursementByKey = ackDocRecvdService.getReimbursementByKey(preauthDTO.getKey());
				
				if(reimbursementByKey != null && reimbursementByKey.getDocAcknowLedgement() != null){
					Boolean isSettled = reimbService.isSettledPaymentAvailable(reimbursementByKey.getRodNumber());
					DocAcknowledgement docAcknowLedgement = reimbursementByKey.getDocAcknowLedgement();
					if(isSettled){
						if(reimbursementByKey.getReconsiderationRequest() != null && reimbursementByKey.getReconsiderationRequest().equalsIgnoreCase("Y") 
								&& docAcknowLedgement.getPaymentCancellationFlag() != null && docAcknowLedgement.getPaymentCancellationFlag().equalsIgnoreCase("N")){
							Double alreadyPaidAmt = 0d;
							List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = ackDocRecvdService.getReimbursementCalculationDetails(preauthDTO.getKey());
							for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
								if(docAcknowLedgement.getDocumentReceivedFromId() != null && docAcknowLedgement.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
									if(reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() !=null && reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() > 0){
										alreadyPaidAmt += reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() !=null ? reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() :0;
									} else {
										alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableToHospAftTDS() !=null ? reimbursementCalCulationDetails2.getPayableToHospAftTDS() :0;
									}
								} else {
									if(reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() != null){
										if(reimbursementCalCulationDetails2.getBillClassificationId() != null && ! reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)
												&& ! reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
											alreadyPaidAmt += reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt();
										}
									}
									/*if(reimbursementCalCulationDetails2.getPayableToHospital() != null){
										alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableToHospital();
									}*/
								}
							}
							
							balanceSI = balanceSI+alreadyPaidAmt;
							
						}
					}
				}
				view.setBalanceSIforRechargedProcess(balanceSI);
				
		}
	
	public void setTotalConsolidateAmtAfterHospitalDiscnt(
			@Observes @CDIEvent(CONSOLIDATE_AMT_AFTER_HOSP_NETWORK_DISCNT) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		view.setTotalConsolidateAmtAfterHospitalDiscnt(preauthDTO);
	}

	/*public void setVerifictaionAccountDetails(
			@Observes @CDIEvent(VERIFICATION_ACCOUNT_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String documentReceivedFrom = verficationAccountDetailsService.getDocumentReceivedFrom(preauthDTO.getDocumentReceivedFromId());
		List<VerificationAccountDeatilsTableDTO> verifiedAccountDetails = dbService.getVerifiedAccountDetails(preauthDTO.getPreauthDataExtractionDetails().getAccountNo());
		preauthDTO.setVerificationAccountDeatilsTableDTO(verifiedAccountDetails);
		
	}*/
	
	

	@SuppressWarnings("static-access")
	public void saveVerifiedAccount(
			@Observes @CDIEvent(VERIFIED_ACCOUNT_DETAIL_SAVE) final ParameterDTO parameters) {		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		verficationAccountDetailsService.submitAccountVerificationDetails(preauthDTO.getVerificationAccountDeatilsTableDTO(),SHAConstants.FA_VERIFICATION);
		preauthDTO.setVerificationClicked(true);
	}
	
	public void generateRejSubCategoryLayoutForFA(
			@Observes @CDIEvent(REJECT_SUB_CATEG_LAYOUT_FA_MEDICAL_DECISION) final ParameterDTO parameters) {

		Long rejCategId = (Long) parameters.getPrimaryParameter();
		
		BeanItemContainer<SelectValue> rejSubcategContainer = masterService.getRejSubcategContainer(rejCategId);
		
		view.setSubCategContainer(rejSubcategContainer);
	}

	public void getLegalHeirDocAvailable(
			@Observes @CDIEvent(VERIFY_LEGAL_HEIR_DOC_UPLOADED) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		boolean available = masterService.getLegalHeirDocAvailableByIntimation(ReferenceTable.HEALTH_LEGAL_HEIR_DOC_KEY, preauthDTO.getNewIntimationDTO().getIntimationId());
		
		preauthDTO.getClaimDTO().setLegalHeirDocAvailable(available);
	}
	
	public void setupAddBankIFSCDetails(
			@Observes @CDIEvent(ADD_BANK_IFSC_DETAILS_FA) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		
		BankMaster masBank = masterService.getBankDetails(viewSearchCriteriaDTO.getIfscCode());
		
		if(masBank != null) {
			viewSearchCriteriaDTO.setBankId(masBank.getKey());
			viewSearchCriteriaDTO.setBankName(masBank.getBankName());
			viewSearchCriteriaDTO.setBranchName(masBank.getBranchName());
			viewSearchCriteriaDTO.setCity(masBank.getCity());
		}
		
		view.setUpAddBankIFSCDetails(viewSearchCriteriaDTO);
	}
	
	public void setAppAmountBSIAlert(@Observes @CDIEvent(FINANCIAL_APPROVE_BSI_ALERT) final ParameterDTO parameters) {
		view.setFaAppAmountBSIAlert();
	}
	
	public void setupPanDetailsMandatory(@Observes @CDIEvent(FINANCIAL_TDS_PAN_VALIDATION) final ParameterDTO parameters)
	 {
		 boolean panDetails = (boolean) parameters.getPrimaryParameter();
		 view.setupPanDetailsMandatory(panDetails);
	 }
	
	public void generateBillingFAHoldLayout(@Observes @CDIEvent(BILLING_HOLD_EVENT) final ParameterDTO parameters)
	{
		view.generateHoldLayout();
	}
}
