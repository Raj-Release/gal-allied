package com.shaic.paclaim.health.reimbursement.financial.pages.billinghospitalization;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAHealthFinancialHospitalizationPageWizard extends GMVPView, WizardStep<PreauthDTO> {
	
	void init(PreauthDTO bean, GWizard wizard);
	
	void buildSuccessLayout();

	void generateReferCoOrdinatorLayout(BeanItemContainer<SelectValue> selectValueContainer);

	void generateReferToMedicalApproverLayout();

	void generateSendToFinancialLayout();

	void generateApproveLayout();

	void generateQueryLayout();
	
	void generateCancelRODLayout(BeanItemContainer<SelectValue> selectValueContainer);

	void generateRejectLayout();

	void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> beanItemContainer, BeanItemContainer<SelectValue> beanItemContainer2);

	void genertateInvestigationLayout(
			BeanItemContainer<SelectValue> selectValueContainer);

	void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist);

	void genertateReferToBillingLayout();

	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);

	void generateReferToBillEntryLayout();
	
	void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO);
	
	void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto);

	void setupPanDetailsMandatory(Boolean panDetails);
}
