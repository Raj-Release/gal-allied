package com.shaic.claim.outpatient.processOPpages.assessmentsheet;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
	
public interface OPBillAssessmentPageInterface extends GMVPView , WizardStep<OutPatientDTO>{
	void init(OutPatientDTO bean, GWizard wizard);
//	void editUploadDocumentDetails(UploadDocumentDTO uploadDTO);
//	void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocDTO);
//	void deleteUploadDocumentDetails(UploadDocumentDTO uploadDTO);
}
