package com.shaic.claim.outpatient.registerclaim.pages.claimanddocumentdetails;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
	
public interface ClaimAndDocumentDetailsPageInterface extends GMVPView , WizardStep<OutPatientDTO>{
	void init(OutPatientDTO bean, GWizard wizard);

	void setSumInsuredValidation(Integer healthCheckupSumInsured);

	public Long getInsuredKey();
}
