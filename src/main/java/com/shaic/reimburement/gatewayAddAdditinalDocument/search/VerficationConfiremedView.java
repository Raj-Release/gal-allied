package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

public interface VerficationConfiremedView extends GMVPView, WizardStep<ReceiptOfDocumentsDTO>{
		
	void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO);

}
