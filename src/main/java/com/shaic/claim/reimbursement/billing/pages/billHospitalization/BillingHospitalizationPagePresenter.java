package com.shaic.claim.reimbursement.billing.pages.billHospitalization;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.BankMaster;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(BillingHospitalizationPageWizard.class)
public class BillingHospitalizationPagePresenter extends AbstractMVPPresenter<BillingHospitalizationPageWizard>{
	private static final long serialVersionUID = 7488192193097057694L;
	public static final String BILLING_REFERCOORDINATOR_EVENT = "billing_refer_to_coordinator_event";
	public static final String BILLING_REFER_TO_MEDICAL_APPROVER_EVENT = "billing_refer_to_medical_approver_event";
	public static final String BILLING_SEND_TO_FINANCIAL_EVENT = "billing_send_to_financial_event";
	public static final String BILLING_CANCEL_ROD_EVENT="billing_cancel_rod_event";

	public static final String FA_SETUP_IFSC_DETAILS = "ifsc code Search in Billing Screen";

	public static final String BILLING_HOSPITALIZATION_REFER_TO_BILL_ENTRY = "billing_hospitalization_refer_to_bill_entry";

	public static final String BILLING_POPULATE_PREVIOUS_ACCT_DETAILS = "billing_populate_previous_acct_details";

	public static final String BILLING_APPROVED_AMOUNT_VALIDATION = "approved amount validation for billing";


	public static final String SETUP_BILLING_PAYABLE_DETAILS  = "setup_payable_details_billing";

	public static final String RECHARGE_SI_FOR_PRODUCT = "recharging SI for product in Process claim billing hospital page";

	public static final String ADD_BANK_IFSC_DETAILS_BILLING = "setup Add Bank IFSC Details Billing";

	public static final String BILLING_TDS_PAN_VALIDATION = "billing_tds_pan_validation";
	
	public static final String BILLING_HOLD_EVENT = "billing_hold_event";

	@EJB
	private MasterService masterService;

	@EJB
	private ReimbursementService reimbService;

	@EJB
	private AcknowledgementDocumentsReceivedService ackDocRecvdService;

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

	public void generateReferCoOrdinatorLayout(@Observes @CDIEvent(BILLING_REFERCOORDINATOR_EVENT) final ParameterDTO parameters)
	{
		view.generateReferCoOrdinatorLayout(masterService.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
	}

	public void generateReferToMedicalApproverLayout(@Observes @CDIEvent(BILLING_REFER_TO_MEDICAL_APPROVER_EVENT) final ParameterDTO parameters)
	{
		view.generateReferToMedicalApproverLayout();
	}

	public void generateSendToFinancialLayout(@Observes @CDIEvent(BILLING_SEND_TO_FINANCIAL_EVENT) final ParameterDTO parameters)
	{
		view.generateSendToFinancialLayout();
	}
	public void generateCancelRODLayout(@Observes @CDIEvent(BILLING_CANCEL_ROD_EVENT) final ParameterDTO parameters)
	{
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		reimbService.isFvrOrInvInitiatedinZMR(preauthDTO);
		if(null != preauthDTO.getProceedWithoutReport() && (SHAConstants.YES_FLAG.equalsIgnoreCase(preauthDTO.getProceedWithoutReport()))){

			view.alertMsgForCancelRod();
		}
		else
		{
			view.generateCancelRODLayout(masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		}
	}

	public void generateReferToBillEntryLayout(@Observes @CDIEvent(BILLING_HOSPITALIZATION_REFER_TO_BILL_ENTRY) final ParameterDTO parameters){
		view.generateReferToBillEntryLayout();
	}

	public void populatePreviousPaymentDetails(@Observes @CDIEvent(BILLING_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
	{
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		view.populatePreviousPaymentDetails(tableDTO);

	}

	public void setupPayableDetails(
			@Observes @CDIEvent(SETUP_BILLING_PAYABLE_DETAILS) final ParameterDTO parameters) {
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
			if(preauthDTO.getIsModernSublimitSelected()){
				balanceSI = dbCalculationService.getBalanceSIForModernSublimit(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(),preauthDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			}else{
				/*balanceSI = dbCalculationService.getBalanceSI(
=======
	
	 public void generateReferToBillEntryLayout(@Observes @CDIEvent(BILLING_HOSPITALIZATION_REFER_TO_BILL_ENTRY) final ParameterDTO parameters){
			view.generateReferToBillEntryLayout();
	 }
	 
	 public void populatePreviousPaymentDetails(@Observes @CDIEvent(BILLING_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
		{
			PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
			view.populatePreviousPaymentDetails(tableDTO);
			
		}
	 
	 public void setupPayableDetails(
				@Observes @CDIEvent(SETUP_BILLING_PAYABLE_DETAILS) final ParameterDTO parameters) {
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
						}else{
						/*balanceSI = dbCalculationService.getBalanceSI(
>>>>>>> e3a076027cdf2c5f48223d2739f0e268fcdbf910
								policyKey,insuredKey, preauthDTO.getClaimDTO().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured(),preauthDTO.getNewIntimationDTO().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);*/
				balanceSI = dbCalculationService.getBalanceSIForReimbursement(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(),preauthDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			}
		}

		//					Double balanceSI = 500000d;

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

					balanceSI = balanceSI+alreadyPaidAmt;

				}
			}
		}
		view.setBalanceSIforRechargedProcess(balanceSI);
	}

	public void setupAddBankIFSCDetails(
			@Observes @CDIEvent(ADD_BANK_IFSC_DETAILS_BILLING) final ParameterDTO parameters) {
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
	
	public void setupPanDetailsMandatory(@Observes @CDIEvent(BILLING_TDS_PAN_VALIDATION) final ParameterDTO parameters)
	{
		boolean panDetails = (boolean) parameters.getPrimaryParameter();
		view.setupPanDetailsMandatory(panDetails);
	}

	public void generateBillingHoldLayout(@Observes @CDIEvent(BILLING_HOLD_EVENT) final ParameterDTO parameters)
	{
		view.generateHoldLayout();
	}
}
