package com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface FinancialHospitalizationPageWizard extends GMVPView, WizardStep<PreauthDTO> {
	
	void init(PreauthDTO bean, GWizard wizard);
	
	void buildSuccessLayout();

	void generateReferCoOrdinatorLayout(BeanItemContainer<SelectValue> selectValueContainer);

	void generateReferToMedicalApproverLayout();

	void generateSendToFinancialLayout();

	void generateApproveLayout();

	void generateQueryLayout();
	
	void generateCancelRODLayout(BeanItemContainer<SelectValue> selectValueContainer);

	void generateRejectLayout(BeanItemContainer<SelectValue> selectValueContainer);

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
	
	void alertMessageForInvestigation();
	
	void setUpPayableDetails(String payableAt);

	void alertMsgForCancelRod(String screenName);
	void confirmationForInvestigation();
	void alertMsgForInvsPending(String decisionStatus);

	void setBalanceSIforRechargedProcess(Double balanceSI);
	
	void setTotalConsolidateAmtAfterHospitalDiscnt(PreauthDTO bean);
	
	void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer);
	
	void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto);
	
	void setFaAppAmountBSIAlert();

	void setupPanDetailsMandatory(Boolean panDetails);
	
	void generateHoldLayout();
	
}
