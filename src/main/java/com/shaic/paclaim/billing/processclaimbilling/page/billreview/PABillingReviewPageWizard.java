package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.util.List;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;

public interface PABillingReviewPageWizard extends GMVPView, WizardStep<PreauthDTO>{
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
	void generateSendToClaimApprovalLayout();
	void generateSendToFALayout();
	void generateAddOnCoverValue(AddOnCoversTableDTO setAddonDtoforCoverId,ComboBox cmbBox);
//	void setUpNomineeIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);	
	void populateApproveAmntOPT(List<OptionalCoversDTO> optListBilling);
}
