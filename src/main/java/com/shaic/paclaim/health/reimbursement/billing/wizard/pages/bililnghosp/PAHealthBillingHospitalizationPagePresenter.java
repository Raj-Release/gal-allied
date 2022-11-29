package com.shaic.paclaim.health.reimbursement.billing.wizard.pages.bililnghosp;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.BankMaster;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;

@ViewInterface(PAHealthBillingHospitalizationPageWizard.class)
public class PAHealthBillingHospitalizationPagePresenter extends AbstractMVPPresenter<PAHealthBillingHospitalizationPageWizard>{
	private static final long serialVersionUID = 7488192193097057694L;
	public static final String BILLING_REFERCOORDINATOR_EVENT = "pa_health_billing_refer_to_coordinator_event";
	public static final String BILLING_REFER_TO_MEDICAL_APPROVER_EVENT = "pa_health_billing_refer_to_medical_approver_event";
	public static final String BILLING_SEND_TO_FINANCIAL_EVENT = "pa_health_billing_send_to_financial_event";
	public static final String BILLING_CANCEL_ROD_EVENT="pa_health_billing_cancel_rod_event";
	
	public static final String FA_SETUP_IFSC_DETAILS = "pa_health_ifsc code Search in Billing Screen";
	
	public static final String BILLING_HOSPITALIZATION_REFER_TO_BILL_ENTRY = "pa_health_billing_hospitalization_refer_to_bill_entry";
	
	public static final String BILLING_POPULATE_PREVIOUS_ACCT_DETAILS = "pa_health_billing_populate_previous_acct_details";

	public static final String ADD_BANK_IFSC_DETAILS_PA_BILLING = "setup Add Bank IFSC Details PA Billing";
	
	public static final String PA_BILLING_HOSP_TDS_PAN_VALIDATION = "pa_billing_hosp_tds_pan_validation";

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
		view.generateCancelRODLayout(masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
	}
	
	 public void generateReferToBillEntryLayout(@Observes @CDIEvent(BILLING_HOSPITALIZATION_REFER_TO_BILL_ENTRY) final ParameterDTO parameters){
			view.generateReferToBillEntryLayout();
	 }
	 
	 public void populatePreviousPaymentDetails(@Observes @CDIEvent(BILLING_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
		{
			PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
			view.populatePreviousPaymentDetails(tableDTO);
			
		}

	 public void setupAddBankIFSCDetails(
				@Observes @CDIEvent(ADD_BANK_IFSC_DETAILS_PA_BILLING) final ParameterDTO parameters) {
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

	 public void setupPanDetailsMandatory(@Observes @CDIEvent(PA_BILLING_HOSP_TDS_PAN_VALIDATION) final ParameterDTO parameters)
	 {
		 boolean panDetails = (boolean) parameters.getPrimaryParameter();
		 view.setupPanDetailsMandatory(panDetails);
	 }

}
