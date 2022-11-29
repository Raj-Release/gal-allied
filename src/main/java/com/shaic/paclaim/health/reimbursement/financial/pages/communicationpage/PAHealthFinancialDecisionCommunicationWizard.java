package com.shaic.paclaim.health.reimbursement.financial.pages.communicationpage;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public interface PAHealthFinancialDecisionCommunicationWizard extends GMVPView, WizardStep<PreauthDTO> {
	
	void init(PreauthDTO bean, GWizard wizard);

}
