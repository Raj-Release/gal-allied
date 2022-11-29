package com.shaic.paclaim.health.reimbursement.financial.pages.billinghospitalization;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerficationAccountDetailsService;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.BankMaster;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(PAHealthFinancialHospitalizationPageWizard.class)
public class PAHealthFinancialHospitalizationPagePresenter extends AbstractMVPPresenter<PAHealthFinancialHospitalizationPageWizard>{
	private static final long serialVersionUID = 7488192193097057694L;
	public static final String FINANCIAL_REFERCOORDINATOR_EVENT = "pa_health_financial_refer_to_coordinator_event";
	public static final String FINANCIAL_REFER_TO_MEDICAL_APPROVER_EVENT = "pa_health_financial_refer_to_medical_approver_event";
	public static final String FIANANCIAL_APPROVE_EVENT = "pa_health_financial_approve_event";
	public static final String FIANANCIAL_QUERY_EVENT = "pa_health_financial_query_event";
	public static final String FINANCIAL_CANCEL_ROD="pa_health_financial_cancel_rod";
	public static final String FIANANCIAL_REJECT_EVENT = "pa_health_financial_reject_event";
	public static final String FIANANCIAL_FVR_EVENT = "pa_health_financial_fvr_event";
	public static final String FIANANCIAL_INVESTIGATION_EVENT = "pa_health_financial_investigation_event";
	public static final String FIANANCIAL_REFER_TO_BILLING_EVENT = "pa_health_financial_refer_to_billing_event";
	public static final String FIANANCIAL_REFER_TO_SPECIALIST_EVENT = "pa_health_financial_refer_to_specialist_event";
	public static final String FA_SETUP_IFSC_DETAILS = "pa_health_ifsc code Search in Financial Approver";
	public static final String FINANCIAL_HOSPITALIZATION_REFER_TO_BILL_ENTRY = "pa_health_financial_hospitalization_refer_to_bill_entry";
	
	public static final String FINANCIAL_POPULATE_PREVIOUS_ACCT_DETAILS = "pa_health_financial_populate_previous_acct_details";
	
	public static final String PA_VERIFIED_ACCOUNT_DETAIL_SAVE ="PA Save Account Verificatin Details";
	
//	public static final String PA_VERIFICATION_ACCOUNT_DETAILS ="PA_Account Verificatin Details";

	@EJB
	private VerficationAccountDetailsService verficationAccountDetailsService;
	
	@EJB
	private DBCalculationService dbService;

	public static final String CHECK_LEGAL_HEIR_DOC_AVAILABLE = "Check Legal Heir Document Available for PA Hospitalisation Death";

	public static final String ADD_BANK_IFSC_DETAILS_PA_FA = "Setup Add Bank IFSC Bank Details PA FA";
	
	public static final String PA_FINANCIAL_HOSP_TDS_PAN_VALIDATION = "pa_billing_hosp_tds_pan_validation";
	
	@EJB
	private MasterService masterService;
	
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
		view.generateApproveLayout();
	}
	
	public void generateQueryLayout(@Observes @CDIEvent(FIANANCIAL_QUERY_EVENT) final ParameterDTO parameters)
	{
		view.generateQueryLayout();
	}
	public void generateCancelRODLayout(@Observes @CDIEvent(FINANCIAL_CANCEL_ROD) final ParameterDTO parameters)
	{
		view.generateCancelRODLayout(masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
	}
	
	public void generateRejectLayout(@Observes @CDIEvent(FIANANCIAL_REJECT_EVENT) final ParameterDTO parameters)
	{
		view.generateRejectLayout();
	}
	
	public void generateFieldVisitLayout(@Observes @CDIEvent(FIANANCIAL_FVR_EVENT) final ParameterDTO parameters)
	{
		view.genertateFieldsBasedOnFieldVisit(masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO),
				masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO),masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY));
	}
	
	public void generateInvestigationLayout(@Observes @CDIEvent(FIANANCIAL_INVESTIGATION_EVENT) final ParameterDTO parameters)
	{
		view.genertateInvestigationLayout(masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION));
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
	
	public void getLegalHeirDocAvailableByIntimation(@Observes @CDIEvent(CHECK_LEGAL_HEIR_DOC_AVAILABLE) final ParameterDTO parameters)
	{
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		boolean available = masterService.getLegalHeirDocAvailableByIntimation(ReferenceTable.HEALTH_LEGAL_HEIR_DOC_KEY, preauthDto.getNewIntimationDTO().getIntimationId());
		
		preauthDto.getClaimDTO().setLegalHeirDocAvailable(available);			
	}
	
	@SuppressWarnings("static-access")
	public void saveVerifiedAccount(
			@Observes @CDIEvent(PA_VERIFIED_ACCOUNT_DETAIL_SAVE) final ParameterDTO parameters) {		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		verficationAccountDetailsService.submitAccountVerificationDetails(preauthDTO.getVerificationAccountDeatilsTableDTO(),SHAConstants.FA_PA_H_VERIFICATION);
		preauthDTO.setVerificationClicked(true);
	}
	
	/*public void setVerifictaionAccountDetails(
			@Observes @CDIEvent(PA_VERIFICATION_ACCOUNT_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String documentReceivedFrom = verficationAccountDetailsService.getDocumentReceivedFrom(preauthDTO.getDocumentReceivedFromId());
		List<VerificationAccountDeatilsTableDTO> verifiedAccountDetails = dbService.getVerifiedAccountDetails(preauthDTO.getPreauthDataExtractionDetails().getAccountNo());
		preauthDTO.setVerificationAccountDeatilsTableDTO(verifiedAccountDetails);
		
	}*/

	public void setupAddBankIFSCDetails(
			@Observes @CDIEvent(ADD_BANK_IFSC_DETAILS_PA_FA) final ParameterDTO parameters) {
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
	
	public void setupPanDetailsMandatory(@Observes @CDIEvent(PA_FINANCIAL_HOSP_TDS_PAN_VALIDATION) final ParameterDTO parameters)
	 {
		 boolean panDetails = (boolean) parameters.getPrimaryParameter();
		 view.setupPanDetailsMandatory(panDetails);
	 }
}
