package com.shaic.paclaim.health.reimbursement.billing.wizard.pages.bililnghosp;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAHealthBillingHospitalizationPageWizard extends GMVPView, WizardStep<PreauthDTO> {
	void buildSuccessLayout();

	void generateReferCoOrdinatorLayout(BeanItemContainer<SelectValue> selectValueContainer);

	void generateReferToMedicalApproverLayout();

	void generateSendToFinancialLayout();
	
	void generateCancelRODLayout(BeanItemContainer<SelectValue> selectValueContainer);
	
	void init(PreauthDTO bean, GWizard wizard);

	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);

	void generateReferToBillEntryLayout();
	
	void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO);
	
	void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto);
	
	void setupPanDetailsMandatory(Boolean panDetails);
}
