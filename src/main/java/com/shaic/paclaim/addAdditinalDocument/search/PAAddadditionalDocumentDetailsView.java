package com.shaic.paclaim.addAdditinalDocument.search;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claims.reibursement.addaditionaldocuments.AcknowledgementReceiptViewImpl;

public interface PAAddadditionalDocumentDetailsView extends GMVPView, WizardStep<ReceiptOfDocumentsDTO>{

	 void init(ReceiptOfDocumentsDTO bean, GWizard wizard);
		/* void setTableValuesToDTO();
		 void setTableValues();*/
		 void setUpDropDownValues(Map<String,Object> referenceDataMap);
		 void setDocumentCheckListTableValues(List<DocumentCheckListDTO> documentCheckList);
		 void saveReconsiderRequestTableItems(ReconsiderRODRequestTableDTO dto,List<UploadDocumentDTO> uploadDocsDTO);
		 void setDocumentDetailsDTOForValidation(List<DocumentDetailsDTO> documentDetailsDTO);
		 void returnPreviousPage();
		 
		 void updateBean(ReceiptOfDocumentsDTO bean);
		 
		 void setThirdPageInstance(AcknowledgementReceiptViewImpl detailsViewImpl);
	
}
