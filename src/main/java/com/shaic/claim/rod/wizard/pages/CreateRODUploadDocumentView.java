package com.shaic.claim.rod.wizard.pages;

import java.util.Map;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

/**
 * @author ntv.vijayar
 *
 */

public interface CreateRODUploadDocumentView  extends GMVPView, WizardStep<ReceiptOfDocumentsDTO> {
	
	
	void setUpDropDownValues(Map<String, Object> referenceDataMap);
//	void setUpDropDownValues(Map<String, Object> referenceDataMap, List<UploadDocumentDTO> uploadDocDTO);
	//void loadUploadedDocsTableValues(List<UploadDocumentDTO> uploadDocList);
	void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocList);
	void loadAlreadyUploadedDocsTable(UploadDocumentDTO uploadDocList);
	void deleteUploadDocumentDetails(UploadDocumentDTO dto);
	void editUploadDocumentDetails(UploadDocumentDTO dto);
	void resetPage();
	void setClearReferenceData();
}
