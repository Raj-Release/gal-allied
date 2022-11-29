package com.shaic.paclaim.billing.processclaimbilling.page.billassesmentsheet;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public interface PABillingAssesmentSheetWizard extends GMVPView, WizardStep<PreauthDTO> {
	
	void init(PreauthDTO bean, GWizard wizard);

}
