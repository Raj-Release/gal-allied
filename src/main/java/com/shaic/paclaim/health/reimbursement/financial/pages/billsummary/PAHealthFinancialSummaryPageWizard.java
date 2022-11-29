package com.shaic.paclaim.health.reimbursement.financial.pages.billsummary;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public interface PAHealthFinancialSummaryPageWizard extends GMVPView, WizardStep<PreauthDTO> {
	void buildSuccessLayout();
}
