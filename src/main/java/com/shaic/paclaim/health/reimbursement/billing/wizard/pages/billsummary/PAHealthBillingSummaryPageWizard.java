package com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billsummary;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public interface PAHealthBillingSummaryPageWizard extends GMVPView, WizardStep<PreauthDTO> {
	void buildSuccessLayout();
}
