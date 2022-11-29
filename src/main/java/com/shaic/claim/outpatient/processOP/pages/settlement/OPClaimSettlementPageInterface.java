package com.shaic.claim.outpatient.processOP.pages.settlement;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
	
public interface OPClaimSettlementPageInterface extends GMVPView , WizardStep<OutPatientDTO>{
	void init(OutPatientDTO bean, GWizard wizard);
	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);
	void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO);
}
