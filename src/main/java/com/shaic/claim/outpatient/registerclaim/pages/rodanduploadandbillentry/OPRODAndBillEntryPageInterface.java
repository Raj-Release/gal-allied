package com.shaic.claim.outpatient.registerclaim.pages.rodanduploadandbillentry;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
	
public interface OPRODAndBillEntryPageInterface extends GMVPView , WizardStep<OutPatientDTO>{
	void init(OutPatientDTO bean, GWizard wizard);

	void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocDTO);

	void deleteUploadDocumentDetails(UploadDocumentDTO uploadDTO);

	void editUploadDocumentDetails(UploadDocumentDTO uploadDTO);
	
	void resetView();
}
