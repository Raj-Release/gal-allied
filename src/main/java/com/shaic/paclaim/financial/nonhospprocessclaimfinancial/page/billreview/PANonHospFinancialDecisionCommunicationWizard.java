package com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.billreview;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public interface PANonHospFinancialDecisionCommunicationWizard extends GMVPView, WizardStep<PreauthDTO>{

	void init(PreauthDTO bean, GWizard wizard);
}
