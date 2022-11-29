package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;
import java.util.Map;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

/**
 * @author Panneerselvam
 *
 */

public interface OMPAckDocUploadDocumentsView  extends GMVPView, WizardStep<OMPClaimCalculationViewTableDTO> {
	
	
	void setUpDropDownValues(Map<String, Object> referenceDataMap);
	void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocList);
	void deleteUploadDocumentDetails(UploadDocumentDTO dto);
	void editUploadDocumentDetails(UploadDocumentDTO dto);
	void resetPage();
}

