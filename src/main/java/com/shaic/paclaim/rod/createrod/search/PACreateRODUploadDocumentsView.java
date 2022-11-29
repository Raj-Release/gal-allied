package com.shaic.paclaim.rod.createrod.search;

import java.util.Map;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

public interface PACreateRODUploadDocumentsView extends GMVPView, WizardStep<ReceiptOfDocumentsDTO>{

	
	void setUpDropDownValues(Map<String, Object> referenceDataMap);
//	void setUpDropDownValues(Map<String, Object> referenceDataMap, List<UploadDocumentDTO> uploadDocDTO);
	//void loadUploadedDocsTableValues(List<UploadDocumentDTO> uploadDocList);
	void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocList);
	void loadAlreadyUploadedDocsTable(UploadDocumentDTO uploadDocList);
	void deleteUploadDocumentDetails(UploadDocumentDTO dto);
	void editUploadDocumentDetails(UploadDocumentDTO dto);
	void resetPage();

}
