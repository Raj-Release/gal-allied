package com.shaic.claim.outpatient.processOP.pages.claimDecision;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
	
public interface OPClaimDecisionPageInterface extends GMVPView , WizardStep<OutPatientDTO>{
	void init(OutPatientDTO bean, GWizard wizard);
//	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);
	void setHospitalDetails(HospitalDto dto);
	
}
