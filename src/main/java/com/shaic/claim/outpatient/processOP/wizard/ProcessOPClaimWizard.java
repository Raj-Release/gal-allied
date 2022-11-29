package com.shaic.claim.outpatient.processOP.wizard;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.claim.intimation.create.dto.HospitalDto;

/**
 * @author Saravana Kumar P
 *
 */

public interface ProcessOPClaimWizard  extends GMVPView, GWizardListener{
	void buildSuccessLayout();
	void setHospitalDetails(HospitalDto dto);
	void setDiagnosticHospitalDetails(HospitalDto dto);
	void setUpPayableDetails(String payableAt);
	
}
