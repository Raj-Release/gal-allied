package com.shaic.claim.outpatient.processOP.pages.assessmentsheet;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
	
public interface OPClaimAssessmentPageInterface extends GMVPView , WizardStep<OutPatientDTO>{
	void init(OutPatientDTO bean, GWizard wizard);
	void editUploadDocumentDetails(UploadDocumentDTO uploadDTO);
	void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocDTO);
	void deleteUploadDocumentDetails(UploadDocumentDTO uploadDTO);
	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO);
	void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO);
	void setPayableDtls(String payableAt);
}
