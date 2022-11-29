package com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.billreview;

import java.util.List;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PANonHospFinancialReviewPageWizard extends GMVPView, WizardStep<PreauthDTO>{
	void buildSuccessLayout();
	void init(PreauthDTO bean, GWizard wizard);
	void setMappingData(List<BillItemMapping> mappingData, Boolean isInvokeForOneToOne);
	void setBenefitsData(List<AddOnBenefitsDTO> addOnBenefitsDTO);
	void setCompareWithRODResult(String comparisonResult);
	void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateCancelRODLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void generateReferToMedicalApproverLayout();
	void generateReferToBillEntryLayout();
	void generateQueryLayout();
	void generateApproveLayout();
	void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void genertateSentToReplyLayout();
	void generateReferToBillingLayout();
	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);
	void setUpNomineeIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);
	void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);
	void genertateInvestigationLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void genertateClaimAprlayout();
	void generatePaymentLayout();
	void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> selectValueContainer2,
			BeanItemContainer<SelectValue> selectValueContainer3);
	
	void setupPanDetailsMandatory(Boolean panDetails);
	//R1022
	void populateApproveAmntOPT(List<OptionalCoversDTO> optListFinancialApproval);
}
