package com.shaic.paclaim.financial.claimapproval.nonhosiptalpagereview;

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

public interface PAClaimAprNonHosReviewPageWizard extends GMVPView, WizardStep<PreauthDTO>{
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
	void generateReferToBillingLayout();
	void generateQueryLayout();
	void generateApproveLayout();
	void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void genertateSentToReplyLayout();
	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);
	void generatePaymentQueryLayout();
	void genertateInvestigationLayout(
			BeanItemContainer<SelectValue> selectValueContainer);
	void genertateFieldsBasedOnFieldVisit(
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> selectValueContainer2,
			BeanItemContainer<SelectValue> selectValueContainer3);
	
	void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto);
	void setupPanDetailsMandatory(Boolean panDetails);
	//R1022
	void populateApproveAmntOPT(List<OptionalCoversDTO> optListClaimApproval);
}
