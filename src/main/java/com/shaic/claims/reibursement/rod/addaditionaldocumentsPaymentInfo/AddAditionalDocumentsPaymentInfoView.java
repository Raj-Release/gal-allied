package com.shaic.claims.reibursement.rod.addaditionaldocumentsPaymentInfo;

import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

public interface AddAditionalDocumentsPaymentInfoView extends GMVPView {

	void init();
	void setUpDropDownValues(Map<String, Object> referenceDataMap);
	void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocList);
	void deleteUploadDocumentDetails(UploadDocumentDTO dto);
	void editUploadDocumentDetails(UploadDocumentDTO dto);
	void resetPage();
	void updateBean(ReceiptOfDocumentsDTO bean, Map<String, Object> referenceData);
	void buildSuccessLayout();
	void setUpPayableDetails(String payableAt);
	void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto);
	void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO);

}
