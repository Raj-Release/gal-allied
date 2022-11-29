package com.shaic.reimbursement.draftinvesigation;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;

public interface DraftInvestigatorView extends GMVPView,WizardStep<DraftInvestigatorDto> {
	void init(DraftInvestigatorDto bean, GWizard wizard);

}
