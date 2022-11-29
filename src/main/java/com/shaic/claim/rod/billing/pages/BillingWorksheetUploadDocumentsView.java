/**
 * 
 */
package com.shaic.claim.rod.billing.pages;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.ui.Window;

/**
 * @author ntv.vijayar
 *
 */
public interface BillingWorksheetUploadDocumentsView   extends GMVPView {
	
	void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocList);
	void deleteUploadDocumentDetails(UploadDocumentDTO dto);
	void setUploadedDocsTableValues(List<UploadDocumentDTO> uploadList);
	void init(PreauthDTO bean,Window popup);
	void resetPage();
	//void buildSuccessLayout(Boolean value,Window popup);
	void buildSuccessLayout(String value,Window popup);

}
