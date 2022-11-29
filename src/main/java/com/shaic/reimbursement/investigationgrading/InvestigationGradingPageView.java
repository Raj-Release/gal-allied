package com.shaic.reimbursement.investigationgrading;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;

public interface InvestigationGradingPageView extends GMVPView,WizardStep<AssignInvestigatorDto>{

	void init(AssignInvestigatorDto bean,GWizard wizard);
	public Boolean validatePage();
}
