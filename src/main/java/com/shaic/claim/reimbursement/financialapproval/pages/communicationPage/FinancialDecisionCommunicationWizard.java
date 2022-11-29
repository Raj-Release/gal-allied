package com.shaic.claim.reimbursement.financialapproval.pages.communicationPage;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billreview.FinancialReviewPageViewImpl;

public interface FinancialDecisionCommunicationWizard extends GMVPView, WizardStep<PreauthDTO> {

	void init(PreauthDTO bean, GWizard wizard,
			FinancialReviewPageViewImpl argBillingReviewPageViewImplObj);

}
