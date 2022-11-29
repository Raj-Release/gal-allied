package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

public interface ReceivedPhysicalDocumentDetailsView extends GMVPView, WizardStep<ReceiptOfDocumentsDTO>{

	 void init(ReceiptOfDocumentsDTO bean, GWizard wizard);	
	 void setUpDropDownValues(Map<String,Object> referenceDataMap);	
	 void saveReconsiderRequestTableItems(ReconsiderRODRequestTableDTO dto,List<UploadDocumentDTO> uploadDocsDTO);
	 void setDocumentDetailsDTOForValidation(List<DocumentDetailsDTO> documentDetailsDTO);
	 void returnPreviousPage();
}
